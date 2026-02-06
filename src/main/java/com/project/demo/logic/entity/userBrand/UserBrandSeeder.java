package com.project.demo.logic.entity.userBrand;

import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserBrandSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserBrandRepository userBrandRepository;

    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;


    public UserBrandSeeder(
            RoleRepository roleRepository,
            UserBrandRepository userBrandRepository,
            PasswordEncoder passwordEncoder,
            CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.userBrandRepository = userBrandRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createUserBrands();
    }

    private void createUserBrands() {
        Object[][] userBrandsData = {
                {"3101123456", "https://res.cloudinary.com/drlznypvr/image/upload/c_fill,w_200,h_200/v1721022831/logo-compania_125964-228_s1iogw.avif", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722045648/Tarea1_BraylieUre%C3%B1a_h2nush.pdf", "Lorem Ipsum", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalonetas", "user.brand@gmail.com", "userbrand123", 0, "Activo"},
                {"3101874456", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674249/Guess.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675548/Guess-documento_knhvwe.pdf", "Guess", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "guess@gmail.com", "guess123", 0, "Activo"},
                {"129634789", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674249/Converse.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675488/Converse-documento_sck41y.pdf", "Converse", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "converse@gmail.com", "converse123", 3, "Activo"},
                {"129612889", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674249/CalvinKlein.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674971/CalvinKlein-documento_bgi8jq.pdf", "Calvin Klein", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "calvinklein@gmail.com", "calvinklein123", 1, "Activo"},
                {"736634789", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674249/Nike.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675604/Nike-documento_pxtjx1.pdf", "Nike", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "nike@gmail.com", "nike123", 1, "Activo"},
                {"866634789", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674249/Levis.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675564/Levis-documento_rpstv4.pdf", "Levis", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "levis@gmail.com", "levis123", 2, "Activo"},
                {"986734789", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674249/Adidas.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674925/Adidas-documento_md0hia.pdf", "Adidas", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "adidas@gmail.com", "levis123", 0, "Activo"},
                {"986945789", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674250/NewBalance.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675585/NewBalance-documento_gqb0am.pdf", "New Balance", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "newbalance@gmail.com", "newbalance123", 4, "Activo"},
                {"351645789", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674250/Puma.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675623/Puma-documento_xadezo.pdf", "Puma", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "puma@gmail.com", "puma123", 4, "Activo"},
                {"351758789", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674250/Reebok.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675643/Reebok-documento_izppyx.pdf", "Reebok", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "reebok@gmail.com", "reebok123", 2, "Activo"},
                {"241947689", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674250/Zara.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675675/Zara-documento_bv0iku.pdf", "Zara", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "zara@gmail.com", "zara123", 0, "Activo"},
                {"241945789", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722674250/Tommy.png", "https://res.cloudinary.com/dbd6uaiux/image/upload/v1722675659/TommyHilfiger-documento_xidgfv.pdf", "Tommy Hilfiger", "Pedro Pascal", "San Rafael Arriba, Desamparados", "Camisas, Pantalones, Calzado, Pantalonetas, Sweaters", "tommyhilfiger@gmail.com", "tommyhilfiger123", 0, "Activo"}
        };

        for (Object[] userBrandData : userBrandsData) {
            createUserBrand(userBrandData);
        }
    }

    private void createUserBrand(Object[] userBrandData) {
        String email = (String) userBrandData[7];
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER_BRAND);
        Optional<UserBrand> optionalUser = userBrandRepository.findByEmail(email);

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        UserBrand user = new UserBrand();
        user.setLegalId(Long.parseLong((String) userBrandData[0]));
        user.setLogoType((String) userBrandData[1]);
        user.setLegalDocuments((String) userBrandData[2]);
        user.setBrandName((String) userBrandData[3]);
        user.setLegalRepresentativeName((String) userBrandData[4]);
        user.setMainLocationAddress((String) userBrandData[5]);
        user.setBrandCategories((String) userBrandData[6]);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode((String) userBrandData[8]));
        user.setRole(optionalRole.get());
        user.setStatus((String) userBrandData[10]);
        user.setRate((Integer) userBrandData[9]);
        userBrandRepository.save(user);
    }

}
