package com.ahao.util;

import org.apache.http.client.utils.CloneUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/7/20.
 * <p>
 * Clone工具类
 */
public abstract class CloneHelper {
    private static final Logger logger = LoggerFactory.getLogger(CloneHelper.class);

    public static <T> T clone(T obj) {
        try {
            return CloneUtils.cloneObject(obj);
        } catch (CloneNotSupportedException e) {
            logger.warn(obj.getClass().getSimpleName() + "未实现Cloneable接口," +
                    " 若为自定义类, 建议使用复制构造函数");
            return null;
        }
    }

}
