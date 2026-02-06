package com.project.demo.logic.entity.notificationTemplate;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class NotificationTemplateSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final NotificationTemplateRepository notificationTemplateRepository;

    public NotificationTemplateSeeder(NotificationTemplateRepository notificationTemplateRepository) {
        this.notificationTemplateRepository = notificationTemplateRepository;
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.loadNotificationTemplates();
    }

    public void loadNotificationTemplates() {
        String[] notificationTemplatesTitles = new String[]{"Orden confirmada", "Orden empacada", "Producto creado", "Orden entregada", "Orden creada"};
        Map<String, String> notificationTemplatesDescriptions = Map.of(
                "Orden confirmada", "Tu orden ha sido confirmada, haz click para ver mas detalles",
                "Orden empacada", "Tu orden ha sido empacada, haz click para ver detalles del estado actual de la orden",
                "Producto creado", "Producto creado correctamente",
                "Orden entregada", "Tu orden ha sido entregada, haz click para ver mas detalles y calificar la entrega de la orden",
                "Orden creada", "ha creado una nueva orden, haz click para ver mas detalles"
        );
        Map<String, String> notificationTemplateRedirectLinks = Map.of(
                "Orden confirmada", "user-orders",
                "Orden empacada", "user-orders",
                "Producto creado", "products",
                "Orden entregada", "brand-orders",
                "Orden creada", "brand-orders"
        );

        Arrays.stream(notificationTemplatesTitles).forEach((notificationTemplateTitle) -> {
            Optional<NotificationTemplate> optionalNotificationTemplate = notificationTemplateRepository.findByTitle(notificationTemplateTitle);

            optionalNotificationTemplate.ifPresentOrElse(System.out::println, () -> {
                NotificationTemplate notificationTemplateToCreate = new NotificationTemplate();

                notificationTemplateToCreate.setTitle(notificationTemplateTitle);
                notificationTemplateToCreate.setDescription(notificationTemplatesDescriptions.get(notificationTemplateTitle));
                notificationTemplateToCreate.setRedirectLink(notificationTemplateRedirectLinks.get(notificationTemplateTitle));
                notificationTemplateRepository.save(notificationTemplateToCreate);
            });
        });
    }
}
