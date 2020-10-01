package net.codingarea.botmanager.commandmanager;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.5
 */
public interface ResultHandler {

	void handle(@Nonnull MessageReceivedEvent event, @Nonnull CommandResult result, Object arg);

}
