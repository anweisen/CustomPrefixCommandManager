package net.anweisen.commandmanager.sql;

import net.anweisen.commandmanager.sql.source.DataSource;

import javax.annotation.Nonnull;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.logging.Logger;

/**
 * Developed in the CommandManager project
 * on 08-31-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class SQL {

	@Nonnull
	public static String removeInjectionPossibility(@Nonnull String string) {
		return string.replaceAll("[']", "\\\\'").replaceAll("[`]", "\\\\`");
	}

	protected final DataSource dataSource;
	protected Connection connection;
	protected Logger logger = Logger.getGlobal();

	public SQL(@Nonnull DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public SQL(@Nonnull DataSource dataSource, @Nonnull Logger logger) {
		this.dataSource = dataSource;
		this.logger = logger;
	}

	public boolean connectionIsOpened() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException ignored) {
			return false;
		}
	}

	public void connect() throws SQLException {
		if (connectionIsOpened()) {
			disconnect();
		}
		connection = dataSource.createConnection();
		logger.info("Connection to database created");
	}

	public void disconnect() throws SQLException {
		connection.close();
		logger.info("Connection to database closed");
	}

	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	@Nonnull
	public CachedRowSet executeQuery(@Nonnull String sql) throws SQLException {
		if (!connectionIsOpened()) connect();
		Statement statement = createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(resultSet);
		statement.close();
		return cachedRowSet;
	}

	public void executeUpdate(@Nonnull String sql) throws SQLException {
		if (!connectionIsOpened()) connect();
		Statement statement = createStatement();
		statement.executeUpdate(sql);
		statement.close();
	}

	public PreparedStatement prepareStatement(@Nonnull String sql) throws SQLException {
		if (!connectionIsOpened()) connect();
		return connection.prepareStatement(sql);
	}

	public Connection getConnection() {
		return connection;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(@Nonnull Logger logger) {
		this.logger = logger;
	}

	@Override
	public String toString() {
		return "SQL{" +
				"dataSource=" + dataSource +
				", connection=" + connection +
				'}';
	}
}
