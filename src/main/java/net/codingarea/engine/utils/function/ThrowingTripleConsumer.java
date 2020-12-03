package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 *
 * @see TripleConsumer
 */
@FunctionalInterface
public interface ThrowingTripleConsumer<A, B, C> extends TripleConsumer<A, B, C> {

	void acceptExceptionally(A a, B b, C c) throws Exception;

	@Override
	default void accept(A a, B b, C c) {
		try {
			acceptExceptionally(a, b, c);
		} catch (Throwable ex) {
			throw new ConsumeException(ex);
		}
	}

	@Nonnull
	@CheckReturnValue
	static <A, B, C> ThrowingTripleConsumer<A, B, C> of(@Nonnull TripleConsumer<A, B, C> consumer) {
		return consumer::accept;
	}

}
