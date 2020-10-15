package net.codingarea.engine.utils;

import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class LogLevel extends Level {

	public static final LogLevel ERROR = new LogLevel("ERROR", Level.SEVERE),
								 DEBUG = new LogLevel("DEBUG", Level.FINEST),
								 STATUS = new LogLevel("STATUS", Level.INFO),
	                             NONE = new LogLevel("NONE", Integer.MAX_VALUE);

	private LogLevel(String name, int value) {
		super(name, value);
	}

	private LogLevel(String name, Level parent) {
		super(name, parent.intValue());
	}

}
