package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.ListFactory;
import net.codingarea.engine.utils.function.ThrowingFunction;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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
	 * @return The {@link java.sql.ResultSet} returned by the server as {@link CachedRowSet}
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

	@Nonnull
	@CheckReturnValue
	public <T> Optional<T> findFirst(final @Nonnull Function<? super CachedRowSet, T> mapper) throws SQLException {
		CachedRowSet result = execute();
		if (result.next()) {
			T value = mapper.apply(result);
			result.close();
			return Optional.ofNullable(value);
		} else {
			result.close();
			return Optional.empty();
		}
	}

	@Nonnull
	@CheckReturnValue
	public <T> Optional<T> findFirst(final @Nonnull ThrowingFunction<? super CachedRowSet, T> mapper) throws SQLException {
		return findFirst((Function<? super CachedRowSet, T>) mapper);
	}

	/**
	 * @param mapper The result is listed and returned
	 * @param <T> The type of the objects to map to
	 * @return A new {@link Collection} filled with the objects generated by the mapper
	 *
	 * @throws SQLException
	 *         If a database error occurs
	 */
	@Nonnull
	@CheckReturnValue
	public <T> Collection<T> findAll(final @Nonnull Function<? super CachedRowSet, ? extends T> mapper) throws SQLException {
		CachedRowSet result = execute();
		List<T> list = new ArrayList<>();
		while (result.next()) {
			try {
				T value = mapper.apply(result);
				if (value != null)
					list.add(value);
			} catch (Exception ignored) {
				/* Ignoring any exceptions while mapping */
			}
		}
		result.close();
		return list;
	}

	@Nonnull
	@CheckReturnValue
	public <T> Collection<T> findAll(final @Nonnull ThrowingFunction<? super CachedRowSet, ? extends T> mapper) throws SQLException {
		return findAll((Function<? super CachedRowSet, ? extends T>) mapper);
	}

	/**
	 * @return The number of the rows the result has
	 *
	 * @throws SQLException
	 *         If a database error occurs
	 */
	@CheckReturnValue
	public int count() throws SQLException {
		CachedRowSet result = execute();
		int size = result.size();
		result.close();
		return size;
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

	@Nonnull
	public PreparedQuery apply(final @Nullable Consumer<? super PreparedQuery> action) {
		if (action != null)
			action.accept(this);
		return this;
	}

	@Nullable
	@CheckReturnValue
	public Integer getLimit() {
		return limit;
	}

	@Nullable
	@CheckReturnValue
	public Order getOrder() {
		return order;
	}

	@Nullable
	@CheckReturnValue
	public String getOrderBy() {
		return orderBy;
	}

	@Nullable
	@CheckReturnValue
	public String getGroupBy() {
		return group;
	}

	@Nullable
	@CheckReturnValue
	public String[] getAs() {
		return as;
	}

	@Nonnull
	@CheckReturnValue
	public String[] getSelection() {
		return select;
	}

}
