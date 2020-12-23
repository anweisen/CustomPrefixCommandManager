package net.codingarea.engine.discord.commandmanager.sub;

import net.codingarea.engine.discord.commandmanager.Command;
import net.codingarea.engine.discord.commandmanager.CommandType;
import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.exceptions.SimilarArgumentParserRegisteredException;
import net.codingarea.engine.exceptions.SimilarCommandRegisteredException;
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
		register();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, @Nonnull String... alias) {
		super(name, processInNewThread, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull CommandType commandType, @Nonnull String... alias) {
		super(name, commandType, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull Permission permission, @Nonnull String... alias) {
		super(name, permission, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread, @Nonnull String... alias) {
		super(name, commandType, processInNewThread, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull String... alias) {
		super(name, processInNewThread, reactToMentionPrefix, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread,
	                         boolean reactToMentionPrefix, @Nonnull String... alias) {
		super(name, commandType, processInNewThread, reactToMentionPrefix, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull Permission permission,
	                         @Nonnull String... alias) {
		super(name, processInNewThread, reactToMentionPrefix, permission, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, Permission permission, boolean reactToWebhooks,
	                         boolean reactToBots, boolean reactToMentionPrefix, boolean teamCommand, @Nonnull String... alias) {
		super(name, processInNewThread, permission, reactToWebhooks, reactToBots, reactToMentionPrefix, teamCommand, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, @Nonnull Permission permission, boolean teamCommand, @Nonnull String... alias) {
		super(name, permission, teamCommand, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, @Nonnull Permission permission,
	                         boolean teamCommand, @Nonnull String... alias) {
		super(name, processInNewThread, permission, teamCommand, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, @Nonnull Permission permission, boolean reactToWebhooks,
	                         boolean reactToBots, boolean reactToMentionPrefix, boolean teamCommand, boolean executeOnUpdate, @Nonnull String... alias) {
		super(name, processInNewThread, permission, reactToWebhooks, reactToBots, reactToMentionPrefix, teamCommand, executeOnUpdate, alias);
		register();
	}

	public SubCommandHandler(@Nonnull String name, boolean processInNewThread, @Nonnull CommandType type, boolean teamCommand,
	                         boolean executeOnUpdate, @Nonnull String... alias) {
		super(name, processInNewThread, type, teamCommand, executeOnUpdate, alias);
		register();
	}

	public SubCommandHandler() {
		super();
		register();
	}

	private final List<SubCommandImpl> commands = new ArrayList<>();
	private final List<ArgumentParserImpl> parsers = new ArrayList<>();

	private void register() {
		Utils.getMethodsAnnotatedWith(this.getClass(), SubCommand.class).forEach(this::registerSubCommand);
		Utils.getMethodsAnnotatedWith(this.getClass(), ArgumentParser.class).forEach(this::registerArgumentParser);
	}

	private void registerArgumentParser(@Nonnull Method method) {

		ArgumentParserImpl parser = new ArgumentParserImpl(method, this);

		// Make sure no parser with the same type is registered
		for (ArgumentParserImpl registered : parsers) {
			if (registered.getArgument() == parser.getArgument())
				throw new SimilarArgumentParserRegisteredException(parser.getArgument(), method);
		}

		parsers.add(parser);

	}

	private void registerSubCommand(@Nonnull Method method) {

		SubCommandImpl command = new SubCommandImpl(method, this);

		// Make sure no command with the same args length has the same name
		for (SubCommandImpl registered : commands) {
			if (registered.getArgs().length != command.getArgs().length) continue;
			for (String name : registered.getNames()) {
				for (String name2 : command.getNames()) {
					if (name.equalsIgnoreCase(name2))
						throw new SimilarCommandRegisteredException(name, command.getArgs().length);
				}
			}
		}

		commands.add(command);
	}

	protected final SubCommandImpl findCommand(@Nonnull String name, int args, boolean ignoreArgs) {

		for (SubCommandImpl command : commands) {
			if (!ignoreArgs && command.getArgs().length != args) continue;
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

		event.sendTyping();

		String commandName = event.getArg(0);
		SubCommandImpl name = findCommand(commandName, 0, true);

		if (name == null) {
			onSubCommandNotFound(event, commandName);
			return;
		}

		SubCommandImpl command = findCommand(commandName, event.getArgsLength() - 1, false);
		if (command == null) {
			onInvalidSubCommandArguments(event, name);
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
		event.replySyntax("<subcommand>");
	}

	protected void onSubCommandNotFound(@Nonnull CommandEvent event, @Nonnull String subCommand) throws Exception {
		event.replyMessage("sub-command-not-found", "The SubCommand `%subcommand%` is invalid.",
						   new Replacement("%subcommand%", removeMarkdown(subCommand, true)));
	}

	protected void onInvalidSubCommandArguments(@Nonnull CommandEvent event, @Nonnull SubCommandImpl command) throws Exception {
		event.reply(syntax(event, command.getName() + " " + command.getSyntax()));
	}

	protected void onInvalidSubCommandArgument(@Nonnull CommandEvent event, @Nonnull SubCommandImpl command,
	                                           @Nonnull Class<?> expected, @Nonnull String given, int argumentIndex) throws Exception {
		event.replyMessage("invalid-sub-command-argument", "Invalid argument at **%index%**: Expected `%expected%`, got `%given%`",
						   new Replacement("%index%", argumentIndex),
						   new Replacement("%given%", removeMarkdown(given, true)),
						   new Replacement("%expected%", expected.getSimpleName()));
	}

	@CheckReturnValue
	protected Object parseArgument(@Nonnull Class<?> argument, @Nonnull String input, @Nonnull CommandEvent event) throws Exception {
		for (ArgumentParserImpl parser : parsers) {
			if (parser.getArgument() == argument)
				return parser.parse(event, input);
		} if (argument == Object.class || argument == String.class || argument == CharSequence.class) {
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
		} else if (argument == Double.class || argument == double.class ||  argument == Number.class) {
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
		} else if (argument == GuildChannel.class) {
			return findGuildChannel(event, input);
		} else if (argument == Category.class) {
			return findCategory(event, input);
		} else if (argument == Role.class) {
			return findRole(event, input);
		} else if (argument == Message.class) {
			return findMessage(event.getChannel(), input);
		} else {
			return null;
		}
	}

	public final SubCommandImpl[] getSubCommands() {
		return commands.toArray(new SubCommandImpl[0]);
	}

}
