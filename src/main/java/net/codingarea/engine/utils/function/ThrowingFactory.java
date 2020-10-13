package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;
import net.codingarea.engine.utils.function.Factory;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public interface ThrowingFactory<R, T> extends Factory<R, T> {

	@Nonnull
	@Override
	default R get(T t) {
		try {
			return getThrowing(t);
		} catch (Throwable ex) {
			throw new ConsumeException(ex);
		}
	}

	R getThrowing(T t);

}
