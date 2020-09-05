package net.anweisen.commandmanager.commands;

import net.anweisen.commandmanager.CommandEvent;
import net.anweisen.commandmanager.CommandType;

import javax.annotation.Nonnull;

/**
 * Developed in the CommandManager project
 * on 07-12-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public interface ICommand {

	void onCommand(@Nonnull final CommandEvent event);

	String getName();

	String[] getAlias();

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

}
