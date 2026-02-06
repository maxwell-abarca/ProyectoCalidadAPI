package com.project.demo.logic.entity.Otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    @Query("SELECT o FROM Otp o WHERE o.expiryTime < :now")
    List<Otp> findExpiredOtps(LocalDateTime now);

    @Query("SELECT o FROM Otp o WHERE o.otpCode = :otpCode AND o.email = :email")
    Optional<Otp> findByOtpCodeAndEmail(@Param("otpCode") String otpCode, @Param("email") String email);
}
