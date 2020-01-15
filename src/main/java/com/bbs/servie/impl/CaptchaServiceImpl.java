package com.bbs.servie.impl;

import com.bbs.exception.customexception.ArgsNotValidException;
import com.bbs.servie.CaptchaService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private JavaMailSenderImpl mailSender;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Async
    public void sendCaptchaAndSave(String email) {
        //创建一个6位数的随机字符串作为验证码
        String captcha = UUID.randomUUID().toString().substring(0, 6);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("验证码");
        mailMessage.setText("验证码：" + captcha + "\n15分钟之内有效");
        mailMessage.setFrom("x_201904@sina.com");
        mailMessage.setTo(email);

        stringRedisTemplate.opsForValue().set(email, captcha, 15, TimeUnit.MINUTES);
        mailSender.send(mailMessage);
    }

    @Override
    public void checkCaptcha(String email, String vCaptcha) throws ArgsNotValidException {
        String captcha = stringRedisTemplate.opsForValue().get(email);
        if (captcha == null || !captcha.equals(vCaptcha)) {
            throw new ArgsNotValidException("验证码错误");
        }
    }
}
