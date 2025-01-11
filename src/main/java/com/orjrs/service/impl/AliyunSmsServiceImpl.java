package com.orjrs.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.orjrs.common.exception.BusinessException;
import com.orjrs.config.SmsConfig;
import com.orjrs.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@ConditionalOnProperty(name = "sms.provider", havingValue = "aliyun")
public class AliyunSmsServiceImpl implements SmsService {
    private final SmsConfig smsConfig;
    private final RedisTemplate<String, Object> redisTemplate;
    private final Client client;

    public AliyunSmsServiceImpl(SmsConfig smsConfig, RedisTemplate<String, Object> redisTemplate) throws Exception {
        this.smsConfig = smsConfig;
        this.redisTemplate = redisTemplate;

        // 初始化阿里云短信客户端
        Config config = new Config()
                .setAccessKeyId(smsConfig.getAccessKeyId())
                .setAccessKeySecret(smsConfig.getAccessKeySecret())
                .setEndpoint("dysmsapi.aliyuncs.com");
        this.client = new Client(config);
    }

    @Override
    public String sendCode(String phone) {
        // 检查发送频率
        checkSendFrequency(phone);

        // 生成验证码
        String code = generateCode();

        try {
            // 构建请求
            SendSmsRequest request = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(smsConfig.getSignName())
                    .setTemplateCode(smsConfig.getTemplateId())
                    .setTemplateParam("{\"code\":\"" + code + "\"}");

            // 发送短信
            SendSmsResponse response = client.sendSms(request);
            if (!"OK".equals(response.getBody().getCode())) {
                log.error("短信发送失败：{}", response.getBody().getMessage());
                throw new BusinessException("短信发送失败");
            }

            // 保存验证码
            String key = "sms:code:" + phone;
            redisTemplate.opsForValue().set(key, code, smsConfig.getExpireTime(), TimeUnit.SECONDS);

            // 更新发送记录
            updateSendRecord(phone);

            return code;
        } catch (Exception e) {
            log.error("短信发送异常", e);
            throw new BusinessException("短信发送失败");
        }
    }

    @Override
    public boolean verifyCode(String phone, String code) {
        String key = "sms:code:" + phone;
        String savedCode = (String) redisTemplate.opsForValue().get(key);
        if (savedCode != null && savedCode.equals(code)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    /**
     * 检查发送频率
     */
    private void checkSendFrequency(String phone) {
        // 检查发送间隔
        String lastSendKey = "sms:last_send:" + phone;
        String lastSend = (String) redisTemplate.opsForValue().get(lastSendKey);
        if (lastSend != null) {
            throw new BusinessException(smsConfig.getInterval() + "秒内不能重复发送");
        }

        // 检查每日发送次数
        String dailyKey = "sms:daily:" + phone + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Integer count = (Integer) redisTemplate.opsForValue().get(dailyKey);
        if (count != null && count >= smsConfig.getMaxDaily()) {
            throw new BusinessException("今日发送次数已达上限");
        }
    }

    /**
     * 更新发送记录
     */
    private void updateSendRecord(String phone) {
        // 记录最后发送时间
        String lastSendKey = "sms:last_send:" + phone;
        redisTemplate.opsForValue().set(lastSendKey, "1", smsConfig.getInterval(), TimeUnit.SECONDS);

        // 更新每日发送次数
        String dailyKey = "sms:daily:" + phone + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        redisTemplate.opsForValue().increment(dailyKey);
        redisTemplate.expire(dailyKey, 24, TimeUnit.HOURS);
    }

    /**
     * 生成验证码
     */
    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < smsConfig.getCodeLength(); i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 