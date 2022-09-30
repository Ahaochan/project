package moe.ahao.unit.test.support.lambda;

/**
 * 属性设置器
 */
public interface PropertySetter<Func> {
	PropertySetter<Func> set(Func func, Object val);
}
