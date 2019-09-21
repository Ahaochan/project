package moe.ahao.spring.boot.elasticjob.handler;

import com.dangdang.ddframe.job.executor.handler.ExecutorServiceHandler;
import com.dangdang.ddframe.job.util.concurrent.ExecutorServiceObject;

import java.util.concurrent.ExecutorService;

public class LogExecutorServiceHandler implements ExecutorServiceHandler {

    @Override
    public ExecutorService createExecutorService(String jobName) {
        return new ExecutorServiceObject("inner-job-" + jobName, Runtime.getRuntime().availableProcessors() * 2).createExecutorService();
    }
}
