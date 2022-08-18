package io.github.ordinarykai.framework.file.config;

import io.github.ordinarykai.framework.file.core.FileTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author wukai
 * @date 2022/8/11 17:14
 */
@Data
@ConfigurationProperties(prefix = "file-upload")
public class FileUploadProperties {

    /**
     * 物理路径
     */
    private String path;
    /**
     * 网络映射路径
     */
    private String url;

    /**
     * 允许上传的文件类型
     */
    private List<FileTypeEnum> types;

}
