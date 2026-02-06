package com.project.demo.logic.entity.notification;

import com.project.demo.logic.entity.notificationTemplate.NotificationTemplate;
import com.project.demo.logic.entity.notificationTemplate.NotificationTemplateRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.userBrand.UserBrandRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NotificationSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserBuyerRepository userBuyerRepository;
    private final UserBrandRepository userBrandRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;


    public NotificationSeeder(NotificationRepository roleRepository, UserRepository userRepository, UserBuyerRepository userBuyerRepository, UserBrandRepository userBrandRepository, NotificationTemplateRepository notificationTemplateRepository) {
        this.notificationRepository = roleRepository;
        this.userRepository = userRepository;
        this.userBuyerRepository = userBuyerRepository;
        this.userBrandRepository = userBrandRepository;
        this.notificationTemplateRepository = notificationTemplateRepository;
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createNotification();
    }

    private void createNotification() {
        Optional<User> userOptional = userRepository.findByEmail("super.admin@gmail.com");
        Optional<NotificationTemplate> notificationTemplateOptional = notificationTemplateRepository.findById(1L);
        if (userOptional.isEmpty()) {
            System.err.println("User not found");
            return;
        }
        if (notificationTemplateOptional.isEmpty()) {
            System.err.println("Template not found");
            return;
        }


        User user = new User();
        user.setId(userOptional.get().getId());
        Notification notification = new Notification();
        notification.setNotificationTemplate(notificationTemplateOptional.get());
        notification.setUser(user);
        notification.setSeen(false);
        notificationRepository.save(notification);
        notificationRepository.save(notification);
        notificationRepository.save(notification);
    }
}
