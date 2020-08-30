package net.anweisen.commandmanager.commands;

import net.anweisen.commandmanager.CommandEvent;
import net.anweisen.commandmanager.CommandType;

/**
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 * @author anweisen
 * @since 1.2
 */

public interface ICommand {

	void onCommand(CommandEvent event);

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
