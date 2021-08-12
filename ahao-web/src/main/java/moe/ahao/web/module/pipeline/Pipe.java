package moe.ahao.web.module.pipeline;

import java.util.concurrent.TimeUnit;

public interface Pipe<IN, OUT> {
    void setNextPipe(Pipe<?, ?> nextPipe);
    void init(PipeContext pipeContext);
    void shutdown(long timeout, TimeUnit unit);
    void process(IN in) throws InterruptedException;
}
