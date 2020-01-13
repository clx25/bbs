package com.own.mapper.validation;

import com.own.exception.customexception.MapperArgsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 用于校验mapper方法的入参
 */
@Aspect
@Component
public class MapperValidator {

    @Around("execution(* com.own.mapper.*Mapper.*(*))")
    public Object isValid(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null) {
                throw new MapperArgsException(joinPoint.getSignature() + "参数为空");
            }
            if (arg instanceof Collection && ((Collection) arg).size() < 1) {
                throw new MapperArgsException(joinPoint.getSignature() + "集合参数长度小于1");
            }
            if (arg instanceof Number && ((Number) arg).intValue() < 1) {
                throw new MapperArgsException(joinPoint.getSignature() + "数字参数小于1");
            }
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }
}
