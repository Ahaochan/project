package moe.ahao.spring.boot.websocket.endpoint.controller;

import moe.ahao.spring.boot.websocket.endpoint.service.ChatService;
import moe.ahao.util.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

//    private static ChatService chatService;
//    @Autowired
//    public void setChatService(ChatService chatService) {
//        ChatController.chatService = chatService;
////        chatService = SpringContextHolder.getBean(ChatService.class);
//    }

    public ChatService getChatService() {
        return SpringContextHolder.getBean(ChatService.class);
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("{} : 打开新的Session{}", this.toString(), session.getId()); // 每次创建一个 ChatController 对象
        getChatService().joinChat(session);
        getChatService().sendToAll(session.getId()+"加入了群聊");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        getChatService().sendToAll(session.getId() + ": " + message);
    }

    @OnClose
    public void onClose(Session session) {
        getChatService().cancelChat(session);
        getChatService().sendToAll(session.getId()+"离开了群聊");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}
