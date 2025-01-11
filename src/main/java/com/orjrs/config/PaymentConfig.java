package com.orjrs.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * 支付配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "payment")
public class PaymentConfig {
    /**
     * 微信支付配置
     */
    private WxPay wxPay = new WxPay();

    /**
     * 支付宝配置
     */
    private AliPay aliPay = new AliPay();

    @Data
    public static class WxPay {
        private String appId;
        private String mchId;
        private String apiKey;
        private String privateKey;
        private String privateCertPath;
        private String notifyUrl;
        private String refundNotifyUrl;
    }

    @Data
    public static class AliPay {
        private String appId;
        private String privateKey;
        private String publicKey;
        private String notifyUrl;
        private String refundNotifyUrl;
        private String returnUrl;
        private boolean sandbox = false;
    }

    /**
     * 配置微信支付客户端
     */
    @Bean
    public CloseableHttpClient wxPayClient() throws Exception {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
                new ByteArrayInputStream(wxPay.getPrivateKey().getBytes(StandardCharsets.UTF_8)));

        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
        certificatesManager.putMerchant(wxPay.getMchId(), 
                new WechatPay2Credentials(wxPay.getMchId(), 
                        new PrivateKeySigner(wxPay.getMchId(), merchantPrivateKey)),
                wxPay.getApiKey().getBytes(StandardCharsets.UTF_8));

        // 构造支付客户端
        return WechatPayHttpClientBuilder.create()
                .withMerchant(wxPay.getMchId(), wxPay.getApiKey(), merchantPrivateKey)
                .withValidator(response -> true) // 临时设置为不验证
                .build();
    }

    /**
     * 配置支付宝客户端
     */
    @Bean
    public AlipayClient alipayClient() {
        String serverUrl = aliPay.isSandbox() 
                ? "https://openapi.alipaydev.com/gateway.do" 
                : "https://openapi.alipay.com/gateway.do";
        
        return new DefaultAlipayClient(serverUrl, 
                aliPay.getAppId(),
                aliPay.getPrivateKey(), 
                "json", 
                StandardCharsets.UTF_8.name(),
                aliPay.getPublicKey(), 
                "RSA2");
    }
} 