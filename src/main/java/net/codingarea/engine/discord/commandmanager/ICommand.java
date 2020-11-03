package net.codingarea.engine.discord.commandmanager;

import net.dv8tion.jda.api.Permission;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public interface ICommand {

	void onCommand(@Nonnull final CommandEvent event) throws Exception;

	@Nonnull
	@CheckReturnValue
	String getName();

	@Nonnull
	@CheckReturnValue
	String[] getAlias();

	@Nullable
	@CheckReturnValue
	default String getDescription() {
		return null;
	}

	@Nullable
	@CheckReturnValue
	Permission getPermissionNeeded();

	@Nonnull
	@CheckReturnValue
	default CommandType getType() {
		return CommandType.GENERAL;
	}

	@CheckReturnValue
	default boolean shouldReactToWebhooks() {
		return false;
	}

	@CheckReturnValue
	default boolean shouldReactToBots() {
		return false;
	}

	@CheckReturnValue
	default boolean shouldProcessInNewThread() {
		return false;
	}

	@CheckReturnValue
	default boolean shouldReactToMentionPrefix() {
		return true;
	}

	@CheckReturnValue
	default boolean isTeamCommand() {
		return false;
	}

	@CheckReturnValue
	default boolean executeOnUpdate() {
		return true;
	}

}
