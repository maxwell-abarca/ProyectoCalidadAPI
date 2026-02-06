package com.project.demo.rest.order;

import com.project.demo.logic.entity.avatar.Avatar;
import com.project.demo.logic.entity.design.Design;
import com.project.demo.logic.entity.design.DesignRepository;
import com.project.demo.logic.entity.order.Order;
import com.project.demo.logic.entity.order.OrderRepository;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.userBrand.UserBrand;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import com.project.demo.rest.userBuyer.UserBuyerRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserBuyerRepository userBuyerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserBuyerRestController userBuyerRestController;
    @Autowired
    private DesignRepository designRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Order addOrder(@RequestBody Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        return orderRepository.save(order);
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN')")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setQuantity(order.getQuantity());
                    existingOrder.setSubtotal(order.getSubtotal());
                    existingOrder.setShippingCost(order.getShippingCost());
                    existingOrder.setTotal(order.getTotal());
                    existingOrder.setStatus(order.getStatus());
                    existingOrder.setUpdatedAt(order.getUpdatedAt());

                    if (order.getUserBuyer() != null && order.getUserBuyer().getId() != null) {
                        Optional<UserBuyer> userBuyer = userBuyerRepository.findById(order.getUserBuyer().getId());
                        if (userBuyer.isPresent()) {
                            existingOrder.setUserBuyer(userBuyer.get());
                        } else {
                            throw new RuntimeException("UserBuyer not found with id: " + order.getUserBuyer().getId());
                        }
                    }

                    if (order.getDesign() != null && order.getDesign().getId() != null) {
                        Optional<Design> design = designRepository.findById(order.getDesign().getId());
                        if (design.isPresent()) {
                            existingOrder.setDesign(design.get());
                        } else {
                            throw new RuntimeException("Product not found with id: " + order.getDesign().getId());
                        }
                    }

                    return orderRepository.save(existingOrder);
                })
                .orElseGet(() -> {
                    order.setId(id);
                    return orderRepository.save(order);
                });
    }

    @PutMapping("/upStatus/{id}")
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN')")
    public Order updateOrderStatus(@PathVariable Long id, @RequestBody Order updatedOrder) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(updatedOrder.getStatus());
                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @GetMapping("/ByOrderStatus/{id}")
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public List<Order> getByOrderStatus(@PathVariable Long id) {
        Long buyerId = userBuyerRestController.authenticatedUser().getId();
        return orderRepository.findByOrderStatus(buyerId, id);
    }

    @GetMapping("/brand")
    @PreAuthorize("hasAnyRole('USER_BRAND', 'SUPER_ADMIN')")
    public List<Order> getOrdersForBrand() { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserBrand currentUser = (UserBrand) authentication.getPrincipal();
        return orderRepository.findByBrandId(currentUser.getId());
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'SUPER_ADMIN')")
    public List<Order> getOrdersForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserBuyer currentUser = (UserBuyer) authentication.getPrincipal();
        return orderRepository.findByUserId(currentUser.getId());
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
