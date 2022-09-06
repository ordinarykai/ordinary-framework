package io.github.ordinarykai.framework.file.config;

import io.github.ordinarykai.framework.file.core.service.FileService;
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
    public FileService fileService(FileUploadProperties properties) throws ReflectiveOperationException {
        return properties.getService().getConstructor(FileUploadProperties.class).newInstance(properties);
    }

}
