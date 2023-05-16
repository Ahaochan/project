package moe.ahao.process.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import moe.ahao.exception.BizException;
import moe.ahao.exception.BizExceptionEnum;

@Getter
@AllArgsConstructor
public enum ProcessExceptionEnum implements BizExceptionEnum<BizException> {
    /**
     * 流程节点管理
     * +--------+---------+-------+
     * | first  | middle  | last  |
     * +========+=========+=======+
     * | 23     | 01      | 00    |
     * +--------+---------+-------+
     */
    PROCESS_NODE_NAME_IS_EMPTY(230100, "流程节点名称不能为空"),
    PROCESS_NODE_ID_IS_EMPTY(230101, "流程节点ID不能为空"),
    PROCESS_NODE_BEAN_NAME_IS_EMPTY(230102, "流程节点bean名称不能为空"),
    PROCESS_NODE_BEAN_CLAZZ_NAME_IS_EMPTY(230103, "流程节点bean全限定类名不能为空"),
    PROCESS_NODE_NOT_FOUND(230104, "流程节点不存在"),
    PROCESS_NODE_NAME_NOT_MATCH(230105, "流程节点名称不匹配"),


    /**
     * 流程管理
     * +--------+---------+-------+
     * | first  | middle  | last  |
     * +========+=========+=======+
     * |  23    | 02      | 00    |
     * +--------+---------+-------+
     */
    PROCESS_NAME_IS_EMPTY(230200, "流程名称不能为空"),
    PROCESS_XML_NAME_IS_EMPTY(230201, "流程xml name不能为空"),
    PROCESS_NODE_LINKED_IS_EMPTY(230202, "流程节点构建不能为空"),
    BIZ_CONFIG_RELATION_IS_EMPTY(230203, "业务关联关系不能为空"),
    INVOKE_METHOD_IS_ERROR(230204, "调用方式填写有误"),
    PROCESS_ENABLE_IS_ERROR(230205, "流程启用/禁用类型异常"),
    ;

    private final int code;
    private final String message;

    @Override
    public BizException msg(Object... args) {
        return new BizException(code, String.format(message, args));
    }
}
