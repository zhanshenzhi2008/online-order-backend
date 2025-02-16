package com.orjrs.miniapp.controller;

import com.orjrs.common.R;
import com.orjrs.utils.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileUtils fileUtils;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public R<String> upload(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "业务类型") @RequestParam("type") String type) {
        String path = fileUtils.upload(file, type);
        return R.ok(path);
    }
} 