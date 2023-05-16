package moe.ahao.process.management.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * 流程构建DTO
 */
@Data
public class ProcessNodeLinkedDTO {

    /**
     * id
     */
    private Long processNodeId;

    /**
     * 流程节点对应的beanName
     */
    @JsonIgnore
    private String processNodeBeanName;

    /**
     * 流程节点对应的bean的权限定类名
     */
    @JsonIgnore
    private String processNodeBeanClazzName;

    /**
     * 流程节点名称
     */
    private String processNodeName;

    /**
     * 调用方式：SYNC/ASYNC
     */
    private String invokeMethod;

    public ProcessNodeLinkedDTO() {

    }

}
