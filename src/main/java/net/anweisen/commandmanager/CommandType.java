package net.anweisen.commandmanager;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * @see net.anweisen.commandmanager.commands.ICommand
 * @see net.anweisen.commandmanager.CommandHandler
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public enum CommandType {

	/**
	 * Commands of this type are {@link CommandType#PRIVATE} and {@link CommandType#GUILD}
	 */
	GENERAL,

	/**
	 * Commands of this type can only be executed in a private chat
	 * If this is not the case {@link CommandHandler#handleCommand(String, MessageReceivedEvent)}
	 * will return {@link CommandResult#INVALID_CHANNEL_PRIVATE_COMMAND}
	 */
	PRIVATE,

	/**
	 * Commands of this type can only be executed in a guild chat
	 * If this is not the case {@link CommandHandler#handleCommand(String, MessageReceivedEvent)}
	 * will return {@link CommandResult#INVALID_CHANNEL_GUILD_COMMAND}
	 */
	GUILD;

}
