package net.anweisen.commandmanager;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * {@link CommandHandler#handleCommand(String, MessageReceivedEvent)}
 * returns one enum state of this enum, depending on how the command process went
 *
 * @see CommandHandler#handleCommand(String, MessageReceivedEvent)
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.1
 */
public enum CommandResult {

	INVALID_CHANNEL_PRIVATE_COMMAND,
	INVALID_CHANNEL_GUILD_COMMAND,
	WEBHOOK_MESSAGE_NO_REACT,
	BOT_MESSAGE_NO_REACT,
	PREFIX_NOT_USED,
	MENTION_PREFIX_NO_REACT,
	COMMAND_NOT_FOUND,
	SUCCESS;

}
