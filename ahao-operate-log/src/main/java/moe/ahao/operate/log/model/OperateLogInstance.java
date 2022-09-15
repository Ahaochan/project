package moe.ahao.operate.log.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 操作日志实例
 * @author zhonghuashishan
 * @version 1.0
 */
@Data
@Builder
public class OperateLogInstance {

    /**
     * 业务no
     */
    private String bizNo;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作日志内容
     */
    private String logContent;

    @Tolerate
    public OperateLogInstance() {

    }

}
