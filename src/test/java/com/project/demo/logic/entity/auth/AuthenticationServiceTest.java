package com.project.demo.logic.entity.auth;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private User inputUser;
    private User storedUser;

    @BeforeEach
    void setUp() {
        inputUser = new User();
        inputUser.setEmail("test@email.com");
        inputUser.setPassword("password");

        storedUser = new User();
        storedUser.setEmail("test@email.com");
        storedUser.setPassword("encodedPassword");
    }
    @Test
    void authenticate_retornaUsuarioDelRepositorio() {

        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(storedUser));

        User result = authenticationService.authenticate(inputUser);

        assertEquals(storedUser, result);
    }

    @Test
    void authenticate_siRepositorioNoEncuentraUsuario_lanzaNoSuchElementException() {

        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                authenticationService.authenticate(inputUser)
        );
    }

    @Test
    void authenticate_consultaRepositorioConElEmailDelInput() {

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(storedUser));

        authenticationService.authenticate(inputUser);

        verify(userRepository).findByEmail("test@email.com");
    }
}