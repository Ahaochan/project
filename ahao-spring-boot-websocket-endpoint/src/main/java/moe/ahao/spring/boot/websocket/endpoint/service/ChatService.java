package moe.ahao.spring.boot.websocket.endpoint.service;

import javax.websocket.Session;

public interface ChatService {

    void joinChat(Session session);

    void cancelChat(Session session);

    void sendToAll(String msg);
}
