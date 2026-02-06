package com.project.demo.logic.entity.userBuyer;

import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserBuyerSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;


    public UserBuyerSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        createUserBuyers();
    }

    private void createUserBuyers() {
        Object[][] userBuyersData = {
                {"User", "Buyer", "Masculino", "user.buyer@gmail.com", "userbuyer123"},
                {"Manuel", "Garro", "Masculino", "mg@gmail.com", "manuel123"},
                {"Ashley", "Graham", "Femenino", "ag@gmail.com", "ashely123"}
        };

        for (Object[] userData : userBuyersData) {
            createUserBuyer(userData);
        }
    }

    private void createUserBuyer(Object[] userData) {
        String email = (String) userData[3];
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        UserBuyer user = new UserBuyer();
        user.setName((String) userData[0]);
        user.setLastname((String) userData[1]);
        user.setPicture("https://res.cloudinary.com/drlznypvr/image/upload/c_fill,w_200,h_200/v1720842225/439973425_8044300855602982_95489407312113055_n_bhwkz1.jpg");
        user.setGenre((String) userData[2]);
        user.setDeliveryLocation("San José, San Rafael Arriba de Desamparados");
        user.setPhoneNumber("71157914");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode((String) userData[4]));
        user.setRole(optionalRole.get());
        user.setStatus("Activo");
        userRepository.save(user);
    }


}
