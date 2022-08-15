package com.github.ordinarykai.framework.web.swagger.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger配置属性
 *
 * @author kai
 * @date 2022/3/12 14:02
 */
@Data
@ConfigurationProperties(prefix = "swagger")
@ConditionalOnProperty(prefix = "swagger", name = {"enable"}, havingValue = "true")
public class SwaggerProperties {

    /**
     * 是否开启swagger
     */
    private boolean enable = false;

    /**
     * 接口所在包路径
     */
    private String basePackage;

    /**
     * header中token的名字
     */
    private String tokenName = "token";

    /**
     * 接口文档标题
     */
    private String title = "接口文档";

    /**
     * 接口文档版本
     */
    private String version = "1.0.0";

    /**
     * 接口文档相关人员
     */
    private Contact contact = new Contact("kai", "https://www.yuque.com/ordinarykai", "2115114903@qq.com");

    /**
     * 接口文档描述
     */
    private String description;

    @Data
    @AllArgsConstructor
    public static class Contact{
        /**
         * 作者
         */
        private String name;
        /**
         * 作者站点
         */
        private String url;
        /**
         * 作者email
         */
        private String email;
    }

}
