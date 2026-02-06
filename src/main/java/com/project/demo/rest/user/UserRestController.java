package com.project.demo.rest.user;

import com.project.demo.logic.entity.Otp.Otp;
import com.project.demo.logic.entity.Otp.OtpRepository;
import com.project.demo.logic.entity.Otp.ValidateOtpRequest;
import com.project.demo.logic.entity.email.EmailDetails;
import com.project.demo.logic.entity.email.EmailInfo;
import com.project.demo.logic.entity.email.EmailService;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/users")
public class UserRestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setEmail(user.getEmail());
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    user.setId(id);
                    return userRepository.save(user);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @PostMapping("/generatePasswordResetOtp")
    @PreAuthorize("permitAll")
    public String generatePasswordResetOtp(@RequestBody ValidateOtpRequest request) {
        String email = request.getEmail();
        String otpCode = request.getOtpCode();
        String newPassword = request.getNewPassword();
        String otp = String.valueOf(new Random().nextInt(999999));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);

        Otp otpEntity = new Otp();
        otpEntity.setOtpCode(otp);
        otpEntity.setEmail(email);
        otpEntity.setExpiryTime(expiryTime);

        otpRepository.save(otpEntity);

        sendPasswordResetOtpEmail(otp);

        return "OTP generado exitosamente y enviado a " + email;
    }


    @PostMapping("/resetPassword")
    @PreAuthorize("permitAll")
    public boolean resetPassword(@RequestBody ValidateOtpRequest request) {
        String email = request.getEmail();
        String otpCode = request.getOtpCode();
        String newPassword = request.getNewPassword();

        Optional<Otp> otpOptional = otpRepository.findByOtpCodeAndEmail(otpCode, email);

        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            LocalDateTime now = LocalDateTime.now();

            if (otp.getExpiryTime().isAfter(now)) {
                otpRepository.delete(otp);
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado para el email: " + email));
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            } else {
                otpRepository.delete(otp);
                System.out.println("El código OTP ha expirado.");
                return false;
            }
        }
        return false;
    }

    private void sendPasswordResetOtpEmail(String otp) {
        String email = "robertaraya382@gmail.com";
        String subject = "Recuperación de contraseña - Código de verificación";
        String emailBody = "Tu código de verificación para recuperación de contraseña es: " + otp + "\n Este código expira en 10 minutos.";
        EmailDetails emailDetails = createEmailDetails(emailBody);
        try {
            emailService.sendEmail(emailDetails);
            System.out.println("Correo electrónico con OTP de recuperación de contraseña enviado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }


    private EmailDetails createEmailDetails(String emailBody) {
        EmailInfo fromAddress = new EmailInfo("JBart", "robertaraya382@gmail.com");
        EmailInfo toAddress = new EmailInfo("Usuario", "robertaraya382@gmail.com");
        String subject = "Recuperación de contraseña - Código de verificación";

        return new EmailDetails(fromAddress, toAddress, subject, emailBody);
    }

}