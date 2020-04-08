package com.bbs.interceptor;

import com.bbs.entity.database.ReplyDO;
import com.bbs.entity.database.ThreadDO;
import com.bbs.exception.custom.ArgsNotValidException;
import com.bbs.mapper.ReplyMapper;
import com.bbs.mapper.ThreadMapper;
import com.bbs.service.ServiceMediator;
import com.bbs.service.UserService;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 删除帖子权限检查
 */
@Component
public class DelPermissionCheckInterceptor implements HandlerInterceptor {

    @Resource
    private ServiceMediator serviceMediator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();

        //只拦截DELETE提交的请求
        if (!"DELETE".equals(method)) {
            return true;
        }

        String servletPath = request.getServletPath();

        Pattern p = Pattern.compile("[1-9][0-9]*");
        Matcher matcher = p.matcher(servletPath);
        if (!matcher.find()) {
            throw new ArgsNotValidException("参数为空");
        }

        int id;
        try {
            id = Integer.parseInt(matcher.group());
        } catch (NumberFormatException e) {
            throw new ArgsNotValidException("参数错误");
        }

        int userId = serviceMediator.getUserId();

        ReplyDO reply = serviceMediator.getReplyById(id);

        //这个回复是该用户发的
        if (reply.getUserId() == userId) {
            return true;
        }
        //是该用户回帖的楼中楼
        if (reply.getPostId() != 0) {
            ReplyDO post = serviceMediator.getReplyById(reply.getPostId());
            if (post.getUserId() == userId) {
                return true;
            }
        }
        //是该用户帖子中的回复
        ThreadDO thread = serviceMediator.getThreadByThreadId(reply.getThreadId());
        if (thread.getUserId() == userId) {
            return true;
        }
        throw new UnauthorizedException("没有删除权限");

    }
}
