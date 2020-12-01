package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.utils.IAlias;
import net.codingarea.engine.utils.INamed;
import net.dv8tion.jda.api.Permission;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public interface ICommand extends INamed, IAlias {

	void onCommand(@Nonnull CommandEvent event) throws Exception;

	@Nonnull
	@Override
	@CheckReturnValue
	String getName();

	@Nonnull
	@Override
	@CheckReturnValue
	String[] getAlias();

	@Nullable
	@CheckReturnValue
	default String getDescription() {
		return null;
	}

	@Nullable
	@CheckReturnValue
	default Permission getPermissionNeeded(){
		return null;
	}

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
	default boolean isAsync() {
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
	default boolean shouldReactOnEdit() {
		return true;
	}

}
