package com.project.demo.logic.entity.Otp;

import com.project.demo.logic.entity.email.EmailDetails;
import com.project.demo.logic.entity.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OtpService otpService;

    @Test
    void generateOtp_emailVacio_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> otpService.generateOtp(""));

        verifyNoInteractions(otpRepository);
        verifyNoInteractions(emailService);
    }

    @Test
    void generateOtp_emailSoloEspacios_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> otpService.generateOtp("   "));

        verifyNoInteractions(otpRepository);
        verifyNoInteractions(emailService);
    }

    @Test
    void generateOtp_generaCodigoDe6Digitos_soloNumeros() {
        String otp = otpService.generateOtp("usuario@test.com");

        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
    }

    @Test
    void generateOtp_guardaEntidadConEmailYCodigoYExpiracion() {
        String email = "usuario@test.com";

        String otp = otpService.generateOtp(email);

        ArgumentCaptor<Otp> otpCaptor = ArgumentCaptor.forClass(Otp.class);
        verify(otpRepository).save(otpCaptor.capture());
        Otp otpGuardado = otpCaptor.getValue();

        assertEquals(email, otpGuardado.getEmail());
        assertEquals(otp, otpGuardado.getOtpCode());
        assertNotNull(otpGuardado.getExpiryTime());
        assertTrue(otpGuardado.getExpiryTime().isAfter(LocalDateTime.now()));
    }

    @Test
    void generateOtp_expiracionEsAproxAhoraMas5Min() {
        String email = "usuario@test.com";
        LocalDateTime before = LocalDateTime.now();

        otpService.generateOtp(email);

        LocalDateTime after = LocalDateTime.now();
        ArgumentCaptor<Otp> otpCaptor = ArgumentCaptor.forClass(Otp.class);
        verify(otpRepository).save(otpCaptor.capture());

        LocalDateTime expiryDate = otpCaptor.getValue().getExpiryTime();
        LocalDateTime limiteInferior = before.plusMinutes(5).minusSeconds(2);
        LocalDateTime limiteSuperior = after.plusMinutes(5).plusSeconds(2);

        assertFalse(expiryDate.isBefore(limiteInferior));
        assertFalse(expiryDate.isAfter(limiteSuperior));
    }

    @Test
    void generateOtp_enviaCorreoConAsuntoEsperado() throws Exception {
        otpService.generateOtp("usuario@test.com");

        ArgumentCaptor<EmailDetails> emailCaptor = ArgumentCaptor.forClass(EmailDetails.class);
        verify(emailService).sendEmail(emailCaptor.capture());

        assertEquals("Codigo de verificacion", emailCaptor.getValue().getSuject());
    }

    @Test
    void generateOtp_siEmailServiceFalla_noPropagaExcepcion() throws Exception {
        doThrow(new IOException("fallo sendgrid")).when(emailService).sendEmail(any(EmailDetails.class));

        String otp = assertDoesNotThrow(() -> otpService.generateOtp("usuario@test.com"));

        assertNotNull(otp);
        verify(otpRepository).save(any(Otp.class));
        verify(emailService).sendEmail(any(EmailDetails.class));
    }

    @Test
    void validateOtp_emailNull_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> otpService.validateOtp(null, "123456"));

        verifyNoInteractions(otpRepository);
    }

    @Test
    void validateOtp_emailVacio_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> otpService.validateOtp("", "123456"));

        verifyNoInteractions(otpRepository);
    }

    @Test
    void validateOtp_otpNull_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> otpService.validateOtp("usuario@test.com", null));

        verifyNoInteractions(otpRepository);
    }

    @Test
    void validateOtp_otpConCaracteresNoNumericos_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> otpService.validateOtp("usuario@test.com", "12a456"));

        verifyNoInteractions(otpRepository);
    }

    @Test
    void validateOtp_cuandoExisteOtp_siempreEliminaRegistroAunqueSeaInvalido() {
        String email = "usuario@test.com";

        Otp otpValido = new Otp();
        otpValido.setOtpCode("123456");
        otpValido.setEmail(email);
        otpValido.setExpiryTime(LocalDateTime.now().plusMinutes(1));

        Otp otpExpirado = new Otp();
        otpExpirado.setOtpCode("654321");
        otpExpirado.setEmail(email);
        otpExpirado.setExpiryTime(LocalDateTime.now().minusMinutes(1));

        when(otpRepository.findByOtpCodeAndEmail("123456", email)).thenReturn(Optional.of(otpValido));
        when(otpRepository.findByOtpCodeAndEmail("654321", email)).thenReturn(Optional.of(otpExpirado));

        boolean resultadoValido = otpService.validateOtp(email, "123456");
        boolean resultadoExpirado = otpService.validateOtp(email, "654321");

        assertTrue(resultadoValido);
        assertFalse(resultadoExpirado);
        verify(otpRepository).delete(otpValido);
        verify(otpRepository).delete(otpExpirado);
    }
}
