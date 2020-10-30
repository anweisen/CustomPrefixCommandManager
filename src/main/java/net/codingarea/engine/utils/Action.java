package net.codingarea.engine.utils;

import net.codingarea.engine.utils.function.ThrowingConsumer;
import net.codingarea.engine.utils.function.ThrowingRunnable;
import net.codingarea.engine.utils.function.ThrowingSupplier;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public final class Action {

	@CheckReturnValue
	public static <T> T complete(final @Nonnull ThrowingSupplier<T> action) {
		return action.get();
	}

	public static void complete(final @Nonnull ThrowingRunnable action) {
		action.run();
	}

	public static void repeat(int times, Runnable action) {
		for (int i = 0; i < times; i++) {
			action.run();
		}
	}

	public static void repeat(int times, ThrowingRunnable action) throws Exception {
		for (int i = 0; i < times; i++) {
			action.runThrowing();
		}
	}

	public static <T> void repeat(int times, T t, @Nonnull Consumer<? super T> action) {
		for (int i = 0; i < times; i++) {
			action.accept(t);
		}
	}

	public static <T> void repeat(int times, T t, @Nonnull ThrowingConsumer<? super T> action) throws Exception {
		for (int i = 0; i < times; i++) {
			action.acceptThrowing(t);
		}
	}

	@SafeVarargs
	public static <T> void execute(@Nonnull Predicate<? super T> filter, @Nonnull Consumer<? super T> action, @Nonnull T... t) {
		for (T current : t) {
			if (filter.test(current))
				action.accept(current);
		}
	}

}
