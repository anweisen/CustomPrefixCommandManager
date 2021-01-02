package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.log.LogHelper;
import net.codingarea.engine.utils.log.LogLevel;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.OffsetDateTime;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public abstract class SQLHelper {

	public static void update(@Nonnull SQL sql, @Nonnull String table, @Nonnull String column, @Nullable Object value,
	                          @Nonnull String key, @Nonnull Object keyValue) throws SQLException {

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

	@CheckReturnValue
	public static int sqlIndex(int index) {
		return index <= 0 ? 1 : index;
	}

	@Nonnull
	@CheckReturnValue
	public static CachedRowSet select(@Nonnull SQL sql, @Nonnull String table, @Nonnull String selection) throws SQLException {
		return sql.query().table(table).select(selection).execute();
	}

	@Nonnull
	@CheckReturnValue
	public static CachedRowSet select(@Nonnull SQL sql, @Nonnull String table, @Nonnull String selection,
	                                  @Nonnull String key, @Nonnull Object keyValue) throws SQLException {
		return sql.query().table(table).select(selection).where(key, keyValue).execute();
	}


	@CheckReturnValue
	public static int getIntAndClose(@Nonnull ResultSet result, @Nonnull String column) throws SQLException {
		if (result.isBeforeFirst() || result.isAfterLast()) {
			if (!result.next()) {
				result.close();
				return 0;
			}
		}
		int value = result.getInt(column);
		result.close();
		return value;
	}
	@CheckReturnValue
	public static int getIntAndClose(@Nonnull ResultSet result, int column) throws SQLException {
		if (result.isBeforeFirst() || result.isAfterLast()) {
			if (!result.next()) {
				result.close();
				return 0;
			}
		}
		int value = result.getInt(sqlIndex(column));
		result.close();
		return value;
	}

	@CheckReturnValue
	public static long getLongAndClose(@Nonnull ResultSet result, @Nonnull String column) throws SQLException {
		if (result.isBeforeFirst() || result.isAfterLast()) {
			if (!result.next()) {
				result.close();
				return 0;
			}
		}
		long value = result.getLong(column);
		result.close();
		return value;
	}
	@CheckReturnValue
	public static long getLongAndClose(@Nonnull ResultSet result, int column) throws SQLException {
		if (result.isBeforeFirst() || result.isAfterLast()) {
			if (!result.next()) {
				result.close();
				return 0;
			}
		}
		long value = result.getLong(sqlIndex(column));
		result.close();
		return value;
	}

	@CheckReturnValue
	public static String getStringAndClose(@Nonnull ResultSet result, int column) throws SQLException {
		if (result.isBeforeFirst() || result.isAfterLast()) {
			if (!result.next()) {
				result.close();
				return null;
			}
		}
		String value = result.getString(sqlIndex(column));
		result.close();
		return value;
	}
	@CheckReturnValue
	public static String getStringAndClose(@Nonnull ResultSet result, @Nonnull String column) throws SQLException {
		if (result.isBeforeFirst() || result.isAfterLast()) {
			if (!result.next()) {
				result.close();
				return null;
			}
		}
		String value = result.getString(column);
		result.close();
		return value;
	}


	@Nonnull
	@CheckReturnValue
	public static <T> T getPrimitive(@Nonnull ResultSet result, @Nonnull String column, @Nonnull Class<T> type) throws SQLException {
		if (type == long.class || type == Long.class) {
			return (T) Long.valueOf(result.getLong(column));
		} else if (type == int.class || type == Integer.class) {
			return (T) Integer.valueOf(result.getInt(column));
		}  else if (type == short.class || type == Short.class) {
			return (T) Short.valueOf(result.getShort(column));
		} else if (type == byte.class || type == Byte.class) {
			return (T) Byte.valueOf(result.getByte(column));
		} else if (type == float.class || type == Float.class) {
			return (T) Float.valueOf(result.getFloat(column));
		} else if (type == double.class || type == Double.class || type == Number.class) {
			return (T) Double.valueOf(result.getDouble(column));
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
	public static <T> T get(@Nonnull ResultSet result, @Nonnull String column, @Nonnull Class<T> type) throws SQLException {
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
		} catch (SQLException ex) {
			throw ex;
		} catch (Exception ignored) {
			return null;
		}
	}

	public static void logResult(@Nonnull ResultSet result) throws SQLException {
		logResult(result, r -> LogHelper.log(LogLevel.DEBUG, SQLHelper.class, r));
	}

	public static void logResult(@Nonnull ResultSet result, @Nonnull Consumer<? super String> action) throws SQLException {
		if (result.isLast()) {
			action.accept("<Empty ResultSet>");
		} else {
			ResultSetMetaData data = result.getMetaData();
			while (result.next()) {
				action.accept(currentRowToString(result, data));
			}
		}
	}

	@Nonnull
	@CheckReturnValue
	public static String currentRowToString(@Nonnull ResultSet result, @Nonnull ResultSetMetaData data) throws SQLException {
		StringBuilder builder = new StringBuilder();
		builder.append(result.getRow() + " | ");
		for (int i = 1; i <= data.getColumnCount(); i++) {
			Object object = result.getObject(i);
			builder.append(data.getColumnName(i) + "='" + object + "' ");
		}
		return builder.toString();
	}

	public static void handleSQLException(@Nonnull SQLException ex) {
		LogHelper.error("A SQLException occurred: \"" + ex.getMessage() + "\"" +
						"\tCurrent state: \"" + ex.getSQLState() + "\"" +
						"\tError code: \"" + ex.getErrorCode() + "\"");
	}

	public static boolean isClosed(@Nonnull ResultSet result) throws SQLException {
		try {
			return result.isClosed();
		} catch (SQLFeatureNotSupportedException ex) {
			if (result instanceof CachedRowSet) {
				try {
					return isClosed(((CachedRowSet) result).getOriginalRow());
				} catch (Exception ex2) { }
			}
			return false;
		}
	}

}
