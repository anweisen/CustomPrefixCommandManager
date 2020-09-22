package net.codingarea.botmanager.utils;

import net.codingarea.botmanager.exceptions.ConsumeException;

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
