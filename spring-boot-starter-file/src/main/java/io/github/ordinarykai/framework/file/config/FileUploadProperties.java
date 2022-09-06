package io.github.ordinarykai.framework.file.config;

import io.github.ordinarykai.framework.file.core.service.FileService;
import io.github.ordinarykai.framework.file.core.FileTypeEnum;
import io.github.ordinarykai.framework.file.core.service.LocalFileServiceImpl;
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
     * 文件上传组件，默认LocalFileServiceImpl（本地上传）
     */
    private Class<? extends FileService> service = LocalFileServiceImpl.class;
    /**
     * 允许上传的文件类型
     */
    private List<FileTypeEnum> types;
    /**
     * 本地上传配置
     */
    private LocalFileUploadProperties local;
    /**
     * minio上传配置
     */
    private MinioFileUploadProperties minio;

    @Data
    public static class LocalFileUploadProperties {
        /**
         * 物理路径
         */
        private String path;
        /**
         * 网络映射路径
         */
        private String url;
    }

    @Data
    public static class MinioFileUploadProperties {
        /**
         * 是一个URL，域名，IPv4或者IPv6地址
         */
        private String endpoint = "http://127.0.0.1";
        /**
         * TCP/IP端口号
         */
        private Integer port = 9001;
        /**
         * accessKey类似于用户ID，用于唯一标识你的账户
         */
        private String accessKey;
        /**
         * secretKey是你账户的密码
         */
        private String secretKey;
        /**
         * 如果是true，则用的是https而不是http，默认值false
         */
        private boolean secure = false;
        /**
         * 默认存储桶
         */
        private String bucketName;
    }

}
