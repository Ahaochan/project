package moe.ahao.process.management.controller.dto;

import lombok.Data;

/**
 * 流程节点列表DTO
 */
@Data
public class ProcessNodeListDTO {

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

    public ProcessNodeListDTO() {

    }

    public ProcessNodeListDTO(Long id, Integer type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }
}
