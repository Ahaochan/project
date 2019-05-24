package com.ahao.spring.boot.websocket.endpoint.service.impl;

import com.ahao.spring.boot.websocket.endpoint.service.ChatService;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatServiceImpl implements ChatService {
    /**
     * 保存Session对象
     */
    private Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    public void joinChat(Session session) {
        onlineSessions.put(session.getId(), session);
    }

    public void cancelChat(Session session) {
        onlineSessions.remove(session.getId());
    }

    public void sendToAll(String msg) {
        for (Map.Entry<String, Session> entry : onlineSessions.entrySet()) {
//            String sessionId = entry.getKey();
            Session session = entry.getValue();
            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
