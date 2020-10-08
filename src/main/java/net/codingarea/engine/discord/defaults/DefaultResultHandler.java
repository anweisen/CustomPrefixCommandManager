package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.CommandHandler;
import net.codingarea.engine.discord.commandmanager.CommandResult;
import net.codingarea.engine.discord.commandmanager.ResultHandler;
import net.codingarea.engine.utils.NumberFormatter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 * @see CommandHandler
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public class DefaultResultHandler implements ResultHandler {

	@Override
	public void handle(@Nullable GenericMessageEvent event, @Nonnull MessageChannel channel, @Nonnull User user,
	                   @Nullable Member member, @Nonnull CommandResult result, Object arg) {

		String answer = result.getAnswer();
		if (answer == null) return;

		switch (result) {
			case WEBHOOK_MESSAGE_NO_REACT:
			case MENTION_PREFIX_NO_REACT:
			case BOT_MESSAGE_NO_REACT:
			case SUCCESS:
			case PREFIX_NOT_USED:
			default:
				break;

			case COMMAND_NOT_FOUND:
				if (arg instanceof String) {
					answer = answer.replace("%command%", (String) arg);
				}
				break;

			case MEMBER_ON_COOLDOWN:
				if (arg instanceof Number) {
					float time = ((Number) arg).floatValue() / 1000F;
					answer = answer.replace("%cooldown%", NumberFormatter.TIME.format(time));
				}
				break;
			case NO_PERMISSIONS:
				if (arg instanceof Permission && !member.hasPermission((Permission) arg)) {
					answer = answer.replace("%permission%", ((Permission) arg).getName());
				} else {
					answer = answer.replace("%permission%", "TeamRank");
				}
				break;

			case EXCEPTION:
				Throwable ex = null;
				if (arg instanceof Throwable) {
					ex = (Throwable) arg;
					while (ex.getCause() != null) {
						ex = ex.getCause();
					}
				}
				answer = answer.replace("%exception%", ex != null ? ex.getClass().getSimpleName() : "null")
						.replace("%messages%", ex != null && ex.getMessage() != null ? ex.getMessage() : "null");
				break;
		}

		channel.sendMessage(answer).queue();

	}
}
