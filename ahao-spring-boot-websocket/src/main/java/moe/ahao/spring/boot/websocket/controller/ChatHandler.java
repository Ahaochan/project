package moe.ahao.spring.boot.websocket.controller;

import moe.ahao.spring.boot.websocket.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class ChatHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String PATH = "/chat";
    public static final String PATH_SOCKJS = "/chat-sockjs";

    @Autowired
    private ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("{} : 打开新的Session{}", this.toString(), session.getId()); // 每次创建一个 ChatController 对象
        chatService.joinChat(session);
        chatService.sendToAll(session.getId()+"加入了群聊");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        chatService.sendToAll(session.getId() + ": " + message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatService.cancelChat(session);
        chatService.sendToAll(session.getId()+"离开了群聊");
    }
}
