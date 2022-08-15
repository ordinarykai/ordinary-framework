package com.github.ordinarykai.framework.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.ordinarykai.framework.mybatisplus.core.MyMetaObjectHandler;
import com.github.ordinarykai.framework.mybatisplus.core.generator.CodeGenerator;
import com.github.ordinarykai.framework.mybatisplus.core.generator.DatabaseDocGenerator;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus基础配置
 *
 * @author kai
 * @date 2022/3/12 13:18
 */
@Configuration
@EnableConfigurationProperties(MybatisPlusGeneratorProperties.class)
public class MybatisPlusAutoConfiguration {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }

    /**
     * MybatisPlus公共字段插入更新策略
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }

    @Bean
    public CodeGenerator codeGenerator(DataSourceProperties dataSourceProperties,
                                       MybatisPlusGeneratorProperties mybatisPlusGeneratorProperties) {
        return new CodeGenerator(dataSourceProperties, mybatisPlusGeneratorProperties);
    }

    @Bean
    public DatabaseDocGenerator databaseDocGenerator(DataSourceProperties dataSourceProperties,
                                                     MybatisPlusGeneratorProperties mybatisPlusGeneratorProperties) {
        return new DatabaseDocGenerator(dataSourceProperties, mybatisPlusGeneratorProperties);
    }

}
