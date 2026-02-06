package com.project.demo.logic.entity.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class NotificationHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String uri = session.getUri().toString();
        String[] queryParams = uri.split("\\?");
        String userIdParam = Arrays.stream(queryParams)
                .filter(param -> param.startsWith("userId="))
                .findFirst()
                .orElse(null);

        if (userIdParam != null) {
            String userId = userIdParam.split("=")[1];
            session.getAttributes().put("userId", userId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void broadcastNotification(Notification notification) {
        for (WebSocketSession session : sessions) {
            try {
                String sessionUserId = (String) session.getAttributes().get("userId");
                if (sessionUserId != null && sessionUserId.equals(notification.getUser().getId().toString())) {
                    String notificationJson = objectMapper.writeValueAsString(notification);
                    session.sendMessage(new TextMessage(notificationJson));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}