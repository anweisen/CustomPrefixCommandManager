package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public class IllegalTypeException extends IllegalArgumentException {

	public IllegalTypeException() {
		super();
	}

	public IllegalTypeException(String message) {
		super(message);
	}

	public IllegalTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalTypeException(Throwable cause) {
		super(cause);
	}

}
