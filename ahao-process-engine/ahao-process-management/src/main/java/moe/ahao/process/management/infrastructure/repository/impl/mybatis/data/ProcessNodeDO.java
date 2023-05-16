package moe.ahao.process.management.infrastructure.repository.impl.mybatis.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.ahao.domain.entity.BaseDO;

import java.io.Serializable;

/**
 * 流程节点表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("process_node")
public class ProcessNodeDO extends BaseDO implements Serializable {
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
     * 节点名称
     */
    private String name;

    /**
     * 节点对应的beanName
     */
    private String beanName;

    /**
     * bean的权限定类名
     */
    private String beanClazzName;

    /**
     * 备注
     */
    private String remark;
}
