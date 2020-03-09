package com.bbs.controller;

import com.bbs.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;

@Controller
@Slf4j
public class ChatController extends TextWebSocketHandler {


    private static ChatService chatServiceImpl;

    @Resource
    public void setChatServiceImpl(ChatService chatServiceImpl) {
        ChatController.chatServiceImpl = chatServiceImpl;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        chatServiceImpl.onOpen(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        chatServiceImpl.onMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        chatServiceImpl.onClose(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("未知异常", exception);
    }
}
