package moe.ahao.spring.cloud.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LevelHystrixCommandTest {

    @Test
    public void test() throws Exception {
        String name = "hello";

        HystrixCommand<String> command = new FirstHystrixCommand(name);
        String result = command.execute();
        Assertions.assertEquals(name.toUpperCase(), result);
    }

    public class FirstHystrixCommand extends HystrixCommand<String> {
        private final String name;

        public FirstHystrixCommand(String name) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("uppercase-group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("uppercase"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("thread-pool-1"))
            );
            this.name = name;
        }

        @Override
        protected String run() throws Exception {
            throw new Exception("抛出异常");
        }

        @Override
        protected String getFallback() {
            return new UppercaseHystrixCommand(name).execute();
        }
    }


    public class UppercaseHystrixCommand extends HystrixCommand<String> {
        private final String name;

        public UppercaseHystrixCommand(String name) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("uppercase-group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("uppercase"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("thread-pool-2")) // 线程池一定要和FirstHystrixCommand不一致
            );
            this.name = name;
        }

        @Override
        protected String run() throws Exception {
            return name.toUpperCase();
        }
    }
}
