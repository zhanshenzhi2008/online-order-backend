package com.orjrs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "upload")
public class UploadConfig {
    /**
     * 上传目录
     */
    private String uploadDir = "uploads";

    /**
     * 允许的文件类型
     */
    private String[] allowedTypes = {".jpg", ".jpeg", ".png", ".gif"};

    /**
     * 最大文件大小(MB)
     */
    private int maxSize = 5;

    /**
     * 是否添加水印
     */
    private boolean watermark = true;

    /**
     * 水印文字
     */
    private String watermarkText = "在线点餐系统";

    /**
     * 图片压缩质量(1-100)
     */
    private int compressQuality = 75;

    /**
     * 缩略图宽度
     */
    private int thumbnailWidth = 200;

    /**
     * 缩略图高度
     */
    private int thumbnailHeight = 200;
} 