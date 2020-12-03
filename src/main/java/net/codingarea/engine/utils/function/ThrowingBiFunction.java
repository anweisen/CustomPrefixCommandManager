package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.UnexpectedExecutionException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.BiFunction;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> extends BiFunction<T, U, R> {

	@Nonnull
	@Override
	default R apply(T t, U u) {
		try {
			return applyExceptionally(t, u);
		} catch (Exception ex) {
			throw new UnexpectedExecutionException(ex);
		}
	}

	R applyExceptionally(T t, U u) throws Exception;

	@Nonnull
	@CheckReturnValue
	static <R, A, B> ThrowingBiFunction<R, A, B> of(@Nonnull BiFunction<R, A, B> function) {
		return function::apply;
	}

}
