package moe.ahao.tend.consistency.core.adapter.scheduler.leader;

import moe.ahao.tend.consistency.core.adapter.scheduler.common.AbstractSingleScheduler;

/**
 * leader检测follower是否存活的调度器
 **/
public class FollowerAliveCheckScheduler extends AbstractSingleScheduler {
    public FollowerAliveCheckScheduler(int initDelaySecond, int delaySecond) {
        super("followerActiveScheduler", initDelaySecond, delaySecond);
    }
}
