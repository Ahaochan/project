package moe.ahao.tend.consistency.core.adapter.scheduler.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSingleScheduler {
    /**
     * 内部的调度线程
     */
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> scheduledFuture;

    public AbstractSingleScheduler(String threadName) {
        // TODO 懒加载
        this.scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(r -> new Thread(r, threadName));
    }


    /**
     * 开启调度任务
     */
    public void start(Runnable runnable) {
        this.cancel();
        this.scheduledFuture = scheduledExecutorService
            .scheduleWithFixedDelay(runnable, this.initDelaySecond(), this.delaySecond(), TimeUnit.SECONDS);
    }

    /**
     * 开启调度任务
     */
    public void start() {
        this.cancel();
        this.scheduledFuture = scheduledExecutorService
            .scheduleWithFixedDelay(this.task(), this.initDelaySecond(), this.delaySecond(), TimeUnit.SECONDS);
    }

    protected Runnable task() {
        return () -> {};
    }

    protected int initDelaySecond() {
        return 0;
    }

    protected int delaySecond() {
        return 10;
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
