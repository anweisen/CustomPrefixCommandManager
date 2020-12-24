package net.codingarea.engine.utils.log;

import net.codingarea.engine.utils.Bindable;
import net.codingarea.engine.utils.StringBuilderPrintWriter;
import net.codingarea.engine.utils.Utils;
import sun.reflect.Reflection;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public class DefaultLogger extends Logger implements Bindable {

	public static final DefaultLogger DEFAULT = new DefaultLogger("default");
	public static final DefaultLogger EMPTY = new DefaultLogger("empty");

	static {
		DEFAULT.setLevel(Level.ALL);
		EMPTY.setLevel(LogLevel.NONE);
		EMPTY.setFilter(record -> false);
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
		LogRecord record = new LogRecord(LogLevel.ERROR, Utils.getStackTrace(ex));
		record.setSourceClassName(caller.getSimpleName());
		log(record);
	}

	public void error(@Nonnull Throwable ex) {
		PrintWriter writer = new StringBuilderPrintWriter();
		ex.printStackTrace(writer);
	}

	public void log(Level level, Class<?> caller, String message) {
		LogRecord record = new LogRecord(level, message);
		record.setSourceClassName(caller != null ? caller.getSimpleName() : null);
		handler.setUseRecordCaller(true);
		log(record);
		handler.setUseRecordCaller(false);
	}

	public DefaultLogger level(@Nonnull Level level) {
		setLevel(level);
		return this;
	}

}
