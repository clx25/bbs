package com.bbs.interceptor;

import com.bbs.entity.vo.UserVO;
import com.bbs.service.UserService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class WebsocketInterceptor implements HandshakeInterceptor {
    @Resource
    private UserService userService;


    /**
     * 在控制器之前获取用户登录信息，放到session中
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        UserVO userVO = userService.getUserSimpleInfo();
        if (userVO != null) {
            attributes.put("user", userVO);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
