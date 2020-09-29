package net.codingarea.botmanager.commandmanager.helper;

import net.codingarea.botmanager.defaults.DefaultLogger;
import net.codingarea.botmanager.utils.LogLevel;
import sun.reflect.CallerSensitive;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public abstract class LogHelper {

	@CallerSensitive
	public static void log(@Nonnull Level level, @Nonnull String message) {
		DefaultLogger.logDefault(level, message);
	}

	public static void log(@Nonnull Level level, Class<?> caller, @Nonnull String message) {
		DefaultLogger.logDefault(level, caller, message);
	}

	public static void log(@Nonnull Level level, String caller, @Nonnull String message) {
		DefaultLogger.logDefault(level, caller, message);
	}

	public static void log(@Nonnull Throwable thrown, Class<?> caller) {
		DefaultLogger.logDefault(thrown, caller);
	}

	public static void info(@Nonnull String message) {
		LogHelper.log(LogLevel.INFO, message);
	}

	public static void error(@Nonnull String message) {
		LogHelper.log(LogLevel.ERROR, message);
	}

	public static void debug(@Nonnull String message) {
		LogHelper.log(LogLevel.DEBUG, message);
	}

	public static void status(@Nonnull String message) {
		LogHelper.log(LogLevel.STATUS, message);
	}

	public static void warning(@Nonnull String message) {
		LogHelper.log(LogLevel.WARNING, message);
	}

	public static void info(Object message) {
		LogHelper.log(LogLevel.INFO, String.valueOf(message));
	}

	public static void error(Throwable ex) {
		LogHelper.log(ex, null);
	}

	public static void error(Object message) {
		LogHelper.log(LogLevel.ERROR, String.valueOf(message));
	}

	public static void debug(Object message) {
		LogHelper.log(LogLevel.DEBUG, String.valueOf(message));
	}

	public static void status(Object message) {
		LogHelper.log(LogLevel.STATUS, String.valueOf(message));
	}

	public static void warning(Object message) {
		LogHelper.log(LogLevel.WARNING, String.valueOf(message));
	}

}
