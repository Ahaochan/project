package moe.ahao.unit.test.context;

import moe.ahao.unit.test.annotation.StdUt;
import moe.ahao.unit.test.support.AopUtil;
import moe.ahao.util.commons.lang.reflect.ReflectHelper;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义标准单测SpringRunner
 */
public final class StdUtJunit4SpringRunner extends SpringJUnit4ClassRunner {
    private final Set<String> /* Set<ClassName> */ utClassNames = new HashSet<>();
    private final Map<String, List<Method>> /* Map<ClassName, List<Method>> */ utMethodMap = new HashMap<>();
    private final Map<String, Object> /* Set<ClassName, Class> */ utTargetInstanceMap = new HashMap<>();

    public StdUtJunit4SpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected Object createTest() throws Exception {
        // 1. 通过反射获取单测类的对象
        Object testInstance = super.getTestClass().getOnlyConstructor().newInstance();
        Class<?> beanClass = testInstance.getClass();
        super.getTestContextManager().prepareTestInstance(testInstance);

        // 2. 获取所有被 @StdUt 修饰的方法
        List<Method> stdUtMethods = this.getStdUtMethods(beanClass);

        if (stdUtMethods.size() > 0) {
            String utClassName = beanClass.getName();
            utClassNames.add(utClassName);
            utMethodMap.put(utClassName, stdUtMethods);
            utTargetInstanceMap.put(utClassName, testInstance);

            // 3. 代理对象未指定接口, 使用CGLIB生成代理类
            ProxyFactory proxyFactory = new ProxyFactory();
            proxyFactory.setTarget(testInstance);
            proxyFactory.addAdvice(new StdUtInterceptor(utClassNames, utMethodMap, utTargetInstanceMap));
            return proxyFactory.getProxy();
        }

        return testInstance;
    }

    private List<Method> getStdUtMethods(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(SpringBootTest.class)) {
            return Collections.emptyList();
        }

        RunWith runWith = clazz.getAnnotation(RunWith.class);
        if(runWith == null) {
            return Collections.emptyList();
        }
        if (!runWith.value().equals(StdUtJunit4SpringRunner.class)) {
            return Collections.emptyList();
        }
        // 寻找 @StdUt 标注的方法
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.isAnnotationPresent(StdUt.class))
            .collect(Collectors.toList());
    }

    /**
     * 对标注了@StdUt的单测方法进行拦截
     */
    public static class StdUtInterceptor implements MethodInterceptor {
        private final Set<String> utClassNames;
        private final Map<String, List<Method>> utMethodMap;
        private final Map<String, Object> utTargetInstanceMap;

        public StdUtInterceptor(Set<String> utClassNames, Map<String, List<Method>> utMethodMap, Map<String, Object> utTargetInstanceMap) {
            this.utClassNames = utClassNames;
            this.utMethodMap = utMethodMap;
            this.utTargetInstanceMap = utTargetInstanceMap;
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 1. 如果不是被 @StdUt 修饰的方法, 就不拦截
            Class<?> clazz = invocation.getThis().getClass();
            Method method = invocation.getMethod();
            if (!this.isUtMethod(clazz, method)) {
                return invocation.proceed();
            }

            // 1. 获取目标单测类
            Object target = AopUtil.getTarget(((ReflectiveMethodInvocation) invocation).getProxy());
            // 2. 获取获取单测类的StdUtContext属性, 如果没有就不拦截
            StdUtContext stdUtContext = this.getStdUtContexts(target);
            if(stdUtContext == null) {
                return invocation.proceed();
            }

            System.out.println("标准单元测试开始执行****************************************************************");
            try {
                // 3. 执行单测方法（被拦截方法）
                Object res = invocation.proceed();
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                // 4. 恢复上下文
                stdUtContext.destroy();
                System.out.println("标准单元测试执行完毕****************************************************************");
            }
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
                if(StdUtContext.class.isAssignableFrom(field.getType())) {
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
    }
}
