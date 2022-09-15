package moe.ahao.operate.log;

import moe.ahao.operate.log.annotation.EnableOperateLog;
import moe.ahao.operate.log.annotation.OperateLog;
import moe.ahao.operate.log.aspect.OperateLogAspect;
import moe.ahao.operate.log.config.OperateLogAutoConfiguration;
import moe.ahao.operate.log.ifunc.IParseFunction;
import moe.ahao.operate.log.model.OperateLogInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@EnableOperateLog
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {
    AopAutoConfiguration.class, OperateLogAutoConfiguration.class, OperateLogAspect.class,
    TestOperateLogStoreService.class, OperateLogTest.ToLowerCaseFunction.class, OperateLogTest.AhaoService.class})
public class OperateLogTest {
    @Autowired
    private AhaoService ahaoService;
    @Autowired
    private TestOperateLogStoreService operateLogStoreService;
    @Autowired
    private ToLowerCaseFunction toLowerCaseFunction;

    @Test
    public void testNormalOperateLog() throws Exception {
        String bizNo = "bizNo_001" ;
        ahaoService.normalOperateLog(bizNo);

        OperateLogInstance expect = new OperateLogInstance();
        expect.setBizNo(bizNo);
        expect.setLogContent("打印普通日志");
        Assertions.assertEquals(expect, operateLogStoreService.loadOperateLog());
    }

    @Test
    public void testSpELOperateLog() throws Exception {
        String bizNo = "bizNo_001" ;
        String operator = "Ahao" ;
        ahaoService.spELOperateLog(bizNo, "Ahao");

        OperateLogInstance expect = new OperateLogInstance();
        expect.setBizNo(bizNo);
        expect.setOperator(operator);
        expect.setLogContent("打印SpSL表达式, 业务编码【" + bizNo + "】");
        Assertions.assertEquals(expect, operateLogStoreService.loadOperateLog());
    }

    @Test
    public void testFunctionOperateLog() throws Exception {
        String bizNo = "bizNo_001" ;
        String operator = "Ahao" ;
        ahaoService.functionOperateLog(bizNo, "Ahao");

        OperateLogInstance expect = new OperateLogInstance();
        expect.setBizNo(bizNo);
        expect.setOperator(operator);
        expect.setLogContent("打印自定义函数表达式, 【" + toLowerCaseFunction.apply(bizNo) + "】");
        Assertions.assertEquals(expect, operateLogStoreService.loadOperateLog());
    }

    public static class AhaoService {
        @OperateLog(success = "打印普通日志", bizNo = "#bizNo")
        public void normalOperateLog(String bizNo) {
            System.out.println("打印日志");
        }

        @OperateLog(success = "打印SpSL表达式, 业务编码【{#bizNo}】", bizNo = "#bizNo", operator = "#operator")
        public void spELOperateLog(String bizNo, String operator) {
            System.out.println("打印SpSL表达式");
        }

        // ⾃定义函数表达式需要⽤双层花括号{{}}来定义
        @OperateLog(success = "打印自定义函数表达式, 【{toLowerCase{#bizNo}}】", bizNo = "#bizNo", operator = "#operator")
        public void functionOperateLog(String bizNo, String operator) {
            System.out.println("打印自定义函数表达式");
        }
    }

    public static class ToLowerCaseFunction implements IParseFunction {
        @Override
        public String functionName() {
            return "toLowerCase" ;
        }

        @Override
        public String apply(Object value) {
            return value.toString().toLowerCase();
        }
    }
}
