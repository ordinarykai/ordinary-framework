package com.github.ordinarykai.framework.web.apilog.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "api-log")
@ConditionalOnProperty(prefix = "api-log", name = {"enable"}, havingValue = "true")
public class ApiLogProperties {

    /**
     * 是否开启api日志，默认false
     */
    private boolean enable = false;
    /**
     * 需要开启api日志的接口前缀集合
     */
    private List<String> prefix;

}
