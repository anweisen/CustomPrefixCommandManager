package net.codingarea.botmanager.sql.cache;

import net.codingarea.botmanager.sql.SQL;
import net.codingarea.botmanager.utils.Bindable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public class SQLValueCache implements Bindable {

	public static final int DEFAULT_CLEAR_RATE = 3 * 60;

	protected final Map<String, String> cache = new HashMap<>();
	protected boolean cacheValues;

	protected String defaultValue;
	protected final SQL data;
	protected final String table, keyColumn, valueColumn;

	protected int clearRate;
	protected Timer timer;

	@CheckReturnValue
	public SQLValueCache(boolean cacheValues, @Nonnull String defaultValue, @Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, int clearRate) {
		this.cacheValues = cacheValues;
		this.defaultValue = defaultValue;
		this.data = data;
		this.table = table;
		this.keyColumn = keyColumn;
		this.valueColumn = valueColumn;
		this.clearRate = clearRate;
		checkDatabase();
		updateTimer();
	}

	@CheckReturnValue
	public SQLValueCache(@Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, @Nonnull String defaultValue) {
		this(true, defaultValue, data, table, keyColumn, valueColumn, DEFAULT_CLEAR_RATE);
	}

	private synchronized void updateTimer() {
		if (timer != null) timer.cancel();
		if (clearRate <= 0) {
			timer = null;
			return;
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				cache.clear();
			}
		}, 0, clearRate * 1000);
	}

	private void checkDatabase() {
		try {
			data.update("CREATE TABLE IF NOT EXISTS " + table + " (" + keyColumn + " VARCHAR(1000), " + valueColumn + " VARCHAR(9999))");
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * @throws IllegalStateException If not {@link #shouldCacheValues()}
	 * @see #set(String, String)
	 */
	public synchronized void setCached(@Nonnull String key, String value) {
		if (!cacheValues) throw new IllegalStateException("Cannot cache values if disabled");
		if (value == null) {
			cache.remove(key);
		} else {
			cache.put(key, value);
		}
	}

	/**
	 * @param key The key the value is assigned to. <code>null</code> when there is no value
	 * @throws IllegalStateException If not {@link #shouldCacheValues()}
	 */
	public String getCached(@Nonnull String key) {
		if (!cacheValues) throw new IllegalStateException("Cannot read cached values if disabled");
		return cache.get(key);
	}

	/**
	 * @throws SQLException If a database error occurs
	 * @throws IllegalStateException If the database does not contain the given key
	 */
	public String getFromDatabase(String key) throws SQLException {
		ResultSet result = data.query("SELECT " + valueColumn + " FROM " + table + " WHERE " + keyColumn + " = ?", key);
		if (!result.next()) throw new IllegalStateException("Database does not contain the given key");
		String value = result.getString(valueColumn);
		result.close();;
		return value;
	}

	/**
	 * Reads the prefix from the database by the given. If no prefix was found it will return the {@link #getDefaultValue()}
	 * If {@link #shouldCacheValues()}, it'll cache the value as well (using {@link #setCached(String, String)})
	 * @return The loaded value
	 */
	@Nonnull
	public synchronized String load(String key) {

		String value = null;

		try {
			value = getFromDatabase(key);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IllegalStateException ignored) { }

		if (value == null) value = defaultValue;

		if (cacheValues) {
			setCached(key, value);
		}

		return value;

	}

	/**
	 * @see #get(String)
	 */
	@Nonnull
	@CheckReturnValue
	public String getValue(@Nonnull String key) {

		if (cacheValues) {
			String cached = getCached(key);
			if (cached != null) {
				return cached;
			}
		}

		return load(key);

	}

	/**
	 * Loads the value using {@link #load(String)}, by the given key
	 * If the guild is <code>null</code>, it will return the {@link #getDefaultValue()}
	 * @see #getValue(String)
	 * @param key The key to which the searched value should be stored to. If <code>null</code>, it will return {@link #getDefaultValue()}
	 * @return Returns the prefix loaded
	 */
	@Nonnull
	@CheckReturnValue
	public String get(String key) {
		if (key == null) {
			return defaultValue;
		} else {
			return getValue(key);
		}
	}

	public synchronized void set(@Nonnull String key, @Nonnull String value) throws SQLException {
		if (cacheValues) setCached(key, value);
		try {
			getFromDatabase(key); // Checks if the guild is in the database (throws IllegalStateException if not)
			data.update("UPDATE " + table + " SET " + valueColumn + " = ? WHERE " + keyColumn + " = ?", value, key);
		} catch (IllegalStateException ignored) {
			data.update("INSERT INTO " + table + " (" + keyColumn + ", " + valueColumn + ") VALUES (?, ?)", key, value);
		}
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Sets if values are saved in the ram when they were loaded from the database once
	 * @see #shouldCacheValues()
	 */
	public synchronized void setCacheValues(boolean cacheValues) {
		if (cacheValues != this.cacheValues) resetCache();
		this.cacheValues = cacheValues;
	}

	public synchronized void setClearRate(int clearRate) {
		this.clearRate = clearRate;
		updateTimer();
	}

	public synchronized void resetCache() {
		cache.clear();
	}

	public int getClearRate() {
		return clearRate;
	}

	@Nonnull
	@CheckReturnValue
	public String getDefaultValue() {
		return defaultValue;
	}

	@Nonnull
	@CheckReturnValue
	public SQL getDataSource() {
		return data;
	}

	@Nonnull
	@CheckReturnValue
	public String getKeyColumn() {
		return keyColumn;
	}

	@Nonnull
	@CheckReturnValue
	public String getValueColumn() {
		return valueColumn;
	}

	@Nonnull
	@CheckReturnValue
	public String getTable() {
		return table;
	}

	@CheckReturnValue
	public boolean shouldCacheValues() {
		return cacheValues;
	}

	@Nonnull
	@CheckReturnValue
	public Map<String, String> getCache() {
		return cache;
	}
}

