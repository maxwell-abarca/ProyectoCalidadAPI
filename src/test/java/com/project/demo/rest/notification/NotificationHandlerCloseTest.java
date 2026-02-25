package com.project.demo.rest.notification;

import com.project.demo.logic.entity.notification.Notification;
import com.project.demo.logic.entity.notification.NotificationHandler;
import com.project.demo.logic.entity.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationHandlerCloseTest {

    @InjectMocks
    private NotificationHandler notificationHandler;

    @Mock
    private WebSocketSession session;

    @Test
    void afterConnectionClosed_remueveSesion() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws?userId=99");
        when(session.getUri()).thenReturn(uri);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", "99");
        when(session.getAttributes()).thenReturn(attributes);

        notificationHandler.afterConnectionEstablished(session);
        notificationHandler.afterConnectionClosed(session, CloseStatus.NORMAL);
        Notification notification = new Notification();
        User mockUser = new User();
        mockUser.setId(99L);
        notification.setUser(mockUser);
        notificationHandler.broadcastNotification(notification);
        verify(session, never()).sendMessage(any(TextMessage.class));
    }
}