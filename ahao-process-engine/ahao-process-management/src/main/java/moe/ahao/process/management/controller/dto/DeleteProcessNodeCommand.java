package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 删除流程节点请求
 */
@Data
public class DeleteProcessNodeCommand {
    /**
     * 节点名称
     */
    private String name;
}
