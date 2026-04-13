package com.project.demo.logic.entity.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private UserDetails userA;
    private UserDetails userB;
    private long originalExpiration;

    @BeforeEach
    void setUp() {
        userA = User.withUsername("userA")
                .password("password")
                .authorities("USER")
                .build();

        userB = User.withUsername("userB")
                .password("password")
                .authorities("USER")
                .build();

        originalExpiration = jwtService.getExpirationTime();
    }

    @AfterEach
    void tearDown() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", originalExpiration);
    }

    @Test
    void generateToken_incluyeSubjectComoUsername() {
        String token = jwtService.generateToken(userA);
        String username = jwtService.extractUsername(token);

        assertEquals(userA.getUsername(), username);
    }

    @Test
    void generateToken_incluyeClaimsExtra() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        String token = jwtService.generateToken(extraClaims, userA);

        String role = jwtService.extractClaim(token,
                claims -> claims.get("role", String.class));

        assertEquals("ADMIN", role);
    }

    @Test
    void getExpirationTime_retornaValorConfigurado() {
        long expiration = jwtService.getExpirationTime();

        assertTrue(expiration > 0);
    }

    @Test
    void isTokenValid_tokenValidoYUsuarioCorrecto_true() {
        String token = jwtService.generateToken(userA);

        boolean isValid = jwtService.isTokenValid(token, userA);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_usuarioDistinto_false() {
        String token = jwtService.generateToken(userA);

        boolean isValid = jwtService.isTokenValid(token, userB);

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_tokenExpirado_false() throws Exception {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L);

        Map<String, Object> claims = new HashMap<>();
        String token = jwtService.generateToken(claims, userA);

        Thread.sleep(10);

        boolean isValid = jwtService.isTokenValid(token, userA);

        assertFalse(isValid);
    }

    @Test
    void extractClaim_tokenInvalido_lanzaExcepcion() {
        String tokenInvalido = "token_corrupto";

        assertThrows(Exception.class, () ->
                jwtService.extractClaim(tokenInvalido, claims -> claims)
        );
    }

    @Test
    void extractUsername_tokenConFirmaIncorrecta_lanzaExcepcion() {
        String token = jwtService.generateToken(userA);

        String tokenAlterado = token + "abc";

        assertThrows(Exception.class, () ->
                jwtService.extractUsername(tokenAlterado)
        );
    }

    @Test
    void generateToken_cambiaCuandoCambiaIssuedAt() throws InterruptedException {
        String token1 = jwtService.generateToken(userA);

        Thread.sleep(1000);

        String token2 = jwtService.generateToken(userA);

        assertNotEquals(token1, token2);
    }

    @Test
    void extractClaim_puedeExtraerExpiration_noNull() {
        String token = jwtService.generateToken(userA);

        Date expiration = jwtService.extractClaim(token,
                claims -> claims.getExpiration());

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }
}