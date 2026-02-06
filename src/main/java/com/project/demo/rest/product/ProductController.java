package com.project.demo.rest.product;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.userBrand.UserBrand;
import com.project.demo.rest.userBrand.UserBrandRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserBrandRestController userBrandRestController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN', 'USER')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/brands")
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN', 'USER')")
    public List<Product> getAll() {
        UserBrand userBrand = userBrandRestController.authenticatedUser();
        Long brandId = userBrand.getId();
        return productRepository.findProductsByUserBrandId(brandId);
    }

    @GetMapping("/brand/{id}")
    @PreAuthorize("hasAnyRole('USER_BRAND', 'SUPER_ADMIN', 'USER')")
    public List<Product> getByBrand(@PathVariable Long id) {
        return productRepository.findProductsByUserBrandId(id);
    }

    @GetMapping("/category/{id}")
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN', 'USER')")
    public List<Product> getByCategory(@PathVariable Long id) {
        return productRepository.findProductsByCategoryId(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN')")
    public Product addProduct(@RequestBody Product product) {

        if (product == null || product.getCategory() == null || product.getCategory().getId() == null) {
            throw new IllegalArgumentException("Product or category cannot be null");
        }

        Long categoryId = product.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        product.setCategory(category);

        return productRepository.save(product);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/filterByName/{name}")
    public List<Product> getProductById(@PathVariable String name) {
        return productRepository.findProductsWithCharacterInName(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setModel(product.getModel());
                    existingProduct.setQuantityInStock(product.getQuantityInStock());
                    existingProduct.setStatus(product.getStatus());
                    existingProduct.setCategory(product.getCategory());
                    return productRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return productRepository.save(product);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
