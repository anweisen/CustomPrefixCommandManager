package net.codingarea.engine.discord.commandmanager;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

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

	/**
	 * Used when the {@link ICommand Command}'s {@link CommandType} is {@link CommandType#PRIVATE private}
	 * @see CommandType#PRIVATE
	 * @see ICommand#getType()
	 */
	INVALID_CHANNEL_PRIVATE_COMMAND("You can only use this command in a private chat."),

	/**
	 * Used when the {@link ICommand Command}'s {@link CommandType} is {@link CommandType#GUILD guild}
	 * @see CommandType#GUILD
	 * @see ICommand#getType()
	 */
	INVALID_CHANNEL_GUILD_COMMAND("You can only use this command in a guild."),

	/**
	 * Used when a member hasn't enough permissions to execute a command
	 *
	 * @see CommandAccessChecker#isAllowed(Member, ICommand)
	 * @see ICommand#getPermissionNeeded()
	 * @see ICommand#isTeamCommand()
	 */
	NO_PERMISSIONS("You need `%permission%` for that!"),

	/**
	 * Used when a member is still on cooldown, cause he is using commands to quickly
	 *
	 * @see CommandHandler#getCoolDownManager()
	 */
	MEMBER_ON_COOLDOWN("You are currently on cooldown for `%cooldown%`."),

	/**
	 * Used when {@link Throwable Exception} is thrown while processing a command
	 */
	EXCEPTION("Something went wrong: `%exception%`"),

	/**
	 * Used when the {@link GenericMessageEvent MessageEvent}, which triggered the command event,
	 * was a web hook message and the {@link ICommand Command} should not react
	 *
	 * @see CommandHandler#getWebhookMessageBehavior()
	 * @see ICommand#shouldReactToWebhooks()
	 * @see ReactionBehavior
	 */
	WEBHOOK_MESSAGE_NO_REACT,

	/**
	 * Used when the {@link GenericMessageEvent MessageEvent}, which triggered the command event,
	 * was sent by a bot and the {@link ICommand Command} should not react
	 *
	 * @see CommandHandler#getWebhookMessageBehavior()
	 * @see ICommand#shouldReactToWebhooks()
	 * @see ReactionBehavior
	 */
	BOT_MESSAGE_NO_REACT,

	/**
	 * Used when the bot it self triggers the CommandEvent
	 */
	SELF_MESSAGE_NO_REACT,

	/**
	 * Used when the command event was triggered without using a valid prefix
	 *
	 * @see CommandHandler#handleCommand(String, MessageReceivedEvent)
	 * @see CommandHandler#handleCommand(String, MessageUpdateEvent)
	 */
	PREFIX_NOT_USED,

	/**
	 * Used when a mention prefix was used, which was not the guild prefix, and the {@link ICommand Command} should not react
	 *
	 * @see ICommand#shouldReactToMentionPrefix()
	 */
	MENTION_PREFIX_NO_REACT,

	/**
	 * Used when the command event was triggered by a message update, but the command should not react
	 *
	 * @see ICommand#executeOnUpdate()
	 */
	MESSAGE_EDIT_NO_REACT,

	/**
	 * Used when no command was found by the name used
	 *
	 * @see CommandHandler#getCommand(String)
	 */
	COMMAND_NOT_FOUND,

	/**
	 * Used when the {@link ICommand Command} was executed and no {@link Throwable Exception} was thrown
	 */
	SUCCESS;

	private String answer;

	CommandResult(String answer) {
		this.answer = answer;
	}

	CommandResult() {
		this.answer = null;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
