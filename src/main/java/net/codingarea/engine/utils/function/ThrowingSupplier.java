package net.codingarea.engine.utils.function;

import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public interface ThrowingSupplier<T> extends Supplier<T> {

	@Override
	default T get() {
		try {
			return getThrowing();
		} catch (Throwable ignored) {
			return null;
		}
	}

	T getThrowing() throws Exception;

}
