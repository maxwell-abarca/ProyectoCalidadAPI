package com.project.demo.logic.entity.property;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
@Component
public class PropertySeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final PropertyRepository propertyRepository;

    public PropertySeeder(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadProperties();
    }

    private void loadProperties() {
        String[] propertyNames = new String[]{"Correo Sendgrid", "Asunto Recuperacion"};
        Map<String, String> propertyParameterMap = Map.of(
                "Correo Sendgrid", "robertaraya382@gmail.com",
                "Asunto Recuperacion", "Recuperación de contraseña - Código de verificación"
        );
        Arrays.stream(propertyNames).forEach((propertyName) -> {
            Optional<Property> optionalCategory = propertyRepository.findByName(propertyName);

            optionalCategory.ifPresentOrElse(System.out::println, () -> {
                Property propertyToCreate = new Property();

                propertyToCreate.setName(propertyName);
                propertyToCreate.setParameter(propertyParameterMap.get(propertyName));
                propertyRepository.save(propertyToCreate);
            });
        });
    }
}
