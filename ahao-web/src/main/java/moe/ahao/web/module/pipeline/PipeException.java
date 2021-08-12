package moe.ahao.web.module.pipeline;

public class PipeException extends RuntimeException {
    public <IN, OUT> PipeException(Pipe<IN, OUT> pipe, IN in, String s, Exception e) {

    }
}
