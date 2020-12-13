package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public class ConsumeException extends UnexpectedExecutionException {

	public ConsumeException() {
		super();
	}

	public ConsumeException(String message) {
		super(message);
	}

	public ConsumeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConsumeException(Throwable cause) {
		super(cause);
	}

}
