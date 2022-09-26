package moe.ahao.operate.log.model;

import lombok.Data;

/**
 * 操作日志实例
 */
@Data
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
}
