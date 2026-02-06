package com.project.demo.logic.entity.rateOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RateOrderRepository extends JpaRepository<RateOrder, Long> {

    @Query("SELECT r FROM RateOrder r WHERE r.userBuyer.id = ?1 AND r.order.Id = ?2")
    Optional<RateOrder> findByIdBuyerAndOrderId(Long buyerId, Long orderId);

    List<RateOrder> findByOrderId(Long orderId);
}
