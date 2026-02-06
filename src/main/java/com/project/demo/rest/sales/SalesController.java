package com.project.demo.rest.sales;

import com.project.demo.logic.entity.order.OrderRepository;
import com.project.demo.logic.entity.userBrand.UserBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/most-sold-products")
    @PreAuthorize("hasAnyRole('USER_BRAND')")
    public List<Object[]> getSoldProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserBrand currentUser = (UserBrand) authentication.getPrincipal();
        Long userBrandId = currentUser.getId();
        try {
            return orderRepository.getMostSoldProducts(userBrandId);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching most sold products", e);
        }
    }

    @GetMapping("/total-earnings")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<Object[]> getTotalEarnings() {
        try {
            return orderRepository.getTotalEarningsByBrand();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching total earnings", e);
        }
    }
}
