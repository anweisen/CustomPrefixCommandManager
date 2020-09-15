package net.codingarea.botmanager.defaults;

import net.codingarea.botmanager.commandmanager.CommandHandler;
import net.codingarea.botmanager.commandmanager.CommandResult;
import net.codingarea.botmanager.listener.EventHandler;
import net.codingarea.botmanager.listener.Listener;
import net.codingarea.botmanager.utils.Factory;
import net.codingarea.botmanager.utils.TripleConsumer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public final class DefaultMessageListener implements Listener {

	private final CommandHandler commandHandler;
	private Factory<String, Guild> prefix;
	private TripleConsumer<MessageReceivedEvent, CommandResult, Object> resultHandler;

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

	/**
	 * @param prefix The {@link Guild} param is null when the message is not from a guild
	 */
	public DefaultMessageListener(@Nonnull CommandHandler commandHandler, @Nonnull Factory<String, Guild> prefix, @Nonnull TripleConsumer<MessageReceivedEvent, CommandResult, Object> resultHandler) {
		this.commandHandler = commandHandler;
		this.prefix = prefix;
		this.resultHandler = resultHandler;
	}

	/**
	 * @param prefix {@link Factory#get(Object)} is used to get the prefix which should be used
	 *               The {@link Guild} param is null when the message is not from a guild
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public DefaultMessageListener setPrefix(@Nonnull Factory<String, Guild> prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * @param resultHandler <code>null</code> to disable results
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public DefaultMessageListener setResultHandler(TripleConsumer<MessageReceivedEvent, CommandResult, Object> resultHandler) {
		this.resultHandler = resultHandler;
		return this;
	}

	@EventHandler
	public void onMessage(MessageReceivedEvent event) {
		commandHandler.handleCommand(prefix.get(event.isFromGuild() ? event.getGuild() : null), event, resultHandler);
	}

}
