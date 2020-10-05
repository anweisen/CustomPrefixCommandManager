package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public class MessageException extends RuntimeException {

	public MessageException() {
		super();
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}

	public static void create(Throwable ex) {
		throw new MessageException(ex);
	}

}
