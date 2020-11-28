package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ExecutionException;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 *
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
public interface ThrowingBiFunction<R, A, B> extends BiFunction<A, B, R> {

	@Nonnull
	@Override
	default R apply(A a, B b) {
		try {
			return applyExceptionally(a, b);
		} catch (Exception ex) {
			throw new ExecutionException(ex);
		}
	}

	R applyExceptionally(A a, B b) throws Exception;

}
