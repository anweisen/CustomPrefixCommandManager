package net.anweisen.commandmanager.exceptions;

import net.anweisen.commandmanager.listener.Listener;

/**
 * This exception is thrown when an unexpected {@link Throwable} happens while executing
 * a {@link Listener} method
 *
 * @see Listener
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
