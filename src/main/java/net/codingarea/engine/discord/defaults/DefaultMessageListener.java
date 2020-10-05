package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.CommandHandler;
import net.codingarea.engine.discord.listener.DiscordEvent;
import net.codingarea.engine.discord.listener.Listener;
import net.codingarea.engine.utils.Factory;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public class DefaultMessageListener implements Listener {

	private final CommandHandler commandHandler;
	private Factory<String, Guild> prefix;

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

	@DiscordEvent
	public void onMessage(MessageReceivedEvent event) {
		commandHandler.handleCommand(prefix.get(event.isFromGuild() ? event.getGuild() : null), event);
	}

}
