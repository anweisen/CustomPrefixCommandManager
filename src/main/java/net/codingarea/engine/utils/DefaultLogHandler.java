package net.codingarea.engine.utils;

import net.codingarea.engine.utils.Utils;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public class DefaultLogHandler extends Handler {

	@Nonnull
	public static String getRecordAsString(Thread thread, LogRecord record, String caller) {
		return "[" + getCurrentTimeAsString() + " " + (thread != null ? thread : Thread.currentThread()).getName() + "/" + record.getLevel().getName() + "]: " + (caller != null ? caller + " - " : "") + record.getMessage();
	}

	@Contract("null -> null; !null -> !null")
	public static String getCallerName(String caller) {
		if (caller == null) return null;
		try {
			Class<?> clazz = Class.forName(caller);
			return clazz.getSimpleName();
		} catch (ClassNotFoundException ignored) {
			String[] args = caller.split("\\.");
			return args[args.length-1];
		}
	}

	@Nonnull
	public static String getCurrentTimeAsString() {
		OffsetDateTime time = Utils.centralEuropeTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter);
	}

	private final Class<?> caller;
	private PrintStream stream;
	private transient boolean useRecordCaller = false;

	public DefaultLogHandler(@Nonnull PrintStream stream) {
		this(stream, null);
	}

	public DefaultLogHandler(@Nonnull PrintStream stream, Class<?> caller) {
		this.stream = stream;
		this.caller = caller;
	}

	@Override
	public void publish(LogRecord record) {
		stream.println(getRecordAsString(null, record, useRecordCaller || caller == null ? getCallerName(record.getSourceClassName()) : caller.getSimpleName()));
	}

	@Override
	public void flush() { }

	@Override
	public void close() { }

	void setUseRecordCaller(boolean useRecordCaller) {
		this.useRecordCaller = useRecordCaller;
	}

	public void setStream(@Nonnull PrintStream stream) {
		this.stream = stream;
	}

	public Class<?> getCaller() {
		return caller;
	}

	public PrintStream getStream() {
		return stream;
	}

}
