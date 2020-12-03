package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.UnexpectedExecutionException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
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
			throw new UnexpectedExecutionException();
		}
	}

	boolean testExceptionally(T t) throws Exception;

	@Nonnull
	@CheckReturnValue
	static <T> ThrowingPredicate<T> of(@Nonnull Predicate<T> predicate) {
		return predicate::test;
	}

}
