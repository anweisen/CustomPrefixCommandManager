package net.anweisen.commandmanager;

import net.anweisen.commandmanager.commands.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

/**
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 * @author anweisen
 */

public class CommandHandler {

	public enum MessageReactionBehavior {
		REACT_NEVER,
		REACT_IF_COMMAND_WANTS,
		REACT_ALWAYS;
	}

	private MessageReactionBehavior reactToWebhooks = MessageReactionBehavior.REACT_IF_COMMAND_WANTS;
	private MessageReactionBehavior reactToBots = MessageReactionBehavior.REACT_IF_COMMAND_WANTS;
	private final ArrayList<ICommand> commands = new ArrayList<>();

	public void registerCommand(ICommand command) {
		commands.add(command);
	}

	public void registerCommands(ICommand... commands) {
		for (ICommand currentCommand : commands) {
			registerCommand(currentCommand);
		}
	}

	public void unregisterAllCommands() {
		commands.clear();
	}

	/**
	 * @return returns null when no command is registered with this name
	 */
	public ICommand getCommand(String name) {

		name = name.toLowerCase();

		for (ICommand currentCommand : commands) {

			if (currentCommand.getName() == null) continue;
			if (correctName(name, currentCommand.getName())) return currentCommand;

			if (currentCommand.getAlias() == null) continue;
			for (String currentAlias : currentCommand.getAlias()) {
				if (correctName(name, currentAlias)) return currentCommand;
			}
		}

		return null;

	}

	private boolean correctName(String query, String current) {
		if (!query.startsWith(current.toLowerCase())) {
			return false;
		} else {
			int endIndex = current.length();
			String after = query.substring(endIndex);
			return after.isEmpty() || after.startsWith(" ");
		}
	}

	private String getCommandName(ICommand command, String raw) {

		raw = raw.toLowerCase();

		if (raw.startsWith(command.getName().toLowerCase())) {
			return command.getName();
		}

		if (command.getAlias() != null) {
			for (String currentAlias : command.getAlias()) {
				if (currentAlias == null) continue;
				if (raw.startsWith(currentAlias.toLowerCase())) return currentAlias;
			}
		}

		return null;

	}

	/**
	 * Returns a list with all commands registered.
	 * It will return a empty list when there are no commands registered
	 * @return a copy of the list with all commands
	 */
	public ArrayList<ICommand> getCommands() {
		return new ArrayList<>(this.commands);
	}

	/**
	 * @see net.anweisen.commandmanager.CommandResult
	 * @return returns
	 * - INVALID_CHANNEL_PRIVATE_COMMAND the command was a private command and was performed in a guild chat <br>
	 * - INVALID_CHANNEL_GUILD_COMMAND the command was a guild command and was performed in a private chat <br>
	 * - WEBHOOK_MESSAGE_NO_REACT the message came from a webhook, and the command should not react <br>
	 * - BOT_MESSAGE_NO_REACT the message came from a bot, and the command should not react <br>
	 * - PREFIX_NOT_USED given prefix and mention prefix was not used <br>
	 * - MENTION_PREFIX_NO_REACT the mention prefix was used, but the command should not react <br>
	 * - COMMAND_NOT_FOUND if there was not command with the given name <br>
	 * - SUCCESS if the command was executed <br>
	 *
	 * @param prefix the prefix which should be in front of the command
	 * @param event the command event the command was received
	 */
	public CommandResult handleCommand(String prefix, MessageReceivedEvent event) {

		String raw = event.getMessage().getContentRaw().toLowerCase().trim();
		boolean byMention = false;
		String mention = mention(event);

		if (!raw.startsWith(prefix)) {
			if (raw.startsWith(mention)) {
				prefix = mention;
				byMention = true;
			} else {
				return CommandResult.PREFIX_NOT_USED;
			}
		}

		int commandIndex = prefix.length();
		try {
			while (raw.substring(commandIndex).startsWith(" ")) {
				commandIndex++;
				prefix = prefix + " ";
			}
		} catch (Exception ignored) { }

		String commandName = raw.substring(commandIndex);
		ICommand command = getCommand(commandName);

		if (command == null) return CommandResult.COMMAND_NOT_FOUND;

		if (!command.shouldReactToMentionPrefix() && byMention) {
			return CommandResult.MENTION_PREFIX_NO_REACT;
		} else if (reactToWebhooks != MessageReactionBehavior.REACT_ALWAYS && event.isWebhookMessage() && (!command.shouldReactToWebhooks() || reactToWebhooks == MessageReactionBehavior.REACT_NEVER)) {
			return CommandResult.WEBHOOK_MESSAGE_NO_REACT;
		} else if (reactToBots != MessageReactionBehavior.REACT_ALWAYS && event.getAuthor().isBot() && (!command.shouldReactToBots() || reactToBots == MessageReactionBehavior.REACT_NEVER)) {
			return CommandResult.BOT_MESSAGE_NO_REACT;
		} else if (command.getType() != null && command.getType() == CommandType.GUILD && !event.isFromGuild()) {
			return CommandResult.INVALID_CHANNEL_GUILD_COMMAND;
		} else if (command.getType() != null && command.getType() == CommandType.PRIVATE && event.isFromGuild()) {
			return CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND;
		}

		process(command, new CommandEvent(prefix, getCommandName(command, commandName), event));
		return CommandResult.SUCCESS;

	}

	private String mention(MessageReceivedEvent event) {
		return "<@!" + event.getJDA().getSelfUser().getId() + ">";
	}

	public void setWebhookMessageBehavior(MessageReactionBehavior behavior) {
		this.reactToWebhooks = behavior;
	}

	public void setBotMessageBehavior(MessageReactionBehavior behavior) {
		this.reactToBots = behavior;
	}

	public MessageReactionBehavior getBotMessageBehavior() {
		return reactToBots;
	}

	public MessageReactionBehavior getWebhookMessageBehavior() {
		return reactToWebhooks;
	}

	public static final ThreadGroup THREAD_GROUP = new ThreadGroup("CommandProcessGroup");
	private static final UncaughtExceptionHandler EXCEPTION_HANDLER = new ExceptionHandler();

	private static void process(ICommand command, CommandEvent event) {
		if (!command.shouldProcessInNewThread()) {
			command.onCommand(event);
		} else {
			Thread thread = new Thread(THREAD_GROUP, () -> command.onCommand(event), "CommandProcess-" + (THREAD_GROUP.activeCount()+1));
			thread.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
			thread.start();
		}
	}

	private static class ExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable exception) {
			System.err.println("[" + thread.getName() + "] One of your command generated an exception: " + exceptionMessage(exception));
		}

		private static String exceptionMessage(Throwable exception) {

			StringBuilder builder = new StringBuilder();
			builder.append(exception.getMessage());

			for (StackTraceElement currentTraceElement : exception.getStackTrace()) {
				builder.append("\nat " + currentTraceElement.toString());
			}

			return builder.toString();

		}

	}

}
