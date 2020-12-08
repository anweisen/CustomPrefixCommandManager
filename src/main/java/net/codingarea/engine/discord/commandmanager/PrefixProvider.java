package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public interface PrefixProvider {

	@Nonnull
	@CheckReturnValue
	String getGuildPrefix(@Nonnull Guild guild);

	@Nonnull
	@CheckReturnValue
	String getDefaultPrefix();

	default void setGuildPrefix(@Nonnull Guild guild, @Nonnull String prefix) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@CheckReturnValue
	default String getPrefix(@Nonnull GenericMessageEvent event) {
		return event.isFromGuild() ? getGuildPrefix(event.getGuild()) : getDefaultPrefix();
	}

	@Nonnull
	@CheckReturnValue
	default String getPrefix(@Nonnull CommandEvent event) {
		return getPrefix(event.getEvent());
	}

	static PrefixProvider constant(@Nonnull String prefix) {
		return new PrefixProvider() {

			@Nonnull
			@Override
			public String getGuildPrefix(final @Nonnull Guild guild) {
				return prefix;
			}

			@Nonnull
			@Override
			public String getDefaultPrefix() {
				return prefix;
			}

		};
	}

}
