package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

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
		return sql.query("SELECT " + selection + " FROM " + table);
	}

	@Nonnull
	@CheckReturnValue
	public static CachedRowSet select(final @Nonnull SQL sql, final @Nonnull String table, final @Nonnull String selection,
	                                  final @Nonnull String key, final @Nonnull Object keyValue) throws SQLException {
		return sql.query("SELECT " + selection + " FROM " + table + " WHERE " + key + " = ?", keyValue);
	}

}
