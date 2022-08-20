package moe.ahao.tend.consistency.core.utils;

/**
 * 当前线程的标志位
 *
 */
public class ThreadLocalUtil {
    /**
     * 任务表示Action被AOP拦截的时候是不是应该立即执行，不再创建任务
     */
    private static final ThreadLocal<Boolean> FLAG = ThreadLocal.withInitial(() -> false);

    public static void setFlag(boolean flag) {
        FLAG.set(flag);
    }

    public static Boolean getFlag() {
        return FLAG.get();
    }
}
