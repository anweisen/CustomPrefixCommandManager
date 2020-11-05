package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public abstract class AbstractPreparedAccess implements PreparedAccess {

	protected final SQL sql;
	protected final Map<String, Where> where = new HashMap<>();
	protected String table;

	public AbstractPreparedAccess(final @Nonnull SQL sql) {
		this.sql = sql;
	}

	/**
	 * @param table The table from where the data should be accessed
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public AbstractPreparedAccess table(final @Nonnull String table) {
		this.table = table;
		return this;
	}

	/**
	 * @param key The key to which the value should be assigned
	 * @param value The value the key should have
	 * @param operator Which operator sql should use to compare objects
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public AbstractPreparedAccess where(final @Nonnull String key, final @Nonnull Object value, final @Nonnull String operator) {
		where.put(key, new Where(operator, value, key));
		return this;
	}

	/**
	 * @param key {@link Where#DEFAULT_OPERATOR}
	 * @param value The value the key should have
	 * @return {@code this} for chaining
	 *
	 * @see #where(String, Object, String)
	 */
	@Nonnull
	public AbstractPreparedAccess where(final @Nonnull String key, final @Nonnull Object value) {
		return where(key, value, Where.DEFAULT_OPERATOR);
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public AbstractPreparedAccess where(final @Nonnull Collection<? extends Where> where) {
		for (Where entry : where) {
			where(entry.getColumn(), entry.getValue(), entry.getOperator());
		}
		return this;
	}

	protected StringBuilder whereAsString() {

		StringBuilder builder = new StringBuilder();
		if (!where.isEmpty()) {

			builder.append(" WHERE ");

			int argument = 0;
			for (Where where : this.where.values()) {
				if (argument != 0)
					builder.append(" AND ");
				builder.append(where.getColumn() + " " + where.getOperator() + " ?");
				argument++;
			}

		}

		return builder;

	}

	protected Object[] args(final @Nonnull Collection<?>... collections) {
		List<Object> list = new ArrayList<>();
		Arrays.stream(collections).forEach(list::addAll);
		return list.toArray();
	}

}
