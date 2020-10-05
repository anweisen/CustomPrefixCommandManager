package net.codingarea.engine.utils;

import sun.reflect.CallerSensitive;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public abstract class LogHelper {

	@CallerSensitive
	public static void log(@Nonnull Level level, String message) {
		LogHelper.log(level, Utils.caller(5), message);
	}

	public static void log(@Nonnull Level level, Class<?> caller, String message) {
		DefaultLogger.DEFAULT.log(level, caller, message);
	}

	public static void log(@Nonnull Level level, Class<?> caller, Throwable ex) {
		DefaultLogger.DEFAULT.log(level, caller, ex != null ? Utils.exceptionToString(ex) : null);
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
		LogHelper.log(LogLevel.ERROR, ex != null ? Utils.exceptionToString(ex) : null);
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
