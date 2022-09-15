package moe.ahao.operate.log.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LogRecordContext {
    // TODO 改造成TTL
    private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

    public static void putEmptySpan() {
        variableMapStack.get().push(new HashMap<>());
    }

    public void putVariable(String key, Object value) {
        getVariables().put(key, value);
    }

    public static Map<String, Object> getVariables() {
        return variableMapStack.get().peek();
    }
}
