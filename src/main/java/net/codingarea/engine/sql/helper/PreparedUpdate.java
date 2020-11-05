package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.ListFactory;
import net.codingarea.engine.utils.MapFactory;

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
public final class PreparedUpdate extends AbstractPreparedAccess {

	private final Map<String, Object> set = new HashMap<>();

	@CheckReturnValue
	public PreparedUpdate(final @Nonnull SQL sql) {
		super(sql);
	}

	/**
	 * @param table Sets the table where the values should be updated
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#table(String)
	 */
	@Nonnull
	@Override
	public PreparedUpdate table(final @Nonnull String table) {
		return (PreparedUpdate) super.table(table);
	}

	/**
	 * @param key The key to which the value should be assigned
	 * @param value The value the key should have
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#where(String, Object)
	 */
	@Nonnull
	public PreparedUpdate where(final @Nonnull String key, final @Nonnull Object value) {
		return (PreparedUpdate) super.where(key, value);
	}

	/**
	 * @param key {@link Where#DEFAULT_OPERATOR}
	 * @param value The value the key should have
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#where(String, Object)
	 */
	@Nonnull
	public PreparedUpdate where(final @Nonnull String key, final @Nonnull Object value, final @Nonnull String operator) {
		return (PreparedUpdate) super.where(key, value, operator);
	}

	@Nonnull
	@Override
	public PreparedUpdate where(@Nonnull Collection<? extends Where> where) {
		return (PreparedUpdate) super.where(where);
	}

	/**
	 * @param key The key which the value should be assigned to
	 * @param value The value which the key should be set to
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedUpdate set(final @Nonnull String key, final @Nullable Object value) {
		set.put(key, value);
		return this;
	}

	/**
	 * @return {@code this} for chaining
	 */
	@Nonnull
	public PreparedUpdate set(final @Nonnull Map<String, Object> set) {
		for (Entry<String, Object> entry : set.entrySet()) {
			this.set.put(entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedInsertion toInsertion() {
		return new PreparedInsertion(sql).table(table).insert(MapFactory.map(new HashMap<>(), where, key -> key, Where::getValue)).insert(set);
	}

	@Nonnull
	@Override
	public PreparedStatement prepare() throws SQLException {

		if (set.size() == 0)
			throw new IllegalArgumentException("Cannot update nothing");
		if (table == null)
			throw new IllegalArgumentException("Table cannot be null");

		StringBuilder update = new StringBuilder();
		update.append("UPDATE ");
		update.append(table);
		update.append(" SET ");

		int argument = 0;
		for (Entry<String, Object> entry : set.entrySet()) {
			if (argument != 0)
				update.append(", ");
			update.append(entry.getKey() + " = ?");
			argument++;
		}

		update.append(super.whereAsString());

		String finalUpdate = update.toString();
		return sql.prepare(finalUpdate, ListFactory.toArray(set.values(), ListFactory.list(Where::getValue, where.values())));

	}

	public int execute() throws SQLException {
		PreparedStatement statement = prepare();
		return sql.executeUpdate(statement);
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
