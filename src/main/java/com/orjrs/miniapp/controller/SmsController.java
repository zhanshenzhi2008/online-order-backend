package com.orjrs.miniapp.controller;

import com.orjrs.common.R;
import com.orjrs.miniapp.service.SmsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "短信服务")
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @Operation(summary = "发送验证码")
    @PostMapping("/code")
    public R<String> sendCode(@Parameter(description = "手机号") @RequestParam String phone) {
        String code = smsService.sendCode(phone);
        return R.ok(code);
    }

    @Operation(summary = "验证验证码")
    @PostMapping("/verify")
    public R<Boolean> verifyCode(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "验证码") @RequestParam String code) {
        boolean verified = smsService.verifyCode(phone, code);
        return R.ok(verified);
    }
} 