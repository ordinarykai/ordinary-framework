package com.github.ordinarykai.framework.mybatisplus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * @author wukai
 * @date 2022/8/4 16:53
 */
@Data
@ConfigurationProperties(prefix = "mybatis-plus.generator")
public class MybatisPlusGeneratorProperties {

    /**
     * 代码生成器配置
     */
    private CodeGeneratorProperties code;
    /**
     * 数据库文档生成器配置
     */
    private DatabaseDocGeneratorProperties doc;

    @Data
    public static class CodeGeneratorProperties {
        /**
         * 作者
         */
        private String author;
        /**
         * 输出目录
         */
        private String outputDir;
        /**
         * 上级包路径，比如 com.github.ordinarykai
         */
        private String parent;
        /**
         * 模块名
         */
        private String moduleName;
        /**
         * 指定表前缀
         */
        private List<String> tablePrefix = Collections.emptyList();
        /**
         * 指定表
         */
        private List<String> includes = Collections.emptyList();
    }

    @Data
    public static class DatabaseDocGeneratorProperties {
        /**
         * 输出目录
         */
        private String outputDir;
        /**
         * 文件名
         */
        private String fileName;
        /**
         * 版本
         */
        private String version;
        /**
         * 描述
         */
        private String description;
    }

}
