package com.project.demo.logic.entity.avatar;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;


public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    @Query("SELECT c FROM Avatar c WHERE c.userBuyer.id = ?1")
    Optional<Avatar> findByUserBuyer(Long buyerId);

    @Query("SELECT c FROM Avatar c WHERE c.userBuyer.id = ?1")
    boolean existsByUserBuyer(Long buyerId);
}
