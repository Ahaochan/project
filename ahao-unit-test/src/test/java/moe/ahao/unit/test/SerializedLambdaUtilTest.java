package moe.ahao.unit.test;

import moe.ahao.unit.test.support.SerializedLambdaUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SerializedLambdaUtilTest {

    @Test
    public void test() {
        Assertions.assertEquals("success", SerializedLambdaUtil.resolveProperty(Data::isSuccess));
        Assertions.assertEquals("name", SerializedLambdaUtil.resolveProperty(Data::getName));
    }

    public static class Data {
        private boolean success;
        private String name;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
