package net.codingarea.engine.lang.support;

import net.codingarea.engine.lang.LanguageManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public final class StaticLanguageManagerHolder {

	private StaticLanguageManagerHolder() { }

	private static LanguageManager instance;

	@Nonnull
	public static LanguageManager setInstance(@Nonnull LanguageManager instance) {
		return StaticLanguageManagerHolder.instance = instance;
	}

	@Nonnull
	@CheckReturnValue
	public static LanguageManager getInstance() {
		return instance;
	}

	@CheckReturnValue
	public static boolean hasInstance() {
		return instance != null;
	}

}
