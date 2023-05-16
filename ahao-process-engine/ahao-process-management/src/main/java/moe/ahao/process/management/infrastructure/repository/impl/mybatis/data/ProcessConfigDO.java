package moe.ahao.process.management.infrastructure.repository.impl.mybatis.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.ahao.domain.entity.BaseDO;

import java.io.Serializable;

/**
 * 流程配置表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("process_config")
public class ProcessConfigDO extends BaseDO implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型，1-正向，2-逆向
     */
    private Integer type;

    /**
     * 流程xml name
     */
    private String xmlName;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 启用/禁用，0-禁用，1-启用
     */
    private Integer enable;
}
