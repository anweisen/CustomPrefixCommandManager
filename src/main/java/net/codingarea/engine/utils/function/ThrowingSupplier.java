package net.codingarea.engine.utils.function;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 *
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {

	T getExceptionally() throws Exception;

	@Override
	default T get() {
		try {
			return getExceptionally();
		} catch (Throwable ignored) {
			return null;
		}
	}

	@Nonnull
	@CheckReturnValue
	static <T> ThrowingSupplier<T> of(@Nonnull Supplier<T> supplier) {
		return supplier::get;
	}

}
