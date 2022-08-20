package moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.mapper;

import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data.ConsistencyTaskInstance;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 一致性任务数据存储接口
 **/
@Mapper
@Repository
public interface TaskStoreMapper {

    /**
     * 保存最终一致性任务实例
     *
     * @param consistencyTaskInstance 要存储的最终一致性任务的实例信息
     * @return 存储结果
     */
    @Insert("INSERT INTO ruyuan_tend_consistency_task("
                + "task_id,"
                + "task_status,"
                + "execute_times,"
                + "execute_time,"
                + "parameter_types,"
                + "method_name,"
                + "method_sign_name,"
                + "execute_interval_sec,"
                + "delay_time,"
                + "task_parameter,"
                + "performance_way,"
                + "thread_way,"
                + "error_msg,"
                + "alert_expression,"
                + "alert_action_bean_name,"
                + "fallback_class_name,"
                + "fallback_error_msg,"
                + "shard_key,"
                + "gmt_create,"
                + "gmt_modified"
            + ") VALUES("
                + "#{taskId},"
                + "#{taskStatus},"
                + "#{executeTimes},"
                + "#{executeTime},"
                + "#{parameterTypes},"
                + "#{methodName},"
                + "#{methodSignName},"
                + "#{executeIntervalSec},"
                + "#{delayTime},"
                + "#{taskParameter},"
                + "#{performanceWay},"
                + "#{threadWay},"
                + "#{errorMsg},"
                + "#{alertExpression},"
                + "#{alertActionBeanName},"
                + "#{fallbackClassName},"
                + "#{fallbackErrorMsg},"
                + "#{shardKey},"
                + "#{gmtCreate},"
                + "#{gmtModified}"
            + ")")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    Long initTask(ConsistencyTaskInstance consistencyTaskInstance);

