package com.project.demo.logic.entity.userBuyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
public interface UserBuyerRepository extends JpaRepository<UserBuyer, Long> {

    @Query("SELECT u FROM UserBuyer u WHERE LOWER(u.name) LIKE %?1%")
    List<UserBuyer> findUsersWithCharacterInName(String character);
    @Query("SELECT u FROM UserBuyer u WHERE u.name = ?1")
    Optional<UserBuyer> findByName(String name);
    Optional<UserBuyer> findByEmail(String email);
}