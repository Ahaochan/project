package moe.ahao.unit.test.support;

import moe.ahao.unit.test.support.lambda.PropertySetter;
import moe.ahao.unit.test.support.lambda.StdUtFunction;

public abstract class AbstractPropertySetter<T> implements PropertySetter<StdUtFunction<T,?>> {

	@Override
	public PropertySetter<StdUtFunction<T, ?>> set(StdUtFunction<T, ?> func, Object val) {
		return null;
	}
}
