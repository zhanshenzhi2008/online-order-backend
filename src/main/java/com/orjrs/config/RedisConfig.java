package com.orjrs.config;

import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 */
@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用FastJson2序列化
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        
        // 设置key和value的序列化规则
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(fastJsonRedisSerializer);
        
        // 设置hash key和value的序列化规则
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(fastJsonRedisSerializer);
        
        // 初始化RedisTemplate
        template.afterPropertiesSet();
        
        return template;
    }
} 