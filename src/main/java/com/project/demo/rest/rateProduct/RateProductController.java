package com.project.demo.rest.rateProduct;


import com.project.demo.logic.entity.order.Order;
import com.project.demo.logic.entity.order.OrderRepository;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.rateProduct.RateProduct;
import com.project.demo.logic.entity.rateProduct.RateProductRepository;
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
@RequestMapping("/ratesProduct")
public class RateProductController {
    @Autowired
    private RateProductRepository rateProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserBuyerRepository userBuyerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserBuyerRestController userBuyerRestController;

    @Autowired
    private OrderController orderController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN', 'USER')")
    public List<RateProduct> getAllRatesProduct() {
        return rateProductRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public RateProduct addRateProduct(@RequestBody RateProduct rateProduct) {

        if (rateProduct == null || rateProduct.getRate() == null || rateProduct.getProduct().getId() == null) {
            throw new IllegalArgumentException("RateProduct or users cannot be null");
        }

        Long productId = rateProduct.getProduct().getId();
        Long buyerId = userBuyerRestController.authenticatedUser().getId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        UserBuyer userBuyer = userBuyerRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("User Buyer not found with id: " + buyerId));

        Optional<RateProduct> existingRating = rateProductRepository.findByIdBuyerAndIdProduct(buyerId, productId);

        if (existingRating.isPresent()) {
            throw new IllegalArgumentException("User has already rated this brand.");
        } else {
            List<Order> existingOrder = orderRepository.findByOrderStatus(buyerId, productId);
            if (!existingOrder.isEmpty()) {
                rateProduct.setProduct(product);
                rateProduct.setUserBuyer(userBuyer);

                RateProduct savedRateBrand = rateProductRepository.save(rateProduct);
                Integer averageRating = calculateAverageRate(productId);
                product.setRate(averageRating);
                productRepository.save(product);

                return savedRateBrand;
            }else {
                throw new IllegalArgumentException("The order list is pending.");
            }
        }
    }

    private Integer calculateAverageRate(Long productId) {
        List<RateProduct> rates = rateProductRepository.findByProductId(productId);
        if (rates.isEmpty()) {
            return 0;
        }
        int sum = rates.stream().mapToInt(RateProduct::getRate).sum();
        return sum / rates.size();
    }

    @GetMapping("/{id}")
    public RateProduct getProductById(@PathVariable Long id) {
        return rateProductRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/rate/{productId}")
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public Optional<RateProduct> hasRatedProduct(@PathVariable Long productId) {
        Long userId = userBuyerRestController.authenticatedUser().getId();

        return rateProductRepository.findByIdBuyerAndIdProduct(userId,productId);
    }
}
