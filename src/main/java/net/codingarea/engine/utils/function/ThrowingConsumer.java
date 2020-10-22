package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
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

}
