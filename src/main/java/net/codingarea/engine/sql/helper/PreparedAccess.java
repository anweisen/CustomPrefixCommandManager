package net.codingarea.engine.sql.helper;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Util class for VERY VERY VERY simple sql access.
 * If you are advanced you should NOT use this class!
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
public interface PreparedAccess extends Cloneable {

	@Nonnull
	@CheckReturnValue
	PreparedStatement prepare() throws SQLException;

	void execute0() throws SQLException;

	@Nonnull
	PreparedAccess table(@Nonnull String table);

}
