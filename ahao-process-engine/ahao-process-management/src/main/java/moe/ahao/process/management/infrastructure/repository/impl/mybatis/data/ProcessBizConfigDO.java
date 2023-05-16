package moe.ahao.process.management.infrastructure.repository.impl.mybatis.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moe.ahao.domain.entity.BaseDO;

import java.io.Serializable;

/**
 * 业务配置表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("process_biz_config")
public class ProcessBizConfigDO extends BaseDO implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务线
     */
    private Integer businessIdentifier;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 业务名称
     */
    private String name;
}
