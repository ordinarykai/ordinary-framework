package com.github.ordinarykai.framework.web.swagger.config;

import com.github.ordinarykai.framework.web.swagger.core.SpringFoxHandlerProviderBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 * Swagger自动配置类
 *
 * @author kai
 * @date 2022/3/12 14:02
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(prefix = "swagger", name = {"enable"}, havingValue = "true")
public class SwaggerAutoConfiguration {

    @Bean
    public SpringFoxHandlerProviderBeanPostProcessor springFoxHandlerProviderBeanPostProcessor() {
        return new SpringFoxHandlerProviderBeanPostProcessor();
    }

    @Bean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
        return new Docket(DocumentationType.OAS_30)
                // 是否开启swagger接口文档
                .enable(swaggerProperties.isEnable())
                // 接口文档信息
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                // 要展示的接口所在包路径
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes(swaggerProperties.getTokenName()))
                .securityContexts(securityContexts(swaggerProperties.getTokenName()));
    }

    /**
     * API 摘要信息
     */
    @Bean
    public ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        SwaggerProperties.Contact contact = swaggerProperties.getContact();
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

    /**
     * 安全模式，这里配置通过请求头 Authorization 传递 token 参数
     */
    private List<SecurityScheme> securitySchemes(String tokenName) {
        return Collections.singletonList(new ApiKey(tokenName, tokenName, "header"));
    }

    private List<SecurityContext> securityContexts(String tokenName) {
        return Collections.singletonList(SecurityContext.builder()
                .securityReferences(securityReferences(tokenName))
                .build());
    }

    private List<SecurityReference> securityReferences(String tokenName) {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")};
        return Collections.singletonList(new SecurityReference(tokenName, authorizationScopes));
    }

}
