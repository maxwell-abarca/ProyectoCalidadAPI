package com.project.demo.logic.entity.userBrand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserBrandRepository extends JpaRepository<UserBrand, Long> {
    @Query("SELECT u FROM UserBrand u WHERE LOWER(u.brandName) LIKE %?1%")
    List<UserBrand> findUsersWithCharacterInName(String character);
    @Query("SELECT u FROM UserBrand u WHERE u.brandName = ?1")
    Optional<UserBrand> findByName(String name);
    Optional<UserBrand> findByEmail(String email);
    @Query(
            "SELECT u FROM UserBrand u WHERE u.status = 'Inactivo'"
    )
    List<UserBrand> findUserBrandByStatusInactive ();
    @Query(
            "SELECT u FROM UserBrand u WHERE u.status = 'Activo'"
    )
    List<UserBrand> findUserBrandByStatusActive ();
}