package com.project.demo.logic.entity.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT n FROM Cart n WHERE n.userBuyer.id= ?1")
    List<Cart> findByUserId(Long userId);

}
