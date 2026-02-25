package com.project.demo.rest.email;

import com.project.demo.logic.entity.email.EmailDetails;
import com.project.demo.logic.entity.email.EmailInfo;
import com.project.demo.logic.entity.email.EmailService;
import com.sendgrid.helpers.mail.objects.Email;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EmailRecipientBugTest {

    private final EmailService emailService = new EmailService();

    @Test
    void sendEmail_detectaBugDeDestinatario_toEmailDebeUsarToInfoNoFromInfo() {
        // GIVEN
        EmailInfo from = new EmailInfo();
        from.setName("Remitente");
        from.setEmailAddress("remitente@qa.com");

        EmailInfo to = new EmailInfo();
        to.setName("Destinatario");
        to.setEmailAddress("destinatario@test.com");

        EmailDetails details = new EmailDetails();
        details.setFromAddress(from);
        details.setToAddress(to);

        Email fromEmailResult = ReflectionTestUtils.invokeMethod(emailService, "setEmail", from.getName(), from.getEmailAddress());

        Email toEmailResult = ReflectionTestUtils.invokeMethod(emailService, "setEmail", to.getName(), to.getEmailAddress());

        assertEquals("destinatario@test.com", toEmailResult.getEmail(),
                "ERROR: El destinatario está recibiendo la dirección del remitente (Bug detectado)");
        assertNotEquals(fromEmailResult.getEmail(), toEmailResult.getEmail(),
                "El email de origen y destino no deben ser el mismo en esta prueba.");
    }
}