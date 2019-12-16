package moe.ahao.spring.boot.log.thread;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;

import java.util.Map;

/**
 * 所有线程池最终都会执行 {@link java.util.concurrent.Executor#execute(Runnable)} 方法
 * 我们通过 AOP 拦截, 将父线程的 MDC 传入 子线程.
 * Logback 在最新的版本中因为性能问题, 不会自动的将 MDC 的内存传给子线程。
 *
 * @see <a href="https://logback.qos.ch/manual/mdc.html#managedThreads">managedThreads</>
 */
@Aspect
public class MDCThreadPoolExecutorAspect {

    @Before("execution(* java.util.concurrent.Executor.execute(Runnable))")
    public void before(JoinPoint jp) {
        // 1. 获取父线程的 MDC
        Object[] args = jp.getArgs();
        Runnable oldRunnable = (Runnable) args[0];
        Map<String, String> context = MDC.getCopyOfContextMap();

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

    }
}
