package net.codingarea.botmanager.commandmanager;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * {@link CommandHandler#handleCommand(String, MessageReceivedEvent)}
 * returns one enum state of this enum, depending on how the command process went
 *
 * @see CommandHandler#handleCommand(String, MessageReceivedEvent)
 * @see CommandType
 * @author anweisen | https://github.com/anweisen
 * @since 1.1
 */
public enum CommandResult {

	INVALID_CHANNEL_PRIVATE_COMMAND("You can only use this command in a private chat."),
	INVALID_CHANNEL_GUILD_COMMAND("You can only use this command in a guild."),
	NO_PERMISSIONS("You need the permission `%permission%` for that!"),
	MEMBER_ON_COOLDOWN("You are currently on cooldown for `%cooldown%s`."),
	EXCEPTION("Something went wrong: `%exception%: %message%`"),
	WEBHOOK_MESSAGE_NO_REACT,
	BOT_MESSAGE_NO_REACT,
	PREFIX_NOT_USED,
	MENTION_PREFIX_NO_REACT,
	COMMAND_NOT_FOUND,
	SUCCESS;

	private String answer;

	CommandResult(String answer) {
		this.answer = answer;
	}

	CommandResult() { }

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
