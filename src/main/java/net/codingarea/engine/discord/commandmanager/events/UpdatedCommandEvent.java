package net.codingarea.engine.discord.commandmanager.events;

import net.codingarea.engine.discord.commandmanager.CommandHandler;
import net.codingarea.engine.discord.commandmanager.ICommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.Nonnull;

/**
 * @see CommandHandler
 * @see ICommand
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public class UpdatedCommandEvent extends GenericCommandEvent {

	protected final MessageUpdateEvent event;

	public UpdatedCommandEvent(@Nonnull MessageUpdateEvent event, @Nonnull String prefix, @Nonnull String commandName) {
		super(event, event.getMessage(), prefix, commandName);
		this.event = event;
	}

	@Nonnull
	@Override
	public MessageReceivedEvent getReceiveEvent() {
		throw new IllegalStateException();
	}

	@Nonnull
	@Override
	public MessageUpdateEvent getUpdateEvent() {
		return event;
	}

}
