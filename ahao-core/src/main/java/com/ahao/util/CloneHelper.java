package com.ahao.util;

import org.apache.http.client.utils.CloneUtils;
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
        try {
            return CloneUtils.cloneObject(instance);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            logger.warn(instance.getClass().getSimpleName() + "未实现Cloneable接口," +
                    " 若为自定义类, 建议使用复制构造函数");
            return null;
        }
    }
}