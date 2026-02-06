package com.project.demo.logic.entity.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.project.demo.logic.entity.design.Design;
import com.project.demo.logic.entity.design.DesignRepository;
import com.project.demo.logic.entity.notification.Notification;
import com.project.demo.logic.entity.notification.NotificationHandler;
import com.project.demo.logic.entity.notification.NotificationRepository;
import com.project.demo.logic.entity.notificationTemplate.NotificationTemplate;
import com.project.demo.logic.entity.notificationTemplate.NotificationTemplateRepository;
import com.project.demo.logic.entity.order.Order;
import com.project.demo.logic.entity.order.OrderRepository;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaypalService {

    @Autowired
    private APIContext apiContext;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DesignRepository designRepository;
    @Autowired
    private UserBuyerRepository userBuyerRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private NotificationHandler notificationHandler;


    public Payment createPayment(List<ItemDto> items, String baseUrl, Long userId, String currency) throws PayPalRESTException {
        double subtotal = 0;

        ItemList itemList = new ItemList();
        List<Item> itemListItems = new ArrayList<>();

        for (ItemDto itemDto : items) {
            subtotal += itemDto.getPrice() * itemDto.getQuantity();
            Item item = new Item();
            item.setName(itemDto.getName());
            item.setCurrency(currency);
            item.setPrice(String.format(Locale.US, "%.2f", itemDto.getPrice()));
            item.setQuantity(String.valueOf(itemDto.getQuantity()));
            itemListItems.add(item);
        }

        itemList.setItems(itemListItems);

        double shipping = 0;
        double tax = 0;

        Amount amount = new Amount();
        amount.setCurrency(currency);
        Details details = new Details();
        details.setShipping(String.format(Locale.US, "%.2f", shipping));
        details.setTax(String.format(Locale.US, "%.2f", tax));
        details.setSubtotal(String.format(Locale.US, "%.2f", subtotal));
        amount.setDetails(details);
        amount.setTotal(String.format(Locale.US, "%.2f", (shipping + tax + subtotal)));

        Transaction transaction = new Transaction();
        transaction.setDescription("Shopping Cart purchase");
        transaction.setItemList(itemList);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(baseUrl + "/");
        redirectUrls.setReturnUrl(baseUrl + "/executePayment");

        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        UserBuyer userBuyer = userBuyerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("UserBuyer not found"));

        createOrder(items, userBuyer, subtotal, shipping, tax, shipping + tax + subtotal, "Pendiente", userBuyer.getDeliveryLocation());

        return createdPayment;
    }

    private void createOrder(List<ItemDto> items, UserBuyer userBuyer, double subtotal, double shipping, double tax, double total, String status, String deliveryLocation) {
        for (ItemDto itemDto : items) {
            Order order = new Order();
            order.setUserBuyer(userBuyer);
            order.setDesign(getDesignFromItemDto(itemDto));
            order.setQuantity(itemDto.getQuantity());
            order.setSubtotal(subtotal);
            order.setShippingCost(shipping);
            order.setTotal(total);
            order.setStatus(status);
            order.setDeliveryLocation(deliveryLocation);
            orderRepository.save(order);

            Notification buyerNotification = new Notification();
            buyerNotification.setUser(userBuyer);
            buyerNotification.setSeen(false);
            Optional<NotificationTemplate> buyerNotificationTemplateOptional = this.notificationTemplateRepository.findById(1L);
            buyerNotification.setNotificationTemplate(buyerNotificationTemplateOptional.get());
            notificationRepository.save(buyerNotification);
            notificationHandler.broadcastNotification(buyerNotification);


            Notification brandNotification = new Notification();
            brandNotification.setUser(getProductFromItemDto(itemDto).getUserBrand());
            brandNotification.setSeen(false);
            Optional<NotificationTemplate> brandNotificationTemplateOptional = this.notificationTemplateRepository.findById(5L);
            brandNotification.setNotificationTemplate(brandNotificationTemplateOptional.get());
            notificationRepository.save(brandNotification);
            notificationHandler.broadcastNotification(brandNotification);
        }
    }

    private Design getDesignFromItemDto(ItemDto itemDto) {
        return designRepository.findById(itemDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private Product getProductFromItemDto(ItemDto itemDto) {
        return productRepository.findByName(itemDto.getName())
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

}
