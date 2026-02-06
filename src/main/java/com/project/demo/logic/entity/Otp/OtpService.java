package com.project.demo.logic.entity.Otp;

import com.project.demo.logic.entity.email.EmailDetails;
import com.project.demo.logic.entity.email.EmailInfo;
import com.project.demo.logic.entity.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private EmailService emailService;

    public String generateOtp() {
        String email = "robertaraya382@gmail.com";
        String otp = String.valueOf(new Random().nextInt(999999));
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
        Optional<Otp> otpOptional = otpRepository.findByOtpCodeAndEmail(otpCode, email);

        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            LocalDateTime now = LocalDateTime.now();

            if (otp.getExpiryTime().isAfter(now)) {
                otpRepository.delete(otp);
                return true;
            } else {
                otpRepository.delete(otp);
                return false;
            }
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
        String subject = "Codigo de verificacion";
        String emailBody = "Tu codigo de verificacion es: " + otp + "\n Este codigo expira en 10 minutos.";
        EmailDetails emailDetails = createEmailDetails(email, emailBody);
        try {
            emailService.sendEmail(emailDetails);
            System.out.println("El correo se envio con exito.");
        } catch (IOException e) {
            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }

    private EmailDetails createEmailDetails(String email, String emailBody) {
        EmailInfo fromAddress = new EmailInfo("JBart", email);
        EmailInfo toAddress = new EmailInfo("User", email);
        String subject = "Codigo de verificacion";

        return new EmailDetails(fromAddress, toAddress, subject, emailBody);
    }
}