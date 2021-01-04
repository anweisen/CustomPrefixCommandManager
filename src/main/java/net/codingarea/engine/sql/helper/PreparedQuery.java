package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.factory.ListFactory;
import net.codingarea.engine.utils.function.ThrowingFunction;
import net.codingarea.engine.utils.function.ThrowingPredicate;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

/**
 * Util class for VERY VERY VERY simple sql access.
 * If you are advanced you should NOT use this class!
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public final class PreparedQuery extends AbstractPreparedAccess {

	private String[] with = null;
	private String[] select = {"*"};
	private String[] as = null;
	private Order order;
	private String orderBy;
	private String group;
	private Integer limit = null;
	private boolean distinct = false;

	@CheckReturnValue
	public PreparedQuery(@Nonnull SQL sql) {
		super(sql);
	}

	/**
	 * @param select The columns which should be selected
	 * @return {@code this} for chaining
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery select(@Nonnull String... select) {
		this.select = select;
		return this;
	}

	/**
	 * @param column The column whose entries should be counted
	 * @return The amount of rows the table has matching the where requirements.
	 *         The value cannot be greater than the {@link #getLimit() limit}
	 *
	 * @throws SQLException
	 *         If a database error occurs
	 */
	@CheckReturnValue
	public int count(@Nonnull String column, boolean distinct) throws SQLException {
		PreparedQuery query = this.clone()
				.select("count(" + (distinct ? "DISTINCT " : "") + column + ") count")
				.resetAs().resetGrouping().resetLimit().distinct(false);
		CachedRowSet result = query.execute();
		int count = result.getInt("count");
		result.close();
		return count;
	}

	/**
	 * @param column The column whose entries should be counted
	 * @return The amount of rows the table has matching the where requirements.
	 *         The value cannot be greater than the {@link #getLimit() limit}
	 *
	 * @throws SQLException
	 *         If a database error occurs
	 */
	@CheckReturnValue
	public int count(@Nonnull String column) throws SQLException {
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
	@CheckReturnValue
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
	@CheckReturnValue
	public PreparedQuery where(final @Nonnull String key, final @Nonnull Object value) {
		return (PreparedQuery) super.where(key, value);
	}

	/**
	 * @see AbstractPreparedAccess#where(Collection)
	 * @see AbstractPreparedAccess#where(String, Object, String)
	 */
	@Nonnull
	@Override
	@CheckReturnValue
	public PreparedQuery where(final @Nonnull Collection<? extends Where> where) {
		return (PreparedQuery) super.where(where);
	}

	/**
	 * @see AbstractPreparedAccess#removeWhere()
	 */
	@Nonnull
	@Override
	@CheckReturnValue
	public PreparedQuery removeWhere() {
		return (PreparedQuery) super.removeWhere();
	}

	/**
	 * @apiNote key word {@code ORDER BY %column% %order%}
	 * @param column The column the result should be ordered by
	 * @param order The order the result should have
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery order(final @Nonnull String column, final @Nullable Order order) {
		this.orderBy = column;
		this.order = order;
		return this;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedQuery removeOrder() {
		this.orderBy = null;
		this.order = null;
		return this;
	}

	/**
	 * @apiNote key word {@code ORDER BY %column%}
	 * @param column The column the result should be ordered by
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery order(final @Nonnull String column) {
		this.orderBy = column;
		return this;
	}

	/**
	 * @param table The table from where the data should be accessed
	 *
	 * @see AbstractPreparedAccess#table(String)
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery table(@Nonnull String table) {
		return (PreparedQuery) super.table(table);
	}

	/**
	 * @apiNote key word {@code LIMIT %limit%}
	 * @param limit Sets the limit of rows to be selected
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery limit(int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * @return {@code this}
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery resetLimit() {
		this.limit = null;
		return this;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedQuery with(@Nonnull String... with) {
		this.with = with;
		return this;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedQuery resetWith() {
		this.with = null;
		return this;
	}

	/**
	 * @apiNote key word {@code DISTINCT}
	 * @param distinct {@code true} if the result should return different values
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery distinct(final boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	/**
	 * @apiNote key word {@code GROUP BY %column%}
	 * @param group The column the result should be grouped by
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery group(final @Nonnull String group) {
		this.group = group;
		return this;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedQuery resetGrouping() {
		this.group = null;
		return this;
	}

	/**
	 * @apiNote key word {@code AS}
	 * @param as The column names the result columns should have
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedQuery as(final @Nonnull String... as) {
		this.as = as;
		return this;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedQuery resetAs() {
		this.as = null;
		return this;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public PreparedStatement prepare() throws SQLException {

		if (table == null)
			throw new IllegalArgumentException("Table cannot be null");
		if (select.length == 0)
			throw new IllegalArgumentException("Cannot select noting");
		if (as != null && as.length != select.length)
			throw new IllegalArgumentException("Length of 'as' cannot be different as length of 'select'");

		StringBuilder query = new StringBuilder();
		
		if (with != null && with.length > 0) {
			query.append("WITH ");

			for (int i = 0; i < with.length; i++) {
				String current = with[i];
				if (i != 0)
					query.append(",");
				query.append(current + " ");
			}

		}

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
	public <T> Optional<T> first(@Nonnull Function<? super CachedRowSet, T> mapper) throws SQLException {
		CachedRowSet result = this.clone().limit(1).execute();
		if (result.next()) {
			T value = mapper.apply(result);
			if (SQLHelper.isClosed(result)) result.close();
			return Optional.ofNullable(value);
		} else {
			if (SQLHelper.isClosed(result)) result.close();
			return Optional.empty();
		}
	}

	@Nonnull
	@CheckReturnValue
	public <T> Optional<T> first(final @Nonnull ThrowingFunction<? super CachedRowSet, T> mapper) throws SQLException {
		return first((Function<? super CachedRowSet, T>) mapper);
	}

	/**
	 * @param mapper The result is listed and returned
	 * @param <T> The type of the objects to which it should map to
	 * @return A new {@link List} filled with the objects generated by the mapper.
	 *        The maximal size of the {@link List} is {@link #getLimit()} (if not {@code null})
	 *
	 * @throws SQLException
	 *         If a database error occurs
	 */
	@Nonnull
	@CheckReturnValue
	public <T> List<T> list(final @Nonnull Function<? super CachedRowSet, ? extends T> mapper) throws SQLException {
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
		if (SQLHelper.isClosed(result)) result.close();
		return list;
	}

	@Nonnull
	@CheckReturnValue
	public <T> List<T> list(final @Nonnull ThrowingFunction<? super CachedRowSet, ? extends T> mapper) throws SQLException {
		return list((Function<? super CachedRowSet, ? extends T>) mapper);
	}

	@Nonnull
	@CheckReturnValue
	public <T> Stream<T> stream(final @Nonnull Function<? super CachedRowSet, ? extends T> mapper) throws SQLException {
		return this.<T>list(mapper).stream();
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

	@CheckReturnValue
	public int count(@Nonnull ToIntFunction<? super CachedRowSet> counter) throws SQLException {

		CachedRowSet result = execute();
		int value = 0;
		while (result.next()) {
			value += counter.applyAsInt(result);
		}
		return value;

	}

	@CheckReturnValue
	public int position(@Nonnull Predicate<? super CachedRowSet> filter) throws SQLException {

		int pos;
		CachedRowSet result = execute();
		for (pos = 1; result.next(); pos++) {
			if (filter.test(result))
				return pos;
		}

		return pos;

	}

	@CheckReturnValue
	public int position(@Nonnull ThrowingPredicate<? super CachedRowSet> filter) throws SQLException {
		return position((Predicate<? super CachedRowSet>) filter);
	}

	@CheckReturnValue
	public <T> int position(@Nonnull Function<? super CachedRowSet, ? extends T> mapper,
	                        @Nonnull Predicate<? super T> filter) throws SQLException {

		int pos;
		CachedRowSet result = execute();
		for (pos = 1; result.next(); pos++) {
			try {

				T current = mapper.apply(result);
				if (filter.test(current))
					return pos;

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return pos;

	}

	@CheckReturnValue
	public <T> int position(final @Nonnull ThrowingFunction<? super CachedRowSet, ? extends T> mapper,
	                        final @Nonnull ThrowingPredicate<? super T> filter) throws SQLException {
		return position((Function<? super CachedRowSet, ? extends T>) mapper, (Predicate<? super T>) filter);
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
	public PreparedQuery apply(@Nullable Consumer<? super PreparedQuery> action) {
		if (action != null)
			action.accept(this);
		return this;
	}

	public void print() throws SQLException {
		SQLHelper.logResult(execute());
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

	@Override
	public PreparedQuery clone() {
		PreparedQuery clone = new PreparedQuery(sql);
		clone.where(where.values());
		clone.select(select);
		clone.distinct(distinct);
		if (table != null) clone.table(table);
		if (as != null) clone.as(as);
		if (orderBy != null) clone.order(orderBy, order);
		if (limit != null) clone.limit(limit);
		return clone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PreparedQuery query = (PreparedQuery) o;
		return distinct == query.distinct &&
				Arrays.equals(select, query.select) &&
				Arrays.equals(as, query.as) &&
				order == query.order &&
				Objects.equals(orderBy, query.orderBy) &&
				Objects.equals(group, query.group) &&
				Objects.equals(limit, query.limit);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(order, orderBy, group, limit, distinct);
		result = 31 * result + Arrays.hashCode(select);
		result = 31 * result + Arrays.hashCode(as);
		return result;
	}

}
