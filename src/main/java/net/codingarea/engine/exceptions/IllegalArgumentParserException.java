package net.codingarea.engine.exceptions;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public class IllegalArgumentParserException extends RuntimeException {

	public IllegalArgumentParserException(final @Nonnull String message, final @Nonnull Method method) {
		super(message + ": " + method.getName());
	}

}
