package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.discord.commandmanager.event.CommandEventImpl;
import net.codingarea.engine.discord.commandmanager.helper.CommandHelper;
import net.codingarea.engine.utils.CoolDownManager;
import net.codingarea.engine.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public class CommandHandler implements ICommandHandler {

	protected CoolDownManager cooldown;
	protected PrefixProvider prefixProvider;
	protected TeamRankChecker teamRankChecker;
	protected ReactionBehavior webhookReactionBehavior = ReactionBehavior.COMMAND_DECISION,
							   botReactionBehavior = ReactionBehavior.COMMAND_DECISION;
	protected final AtomicLong executedCommands = new AtomicLong();
	protected final List<ICommand> registry = new ArrayList<>();
	protected final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

	public CommandHandler(@Nonnull PrefixProvider prefixProvider) {
		this.prefixProvider = prefixProvider;
	}

	public CommandHandler(@Nonnull String prefix) {
		this(PrefixProvider.constant(prefix));
	}

	@Nonnull
	@Override
	public CompletableFuture<CommandResult> handleEvent(@Nonnull MessageUpdateEvent event) {
		CompletableFuture<CommandResult> future = new CompletableFuture<>();
		handleEvent(event, event.getMessage(), future, event.getMember());
		return future;
	}

	@Nonnull
	@Override
	public CompletableFuture<CommandResult> handleEvent(@Nonnull MessageReceivedEvent event) {
		CompletableFuture<CommandResult> future = new CompletableFuture<>();
		handleEvent(event, event.getMessage(), future, event.getMember());
		return future;
	}

	protected Object handleEvent(@Nonnull GenericMessageEvent event, @Nonnull Message message,
	                             @Nonnull CompletableFuture<CommandResult> callback, @Nullable Member member) {

		if (message.getType() != MessageType.DEFAULT && message.getType() != MessageType.INLINE_REPLY) {
			return callback.complete(CommandResult.INVALID_MESSAGE_TYPE);
		} else if (message.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
			return callback.complete(CommandResult.SELF_MESSAGE_NO_REACT);
		}

		String raw = message.getContentRaw().toLowerCase().trim();
		String prefix = member != null ? prefixProvider.getGuildPrefix(member.getGuild()) : prefixProvider.getDefaultPrefix();
		String mention = CommandHelper.mentionJDA(event);
		boolean mentionPrefix = false;

		if (!raw.startsWith(prefix)) {
			if (raw.startsWith(mention)) {
				mentionPrefix = true;
				prefix = mention;
				try {
					while (raw.substring(prefix.length()).startsWith(" "))
						prefix += " ";
				} catch (Exception ignored) { }
			} else {
				return callback.complete(CommandResult.PREFIX_NOT_USED);
			}
		}

		String givenCommand = raw.substring(prefix.length());
		ICommand command = findCommand(givenCommand).orElse(null);

		if (command == null) {
			return callback.complete(CommandResult.COMMAND_NOT_FOUND);
		} else if (cooldown != null && cooldown.isOnCoolDown(message.getAuthor())) {
			return callback.complete(CommandResult.MEMBER_ON_COOLDOWN);
		} else if (event.isFromGuild() && !command.getType().isAccessibleFromGuild()) {
			return callback.complete(CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND);
		} else if (!event.isFromGuild() && !command.getType().isAccessibleFromPrivate()) {
			return callback.complete(CommandResult.INVALID_CHANNEL_GUILD_COMMAND);
		} else if (mentionPrefix && !command.shouldReactToMentionPrefix()) {
			return callback.complete(CommandResult.MENTION_PREFIX_NO_REACT);
		} else if (event instanceof MessageUpdateEvent && !command.shouldReactOnEdit()) {
			return callback.complete(CommandResult.MESSAGE_EDIT_NO_REACT);
		} else if (member != null && command.getPermissionNeeded() != null && !member.hasPermission(command.getPermissionNeeded())) {
			return callback.complete(CommandResult.NO_PERMISSIONS);
		} else if (command.isTeamCommand() && member != null && teamRankChecker != null && !teamRankChecker.hasTeamRank(member)) {
			return callback.complete(CommandResult.NO_PERMISSIONS_TEAM_RANK);
		} else if (webhookReactionBehavior != ReactionBehavior.ALWAYS && message.isWebhookMessage() && (!command.shouldReactToWebhooks() || webhookReactionBehavior == ReactionBehavior.NEVER)) {
			return callback.complete(CommandResult.WEBHOOK_MESSAGE_NO_REACT);
		} else if (botReactionBehavior != ReactionBehavior.ALWAYS && message.getAuthor().isBot() && (!command.shouldReactToBots() || botReactionBehavior == ReactionBehavior.NEVER)) {
			return callback.complete(CommandResult.BOT_MESSAGE_NO_REACT);
		}

		if (cooldown != null && member != null)
			cooldown.addToCoolDown(member);

		givenCommand = correctName(givenCommand, command);

		process(command, new CommandEventImpl(this, command, givenCommand, prefix, mentionPrefix, event), callback);
		return null;

	}

	protected void process(@Nonnull ICommand command, @Nonnull CommandEvent event, @Nonnull CompletableFuture<CommandResult> callback) {
		executedCommands.incrementAndGet();
		if (command.isAsync()) {
			executorService.execute(() -> execute(command, event, callback));
		} else {
			execute(command, event, callback);
		}
	}

	protected void execute(@Nonnull ICommand command, @Nonnull CommandEvent event, @Nonnull CompletableFuture<CommandResult> callback) {
		try {
			command.onCommand(event);
		} catch (Throwable ex) {
			callback.complete(CommandResult.EXCEPTION);
			callback.completeExceptionally(ex);
			Utils.handleException(ex);
			return;
		}
		callback.complete(CommandResult.SUCCESS);
	}

	@Nonnull
	@Override
	public CommandHandler registerCommand(@Nonnull ICommand command) {
		this.registry.add(command);
		return this;
	}

	@Nonnull
	@Override
	public CommandHandler registerCommands(@Nonnull ICommand... command) {
		Arrays.stream(command).forEach(this::registerCommand);
		return this;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public Collection<ICommand> getCommands() {
		return new ArrayList<>(registry);
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public Optional<ICommand> findCommand(@Nonnull String message) {
		return registry.stream().filter(command -> {
			if (nameIsOK(command.getName(), message))
				return true;
			for (String alias : command.getAlias())
				if (nameIsOK(alias, message))
					return true;
			return false;
		}).findFirst();
	}

	@CheckReturnValue
	protected boolean nameIsOK(@Nonnull String command, @Nonnull String message) {
		command = command.toLowerCase().trim();
		if (message.length() == command.length() && message.startsWith(command)) {
			return true;
		} else return message.startsWith(command + " ");
	}

	@Nonnull
	@CheckReturnValue
	protected String correctName(@Nonnull String message, @Nonnull ICommand command) {
		if (nameIsOK(command.getName(), message))
			return command.getName().toLowerCase();
		for (String alias : command.getAlias()) {
			if (nameIsOK(alias, message))
				return alias.toLowerCase();
		}
		throw new IllegalStateException();
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public Optional<ICommand> findCommand(@Nonnull Class<? extends ICommand> clazz) {
		return registry.stream().filter(command -> command.getClass() == clazz).findFirst();
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public PrefixProvider getPrefixProvider() {
		return prefixProvider;
	}

	@Nonnull
	@Override
	public CommandHandler setPrefixProvider(@Nonnull PrefixProvider provider) {
		this.prefixProvider = provider;
		return this;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public ReactionBehavior getWebHookReactionBehavior() {
		return webhookReactionBehavior;
	}

	@Nonnull
	@Override
	public CommandHandler setWebHookReactionBehavior(@Nonnull ReactionBehavior behavior) {
		this.webhookReactionBehavior = behavior;
		return this;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public ReactionBehavior getBotReactionBehavior() {
		return botReactionBehavior;
	}

	@Nonnull
	@Override
	public CommandHandler setBotReactionBehavior(@Nonnull ReactionBehavior behavior) {
		this.botReactionBehavior = behavior;
		return this;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public ThreadPoolExecutor getAsyncCommandExecutorService() {
		return executorService;
	}

	@Nullable
	@Override
	@CheckReturnValue
	public CoolDownManager getCoolDownManager() {
		return cooldown;
	}

	@Nonnull
	@Override
	public CommandHandler setCoolDownManager(@Nullable CoolDownManager manager) {
		this.cooldown = manager;
		return this;
	}

	@Nonnull
	@Override
	public CommandHandler setTeamRankChecker(@Nullable TeamRankChecker checker) {
		this.teamRankChecker = checker;
		return this;
	}

	@Nullable
	@Override
	@CheckReturnValue
	public TeamRankChecker getAccessChecker() {
		return teamRankChecker;
	}

	@Override
	public long getExecutedCommandsCount() {
		return executedCommands.get();
	}
}
