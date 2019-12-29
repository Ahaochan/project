package moe.ahao.btrace;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

@BTrace
public class ThrowBTrace {
    @TLS
    static Throwable currentException;

    @OnMethod(clazz = "java.lang.Throwable", method = "<init>")
    public static void _throw(@Self Throwable self) {
        currentException = self;
    }

    @OnMethod(clazz = "java.lang.Throwable", method = "<init>")
    public static void _throw(@Self Throwable self, String s) {
        currentException = self;
    }

    @OnMethod(clazz = "java.lang.Throwable", method = "<init>")
    public static void _throw(@Self Throwable self, String s, Throwable cause) {
        currentException = self;
    }

    @OnMethod(clazz = "java.lang.Throwable", method = "<init>")
    public static void _throw(@Self Throwable self, Throwable cause) {
        currentException = self;
    }

    @OnMethod(clazz = "java.lang.Throwable", method = "<init>", location = @Location(Kind.RETURN))
    public static void initAfter() {
        if (currentException != null) {
            BTraceUtils.Threads.jstack(currentException);
            currentException = null;
        }
    }
}
