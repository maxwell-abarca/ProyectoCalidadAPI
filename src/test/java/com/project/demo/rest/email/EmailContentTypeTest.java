package com.project.demo.rest.email;

import com.project.demo.logic.entity.email.EmailService;
import com.sendgrid.helpers.mail.objects.Content;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailContentTypeTest {

    private final EmailService emailService = new EmailService();

    @Test
    void sendEmail_construyeContentTipoTextHtml() {
        // GIVEN
        String name = "Test";
        String body = "Body";
        // WHEN
        String html = ReflectionTestUtils.invokeMethod(emailService, "createHtmlContent", name, body);
        Content content = new Content("text/html", html);
        // THEN
        assertEquals("text/html", content.getType(), "El tipo de contenido debe ser text/html");
    }
}
