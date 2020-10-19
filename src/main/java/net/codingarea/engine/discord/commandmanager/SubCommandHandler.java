package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.exceptions.SubCommandNameAlreadyTakenException;
import net.codingarea.engine.utils.NumberConversions;
import net.codingarea.engine.utils.Replacement;
import net.codingarea.engine.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public abstract class SubCommandHandler extends Command {

	public SubCommandHandler(@Nonnull String name, @Nonnull String... alias) {
		super(name, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, @Nonnull String... alias) {
		super(name, processInNewThread, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull CommandType commandType, @Nonnull String... alias) {
		super(name, commandType, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull Permission permission, @Nonnull String... alias) {
		super(name, permission, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread, @Nonnull String... alias) {
		super(name, commandType, processInNewThread, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull String... alias) {
		super(name, processInNewThread, reactToMentionPrefix, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread,
	                         boolean reactToMentionPrefix, @Nonnull String... alias) {
		super(name, commandType, processInNewThread, reactToMentionPrefix, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull Permission permission,
	                         @Nonnull String... alias) {
		super(name, processInNewThread, reactToMentionPrefix, permission, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, Permission permission, boolean reactToWebhooks,
	                         boolean reactToBots, boolean reactToMentionPrefix, boolean teamCommand, @Nonnull String... alias) {
		super(name, processInNewThread, permission, reactToWebhooks, reactToBots, reactToMentionPrefix, teamCommand, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull Permission permission, boolean teamCommand, @Nonnull String... alias) {
		super(name, permission, teamCommand, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, @Nonnull Permission permission,
	                         boolean teamCommand, @Nonnull String... alias) {
		super(name, processInNewThread, permission, teamCommand, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, @Nonnull Permission permission, boolean reactToWebhooks,
	                         boolean reactToBots, boolean reactToMentionPrefix, boolean teamCommand, boolean executeOnUpdate, @Nonnull String... alias) {
		super(name, processInNewThread, permission, reactToWebhooks, reactToBots, reactToMentionPrefix, teamCommand, executeOnUpdate, alias);
		registerSubCommands();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, @Nonnull CommandType type, boolean teamCommand,
	                         boolean executeOnUpdate, @Nonnull String... alias) {
		super(name, processInNewThread, type, teamCommand, executeOnUpdate, alias);
		registerSubCommands();
	}

	public SubCommandHandler() {
		super();
		registerSubCommands();
	}

	private final List<SubCommandInstance> commands = new ArrayList<>();

	private void registerSubCommands() {
		Utils.getMethodsAnnotatedWith(this.getClass(), SubCommand.class).forEach(this::registerSubCommand);
	}

	private void registerSubCommand(@Nonnull Method method) {
		SubCommandInstance command = new SubCommandInstance(method, this);

		// Make sure there is no subcommand with a name this command has too
		for (SubCommandInstance registered : commands) {
			for (String name : registered.getNames()) {
				for (String name2 : command.getNames()) {
					if (name.equalsIgnoreCase(name2))
						throw new SubCommandNameAlreadyTakenException(name);
				}
			}
		}

		commands.add(command);
	}

	protected final SubCommandInstance findCommand(@Nonnull String name) {

		for (SubCommandInstance command : commands) {
			for (String alias : command.getNames()) {
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

			try {
				Object argument = parseArgument(command.getArgs()[i], event.getArg(i + 1), event);
				if (argument == null) throw new NullPointerException();
				args[i] = argument;
			} catch (Exception ignored) {
				onInvalidSubCommandArgument(event, command, command.getArgs()[i], event.getArg(i + 1), i + 1);
				return;
			}

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
									"Invalid argument at **%index%**: Expected `%expected%`, got `%given%`",
									new Replacement("%index%", argumentIndex),
									new Replacement("%given%", given),
									new Replacement("%expected%", expected.getSimpleName())));
	}

	@CheckReturnValue
	protected Object parseArgument(@Nonnull Class<?> argument, @Nonnull String input, @Nonnull CommandEvent event) throws Exception {
		if (argument == Object.class || argument == String.class || argument == CharSequence.class) {
			return input;
		} else if (argument == char[].class) {
			return input.toCharArray();
		} else if (argument == StringBuilder.class) {
			return new StringBuilder(input);
		} else if (argument == Integer.class || argument == int.class) {
			return Integer.parseInt(input);
		} else if (argument == Long.class || argument == long.class) {
			return Long.parseLong(input);
		} else if (argument == Byte.class || argument == byte.class) {
			return Byte.parseByte(input);
		} else if (argument == Short.class || argument == short.class) {
			return Short.parseShort(input);
		} else if (argument == Double.class || argument == double.class) {
			return Double.parseDouble(input);
		} else if (argument == Float.class || argument == float.class) {
			return Float.parseFloat(input);
		} else if (argument == OffsetDateTime.class) {
			return OffsetDateTime.parse(input);
		} else if (argument == Member.class) {
			return findMember(event, input);
		} else if (argument == User.class) {
			Member member = findMember(event, input);
			return member == null ? null : member.getUser();
		} else if (argument == TextChannel.class) {
			return findTextChannel(event, input);
		} else if (argument == VoiceChannel.class) {
			return findVoiceChannel(event, input);
		} else if (argument == Category.class) {
			return findCategory(event, input);
		} else if (argument == Role.class) {
			return findRole(event, input);
		} else {
			return null;
		}
	}

	public final SubCommandInstance[] getSubCommands() {
		return commands.toArray(new SubCommandInstance[0]);
	}

}
