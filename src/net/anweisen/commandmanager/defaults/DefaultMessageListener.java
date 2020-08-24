package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.CommandHandler;
import net.anweisen.commandmanager.listener.EventHandler;
import net.anweisen.commandmanager.listener.Listener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * @author anweisen
 * CommandManager developed on 08-23-2020
 * https://github.com/anweisen
 */


public final class DefaultMessageListener implements Listener {

	private final CommandHandler commandHandler;
	private final String prefix;

	public DefaultMessageListener(CommandHandler commandHandler, String prefix) {
		this.commandHandler = commandHandler;
		this.prefix = prefix;
	}

	@EventHandler
	public final void onNewMessage(MessageReceivedEvent event) {
		commandHandler.handleCommand(prefix, event);
	}

}
