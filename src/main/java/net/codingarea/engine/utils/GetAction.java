package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class GetAction<T> {

	@Nonnull
	@CheckReturnValue
	public static <T> GetAction<T> of(final @Nonnull Supplier<T> supplier) {
		return new GetAction<>(supplier);
	}

	@Nonnull
	@CheckReturnValue
	public static <T> GetAction<T> of(final @Nullable T object) {
		return new GetAction<>(object);
	}

	private final Supplier<? extends T> supplier;
	private Supplier<? extends T> orElse;

	public GetAction(final @Nonnull Supplier<? extends T> supplier) {
		this.supplier = supplier;
	}

	public GetAction(final @Nullable T object) {
		this.supplier = () -> object;
	}

	public <V extends T> GetAction<T> orElse(final @Nullable V orElse) {
		this.orElse = () -> orElse;
		return this;
	}

	public GetAction<T> orElse(final @Nonnull Supplier<? extends T> orElse) {
		this.orElse = orElse;
		return this;
	}

	public T get() {
		T t = null;
		try {
			t = supplier.get();
		} catch (Exception ignored) { }
		return t != null ? t : orElse.get();
	}

}
