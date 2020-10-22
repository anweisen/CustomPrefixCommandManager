package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

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
			throw new ConsumeException(ex);
		}
	}

	void acceptThrowing(A a, B b) throws Exception;

}
