package moe.ahao.unit.test.support.lambda;


import moe.ahao.unit.test.support.SerializedLambdaUtil;
import moe.ahao.unit.test.support.template.AbstractDataTemplate;

import java.lang.reflect.Field;

public class LambdaDataTemplate<T> extends AbstractDataTemplate<T> implements PropertySetter<StdUtFunction<T,?>> {
    private final T data;
    public LambdaDataTemplate(T data) {
        this.data = data;
    }

    /**
     * 通过lambda设置属性
     */
    @Override
    public LambdaDataTemplate<T> set(StdUtFunction<T, ?> func, Object val) {
        String fieldName = SerializedLambdaUtil.resolveProperty(func);
        this.setFieldValueByFieldName(fieldName, data, val);
        return this;
    }

    @Override
    public T build() {
        return data;
    }


    private void setFieldValueByFieldName(String fieldName, Object object, Object value) {
        if (null == object) {
            throw new UnsupportedOperationException("【LambdaDataTemplate】：object为null ！");
        }
        try {
            Class<?> clz = object.getClass();
            Field f = clz.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(object, value);
            f.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("【LambdaDataTemplate】：根据属性名设置属性值异常 ！");
        }
    }


}
