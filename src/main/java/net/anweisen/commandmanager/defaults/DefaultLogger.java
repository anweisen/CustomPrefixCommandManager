package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.utils.Bindable;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * Developed in the CommandManager project
 * on 09-05-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultLogger extends Logger implements Bindable {

	public DefaultLogger() {
		this("default");
	}

	public DefaultLogger(String name) {
		this(name, null, null);
	}

	public DefaultLogger(@Nonnull Class<?> caller) {
		this(caller.getName(), null, caller);
	}

	public DefaultLogger(@Nonnull Class<?> caller, @Nonnull PrintStream stream) {
		this(caller.getName(), stream, caller);
	}

	public DefaultLogger(@Nonnull String name, PrintStream stream, Class<?> caller) {
		super(name, null);
		addHandler(new DefaultLogHandler(stream != null ? stream : System.err, caller));
	}

}
