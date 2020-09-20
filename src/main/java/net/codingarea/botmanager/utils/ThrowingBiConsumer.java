package net.codingarea.botmanager.utils;

import net.codingarea.botmanager.exceptions.ConsumerExecutionException;

import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public interface ThrowingBiConsumer<A, B> extends BiConsumer<A, B> {

	@Override
	default void accept(A a, B b) {
		try {
			acceptThrowing(a, b);
		} catch (Throwable ex) {
			throw new ConsumerExecutionException(ex);
		}
	}

	void acceptThrowing(A a, B b) throws Throwable;

}
