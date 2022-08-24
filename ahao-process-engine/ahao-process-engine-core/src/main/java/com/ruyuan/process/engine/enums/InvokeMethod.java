package com.ruyuan.process.engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程之间的调用方式
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum InvokeMethod {
    SYNC("同步"),
    ASYNC("异步");
    private String desc;
}
