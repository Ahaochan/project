package moe.ahao.btrace;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

@BTrace
public class MethodBTrace {

    @OnMethod(clazz = "moe.ahao.TestClass", method = "method1", location = @Location(Kind.ENTRY))
    public static void simple(@ProbeClassName String className, @ProbeMethodName String methodName, AnyType[] args) {
        BTraceUtils.println("拦截普通方法: " + className + "#" + methodName);
        BTraceUtils.printArray(args);
    }

    @OnMethod(clazz = "moe.ahao.TestClass", method = "<init>", location = @Location(Kind.ENTRY))
    public static void constructor(@ProbeClassName String className, @ProbeMethodName String methodName, AnyType[] args) {
        BTraceUtils.println("拦截构造方法: " + className + "#" + methodName);
        BTraceUtils.printArray(args);
    }

    @OnMethod(clazz = "moe.ahao.TestClass", method = "method2", location = @Location(Kind.ENTRY))
    public static void overload(@ProbeClassName String className, @ProbeMethodName String methodName, String arg1, Integer arg2) {
        BTraceUtils.println("拦截重载方法: " + className + "#" + methodName);
        BTraceUtils.println("参数: " + arg1 + "," + arg2);
    }

    @OnMethod(clazz = "moe.ahao.TestClass", method = "/.*/")
    public static void allMethod(@ProbeClassName String className, @ProbeMethodName String methodName) {
        BTraceUtils.println("拦截所有方法: " + className + "#" + methodName);
    }

    @OnMethod(clazz = "moe.ahao.TestClass", location = @Location(value = Kind.LINE, line = 22))
    public static void line(@ProbeClassName String className, @ProbeMethodName String methodName, int line) {
        BTraceUtils.println("拦截第" + line + "行: " + className + "#" + methodName);
    }
}
