package moe.ahao.spring.boot.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import moe.ahao.spring.boot.elasticjob.job.capable.ElasticJobListenerCapable;
import moe.ahao.spring.boot.elasticjob.listener.ElasticJobLogListener;
import moe.ahao.util.commons.lang.RandomHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SimpleJobExample implements SimpleJob, ElasticJobListenerCapable {
    private static final Logger logger = LoggerFactory.getLogger(SimpleJobExample.class);

    @Autowired
    private ElasticJobLogListener listener;

    @Override
    public void execute(ShardingContext shardingContext) {
        int number = RandomHelper.getInt(10000);
        List<Integer> primeFactors = resolvePrime(number);
        logger.info(number + " = " + StringUtils.join(primeFactors, "*"));
    }

    @Override
    public List<ElasticJobListener> getElasticJobListeners() {
        return Arrays.asList(listener);
    }

    private List<Integer> resolvePrime(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("num: " + num + "必须为正数");
        }

        List<Integer> result = new ArrayList<>();
        int i = 2;
        while (i <= num) {
            if (num % i == 0) {
                result.add(i);
                num = num / i;
                i = 2;
            } else {
                i++;
            }
        }
        return result;
    }
}
