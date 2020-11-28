package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 *
 * @see java.util.function.Function
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

	@Nonnull
	@Override
	default R apply(T t) {
		try {
			return applyExceptionally(t);
		} catch (Exception ex) {
			throw new ConsumeException(ex);
		}
	}

	R applyExceptionally(T t) throws Exception;

}
