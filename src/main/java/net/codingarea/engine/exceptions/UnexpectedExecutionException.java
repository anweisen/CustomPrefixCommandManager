package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public class UnexpectedExecutionException extends RuntimeException {

	public UnexpectedExecutionException() {
		super();
	}

	public UnexpectedExecutionException(String message) {
		super(message);
	}

	public UnexpectedExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnexpectedExecutionException(Throwable cause) {
		super(cause);
	}

}
