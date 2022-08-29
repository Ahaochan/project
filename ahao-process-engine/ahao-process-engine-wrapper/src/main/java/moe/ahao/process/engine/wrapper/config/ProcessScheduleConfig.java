package moe.ahao.process.engine.wrapper.config;

import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.wrapper.ProcessContextFactory;
import moe.ahao.process.engine.wrapper.schedule.ProcessPlaybackTask;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

public class ProcessScheduleConfig {
    @Bean
    @ConditionalOnBean({ProcessStateStore.class, RedissonClient.class})
    public ProcessPlaybackTask processPlaybackTask(ProcessContextFactory processContextFactory, ProcessStateStore processStateStores, RedissonClient redissonClient) {
        return new ProcessPlaybackTask(processContextFactory, processStateStores, redissonClient);
    }
}
