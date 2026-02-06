package com.project.demo.rest.email;

import com.project.demo.logic.entity.email.EmailDetails;
import com.project.demo.logic.entity.email.EmailService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public String sendSimpleEmail(@RequestBody EmailDetails emailDetails) throws IOException {
        return emailService.sendEmail(emailDetails);
    }

}
