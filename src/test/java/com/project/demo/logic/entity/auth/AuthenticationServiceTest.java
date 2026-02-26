package com.project.demo.logic.entity.auth;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private AuthenticationService authenticationService;

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
    void authenticate_llamaAuthenticationManagerConEmailYPassword_yRetornaUsuarioDelRepositorio() {
        // auth OK
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(mock(Authentication.class));

        // user exists
        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(storedUser));

        User result = authenticationService.authenticate(inputUser);

        // verify token passed to auth manager
        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);
        verify(authenticationManager).authenticate(captor.capture());
        Authentication authArg = captor.getValue();

        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authArg);
        assertEquals("test@email.com", authArg.getPrincipal());
        assertEquals("password", authArg.getCredentials());

        // verify returned user
        assertSame(storedUser, result);
    }

    @Test
    void authenticate_siAuthenticationManagerLanzaExcepcion_sePropagaYNoConsultaRepositorio() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Credenciales invalidas"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(inputUser));

        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void authenticate_siRepositorioNoEncuentraUsuario_lanzaNoSuchElementException() {
        // auth OK
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(mock(Authentication.class));

        // user missing
        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> authenticationService.authenticate(inputUser));

        verify(userRepository).findByEmail("test@email.com");
    }

    @Test
    void authenticate_consultaRepositorioConElEmailDelInput() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(storedUser));

        authenticationService.authenticate(inputUser);

        verify(userRepository).findByEmail("test@email.com");
    }

    @Test
    void authenticate_noModificaInputUser() {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail("test@email.com"))
                .thenReturn(Optional.of(storedUser));

        String emailOriginal = inputUser.getEmail();
        String passwordOriginal = inputUser.getPassword();

        authenticationService.authenticate(inputUser);

        assertEquals(emailOriginal, inputUser.getEmail());
        assertEquals(passwordOriginal, inputUser.getPassword());
    }
}