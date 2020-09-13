package net.anweisen.commandmanager;

import net.anweisen.commandmanager.commands.ICommand;
import net.anweisen.commandmanager.defaults.DefaultLogger;
import net.anweisen.commandmanager.exceptions.CommandExecutionException;
import net.anweisen.commandmanager.utils.Bindable;
import net.anweisen.commandmanager.utils.CoolDownManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class CommandHandler implements Bindable {

	public enum MessageReactionBehavior {
		REACT_NEVER,
		REACT_IF_COMMAND_WANTS,
		REACT_ALWAYS;
	}

	private MessageReactionBehavior reactToWebhooks = MessageReactionBehavior.REACT_IF_COMMAND_WANTS;
	private MessageReactionBehavior reactToBots = MessageReactionBehavior.REACT_IF_COMMAND_WANTS;
	private final ArrayList<ICommand> commands = new ArrayList<>();

	private CoolDownManager<Member> cooldownManager;

	@Nonnull
	public CommandHandler registerCommand(ICommand command) {
		commands.add(command);
		return this;
	}

	@Nonnull
	public CommandHandler registerCommands(ICommand... commands) {
		this.commands.addAll(Arrays.asList(commands));
		return this;
	}

	public void unregisterAllCommands() {
		commands.clear();
	}

	/**
	 * @return returns null when no command is registered with this name
	 */
	@Nullable
	@CheckReturnValue
	public ICommand getCommand(String name) {

		name = name.toLowerCase();

		for (ICommand currentCommand : commands) {

			if (correctName(name, currentCommand.getName())) return currentCommand;

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

	@Nonnull
	@CheckReturnValue
	private String getCommandName(@Nonnull ICommand command, @Nonnull String raw) {

		raw = raw.toLowerCase();

		if (raw.startsWith(command.getName().toLowerCase())) {
			return command.getName();
		}

		for (String currentAlias : command.getAlias()) {
			if (currentAlias == null) continue;
			if (raw.startsWith(currentAlias.toLowerCase())) return currentAlias;
		}

		return raw;

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
	 * @param prefix the prefix which should be in front of the command
	 * @param event the {@link MessageReceivedEvent} the command was received
	 * @see CommandResult
	 * @return returns
	 * - INVALID_CHANNEL_PRIVATE_COMMAND if the command was a private command and was performed in a guild chat <br>
	 * - INVALID_CHANNEL_GUILD_COMMAND if the command was a guild command and was performed in a private chat <br>
	 * - WEBHOOK_MESSAGE_NO_REACT if the message came from a webhook, and the command should not react <br>
	 * - BOT_MESSAGE_NO_REACT if the message came from a bot, and the command should not react <br>
	 * - PREFIX_NOT_USED if the given prefix and mention prefix was not used <br>
	 * - MENTION_PREFIX_NO_REACT the mention prefix was used, but the command should not react <br>
	 * - COMMAND_NOT_FOUND if there was not command with the given name <br>
	 * - SUCCESS if the command was executed <br>
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

		// If the mention prefix ends with one (or more) space(s), we'll add the space(s) to the prefix
		if (byMention) {
			try {
				while (raw.substring(prefix.length()).startsWith(" ")) {
					prefix += " ";
				}
			} catch (Exception ignored) { }
		}

		String commandName = raw.substring(prefix.length());
		ICommand command = getCommand(commandName);

		if (command == null) return CommandResult.COMMAND_NOT_FOUND;

		if (cooldownManager != null && cooldownManager.isOnCoolDown(event.getMember())) {
			return CommandResult.MEMBER_ON_COOLDOWN;
		} else if (!command.shouldReactToMentionPrefix() && byMention) {
			return CommandResult.MENTION_PREFIX_NO_REACT;
		} else if (command.getPermissionNeeded() != null && !event.getMember().hasPermission(command.getPermissionNeeded())) {
			return CommandResult.NO_PERMISSIONS;
		} else if (reactToWebhooks != MessageReactionBehavior.REACT_ALWAYS && event.isWebhookMessage() && (!command.shouldReactToWebhooks() || reactToWebhooks == MessageReactionBehavior.REACT_NEVER)) {
			return CommandResult.WEBHOOK_MESSAGE_NO_REACT;
		} else if (reactToBots != MessageReactionBehavior.REACT_ALWAYS && event.getAuthor().isBot() && (!command.shouldReactToBots() || reactToBots == MessageReactionBehavior.REACT_NEVER)) {
			return CommandResult.BOT_MESSAGE_NO_REACT;
		} else if (command.getType() == CommandType.GUILD && !event.isFromGuild()) {
			return CommandResult.INVALID_CHANNEL_GUILD_COMMAND;
		} else if (command.getType() == CommandType.PRIVATE && event.isFromGuild()) {
			return CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND;
		}

		if (cooldownManager != null) cooldownManager.addToCoolDown(event.getMember());

		process(command, new CommandEvent(prefix, getCommandName(command, commandName), event));
		return CommandResult.SUCCESS;

	}

	private String mention(MessageReceivedEvent event) {
		return event.getJDA().getSelfUser().getAsMention();
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

	public CoolDownManager<Member> getCoolDownManager() {
		return cooldownManager;
	}

	public void setCoolDownManager(CoolDownManager<Member> cooldownManager) {
		this.cooldownManager = cooldownManager;
	}

	public static final ThreadGroup THREAD_GROUP = new ThreadGroup("CommandProcessGroup");
	private static final UncaughtExceptionHandler EXCEPTION_HANDLER = new ExceptionHandler();

	private static void process(@Nonnull ICommand command, @Nonnull CommandEvent event) throws CommandExecutionException {
		if (!command.shouldProcessInNewThread()) {
			execute(command, event);
		} else {
			Thread thread = new Thread(THREAD_GROUP, () -> execute(command, event), "CommandProcess-" + (THREAD_GROUP.activeCount()+1));
			thread.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
			thread.setDaemon(true);
			thread.start();
		}
	}

	private static void execute(@Nonnull ICommand command, @Nonnull CommandEvent event) throws CommandExecutionException {
		try {
			command.onCommand(event);
		} catch (Throwable ex) {
			throw new CommandExecutionException(ex);
		}
	}

	private static class ExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable exception) {
			DefaultLogger.logDefault(exception, CommandHandler.class);
		}

	}

}
