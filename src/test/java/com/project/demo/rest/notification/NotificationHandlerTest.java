package com.project.demo.rest.notification;

import com.project.demo.logic.entity.notification.Notification;
import com.project.demo.logic.entity.notification.NotificationHandler;
import com.project.demo.logic.entity.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class NotificationHandlerTest {
    @InjectMocks
    private NotificationHandler notificationHandler;

    @Mock
    private WebSocketSession session;

    @Mock
    private User user;


    @Test
    void afterConnectionEstablished_agregaSesionALaLista() throws Exception {
        URI uri = new URI("ws://localhost:8080/notifications?userId=10");
        when(session.getUri()).thenReturn(uri);
        Map<String, Object> attributes = new HashMap<>();
        when(session.getAttributes()).thenReturn(attributes);
        Notification notification = new Notification();
        when(user.getId()).thenReturn(10L);
        notification.setUser(user);
        notificationHandler.afterConnectionEstablished(session);
        // (Evidencia por Broadcast)
        // Si la sesión se agregó, al hacer broadcast debería intentar enviar un mensaje
        notificationHandler.broadcastNotification(notification);

        verify(session, times(1)).sendMessage(any(TextMessage.class));
    }



}


