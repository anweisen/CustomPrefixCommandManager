package net.codingarea.botmanager.commandmanager;

import net.codingarea.botmanager.commandmanager.commands.ICommand;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.5
 */
public enum ReactionBehavior {

	NEVER,

	/**
	 * @see ICommand#shouldReactToBots()
	 * @see ICommand#shouldReactToWebhooks()
	 */
	COMMAND_DECISION,

	ALWAYS

}
