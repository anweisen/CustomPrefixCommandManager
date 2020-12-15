package net.codingarea.engine.exceptions;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public class NoSuchTranslationException extends RuntimeException {

	public NoSuchTranslationException(@Nonnull String translation) {
		super("Invalid translation: \"" + translation + "\"");
	}

}
