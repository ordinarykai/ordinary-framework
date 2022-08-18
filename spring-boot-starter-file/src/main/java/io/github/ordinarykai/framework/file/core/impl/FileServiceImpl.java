package io.github.ordinarykai.framework.file.core.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import io.github.ordinarykai.framework.file.config.FileUploadProperties;
import io.github.ordinarykai.framework.file.core.FileService;
import io.github.ordinarykai.framework.file.core.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
        try {
            byte[] bytes = IoUtil.readBytes(file.getInputStream());
            // 随机文件名
            String type = FileTypeUtil.getType(new ByteArrayInputStream(bytes), file.getOriginalFilename());
            if (StrUtil.isBlank(type)) {
                throw new RuntimeException("Unknown file type");
            }
            if (CollectionUtil.isNotEmpty(properties.getTypes()) && !properties.getTypes().contains(FileTypeEnum.getByType(type))) {
                throw new RuntimeException("The file type is not be allowed. Allowed file types are " + properties.getTypes());
            }
            String fileName = UUID.fastUUID().toString(true) + "." + type;
            FileUtil.writeBytes(bytes, path + File.separator + parentDirectory + fileName);
            return properties.getUrl() + File.separator + parentDirectory + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
