package moe.ahao.unit.test.support.template;


import moe.ahao.unit.test.support.lambda.LambdaAction;
import moe.ahao.unit.test.support.lambda.LambdaDataTemplate;

/**
 * 抽象数据模板基类
 */
public class AbstractDataTemplate<T> implements DataTemplate<T>, LambdaAction<T> {

    protected T data;

    @Override
    public T build() {
        return data;
    }

    @Override
    public LambdaDataTemplate<T> lambda() {
        return new LambdaDataTemplate<>(data);
    }


    protected void setData(T data) {
        this.data = data;
    }

}
