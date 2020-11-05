package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.ListFactory;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public final class PreparedDeletion extends AbstractPreparedAccess {

	public PreparedDeletion(final @Nonnull SQL sql) {
		super(sql);
	}

	@Nonnull
	@Override
	public PreparedDeletion table(final @Nonnull String table) {
		return (PreparedDeletion) super.table(table);
	}

	@Nonnull
	@Override
	public PreparedDeletion where(final @Nonnull String key, final @Nonnull Object value, final @Nonnull String operator) {
		return (PreparedDeletion) super.where(key, value, operator);
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

	@Nonnull
	@Override
	public PreparedDeletion where(final @Nonnull Collection<? extends Where> where) {
		return (PreparedDeletion) super.where(where);
	}

	@Nonnull
	@Override
	public PreparedStatement prepare() throws SQLException {

		StringBuilder deletion = new StringBuilder();
		deletion.append("DELETE FROM ");
		deletion.append(table);
		deletion.append(super.whereAsString());

		String finalDeletion = deletion.toString();
		return sql.prepare(finalDeletion, ListFactory.list(Where::getValue, where.values()).toArray());

	}

	public int execute() throws SQLException {
		PreparedStatement statement = prepare();
		return sql.executeUpdate(statement);
	}

	@Override
	public void execute0() throws SQLException {

	}

}
