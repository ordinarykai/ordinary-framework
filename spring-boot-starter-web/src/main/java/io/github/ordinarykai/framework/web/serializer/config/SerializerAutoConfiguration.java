package io.github.ordinarykai.framework.web.serializer.config;

import io.github.ordinarykai.framework.web.serializer.core.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kai
 * @date 2022/3/12 14:02
 */
@Configuration
public class SerializerAutoConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeSerializer() {
        return new LocalDateTimeSerializer();
    }

}
