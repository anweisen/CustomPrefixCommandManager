package net.codingarea.engine.discord.commandmanager;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.1
 *
 * @see ICommandHandler
 * @see CommandType
 */
public enum CommandResult {

	/**
	 * Used when the {@link ICommand Command}'s {@link CommandType} is {@link CommandType#PRIVATE PRIVATE}
	 *
	 * @see CommandType#PRIVATE
	 * @see ICommand#getType()
	 */
	INVALID_CHANNEL_PRIVATE_COMMAND("You can only use this command in a private chat"),

	/**
	 * Used when the {@link ICommand Command}'s {@link CommandType} is {@link CommandType#GUILD guild}
	 *
	 * @see CommandType#GUILD
	 * @see ICommand#getType()
	 */
	INVALID_CHANNEL_GUILD_COMMAND("You can only use this command in a guild"),

	/**
	 * Used when a member hasn't enough permissions to execute a command
	 *
	 * @see ICommand#getPermissionNeeded()
	 */
	NO_PERMISSIONS("You do not have enough permission for that"),

	/**
	 * Used when a member does not have the team rank and the command is a team command
	 *
	 * @see ICommand#isTeamCommand()
	 * @see TeamRankChecker#hasTeamRank(Member)
	 */
	NO_PERMISSIONS_TEAM_RANK("You need the team rank for that"),

	/**
	 * Used when a member is still on cooldown, cause he is using commands to quickly
	 *
	 * @see CommandHandler#getCoolDownManager()
	 */
	MEMBER_ON_COOLDOWN("You are currently on cooldown for `%cooldown%`"),

	/**
	 * Used when {@link Exception Exception} is thrown while processing a command
	 */
	EXCEPTION("Something went wrong"),

	/**
	 * Used when the {@link GenericMessageEvent MessageEvent}, which triggered the command event,
	 * was a web hook message and the {@link ICommand Command} should not react
	 *
	 * @see ICommandHandler#getWebHookReactionBehavior()
	 * @see ICommand#shouldReactToWebhooks()
	 * @see ReactionBehavior
	 */
	WEBHOOK_MESSAGE_NO_REACT,

	/**
	 * Used when the {@link GenericMessageEvent MessageEvent}, which triggered the command event,
	 * was sent by a bot and the {@link ICommand Command} should not react
	 *
	 * @see ICommandHandler#getBotReactionBehavior()
	 * @see ICommand#shouldReactToBots()
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
	 * @see PrefixProvider
	 * @see ICommandHandler#setPrefixProvider(PrefixProvider)
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
	 * @see ICommand#shouldReactOnEdit()
	 */
	MESSAGE_EDIT_NO_REACT,

	/**
	 * Used when the {@link MessageType} of the {@link Message}, which triggered the command, is not {@link MessageType#DEFAULT}
	 */
	INVALID_MESSAGE_TYPE,

	/**
	 * Used when no command was found by the name used
	 *
	 * @see ICommandHandler#findCommand(String)
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
