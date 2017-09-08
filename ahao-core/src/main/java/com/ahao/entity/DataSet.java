package com.ahao.entity;

import com.ahao.util.CollectionHelper;
import com.ahao.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ahaochan on 2017/8/22.
 */
public class DataSet implements IDataSet, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(DataSet.class);
    private static final long serialVersionUID = 7641097398093605780L;

    // 优先使用组合，而不用继承
    private Map<String, Object> dataMap;

    public DataSet() {
        dataMap = new HashMap<>();
    }

    public DataSet(final int initialCapacity) {
        dataMap = new HashMap<>(initialCapacity);
    }

    public DataSet(Map<? extends String, ?> sourceMap) {
        if (sourceMap != null) {
            dataMap = new HashMap<>(sourceMap);
        } else {
            dataMap = new HashMap<>();
        }
    }

    @Override
    public Object put(String key, Object value) {
        if (StringHelper.isEmpty(key)) {
            return null;
        }
        return dataMap.put(key, value);
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        Object v = dataMap.get(key);
        if (v == null) {
            v = dataMap.put(key, value);
        }
        return v;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        if (CollectionHelper.isNotEmpty(m)) {
            dataMap.putAll(m);
        }
    }

    @Override
    public Object get(Object key) {
        return dataMap.get(key);
    }

    @Override
    public boolean getBoolean(String key) {
        Object obj = dataMap.get(key);
        return Boolean.valueOf(obj.toString());
    }

    @Override
    public int getInt(String key) {
        Object obj = dataMap.get(key);
        try {
            return Integer.parseInt(obj == null ? "0" : obj.toString());
        } catch (NumberFormatException e) {
            logger.error("数据集获取int错误:", e);
        }
        return 0;
    }

    @Override
    public long getLong(String key) {
        Object obj = dataMap.get(key);
        try {
            return Long.parseLong(obj == null ? "0" : obj.toString());
        } catch (NumberFormatException e) {
            logger.error("数据集获取long错误:" , e);
        }
        return 0L;
    }

    @Override
    public String getString(String key) {
        Object obj = dataMap.get(key);
        try {
            if (obj instanceof Clob) {
                Clob clob = (Clob) obj;
                return clob.getSubString(1, (int) clob.length());
            } else {
                return obj.toString();
            }
        } catch (SQLException | NullPointerException e) {
            logger.error("数据集获取String错误:" , e);
        }
        return "";
    }

    @Override
    public List<String> getStringList(String... keys) {
        return Stream.of(keys)
                .map(this::getString)
                .collect(Collectors.toList());
    }

    @Override
    public Date getDate(String key) {
        Object result = getObject(key);
        if (result == null || !(result instanceof Date)) {
            return null;
        } else {
            return (Date) result;
        }
    }

    @Override
    public Object getObject(String key) {
        return dataMap.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return dataMap.containsKey(key);
    }

    @Override
    public Set<String> keySet() {
        return dataMap.keySet();
    }

    @Override
    public Object remove(Object key) {
        return dataMap.remove(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return dataMap.containsValue(value);
    }

    @Override
    public Collection<Object> values() {
        return dataMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return dataMap.entrySet();
    }

    @Override
    public int size() {
        return dataMap.size();
    }

    @Override
    public boolean isEmpty() {
        return dataMap.isEmpty();
    }

    @Override
    public void clear() {
        dataMap.clear();
    }
}
