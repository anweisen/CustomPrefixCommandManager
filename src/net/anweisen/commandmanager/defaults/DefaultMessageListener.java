package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.CommandHandler;
import net.anweisen.commandmanager.listener.EventHandler;
import net.anweisen.commandmanager.listener.Listener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

/**
 * CommandManager developed on 08-23-2020
 * https://github.com/anweisen
 * @author anweisen
 * @since 1.2.0
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
