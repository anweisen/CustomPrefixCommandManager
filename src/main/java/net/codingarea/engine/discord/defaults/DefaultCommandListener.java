package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.CommandResult;
import net.codingarea.engine.discord.commandmanager.ICommandHandler;
import net.codingarea.engine.discord.listener.DiscordEvent;
import net.codingarea.engine.discord.listener.Listener;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public class DefaultCommandListener implements Listener {

	protected final ICommandHandler handler;

	public DefaultCommandListener(final @Nonnull ICommandHandler handler) {
		this.handler = handler;
	}

	@Nonnull
	@CheckReturnValue
	public ICommandHandler getHandler() {
		return handler;
	}

	@DiscordEvent
	public void onMessage(final @Nonnull MessageReceivedEvent event) {
		handler.handleEvent(event).thenAccept(result -> react(result, event));
	}

	@DiscordEvent
	public void onEdit(final @Nonnull MessageUpdateEvent event) {
		handler.handleEvent(event).thenAccept(result -> react(result, event));
	}

	protected void react(final @Nonnull CommandResult result, final @Nonnull GenericMessageEvent event) {

		if (result == CommandResult.SELF_MESSAGE_NO_REACT) return;
		String answer = result.getAnswer();
		if (answer == null) return;

		event.getChannel().sendMessage(answer).queue(message -> {}, ex -> {});

	}

}
