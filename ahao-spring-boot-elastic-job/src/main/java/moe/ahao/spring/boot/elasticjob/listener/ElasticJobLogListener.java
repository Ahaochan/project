package moe.ahao.spring.boot.elasticjob.listener;

import com.ahao.util.commons.lang.BeanHelper;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ElasticJobLogListener implements ElasticJobListener {
    private static final Logger logger = LoggerFactory.getLogger(ElasticJobLogListener.class);


    private Map<String, StopWatch> stopWatchMap = new ConcurrentHashMap<>();

    @Override
    public void beforeJobExecuted(ShardingContexts ctx) {
        String jobName = ctx.getJobName();

        StopWatch stopWatch = stopWatchMap.get(jobName);
        if(stopWatch == null) {
            stopWatch = new StopWatch(jobName);
            stopWatchMap.put(jobName, stopWatch);
        }

        stopWatch.start(jobName);

        logger.info("作业开始执行, Thread ID: {}, " +
                "作业名称: {}, 作业任务ID: {}, " +
                "作业分片总数: {}, 作业自定义参数: {}, 作业分片参数: {}, " +
                "作业事件采样统计数: {}, 当前作业事件采样统计数: {}, 是否允许可以发送作业事件: {}",
            Thread.currentThread().getId(),
            ctx.getJobName(), ctx.getTaskId(),
            ctx.getShardingTotalCount(), ctx.getJobParameter(), BeanHelper.obj2JsonString(ctx.getShardingItemParameters()),
            ctx.getJobEventSamplingCount(), ctx.getCurrentJobEventSamplingCount(), ctx.isAllowSendJobEvent());
    }

    @Override
    public void afterJobExecuted(ShardingContexts ctx) {
        String jobName = ctx.getJobName();

        StopWatch stopWatch = stopWatchMap.remove(jobName);
        if(stopWatch != null) {
            logger.info("作业结束执行, 作业名称: {}, 作业任务ID: {}, {}", ctx.getJobName(), ctx.getTaskId(), stopWatch);
            stopWatch.stop();
        }
    }
}
