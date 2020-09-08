package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.sql.SQL;
import net.anweisen.commandmanager.utils.Bindable;
import net.anweisen.commandmanager.utils.Factory;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Developed in the CommandManager project
 * on 09-06-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class DefaultPrefixCache implements Factory<String, Guild>, Bindable {

	//                          guildID, prefix
	private final transient Map<String, String> cache = new HashMap<>();
	private boolean cachePrefix;

	private String defaultPrefix;

	private final String table, guildColumn, prefixColumn;
	private final SQL dataSource;

	public DefaultPrefixCache(@Nonnull String defaultPrefix, @Nonnull SQL dataSource) {
		this(true, defaultPrefix, "server", "guildID", "prefix", dataSource);
	}

	public DefaultPrefixCache(boolean cachePrefix, @Nonnull String defaultPrefix, @Nonnull String table, @Nonnull String guildColumn, @Nonnull String prefixColumn, @Nonnull SQL dataSource) {
		this.cachePrefix = cachePrefix;
		this.defaultPrefix = defaultPrefix;
		this.table = table;
		this.guildColumn = guildColumn;
		this.prefixColumn = prefixColumn;
		this.dataSource = dataSource;
		checkDatabaseTable();
	}

	private void checkDatabaseTable() {
		try {
			dataSource.update("CREATE TABLE IF NOT EXISTS " + table + " (" + guildColumn + " VARCHAR(18), " + prefixColumn + " VARCHAR(1000))");
		} catch (SQLException ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * @param guildID The key, the prefix is stored to
	 * @return The prefix which is currently saved in this class. It will be null if no prefix is cached
	 */
	@Nullable
	@CheckReturnValue
	public String getCached(@Nonnull String guildID) {
		return cache.get(guildID);
	}

	@CheckReturnValue
	public boolean isCached(@Nonnull String guildID) {
		return getCached(guildID) != null;
	}

	/**
	 * @param guildID The guild, the prefix is assigned to
	 * @param prefix The prefix which should be stored. If null, the entry will be removed
	 * @exception IllegalStateException If not {@link DefaultPrefixCache#shouldCachePrefix()}
	 */
	public synchronized void setCached(@Nonnull String guildID, String prefix) {
		if (!cachePrefix) throw new IllegalStateException();
		if (prefix == null) {
			cache.remove(guildID);
		} else {
			cache.put(guildID, prefix);
		}
	}

	public void setDefaultPrefix(@Nonnull String defaultPrefix) {
		this.defaultPrefix = defaultPrefix;
	}

	@Nonnull
	@CheckReturnValue
	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	@Nonnull
	@CheckReturnValue
	public SQL getDataSource() {
		return dataSource;
	}

	public boolean shouldCachePrefix() {
		return cachePrefix;
	}

	/**
	 * Sets if, when a prefix was loaded, it will be stored and
	 */
	public void setCachePrefix(boolean cache) {
		if (this.cachePrefix != cache) {
			this.cache.clear();
		}
		this.cachePrefix = cache;
	}

	/**
	 * @param guildID The key, the prefix is saved to
	 * @return The prefix saved in the database by the given guildID
	 * @throws SQLException If the database does not contain a entry for the given guildID (or an {@link SQLException} happens while using the sql
	 */
	public String getFromDatabase(String guildID) throws SQLException {
		ResultSet result = dataSource.query("SELECT " + prefixColumn + " FROM " + table + " WHERE " + guildColumn + " = ?", guildID);
		if (!result.next()) throw new IllegalStateException("Database does not contain the given guild");
		String prefix = result.getString(prefixColumn);
		result.close();
		return prefix;
	}

	/**
	 * Reads the prefix from the database by the given. If not prefix was found it will return the <code>defaultPrefix</code>.
	 * If {@link DefaultPrefixCache#shouldCachePrefix()}, it'll cache the prefix as well (using {@link DefaultPrefixCache#setCached(String, String)})
	 * @return The prefix loaded
	 */
	@Nonnull
	public String load(@Nonnull String guildID) {

		String prefix = defaultPrefix;

		try {
			prefix = getFromDatabase(guildID);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IllegalStateException ignored) { }

		if (prefix == null) prefix = defaultPrefix;

		if (cachePrefix) {
			setCached(guildID, prefix);
		}

		return prefix;

	}

	/**
	 * @return The cached prefix, if {@link DefaultPrefixCache#shouldCachePrefix()} is enabled. Otherwise it will load the prefix using {@link DefaultPrefixCache#load(String)}
	 */
	@Nonnull
	@CheckReturnValue
	public String getPrefix(@Nonnull String guildID) {

		if (cachePrefix) {
			String prefix = getCached(guildID);
			if (prefix != null) {
				return prefix;
			}
		}

		return load(guildID);

	}

	/**
	 * Sets a prefix for the given guildID and writes it into the database.
	 * If {@link DefaultPrefixCache#shouldCachePrefix()}, it will also cache the prefix (using {@link DefaultPrefixCache#setCached(String, String)})
	 * @param guildID The guildID, the prefix should be saved to
	 * @param prefix The prefix to set
	 * @throws SQLException If a {@link SQLException} is thrown while executing the sql request
	 */
	public synchronized void set(String guildID, String prefix) throws SQLException {
		if (cachePrefix) setCached(guildID, prefix);
		try {
			getFromDatabase(guildID);
			dataSource.update("UPDATE " + table + " SET " + prefixColumn + " = ? WHERE " + guildColumn + " = ?", prefix, guildID);
		} catch (IllegalStateException ignored) {
			dataSource.update("INSERT INTO " + table + " (" + guildColumn + ", " + prefixColumn + ") VALUES (?, ?)", guildID, prefix);
		}
	}

	/**
	 * Loads the prefix using {@link DefaultPrefixCache#load(String)}, by the given guild ({@link Guild#getId()})
	 * If the guild is null, it will return the <code>defaultPrefix</code>
	 * @param guild The guild, whose id ({@link Guild#getId()}) will be used to load the prefix. If null, it will return the <code>defaultPrefix</code>
	 * @return Returns the prefix loaded
	 */
	@Nonnull
	@Override
	public String get(Guild guild) {
		if (guild == null) {
			return defaultPrefix;
		} else {
			return getPrefix(guild.getId());
		}
	}

}
