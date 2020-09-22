package net.codingarea.botmanager.defaults;

import net.codingarea.botmanager.sql.SQL;
import net.codingarea.botmanager.sql.cache.SQLValueCache;
import net.codingarea.botmanager.utils.Factory;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultPrefixCache extends SQLValueCache implements Factory<String, Guild> {

	@CheckReturnValue
	public DefaultPrefixCache(@Nonnull SQL data, @Nonnull String defaultPrefix) {
		super(data, "server", "guildID", "prefix", defaultPrefix);
	}

	@CheckReturnValue
	public DefaultPrefixCache(@Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, @Nonnull String defaultPrefix) {
		super(data, table, keyColumn, valueColumn, defaultPrefix);
	}

	@CheckReturnValue
	public DefaultPrefixCache(boolean cache, @Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, @Nonnull String defaultPrefix) {
		super(data, table, keyColumn, valueColumn, defaultPrefix);
		this.cacheValues = cache;
	}

	@CheckReturnValue
	public DefaultPrefixCache(boolean cache, @Nonnull String defaultPrefix, @Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, int clearRate) {
		super(cache, defaultPrefix, data, table, keyColumn, valueColumn, clearRate);
	}

	/**
	 * Uses {@link Guild#getId()} as the key and passes it to {@link SQLValueCache#get(String)}
	 * @return {@link #getDefaultValue()} if guild is <code>null</code>
	 */
	@Nonnull
	@Override
	@CheckReturnValue
	public String get(Guild guild) {
		return super.get(guild == null ? null : guild.getId());
	}

}
