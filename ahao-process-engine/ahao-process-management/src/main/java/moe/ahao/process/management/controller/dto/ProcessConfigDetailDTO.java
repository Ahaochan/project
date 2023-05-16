package moe.ahao.process.management.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * 流程配置DTO
 */
@Data
public class ProcessConfigDetailDTO {
    /**
     * 主键id
     */
    private Long id;

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
     * 启用/禁用，0-禁用，1-启用
     */
    private Integer enable;

    /**
     * 流程节点构建
     */
    private List<ProcessNodeLinkedDTO> processNodeLinked;

    /**
     * 业务关联关系
     */
    private List<BizConfigListDTO> bizConfigRelations;

    /**
     * 流程构建DTO
     */
    @Data
    public static class ProcessNodeLinkedDTO {
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
    }

    /**
     * 业务配置列表DTO
     */
    @Data
    public static class BizConfigListDTO {
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
    }
}
