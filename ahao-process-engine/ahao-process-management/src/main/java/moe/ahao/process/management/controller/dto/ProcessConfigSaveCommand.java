package moe.ahao.process.management.controller.dto;

import lombok.Data;

import java.util.List;

/**
 * 保存流程节点请求（兼容新增和修改）
 */
@Data
public class ProcessConfigSaveCommand {
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
     * 流程节点构建
     */
    private List<ProcessNodeLinkedDTO> processNodeLinked;

    /**
     * 业务关联关系
     */
    private List<BizConfigListDTO> bizConfigRelations;
}
