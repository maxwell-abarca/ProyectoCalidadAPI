package com.project.demo.logic.entity.property;

import com.project.demo.logic.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.name = ?1")
    Optional<Property> findByName(String name);

    boolean existsByName(String name);
}
