package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.utils.Replacement;
import net.codingarea.engine.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public abstract class SubbedCommand extends Command {

	public SubbedCommand(@Nonnull String name, @Nonnull String... alias) {
		super(name, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, boolean processInNewThread, @Nonnull String... alias) {
		super(name, processInNewThread, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, @Nonnull CommandType commandType, @Nonnull String... alias) {
		super(name, commandType, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, @Nonnull Permission permission, @Nonnull String... alias) {
		super(name, permission, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread, @Nonnull String... alias) {
		super(name, commandType, processInNewThread, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull String... alias) {
		super(name, processInNewThread, reactToMentionPrefix, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread,
	                     boolean reactToMentionPrefix, @Nonnull String... alias) {
		super(name, commandType, processInNewThread, reactToMentionPrefix, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull Permission permission,
	                     @Nonnull String... alias) {
		super(name, processInNewThread, reactToMentionPrefix, permission, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, boolean processInNewThread, Permission permission, boolean reactToWebhooks,
	                     boolean reactToBots, boolean reactToMentionPrefix, boolean teamCommand, @Nonnull String... alias) {
		super(name, processInNewThread, permission, reactToWebhooks, reactToBots, reactToMentionPrefix, teamCommand, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, @Nonnull Permission permission, boolean teamCommand, @Nonnull String... alias) {
		super(name, permission, teamCommand, alias);
		registerSubCommands();
	}

	public SubbedCommand(@Nonnull String name, boolean processInNewThread, @Nonnull Permission permission,
	                     boolean teamCommand, @Nonnull String... alias) {
		super(name, processInNewThread, permission, teamCommand, alias);
		registerSubCommands();
	}

	private final List<SubCommandInstance> commands = new ArrayList<>();

	private void registerSubCommands() {
		Utils.getMethodsAnnotatedWith(this.getClass(), SubCommand.class).forEach(this::registerSubCommand);
	}

	private void registerSubCommand(@Nonnull Method method) {
		SubCommandInstance command = new SubCommandInstance(method, this);
		commands.add(command);
	}

	private SubCommandInstance findCommand(@Nonnull String name) {

		for (SubCommandInstance command : commands) {

			if (command.getName().equalsIgnoreCase(name))
				return command;

			for (String alias : command.getAlias()) {
				if (alias.equalsIgnoreCase(name))
					return command;
			}
		}

		return null;

	}

	@Override
	public final void onCommand(@Nonnull CommandEvent event) throws Exception {

		if (event.getArgsLength() == 0) {
			onNoCommandGiven(event);
			return;
		}

		String commandName = event.getArg(0);
		SubCommandInstance command = findCommand(commandName);

		if (command == null) {
			onSubCommandNotFound(event, commandName);
			return;
		}

		if (event.getArgsLength() <= command.getArgs().length) {
			onInvalidSubCommandArguments(event, command);
			return;
		}

		Object[] args = new Object[command.getArgs().length];
		for (int i = 0; i < command.getArgs().length; i++) {

			Object argument = parseArgument(command.getArgs()[i], event.getArg(i + 1), event);
			if (argument == null) {
				onInvalidSubCommandArgument(event, command, command.getArgs()[i], event.getArg(i + 1), i + 1);
				return;
			}

			args[i] = argument;

		}

		command.invoke(event, args);

	}

	protected void onNoCommandGiven(@Nonnull CommandEvent event) throws Exception {
		sendSyntax(event, "<subcommand>");
	}

	protected void onSubCommandNotFound(@Nonnull CommandEvent event, @Nonnull String subCommand) throws Exception {
		event.queueReply(getMessage(event, "sub-command-not-found", "The SubCommand `%subcommand%` is invalid.",
						 new Replacement("%subcommand%", subCommand)));
	}

	protected void onInvalidSubCommandArguments(@Nonnull CommandEvent event, @Nonnull SubCommandInstance command) throws Exception {
		StringBuilder builder = new StringBuilder();
		for (Class<?> arg : command.getArgs()) {
			builder.append(" <" + arg.getSimpleName().toLowerCase() + ">");
		}
		event.queueReply(syntax(event, command.getName() + builder));
	}

	protected void onInvalidSubCommandArgument(@Nonnull CommandEvent event, @Nonnull SubCommandInstance command,
	                                           @Nonnull Class<?> expected, @Nonnull String given, int argumentIndex) throws Exception {
		event.queueReply(getMessage(event, "invalid-sub-command-argument",
									"Invalid argument **%index%**: Expected `%expected%`, got `%given%`",
									new Replacement("%index%", argumentIndex),
									new Replacement("%given%", given),
									new Replacement("%expected%", expected.getSimpleName())));
	}

	@CheckReturnValue
	protected <T> Object parseArgument(@Nonnull Class<T> clazz, @Nonnull String input, @Nonnull CommandEvent event) {
		if (clazz == Object.class || clazz == String.class || clazz == CharSequence.class) {
			return input;
		} else if (clazz == StringBuilder.class) {
			return new StringBuilder(input);
		} else if (clazz == Integer.class || clazz == int.class) {
			return Integer.parseInt(input);
		} else if (clazz == Long.class || clazz == long.class) {
			return Long.parseLong(input);
		} else if (clazz == Byte.class || clazz == byte.class) {
			return Byte.parseByte(input);
		} else if (clazz == Short.class || clazz == short.class) {
			return Short.valueOf(input);
		} else if (clazz == Double.class || clazz == double.class) {
			return Double.parseDouble(input);
		} else if (clazz == Float.class || clazz == float.class) {
			return Float.parseFloat(input);
		} else if (clazz == Member.class) {
			return findMember(event, input);
		} else if (clazz == User.class) {
			Member member = findMember(event, input);
			return member == null ? null : member.getUser();
		} else if (clazz == TextChannel.class) {
			return findTextChannel(event, input);
		} else if (clazz == VoiceChannel.class) {
			return findVoiceChannel(event, input);
		} else if (clazz == Category.class) {
			return findCategory(event, input);
		} else {
			return null;
		}
	}

}
