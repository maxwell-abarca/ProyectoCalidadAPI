package com.project.demo.rest.notification;

import com.project.demo.logic.entity.notification.NotificationHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationHandlerTest1 {

    @InjectMocks
    private NotificationHandler notificationHandler;

    @Mock
    private WebSocketSession session;

    @Test
    void afterConnectionEstablished_conUserIdEnQuery_seteaAtributoUserId() throws Exception {
        URI uri = new URI("ws://localhost:8080/notifications?userId=10");
        when(session.getUri()).thenReturn(uri);
        Map<String, Object> attributes = new HashMap<>();
        when(session.getAttributes()).thenReturn(attributes);

        notificationHandler.afterConnectionEstablished(session);

        assertEquals("10", attributes.get("userId"),
                "El handler debería haber extraído '10' de la URI y guardarlo en los atributos de la sesión");
    }
    @Test
    void afterConnectionEstablished_sinUserId_noSeteaAtributo() throws Exception {
        URI uri = new URI("ws://localhost:8080/notifications");
        when(session.getUri()).thenReturn(uri);

        Map<String, Object> attributes = new HashMap<>();
        lenient().when(session.getAttributes()).thenReturn(attributes);

        notificationHandler.afterConnectionEstablished(session);

        assertNull(attributes.get("userId"),
                "Si no se provee userId en la URI, el atributo no debe setearse");
    }
}