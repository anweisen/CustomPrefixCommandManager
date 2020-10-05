package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public class LanguageNotFoundException extends RuntimeException {

	public LanguageNotFoundException() {
		super();
	}

	public LanguageNotFoundException(String message) {
		super(message);
	}

	public LanguageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LanguageNotFoundException(Throwable cause) {
		super(cause);
	}
}
