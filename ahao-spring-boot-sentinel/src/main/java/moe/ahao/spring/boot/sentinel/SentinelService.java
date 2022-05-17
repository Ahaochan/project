package moe.ahao.spring.boot.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import moe.ahao.util.commons.lang.RandomHelper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SentinelService {
    @SentinelResource(value = "test", blockHandler = "exceptionHandler", fallback = "testFallback")
    public String test(String prefix) {
        if(RandomHelper.getInt(100) % 2 == 0) {
            throw new RuntimeException("发生异常");
        }
        return prefix + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
    public String exceptionHandler(String prefix, BlockException e) {
        e.printStackTrace();
        return prefix + "发生熔断降级block: " + DateFormatUtils.format(new Date(), "yyyy-MM-dd");
    }
    public String testFallback(String prefix, Throwable throwable) {
        throwable.printStackTrace();
        return prefix + "发生异常fallback: " + DateFormatUtils.format(new Date(), "yyyy-MM-dd");
    }



    // // 这里单独演示 blockHandlerClass 的配置.
    // // 对应的 `handleException` 函数需要位于 `ExceptionUtil` 类中，并且必须为 public static 函数.
    // @SentinelResource(value = "test", blockHandler = "handleException", blockHandlerClass = SentinelExceptionHandler.class)
    // public void test() {
    //     System.out.println("Test");
    // }
}
