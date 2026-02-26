package com.project.demo.logic.entity.auth;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticate_llamaAuthenticationManagerConEmailYPassword() {
        User input = new User();
        input.setEmail("usuario@test.com");
        input.setPassword("secreto123");

        User expectedUser = new User();
        expectedUser.setEmail(input.getEmail());
        when(userRepository.findByEmail(input.getEmail())).thenReturn(Optional.of(expectedUser));

        User result = authenticationService.authenticate(input);

        ArgumentCaptor<Authentication> authenticationCaptor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(authenticationCaptor.capture());
        Authentication authentication = authenticationCaptor.getValue();

        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
        assertEquals(input.getEmail(), authentication.getPrincipal());
        assertEquals(input.getPassword(), authentication.getCredentials());
        assertSame(expectedUser, result);
    }

    @Test
    void authenticate_siAuthenticationManagerLanzaExcepcion_sePropagaYNoConsultaRepositorio() {
        User input = new User();
        input.setEmail("usuario@test.com");
        input.setPassword("incorrecta");

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Credenciales invalidas"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(input));
        verify(userRepository, never()).findByEmail(anyString());
    }

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

        assertEquals(emailOriginal, inputUser.getEmail());
        assertEquals(passwordOriginal, inputUser.getPassword());
    }
}
