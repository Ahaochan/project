package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 保存流程节点请求（兼容新增和修改）
 */
@Data
public class SaveProcessNodeCommand {

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
