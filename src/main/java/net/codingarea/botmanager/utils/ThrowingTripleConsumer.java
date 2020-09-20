package net.codingarea.botmanager.utils;

import net.codingarea.botmanager.exceptions.ConsumerExecutionException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public interface ThrowingTripleConsumer<A, B, C> extends TripleConsumer<A, B, C> {

	@Override
	default void accept(A a, B b, C c) {
		try {
			acceptThrowing(a, b, c);
		} catch (Throwable ex) {
			throw new ConsumerExecutionException(ex);
		}
	}

	void acceptThrowing(A a, B b, C c);

}
