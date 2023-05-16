package moe.ahao.process.management.infrastructure.repository.impl.mybatis.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.ahao.domain.entity.BaseDO;

import java.io.Serializable;

/**
 * 流程节点构建表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("process_node_linked")
public class ProcessNodeLinkedDO extends BaseDO implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属流程配置名称
     */
    private String processConfigName;

    /**
     * 流程节点名称
     */
    private String processNodeName;

    /**
     * 调用方式：SYNC/ASYNC
     */
    private String invokeMethod;
}
