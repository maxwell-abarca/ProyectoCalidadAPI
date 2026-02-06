package com.project.demo.logic.entity.product;

import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.userBrand.UserBrandRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ProductSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final UserBrandRepository userBrandRepository;



    public ProductSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            UserBrandRepository userBrandRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userBrandRepository = userBrandRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createProducts();
    }

    private void createProducts() {
        Object[][] productsData = {
                {"Camisa", 60.00, "48", "https://res.cloudinary.com/drlznypvr/image/upload/v1724432263/model_fnelge.glb", 9, "Activo", "Camisas", "Lorem Ipsum"},
                {"Camisa Blanca 2", 30.00, "24" ,"https://res.cloudinary.com/drlznypvr/image/upload/v1724372297/model_jt443k.glb", 9, "Activo", "Camisas", "Lorem Ipsum"},
                {"Sweater de mujer", 100.00, "M" , "https://res.cloudinary.com/drlznypvr/image/upload/v1724388828/sweater_woman_rstuqe.glb", 9, "Activo", "Sweaters", "Guess"},
                {"Camisa", 60.00, "48", "https://res.cloudinary.com/drlznypvr/image/upload/v1724432263/model_fnelge.glb", 9, "Activo", "Camisas", "Guess"},
                {"Camisa", 30.00, "24" ,"https://res.cloudinary.com/drlznypvr/image/upload/v1724372297/model_jt443k.glb", 9, "Activo", "Camisas", "Calvin Klein"},
                {"Sweater de mujer", 100.00, "M" , "https://res.cloudinary.com/drlznypvr/image/upload/v1724388828/sweater_woman_rstuqe.glb", 9, "Activo", "Sweaters", "Nike"},
                {"Camisa", 60.00, "48", "https://res.cloudinary.com/drlznypvr/image/upload/v1724432263/model_fnelge.glb", 9, "Activo", "Camisas", "Calvin Klein"},
                {"Camisa", 30.00, "24" ,"https://res.cloudinary.com/drlznypvr/image/upload/v1724372297/model_jt443k.glb", 9, "Activo", "Camisas", "Adidas"},
                {"Sweater de mujer", 100.00, "M" , "https://res.cloudinary.com/drlznypvr/image/upload/v1724388828/sweater_woman_rstuqe.glb", 9, "Activo", "Sweaters", "Nike"},
                {"Camisa", 60.00, "48", "https://res.cloudinary.com/drlznypvr/image/upload/v1724432263/model_fnelge.glb", 9, "Activo", "Camisas", "Adidas"},
                {"Camisa", 30.00, "24" ,"https://res.cloudinary.com/drlznypvr/image/upload/v1724372297/model_jt443k.glb", 9, "Activo", "Camisas", "Nike"},
                {"Sweater de mujer", 100.00, "M" , "https://res.cloudinary.com/drlznypvr/image/upload/v1724388828/sweater_woman_rstuqe.glb", 9, "Activo", "Sweaters", "Nike"},
                {"Camisa", 60.00, "48", "https://res.cloudinary.com/drlznypvr/image/upload/v1724432263/model_fnelge.glb", 9, "Activo", "Camisas", "Nike"},
                {"Camisa", 30.00, "24" ,"https://res.cloudinary.com/drlznypvr/image/upload/v1724372297/model_jt443k.glb", 9, "Activo", "Camisas", "Levis"},
                {"Sweater de mujer", 100.00, "M" , "https://res.cloudinary.com/drlznypvr/image/upload/v1724388828/sweater_woman_rstuqe.glb", 9, "Activo", "Sweaters", "Nike"},
                {"Camisa", 60.00, "48", "https://res.cloudinary.com/drlznypvr/image/upload/v1724432263/model_fnelge.glb", 9, "Activo", "Camisas", "Levis"},
                {"Camisa", 30.00, "24" ,"https://res.cloudinary.com/drlznypvr/image/upload/v1724372297/model_jt443k.glb", 9, "Activo", "Camisas", "Puma"},
                {"Sweater de mujer", 100.00, "M" , "https://res.cloudinary.com/drlznypvr/image/upload/v1724388828/sweater_woman_rstuqe.glb", 9, "Activo", "Sweaters", "Nike"},
        };

        for (Object[] productData : productsData) {
            Product product = new Product();
            product.setName((String) productData[0]);
            product.setPrice((Double) productData[1]);
            product.setSize((String) productData[2]);
            product.setModel((String) productData[3]);
            product.setQuantityInStock((Integer) productData[4]);
            product.setStatus((String) productData[5]);

            categoryRepository.findByName((String) productData[6]).ifPresent(product::setCategory);
            userBrandRepository.findByName((String) productData[7]).ifPresent(product::setUserBrand);

            if (product.getCategory() != null && product.getUserBrand() != null) {
                productRepository.save(product);
            }
        }
    }



}
