package net.codingarea.botmanager.commandmanager;

import net.codingarea.botmanager.commandmanager.commands.ICommand;
import net.codingarea.botmanager.defaults.DefaultLogger;
import net.codingarea.botmanager.exceptions.CommandExecutionException;
import net.codingarea.botmanager.utils.Bindable;
import net.codingarea.botmanager.utils.CoolDownManager;
import net.codingarea.botmanager.utils.TripleConsumer;
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

	protected MessageReactionBehavior reactToWebhooks = MessageReactionBehavior.REACT_IF_COMMAND_WANTS;
	protected MessageReactionBehavior reactToBots = MessageReactionBehavior.REACT_IF_COMMAND_WANTS;
	protected final ArrayList<ICommand> commands = new ArrayList<>();
	protected CoolDownManager<Member> cooldownManager;

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

	public void handleCommand(@Nonnull String prefix, @Nonnull MessageReceivedEvent event) {
		handleCommand(prefix, event, null);
	}

	/**
	 * @param prefix the prefix which should be in front of the command
	 * @param event the {@link MessageReceivedEvent} the command was received
	 * @see CommandResult
	 * @param result
	 * - {@link CommandResult#INVALID_CHANNEL_PRIVATE_COMMAND} if the command was a private command and was performed in a guild chat <br>
	 * - {@link CommandResult#INVALID_CHANNEL_GUILD_COMMAND} if the command was a guild command and was performed in a private chat <br>
	 * - {@link CommandResult#WEBHOOK_MESSAGE_NO_REACT} if the message came from a webhook, and the command should not react <br>
	 * - {@link CommandResult#BOT_MESSAGE_NO_REACT} if the message came from a bot, and the command should not react <br>
	 * - {@link CommandResult#PREFIX_NOT_USED} if the given prefix and mention prefix was not used <br>
	 * - {@link CommandResult#MENTION_PREFIX_NO_REACT} the mention prefix was used, but the command should not react <br>
	 * - {@link CommandResult#COMMAND_NOT_FOUND} if there was not command with the given name <br>
	 * -
	 * - {@link CommandResult#SUCCESS} if the command was executed <br>
	 */
	public void handleCommand(@Nonnull String prefix, @Nonnull MessageReceivedEvent event, @Nullable TripleConsumer<MessageReceivedEvent, CommandResult, Object> result) {

		if (result == null) result = (e, r, o) -> {};

		String raw = event.getMessage().getContentRaw().toLowerCase().trim();
		boolean byMention = false;
		String mention = mention(event);

		if (!raw.startsWith(prefix)) {
			if (raw.startsWith(mention)) {
				prefix = mention;
				byMention = true;
			} else {
				result.accept(event, CommandResult.PREFIX_NOT_USED, null);
				return;
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

		if (command == null) {
			result.accept(event, CommandResult.COMMAND_NOT_FOUND, commandName.trim());
			return;
		}

		if (cooldownManager != null && cooldownManager.isOnCoolDown(event.getMember())) {
			result.accept(event, CommandResult.MEMBER_ON_COOLDOWN, cooldownManager.getCoolDownLeft(event.getMember()));
			return;
		} else if (command.getType() == CommandType.GUILD && !event.isFromGuild()) {
			result.accept(event, CommandResult.INVALID_CHANNEL_GUILD_COMMAND, commandName.trim());
			return;
		} else if (command.getType() == CommandType.PRIVATE && event.isFromGuild()) {
			result.accept(event, CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND, commandName.trim());
			return;
		} else if (!command.shouldReactToMentionPrefix() && byMention) {
			result.accept(event, CommandResult.MENTION_PREFIX_NO_REACT, null);
			return;
		} else if (command.getPermissionNeeded() != null && !event.getMember().hasPermission(command.getPermissionNeeded())) {
			result.accept(event, CommandResult.NO_PERMISSIONS, command.getPermissionNeeded());
			return;
		} else if (reactToWebhooks != MessageReactionBehavior.REACT_ALWAYS && event.isWebhookMessage() && (!command.shouldReactToWebhooks() || reactToWebhooks == MessageReactionBehavior.REACT_NEVER)) {
			result.accept(event, CommandResult.WEBHOOK_MESSAGE_NO_REACT, null);
			return;
		} else if (reactToBots != MessageReactionBehavior.REACT_ALWAYS && event.getAuthor().isBot() && (!command.shouldReactToBots() || reactToBots == MessageReactionBehavior.REACT_NEVER)) {
			result.accept(event, CommandResult.BOT_MESSAGE_NO_REACT, null);
			return;
		}

		if (cooldownManager != null) cooldownManager.addToCoolDown(event.getMember());

		process(command, new CommandEvent(prefix, getCommandName(command, commandName), event), result);

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

	private static void process(@Nonnull ICommand command, @Nonnull CommandEvent event, @Nonnull TripleConsumer<MessageReceivedEvent, CommandResult, Object> result) {
		if (!command.shouldProcessInNewThread()) {
			execute(command, event, result);
		} else {
			Thread thread = new Thread(THREAD_GROUP, () -> execute(command, event, result), "CommandProcess-" + (THREAD_GROUP.activeCount()+1));
			thread.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
			thread.setDaemon(true);
			thread.start();
		}
	}

	private static void execute(@Nonnull ICommand command, @Nonnull CommandEvent event, @Nonnull TripleConsumer<MessageReceivedEvent, CommandResult, Object> result) throws CommandExecutionException {
		try {
			command.onCommand(event);
			result.accept(event.getReceivedEvent(), CommandResult.SUCCESS, null);
		} catch (Throwable ex) {
			result.accept(event.getReceivedEvent(), CommandResult.EXCEPTION, ex);
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
