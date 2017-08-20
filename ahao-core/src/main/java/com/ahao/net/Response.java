package com.ahao.net;

import com.ahao.net.adapter.Adapter;
import com.ahao.net.convert.Convert;
import com.ahao.net.convert.StringConvert;
import com.ahao.util.CloneHelper;
import com.ahao.util.ReflectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ahaochan on 2017/8/10.
 * <p>
 * 封装了响应体的类, 用于格式化
 */
public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);


    private byte[] data;

    public Response(byte[] data) {
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
        StringConvert convert = new StringConvert();
        return convert.convert(data);
    }

    /**
     * 格式化数据, 不进行二次加工数据
     *
     * @param convert 进行格式化的转换器
     * @param <T>     返回数据类型
     * @return 返回格式化后的数据
     */
    public <T> T convert(Convert<T> convert) {
        T origin = convert.convert(data);
        return origin;
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
        C converter = ReflectHelper.create(convert);
        return convert(converter);
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
        C converter = ReflectHelper.create(convertClass);
        A adapter = ReflectHelper.create(adapterClass);
        return adapter(converter, adapter);
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
        C converter = ReflectHelper.create(convertClass);
        return adapter(converter, adapter);
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
        T origin = convert.convert(data);
        T format = adapter.adapter(origin);
        return format;
    }
}