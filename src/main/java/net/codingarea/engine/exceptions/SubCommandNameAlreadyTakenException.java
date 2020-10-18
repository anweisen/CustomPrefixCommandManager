package net.codingarea.engine.exceptions;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public class SubCommandNameAlreadyTakenException extends IllegalSubCommandNameException {

	public SubCommandNameAlreadyTakenException(String name) {
		super(name);
	}

}
