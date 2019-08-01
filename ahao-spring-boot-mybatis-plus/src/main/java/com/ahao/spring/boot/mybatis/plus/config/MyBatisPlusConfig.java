package com.ahao.spring.boot.mybatis.plus.config;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
@MapperScan("com.ahao.spring.boot.**.mapper")
public class MyBatisPlusConfig {

    /**
     * 自动填充字段, 配合 {@link TableField}
     * @see <a href="https://mp.baomidou.com/guide/auto-fill-metainfo.html">自动填充功能</a>
     */
    @Bean
    public MetaObjectHandler fillField() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.setFieldValByName("createTime", new Date(), metaObject);
                this.setFieldValByName("updateTime", new Date(), metaObject);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                this.setFieldValByName("updateTime", new Date(), metaObject);
            }
        };
    }

    /**
     * 阻止恶意的 SQL 语句执行
     * @see <a href="https://mp.baomidou.com/guide/block-attack-sql-parser.html">攻击 SQL 阻断解析器</a>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        List<ISqlParser> sqlParserList = Arrays.asList(
            new BlockAttackSqlParser() // update 和 delete 必须要有 where
        );

        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    /**
     * SQL执行效率插件
     * @see <a href="https://mp.baomidou.com/guide/performance-analysis-plugin.html">攻击 SQL 阻断解析器</a>
     */
    @Bean
    @Profile({"dev", "test"})
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor interceptor = new PerformanceInterceptor();
        interceptor.setMaxTime(100);
        interceptor.setFormat(false);
        return interceptor;
    }
}
