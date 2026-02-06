package com.project.demo.rest.property;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.property.Property;
import com.project.demo.logic.entity.property.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("properties")
public class PropertyController {
    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER_BRAND','USER','SUPER_ADMIN')")
    public List<Property> getAllProperty() {
        return propertyRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    public Property addProperty(@RequestBody Property property) {
        String propertyName = property.getName();
        if (propertyRepository.existsByName(propertyName)) {
            throw new IllegalArgumentException("Property with name '" + propertyName + "' already exists");
        }
        return propertyRepository.save(property);
    }

    @GetMapping("/{id}")
    public Property getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Property updateProperty(@PathVariable Long id, @RequestBody Property property) {
        return propertyRepository.findById(id)
                .map(existingProperty -> {
                    existingProperty.setName(property.getName());
                    existingProperty.setParameter(property.getParameter());
                    return propertyRepository.save(existingProperty);
                })
                .orElseGet(() -> {
                    property.setId(id);
                    return propertyRepository.save(property);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteProperty(@PathVariable Long id) {
        propertyRepository.deleteById(id);
    }

}
