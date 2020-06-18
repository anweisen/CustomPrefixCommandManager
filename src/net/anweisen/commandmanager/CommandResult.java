package net.anweisen.commandmanager;

/**
 * @author anweisen
 * CommandManager developed on 05-25-2020
 * Website: www.anweisen.net
 */

public enum CommandResult {

    INVALID_CHANNEL_PRIVATE_COMMAND,
    INVALID_CHANNEL_GUILD_COMMAND,
    WEBHOOK_MESSAGE_NO_REACT,
    BOT_MESSAGE_NO_REACT,
    PREFIX_NOT_USED,
    COMMAND_NOT_FOUND,
    SUCCESS;

}
