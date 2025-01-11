package com.orjrs.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付宝工具类
 */
@Slf4j
public class AlipayUtils {

    private static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    private static final String FORMAT = "json";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    /**
     * 创建支付宝客户端
     *
     * @param appId 应用ID
     * @param privateKey 商户私钥
     * @param publicKey 支付宝公钥
     * @param isSandbox 是否沙箱环境
     * @return 支付宝客户端
     */
    public static AlipayClient createAlipayClient(String appId, String privateKey, String publicKey, boolean isSandbox) {
        String gatewayUrl = isSandbox ? "https://openapi.alipaydev.com/gateway.do" : GATEWAY_URL;
        try {
            return new DefaultAlipayClient(gatewayUrl, appId, privateKey, FORMAT, CHARSET, publicKey, SIGN_TYPE);
        } catch (Exception e) {
            log.error("创建支付宝客户端失败", e);
            throw new RuntimeException("创建支付宝客户端失败", e);
        }
    }

    /**
     * 验证支付回调签名
     *
     * @param params 回调参数
     * @param publicKey 支付宝公钥
     * @return 验证结果
     */
    public static boolean verifySign(Map<String, String> params, String publicKey) throws AlipayApiException {
        // 移除sign和sign_type参数
        String sign = params.remove("sign");
        String signType = params.remove("sign_type");

        // 按照字典序排序参数
        String content = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // 验证签名
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA");
        signature.initVerify(getPublicKey(publicKey));
        signature.update(content.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 从字符串加载公钥
     */
    private static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
} 