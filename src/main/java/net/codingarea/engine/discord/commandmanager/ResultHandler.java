package net.codingarea.engine.discord.commandmanager;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.5
 */
public interface ResultHandler {

	void handle(@Nonnull GenericMessageEvent event, @Nonnull MessageChannel channel, @Nonnull User user,
	            @Nullable Member member, @Nonnull CommandResult result, @Nullable Object arg);

	default void handle(final @Nonnull GenericMessageEvent event, final @Nonnull CommandResult result, final @Nullable Object arg) {
		if (event instanceof MessageReceivedEvent) {
			MessageReceivedEvent receivedEvent = (MessageReceivedEvent) event;
			handle(event, event.getChannel(), receivedEvent.getAuthor(), receivedEvent.getMember(), result, arg);
		} else if (event instanceof MessageUpdateEvent) {
			MessageUpdateEvent updatedEvent = (MessageUpdateEvent) event;
			handle(event, event.getChannel(), updatedEvent.getAuthor(), updatedEvent.getMember(), result, arg);
		}
	}

}
