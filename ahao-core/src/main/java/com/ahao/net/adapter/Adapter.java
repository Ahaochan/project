package com.ahao.net.adapter;

/**
 * Created by Ahaochan on 2017/8/14.
 * 适配器, 对T类型数据进行二次加工, 常用于json格式化, 图片二次处理等
 * 一般使用匿名内部类实现, 方法体太长的话可以在模块内写子类
 */
public interface Adapter<T> {
    T adapter(T data);
}