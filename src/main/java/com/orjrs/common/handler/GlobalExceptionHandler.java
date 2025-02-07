package com.orjrs.common.handler;

import com.orjrs.common.R;
import com.orjrs.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getFieldError().getDefaultMessage();
        log.error("参数校验异常：{}", message);
        return R.error(message);
    }

    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getFieldError().getDefaultMessage();
        log.error("参数绑定异常：{}", message);
        return R.error(message);
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return R.error("系统异常，请联系管理员");
    }
} 