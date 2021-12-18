package moe.ahao.spring.boot.mybatis.plus.config;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration(proxyBeanMethods = false)
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
                this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
                // this.strictUpdateFill(metaObject, "createTime", Date::new, Date.class);
                // this.fillStrategy(metaObject, "createTime", new Date());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
                this.strictUpdateFill(metaObject, "updateTime", Date::new, Date.class);
                // this.fillStrategy(metaObject, "updateTime", new Date());
            }
        };
    }

    /**
     * @see <a href="https://mp.baomidou.com/guide/interceptor.html">插件主体(必看!)(since 3.4.0)</a>
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        List<InnerInterceptor> innerInterceptors = Arrays.asList(
            // new TenantLineInnerInterceptor(), new DynamicTableNameInnerInterceptor(),
            new PaginationInnerInterceptor(), new OptimisticLockerInnerInterceptor(),
            // new IllegalSQLInnerInterceptor(),
            new BlockAttackInnerInterceptor() // update 和 delete 必须要有 where
        );

        mybatisPlusInterceptor.setInterceptors(innerInterceptors);
        return mybatisPlusInterceptor;
    }

    /**
     * SQL执行效率插件, 该插件 3.2.0 以上版本移除
     * @see <a href="https://mp.baomidou.com/guide/performance-analysis-plugin.html">攻击 SQL 阻断解析器</a>
     * @deprecated <a href="https://mp.baomidou.com/guide/p6spy.html">执行 SQL 分析打印</>
     */
    // @Bean
    // @Profile({"dev", "test"})
    // @Deprecated
    // public PerformanceInterceptor performanceInterceptor() {
    //     PerformanceInterceptor interceptor = new PerformanceInterceptor();
    //     interceptor.setMaxTime(100);
    //     interceptor.setFormat(false);
    //     return interceptor;
    // }
}
