package com.github.ordinarykai.framework.web.apilog.config;

import com.github.ordinarykai.framework.web.apilog.core.filter.ApiLogFilter;
import com.github.ordinarykai.framework.web.apilog.core.service.ApiLogService;
import com.github.ordinarykai.framework.web.apilog.core.service.ApiLogServiceImpl;
import com.github.ordinarykai.framework.web.web.config.WebAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AutoConfigureAfter(WebAutoConfiguration.class)
@EnableConfigurationProperties(ApiLogProperties.class)
@ConditionalOnProperty(prefix = "api-log", name = {"enable"}, havingValue = "true")
public class ApiLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ApiLogService apiLogService(RestTemplate restTemplate, ApiLogProperties apiLogProperties) {
        return new ApiLogServiceImpl(restTemplate, apiLogProperties.getUrl());
    }

    /**
     * 创建 ApiAccessLogFilter Bean，记录 API 请求日志
     */
    @Bean
    public FilterRegistrationBean<ApiLogFilter> apiAccessLogFilter(ApiLogProperties apiLogProperties, ApiLogService apiLogService) {
        ApiLogFilter filter = new ApiLogFilter(apiLogProperties.getPrefix(), apiLogService);
        FilterRegistrationBean<ApiLogFilter> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(0);
        return bean;
    }

}
