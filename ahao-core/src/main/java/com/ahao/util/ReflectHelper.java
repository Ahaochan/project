package com.ahao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/8/10.
 */
public abstract class ReflectHelper {
    private static final Logger logger = LoggerFactory.getLogger(ReflectHelper.class);

    public static <T> T create(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            logger.warn("创建失败:" + clazz.getName() + ", 该类是一个抽象类或接口");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.warn("创建失败:" + clazz.getName() + ", 请检查构造函数是否为private");
        }

        return null;
    }
}
