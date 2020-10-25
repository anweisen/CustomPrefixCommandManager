package net.codingarea.engine.exceptions;

import java.lang.reflect.Method;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public class InvalidListenerException extends ListenerException {

	public InvalidListenerException(Method method) {
		super(String.valueOf(method));
	}

}
