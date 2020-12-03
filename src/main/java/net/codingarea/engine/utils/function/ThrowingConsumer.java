package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 *
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

	@Override
	default void accept(T t) {
		try {
			acceptThrowing(t);
		} catch (Throwable ex) {
			throw new ConsumeException(ex);
		}
	}

	void acceptThrowing(T t) throws Exception;

	@Nonnull
	@CheckReturnValue
	static <T> ThrowingConsumer<T> of(@Nonnull Consumer<T> consumer) {
		return consumer::accept;
	}

}
