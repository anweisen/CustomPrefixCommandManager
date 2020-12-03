package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 *
 * @see java.util.function.BiConsumer
 */
@FunctionalInterface
public interface ThrowingBiConsumer<A, B> extends BiConsumer<A, B> {

	@Override
	default void accept(A a, B b) {
		try {
			acceptExceptionally(a, b);
		} catch (Throwable ex) {
			throw new ConsumeException(ex);
		}
	}

	void acceptExceptionally(A a, B b) throws Exception;

	@Nonnull
	@CheckReturnValue
	static <A, B> ThrowingBiConsumer<A, B> of(@Nonnull BiConsumer<A, B> consumer) {
		return consumer::accept;
	}

}
