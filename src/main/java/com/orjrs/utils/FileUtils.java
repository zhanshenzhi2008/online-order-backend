package com.orjrs.utils;

import com.orjrs.common.exception.BusinessException;
import com.orjrs.config.UploadConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtils {
    private final UploadConfig uploadConfig;

    /**
     * 上传文件
     *
     * @param file     文件
     * @param type     业务类型(例如: food, avatar)
     * @return 文件访问路径
     */
    public String upload(MultipartFile file, String type) {
        // 检查文件
        checkFile(file);

        try {
            // 生成文件名
            String fileName = generateFileName(file);

            // 创建目录
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String dirPath = String.format("%s/%s/%s", uploadConfig.getUploadDir(), type, datePath);
            Path uploadPath = Paths.get(dirPath);
            Files.createDirectories(uploadPath);

            // 保存原图
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // 处理图片
            processImage(filePath.toFile(), fileName);

            // 返回文件访问路径
            return String.format("/%s/%s/%s/%s", uploadConfig.getUploadDir(), type, datePath, fileName);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
    }

    /**
     * 检查文件
     */
    private void checkFile(MultipartFile file) {
        // 检查文件大小
        if (file.getSize() > uploadConfig.getMaxSize() * 1024 * 1024) {
            throw new BusinessException("文件大小不能超过" + uploadConfig.getMaxSize() + "MB");
        }

        // 检查文件类型
        String extension = "." + FilenameUtils.getExtension(file.getOriginalFilename());
        boolean allowed = false;
        for (String type : uploadConfig.getAllowedTypes()) {
            if (type.equalsIgnoreCase(extension)) {
                allowed = true;
                break;
            }
        }
        if (!allowed) {
            throw new BusinessException("不支持的文件类型");
        }
    }

    /**
     * 生成文件名
     */
    private String generateFileName(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return UUID.randomUUID().toString().replace("-", "") + "." + extension;
    }

    /**
     * 处理图片
     */
    private void processImage(File imageFile, String fileName) throws IOException {
        // 读取图片
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            return;
        }

        // 添加水印
        if (uploadConfig.isWatermark()) {
            addWatermark(image);
        }

        // 压缩图片
        Thumbnails.of(image)
                .scale(1f)
                .outputQuality((float) uploadConfig.getCompressQuality() / 100)
                .toFile(imageFile);

        // 生成缩略图
        String thumbnailName = "thumb_" + fileName;
        File thumbnailFile = new File(imageFile.getParent(), thumbnailName);
        Thumbnails.of(image)
                .size(uploadConfig.getThumbnailWidth(), uploadConfig.getThumbnailHeight())
                .toFile(thumbnailFile);
    }

    /**
     * 添加水印
     */
    private void addWatermark(BufferedImage image) {
        Graphics2D g = image.createGraphics();
        try {
            // 设置水印文字样式
            g.setFont(new Font("微软雅黑", Font.BOLD, 30));
            g.setColor(new Color(255, 255, 255, 128));

            // 计算水印位置
            FontMetrics metrics = g.getFontMetrics();
            int x = (image.getWidth() - metrics.stringWidth(uploadConfig.getWatermarkText())) / 2;
            int y = image.getHeight() - metrics.getHeight();

            // 绘制水印
            g.drawString(uploadConfig.getWatermarkText(), x, y);
        } finally {
            g.dispose();
        }
    }
} 