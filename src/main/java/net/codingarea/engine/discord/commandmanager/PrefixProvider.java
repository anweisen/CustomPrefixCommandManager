package net.codingarea.engine.discord.commandmanager;

import net.dv8tion.jda.api.entities.Guild;

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
