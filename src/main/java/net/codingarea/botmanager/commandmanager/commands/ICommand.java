package net.codingarea.botmanager.commandmanager.commands;

import net.codingarea.botmanager.commandmanager.CommandEvent;
import net.codingarea.botmanager.commandmanager.CommandType;
import net.dv8tion.jda.api.Permission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public interface ICommand {

	void onCommand(@Nonnull final CommandEvent event) throws Throwable;

	@Nonnull
	String getName();

	@Nonnull
	String[] getAlias();

	@Nullable
	Permission getPermissionNeeded();

	@Nonnull
	default CommandType getType() {
		return CommandType.GENERAL;
	}

	default boolean shouldReactToWebhooks() {
		return false;
	}

	default boolean shouldReactToBots() {
		return false;
	}

	default boolean shouldProcessInNewThread() {
		return false;
	}

	default boolean shouldReactToMentionPrefix() {
		return false;
	}

	default boolean isTeamCommand() {
		return false;
	}

}
