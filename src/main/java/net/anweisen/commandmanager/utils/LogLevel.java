package net.anweisen.commandmanager.utils;

import java.util.logging.Level;

/**
 * Developed in the CommandManager project
 * on 09-06-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class LogLevel extends Level {

	public static final LogLevel ERROR = new LogLevel("ERROR", Level.SEVERE),
								 DEBUG = new LogLevel("DEBUG", Level.FINEST),
								 STATUS = new LogLevel("STATUS", Level.INFO);

	private LogLevel(String name, int value) {
		super(name, value);
	}

	private LogLevel(String name, Level parent) {
		super(name, parent.intValue());
	}

}
