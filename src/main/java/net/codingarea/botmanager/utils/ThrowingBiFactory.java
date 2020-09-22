package net.codingarea.botmanager.utils;

import net.codingarea.botmanager.exceptions.ConsumeException;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public interface ThrowingBiFactory<R, A, B> extends BiFactory<R, A, B> {

	@Nonnull
	@Override
	default R get(A a, B b) {
		try {
			return getThrowing(a, b);
		} catch (Throwable ex) {
			throw new ConsumeException(ex);
		}
	}

	R getThrowing(A a, B b);

}
