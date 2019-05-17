package com.ahao.util.commons.lang;

import com.ahao.util.commons.lang.time.DateHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ahaochan on 2017/8/10.
 * 反射工具类
 */
public class ReflectHelper {
    private static final Logger logger = LoggerFactory.getLogger(ReflectHelper.class);
    private static final int DEFAULT_ARRAY_LENGTH = 16;

    /**
     * 反射创建无参实例
     */
    public static <T> T create(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            logger.error(String.format("实例化%s失败", clazz.getName()), e);
        }
        return null;
    }

    /**
     * 反射创建一个数组, 默认长度为16
     * @param clazz 类型
     */
    public static <T> T[] createArray(Class<T> clazz) {
        return createArray(clazz, DEFAULT_ARRAY_LENGTH);
    }

    /**
     * 反射创建一个数组
     * @param clazz  类型
     * @param length 数组长度
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> clazz, int length) {
        return (T[]) Array.newInstance(clazz, length);
    }

    /**
     * 获取 collection 集合中子元素的泛型类型
     * @param collection 集合
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getElementClass(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        return (Class<T>) collection.iterator().next().getClass();
    }

    /**
     * 获取数组中子元素的泛型类型
     * @param array 数组
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getElementClass(T... array) {
        if (ArrayUtils.isEmpty(array)) {
            return null;
        }
        return (Class<T>) array[0].getClass();
    }

    public static List<Field> getAllField(Object obj) {
        if(obj == null) {
            return Collections.emptyList();
        }

        List<Field> fields = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        while(clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
    public static Field getField(Object obj, String fieldName) {
        if(obj == null || StringUtils.isEmpty(fieldName)) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        try {
            Field field = null;
            while(clazz != null && field == null) {
                field = clazz.getDeclaredField(fieldName);
                clazz = clazz.getSuperclass();
            }
            if(field == null) {
                logger.error("{}没有找到{}属性!", obj.getClass().getName(), fieldName);
            }
            return field;
        } catch (NoSuchFieldException e) {
            logger.error("{}没有找到{}属性!", clazz.getName(), fieldName);
            return null;
        }
    }
    public static <T> T getValue(Object obj, String fieldName) {
        if(obj == null || StringUtils.isEmpty(fieldName)) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        Field field = getField(obj, fieldName);
        if(field == null) {
            logger.error("{}没有找到{}属性!", clazz.getName(), fieldName);
            return null;
        }
        return getValue(obj, field);
    }
    public static <T> T getValue(Object obj, Field field) {
        if(obj == null || field == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            return (T) field.get(obj);
        } catch (IllegalAccessException e) {
            logger.error("获取{}的{}属性的值失败!", clazz.getName(), field.getName());
        } finally {
            field.setAccessible(accessible);
        }
        return null;
    }
    public static boolean setValue(Object obj, String fieldName, Object value) {
        return setValue(obj, getField(obj, fieldName), value);
    }
    public static boolean setValue(Object obj, Field field, Object value) {
        if(obj == null || field == null) {
            return false;
        }

        Class<?> typeClazz = field.getType();
        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        try {
            // 1. 解析为 Boolean
            if (typeClazz.equals(Boolean.TYPE)) {
                field.setBoolean(obj, Boolean.parseBoolean(value.toString()));
            } else if (typeClazz.equals(Boolean.class)) {
                field.set(obj, Boolean.valueOf(value.toString()));
            }
            // 2. 解析为 Byte
            else if (typeClazz.equals(Byte.TYPE)) {
                field.setByte(obj, Byte.parseByte(value.toString()));
            } else if (typeClazz.equals(Byte.class)) {
                field.set(obj, Byte.valueOf(value.toString()));
            }
            // 3. 解析为 Char
            else if (typeClazz.equals(Character.TYPE)) {
                String chars = value.toString();
                if (StringUtils.isNotEmpty(chars)) {
                    field.setChar(obj, chars.charAt(0));
                }
            } else if (typeClazz.equals(Character.class)) {
                String chars = value.toString();
                if (StringUtils.isNotEmpty(chars)) {
                    field.set(obj, chars.charAt(0));
                }
            }
            // 4. 解析为 Short
            else if (typeClazz.equals(Short.TYPE)) {
                field.setShort(obj, Short.parseShort(value.toString()));
            } else if (typeClazz.equals(Short.class)) {
                field.set(obj, Short.valueOf(value.toString()));
            }
            // 5. 解析为 Int
            else if (typeClazz.equals(Integer.TYPE)) {
                field.setInt(obj, Integer.parseInt(value.toString()));
            } else if (typeClazz.equals(Integer.class)) {
                field.set(obj, Integer.valueOf(value.toString()));
            }
            // 6. 解析为 Long
            else if (typeClazz.equals(Long.TYPE)) {
                field.setLong(obj, Long.parseLong(value.toString()));
            } else if (typeClazz.equals(Long.class)) {
                field.set(obj, Long.valueOf(value.toString()));
            }
            // 7. 解析为 Float
            else if (typeClazz.equals(Float.TYPE)) {
                field.setFloat(obj, Float.parseFloat(value.toString()));
            } else if (typeClazz.equals(Float.class)) {
                field.set(obj, Float.valueOf(value.toString()));
            }
            // 8. 解析为 Double
            else if (typeClazz.equals(Double.TYPE)) {
                field.setDouble(obj, Double.parseDouble(value.toString()));
            } else if (typeClazz.equals(Double.class)) {
                field.set(obj, Double.valueOf(value.toString()));
            }
            // 9. 解析为 String
            else if (typeClazz.equals(String.class)) {
                field.set(obj, value.toString());
            }
            // 10. 解析为 Date
            else if (typeClazz.equals(Date.class)) {
                field.set(obj, DateHelper.getDate(value.toString(), "yyyy-MM-dd hh:mm:ss"));
            }
            // 解析失败
            else {
                logger.error("{}的属性类型{}没有被解析类{}支持!", obj.getClass().getName(), typeClazz, ReflectHelper.class.getSimpleName());
                return false;
            }
        } catch (IllegalAccessException e) {
            logger.error("为"+obj.getClass().getName()+"赋值"+field.getName()+"失败!", e);
        } finally {
            field.setAccessible(accessible);
        }
        return true;
    }

    /**
     * 获取指定包 pkg 下的所有 Class 对象
     * @param pkg 包
     */
    public static List<Class> getClass(String pkg) {
        String[] extensions = { "class" };
        List<Class> result = new ArrayList<>();
        try {
            // 1. 获取不同模块的 URL
            Enumeration<URL> roots = ReflectHelper.class.getClassLoader().getResources("");
            while (roots.hasMoreElements()) {
                // 2. 获取每个模块的 target/class 目录 和 指定包所在目录
                File classesPath = new File(roots.nextElement().getFile());
                File packagePath = new File(classesPath, StringUtils.replace(pkg, ".", "/"));
                // 3. 获取当前模块下的所有 class 文件
                Collection<File> classes = FileUtils.listFiles(packagePath, extensions, true);

                // 4. 将 class 文件 转为 Class 对象
                List<Class> list = classes.stream()
                        .map(File::getPath)
                        .map(p -> StringUtils.removeStart(p, classesPath.getAbsolutePath()))
                        .map(p -> StringUtils.replace(p, "\\", "."))
                        .map(p -> StringUtils.removeStart(p, "."))
                        .map(p -> StringUtils.removeEnd(p, ".class"))
                        .map(p -> { try { return Class.forName(p); }
                        catch (ClassNotFoundException e) { e.printStackTrace(); return null; }
                        })
                        .collect(Collectors.toList());
                result.addAll(list);
            }
            return result;
        } catch (IOException e) {
            logger.error("获取" + pkg + "下的class失败", e);
        }
        return Collections.emptyList();
    }
}