package io.github.ordinarykai.framework.file.core.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import io.github.ordinarykai.framework.file.config.FileUploadProperties;
import io.github.ordinarykai.framework.file.core.FileTypeEnum;
import io.github.ordinarykai.framework.file.core.FileTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author wukai
 * @date 2022/8/11 17:19
 */
@Data
@AllArgsConstructor
public class LocalFileServiceImpl implements FileService {

    private final FileUploadProperties.LocalFileUploadProperties properties;
    private final List<FileTypeEnum> allowedTypes;

    public LocalFileServiceImpl(FileUploadProperties properties) {
        this.properties = properties.getLocal();
        this.allowedTypes = properties.getTypes();
    }

    @Override
    public String upload(MultipartFile file) {
        // 上传文件存储根目录
        String path = properties.getPath();
        // 上传文件上级目录
        String parentDirectory = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + File.separator;
        try {
            byte[] bytes = IoUtil.readBytes(file.getInputStream());
            String type = FileTypeUtil.allowUpload(allowedTypes, bytes, file.getOriginalFilename());
            // 随机文件名
            String fileName = UUID.fastUUID().toString(true) + "." + type;
            FileUtil.writeBytes(bytes, path + File.separator + parentDirectory + fileName);
            return properties.getUrl() + File.separator + parentDirectory + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
