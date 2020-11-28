package net.codingarea.engine.utils.function;

import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 *
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {

	@Override
	default T get() {
		try {
			return getExceptionally();
		} catch (Throwable ignored) {
			return null;
		}
	}

	T getExceptionally() throws Exception;

}
