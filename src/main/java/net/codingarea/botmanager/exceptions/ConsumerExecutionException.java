package net.codingarea.botmanager.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public class ConsumerExecutionException extends RuntimeException {

	public ConsumerExecutionException() {
		super();
	}

	public ConsumerExecutionException(String message) {
		super(message);
	}

	public ConsumerExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConsumerExecutionException(Throwable cause) {
		super(cause);
	}
}
