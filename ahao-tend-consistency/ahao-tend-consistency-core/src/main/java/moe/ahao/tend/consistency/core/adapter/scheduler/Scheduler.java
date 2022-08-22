package moe.ahao.tend.consistency.core.adapter.scheduler;

/**
 * 任务调度器接口
 **/
public interface Scheduler {
    /**
     * 创建leader用于检测follower是否存活的调度器
     *
     * @param task 任务task
     * @return 选举超时调度器
     */
    void startFollowerAliveCheckScheduler(Runnable task);

    /**
     * 心跳任务的调度器
     */
    void startLeaderToFollowerHeartbeatScheduler();

    /**
     * 任务执行调度器
     */
    void createConsistencyTaskScheduler();

    /**
     * 创建follower对leader心跳检测的调度器
     */
    void startFollowerHeartbeatScheduler();

    /**
     * 创建follower用于检测leader是否存活的调度器
     */
    void startLeaderAliveScheduler();

    /**
     * 取消所有调度任务
     */
    void cancelAllScheduler();

}
