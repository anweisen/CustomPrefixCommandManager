package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ExecutionException;

import java.util.function.Predicate;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 *
 * @see java.util.function.Predicate
 */
@FunctionalInterface
public interface ThrowingPredicate<T> extends Predicate<T> {

	@Override
	default boolean test(T t) {
		try {
			return testExceptionally(t);
		} catch (Exception ignored) {
			throw new ExecutionException();
		}
	}

	boolean testExceptionally(T t) throws Exception;

}
