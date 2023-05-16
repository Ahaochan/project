package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 业务配置查询请求
 */
@Data
public class BizConfigListQuery {

    /**
     * 业务名称，可以模糊匹配
     */
    private String name;
}
