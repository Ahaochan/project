package moe.ahao.spring.boot.log.thread;

import org.apache.commons.collections4.MapUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;

import java.util.Map;

/**
 * 所有线程池最终都会执行 {@link java.util.concurrent.Executor#execute(Runnable)} 方法
 * 我们通过 AOP 拦截, 将父线程的 MDC 传入 子线程.
 * Logback 在最新的版本中因为性能问题, 不会自动的将 MDC 的内存传给子线程.
 * <p>
 * 需要注意的是, 如果 AOP 拦截失败, 请检查线程池是否在 Spring 容器的管理范围内.
 *
 * @see MDCTaskDecorator
 * @see <a href="https://logback.qos.ch/manual/mdc.html#managedThreads">managedThreads</>
 */
@Aspect
public class MDCThreadPoolExecutorAspect {

    @Around("execution(* java.util.concurrent.Executor.execute(..))")
    // @Around("execution(* java.util.concurrent.ThreadPoolExecutor.execute(..))")
    public Object before(ProceedingJoinPoint pjp) throws Throwable {
        // 1. 获取父线程的 MDC
        Object[] args = pjp.getArgs();
        Runnable oldRunnable = (Runnable) args[0];
        Map<String, String> context = MDC.getCopyOfContextMap();
        if (MapUtils.isEmpty(context)) {
            return pjp.proceed();
        }


        // 2. 将父线程的 MDC 传给子线程
        Runnable newRunnable = () -> {
            MDC.setContextMap(context);
            try {
                oldRunnable.run();
            } finally {
                MDC.clear(); // 清空子线程的 MDC
            }
        };
        args[0] = newRunnable;

        return pjp.proceed(args);
    }
}
