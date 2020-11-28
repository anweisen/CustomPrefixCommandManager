package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 *
 * @see TripleConsumer
 */
@FunctionalInterface
public interface ThrowingTripleConsumer<A, B, C> extends TripleConsumer<A, B, C> {

	@Override
	default void accept(A a, B b, C c) {
		try {
			acceptExceptionally(a, b, c);
		} catch (Throwable ex) {
			throw new ConsumeException(ex);
		}
	}

	void acceptExceptionally(A a, B b, C c) throws Exception;

}
