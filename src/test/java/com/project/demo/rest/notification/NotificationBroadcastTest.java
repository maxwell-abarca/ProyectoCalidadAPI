package com.project.demo.rest.notification;


import com.project.demo.logic.entity.notification.Notification;
import com.project.demo.logic.entity.notification.NotificationHandler;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.rol.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationBroadcastTest {

    @InjectMocks
    private NotificationHandler notificationHandler;

    @Test
    void broadcastNotification_enviaSoloASesionDelMismoUserId() throws Exception {
        // 1. GIVEN: Creamos dos sesiones distintas (Mocks manuales para tener varias instancias)
        WebSocketSession sessionUsuario10 = mock(WebSocketSession.class);
        WebSocketSession sessionUsuario20 = mock(WebSocketSession.class);

        // Configuración Sesión Usuario 10
        Map<String, Object> attr10 = new HashMap<>();
        attr10.put("userId", "10");
        when(sessionUsuario10.getAttributes()).thenReturn(attr10);
        when(sessionUsuario10.getUri()).thenReturn(new URI("ws://localhost/ws?userId=10"));

        // Configuración Sesión Usuario 20
        Map<String, Object> attr20 = new HashMap<>();
        attr20.put("userId", "20");
        when(sessionUsuario20.getAttributes()).thenReturn(attr20);
        when(sessionUsuario20.getUri()).thenReturn(new URI("ws://localhost/ws?userId=20"));

        // Registramos ambas en el handler
        notificationHandler.afterConnectionEstablished(sessionUsuario10);
        notificationHandler.afterConnectionEstablished(sessionUsuario20);

        // 2. WHEN: Creamos la notificación y el Usuario con su Rol
        Notification notification = new Notification();
        notification.setMessage("Mensaje para el 10");

        User recipient = new User();
        recipient.setId(10L);

        // IMPORTANTE: Evitamos el NullPointerException de Jackson/getAuthorities
        Role mockRole = new Role();
        mockRole.setName(RoleEnum.valueOf("USER"));
        recipient.setRole(mockRole);
        notification.setUser(recipient);
        // Ejecutamos el broadcast
        notificationHandler.broadcastNotification(notification);
        // 3. THEN: Verificaciones
        // El usuario 10 DEBE recibir el mensaje
        verify(sessionUsuario10, times(1)).sendMessage(any(TextMessage.class));
        // El usuario 20 NUNCA debe recibirlo
        verify(sessionUsuario20, never()).sendMessage(any(TextMessage.class));
    }
}