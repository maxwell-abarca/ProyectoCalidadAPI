package com.project.demo.logic.entity.rateBrand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RateBrandRepository extends JpaRepository<RateBrand, Long> {

    @Query("SELECT r FROM RateBrand r WHERE r.userBuyer.id = ?1 AND r.userBrand.id = ?2")
    Optional<RateBrand> findByIdBuyerAndIdBrand(Long buyerId, Long brandId);

    List<RateBrand> findByUserBrandId(Long brandId);
}
