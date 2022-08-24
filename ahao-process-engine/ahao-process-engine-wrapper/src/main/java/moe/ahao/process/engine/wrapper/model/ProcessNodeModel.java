package moe.ahao.process.engine.wrapper.model;

import lombok.Getter;
import lombok.Setter;
import moe.ahao.process.engine.core.enums.InvokeMethod;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 流程中其中一个节点的配置信息
 */
@Getter
public class ProcessNodeModel {
    private String name;
    private Class<?> clazz;
    private Set<String> nextNodeNames;
    @Setter
    private boolean begin = false;
    @Setter
    private InvokeMethod invokeMethod = InvokeMethod.SYNC;

    public ProcessNodeModel(String name, String className) {
        this.setName(name);
        this.setClazz(className);
        this.nextNodeNames = Collections.emptySet();
    }

    private void setName(String name) {
        if(StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("流程节点配置的name属性不能为空！");
        }
        this.name = name;
    }

    private void setClazz(String className) {
        if(StringUtils.isEmpty(className)) {
            throw new IllegalArgumentException("流程节点配置的class属性不能为空！");
        }
        try {
            this.clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("流程节点配置的class属性加载类" + className + "失败！", e);
        }
    }

    public void setNextNodeNames(String nextNodeNameStr) {
        if(StringUtils.isBlank(nextNodeNameStr)) {
            return;
        }
        String[] nextNameArray = StringUtils.split(nextNodeNameStr, ",");
        List<String> nextNameList = Arrays.asList(nextNameArray);
        this.nextNodeNames = Collections.unmodifiableSet(new HashSet<>(nextNameList));
    }
}
