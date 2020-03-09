package com.bbs.config;

import com.bbs.controller.ChatController;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;


@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer {


    private final HandshakeInterceptor websocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(new ChatController(), "/chat").addInterceptors(websocketInterceptor);
    }


}

