package com.project.demo.logic.entity.design;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Long> {
    @Query("SELECT d FROM Design d WHERE d.userBuyer.id= ?1")
    List<Design> findByUserId(Long userId);

}
