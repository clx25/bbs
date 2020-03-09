package com.bbs.service.impl;

import com.bbs.entity.vo.ChatVO;
import com.bbs.entity.vo.UserVO;
import com.bbs.exception.custom.ChatException;
import com.bbs.service.ChatService;
import com.bbs.util.EntityUtil;
import com.bbs.util.JsonUtil;
import com.bbs.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    //保存在redis中聊天信息的key
    private static final String CHAT_MSG_KEY = "chatData";

    //保存已连接的session
    private static final ConcurrentLinkedQueue<WebSocketSession> SESSIONS = new ConcurrentLinkedQueue<>();


    /**
     * 连接成功后发送聊天历史数据
     *
     * @param session 连接成功的session
     */
    @Override
    public void onOpen(WebSocketSession session) {

        long len = RedisUtil.llen(CHAT_MSG_KEY);
        if (len > 0) {
            List<Object> lrange = RedisUtil.lrange(CHAT_MSG_KEY, 0, -1);
            try {

                String json = JsonUtil.toJson(lrange);
                session.sendMessage(new TextMessage(json));
            } catch (Exception e) {
                log.error("聊天记录初始化失败", new ChatException());
            }
        }
        SESSIONS.add(session);

    }

    /**
     * 1.判断能否从session中获取用户名（用户是否登录）
     * 2.把聊天信息保存到实体类中，保存到redis
     * 3.发送消息到所有session
     *
     * @param session 当前连接session
     * @param message 聊天数据
     */
    @Override
    public void onMessage(WebSocketSession session, WebSocketMessage<?> message) {
        if (message instanceof PongMessage) {
            return;
        }

        Map<String, Object> attributes = session.getAttributes();
        if (attributes.size() > 0) {
            UserVO userVO = (UserVO) attributes.get("user");
            ChatVO chatVO = saveMessage(userVO, message);
            sendAll(chatVO);
        }
    }


    /**
     * 包装和保存聊天信息
     * 暂只支持文本消息
     *
     * @param userVO  用户信息
     * @param message 聊天消息
     * @return 包含聊天信息的实体类
     */
    private ChatVO saveMessage(UserVO userVO, WebSocketMessage<?> message) {

        ChatVO chatVO = new ChatVO();

        if (!(message instanceof TextMessage)) {
            return chatVO;
        }

        TextMessage textMessage = (TextMessage) message;
        String msg = textMessage.getPayload();

        EntityUtil.CopyToEntity(userVO, chatVO);
        chatVO.setChatMsg(msg);
        chatVO.setTime(new Date());

        RedisUtil.linsert(CHAT_MSG_KEY, chatVO);
        trim();
        return chatVO;
    }

    /**
     * 发送消息
     *
     * @param chatVO 消息实体类
     */
    private void sendAll(ChatVO chatVO) {
        if (!SESSIONS.isEmpty()) {
            for (WebSocketSession webSocketSession : SESSIONS) {
                try {

                    String json = JsonUtil.toJson(chatVO);
                    webSocketSession.sendMessage(new TextMessage(json));
                    webSocketSession.sendMessage(new PingMessage());

                } catch (JsonProcessingException e) {
                    log.error("聊天消息转json失败", new ChatException());
                } catch (IOException e) {
                    log.error("聊天消息同步失败", new ChatException());
                }
            }
        }
    }

    /**
     * 修剪聊天列表
     * 聊天数据如果大于500，那么就删除最近500个消息之前的数据
     */
    private void trim() {
        long len = RedisUtil.llen(CHAT_MSG_KEY);
        if (len > 1000) {
            RedisUtil.trim(CHAT_MSG_KEY, -500, -1);
        }
    }

    @Override
    public void onClose(WebSocketSession session) {

        SESSIONS.remove(session);
        try {
            session.close();
        } catch (IOException e) {
            log.error("关闭WebSocketSession失败", new ChatException());
        }
    }

    @Scheduled(cron = "0 0/5 * * * 0-7")
    public void heartbeat() {
        if (!SESSIONS.isEmpty()) {
            for (WebSocketSession webSocketSession : SESSIONS) {
                try {
                    webSocketSession.sendMessage(new PingMessage());
                } catch (IOException e) {
                    log.error("发送ping失败", new ChatException());
                }
            }
        }
    }
}
