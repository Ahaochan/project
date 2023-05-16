package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 业务配置列表DTO
 */
@Data
public class BizConfigListDTO {

    /**
     * 业务配置id
     */
    private Long id;

    /**
     * 业务配置名称
     */
    private String name;

    /**
     * 业务线
     */
    private Integer businessIdentifier;

    /**
     * 订单类型
     */
    private Integer orderType;

    public BizConfigListDTO() {

    }

    public BizConfigListDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
