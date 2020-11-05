package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.LogHelper;
import net.codingarea.engine.utils.LogLevel;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public abstract class SQLHelper {

	public static void update(final @Nonnull SQL sql, final @Nonnull String table, final @Nonnull String column, final @Nullable Object value,
	                          final @Nonnull String key, final @Nonnull Object keyValue) throws SQLException {

		if (sql.isSet("SELECT " + column + " FROM " + table + " WHERE " + key + " = " + keyValue)) {
			if (value == null) {
				sql.update("UPDATE " + table + " SET " + column + " = NULL WHERE " + key + " = ?", keyValue);
			} else {
				sql.update("UPDATE " + table + " SET " + column + " = ? WHERE " + key + " = ?", value, keyValue);
			}
		} else {
			sql.update("INSERT INTO " + table + " (" + key + ", " + column + ") VALUES (?, ?)", keyValue, value);
		}

	}

	@Nonnull
	@CheckReturnValue
	public static CachedRowSet select(final @Nonnull SQL sql, final @Nonnull String table, final @Nonnull String selection) throws SQLException {
		return sql.query().table(table).select(selection).execute();
	}

	@Nonnull
	@CheckReturnValue
	public static CachedRowSet select(final @Nonnull SQL sql, final @Nonnull String table, final @Nonnull String selection,
	                                  final @Nonnull String key, final @Nonnull Object keyValue) throws SQLException {
		return sql.query().table(table).select(selection).where(key, keyValue).execute();
	}

	@Nonnull
	@CheckReturnValue
	public static <T> T getPrimitive(final @Nonnull ResultSet result, final @Nonnull String column, final @Nonnull Class<T> type) throws SQLException {
		if (type == long.class || type == Long.class) {
			return (T) Long.valueOf(result.getLong(column));
		} else if (type == int.class || type == Integer.class) {
			return (T) Integer.valueOf(result.getInt(column));
		}  else if (type == short.class || type == Short.class) {
			return (T) Short.valueOf(result.getShort(column));
		} else if (type == byte.class || type == Byte.class) {
			return (T) Byte.valueOf(result.getByte(column));
		} else if (type == char.class || type == Character.class) {
			String string = result.getString(column);
			char c = string.length() > 0 ? string.charAt(0) : ' ';
			return (T) Character.valueOf(c);
		} else if (type == boolean.class || type == Boolean.class) {
			return (T) Boolean.valueOf(result.getBoolean(column));
		} else {
			throw new IllegalArgumentException(type.getName() + " is not a supported type");
		}
	}

	@Nullable
	@CheckReturnValue
	public static <T> T get(final @Nonnull ResultSet result, final @Nonnull String column, final @Nonnull Class<T> type) throws SQLException {
		try {
			if (type.isPrimitive()) {
				return getPrimitive(result, column, type);
			} else if (type == String.class || type == CharSequence.class) {
				return (T) result.getString(column);
			} else if (type == StringBuilder.class) {
				return (T) new StringBuilder(String.valueOf(result.getString(column)));
			} else if (type == char[].class || type == Character[].class) {
				return (T) String.valueOf(result.getString(column)).toCharArray();
			} else if (type == OffsetDateTime.class) {
				return (T) OffsetDateTime.parse(result.getString(column));
			} else {
				throw new IllegalArgumentException(type.getName() + " is not a supported type");
			}
		} catch (Exception ignored) {
			return null;
		}
	}

	public static void logResult(final @Nonnull ResultSet result) throws SQLException {
		logResult(result, r -> LogHelper.log(LogLevel.DEBUG, SQLHelper.class, r));
	}

	public static void logResult(final @Nonnull ResultSet result, final @Nonnull Consumer<? super String> action) throws SQLException {
		if (result.isLast()) {
			action.accept("<Empty ResultSet>");
		} else {
			ResultSetMetaData data = result.getMetaData();
			while (result.next()) {
				action.accept(currentRowToString(result, data));
			}
		}
	}

	public static String currentRowToString(final @Nonnull ResultSet result, final @Nonnull ResultSetMetaData data) throws SQLException {
		StringBuilder builder = new StringBuilder();
		builder.append(result.getRow() + " | ");
		for (int i = 1; i <= data.getColumnCount(); i++) {
			Object object = result.getObject(i);
			builder.append(data.getColumnName(i) + "='" + object + "' ");
		}
		return builder.toString();
	}

}
