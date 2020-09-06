package net.anweisen.commandmanager.sql;

import net.anweisen.commandmanager.defaults.DefaultLogger;
import net.anweisen.commandmanager.sql.source.DataSource;
import net.anweisen.commandmanager.utils.Bindable;
import net.anweisen.commandmanager.utils.LogLevel;

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
public abstract class SQL implements Bindable {

	@Nonnull
	public static String removeInjectionPossibility(@Nonnull String string) {
		return string.replaceAll("[']", "\\\\'").replaceAll("[`]", "\\\\`");
	}

	@Nonnull
	public static CachedRowSet cache(ResultSet result) throws SQLException {
		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(result);
		return cachedRowSet;
	}

	public static void fillParams(PreparedStatement statement, Object... params) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			statement.setObject(i+1, params[i]);
		}
	}

	protected final DataSource dataSource;
	protected Connection connection;
	protected Logger logger = new DefaultLogger(this);

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
		if (logger != null) logger.log(LogLevel.STATUS, "Connection to database successfully created");
	}

	public void disconnect() throws SQLException {
		connection.close();
		if (logger != null) logger.log(LogLevel.STATUS, "Connection to database closed");
	}

	public void verifyConnection() throws SQLException {
		if (!connectionIsOpened()) {
			connect();
		}
	}

	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	@Nonnull
	public CachedRowSet executeQuery(@Nonnull String sql) throws SQLException {
		verifyConnection();
		Statement statement = createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		CachedRowSet cachedRowSet = cache(resultSet);
		statement.close();
		return cachedRowSet;
	}

	public void executeUpdate(@Nonnull String sql) throws SQLException {
		verifyConnection();
		Statement statement = createStatement();
		statement.executeUpdate(sql);
		statement.close();
	}

	public PreparedStatement prepare(@Nonnull String sql) throws SQLException {
		verifyConnection();
		return connection.prepareStatement(sql);
	}

	public PreparedStatement prepare(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql);
		fillParams(statement, params);
		return statement;
	}

	public CachedRowSet query(String sql, @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql, params);
		ResultSet resultSet = statement.executeQuery();
		CachedRowSet cachedRowSet = cache(resultSet);
		statement.close();
		return cachedRowSet;
	}

	public int update(String sql, @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql, params);
		int result = statement.executeUpdate();
		statement.close();
		return result;
	}

	public Connection getConnection() {
		return connection;
	}

	@Nonnull
	public DataSource getDataSource() {
		return dataSource;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
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
