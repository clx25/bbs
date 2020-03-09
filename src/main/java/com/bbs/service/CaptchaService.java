package com.bbs.service;

import com.bbs.exception.custom.ArgsNotValidException;

public interface CaptchaService {
    /**
     * 发送验证码并保存到redis中
     *
     * @param email 邮箱
     */
    void sendCaptchaAndSave(String email);

    /**
     * 检查验证码
     *
     * @param email    邮箱
     * @param vCaptcha 待验证的验证码
     * @throws ArgsNotValidException 验证码无效
     */
    void checkCaptcha(String email, String vCaptcha) throws ArgsNotValidException;
}
