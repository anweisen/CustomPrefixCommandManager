package net.codingarea.engine.exceptions;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public class LanguageNotFoundException extends RuntimeException {

	public LanguageNotFoundException(@Nonnull String language) {
		super("Invalid language \"" + language + "\"");
	}

}
