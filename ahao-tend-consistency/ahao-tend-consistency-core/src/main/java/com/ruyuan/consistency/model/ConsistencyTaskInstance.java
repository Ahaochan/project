package com.ruyuan.consistency.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 一致性任务的实例信息
 *
 * @author zhonghuashishan
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsistencyTaskInstance {

    private Long id;
    /**
     * 用户在主接中自定义的task名称/action名称
     */
    private String taskId;
    /**
     * 方法签名：格式：类路径#方法名(参数1的类型,参数2的类型,...参数N的类型)
     */
    private String methodSignName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数的类路径名称
     */
    private String parameterTypes;
    /**
     * 参数 JSON值
     */
    private String taskParameter;
    /**
     * 任务状态 TaskStatusEnum
     */
    private int taskStatus;
    /**
     * 执行间隔默认60s
     */
    private int executeIntervalSec;
    /**
     * 初始化延迟时间
     */
    private int delayTime;
    /**
     * 执行次数
     */
    private int executeTimes;
    /**
     * 执行时间
     */
    private Long executeTime;
    /**
     * 执行的错误信息
     */
    private String errorMsg;
    /**
     * 执行模式
     */
    private Integer performanceWay;
    /**
     * 线程模型
     */
    private Integer threadWay;
    /**
     * 告警表达式
     */
    private String alertExpression;
    /**
     * 告警的动作执行实现类的beanName 需要实现 ConsistencyFrameworkAlerter方法 并注入spring容器
     */
    private String alertActionBeanName;
    /**
     * 降级类的class
     */
    private String fallbackClassName;
    /**
     * 降级失败时的错误信息
     */
    private String fallbackErrorMsg;
    /**
     * 分片键
     */
    private Long shardKey;

    private Date gmtCreate;

    private Date gmtModified;


}
