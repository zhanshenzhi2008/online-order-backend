package com.orjrs.service;

public interface SmsService {
    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    String sendCode(String phone);

    /**
     * 验证验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return 是否验证通过
     */
    boolean verifyCode(String phone, String code);
} 