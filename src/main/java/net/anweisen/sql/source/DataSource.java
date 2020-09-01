package net.anweisen.sql.source;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Developed in the CommandManager project
 * on 09-01-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface DataSource {

	String MYSQL_URL = "jdbc:mysql://%host/%database",
		   LITESQL_URL = "jdbc:sqlite:";

	@Nonnull
	default Connection createConnection() throws SQLException {
		return DriverManager.getConnection(getURL());
	}

	@Nonnull
	String getURL();

}
