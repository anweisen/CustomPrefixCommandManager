package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public final class PreparedQuery extends AbstractPreparedAccess {

	private String[] select = {"*"};
	private Order order;
	private String orderBy;

	@CheckReturnValue
	public PreparedQuery(final @Nonnull SQL sql) {
		super(sql);
	}

	/**
	 * @param select The columns which should be selected
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery select(final @Nonnull String... select) {
		this.select = select;
		return this;
	}

	/**
	 * @param key The key to which the value should be assigned
	 * @param value The value the key should have
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#where(String, Object)
	 */
	@Nonnull
	public PreparedQuery where(final @Nonnull String key, final @Nonnull Object value) {
		return (PreparedQuery) super.where(key, value);
	}

	/**
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#where(Map)
	 */
	@Nonnull
	@Override
	public PreparedQuery where(@Nonnull Map<String, Object> where) {
		return (PreparedQuery) super.where(where);
	}

	/**
	 * @param key The column the result should be ordered by
	 * @param order The order the result should have
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery order(final @Nonnull String key, final @Nullable Order order) {
		this.orderBy = key;
		this.order = order;
		return this;
	}

	/**
	 * @param key The column the result should be ordered by
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery order(final @Nonnull String key) {
		this.orderBy = key;
		return this;
	}

	/**
	 * @param table The table from where the data should be accessed
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#table(String)
	 */
	@Nonnull
	public PreparedQuery table(final @Nonnull String table) {
		return (PreparedQuery) super.table(table);
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public PreparedStatement prepare() throws SQLException {

		if (select.length == 0)
			throw new IllegalArgumentException("Cannot select noting");
		if (table == null)
			throw new IllegalArgumentException("Table cannot be null");

		StringBuilder query = new StringBuilder();
		query.append("SELECT ");

		for (int i = 0; i < select.length; i++) {
			String current = select[i];
			if (i != 0)
				query.append(", ");
			query.append(current);
		}

		query.append(" FROM ");
		query.append(table);
		query.append(super.whereAsString());

		if (orderBy != null) {
			query.append(" ORDER BY " + orderBy);
			if (order != null)
				query.append(" " + order.name());
		}

		String finalQuery = query.toString();
		return sql.prepare(finalQuery, where.values().toArray());

	}

	/**
	 * @return The {@link java.sql.ResultSet} returned by the server packed into a {@link CachedRowSet}
	 *
	 * @throws SQLException
	 *         If a database error occurs
	 *
	 * @see #prepare()
	 * @see SQL#query(String, Object...)
	 * @see SQL#executeQuery(PreparedStatement)
	 */
	@Nonnull
	public CachedRowSet execute() throws SQLException {
		PreparedStatement statement = prepare();
		return sql.executeQuery(statement);
	}

	/**
	 * @return The value of {@link SQL#isSet(ResultSet)} using the {@link #execute()} method
	 *
	 * @throws SQLException
	 *         If a database error occurs
	 *
	 * @see #execute()
	 * @see SQL#isSet(ResultSet)
	 */
	@CheckReturnValue
	public boolean isSet() throws SQLException {
		return sql.isSet(execute());
	}

	/**
	 * @throws SQLException
	 *         If a database error occurs
	 *
	 * @see #execute()
	 */
	@Override
	public void execute0() throws SQLException {
		execute();
	}

}
