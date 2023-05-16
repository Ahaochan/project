package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 流程节点查询请求
 */
@Data
public class ProcessNodeDetailQuery {
    /**
     * 节点名称
     */
    private String name;
}
