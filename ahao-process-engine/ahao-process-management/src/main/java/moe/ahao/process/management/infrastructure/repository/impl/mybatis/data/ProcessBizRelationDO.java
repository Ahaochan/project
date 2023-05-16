package moe.ahao.process.management.infrastructure.repository.impl.mybatis.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.ahao.domain.entity.BaseDO;

import java.io.Serializable;

/**
 * 流程业务关联表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("process_biz_relation")
public class ProcessBizRelationDO extends BaseDO implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流程配置名称
     */
    private String processConfigName;

    /**
     * 业务配置id
     */
    private Long processBizConfigId;
}
