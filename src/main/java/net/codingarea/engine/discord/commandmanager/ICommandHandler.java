package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.utils.CoolDownManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public interface ICommandHandler {

	@Nonnull
	ICommandHandler registerCommand(@Nonnull ICommand command);

	@Nonnull
	ICommandHandler registerCommands(@Nonnull ICommand... command);

	/**
	 * @return A copy of the {@link Collection} with all current registered commands
	 * @implNote Return a copy of the registry and not the actual list!
	 */
	@Nonnull
	@CheckReturnValue
	Collection<ICommand> getCommands();

	@Nonnull
	@CheckReturnValue
	Optional<ICommand> findCommand(@Nonnull String message);


	@Nonnull
	@CheckReturnValue
	Optional<ICommand> findCommand(@Nonnull Class<? extends ICommand> clazz);

	@Nonnull
	ICommandHandler setPrefixProvider(@Nonnull PrefixProvider provider);

	@Nonnull
	@CheckReturnValue
	PrefixProvider getPrefixProvider();

	@Nonnull
	@CheckReturnValue
	ReactionBehavior getBotReactionBehavior();

	@Nonnull
	ICommandHandler setBotReactionBehavior(@Nonnull ReactionBehavior behavior);

	@Nonnull
	@CheckReturnValue
	ReactionBehavior getWebHookReactionBehavior();

	@Nonnull
	ICommandHandler setWebHookReactionBehavior(@Nonnull ReactionBehavior behavior);

	@Nonnull
	@CheckReturnValue
	ThreadPoolExecutor getAsyncCommandExecutorService();

	@Nullable
	@CheckReturnValue
	CoolDownManager getCoolDownManager();

	@Nonnull
	ICommandHandler setCoolDownManager(@Nullable CoolDownManager manager);

	@Nonnull
	default ICommandHandler setCoolDown(float seconds) {
		return setCoolDownManager(new CoolDownManager(seconds));
	}

	@Nonnull
	default ICommandHandler setCoolDown(long millis) {
		return setCoolDownManager(new CoolDownManager(millis));
	}

	@CheckReturnValue
	long getExecutedCommandsCount();

	@Nonnull
	ICommandHandler setTeamRankChecker(@Nullable TeamRankChecker checker);

	@Nullable
	@CheckReturnValue
	TeamRankChecker getAccessChecker();

	default boolean hasAccess(@Nonnull Member member, @Nonnull ICommand command) {
		return command.getPermissionNeeded() != null && member.hasPermission(command.getPermissionNeeded())
		 	|| command.isTeamCommand() && getAccessChecker() != null && getAccessChecker().hasTeamRank(member);
	}

	@Nonnull
	CompletableFuture<CommandResult> handleEvent(@Nonnull MessageUpdateEvent event);

	@Nonnull
	CompletableFuture<CommandResult> handleEvent(@Nonnull MessageReceivedEvent event);

}
