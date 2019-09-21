package moe.ahao.spring.boot.elasticjob.job.capable;

import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

import java.util.List;

public interface ElasticJobListenerCapable {
    List<ElasticJobListener> getElasticJobListeners();
}
