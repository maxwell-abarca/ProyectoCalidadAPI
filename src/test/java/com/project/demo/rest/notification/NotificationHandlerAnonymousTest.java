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
class NotificationHandlerAnonymousTest {

    @InjectMocks
    private NotificationHandler notificationHandler;

    @Test
    void broadcastNotification_siSesionNoTieneUserId_noEnvia() throws Exception {
        // 1. GIVEN: Una sesión que NO tiene el atributo "userId"
        WebSocketSession anonymousSession = mock(WebSocketSession.class);
        // El mapa de atributos está vacío o no contiene "userId"
        Map<String, Object> emptyAttributes = new HashMap<>();
        when(anonymousSession.getAttributes()).thenReturn(emptyAttributes);
        // Agregamos la sesión al handler (simulando que se conectó sin ?userId=)
        when(anonymousSession.getUri()).thenReturn(new URI("ws://localhost/ws"));
        notificationHandler.afterConnectionEstablished(anonymousSession);
        // 2. WHEN: Se intenta enviar una notificación para cualquier usuario (ej. ID 10)
        Notification notification = new Notification();
        notification.setMessage("Test anonimo");
        User recipient = new User();
        recipient.setId(10L);
        Role mockRole = new Role();
        mockRole.setName(RoleEnum.valueOf("USER"));
        recipient.setRole(mockRole);
        notification.setUser(recipient);
        notificationHandler.broadcastNotification(notification);
        // 3. THEN: Verificamos que NUNCA se llamó a sendMessage
        verify(anonymousSession, never()).sendMessage(any(TextMessage.class));
    }
}