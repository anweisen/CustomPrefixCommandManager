package net.codingarea.engine.sql.helper;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.ListFactory;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

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
	public PreparedDeletion table(@Nonnull String table) {
		return (PreparedDeletion) super.table(table);
	}

	@Nonnull
	@Override
	public PreparedDeletion where(@Nonnull String key, @Nonnull Object value) {
		return (PreparedDeletion) super.where(key, value);
	}

	@Nonnull
	@Override
	public PreparedDeletion where(@Nonnull Map<String, Object> where) {
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
		return sql.prepare(finalDeletion, ListFactory.toArray(where.values()));

	}

	public int execute() throws SQLException {
		PreparedStatement statement = prepare();
		return sql.executeUpdate(statement);
	}

	@Override
	public void execute0() throws SQLException {

	}

}
