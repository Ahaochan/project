package moe.ahao.spring.cloud.hystrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class RequestCollapserTest {

    @Test
    public void test() throws Exception {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();

        try {
            List<String> args = Arrays.asList("hello", "world", "ahao", "chan");

            List<Future<String>> futures = args.stream()
                .map(UppercaseHystrixCollapser::new)
                .map(HystrixCollapser::queue)
                .collect(Collectors.toList());

            for (int i = 0; i < args.size(); i++) {
                Assertions.assertEquals(args.get(i).toUpperCase(), futures.get(i).get());
            }

            Assertions.assertEquals(1, HystrixRequestLog.getCurrentRequest().getExecutedCommands().size());
            HystrixCommand<?> command = HystrixRequestLog.getCurrentRequest().getExecutedCommands().toArray(new HystrixCommand<?>[0])[0];
            Assertions.assertEquals("uppercase", command.getCommandKey().name());
            Assertions.assertTrue(command.getExecutionEvents().contains(HystrixEventType.COLLAPSED));
            Assertions.assertTrue(command.getExecutionEvents().contains(HystrixEventType.SUCCESS));
        } finally {
            context.shutdown();
        }


    }

    public static class UppercaseHystrixCollapser extends HystrixCollapser<List<String>, String, String> {
        private final String arg;

        public UppercaseHystrixCollapser(String key) {
            super(HystrixCollapser.Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("uppercase-collapser"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter()
                    .withMaxRequestsInBatch(100)
                    .withTimerDelayInMilliseconds(200))
            );
            this.arg = key;
        }

        @Override
        public String getRequestArgument() {
            return arg;
        }

        @Override
        protected HystrixCommand<List<String>> createCommand(Collection<CollapsedRequest<String, String>> requests) {
            return new UppercaseBatchHystrixCommand(requests);
        }

        @Override
        protected void mapResponseToRequests(List<String> batchResponse, Collection<CollapsedRequest<String, String>> requests) {
            int count = 0;
            for (CollapsedRequest<String, String> request : requests) {
                request.setResponse(batchResponse.get(count++));
            }
        }
    }

    public static class UppercaseBatchHystrixCommand extends HystrixCommand<List<String>> {
        private final Collection<HystrixCollapser.CollapsedRequest<String, String>> requests;

        public UppercaseBatchHystrixCommand(Collection<HystrixCollapser.CollapsedRequest<String, String>> requests) {
            super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("uppercase-group"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("uppercase"))
            );
            this.requests = requests;
        }

        @Override
        protected List<String> run() throws Exception {
            return requests.stream()
                .map(HystrixCollapser.CollapsedRequest::getArgument)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        }
    }
}
