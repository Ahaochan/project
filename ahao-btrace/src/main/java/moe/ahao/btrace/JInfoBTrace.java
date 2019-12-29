package moe.ahao.btrace;

import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.BTrace;

@BTrace
public class JInfoBTrace {
    static {
        BTraceUtils.println("系统属性 jinfo -sysprops <pid>");
        BTraceUtils.printProperties();

        BTraceUtils.println("VM 参数 jinfo -flags <pid>");
        BTraceUtils.printVmArguments();

        BTraceUtils.println("环境变量");
        BTraceUtils.printEnv();
    }
}
