package com.project.demo.rest.cart;

import com.project.demo.logic.entity.cart.Cart;
import com.project.demo.logic.entity.cart.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/user/{id}")
    public List<Cart> getCartsByUserId(@PathVariable Long id) {
        return this.cartRepository.findByUserId(id);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Cart addCart(@RequestBody Cart cart) {
        return cartRepository.save(cart);
    }

    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Long id) {
        return cartRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PutMapping("/user/{id}")
    public Cart updateCart(@PathVariable Long id, @RequestBody Cart cart) {
        return cartRepository.findById(id)
                .map(existingCart -> {
                    existingCart.setQuantity(cart.getQuantity());
                    return cartRepository.save(existingCart);
                })
                .orElseGet(() -> {
                    cart.setId(id);
                    return cartRepository.save(cart);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteCart(@PathVariable Long id) {
        cartRepository.deleteById(id);
    }
}
