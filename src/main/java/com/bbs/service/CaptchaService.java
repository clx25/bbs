package com.bbs.service;

import com.bbs.exception.custom.ArgsNotValidException;
import com.bbs.util.RedisUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {

    @Resource
    private JavaMailSenderImpl mailSender;


    /**
     * 发送验证码并保存到redis
     *
     * @param email 目标邮箱
     */
    @Async
    public void sendCaptchaAndSave(String email) {
        //创建一个6位数的随机字符串作为验证码
        String captcha = UUID.randomUUID().toString().substring(0, 6);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("验证码");
        mailMessage.setText("验证码：" + captcha + "\n15分钟之内有效");
        mailMessage.setFrom("x_201904@sina.com");
        mailMessage.setTo(email);

        RedisUtil.set(email, captcha, 15, TimeUnit.MINUTES);
        mailSender.send(mailMessage);
    }

    /**
     * 检查验证码
     *
     * @param email    邮箱
     * @param vCaptcha 验证码
     * @throws ArgsNotValidException 验证码错误
     */
    public void checkCaptcha(String email, String vCaptcha) throws ArgsNotValidException {
        String captcha = RedisUtil.get(email);

        if (captcha == null || !captcha.equals(vCaptcha)) {
            throw new ArgsNotValidException("验证码错误");
        }
    }
}
