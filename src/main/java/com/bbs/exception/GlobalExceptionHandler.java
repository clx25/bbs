package com.bbs.exception;

import com.bbs.entity.Status;
import com.bbs.exception.customexception.ArgsNotValidException;
import com.bbs.exception.customexception.MapperArgsException;
import com.bbs.util.ResultUtil;
import com.bbs.util.StatusEnum;
import com.bbs.exception.customexception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Method;

/**
 * 异常统一处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //未知异常
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Status exception(Exception e) {
        log.error("未知异常", e);
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), "网络异常");
    }

    //数据库访问异常
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Status DataAccessException(DataAccessException e) {
        log.error("数据访问异常", e);
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), "网络异常");
    }

    //数据绑定失败，如传入的数据类型与目标类型不符
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status bindException(BindException e) {
        String msg = e.getFieldError().getDefaultMessage();
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), msg);
    }

    //jsr303注解校验参数不符合规则
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), msg);
    }


    //传入的数据类型错误
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status methodArgumentTypeMismatchException() {
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), "参数类型错误");
    }

    //也是参数校验异常
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status constraintViolationException(ConstraintViolationException e) {
        int index = e.getMessage().indexOf(":");
        String msg = null;
        if (index != -1) {
            msg = e.getMessage().substring(index + 1);
        }
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), msg);
    }

    //一些手动校验的参数，不符合规则抛出的异常
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status argsNotValidException(ArgsNotValidException e) {
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }


    //登录验证异常
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Status authenticationException(AuthenticationException e) {
        return ResultUtil.info(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    //登录失败
    @ExceptionHandler({CredentialsException.class, UnknownAccountException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Status credentialsException() {
        return ResultUtil.info(HttpStatus.UNAUTHORIZED.value(), "账号或密码错误");
    }

    //账号被锁定
    @ExceptionHandler(LockedAccountException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Status lockedAccountException() {
        return ResultUtil.info(HttpStatus.FORBIDDEN.value(), "账号被禁用");
    }

    //不支持的http请求
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Status UnauthenticatedException() {
        return ResultUtil.info(StatusEnum.NOT_FOUND);
    }

    //不支持的HttpMediaType，除了文件外，都使用URL传参或json格式参数
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status httpMediaTypeNotSupportedException() {
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), "请使用json格式提交");
    }


    //由于都需要json格式数据，如果以json方式提交但数据不是json格式，数据转换异常
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status httpMessageNotReadableException() {
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), "参数格式错误");
    }


    //缺少参数，如get /post?postId=1,但是提交了get /post
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Status missingServletRequestParameterException() {
        return ResultUtil.info(HttpStatus.BAD_REQUEST.value(), "缺少必要的参数");
    }

    //参数符合规则，但是该数据在数据库中找不到
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Status notFoundException() {
        return ResultUtil.info(StatusEnum.NOT_FOUND);
    }


    //mapper接口方法的入参校验
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Status mapperArgsException(MapperArgsException e) {
        log.error("mapper入参异常", e);
        Status status = new Status();
        status.setMsg("网络异常，请稍后再试（手动滑稽）");
        return status;
    }


    //捕获@Async注解的多线程中的异常
    public static class MyAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            if (throwable instanceof MailException) {
                log.error("邮件发送异常", throwable);
            } else {
                log.error("未知异常", throwable);
            }

        }
    }

}
