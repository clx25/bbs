package com.bbs.shiro;

import com.bbs.entity.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 重写shiro的user拦截器，主要用于在没有登录或没有记住的情况下返回json数据，而不是重定向页面
 */
@Slf4j
public class HttpMethodUserFilter extends UserFilter {

    @Override
    protected boolean pathsMatch(String path, ServletRequest request) {
       return HttpMethodPathCheck.check(path,request);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        Status status = new Status();
        status.setCode(HttpStatus.UNAUTHORIZED.value());
        status.setMsg("未登录");
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(status);

        PrintWriter writer = httpServletResponse.getWriter();
        writer.print(json);
        return false;

    }





}
