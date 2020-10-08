package net.codingarea.engine.discord.commandmanager.events;

import net.codingarea.engine.discord.commandmanager.CommandEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
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
