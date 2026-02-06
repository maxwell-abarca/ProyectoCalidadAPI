package com.project.demo.rest.Otp;

import com.project.demo.logic.entity.Otp.OtpService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @PostMapping("/generate")
    public String generateOtp() {
        return otpService.generateOtp();
    }

    @PostMapping("/validate")
    public boolean validateOtp(@RequestBody String email, @RequestBody String otp) {
        return otpService.validateOtp(email,otp);
    }

    @PostConstruct
    public void cleanExpiredOtpsOnStartup() {
        otpService.cleanExpiredOtps();
        System.out.println("Limpieza de OTPs expirados realizada al iniciar la aplicación.");
    }
}
