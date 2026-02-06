package com.project.demo.rest.userBuyer;

import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserBuyerRestControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserBuyerRestController userBuyerRestController;

    @Test
    void deactivateUser_UserNotFound_ReturnsNotFound() {
        UserBuyer user = new UserBuyer();
        user.setId(133L);
        user.setPassword("newPassword123");

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        ResponseEntity<?> response = userBuyerRestController.deactivateUser(user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuario no encontrado.", response.getBody());
    }

    @Test
    void deactivateUser_IncorrectPassword_ReturnsForbidden() {
        UserBuyer user = new UserBuyer();
        user.setId(13L);
        user.setPassword("newPassword123");

        UserBuyer existingUser = new UserBuyer();
        existingUser.setId(13L);
        existingUser.setPassword("encodedPassword");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(user.getPassword(), existingUser.getPassword())).thenReturn(false);

        ResponseEntity<?> response = userBuyerRestController.deactivateUser(user);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Contraseña incorrecta.", response.getBody());
    }
}