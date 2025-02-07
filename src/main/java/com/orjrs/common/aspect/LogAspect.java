package com.orjrs.common.aspect;

import com.alibaba.fastjson.JSON;
import com.orjrs.common.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Before("@annotation(logAnnotation)")
    public void doBefore(JoinPoint joinPoint, Log logAnnotation) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            
            // 记录请求信息
            log.info("=================请求开始=================");
            log.info("请求模块：{}", logAnnotation.title());
            log.info("请求方法：{}", logAnnotation.action());
            log.info("请求URL：{}", request.getRequestURL().toString());
            log.info("请求方式：{}", request.getMethod());
            log.info("请求IP：{}", request.getRemoteAddr());
            log.info("请求方法：{}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            
            if (logAnnotation.isSaveRequestData()) {
                log.info("请求参数：{}", Arrays.toString(joinPoint.getArgs()));
            }
        }
    }

    @AfterReturning(pointcut = "@annotation(logAnnotation)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Log logAnnotation, Object result) {
        if (logAnnotation.isSaveResponseData()) {
            log.info("返回参数：{}", JSON.toJSONString(result));
        }
        log.info("=================请求结束=================");
    }

    @AfterThrowing(pointcut = "@annotation(logAnnotation)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log logAnnotation, Exception e) {
        log.error("发生异常：", e);
        log.info("=================请求结束=================");
    }
} 