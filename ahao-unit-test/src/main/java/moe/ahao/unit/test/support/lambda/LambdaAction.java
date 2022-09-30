package moe.ahao.unit.test.support.lambda;


public interface LambdaAction<T> {
    LambdaDataTemplate<T> lambda();
    T build();
}
