package moe.ahao.tend.consistency.core.adapter.scheduler.leader;

import moe.ahao.tend.consistency.core.adapter.scheduler.common.AbstractSingleScheduler;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * leader检测follower是否存活的调度器
 **/
@Component
public class FollowerAliveCheckScheduler extends AbstractSingleScheduler {
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;

    public FollowerAliveCheckScheduler() {
        super("followerActiveScheduler");
    }

    @Override
    protected int initDelaySecond() {
        return 10;
    }

    @Override
    protected int delaySecond() {
        return tendConsistencyConfiguration.getFollowerAliveCheckIntervalSeconds();
    }
}
