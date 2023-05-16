package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 删除流程请求
 */
@Data
public class ProcessConfigDeleteCommand {
    /**
     * 流程配置名称
     */
    private String name;
}
