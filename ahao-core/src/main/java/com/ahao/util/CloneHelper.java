package com.ahao.util;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/7/20.
 * <p>
 * 对未实现复制构造函数的jdk的类进行clone
 */
public abstract class CloneHelper {
    private static final Logger logger = LoggerFactory.getLogger(CloneHelper.class);

    private CloneHelper() {
    }

    public static <T extends Cloneable> T clone(T instance) {
        return ObjectUtils.clone(instance);
    }
}