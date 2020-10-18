package net.codingarea.engine.exceptions;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class IllegalSubCommandException extends IllegalStateException {

	public IllegalSubCommandException(String message, @Nonnull Method method) {
		super(message + ": " + method.toString());
	}

	public IllegalSubCommandException(@Nonnull Method method) {
		super(method.toString());
	}

	public IllegalSubCommandException(String cause) {
		super(cause);
	}

}
