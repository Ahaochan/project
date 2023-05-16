package moe.ahao.process.management.controller.dto;

import lombok.Data;

@Data
public class ProcessConfigEnableCommand {
    /**
     * 流程配置名称
     */
    private String name;
    /**
     * 1启用, 0禁用
     */
    private Integer enable;
}
