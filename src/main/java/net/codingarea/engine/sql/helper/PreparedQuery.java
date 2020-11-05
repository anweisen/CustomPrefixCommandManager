package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.ListFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public final class PreparedQuery extends AbstractPreparedAccess {

	private String[] select = {"*"};
	private String[] as = null;
	private Order order;
	private String orderBy;
	private String group;
	private Integer limit = null;
	private boolean distinct = false;

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
	 * @param column The column which entries should be counted
	 * @param distinct {@code true} if only different values should be counted
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery count(final @Nonnull String column, final boolean distinct) {
		this.select = new String[]{"count(" + (distinct ? "DISTINCT " : "") + column + ") count"};
		return this;
	}

	/**
	 * @param column The column which entries should be counted
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery count(final @Nonnull String column) {
		return count(column, false);
	}

	/**
	 * @param key The key to which the value should be assigned
	 * @param value The value the key should have
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#where(String, Object, String)
	 */
	@Nonnull
	public PreparedQuery where(final @Nonnull String key, final @Nonnull Object value, final @Nonnull String operator) {
		return (PreparedQuery) super.where(key, value, operator);
	}

	/**
	 * @param key {@link Where#DEFAULT_OPERATOR}
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
	 * @see AbstractPreparedAccess#where(Collection)
	 * @see AbstractPreparedAccess#where(String, Object, String)
	 */
	@Nonnull
	@Override
	public PreparedQuery where(final @Nonnull Collection<? extends Where> where) {
		return (PreparedQuery) super.where(where);
	}

	/**
	 * @apiNote key word {@code ORDER BY %column% %order%}
	 * @param column The column the result should be ordered by
	 * @param order The order the result should have
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery order(final @Nonnull String column, final @Nullable Order order) {
		this.orderBy = column;
		this.order = order;
		return this;
	}

	/**
	 * @apiNote key word {@code ORDER BY %column%}
	 * @param column The column the result should be ordered by
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery order(final @Nonnull String column) {
		this.orderBy = column;
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

	/**
	 * @apiNote key word {@code LIMIT %limit%}
	 * @param limit Sets the limit of rows to be selected
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery limit(final int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * @apiNote key word {@code DISTINCT}
	 * @param distinct {@code true} if the result should return different values
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery distinct(final boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	/**
	 * @apiNote key word {@code GROUP BY %column%}
	 * @param group The column the result should be grouped by
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery group(final @Nonnull String group) {
		this.group = group;
		return this;
	}

	/**
	 * @apiNote key word {@code AS}
	 * @param as The column names the result columns should have
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedQuery as(final @Nonnull String... as) {
		this.as = as;
		return this;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public PreparedStatement prepare() throws SQLException {

		if (select.length == 0)
			throw new IllegalArgumentException("Cannot select noting");
		if (table == null)
			throw new IllegalArgumentException("Table cannot be null");
		if (as != null && as.length != select.length)
			throw new IllegalArgumentException("Length of 'as' cannot be different as length of 'select'");

		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		if (distinct)
			query.append(" DISTINCT ");

		for (int i = 0; i < select.length; i++) {
			String current = select[i];
			if (i != 0)
				query.append(", ");
			query.append(current);
		}

		if (as != null) {
			query.append(" AS ");
			for (int i = 0; i < as.length; i++) {
				if (i != 0)
					query.append(", ");
				query.append(as[i]);
			}
		}

		query.append(" FROM ");
		query.append(table);
		query.append(super.whereAsString());

		if (group != null) {
			query.append(" GROUP BY ");
			query.append(group);
		}

		if (orderBy != null) {
			query.append(" ORDER BY " + orderBy);
			if (order != null)
				query.append(" " + order.name());
		}

		if (limit != null) {
			query.append(" LIMIT ");
			query.append(limit);
		}

		String finalQuery = query.toString();
		return sql.prepare(finalQuery, ListFactory.list(Where::getValue, where.values()).toArray());

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
