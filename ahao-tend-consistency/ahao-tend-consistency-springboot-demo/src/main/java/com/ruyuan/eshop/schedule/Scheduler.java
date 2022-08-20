package com.ruyuan.eshop.schedule;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.manager.TaskScheduleManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时任务
 *
 * @author zhonghuashishan
 **/
@Slf4j
@Component
public class Scheduler {

    /**
     * 一致性任务调度器
     */
    @Resource
    private TaskScheduleManager taskScheduleManager;

    /**
     * 对一致性任务进行调度
     * 1、如果使用的是分布式任务调度框架（如：xxl-job或elastic-job）则配置相关策略保证多实例情况下，同一时刻只有一个实例可以调用performanceTask方法
     * 2、如果像我们这里，使用的是spring自带的定时任务，则记的加锁，来保证同一时刻只有一个实例可以调用performanceTask方法，
     */
//    @Scheduled(fixedRate = 5 * 1000L)
//    public void execute() {
//        // TODO 这里记的加分布式锁
//        try {
//            taskScheduleManager.performanceTask();
//        } catch (Exception e) {
//            log.error("一致性任务调度时，发送异常", e);
//        }
//    }

}
