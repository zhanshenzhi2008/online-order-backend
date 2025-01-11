package com.orjrs.utils;

import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付工具类
 */
@Slf4j
public class WxPayUtils {

    /**
     * 创建微信支付客户端
     *
     * @param mchId 商户号
     * @param apiKey API密钥
     * @param privateKey 商户私钥
     * @param privateCertPath 商户证书路径
     * @return 微信支付客户端
     */
    public static CloseableHttpClient createWxPayClient(String mchId, String apiKey, String privateKey, String privateCertPath) {
        try {
            // 加载商户私钥
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
                    new ByteArrayInputStream(privateKey.getBytes(StandardCharsets.UTF_8))
            );

            // 创建签名器
            PrivateKeySigner signer = new PrivateKeySigner(mchId, merchantPrivateKey);

            // 创建认证器
            WechatPay2Credentials credentials = new WechatPay2Credentials(mchId, signer);

            // 创建证书管理器
            CertificatesManager certificatesManager = CertificatesManager.getInstance();
            certificatesManager.putMerchant(mchId, credentials, apiKey.getBytes(StandardCharsets.UTF_8));

            // 创建支付客户端
            return certificatesManager.getBuilder(mchId).build();
        } catch (Exception e) {
            log.error("创建微信支付客户端失败", e);
            throw new RuntimeException("创建微信支付客户端失败", e);
        }
    }

    /**
     * 生成支付参数
     *
     * @param appId 应用ID
     * @param prepayId 预支付交易会话标识
     * @param privateKey 商户私钥
     * @return 支付参数
     */
    public static Map<String, String> generatePayParams(String appId, String prepayId, PrivateKey privateKey) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = String.valueOf(System.nanoTime());

        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        params.put("timeStamp", timestamp);
        params.put("nonceStr", nonceStr);
        params.put("package", "prepay_id=" + prepayId);
        params.put("signType", "RSA");
        
        // 生成签名
        String message = appId + "\n" +
                timestamp + "\n" +
                nonceStr + "\n" +
                "prepay_id=" + prepayId + "\n";
        
        try {
            java.security.Signature sign = java.security.Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(message.getBytes(StandardCharsets.UTF_8));
            params.put("paySign", java.util.Base64.getEncoder().encodeToString(sign.sign()));
        } catch (Exception e) {
            log.error("生成支付参数签名失败", e);
            throw new RuntimeException("生成支付参数签名失败", e);
        }

        return params;
    }

    /**
     * 验证支付回调签名
     *
     * @param params 回调参数
     * @param apiKey API密钥
     * @return 验证结果
     */
    public static boolean verifySign(Map<String, String> params, String apiKey) {
        // TODO: 实现签名验证
        return true;
    }
} 