package com.project.demo.logic.entity.Otp;

import com.project.demo.logic.entity.email.EmailDetails;
import com.project.demo.logic.entity.email.EmailInfo;
import com.project.demo.logic.entity.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {
    private static final String DEFAULT_EMAIL = "robertaraya382@gmail.com";
    private static final String OTP_SUBJECT = "Codigo de verificacion";

    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private EmailService emailService;

    public String generateOtp() {
        return generateOtp(DEFAULT_EMAIL);
    }

    public String generateOtp(String email) {
        validateEmail(email);

        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

        Otp otpEntity = new Otp();
        otpEntity.setOtpCode(otp);
        otpEntity.setEmail(email);
        otpEntity.setExpiryTime(expiryTime);

        otpRepository.save(otpEntity);

        sendOtpEmail(email, otp);

        return otp;
    }

    public boolean validateOtp(String email, String otpCode) {
        validateEmail(email);
        validateOtpCode(otpCode);

        Optional<Otp> otpOptional = otpRepository.findByOtpCodeAndEmail(otpCode, email);

        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            LocalDateTime now = LocalDateTime.now();

            if (otp.getExpiryTime().isAfter(now)) {
                otpRepository.delete(otp);
                return true;
            }
            otpRepository.delete(otp);
            return false;
        }
        return false;
    }

    @Scheduled(fixedRate = 60000)
    public void cleanExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        List<Otp> expiredOtps = otpRepository.findExpiredOtps(now);
        otpRepository.deleteAll(expiredOtps);
        System.out.println("Se han eliminado " + expiredOtps.size() + " OTPs expirados.");
    }

    private void sendOtpEmail(String email, String otp) {
        String emailBody = "Tu codigo de verificacion es: " + otp + "\n Este codigo expira en 10 minutos.";
        EmailDetails emailDetails = createEmailDetails(email, emailBody);
        try {
            emailService.sendEmail(emailDetails);
            System.out.println("El correo se envio con exito.");
        } catch (Exception e) {
            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }

    private EmailDetails createEmailDetails(String email, String emailBody) {
        EmailInfo fromAddress = new EmailInfo("JBart", email);
        EmailInfo toAddress = new EmailInfo("User", email);

        return new EmailDetails(fromAddress, toAddress, OTP_SUBJECT, emailBody);
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email no puede ser nulo o vacio");
        }
    }

    private void validateOtpCode(String otpCode) {
        if (otpCode == null || otpCode.trim().isEmpty()) {
            throw new IllegalArgumentException("OTP no puede ser nulo o vacio");
        }
        if (!otpCode.matches("\\d+")) {
            throw new IllegalArgumentException("OTP debe contener solo digitos");
        }
    }
}
