package moe.ahao.spring.boot.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import moe.ahao.spring.boot.elasticjob.job.capable.ElasticJobListenerCapable;
import moe.ahao.spring.boot.elasticjob.listener.ElasticJobLogListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataFlowJobExample implements DataflowJob<Integer>, ElasticJobListenerCapable {
    private static final Logger logger = LoggerFactory.getLogger(DataFlowJobExample.class);

    @Autowired
    private ElasticJobLogListener listener;

    @Override
	public List<Integer> fetchData(ShardingContext context) {
        List<Integer> data = new ArrayList<>();
        int len = new Random().nextInt() / 10000;
        for (int i = 0; i < len; i++) {
            int number = new Random().nextInt() / 10000;
            data.add(number);
        }
		return data;
	}

	@Override
	public void processData(ShardingContext context, List<Integer> data) {
        for (Integer number : data) {
            List<Integer> primeFactors = resolvePrime(number);
            logger.info(number + " = " + StringUtils.join(primeFactors, "*"));
        }
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
