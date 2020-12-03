package net.codingarea.engine.discord.commandmanager;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 *
 * @see ICommand
 * @see ICommand#getType()
 * @see ICommandHandler
 */
public enum CommandType {

	/**
	 * Commands of this type are {@link CommandType#PRIVATE} and {@link CommandType#GUILD}.
	 * They can be used in both cases
	 */
	GENERAL,

	/**
	 * Commands of this type can only be executed in a private chat.
	 * If this is not the case {@link CommandResult#INVALID_CHANNEL_GUILD_COMMAND} will be returned
	 *
	 * @see #isAccessibleFromPrivate()
	 */
	PRIVATE,

	/**
	 * Commands of this type can only be executed in a guild chat.
	 * If this is not the case {@link CommandResult#INVALID_CHANNEL_GUILD_COMMAND} will be returned
	 *
	 * @see #isAccessibleFromGuild()
	 */
	GUILD;

	public boolean isAccessibleFromGuild() {
		return this == GENERAL || this == GUILD;
	}

	public boolean isAccessibleFromPrivate() {
		return this == GENERAL || this == PRIVATE;
	}

}
