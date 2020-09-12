package net.anweisen.commandmanager.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class Replacement {

	private final String origin, replacement;

	public Replacement(@Nonnull String origin, @Nonnull String replacement) {
		this.origin = origin;
		this.replacement = replacement;
	}

	@Nonnull
	public String getOrigin() {
		return origin;
	}

	@Nonnull
	public String getReplacement() {
		return replacement;
	}

	@Nonnull
	@CheckReturnValue
	public String replace(@Nonnull String string) {
		return string.replace(origin, replacement);
	}

	@Nonnull
	@CheckReturnValue
	public static String replaceAll(@Nonnull String string, @Nonnull Replacement... replacements) {
		for (Replacement replacement : replacements) {
			string = replacement.replace(string);
		}
		return string;
	}

}
