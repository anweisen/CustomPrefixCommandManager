package net.codingarea.engine.exceptions;

import java.lang.reflect.Method;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public class SimilarArgumentParserRegisteredException extends IllegalArgumentParserException {

	public SimilarArgumentParserRegisteredException(Class<?> argument, Method method) {
		super("Argument " + argument + " already registered", method);
	}

}
