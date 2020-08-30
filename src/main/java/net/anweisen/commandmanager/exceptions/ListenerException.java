package net.anweisen.commandmanager.exceptions;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * This exception is thrown when an unexpected {@link java.lang.Exception} happens while executing
 * a listener's method
 *
 * @see net.anweisen.commandmanager.listener.Listener
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
public class ListenerException extends RuntimeException {

	public ListenerException() {
		super();
	}

	public ListenerException(String message) {
		super(message);
	}

	public ListenerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ListenerException(Throwable cause) {
		super(cause);
	}

}
