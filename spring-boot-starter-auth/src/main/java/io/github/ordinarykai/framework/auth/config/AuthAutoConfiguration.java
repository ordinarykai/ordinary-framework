package io.github.ordinarykai.framework.auth.config;


import io.github.ordinarykai.framework.auth.core.AuthInterceptor;
import io.github.ordinarykai.framework.auth.core.InterceptorProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wukai
 * @date 2022/8/8 17:32
 */
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
@ConditionalOnProperty(prefix = "auth", name = {"enable"}, havingValue = "true")
public class AuthAutoConfiguration {

    @Bean
    public AuthInterceptor authInterceptor(AuthProperties authProperties) {
        return new AuthInterceptor(authProperties);
    }

    @Bean
    public InterceptorProcessor interceptorProcessor(AuthInterceptor authInterceptor, AuthProperties authProperties) {
        return new InterceptorProcessor(authInterceptor, authProperties);
    }

}
