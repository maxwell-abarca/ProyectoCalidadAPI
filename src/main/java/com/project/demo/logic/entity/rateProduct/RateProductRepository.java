package com.project.demo.logic.entity.rateProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RateProductRepository extends JpaRepository<RateProduct, Long> {

    @Query("SELECT r FROM RateProduct r WHERE r.userBuyer.id = ?1 AND r.product.Id = ?2")
    Optional<RateProduct> findByIdBuyerAndIdProduct(Long buyerId, Long productId);

    List<RateProduct> findByProductId(Long productId);
}
