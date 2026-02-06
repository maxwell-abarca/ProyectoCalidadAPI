package com.project.demo.rest.notificationTemplate;

import com.project.demo.logic.entity.notificationTemplate.NotificationTemplate;
import com.project.demo.logic.entity.notificationTemplate.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificationTemplates")
public class NotificationTemplateController {

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @GetMapping("/{id}")
    public NotificationTemplate getNotificationTemplateById(@PathVariable Long id) {
        return notificationTemplateRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
