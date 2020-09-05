package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.CommandHandler;
import net.anweisen.commandmanager.listener.EventHandler;
import net.anweisen.commandmanager.listener.Listener;
import net.anweisen.commandmanager.utils.Factory;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

/**
 * Developed in the CommandManager project
 * on 08-23-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public final class DefaultMessageListener implements Listener {

	private final CommandHandler commandHandler;
	private final Factory<String, Guild> prefix;

	public DefaultMessageListener(@Nonnull CommandHandler commandHandler, @Nonnull String prefix) {
		this.commandHandler = commandHandler;
		this.prefix = guild -> prefix;
	}

	/**
	 * @param prefix The {@link Guild} param is null when the message is not from a guild
	 */
	public DefaultMessageListener(@Nonnull CommandHandler commandHandler, @Nonnull Factory<String, Guild> prefix) {
		this.commandHandler = commandHandler;
		this.prefix = prefix;
	}

	@EventHandler
	public void onMessage(MessageReceivedEvent event) {
		commandHandler.handleCommand(prefix.get(event.isFromGuild() ? event.getGuild() : null), event);
	}

}
