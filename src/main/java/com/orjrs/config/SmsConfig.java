package com.orjrs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {
    /**
     * 短信服务商(aliyun, tencent)
     */
    private String provider = "aliyun";

    /**
     * 访问密钥ID
     */
    private String accessKeyId;

    /**
     * 访问密钥密码
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板ID
     */
    private String templateId;

    /**
     * 验证码长度
     */
    private int codeLength = 6;

    /**
     * 验证码有效期(秒)
     */
    private int expireTime = 300;

    /**
     * 同一手机号发送间隔(秒)
     */
    private int interval = 60;

    /**
     * 同一手机号每日最大发送次数
     */
    private int maxDaily = 10;
} 