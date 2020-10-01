package net.codingarea.botmanager.defaults;

import net.codingarea.botmanager.commandmanager.CommandHandler;
import net.codingarea.botmanager.commandmanager.CommandResult;
import net.codingarea.botmanager.commandmanager.ResultHandler;
import net.codingarea.botmanager.utils.NumberFormatter;
import net.codingarea.botmanager.utils.TripleConsumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.sql.SQLException;

/**
 * @see CommandHandler
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultResultHandler implements ResultHandler {

	@Override
	public void handle(@Nonnull MessageReceivedEvent event, @Nonnull CommandResult result, Object arg) {

		String answer = result.getAnswer();
		if (answer == null) return;

		switch (result) {
			case WEBHOOK_MESSAGE_NO_REACT:
			case MENTION_PREFIX_NO_REACT:
			case BOT_MESSAGE_NO_REACT:
			case COMMAND_NOT_FOUND:
			case SUCCESS:
			case PREFIX_NOT_USED:
			default:
				break;

			case MEMBER_ON_COOLDOWN:
				if (arg instanceof Number) {
					float time = ((Number) arg).floatValue() / 1000F;
					answer = answer.replace("%cooldown%", NumberFormatter.TIME.format(time));
				}
				break;
			case NO_PERMISSIONS:
				if (arg instanceof Permission && !event.getMember().hasPermission((Permission) arg)) {
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
				answer = answer.replace("%exception%", ex != null ? ex.getClass().getSimpleName() : "null");
				break;
		}

		event.getChannel().sendMessage(answer).queue();

	}
}
