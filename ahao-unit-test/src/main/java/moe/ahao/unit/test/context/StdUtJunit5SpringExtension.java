package moe.ahao.unit.test.context;

import moe.ahao.unit.test.annotation.StdUt;
import moe.ahao.util.commons.lang.reflect.ReflectHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义标准单测SpringRunner
 */
public final class StdUtJunit5SpringExtension extends SpringExtension {
    private final Set<String> /* Set<ClassName> */ utClassNames = new HashSet<>();
    private final Map<String, List<Method>> /* Map<ClassName, List<Method>> */ utMethodMap = new HashMap<>();
    private final Map<String, Object> /* Set<ClassName, Class> */ utTargetInstanceMap = new HashMap<>();

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Class<?> beanClass = testInstance.getClass();
        super.postProcessTestInstance(testInstance, context);

        List<Method> stdUtMethods = this.getStdUtMethods(beanClass);

        if (stdUtMethods.size() > 0) {
            String utClassName = beanClass.getName();
            utClassNames.add(utClassName);
            utMethodMap.put(utClassName, stdUtMethods);
            utTargetInstanceMap.put(utClassName, testInstance);
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        super.beforeTestExecution(context);

        // 1. 如果不是被 @StdUt 修饰的方法, 就不拦截
        Class<?> clazz = context.getRequiredTestClass();
        Method method = context.getRequiredTestMethod();
        if (!this.isUtMethod(clazz, method)) {
            return;
        }

        // 1. 获取目标单测类
        Object target = context.getRequiredTestInstance();
        // 2. 获取获取单测类的StdUtContext属性, 如果没有就不拦截
        StdUtContext stdUtContext = this.getStdUtContexts(target);
        if (stdUtContext == null) {
            return;
        }

        System.out.println("标准单元测试开始执行****************************************************************");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        super.afterTestExecution(context);

        // 1. 如果不是被 @StdUt 修饰的方法, 就不拦截
        Class<?> clazz = context.getRequiredTestClass();
        Method method = context.getRequiredTestMethod();
        if (!this.isUtMethod(clazz, method)) {
            return;
        }

        // 1. 获取目标单测类
        Object target = context.getRequiredTestInstance();
        // 2. 获取获取单测类的StdUtContext属性, 如果没有就不拦截
        StdUtContext stdUtContext = this.getStdUtContexts(target);
        if (stdUtContext == null) {
            return;
        }

        // 3. 恢复上下文
        stdUtContext.destroy();
        System.out.println("标准单元测试执行完毕****************************************************************");
    }

    boolean isUtMethod(Class<?> clazz, Method method) {
        String className = clazz.getName();
        List<Method> utMethods = utMethodMap.get(className);
        return utMethods.stream()
            .anyMatch(m -> Objects.equals(m.getName(), method.getName()));
    }

    /**
     * 获取标准单测上下文引用
     */
    private StdUtContext getStdUtContexts(Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        List<StdUtContext> contexts = new ArrayList<>();
        for (Field field : fields) {
            if (StdUtContext.class.isAssignableFrom(field.getType())) {
                StdUtContext context = ReflectHelper.getValue(target, field);
                contexts.add(context);
            }
        }
        if (contexts.size() == 0) {
            return null;
        }
        if (contexts.size() > 1) {
            throw new ArrayIndexOutOfBoundsException("StandardUnitTestContext及其子类在一个单元测试类只能存在一个");
        }
        return contexts.get(0);
    }

    private List<Method> getStdUtMethods(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(SpringBootTest.class)) {
            return Collections.emptyList();
        }

        ExtendWith extendWith = clazz.getAnnotation(ExtendWith.class);
        if (extendWith == null) {
            return Collections.emptyList();
        }
        if (!ArrayUtils.contains(extendWith.value(), StdUtJunit5SpringExtension.class)) {
            return Collections.emptyList();
        }
        // 寻找 @StdUt 标注的方法
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.isAnnotationPresent(StdUt.class))
            .collect(Collectors.toList());
    }
}
