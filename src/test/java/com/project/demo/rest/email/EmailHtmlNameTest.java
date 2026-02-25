package com.project.demo.rest.email;

import com.project.demo.logic.entity.email.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailHtmlNameTest {

    private final EmailService emailService = new EmailService();

    @Test
    void createHtmlContent_incluyeNombreDelDestinatario() {
        // 1. GIVEN
        String name = "Javanka Gordons";
        String body = "Cualquier contenido";
        // 2. WHEN
        String htmlResult = ReflectionTestUtils.invokeMethod(emailService, "createHtmlContent", name, body);
        // 3. THEN
        assertTrue(htmlResult.contains("¡Hola Javanka Gordons!"),
                "El HTML generado debe contener el saludo con el nombre del destinatario.");
    }
}