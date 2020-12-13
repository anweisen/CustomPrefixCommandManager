package net.codingarea.engine.utils;

import net.codingarea.engine.utils.function.ThrowingConsumer;
import net.codingarea.engine.utils.function.ThrowingRunnable;
import net.codingarea.engine.utils.function.ThrowingSupplier;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public final class Action<T> {

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
			action.runExceptionally();
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

	public static void doIf(final boolean condition, final @Nonnull Runnable action) {
		if (condition)
			action.run();
	}

	public static <T> void ifPresent(final @Nullable T object, final @Nonnull Consumer<? super T> action) {
		doIf(object != null, () -> action.accept(object));
	}

	public static long count(final @Nonnull BooleanSupplier action) {
		long count;
		for (count = 0; action.getAsBoolean(); count++);
		return count;
	}

	public static void silent(final @Nonnull Runnable action) {
		try {
			action.run();
		} catch (Exception ignored) { }
	}

	@Nonnull
	@CheckReturnValue
	public static <T> Optional<T> silentOptional(final @Nonnull Supplier<? extends T> supplier) {
		try {
			T value = supplier.get();
			return Optional.ofNullable(value);
		} catch (Exception ignored) {
			return Optional.empty();
		}
	}


	@Nonnull
	@CheckReturnValue
	public static <T> Optional<T> silentOptional(final @Nonnull ThrowingSupplier<? extends T> supplier) {
		return silentOptional((Supplier<? extends T>) supplier);
	}

	@Nullable
	@CheckReturnValue
	public static <T> T getSilent(final @Nonnull Supplier<? extends T> supplier) {
		try {
			return supplier.get();
		} catch (Exception ignored) { }
		return null;
	}

	public static void execute(final @Nonnull Runnable action, final @Nonnull Consumer<? super Throwable> onError) {
		try {
			action.run();
		} catch (Throwable ex) {
			onError.accept(ex);
		}
	}

	public static void execute(final @Nonnull Runnable action) {
		execute(action, Throwable::printStackTrace);
	}

	@Nullable
	@CheckReturnValue
	public static <T> T getSilent(final @Nonnull ThrowingSupplier<? extends T> supplier) {
		return getSilent((Supplier<? extends T>) supplier);
	}

	@CheckReturnValue
	public static <R, T> R getOrElse(final @Nullable T origin, final @Nonnull Function<? super T, ? extends R> mapper,
	                                 final @Nullable R orElse) {
		if (origin != null) {
			try {
				R created = mapper.apply(origin);
				if (created != null)
					return created;
			} catch (Exception ignored) {
				/* Ignoring any exceptions while mapping */
			}
		}

		return orElse;

	}

}
