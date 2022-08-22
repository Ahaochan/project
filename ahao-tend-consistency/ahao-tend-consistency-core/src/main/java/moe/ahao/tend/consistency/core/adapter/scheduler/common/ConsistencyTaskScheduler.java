package moe.ahao.tend.consistency.core.adapter.scheduler.common;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import moe.ahao.tend.consistency.core.manager.TaskScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 一致性框架任务调度器 leader follower都会使用
 **/
@Slf4j
@Component
public class ConsistencyTaskScheduler extends AbstractSingleScheduler{
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    @Autowired
    private TaskScheduleManager taskScheduleManager;

    public ConsistencyTaskScheduler() {
        super("taskScheduler");
    }

    @Override
    protected Runnable task() {
        return this::doStartTaskExecuteEngine;
    }

    /**
     * 启动任务执行引擎
     */
    private void doStartTaskExecuteEngine() {
        try {
            // 启动一致性任务执行引擎，定时的扫描db和本地的rocksdb里的任务，来执行了
            // aop切面里，如果说你提交任务，db写失败，就自动降级写入到rocksdb里去，系统正常在运行
            // 就会自动的去处理本地的rocksdb里的任务
            taskScheduleManager.performanceTask();
        } catch (Exception e) {
            log.error("执行任务时，发生异常", e);
        }
    }

    @Override
    protected int delaySecond() {
        return tendConsistencyConfiguration.getConsistencyTaskExecuteIntervalSeconds();
    }
}
