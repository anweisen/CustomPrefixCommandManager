package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.exceptions.IllegalSubCommandException;
import net.codingarea.engine.exceptions.IllegalSubCommandNameException;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public final class SubCommandInstance {

	private final String name;
	private final String[] alias;
	private final Class<?>[] args;
	private final Method method;
	private final SubbedCommand root;

	public SubCommandInstance(@Nonnull Method method, @Nonnull SubbedCommand root) {

		this.root = root;

		if (!method.isAnnotationPresent(SubCommand.class))
			throw new IllegalSubCommandException(method);

		if (method.getReturnType() != void.class)
			throw new IllegalSubCommandException(method);

		Class<?>[] args = method.getParameterTypes();

		if (args.length < 1)
			throw new IllegalSubCommandException(method);

		if (args[0] != CommandEvent.class)
			throw new IllegalSubCommandException(method);

		this.args = new Class[args.length - 1];
		System.arraycopy(args, 1, this.args, 0, args.length - 1);

		SubCommand command = method.getAnnotation(SubCommand.class);

		this.name = command.name();
		this.alias = command.alias();
		this.method = method;

		if (name.contains(" "))
			throw new IllegalSubCommandNameException(name);

		for (String alias : this.alias) {
			if (alias == null || alias.contains(" "))
				throw new IllegalSubCommandNameException(alias);
		}

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

	public String getName() {
		return name;
	}

	public String[] getAlias() {
		return alias;
	}

	public Class<?>[] getArgs() {
		return args;
	}

	public SubbedCommand getRoot() {
		return root;
	}

}
