package net.codingarea.botmanager.defaults;

import net.codingarea.botmanager.commandmanager.CommandHandler;
import net.codingarea.botmanager.commandmanager.CommandResult;
import net.codingarea.botmanager.utils.NumberFormatter;
import net.codingarea.botmanager.utils.TripleConsumer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @see CommandHandler
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultResultHandler implements TripleConsumer<MessageReceivedEvent, CommandResult, Object> {

	@Override
	public void accept(MessageReceivedEvent event, CommandResult result, Object obj) {

		String answer = result.getAnswer();
		if (answer == null) return;

		if (obj instanceof Throwable) {
			Throwable thrown = (Throwable) obj;
			answer = answer.replace("%exception%", thrown.getClass().getName())
						   .replace("%message%", String.valueOf(thrown.getMessage()));
		} else if (obj instanceof Permission) {
			Permission permission = (Permission) obj;
			answer = answer.replace("%permission%", permission.getName());
		} else if (obj instanceof Long) {
			long cooldown = (Long) obj;
			float seconds = cooldown / 1000F;
			String formatted = NumberFormatter.DEFAULT.format(seconds);
			answer = answer.replace("%cooldown%", formatted);
		} else {
			String command = String.valueOf(obj);
			answer = answer.replace("%command%", command);
		}

		event.getChannel().sendMessage(answer).queue();

	}
}
