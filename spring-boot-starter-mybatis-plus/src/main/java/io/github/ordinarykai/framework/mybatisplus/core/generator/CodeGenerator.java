package io.github.ordinarykai.framework.mybatisplus.core.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import io.github.ordinarykai.framework.mybatisplus.config.MybatisPlusGeneratorProperties;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * @author kai
 * @date 2022/3/12 14:41
 */
@AllArgsConstructor
public class CodeGenerator {

    private final DataSourceProperties dataSourceProperties;
    private final MybatisPlusGeneratorProperties.CodeGeneratorProperties properties;

    public CodeGenerator(DataSourceProperties dataSourceProperties, MybatisPlusGeneratorProperties generatorProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.properties = generatorProperties.getCode();
    }

    public void execute() {
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
        fastAutoGenerator.globalConfig(builder -> {
                    // 设置作者
                    builder.author(properties.getAuthor())
                            // 开启 swagger 模式
                            .enableSwagger()
                            // 指定输出目录
                            .outputDir(properties.getOutputDir());
                }).packageConfig(builder -> {
                    // 设置父包名
                    builder.parent(properties.getParent())
                            // 设置父包模块名
                            .moduleName(properties.getModuleName());
                }).strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude(properties.getIncludes())
                            // 设置过滤表前缀
                            .addTablePrefix(properties.getTablePrefix())
                            // RestController控制器
                            .controllerBuilder().enableRestStyle()
                            // service名称格式
                            .serviceBuilder().formatServiceFileName("%sService")
                            // mapper加上@Mapper注解
                            .mapperBuilder().mapperAnnotation(Mapper.class)
                            // 开启lombok模型
                            .entityBuilder().enableLombok()
                            // 开启链式模型
                            .enableChainModel()
                            // 生成字段注解
                            .enableTableFieldAnnotation()
                            // 指定生成的主键的ID类型为INPUT
                            .idType(properties.getIdType())
                            // 开启ActiveRecord模式
                            .enableActiveRecord();
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine()).execute();
    }

}