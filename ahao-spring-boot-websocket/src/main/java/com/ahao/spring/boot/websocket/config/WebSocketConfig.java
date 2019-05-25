package com.ahao.spring.boot.websocket.config;

import com.ahao.spring.boot.websocket.controller.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, ChatHandler.PATH).setAllowedOrigins("*");
        registry.addHandler(chatHandler, ChatHandler.PATH_SOCKJS).setAllowedOrigins("*").withSockJS();
    }
}