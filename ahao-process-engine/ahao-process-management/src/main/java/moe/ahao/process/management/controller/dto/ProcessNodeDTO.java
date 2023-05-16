package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 流程节点DTO
 */
@Data
public class ProcessNodeDTO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 流程类型
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
