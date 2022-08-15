package com.github.ordinarykai.framework.file.core.impl;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.github.ordinarykai.framework.file.config.FileUploadProperties;
import com.github.ordinarykai.framework.file.core.FileService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author wukai
 * @date 2022/8/11 17:19
 */
@Data
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileUploadProperties properties;

    @Override
    public String upload(MultipartFile file) {
        // 上传文件存储根目录
        String path = properties.getPath();
        // 上传文件上级目录
        String parentDirectory = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + File.separator;
        String fileName;
        try (InputStream inputStream = file.getInputStream()) {
            // 随机文件名
            fileName = UUID.fastUUID().toString(true) + "." + FileTypeUtil.getType(inputStream);
            // 创建文件并复制上传文件内容
            File targetFile = FileUtil.touch(path + File.separator + parentDirectory + fileName);
            FileUtil.writeFromStream(inputStream, targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getUrl() + File.separator + parentDirectory + fileName;
    }

}
