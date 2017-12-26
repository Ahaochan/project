package com.ahao.net;

import com.ahao.net.adapter.Adapter;
import com.ahao.net.convert.Convert;
import com.ahao.net.convert.UTF8Convert;
import com.ahao.util.CloneHelper;
import com.ahao.util.JSONHelper;
import com.ahao.util.lang.ReflectHelper;
import com.ahao.util.lang.StringHelper;
import com.ahao.util.lang.time.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ahaochan on 2017/8/10.
 * <p>
 * 封装了响应体的类, 用于格式化
 */
public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private String url;
    private byte[] data;

    public Response(String url, byte[] data) {
        this.url = url;
        this.data = CloneHelper.clone(data);
    }

    public byte[] getByte() {
        return CloneHelper.clone(data);
    }

    /**
     * 默认返回 String 格式数据
     *
     * @return 返回格式化后的数据
     */
    @Override
    public String toString() {
        return adapter(UTF8Convert.class, (Adapter<String>) null);
    }
    /**
     * 格式化数据, 不进行二次加工数据, 反射创建转换器
     *
     * @param convert 进行格式化的转换器
     * @param <T>     返回数据类型
     * @param <C>     进行格式化的转换器的类型
     * @return 返回格式化后的数据
     */
    public <T, C extends Convert<T>> T convert(Class<C> convert) {
        // 进行转换和加工
        T result = adapter(convert, (Adapter<T>) null);
        return result;
    }

    /**
     * 格式化数据, 不进行二次加工数据
     *
     * @param convert 进行格式化的转换器
     * @param <T>     返回数据类型
     * @return 返回格式化后的数据
     */
    public <T> T convert(Convert<T> convert) {
        // 进行转换和加工
        T result = adapter(convert, null);
        return result;
    }


    /**
     * 格式化数据, 进行二次加工数据, 反射创建转换器和适配器
     *
     * @param convertClass 进行格式化的转换器
     * @param adapterClass 进行二次加工的适配器
     * @param <T>          返回数据类型
     * @param <C>          进行格式化的转换器的类型
     * @param <A>          进行二次加工的适配器的类型
     * @return 返回格式化后的数据
     */
    public <T, C extends Convert<T>, A extends Adapter<T>> T adapter(Class<C> convertClass, Class<A> adapterClass) {
        // 反射创建实例
        C converter = ReflectHelper.create(convertClass);
        A adapter = ReflectHelper.create(adapterClass);
        // 进行转换和加工
        T result = adapter(converter, adapter);
        return result;
    }

    /**
     * 格式化数据, 进行二次加工数据, 反射创建转换器
     *
     * @param convertClass 进行格式化的转换器
     * @param adapter      进行二次加工的适配器
     * @param <T>          返回数据类型
     * @param <C>          进行格式化的转换器的类型
     * @return 返回格式化后的数据
     */
    public <T, C extends Convert<T>> T adapter(Class<C> convertClass, Adapter<T> adapter) {
        // 反射创建实例
        C converter = ReflectHelper.create(convertClass);
        // 进行转换和加工
        T result = adapter(converter, adapter);
        return result;
    }

    /**
     * 格式化数据
     *
     * @param convert 进行格式化的转换器
     * @param adapter 进行二次加工的适配器
     * @param <T>     返回数据类型
     * @return 返回格式化后的数据
     */
    public <T> T adapter(Convert<T> convert, Adapter<T> adapter) {
        String clazz = convert.clazz().getSimpleName();

        long convertStart = System.currentTimeMillis();
        logger.trace("[" + url + "]: 开始转换响应体为" + clazz + ", 开始时间:" + DateHelper.getString(convertStart, DateHelper.yyyyMMdd_hhmmssSSS));
        logger.trace("[" + url + "]: 转换前的数据为(UTF-8):" + StringHelper.replace(new String(data, Charset.forName("UTF-8")), "\n", " "));
        T origin = convert.convert(data);
        long convertEnd = System.currentTimeMillis();
        logger.trace("[" + url + "]: 转换后的数据为:" + JSONHelper.toJSONString(origin));
        logger.trace("[" + url + "]: 转换响应体为" + clazz + "结束, 结束时间:" + DateHelper.getString(convertEnd, DateHelper.yyyyMMdd_hhmmssSSS) +
                ", 总耗时: " + DateHelper.getBetween(convertStart, convertEnd, TimeUnit.MILLISECONDS) + "毫秒");

        // 如果有Adapter则加工
        if (adapter != null && origin != null) {
            long adapterStart = System.currentTimeMillis();
            logger.trace("[" + url + "]: 开始加工" + clazz + ", 开始时间:" + DateHelper.getString(adapterStart, DateHelper.yyyyMMdd_hhmmssSSS));
            logger.trace("[" + url + "]: 加工前的数据为:" + JSONHelper.toJSONString(origin));
            T result = adapter.adapter(origin);
            long adapterEnd = System.currentTimeMillis();
            logger.trace("[" + url + "]: 加工后的数据为:" + JSONHelper.toJSONString(result));
            logger.trace("[" + url + "]: 加工" + clazz + "结束, 结束时间:" + DateHelper.getString(adapterEnd, DateHelper.yyyyMMdd_hhmmssSSS) +
                    ", 总耗时: " + DateHelper.getBetween(convertStart, convertEnd, TimeUnit.MILLISECONDS) + "毫秒");
            return result;
        }
        // 没有Adapter则返回原始数据
        return origin;
    }
}