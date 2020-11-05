package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.ListFactory;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public final class PreparedInsertion extends AbstractPreparedAccess {

	private final Map<String, Object> values = new HashMap<>();

	@CheckReturnValue
	public PreparedInsertion(final @Nonnull SQL sql) {
		super(sql);
	}

	/**
	 * @param key The key which the value should be assigned to
	 * @param value The value which the key should be set to
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedInsertion insert(final @Nonnull String key, final @Nullable Object value) {
		values.put(key, value);
		return this;
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedInsertion insert(final @Nonnull Map<String, Object> values) {
		for (Entry<String, Object> entry : values.entrySet()) {
			this.values.put(entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * @param table The table in which the data should be inserted
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#table(String)
	 */
	@Nonnull
	@Override
	public PreparedInsertion table(@Nonnull String table) {
		return (PreparedInsertion) super.table(table);
	}

	@Nonnull
	@Override
	@Deprecated
	public AbstractPreparedAccess where(final @Nonnull String key, final @Nonnull Object value, final @Nonnull String operator) {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	@Deprecated
	public AbstractPreparedAccess where(final @Nonnull Collection<? extends Where> where) {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public PreparedStatement prepare() throws SQLException {

		if (table == null)
			throw new IllegalArgumentException("Table cannot be null");
		if (values.isEmpty())
			throw new IllegalArgumentException("Cannot insert nothing");

		StringBuilder insertion = new StringBuilder();
		insertion.append("INSERT INTO ");
		insertion.append(table);
		insertion.append(" (");

		int argument = 0;
		for (Entry<String, Object> entry : values.entrySet()) {
			if (argument != 0)
				insertion.append(", ");
			insertion.append(entry.getKey());
			argument++;
		}

		insertion.append(") VALUES (");

		for (int i = 0; i < values.size(); i++) {
			if (i != 0)
				insertion.append(", ");
			insertion.append("?");
		}

		insertion.append(")");

		String finalInsertion = insertion.toString();
		return sql.prepare(finalInsertion, ListFactory.list(Where::getValue, where.values()).toArray());

	}

	public int execute() throws SQLException {
		PreparedStatement statement = prepare();
		return sql.executeUpdate(statement);
	}

	@Override
	public void execute0() throws SQLException {
		execute();
	}

}
