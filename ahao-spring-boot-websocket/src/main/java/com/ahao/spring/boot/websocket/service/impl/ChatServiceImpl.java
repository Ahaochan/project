package com.ahao.spring.boot.websocket.service.impl;

import com.ahao.spring.boot.websocket.service.ChatService;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatServiceImpl implements ChatService {
    /**
     * 保存Session对象
     */
    private Map<String, WebSocketSession> onlineSessions = new ConcurrentHashMap<>();

    public void joinChat(WebSocketSession session) {
        onlineSessions.put(session.getId(), session);
    }

    public void cancelChat(WebSocketSession session) {
        onlineSessions.remove(session.getId());
    }

    public void sendToAll(String msg) {
        for (Map.Entry<String, WebSocketSession> entry : onlineSessions.entrySet()) {
//            String sessionId = entry.getKey();
            WebSocketSession session = entry.getValue();
            try {
                session.sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
