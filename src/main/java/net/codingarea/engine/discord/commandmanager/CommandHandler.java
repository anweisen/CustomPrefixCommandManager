package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.discord.commandmanager.impl.CommandEventImpl;
import net.codingarea.engine.discord.defaults.DefaultCommandAccessChecker;
import net.codingarea.engine.discord.defaults.DefaultResultHandler;
import net.codingarea.engine.exceptions.CommandExecutionException;
import net.codingarea.engine.utils.Bindable;
import net.codingarea.engine.utils.CoolDownManager;
import net.codingarea.engine.utils.LogHelper;
import net.codingarea.engine.utils.LogLevel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public class CommandHandler implements Bindable {

	protected final ArrayList<ICommand> commands = new ArrayList<>();
	protected ReactionBehavior reactToWebhooks = ReactionBehavior.COMMAND_DECISION;
	protected ReactionBehavior reactToBots = ReactionBehavior.COMMAND_DECISION;
	protected CoolDownManager cooldownManager;
	protected CommandAccessChecker accessChecker = new DefaultCommandAccessChecker();
	protected ResultHandler resultHandler = new DefaultResultHandler();

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

	@Nullable
	@CheckReturnValue
	public <T extends ICommand> T getCommand(final @Nonnull Class<T> clazz) {
		for (ICommand command : commands) {
			if (command.getClass() == clazz) {
				return (T) command;
			}
		}
		return null;
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
	public List<ICommand> getCommands() {
		return new ArrayList<>(this.commands);
	}

	/**
	 * The {@link ResultHandler} will get one of the following {@link CommandResult CommandResults}
	 * <ul>
	 *     <li>{@link CommandResult#INVALID_CHANNEL_PRIVATE_COMMAND} if the command was a private command and was performed in a guild chat</li>
	 *     <li>{@link CommandResult#INVALID_CHANNEL_GUILD_COMMAND} if the command was a guild command and was performed in a private chat</li>
	 *     <li>{@link CommandResult#WEBHOOK_MESSAGE_NO_REACT} if the message came from a webhook, and the command should not react</li>
	 *     <li>{@link CommandResult#BOT_MESSAGE_NO_REACT} if the message came from a bot, and the command should not react</li>
	 *     <li>{@link CommandResult#PREFIX_NOT_USED} if the given prefix and mention prefix was not used</li>
	 *     <li>{@link CommandResult#MENTION_PREFIX_NO_REACT} the mention prefix was used, but the command should not react</li>
	 *     <li>{@link CommandResult#COMMAND_NOT_FOUND} if there was not command with the given name</li>
	 *     <li>{@link CommandResult#MEMBER_ON_COOLDOWN} if the member is on cooldown, see {@link #setCoolDownManager(CoolDownManager)}</li>
	 *     <li>{@link CommandResult#SELF_MESSAGE_NO_REACT} if the bot it self triggered the event</li>
	 *     <li>{@link CommandResult#INVALID_MESSAGE_TYPE} if the message was not a normal message</li>
	 *     <li>{@link CommandResult#SUCCESS} if the command was executed</li>
	 * </ul>
	 *
	 * @param prefix the prefix which should be in front of the command
	 * @param event the {@link MessageReceivedEvent} the command was received
	 *
	 * @see CommandResult
	 * @see #handleCommand(String, GenericMessageEvent, Member, Message)
	 */
	public void handleCommand(@Nonnull String prefix, @Nonnull MessageReceivedEvent event) {
		handleCommand(prefix, event, event.getMember(), event.getMessage());
	}

	/**
	 * The {@link ResultHandler} will get one of the following {@link CommandResult CommandResults}
	 * <ul>
	 *     <li>{@link CommandResult#INVALID_CHANNEL_PRIVATE_COMMAND} if the command was a private command and was performed in a guild chat</li>
	 *     <li>{@link CommandResult#INVALID_CHANNEL_GUILD_COMMAND} if the command was a guild command and was performed in a private chat</li>
	 *     <li>{@link CommandResult#WEBHOOK_MESSAGE_NO_REACT} if the message came from a webhook, and the command should not react</li>
	 *     <li>{@link CommandResult#BOT_MESSAGE_NO_REACT} if the message came from a bot, and the command should not react</li>
	 *     <li>{@link CommandResult#PREFIX_NOT_USED} if the given prefix and mention prefix was not used</li>
	 *     <li>{@link CommandResult#MENTION_PREFIX_NO_REACT} the mention prefix was used, but the command should not react</li>
	 *     <li>{@link CommandResult#COMMAND_NOT_FOUND} if there was not command with the given name</li>
	 *     <li>{@link CommandResult#MEMBER_ON_COOLDOWN} if the member is on cooldown, see {@link #setCoolDownManager(CoolDownManager)}</li>
	 *     <li>{@link CommandResult#SELF_MESSAGE_NO_REACT} if the bot it self triggered the event</li>
	 *     <li>{@link CommandResult#INVALID_MESSAGE_TYPE} if the message was not a normal message</li>
	 *     <li>{@link CommandResult#SUCCESS} if the command was executed</li>
	 * </ul>
	 *
	 * @param prefix the prefix which should be in front of the command
	 * @param event the {@link MessageReceivedEvent} the command was received
	 *
	 * @see CommandResult
	 * @see #handleCommand(String, GenericMessageEvent, Member, Message)
	 */
	public void handleCommand(@Nonnull String prefix, @Nonnull MessageUpdateEvent event) {
		handleCommand(prefix, event, event.getMember(), event.getMessage());
	}

	/**
	 * @see #handleCommand(String, MessageUpdateEvent)
	 * @see #handleCommand(String, MessageReceivedEvent)
	 */
	protected void handleCommand(@Nonnull String prefix, @Nonnull GenericMessageEvent event, Member member, @Nonnull Message message) {

		if (message.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
			resultHandler.handle(event, CommandResult.SELF_MESSAGE_NO_REACT, null);
			return;
		}

		ResultHandler resultHandler = this.resultHandler;
		if (resultHandler == null) resultHandler = (a, b, c, d, e, f) -> {};

		String raw = message.getContentRaw().toLowerCase().trim();
		boolean byMention = false;
		String mention = mention(event);

		if (!raw.startsWith(prefix)) {
			if (raw.startsWith(mention)) {
				prefix = mention;
				byMention = true;
			} else {
				resultHandler.handle(event, CommandResult.PREFIX_NOT_USED, null);
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
			resultHandler.handle(event, CommandResult.COMMAND_NOT_FOUND, commandName.trim());
			return;
		}

		if (cooldownManager != null && cooldownManager.isOnCoolDown(member)) {
			resultHandler.handle(event, CommandResult.MEMBER_ON_COOLDOWN, cooldownManager.getCoolDownLeft(member));
			return;
		} else if (command.getType() == CommandType.GUILD && !event.isFromGuild()) {
			resultHandler.handle(event, CommandResult.INVALID_CHANNEL_GUILD_COMMAND, commandName.trim());
			return;
		} else if (command.getType() == CommandType.PRIVATE && event.isFromGuild()) {
			resultHandler.handle(event, CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND, commandName.trim());
			return;
		} else if (!command.shouldReactToMentionPrefix() && byMention) {
			resultHandler.handle(event, CommandResult.MENTION_PREFIX_NO_REACT, null);
			return;
		} else if (!command.executeOnUpdate() && event instanceof MessageUpdateEvent) {
			resultHandler.handle(event, CommandResult.MESSAGE_EDIT_NO_REACT, null);
			return;
		} else if (!hasAccess(member, command)) {
			resultHandler.handle(event, CommandResult.NO_PERMISSIONS, command.getPermissionNeeded());
			return;
		} else if (reactToWebhooks != ReactionBehavior.ALWAYS && message.isWebhookMessage() && (!command.shouldReactToWebhooks() || reactToWebhooks == ReactionBehavior.NEVER)) {
			resultHandler.handle(event, CommandResult.WEBHOOK_MESSAGE_NO_REACT, null);
			return;
		} else if (reactToBots != ReactionBehavior.ALWAYS && message.getAuthor().isBot() && (!command.shouldReactToBots() || reactToBots == ReactionBehavior.NEVER)) {
			resultHandler.handle(event, CommandResult.BOT_MESSAGE_NO_REACT, null);
			return;
		}

		if (cooldownManager != null)
			cooldownManager.addToCoolDown(member);

		process(command, CommandEventImpl.create(event, prefix, getCommandName(command, commandName), command, this), resultHandler);

	}

	public boolean hasAccess(final @Nullable Member member, final @Nonnull ICommand command) {
		return member != null && accessChecker != null && accessChecker.isAllowed(member, command);
	}

	protected String mention(Event event) {
		return "<@!" + event.getJDA().getSelfUser().getId() + ">";
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public CommandHandler setWebhookMessageBehavior(ReactionBehavior behavior) {
		this.reactToWebhooks = behavior;
		return this;
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public CommandHandler setBotMessageBehavior(ReactionBehavior behavior) {
		this.reactToBots = behavior;
		return this;
	}

	@CheckReturnValue
	public ReactionBehavior getBotMessageBehavior() {
		return reactToBots;
	}

	@CheckReturnValue
	public ReactionBehavior getWebhookMessageBehavior() {
		return reactToWebhooks;
	}

	@CheckReturnValue
	public CoolDownManager getCoolDownManager() {
		return cooldownManager;
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public CommandHandler setCoolDownManager(CoolDownManager cooldownManager) {
		this.cooldownManager = cooldownManager;
		return this;
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public CommandHandler setPermissionChecker(CommandAccessChecker accessChecker) {
		this.accessChecker = accessChecker;
		return this;
	}

	public CommandAccessChecker getPermissionChecker() {
		return accessChecker;
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public CommandHandler setResultHandler(ResultHandler resultHandler) {
		this.resultHandler = resultHandler;
		return this;
	}

	@CheckReturnValue
	public ResultHandler getResultHandler() {
		return resultHandler;
	}

	public static final ThreadGroup THREAD_GROUP = new ThreadGroup("CommandProcessGroup");
	private static final UncaughtExceptionHandler EXCEPTION_HANDLER = new ExceptionHandler();

	private static void process(@Nonnull ICommand command, @Nonnull CommandEvent event, @Nonnull ResultHandler result) {
		if (!command.shouldProcessInNewThread()) {
			execute(command, event, result);
		} else {
			Thread thread = new Thread(THREAD_GROUP, () -> execute(command, event, result), "CommandProcess-" + (THREAD_GROUP.activeCount()+1));
			thread.setUncaughtExceptionHandler(EXCEPTION_HANDLER);
			thread.setDaemon(true);
			thread.start();
		}
	}

	private static void execute(@Nonnull ICommand command, @Nonnull CommandEvent event, ResultHandler result) throws CommandExecutionException {
		try {
			command.onCommand(event);
			result.handle(event.getEvent(), CommandResult.SUCCESS, null);
		} catch (Throwable ex) {
			result.handle(event.getEvent(), CommandResult.EXCEPTION, ex);
			throw new CommandExecutionException(ex);
		}
	}

	private static class ExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable exception) {
			LogHelper.log(LogLevel.ERROR, CommandHandler.class, exception);
		}

	}

}
