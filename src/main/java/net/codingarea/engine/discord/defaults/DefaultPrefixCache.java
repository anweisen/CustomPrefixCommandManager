package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.PrefixProvider;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.sql.cache.SQLValueCache;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.function.Function;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public class DefaultPrefixCache extends SQLValueCache implements Function<Guild, String>, PrefixProvider {

	@CheckReturnValue
	public DefaultPrefixCache(@Nonnull SQL data, @Nonnull String defaultPrefix) {
		super(data, "guilds", "guildID", "prefix", defaultPrefix);
	}

	@CheckReturnValue
	public DefaultPrefixCache(@Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn,
	                          @Nonnull String valueColumn, @Nonnull String defaultPrefix) {
		super(data, table, keyColumn, valueColumn, defaultPrefix);
	}

	@CheckReturnValue
	public DefaultPrefixCache(boolean cache, @Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn,
	                          @Nonnull String valueColumn, @Nonnull String defaultPrefix) {
		super(data, table, keyColumn, valueColumn, defaultPrefix);
		this.cacheValues = cache;
	}

	@CheckReturnValue
	public DefaultPrefixCache(boolean cache, @Nonnull String defaultPrefix, @Nonnull SQL data,
	                          @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn,
	                          int clearRate) {
		super(cache, defaultPrefix, data, table, keyColumn, valueColumn, clearRate);
	}

	/**
	 * Uses {@link Guild#getId()} as the key and passes it to {@link SQLValueCache#get(String)}
	 *
	 * @return {@link #getDefaultValue()} if guild is {@code null}
	 */
	@Nonnull
	@Override
	@CheckReturnValue
	public String apply(@Nullable Guild guild) {
		return super.get(guild == null ? null : guild.getId());
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public String getGuildPrefix(@Nonnull Guild guild) {
		return apply(guild);
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public String getDefaultPrefix() {
		return defaultValue;
	}

	@Override
	public void setGuildPrefix(@Nonnull Guild guild, @Nonnull String prefix) throws SQLException {
		set(guild.getId(), prefix);
	}

}
