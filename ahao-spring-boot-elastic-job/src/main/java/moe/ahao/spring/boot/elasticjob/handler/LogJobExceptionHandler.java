package moe.ahao.spring.boot.elasticjob.handler;

import com.dangdang.ddframe.job.executor.handler.JobExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogJobExceptionHandler implements JobExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(LogJobExceptionHandler.class);

    @Override
    public void handleException(String jobName, Throwable cause) {
        logger.error(String.format("Job '%s' exception occur in job processing", jobName), cause);
    }

}
