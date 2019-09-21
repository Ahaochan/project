package moe.ahao.spring.boot.elasticjob.job.capable;

import javax.sql.DataSource;

/**
 * 当 jobEventTraceEnabled 属性为 true 时, 必须实现此接口暴露数据源
 */
public interface JobEventTraceDataSourceCapable {
    DataSource getJobEventTraceDataSource();
}
