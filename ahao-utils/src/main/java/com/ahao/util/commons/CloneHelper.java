package com.ahao.util.commons;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/7/20.
 * <p>
 * 对未实现复制构造函数的jdk的类进行clone
 */
public class CloneHelper {
    private static final Logger logger = LoggerFactory.getLogger(CloneHelper.class);

    public static <T> T clone(T instance) {
        if(instance == null) {
            return null;
        }

        if(instance instanceof Cloneable) {
            return ObjectUtils.clone(instance);
        } else if(instance instanceof String) {
            return (T) new String(String.valueOf(instance));
        }
        return null;
    }
}