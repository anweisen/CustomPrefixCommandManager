package net.codingarea.engine.discord.commandmanager.sub;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.exceptions.IllegalArgumentParserException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public final class ArgumentParserImpl {

	private final Class<?> clazz;
	private final Method method;
	private final SubCommandHandler parent;

	public ArgumentParserImpl(final @Nonnull Method method, final @Nonnull SubCommandHandler parent) {

		if (!method.isAnnotationPresent(ArgumentParser.class))
			throw new IllegalArgumentParserException("Method is not annotated as ArgumentParser", method);
		if (method.getReturnType() == void.class)
			throw new IllegalArgumentParserException("Method cannot return void", method);
		if (method.getParameterCount() != 2)
			throw new IllegalArgumentParserException("Method parameters are not zero", method);
		if (method.getParameterTypes()[0] != CommandEvent.class)
			throw new IllegalArgumentParserException("The first argument is not a CommandEvent", method);
		if (method.getParameterTypes()[1] != String.class)
			throw new IllegalArgumentParserException("The second argument is not a String", method);

		ArgumentParser annotation = method.getAnnotation(ArgumentParser.class);

		this.clazz = annotation.argument();
		this.method = method;
		this.parent = parent;

	}

	public Object parse(final @Nonnull CommandEvent event, final @Nonnull String input) throws InvocationTargetException, IllegalAccessException {
		return method.invoke(parent, event, input);
	}

	@Nonnull
	@CheckReturnValue
	public Class<?> getArgument() {
		return clazz;
	}
}
