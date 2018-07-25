package com.ahao.commons.entity;


import java.util.*;

/**
 * Created by Ahaochan on 2017/8/22.
 * dao返回对象, 不使用Entity的形式, 返回Map集合
 * 在Map基础上追加转化基本数据类型的方法
 */
public interface IDataSet extends Map<String, Object> {

    @Override
    Object put(String key, Object value);

    @Override
    Object putIfAbsent(String key, Object value);

    @Override
    void putAll(Map<? extends String, ?> m);

    Object get(Object key);

    boolean getBoolean(String key);

    int getInt(String key);

    long getLong(String key);

    double getDouble(String key);

    String getString(String key);

    List<String> getStringList(String... keys);

    Date getDate(String key);

    Object getObject(String key);

    @Override
    boolean containsKey(Object key);

    @Override
    Set<String> keySet();

    @Override
    Object remove(Object key);

    @Override
    boolean containsValue(Object value);

    @Override
    Collection<Object> values();

    @Override
    Set<Entry<String, Object>> entrySet();

    @Override
    int size();

    @Override
    boolean isEmpty();

    @Override
    void clear();
}
