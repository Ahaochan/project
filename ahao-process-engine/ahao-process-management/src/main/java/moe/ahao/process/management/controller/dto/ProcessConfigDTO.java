package moe.ahao.process.management.controller.dto;

import lombok.Data;

import java.util.List;

/**
 * 流程配置DTO
 */
@Data
public class ProcessConfigDTO {

    /**
     * 主键id
     */
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

    /**
     * 流程节点构建
     */
    private List<ProcessNodeLinkedDTO> processNodeLinked;

    /**
     * 业务关联关系
     */
    private List<BizConfigListDTO> bizConfigRelations;
}
