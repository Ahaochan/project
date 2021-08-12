package moe.ahao.web.module.pipeline;

import java.util.concurrent.TimeUnit;

public abstract class AbstractPipe<IN, OUT> implements Pipe<IN, OUT> {
    protected volatile Pipe<?, ?> nextPipe;
    protected volatile PipeContext pipeContext;


    @Override
    public void init(PipeContext pipeContext) {
        this.pipeContext = pipeContext;
    }

    @Override
    public void setNextPipe(Pipe<?, ?> nextPipe) {
        this.nextPipe = nextPipe;
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {
    }

    protected abstract OUT doProcess(IN in) throws PipeException;

    @Override
    public void process(IN in) throws InterruptedException {
        try {
            OUT out = this.doProcess(in);
            if(nextPipe != null && out != null) {
                ((Pipe<OUT, ?>) nextPipe).process(out);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (PipeException e) {
            pipeContext.handlerError(e);
        } catch (Exception e) {
            pipeContext.handlerError(new PipeException(this, in, "", e));
        }
    }
}
