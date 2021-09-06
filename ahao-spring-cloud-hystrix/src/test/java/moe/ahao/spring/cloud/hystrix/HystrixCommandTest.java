package moe.ahao.spring.cloud.hystrix;

import com.netflix.hystrix.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HystrixCommandTest {

    @DisplayName("获取一条数据")
    @Test
    public void hystrixCommandTest() throws Exception {
        String name = "hello";

        // 1. 同步方式
        HystrixCommand<String> command1 = new UppercaseHystrixCommand(name);
        String result1 = command1.execute();
        Assertions.assertEquals(name.toUpperCase(), result1);

        // 2. 异步方式
        HystrixCommand<String> command2 = new UppercaseHystrixCommand(name);
        String result2 = command2.queue().get();
        Assertions.assertEquals(name.toUpperCase(), result2);
    }

    @DisplayName("获取多条数据")
    @Test
    public void hystrixObservableCommandTest() throws Exception {
        String[] names = {"hello"};

        // 同步方式调用
        HystrixObservableCommand<String> command1 = new UppercaseHystrixObservableCommand(names);
        // command1.observe(): 立即执行 construct()
        // command2.toObservable(): 等subscribe才执行 construct()
        Iterator<String> iterator = command1.observe().toBlocking().toIterable().iterator();
        for (String name : names) {
            Assertions.assertEquals(name.toUpperCase(), iterator.next());
        }

        // 异步方式调用
        CountDownLatch latch = new CountDownLatch(1);
        HystrixObservableCommand<String> command2 = new UppercaseHystrixObservableCommand(names);
        Observable<String> observable = command2.toObservable();
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                latch.countDown();
            }

            @Override
            public void onError(Throwable e) {
                Assertions.fail(e);
            }

            @Override
            public void onNext(String s) {
                Assertions.assertEquals(names[0].toUpperCase(), s);
            }
        });
        Assertions.assertTrue(latch.await(1, TimeUnit.SECONDS));
    }

    @DisplayName("fallback降级")
    @Test
    public void fallbackTest() throws Exception {
        String name = "hello";

        HystrixCommand<String> command = new FallbackCommand(name);
        String result = command.execute();
        Assertions.assertEquals(name, result);
    }


    public class UppercaseHystrixCommand extends HystrixCommand<String> {
        private final String name;

        public UppercaseHystrixCommand(String name) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("uppercase-group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("uppercase-1"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("thread-pool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)) // 线程池
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10) // 设置线程池core大小
                    .withQueueSizeRejectionThreshold(15)) // 阻塞队列长度
            );
            this.name = name;
        }

        @Override
        protected String run() throws Exception {
            return name.toUpperCase();
        }
    }

    public class UppercaseHystrixObservableCommand extends HystrixObservableCommand<String> {
        private final String[] names;

        public UppercaseHystrixObservableCommand(String[] names) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("uppercase-group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("uppercase-2"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))); // 线程池
            this.names = names;
        }

        @Override
        protected Observable<String> construct() {
            return Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        for (String name : names) {
                            subscriber.onNext(name.toUpperCase());
                        }
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.io());
        }
    }

    public class FallbackCommand extends HystrixCommand<String> {
        private final String name;

        public FallbackCommand(String name) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("uppercase-group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("uppercase-4"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("thread-pool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)) // 线程池
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10) // 设置线程池core大小
                    .withQueueSizeRejectionThreshold(15)) // 阻塞队列长度
            );
            this.name = name;
        }
        @Override
        protected String run() throws Exception {
            throw new NullPointerException("错误");
        }

        @Override
        protected String getFallback() {
            return name;
        }
    }
}
