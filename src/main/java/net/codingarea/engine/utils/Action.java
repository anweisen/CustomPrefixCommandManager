package net.codingarea.engine.utils;

import net.codingarea.engine.utils.function.ThrowingConsumer;
import net.codingarea.engine.utils.function.ThrowingRunnable;
import net.codingarea.engine.utils.function.ThrowingSupplier;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

	public static void repeat(final int times, final @Nonnull Runnable action) {
		for (int i = 0; i < times; i++) {
			action.run();
		}
	}

	public static void repeat(final int times, final @Nonnull ThrowingRunnable action) throws Exception {
		for (int i = 0; i < times; i++) {
			action.runThrowing();
		}
	}

	public static <T> void repeat(final int times, T t, final @Nonnull Consumer<? super T> action) {
		for (int i = 0; i < times; i++) {
			action.accept(t);
		}
	}

	public static <T> void repeat(final int times, T t, final @Nonnull ThrowingConsumer<? super T> action) throws Exception {
		for (int i = 0; i < times; i++) {
			action.acceptThrowing(t);
		}
	}

	@SafeVarargs
	public static <T> void execute(final @Nonnull Predicate<? super T> filter, final @Nonnull Consumer<? super T> action, final @Nonnull T... t) {
		for (T current : t) {
			if (filter.test(current))
				action.accept(current);
		}
	}

	public static void doIf(final boolean _if, final @Nonnull Runnable action) {
		if (_if)
			action.run();
	}

	public static <T> void ifPresent(final @Nullable T object, final @Nonnull Consumer<? super T> action) {
		doIf(object != null, () -> action.accept(object));
	}

	public static long count(final @Nonnull BooleanSupplier action) {
		long count = 0;
		while (action.getAsBoolean()) {
			count++;
			if (count >= Long.MAX_VALUE)
				break;
		}
		return count;
	}

	@Nonnull
	@CheckReturnValue
	public static <T> GetAction<T> get(final @Nonnull Supplier<T> supplier) {
		return new GetAction<>(supplier);
	}

	@Nonnull
	@CheckReturnValue
	public static <T> GetAction<T> get(final @Nullable T object) {
		return new GetAction<>(object);
	}

	public static void silent(final @Nonnull Runnable action) {
		try {
			action.run();
		} catch (Exception ignored) { }
	}

}
