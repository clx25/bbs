package com.bbs.web.controller;

import com.bbs.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主要用来捕获404/405异常，统一回复格式
 */
@RestController
@Slf4j
public class CustomErrorController implements ErrorController {




    @RequestMapping("/customError")
    public Status returnErrorMsg(HttpServletRequest request, HttpServletResponse response){
        Status messageVO = new Status();
        HttpStatus status = getStatus(request);
        response.setStatus(status.value());
        messageVO.setMsg(status.getReasonPhrase());
        return messageVO;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            log.error("状态码为空");
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        if(statusCode==404||statusCode==405){
            return HttpStatus.NOT_FOUND;
        }
        else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    @Override
    public String getErrorPath() {
        return "/customError";
    }
}
