package moe.ahao.web.module.pipeline;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ThreadPoolPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {
    private Pipe<IN, OUT> delegate;
    private ExecutorService executorService;
    private AtomicInteger count;

    public ThreadPoolPipeDecorator(Pipe<IN, OUT> delegate, ExecutorService executorService) {
        this.delegate = delegate;
        this.executorService = executorService;
        this.count = new AtomicInteger(0);
    }

    @Override
    public void init(PipeContext pipeContext) {
        this.delegate.init(pipeContext);
    }

    @Override
    public void setNextPipe(Pipe<?, ?> nextPipe) {
        this.delegate.setNextPipe(nextPipe);
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        if()

        this.delegate.shutdown(timeout, unit);
    }

    @Override
    public void process(IN in) throws InterruptedException {
        Runnable task = () -> {
            try {
                delegate.process(in);
            } catch (InterruptedException e) {

            } finally {
                count.decrementAndGet();
            }
        };

        executorService.submit(task);
        count.incrementAndGet();

    }
}
