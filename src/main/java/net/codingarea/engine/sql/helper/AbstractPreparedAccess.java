package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public abstract class AbstractPreparedAccess implements PreparedAccess {

	protected final SQL sql;
	protected final Map<String, Object> where = new HashMap<>();
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
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public AbstractPreparedAccess where(final @Nonnull String key, final @Nonnull Object value) {
		where.put(key, value);
		return this;
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public AbstractPreparedAccess where(final @Nonnull Map<String, Object> where) {
		for (Entry<String, Object> entry : where.entrySet()) {
			this.where.put(entry.getKey(), entry.getValue());
		}
		return this;
	}

	protected StringBuilder whereAsString() {

		StringBuilder builder = new StringBuilder();
		if (!where.isEmpty()) {

			builder.append(" WHERE ");

			int argument = 0;
			for (Entry<String, Object> entry : where.entrySet()) {
				if (argument != 0)
					builder.append(" AND ");
				builder.append(entry.getKey() + " = ?");
				argument++;
			}

		}

		return builder;

	}

}
