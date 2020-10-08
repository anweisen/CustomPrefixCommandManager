package net.codingarea.engine.exceptions;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class IllegalSubCommandException extends IllegalStateException {

	public IllegalSubCommandException(@Nonnull Method method) {
		super(method.toString());
	}

	protected IllegalSubCommandException(String cause) {
		super(cause);
	}
}
