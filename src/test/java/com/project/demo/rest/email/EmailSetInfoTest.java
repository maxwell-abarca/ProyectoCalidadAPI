package com.project.demo.rest.email;

import com.project.demo.logic.entity.email.EmailService;
import com.sendgrid.helpers.mail.objects.Email;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailSetInfoTest {

    private final EmailService emailService = new EmailService();

    @Test
    void setEmail_seteaNameYEmailCorrectamente() {
        // GIVEN
        String nombreTest = "Empresa qa";
        String correoTest = "qa@qa.co.cr";
        // WHEN (setEmail)
        Email result = ReflectionTestUtils.invokeMethod(emailService, "setEmail", nombreTest, correoTest);
        // THEN
        assertEquals(nombreTest, result.getName());
        assertEquals(correoTest, result.getEmail());
    }
}