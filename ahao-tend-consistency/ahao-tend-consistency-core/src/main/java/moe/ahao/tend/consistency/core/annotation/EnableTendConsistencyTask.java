package moe.ahao.tend.consistency.core.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.*;

/**
 * 启用一致性框架的注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import({EnableTendConsistencyTask.ConsistencyTaskSelector.class})
public @interface EnableTendConsistencyTask {
    /**
     * 导入框架相关配置
     **/
    class ConsistencyTaskSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            return new String[]{ComponentScanConfig.class.getName()};
        }
    }
}
