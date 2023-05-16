package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 流程节点查询请求
 */
@Data
public class ProcessNodeListQuery {
    /**
     * 流程类型
     */
    private Integer type;

    /**
     * 节点名称，可以模糊匹配
     */
    private String name;
}
