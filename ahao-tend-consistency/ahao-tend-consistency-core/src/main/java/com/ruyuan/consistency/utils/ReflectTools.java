package com.ruyuan.consistency.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.asm.Type;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.StringJoiner;

/**
 * 反射工具类
 *
 * @author zhonghuashishan
 **/
@Slf4j
public class ReflectTools {

    /**
     * 初始化基础数据类型的MAP
     */
    private static final HashMap<String, Class<?>> PRIMITIVE_MAP = new HashMap<String, Class<?>>() {
        {
            put("java.lang.Integer", int.class);
            put("java.lang.Double", double.class);
            put("java.lang.Float", float.class);
            put("java.lang.Long", long.class);
            put("java.lang.Short", short.class);
            put("java.lang.Boolean", boolean.class);
            put("java.lang.Char", char.class);
        }
    };

    /**
     * 构造参数类型数组
     *
     * @param parameterTypes 参数类型数组
     * @return 参数类型数组
     */
    public static Class<?>[] buildTypeClassArray(String[] parameterTypes) {
        Class<?>[] parameterTypeClassArray = new Class<?>[parameterTypes.length];
        for (int i = parameterTypes.length - 1; i >= 0; i--) {
            try {
                parameterTypeClassArray[i] = Class.forName(parameterTypes[i]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return parameterTypeClassArray;
    }

    /**
     * 根据类名称获取类对象
     *
     * @param className 类名称
     * @return 类对象
     */
    public static Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("发生类找不到异常, 加载的类为 {}", className, e);
            return null;
        }
    }

    /**
     * 根据类名称获取类对象
     *
     * @param className 类名称
     * @return 类对象
     */
    public static Class<?> checkClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 构造方法入参
     *
     * @param parameterText           方法入参JSON字符串 会解析成JSON数组
     * @param parameterTypeClassArray 参数类型的类数组
     * @return 方法入参数组
     */
    public static Object[] buildArgs(String parameterText, Class<?>[] parameterTypeClassArray) {
        JSONArray paramJsonArray = JSONUtil.parseArray(parameterText);
        Object[] args = new Object[paramJsonArray.size()];

        for (int i = paramJsonArray.size() - 1; i >= 0; i--) {
            if (paramJsonArray.getStr(i).startsWith("{")) {
                // 将数据转换为对应的数据类型
                args[i] = JSONUtil.toBean(paramJsonArray.getStr(i), parameterTypeClassArray[i]);
            } else {
                args[i] = paramJsonArray.get(i);
            }
        }
        return args;
    }

    /**
     * 获取入参类名称数组
     *
     * @param signature aop切片切入的方法签名对象
     * @return 签名类字符串 (多个用逗号分隔)
     */
    public static String getArgsClassNames(Signature signature) {
        MethodSignature methodSignature = (MethodSignature) signature;
        Class<?>[] parameterTypes = methodSignature.getParameterTypes();
        StringBuilder parameterStrTypes = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterStrTypes.append(parameterTypes[i].getName());
            if (parameterTypes.length != (i + 1)) {
                parameterStrTypes.append(",");
            }
        }
        return parameterStrTypes.toString();
    }

    /**
     * 获取被拦截方法的全限定名称 格式：类路径#方法名(参数1的类型,参数2的类型,...参数N的类型)
     *
     * @param point     切点
     * @param argsClazz 入参的Class对象
     * @return 被拦截方法的全限定名称
     */
    public static String getTargetMethodFullyQualifiedName(JoinPoint point, Class<?>[] argsClazz) {
        StringJoiner methodSignNameJoiner = new StringJoiner("", "", "");
        methodSignNameJoiner
                .add(point.getTarget().getClass().getName())
                .add("#")
                .add(point.getSignature().getName());
        methodSignNameJoiner.add("(");
        for (int i = 0; i < argsClazz.length; i++) {
            String className = argsClazz[i].getName();
            methodSignNameJoiner.add(className);
            if (argsClazz.length != (i + 1)) {
                methodSignNameJoiner.add(",");
            }
        }
        methodSignNameJoiner.add(")");
        return methodSignNameJoiner.toString();
    }

    /**
     * 获取各个参数的Class对象数组
     *
     * @param args 目标方法入参
     * @return 参数类对象数组
     */
    public static Class<?>[] getArgsClass(Object[] args) {
        Class<?>[] clazz = new Class[args.length];
        for (int k = 0; k < args.length; k++) {
            if (!args[k].getClass().isPrimitive()) {
                // 获取的是封装类型而不是基础类型
                String result = args[k].getClass().getName();
                Class<?> typeClazz = PRIMITIVE_MAP.get(result);
                clazz[k] = ObjectUtils.isEmpty(typeClazz) ? args[k].getClass() : typeClazz;
            }
        }
        return clazz;
    }

    /**
     * 获取类的全路径
     *
     * @param clazz 要获取的类
     * @return 类路径
     */
    public static String getFullyQualifiedClassName(Class<?> clazz) {
        if (ObjectUtils.isEmpty(clazz)) {
            return "";
        }
        return clazz.getName();
    }

    /**
     * 校验目标类是否实现了目标接口
     *
     * @param targetClass              要检查的类
     * @param targetInterfaceClassName 目标接口类名称（包名全路径）
     * @return 结果
     */
    public static boolean isRealizeTargetInterface(Class<?> targetClass, String targetInterfaceClassName) {
        ClassInfo classInfo = ReflectUtils.getClassInfo(targetClass);
        for (Type anInterface : classInfo.getInterfaces()) {
            if (anInterface.getClassName().equals(targetInterfaceClassName)) {
                return true;
            }
        }
        return false;
    }

}
