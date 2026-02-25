package com.project.demo.rest.auth;

import com.project.demo.logic.entity.auth.AuthenticationService;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticate_noModificaInputUser() {
        User inputUser = new User();
        inputUser.setEmail("cl@gmail.com");
        inputUser.setPassword("12345");

        String emailOriginal = inputUser.getEmail();
        String passwordOriginal = inputUser.getPassword();

        User mockUserDb = new User();
        mockUserDb.setEmail(emailOriginal);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUserDb));

        authenticationService.authenticate(inputUser);

        assertEquals(emailOriginal, inputUser.getEmail(), "cl@gmail.com");
        assertEquals(passwordOriginal, inputUser.getPassword(), "12345");
    }
}