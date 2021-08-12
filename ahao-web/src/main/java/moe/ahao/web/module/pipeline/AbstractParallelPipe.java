package moe.ahao.web.module.pipeline;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class AbstractParallelPipe<IN, OUT, V> extends AbstractPipe<IN, OUT> {
    private final ExecutorService executorService;

    protected AbstractParallelPipe(BlockingQueue<IN> queue, ExecutorService executorService) {
        super();
        this.executorService = executorService;
    }

    protected abstract List<Callable<V>> buildTasks(IN input) throws Exception;
    protected List<Future<V>> invokeParallel(List<Callable<V>> tasks) throws Exception {
        return executorService.invokeAll(tasks);
    };
    protected abstract OUT combineResults(List<Future<V>> results) throws Exception;

    @Override
    protected OUT doProcess(IN in) throws PipeException {
        try {
            List<Callable<V>> tasks = this.buildTasks(in);
            List<Future<V>> results = this.invokeParallel(tasks);
            OUT out = this.combineResults(results);
            return out;
        } catch (Exception e) {
            pipeContext.handlerError(new PipeException(this, in, "Task failed", e));
        }
        return null;
    }
}
