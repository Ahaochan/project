package moe.ahao.tend.consistency.core.utils;

import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data.ConsistencyTaskInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static moe.ahao.tend.consistency.core.utils.ExpressionUtils.parseBoolean;

class ExpressionUtilsTest {
    @Test
    void test() {
        ConsistencyTaskInstance instance = new ConsistencyTaskInstance();
        instance.setExecuteTimes(2);
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("taskInstance", instance);
        // dataMap.put("executeTimes", executeTimes);

        Assertions.assertTrue(parseBoolean("executeTimes > 1", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes >= 2", dataMap));
        Assertions.assertFalse(parseBoolean("executeTimes > 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes < 3", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes <= 2", dataMap));
        Assertions.assertFalse(parseBoolean("executeTimes < 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes == 2", dataMap));
        Assertions.assertTrue(parseBoolean("true", dataMap));
        Assertions.assertFalse(parseBoolean("false", dataMap));

        Assertions.assertTrue(parseBoolean("executeTimes > 1 && executeTimes < 3", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes > 1 && executeTimes <= 2", dataMap));
        Assertions.assertFalse(parseBoolean("executeTimes > 1 && executeTimes < 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes >= 2 && executeTimes < 3", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes >= 2 && executeTimes <= 2", dataMap));
        Assertions.assertFalse(parseBoolean("executeTimes >= 2 && executeTimes < 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes == 2 && executeTimes < 3", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes == 2 && executeTimes <= 2", dataMap));
        Assertions.assertFalse(parseBoolean("executeTimes == 2 && executeTimes < 2", dataMap));

        Assertions.assertTrue(parseBoolean("executeTimes > 1 || executeTimes < 3", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes > 1 || executeTimes <= 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes > 1 || executeTimes < 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes >= 2 || executeTimes < 3", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes >= 2 || executeTimes <= 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes >= 2 || executeTimes < 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes == 2 || executeTimes < 3", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes == 2 || executeTimes <= 2", dataMap));
        Assertions.assertTrue(parseBoolean("executeTimes == 2 || executeTimes < 2", dataMap));
        Assertions.assertFalse(parseBoolean("executeTimes < 2 || executeTimes > 3", dataMap));
    }
}
