package com.bbs.service;

import com.bbs.exception.custom.ChatException;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public interface ChatService {
    /**
     * 连接成功
     *
     * @param session 连接成功的session
     */
    void onOpen(WebSocketSession session) throws ChatException;

    /**
     * 接收消息
     *
     * @param session 发送消息的sessino
     * @param message 消息
     */
    void onMessage(WebSocketSession session, WebSocketMessage<?> message);


    /**
     * 连接关闭
     */
    void onClose(WebSocketSession session);
}
