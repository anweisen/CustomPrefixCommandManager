package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.exceptions.IllegalSubCommandException;
import net.codingarea.engine.exceptions.IllegalSubCommandNameException;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public final class SubCommandInstance {

	private final String[] names;
	private final Class<?>[] args;
	private final Method method;
	private final SubCommandHandler root;

	public SubCommandInstance(@Nonnull Method method, @Nonnull SubCommandHandler root) {

		this.root = root;

		if (!method.isAnnotationPresent(SubCommand.class))
			throw new IllegalSubCommandException("Method is not annotated as net.codingarea.engine.commandmanager.SubCommand", method);

		if (method.getReturnType() != void.class)
			throw new IllegalSubCommandException("Method does not return void", method);

		Class<?>[] args = method.getParameterTypes();

		if (args.length < 1)
			throw new IllegalSubCommandException("Method has 0 arguments", method);

		if (args[0] != CommandEvent.class)
			throw new IllegalSubCommandException("First argument is not net.codingarea.engine.commandmanager.CommandEvent", method);

		this.args = new Class[args.length - 1];
		System.arraycopy(args, 1, this.args, 0, args.length - 1);

		SubCommand command = method.getAnnotation(SubCommand.class);
		String[] names = command.name();

		if (names.length == 0) {
			names = new String[] {method.getName()};
		} else {
			// Making sure no name contains a space
			for (String alias : names) {
				if (alias == null || alias.contains(" "))
					throw new IllegalSubCommandNameException(alias);
			}
		}

		this.names = names;
		this.method = method;

	}

	public void invoke(@Nonnull CommandEvent event, @Nonnull Object[] args) throws Exception {

		Object[] arguments = new Object[args.length + 1];
		arguments[0] = event;
		System.arraycopy(args, 0, arguments, 1, args.length);

		method.invoke(root, arguments);

	}

	public Method getMethod() {
		return method;
	}

	public String[] getNames() {
		return Arrays.copyOf(names, names.length);
	}

	public String getName() {
		return names[0];
	}

	public Class<?>[] getArgs() {
		return Arrays.copyOf(args, args.length);
	}

	public SubCommandHandler getRoot() {
		return root;
	}

}
