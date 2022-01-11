package moe.ahao.maven.archetype.api.resp;

public class Result<T> {
    private final T data;
    public Result(T data) {
        this.data = data;
    }
}
