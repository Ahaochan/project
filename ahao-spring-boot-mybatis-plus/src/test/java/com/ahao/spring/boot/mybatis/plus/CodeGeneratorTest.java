package com.ahao.spring.boot.mybatis.plus;

import com.ahao.domain.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {DataSourceAutoConfiguration.class})
@ActiveProfiles("mysql")
public class CodeGeneratorTest {

    @Autowired
    private DataSourceProperties properties;

    @Test
    public void generator() {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        String[] tableName = {"user"}; // show table status WHERE 1=1 AND NAME IN ('cl_transfer_queue_fail')

        // 1. 数据源配置 https://mp.baomidou.com/config/generator-config.html#%E6%95%B0%E6%8D%AE%E6%BA%90-datasourceconfig-%E9%85%8D%E7%BD%AE
        DataSourceConfig dataSourceConfig = initDataSourceConfig(properties);
        mpg.setDataSource(dataSourceConfig);
        // 2. 数据库表配置 https://mp.baomidou.com/config/generator-config.html#%E6%95%B0%E6%8D%AE%E5%BA%93%E8%A1%A8%E9%85%8D%E7%BD%AE
        StrategyConfig strategyConfig = initStrategyConfig(tableName);
        mpg.setStrategy(strategyConfig);
        // 3. 包名配置 https://mp.baomidou.com/config/generator-config.html#%E5%8C%85%E5%90%8D%E9%85%8D%E7%BD%AE
        PackageConfig packageConfig = initPackageConfig("moe.ahao.spring.boot.mybatis.plus", "module");
        mpg.setPackageInfo(packageConfig);
        // 4. 模板配置 https://mp.baomidou.com/config/generator-config.html#%E6%A8%A1%E6%9D%BF%E9%85%8D%E7%BD%AE
        TemplateConfig templateConfig = initTemplateConfig();
        mpg.setTemplate(templateConfig);
        // 5. 全局配置 https://mp.baomidou.com/config/generator-config.html#%E5%85%A8%E5%B1%80%E7%AD%96%E7%95%A5-globalconfig-%E9%85%8D%E7%BD%AE
        GlobalConfig globalConfig = initGlobalConfig();
        mpg.setGlobalConfig(globalConfig);
        // 6. 注入配置 https://mp.baomidou.com/config/generator-config.html#%E6%B3%A8%E5%85%A5-injectionconfig-%E9%85%8D%E7%BD%AE
        InjectionConfig injectionConfig = initInjectionConfig();
        mpg.setCfg(injectionConfig);


        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    private GlobalConfig initGlobalConfig() {
        String projectPath = System.getProperty("user.dir");
        GlobalConfig config = new GlobalConfig()
            .setOutputDir(projectPath + StringUtils.replaceChars("/src/main/java", File.separatorChar, '/'))          // 生成文件的输出目录
            .setFileOverride(true)                  // 是否覆盖已有文件
            .setOpen(false)                         // 是否打开输出目录
            .setEnableCache(false)                  // 是否在xml中添加二级缓存配置
            .setKotlin(false)                       // 开启 Kotlin 模式
            .setSwagger2(true)                      // 开启 swagger2 模式
            .setActiveRecord(false)                 // 开启 ActiveRecord 模式
            .setBaseResultMap(false)                // 开启 BaseResultMap
            .setBaseColumnList(false)               // 开启 baseColumnList
            .setDateType(DateType.ONLY_DATE);       // 时间类型对应策略

        config
            .setEntityName("%sEntity")              // 实体命名方式
            .setMapperName("%sMapper")              // mapper 命名方式
            .setXmlName("%sMapper")                 // Mapper xml 命名方式
            .setServiceName("%sService")            // service 命名方式
            .setServiceImplName("%sServiceImpl")    // service impl 命名方式
            .setControllerName("%sController");      // controller 命名方式

        config.setIdType(IdType.AUTO);
        return config;
    }
    private DataSourceConfig initDataSourceConfig(DataSourceProperties properties) {
        DataSourceConfig config = new DataSourceConfig();
        config.setSchemaName("");
        config.setUrl(properties.getUrl());
        config.setDriverName(properties.getDriverClassName());
        config.setUsername(properties.getUsername());
        config.setPassword(properties.getPassword());
        return config;
    }
    private StrategyConfig initStrategyConfig(String... tableName) {
        StrategyConfig config = new StrategyConfig()
            .setCapitalMode(false) // 是否大写命名
            .setSkipView(false)    // 是否跳过视图
            .setNaming(NamingStrategy.underline_to_camel)       // 数据库表映射到实体的命名策略
            .setColumnNaming(NamingStrategy.underline_to_camel);// 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行

        config
            .setSuperEntityClass(BaseDO.class, NamingStrategy.underline_to_camel)
            .setEntityBooleanColumnRemoveIsPrefix(true)     // Boolean类型字段是否移除is前缀
            .setEntityBuilderModel(false)                   // 是否为构建者模型
            .setEntityColumnConstant(false)                 // 是否生成字段常量
            .setEntityLombokModel(false)                    // 是否为lombok模型
            .setEntitySerialVersionUID(true)                // 实体是否生成 serialVersionUID
            .setEntityTableFieldAnnotationEnable(false);    // 是否生成实体时，生成字段注解

        config
            .setRestControllerStyle(true)           // 生成 @RestController 控制器
            .setControllerMappingHyphenStyle(true); // 驼峰转连字符

        config
            .setTablePrefix("") // 表前缀
            .setFieldPrefix("") // 字段前缀
            .setInclude(tableName)
            .setVersionFieldName("version")         // 乐观锁属性名称
            .setLogicDeleteFieldName("is_deleted")  // 逻辑删除属性名称
            .setTableFillList(Collections.emptyList()); // 表填充字段
        return config;
    }
    private PackageConfig initPackageConfig(String parentPackageName, String moduleName) {
        PackageConfig config = new PackageConfig()
            .setParent(parentPackageName)
            .setModuleName(moduleName);

        config.setEntity("entity")
            .setService("service")
            .setServiceImpl("service.impl")
            .setMapper("mapper")
            .setXml("mapper")
            .setController("controller");

        return config;
    }
    private TemplateConfig initTemplateConfig() {
        String prefix = "/templates/freemarker/";
        TemplateConfig config = new TemplateConfig();
        config.setEntity(prefix + "entity.java");
        config.setEntityKt(prefix + "entity.kt");
        config.setService(prefix + "service.java");
        config.setServiceImpl(prefix + "serviceImpl.java");
        config.setMapper(prefix + "mapper.java");
        config.setXml(prefix + "mapper.xml");
        config.setController(prefix + "controller.java");
        return config;
    }
    private InjectionConfig initInjectionConfig() {
        String prefix = "/templates/freemarker/";
        InjectionConfig config = new InjectionConfig() {
            @Override
            public void initMap() {
                setMap(new HashMap<String, Object>() {{
                    put("key", "value"); // 注入自定义属性
                }});
            }
        };

        // FileOutConfig xmlFileOutConfig = new FileOutConfig(prefix + "mapper.xml.ftl") {
        //     @Override
        //     public String outputFile(TableInfo tableInfo) {
        //         String projectPath = System.getProperty("user.dir");
        //         return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
        //     }
        // };
        // config.setFileOutConfigList(Arrays.asList(xmlFileOutConfig));
        return config;
    }
}
