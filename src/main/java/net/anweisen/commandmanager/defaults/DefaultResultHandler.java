package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.CommandResult;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultResultHandler implements BiConsumer<MessageReceivedEvent, CommandResult> {

	@Override
	public void accept(MessageReceivedEvent event, CommandResult result) {
		if (result.getAnswer() != null) {
			event.getChannel().sendMessage(result.getAnswer()).queue();
		}
	}

}
