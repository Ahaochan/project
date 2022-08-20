package moe.ahao.tend.consistency.core.adapter.scheduler.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSingleScheduler {
    /**
     * 内部的调度线程
     */
    private final ScheduledExecutorService scheduledExecutorService;
    private final int initDelaySecond;
    private final int delaySecond;

    private ScheduledFuture<?> scheduledFuture;

    public AbstractSingleScheduler(String threadName, int initDelaySecond, int delaySecond) {
        this.scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(r -> new Thread(r, threadName));
        this.initDelaySecond = initDelaySecond;
        this.delaySecond = delaySecond;
    }

    /**
     * 开启调度任务
     */
    public void start(Runnable runnable) {
        this.cancel();

        this.scheduledFuture = scheduledExecutorService
            .scheduleWithFixedDelay(runnable, initDelaySecond, delaySecond, TimeUnit.SECONDS);
    }

    /**
     * 开启调度任务
     */
    public void start() {
        this.cancel();
        this.scheduledFuture = scheduledExecutorService
            .scheduleWithFixedDelay(this.task(), initDelaySecond, delaySecond, TimeUnit.SECONDS);
    }

    protected Runnable task() {
        return () -> {};
    }

    /**
     * 取消调度任务
     */
    public void cancel() {
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(false);
        }
    }
}
