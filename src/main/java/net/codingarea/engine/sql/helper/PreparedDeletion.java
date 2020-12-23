package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.factory.ListFactory;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Util class for VERY VERY VERY simple sql access.
 * If you are advanced you should NOT use this class!
 *
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
	public PreparedDeletion where(final @Nonnull String key, final @Nonnull Object value) {
		return (PreparedDeletion) super.where(key, value);
	}

	@Nonnull
	@Override
	public PreparedDeletion where(final @Nonnull Collection<? extends Where> where) {
		return (PreparedDeletion) super.where(where);
	}

	/**
	 * @return {@code this} for chaining
	 *
	 * @see AbstractPreparedAccess#removeWhere()
	 */
	@Nonnull
	@Override
	public PreparedDeletion removeWhere() {
		return (PreparedDeletion) super.removeWhere();
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
		execute();
	}

}
