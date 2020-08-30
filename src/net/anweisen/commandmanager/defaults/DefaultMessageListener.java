package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.CommandHandler;
import net.anweisen.commandmanager.listener.EventHandler;
import net.anweisen.commandmanager.listener.Listener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Developed in the CommandManager project
 * on 08-23-2020
 *
 * @author anweisen
 * @since 1.2
 */

public final class DefaultMessageListener implements Listener {

	private final CommandHandler commandHandler;
	private final String prefix;

	public DefaultMessageListener(@NotNull CommandHandler commandHandler, @NotNull String prefix) {
		this.commandHandler = commandHandler;
		this.prefix = prefix;
	}

	@EventHandler
	public final void onMessageReceived(MessageReceivedEvent event) {
		commandHandler.handleCommand(prefix, event);
	}

}