    /**
     * 根据id获取任务实例信息
     *
     * @param id       任务id
     * @param shardKey 任务分片键
     * @return 任务实例信息
     */
    @Select("SELECT " +
            "id,task_id,task_status,execute_times,execute_time,parameter_types,method_name,method_sign_name, " +
            "execute_interval_sec,delay_time,task_parameter,performance_way," +
            "thread_way, error_msg, alert_expression, " +
            "alert_action_bean_name, fallback_class_name, fallback_error_msg,shard_key," +
            "gmt_create, gmt_modified " +
            "FROM ruyuan_tend_consistency_task " +
            "where " +
            "id = #{id} AND shard_key = #{shardKey}")
    @Results({
            @Result(column = "id", property = "id", id = true),
            @Result(column = "task_id", property = "taskId"),
            @Result(column = "task_status", property = "taskStatus"),
            @Result(column = "execute_times", property = "executeTimes"),
            @Result(column = "execute_time", property = "executeTime"),
            @Result(column = "parameter_types", property = "parameterTypes"),
            @Result(column = "method_name", property = "methodName"),
            @Result(column = "method_sign_name", property = "methodSignName"),
            @Result(column = "execute_interval_sec", property = "executeIntervalSec"),
            @Result(column = "delay_time", property = "delayTime"),
            @Result(column = "bean_class_name", property = "beanClassName"),
            @Result(column = "task_parameter", property = "taskParameter"),
            @Result(column = "performance_way", property = "performanceWay"),
            @Result(column = "thread_way", property = "threadWay"),
            @Result(column = "error_msg", property = "errorMsg"),
            @Result(column = "alert_expression", property = "alertExpression"),
            @Result(column = "alert_action_bean_name", property = "alertActionBeanName"),
            @Result(column = "fallback_class_name", property = "fallbackClassName"),
            @Result(column = "fallback_error_msg", property = "fallbackErrorMsg"),
            @Result(column = "shard_key", property = "shardKey"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    ConsistencyTaskInstance getTaskByIdAndShardKey(@Param("id") Long id, @Param("shardKey") Long shardKey);

    /**
     * 获取未完成的任务
     *
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param limitTaskCount 每次查询限制的条数
     * @return 获取未完成的任务
     */
    @Select("SELECT " +
            "id,task_id,task_status,execute_times,execute_time,parameter_types,method_name,method_sign_name, " +
            "execute_interval_sec,delay_time,task_parameter,performance_way," +
            "thread_way, error_msg, alert_expression, " +
            "alert_action_bean_name, fallback_class_name, fallback_error_msg,shard_key," +
            "gmt_create, gmt_modified " +
            "FROM ruyuan_tend_consistency_task " +
            "WHERE " +
            "task_status <= 2 " +
            "AND execute_time>=#{startTime} AND execute_time<=#{endTime} " +
            "order by execute_time desc " +
            "LIMIT #{limitTaskCount}")
    @Results({
            @Result(column = "id", property = "id", id = true),
            @Result(column = "task_id", property = "taskId"),
            @Result(column = "task_status", property = "taskStatus"),
            @Result(column = "execute_times", property = "executeTimes"),
            @Result(column = "execute_time", property = "executeTime"),
            @Result(column = "parameter_types", property = "parameterTypes"),
            @Result(column = "method_name", property = "methodName"),
            @Result(column = "method_sign_name", property = "methodSignName"),
            @Result(column = "execute_interval_sec", property = "executeIntervalSec"),
            @Result(column = "delay_time", property = "delayTime"),
            @Result(column = "task_parameter", property = "taskParameter"),
            @Result(column = "performance_way", property = "performanceWay"),
            @Result(column = "thread_way", property = "threadWay"),
            @Result(column = "error_msg", property = "errorMsg"),
            @Result(column = "alert_expression", property = "alertExpression"),
            @Result(column = "alert_action_bean_name", property = "alertActionBeanName"),
            @Result(column = "fallback_class_name", property = "fallbackClassName"),
            @Result(column = "fallback_error_msg", property = "fallbackErrorMsg"),
            @Result(column = "shard_key", property = "shardKey"),
            @Result(column = "gmt_create", property = "gmtCreate"),
            @Result(column = "gmt_modified", property = "gmtModified")
    })
    List<ConsistencyTaskInstance> listByUnFinishTask(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("limitTaskCount") Long limitTaskCount);

    /**
     * 启动任务
     *  本次优化去掉了 and task_status!=1 的查询条件 之前是为了防止，多线程情况下，任务被抢占重复执行。
     *  本次优化加入了任务分片执行，同一时间，可以保证，只有一个实例，在运行该一致性任务。
     * @param consistencyTaskInstance 任务实例信息
     * @return 启动任务的结果
     */
    @Update("UPDATE "
            + "ruyuan_tend_consistency_task "
            + "SET "
            + "task_status=#{taskStatus},"
            + "execute_times=execute_times+1,"
            + "execute_time=#{executeTime} "
            + "WHERE id=#{id} and shard_key=#{shardKey}"
    )
    int markStart(ConsistencyTaskInstance consistencyTaskInstance);

    /**
     * 标记任务成功
     *
     * @param taskInstance 一致性任务实例信息
     * @return 标记结果
     */
    @Delete("DELETE FROM ruyuan_tend_consistency_task WHERE id=#{id} and shard_key=#{shardKey}")
    int markSuccess(ConsistencyTaskInstance taskInstance);

    /**
     * 标记任务为失败
     *
     * @param taskInstance 一致性任务实例信息
     * @return 标记结果
     */
    @Update("UPDATE ruyuan_tend_consistency_task SET task_status=2, error_msg=#{errorMsg}, execute_time=#{executeTime} WHERE id=#{id} and shard_key=#{shardKey}")
    int markFail(ConsistencyTaskInstance taskInstance);

    /**
     * 标记为降级失败
     *
     * @param taskInstance 一致性任务实例
     * @return 标记结果
     */
    @Update("UPDATE ruyuan_tend_consistency_task SET fallback_error_msg=#{fallbackErrorMsg} WHERE id=#{id} and shard_key=#{shardKey}")
    int markFallbackFail(ConsistencyTaskInstance taskInstance);

}
