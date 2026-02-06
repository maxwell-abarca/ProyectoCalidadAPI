package com.project.demo.rest.rateOrder;

import com.project.demo.logic.entity.order.Order;
import com.project.demo.logic.entity.order.OrderRepository;
import com.project.demo.logic.entity.rateOrder.RateOrder;
import com.project.demo.logic.entity.rateOrder.RateOrderRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import com.project.demo.rest.order.OrderController;
import com.project.demo.rest.userBuyer.UserBuyerRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ratesOrder")
public class RateOrderController {

    @Autowired
    private RateOrderRepository rateOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserBuyerRepository userBuyerRepository;


    @Autowired
    private UserBuyerRestController userBuyerRestController;

    private OrderController orderController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN', 'USER')")
    public List<RateOrder> getAllRatesOrder() {
        return rateOrderRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public RateOrder addRateOrder(@RequestBody RateOrder rateOrder) {

        if (rateOrder.getRate() == null || rateOrder.getOrder().getId() == null) {
            throw new IllegalArgumentException("RateOrder or order cannot be null");
        }

        Long orderId = rateOrder.getOrder().getId();
        Long buyerId = userBuyerRestController.authenticatedUser().getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        UserBuyer userBuyer = userBuyerRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("User Buyer not found with id: " + buyerId));

        // Check if the user has already rated this RateOrder
        Optional<RateOrder> existingRating = rateOrderRepository.findByIdBuyerAndOrderId(buyerId, orderId);

        if (existingRating.isPresent()) {
            throw new IllegalArgumentException("User has already rated this RateOrder.");
        } else {
            rateOrder.setOrder(order);
            rateOrder.setUserBuyer(userBuyer);

            return rateOrderRepository.save(rateOrder);
        }
    }


    private Integer calculateAverageRate(Long orderId) {
        List<RateOrder> rates = rateOrderRepository.findByOrderId(orderId);
        if (rates.isEmpty()) {
            return 0;
        }
        int sum = rates.stream().mapToInt(RateOrder::getRate).sum();
        return sum / rates.size();
    }

    @GetMapping("/{id}")
    public RateOrder getRateOrderById(@PathVariable Long id) {
        return rateOrderRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/rate/{orderId}")
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public Optional<RateOrder> hasRateOrder(@PathVariable Long orderId) {
        Long userId = userBuyerRestController.authenticatedUser().getId();

        return rateOrderRepository.findByIdBuyerAndOrderId(userId,orderId);
    }
}
