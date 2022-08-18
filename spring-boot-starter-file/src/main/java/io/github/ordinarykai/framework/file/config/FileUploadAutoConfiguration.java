package io.github.ordinarykai.framework.file.config;

import io.github.ordinarykai.framework.file.core.FileService;
import io.github.ordinarykai.framework.file.core.impl.FileServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wukai
 * @date 2022/8/11 17:14
 */
@Configuration
@EnableConfigurationProperties(FileUploadProperties.class)
public class FileUploadAutoConfiguration {

    @Bean
    public FileService FileService(FileUploadProperties properties) {
        return new FileServiceImpl(properties);
    }

}
