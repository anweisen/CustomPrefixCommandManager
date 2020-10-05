package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class Replacement {

	private final String origin;
	private final Supplier<String> replacement;

	public Replacement(@Nonnull String origin, Object replacement) {
		this(origin, () -> String.valueOf(replacement));
	}

	public Replacement(@Nonnull String origin, String replacement) {
		this(origin, () -> String.valueOf(replacement));
	}

	public Replacement(@Nonnull String origin, @Nonnull Supplier<String> replacement) {
		this.origin = origin;
		this.replacement = replacement;
	}

	@Nonnull
	public String getOrigin() {
		return origin;
	}

	@Nonnull
	public String getReplacement() {
		return replacement.get();
	}

	@Nonnull
	@CheckReturnValue
	public String replace(@Nonnull String string) {
		return string.replace(origin, replacement.get());
	}

	@Nonnull
	@CheckReturnValue
	public static String replaceAll(@Nonnull String string, Replacement... replacements) {
		if (replacements != null) {
			for (Replacement replacement : replacements) {
				string = replacement.replace(string);
			}
		}
		return string;
	}

}
