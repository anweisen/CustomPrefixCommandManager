package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public class SimilarCommandRegisteredException extends IllegalSubCommandNameException {

	public SimilarCommandRegisteredException(String name, int argCount) {
		super(name + ", " + argCount + " arguments");
	}

}
