package com.project.demo.rest.email;

import com.project.demo.logic.entity.email.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailHtmlBodyTest {

    private final EmailService emailService = new EmailService();

    @Test
    void createHtmlContent_incluyeEmailBody() {
        // 1. GIVEN
        String name = "Usuario";
        String body = "Este es un mensaje de prueba único 12345";
        // 2. WHEN
        String htmlResult = ReflectionTestUtils.invokeMethod(emailService, "createHtmlContent", name, body);
        // 3. THEN
        assertTrue(htmlResult.contains("Este es un mensaje de prueba único 12345"),
                "El HTML generado debe incluir el contenido (body) proporcionado en EmailDetails.");
    }
}
