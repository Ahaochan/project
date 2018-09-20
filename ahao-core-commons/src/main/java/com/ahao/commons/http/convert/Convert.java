package com.ahao.commons.http.convert;

/**
 * Created by Ahaochan on 2017/8/14.
 * 转换器, 将byte类型转换为T类型
 */
public interface Convert<T> {

    /**
     * 返回转换后的Class信息
     *
     * @return Class
     */
    Class<T> clazz();

    /**
     * 将byte类型转换为T类型
     *
     * @param origin 源数据
     * @return 转换后的数据
     */
    T convert(byte[] origin);
}
