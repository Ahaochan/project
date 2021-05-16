package moe.ahao.spring.boot.websocket.service;

import org.springframework.web.socket.WebSocketSession;

public interface ChatService {

    void joinChat(WebSocketSession session);

    void cancelChat(WebSocketSession session);

    void sendToAll(String msg);
}
