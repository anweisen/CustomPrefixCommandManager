package net.codingarea.botmanager.defaults;

import net.codingarea.botmanager.sql.SQL;
import net.codingarea.botmanager.sql.cache.SQLValueCache;
import net.codingarea.botmanager.utils.Bindable;
import net.codingarea.botmanager.utils.Factory;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultPrefixCache extends SQLValueCache implements Factory<String, Guild>, Bindable {

	public DefaultPrefixCache(@Nonnull SQL data, @Nonnull String defaultPrefix) {
		super(data, "server", "guildID", "prefix", defaultPrefix);
	}

	public DefaultPrefixCache(@NotNull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, @Nonnull String defaultValue) {
		super(data, table, keyColumn, valueColumn, defaultValue);
	}

	public DefaultPrefixCache(boolean cache, @NotNull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, @Nonnull String defaultValue) {
		super(data, table, keyColumn, valueColumn, defaultValue);
		this.cacheValues = cache;
	}

	public DefaultPrefixCache(boolean cacheValues, @Nonnull String defaultValue, @Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, int clearRate) {
		super(cacheValues, defaultValue, data, table, keyColumn, valueColumn, clearRate);
	}

	/**
	 * Uses {@link Guild#getId()} as the key and passes it to {@link SQLValueCache#get(String)}
	 */
	@Nonnull
	@Override
	@CheckReturnValue
	public String get(Guild guild) {
		return super.get(guild == null ? null : guild.getId());
	}

}
