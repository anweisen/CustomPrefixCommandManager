package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public class ExecutionException extends RuntimeException {

	public ExecutionException() {
		super();
	}

	public ExecutionException(String message) {
		super(message);
	}

	public ExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecutionException(Throwable cause) {
		super(cause);
	}
}
