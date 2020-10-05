package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public class CommandExecutionException extends ExecutionException {

	public CommandExecutionException() {
		super();
	}

	public CommandExecutionException(String message) {
		super(message);
	}

	public CommandExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandExecutionException(Throwable cause) {
		super(cause);
	}

}
