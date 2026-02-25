package com.project.demo.rest.email;

import com.project.demo.logic.entity.email.EmailDetails;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailSubjectTest {

    @Test
    void sendEmail_usaSubjectDelEmailDetails() {
        // GIVEN
        EmailDetails details = new EmailDetails();
        details.setSuject("Asunto: Prueba con el email");
        // THEN
        assertEquals("Asunto: Prueba con el email", details.getSuject());
    }
}
