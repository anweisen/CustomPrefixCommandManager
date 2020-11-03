package net.codingarea.engine.sql.helper;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public interface PreparedAccess {

	@Nonnull
	@CheckReturnValue
	PreparedStatement prepare() throws SQLException;

	void execute0() throws SQLException;

	@Nonnull
	PreparedAccess table(@Nonnull String table);

}
