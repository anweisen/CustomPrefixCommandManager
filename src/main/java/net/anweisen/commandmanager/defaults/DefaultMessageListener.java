package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.CommandHandler;
import net.anweisen.commandmanager.CommandResult;
import net.anweisen.commandmanager.listener.EventHandler;
import net.anweisen.commandmanager.listener.Listener;
import net.anweisen.commandmanager.utils.Factory;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public final class DefaultMessageListener implements Listener {

	private final CommandHandler commandHandler;
	private Factory<String, Guild> prefix;
	private BiConsumer<MessageReceivedEvent, CommandResult> resultHandler;

	public DefaultMessageListener(@Nonnull CommandHandler commandHandler, @Nonnull String prefix) {
		this.commandHandler = commandHandler;
		this.prefix = guild -> prefix;
		this.resultHandler = new DefaultResultHandler();
	}

	/**
	 * @param prefix The {@link Guild} param is null when the message is not from a guild
	 */
	public DefaultMessageListener(@Nonnull CommandHandler commandHandler, @Nonnull Factory<String, Guild> prefix) {
		this.commandHandler = commandHandler;
		this.prefix = prefix;
		this.resultHandler = new DefaultResultHandler();
	}

	public DefaultMessageListener(@Nonnull CommandHandler commandHandler, @Nonnull Factory<String, Guild> prefix, @Nonnull BiConsumer<MessageReceivedEvent, CommandResult> resultHandler) {
		this.commandHandler = commandHandler;
		this.prefix = prefix;
		this.resultHandler = resultHandler;
	}

	public void setPrefix(@Nonnull Factory<String, Guild> prefix) {
		this.prefix = prefix;
	}

	public void setResultHandler(BiConsumer<MessageReceivedEvent, CommandResult> resultHandler) {
		this.resultHandler = resultHandler;
	}

	@EventHandler
	public void onMessage(MessageReceivedEvent event) {
		CommandResult result = commandHandler.handleCommand(prefix.get(event.isFromGuild() ? event.getGuild() : null), event);
		if (resultHandler != null) {
			resultHandler.accept(event, result);
		}
	}

}
