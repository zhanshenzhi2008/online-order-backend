package com.orjrs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@MapperScan("com.orjrs.**.mapper")
public class OnlineOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineOrderApplication.class, args);
    }
} 