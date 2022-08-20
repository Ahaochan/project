package moe.ahao.tend.consistency.core.adapter.scheduler.follower;

import moe.ahao.tend.consistency.core.adapter.scheduler.common.AbstractSingleScheduler;

/**
 * follower用于检测leader是否存活的调度器
 **/
public class LeaderAliveCheckScheduler extends AbstractSingleScheduler {
    public LeaderAliveCheckScheduler(int initDelaySecond, int delaySecond) {
        super("leaderAliveScheduler", initDelaySecond, delaySecond);
    }
}
