package com.project.demo.logic.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %?1%")
    List<Product> findProductsWithCharacterInName(String character);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1")
    List<Product> findProductsWithCategory(Long id);

    @Query("SELECT u FROM Product u WHERE u.name = ?1")
    Optional<Product> findByName(String name);

    List<Product> findProductsByCategoryId(Long categoryId);

    List<Product> findProductsByUserBrandId(Long userBrandId);
}
