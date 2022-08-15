package com.github.ordinarykai.framework.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

}
