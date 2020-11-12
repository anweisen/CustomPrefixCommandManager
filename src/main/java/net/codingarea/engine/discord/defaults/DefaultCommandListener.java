package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.CommandHandler;
import net.codingarea.engine.discord.listener.DiscordEvent;
import net.codingarea.engine.discord.listener.Listener;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public class DefaultCommandListener implements Listener {

	private final CommandHandler commandHandler;
	private Function<Guild, String> prefix;

	public DefaultCommandListener(@Nonnull CommandHandler commandHandler, @Nonnull String prefix) {
		this.commandHandler = commandHandler;
		this.prefix = guild -> prefix;
	}

	/**
	 * @param prefix The {@link Guild} param is {@code null} when the message is not from a guild
	 */
	public DefaultCommandListener(@Nonnull CommandHandler commandHandler, @Nonnull Function<Guild, String> prefix) {
		this.commandHandler = commandHandler;
		this.prefix = prefix;
	}

	/**
	 * @param prefix {@link Function#apply(Object)} is used to get the prefix which should be used
	 *               The {@link Guild} param is {@code null} when the message is not from a guild
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public DefaultCommandListener setPrefix(final @Nonnull Function<Guild, String> prefix) {
		this.prefix = prefix;
		return this;
	}

	@DiscordEvent
	public void onMessage(MessageReceivedEvent event) {
		commandHandler.handleCommand(prefix.apply(event.isFromGuild() ? event.getGuild() : null), event);
	}

	@DiscordEvent
	public void onEdit(MessageUpdateEvent event) {
		commandHandler.handleCommand(prefix.apply(event.isFromGuild() ? event.getGuild() : null), event);
	}

}
