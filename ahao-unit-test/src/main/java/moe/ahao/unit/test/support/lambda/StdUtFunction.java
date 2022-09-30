package moe.ahao.unit.test.support.lambda;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface StdUtFunction<T, R> extends Function<T, R>, Serializable {
}
