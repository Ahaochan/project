package moe.ahao.operate.log.ifunc;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ParseFunctionFactory {
    private final Map<String, IParseFunction> allFunctionMap = new ConcurrentHashMap<>();

    public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
        if (CollectionUtils.isEmpty(parseFunctions)) {
            return;
        }
        for (IParseFunction parseFunction : parseFunctions) {
            if (StringUtils.isEmpty(parseFunction.functionName())) {
                continue;
            }
            allFunctionMap.put(parseFunction.functionName(), parseFunction);
        }
    }

    public IParseFunction getFunction(String functionName) {
        IParseFunction function = allFunctionMap.get(functionName);
        if (null == function) {
            throw new UnsupportedOperationException(String.format("please implement custom function=[%s]!", functionName));
        }
        return function;
    }
}
