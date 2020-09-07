package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.utils.Bindable;
import net.anweisen.commandmanager.utils.LogLevel;
import sun.reflect.Reflection;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Developed in the CommandManager project
 * on 09-05-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultLogger extends Logger implements Bindable {

	public static final DefaultLogger DEFAULT = new DefaultLogger("default");

	public static void logDefault(Level level, Class<?> caller, String message) {
		LogRecord record = new LogRecord(level, message);
		record.setSourceClassName(caller.getSimpleName());
		DEFAULT.log(record);
	}

	public static void logDefault(Level level, String message) {
		logDefault(level, Reflection.getCallerClass(), message);
	}

	private final DefaultLogHandler handler;

	public DefaultLogger(String name) {
		this(name, null, null);
	}

	public DefaultLogger(@Nonnull Object caller) {
		this(caller.getClass());
	}

	public DefaultLogger(@Nonnull Class<?> caller) {
		this(caller.getName(), null, caller);
	}

	public DefaultLogger(@Nonnull Class<?> caller, @Nonnull PrintStream stream) {
		this(caller.getName(), stream, caller);
	}

	public DefaultLogger(@Nonnull String name, PrintStream stream, Class<?> caller) {
		super(name, null);
		handler = new DefaultLogHandler(stream != null ? stream : System.err, caller);
		addHandler(handler);
	}

	public DefaultLogHandler getDefaultHandler() {
		return handler;
	}

	public void debug(String message) {
		log(LogLevel.DEBUG,  message);
	}

	public void debug(Supplier<String> message) {
		log(LogLevel.DEBUG,  message);
	}

	public void error(String message) {
		log(LogLevel.DEBUG,  message);
	}

	public void error(Supplier<String> message) {
		log(LogLevel.ERROR,  message);
	}

	public void log(Level level, Class<?> caller, String message) {
		LogRecord record = new LogRecord(level, message);
		record.setSourceClassName(caller.getSimpleName());
		handler.setUseRecordCaller(true);
		log(record);
		handler.setUseRecordCaller(false);
	}

}
