package moe.ahao.process.engine.core.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProcessorUtils {
    private static final ExecutorService DEFAULT_POOL = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        60, TimeUnit.MICROSECONDS, new SynchronousQueue<>());
    public static void executeAsync(Runnable runnable) {
        DEFAULT_POOL.execute(runnable);
    }

}
