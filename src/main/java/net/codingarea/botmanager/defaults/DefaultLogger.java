package net.codingarea.botmanager.defaults;

import jdk.internal.dynalink.beans.CallerSensitiveDetector;
import net.codingarea.botmanager.utils.Bindable;
import net.codingarea.botmanager.utils.LogLevel;
import net.codingarea.botmanager.utils.StringBuilderPrintWriter;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Ref;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultLogger extends Logger implements Bindable {

	public static final DefaultLogger DEFAULT = new DefaultLogger("default");

	public static void logDefault(@Nonnull Level level, String caller, @Nonnull String message) {
		LogRecord record = new LogRecord(level, message);
		if (caller != null) record.setSourceClassName(caller);
		DEFAULT.log(record);
	}

	public static void logDefault(@Nonnull Level level, @Nonnull Class<?> caller, @Nonnull String message) {
		logDefault(level, caller.getSimpleName(), message);
	}

	@CallerSensitive
	public static void logDefault(@Nonnull Level level, @Nonnull String message) {
		new SecurityManager() {
			{
				Class<?> caller = getClassContext()[2];
				logDefault(level, caller, message);
			}
		};
	}

	public static void logDefault(@Nonnull Throwable thrown, Class<?> caller) {
		DEFAULT.error(thrown, caller);
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
		log(LogLevel.ERROR, message);
	}

	public void error(Supplier<String> message) {
		log(LogLevel.ERROR, message);
	}

	public void error(@Nonnull Throwable ex, Class<?> caller) {
		if (caller == null) caller = Reflection.getCallerClass();
		StringBuilderPrintWriter writer = new StringBuilderPrintWriter();
		ex.printStackTrace(writer);
		LogRecord record = new LogRecord(LogLevel.ERROR, writer.getBuilder().toString());
		record.setSourceClassName(caller.getSimpleName());
		log(record);
	}

	public void error(@Nonnull Throwable ex) {
		PrintWriter writer = new StringBuilderPrintWriter();
		ex.printStackTrace(writer);
	}

	public void log(Level level, Class<?> caller, String message) {
		LogRecord record = new LogRecord(level, message);
		record.setSourceClassName(caller.getSimpleName());
		handler.setUseRecordCaller(true);
		log(record);
		handler.setUseRecordCaller(false);
	}
}
