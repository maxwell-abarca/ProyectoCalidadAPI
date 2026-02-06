package com.project.demo.logic.entity.category;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class CategorySeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;

    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadCategories();
    }

    private void loadCategories() {
        String[] categoryNames = new String[]{"CAMISAS", "SWEATERS", "PANTALONES", "PANTALONETAS", "CALZADO"};
        Map<String, String> categoryDescriptionMap = Map.of(
                "CAMISAS", "Camisas",
                "SWEATERS", "Sweaters",
                "PANTALONES", "Pantalones",
                "PANTALONETAS", "Pantalonetas",
                "CALZADO", "Calzado"
        );
        Map<String, String> categoryImageMap = Map.of(
                "CAMISAS", "https://images.unsplash.com/photo-1596918404383-22e2c91f4964?q=80&w=1991&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "SWEATERS", "https://images.unsplash.com/photo-1459200486184-972dac90bfc3?q=80&w=2073&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "PANTALONES", "https://images.unsplash.com/photo-1640336437301-8368b53861ab?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "PANTALONETAS", "https://images.unsplash.com/photo-1667388624717-895854eea032?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "CALZADO", "https://images.unsplash.com/photo-1575537302964-96cd47c06b1b?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
        );
        Arrays.stream(categoryNames).forEach((categoryName) -> {
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);

            optionalCategory.ifPresentOrElse(System.out::println, () -> {
                Category categoryToCreate = new Category();

                categoryToCreate.setName(categoryName);
                categoryToCreate.setDescription(categoryDescriptionMap.get(categoryName));
                categoryToCreate.setImage(categoryImageMap.get(categoryName));
                categoryRepository.save(categoryToCreate);
            });
        });
    }
}
