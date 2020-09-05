package net.anweisen.commandmanager.defaults;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Developed in the CommandManager project
 * on 09-05-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultLogHandler extends Handler {

	public static String getRecordAsString(LogRecord record, Class<?> caller) {
		return "[" + getCurrentTimeAsString() + " " + Thread.currentThread().getName() + "/" + getLevelAsString(record.getLevel()) + "]: " + (caller != null ? caller.getSimpleName() + " - " : "") + record.getMessage();
	}

	public static String getLevelAsString(Level level) {
		if (level == Level.SEVERE) {
			return "ERROR";
		} else {
			return level.getName();
		}
	}

	public static String getCurrentTimeAsString() {
		OffsetDateTime time = OffsetDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		return time.format(formatter);
	}

	private final PrintStream stream;
	private final Class<?> caller;

	public DefaultLogHandler(@Nonnull PrintStream stream) {
		this.stream = stream;
		this.caller = null;
	}

	public DefaultLogHandler(@Nonnull PrintStream stream, @Nonnull Class<?> caller) {
		this.stream = stream;
		this.caller = caller;
	}

	@Override
	public void publish(LogRecord record) {
		stream.println(getRecordAsString(record, caller));
	}

	@Override
	public void flush() { }

	@Override
	public void close() { }
}
