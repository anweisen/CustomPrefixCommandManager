package net.codingarea.engine.sql;

import net.codingarea.engine.sql.constant.ConstSQL;
import net.codingarea.engine.sql.helper.PreparedDeletion;
import net.codingarea.engine.sql.helper.PreparedInsertion;
import net.codingarea.engine.sql.helper.PreparedQuery;
import net.codingarea.engine.sql.helper.PreparedUpdate;
import net.codingarea.engine.sql.source.DataSource;
import net.codingarea.engine.utils.Action;
import net.codingarea.engine.utils.Bindable;
import net.codingarea.engine.utils.DefaultLogger;
import net.codingarea.engine.utils.LogLevel;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class SQL implements Bindable {

	/**
	 * @deprecated You should use the {@link PreparedStatement} to prevent SQLInjection.
	 *             You can use ? as placeholders and set its value afterwords using {@link PreparedStatement#setObject(int, Object)}
	 *             If you use {@link #prepare(String, Object...)} it will already set the object array as the params to the {@link PreparedStatement}
	 */
	@Nonnull
	@Deprecated
	@CheckReturnValue
	public static String removeInjectionPossibility(final @Nonnull String string) {
		return string.replaceAll("[']", "\\\\'").replaceAll("[`]", "\\\\`");
	}

	/**
	 * @param result The {@link ResultSet} which should be stored into the {@link CachedRowSet}
	 * @return A new {@link CachedRowSet} using {@link RowSetProvider#newFactory()} as factory
	 * @throws SQLException If a {@link SQLException} is thrown while creating the {@link CachedRowSet}
	 * @see CachedRowSet#populate(ResultSet)
	 */
	@Nonnull
	@CheckReturnValue
	public static CachedRowSet cache(final @Nonnull ResultSet result) throws SQLException {
		CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
		cachedRowSet.populate(result);
		return cachedRowSet;
	}


	/**
	 * @see PreparedStatement#setObject(int, Object)
	 */
	public static void fillParams(final @Nonnull PreparedStatement statement, final @Nonnull Object... params) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			statement.setObject(i+1, params[i]);
		}
	}

	@Nonnull
	@CheckReturnValue
	public static SQL anonymous(final @Nonnull DataSource dataSource) throws SQLException {
		SQL instance = new SQL(dataSource) { };
		instance.connect();
		return instance;
	}

	public static void disconnect(final @Nullable SQL sql) {
		if (sql != null) {
			try {
				sql.disconnect();
			} catch (Throwable ignored) { }
		}
	}

	protected final DataSource dataSource;
	protected volatile Connection connection;
	protected volatile Logger logger = new DefaultLogger(this);

	public SQL(final @Nonnull DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public SQL(final @Nonnull DataSource dataSource, final @Nonnull Logger logger) {
		this.dataSource = dataSource;
		this.logger = logger;
	}

	@CheckReturnValue
	public boolean connectionIsOpened() {
		try {
			return connection != null && !connection.isClosed();
		} catch (Exception ignored) {
			return false;
		}
	}

	/**
	 * Terminates the existing connection, using {@link #disconnect()}
	 * Then it creates a new connection using {@link DataSource#createConnection()}
	 *
	 * @throws SQLException
	 *         If a {@link SQLException} is thrown while disconnection or creating a new connection to the sql server
	 */
	public void connect() throws SQLException {
		if (connectionIsOpened()) {
			disconnect();
		}
		connection = dataSource.createConnection();
		Action.ifPresent(logger, l -> l.log(LogLevel.STATUS, "Connection to database successfully created"));
	}

	/**
	 * Closes the the connection ({@link #getConnection()}) to the sql server using {@link Connection#close()}
	 *
	 * @throws SQLException
	 *         If a {@link SQLException} is thrown while closing the connection
	 */
	public void disconnect() throws SQLException {
		connection.close();
		Action.ifPresent(logger, l -> l.log(LogLevel.STATUS, "Connection to database closed"));
	}

	public void disconnectSafely() {
		try {
			disconnect();
		} catch (Throwable ex) {
			Action.ifPresent(logger, l -> l.log(LogLevel.WARNING, "Exception while disconnecting", ex));
		}
	}

	/**
	 * Connects to the sql server ({@link #connect()}) if the connection is no longer opened (not {@link #connectionIsOpened()})
	 *
	 * @throws SQLException
	 *         If a {@link SQLException} is thrown while connecting to the server
	 */
	public void verifyConnection() throws SQLException {
		if (!connectionIsOpened()) {
			connect();
		}
	}

	@Nonnull
	@CheckReturnValue
	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	/**
	 * Executes a update to the database using {@link Statement#executeUpdate(String)}.
	 * Be aware of SQLInjection.
	 *
	 * @param sql The command which should be executed
	 *
	 * @throws SQLException If a {@link SQLException} is thrown while creating a {@link Statement} (using {@link #createStatement()}),
	 *                      executing the update (using {@link Statement#executeUpdate(String)})
	 *                      or closing the statement (using {@link Statement#close()})
	 *
	 * @see #update(String, Object...)
	 */
	public int executeUpdate(final @Nonnull String sql) throws SQLException {
		verifyConnection();
		Statement statement = createStatement();
		int result = statement.executeUpdate(sql);
		statement.close();
		return result;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedStatement prepare(final @Nonnull String sql) throws SQLException {
		verifyConnection();
		return connection.prepareStatement(sql);
	}

	/**
	 * Creates a {@link PreparedStatement} using {@link #prepare(String)} and sets the params using {@link #fillParams(PreparedStatement, Object...)}
	 *
	 * @param sql The SQLCommand
	 * @param params The params which replace the ?s in the command.
	 * @return The {@link PreparedStatement} just created
	 * @throws SQLException
	 *         If a {@link SQLException} is thrown while preparing the {@link PreparedStatement} ({@link #prepare(String)})
	 *         or while filling the params ({@link #fillParams(PreparedStatement, Object...)})
	 */
	@Nonnull
	@CheckReturnValue
	public PreparedStatement prepare(final @Nonnull String sql, final @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql);
		fillParams(statement, params);
		return statement;
	}

	@Nonnull
	@CheckReturnValue
	public PreparedQuery query() {
		return new PreparedQuery(this);
	}

	@Nonnull
	@CheckReturnValue
	public PreparedUpdate update() {
		return new PreparedUpdate(this);
	}

	@Nonnull
	@CheckReturnValue
	public PreparedInsertion insert() {
		return new PreparedInsertion(this);
	}

	@Nonnull
	@CheckReturnValue
	public PreparedDeletion delete() {
		return new PreparedDeletion(this);
	}

	@Nonnull
	public CachedRowSet query(final @Nonnull String sql, final @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql, params);
		return executeQuery(statement);
	}

	@Nonnull
	public CachedRowSet executeQuery(final @Nonnull String sql) throws SQLException {
		verifyConnection();
		Statement statement = createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		CachedRowSet cachedRowSet = cache(resultSet);
		statement.close();
		return cachedRowSet;
	}

	@Nonnull
	public CachedRowSet executeQuery(final @Nonnull PreparedStatement statement) throws SQLException {
		ResultSet resultSet = statement.executeQuery();
		CachedRowSet cachedRowSet = cache(resultSet);
		statement.close();
		return cachedRowSet;
	}

	public int update(final @Nonnull String sql, final @Nonnull Object... params) throws SQLException {
		PreparedStatement statement = prepare(sql, params);
		return executeUpdate(statement);
	}

	public int executeUpdate(final @Nonnull PreparedStatement statement) throws SQLException {
		int result = statement.executeUpdate();
		statement.close();
		return result;
	}

	public boolean isSet(final @Nonnull String sql, final @Nonnull Object... params) throws SQLException {
		ResultSet result = query(sql, params);
		return isSet(result);
	}

	public boolean isSet(final @Nonnull ResultSet result) throws SQLException {
		boolean set = result.next();
		result.close();
		return set;
	}

	public boolean paramIsSet(final @Nonnull String sql, final @Nonnull String param, final @Nonnull Object... params) throws SQLException {
		ResultSet result = query(sql, params);
		boolean set = false;
		if (result.next()) {
			set = result.getObject(param) != null;
		}
		result.close();
		return set;
	}

	public void switchDatabase(final @Nonnull String database) throws SQLException {
		executeUpdate("use " + database);
		Action.ifPresent(logger, l -> l.log(LogLevel.INFO, "Switched database to \"" + database + "\""));
	}

	@Nonnull
	@CheckReturnValue
	public Connection getConnection() {
		return connection;
	}

	@Nonnull
	@CheckReturnValue
	public DataSource getDataSource() {
		return dataSource;
	}

	@Nullable
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(final @Nullable Logger logger) {
		this.logger = logger;
	}

	@Nonnull
	public <T extends SQL> T setConstant() {
		ConstSQL.setInstance(this);
		return (T) this;
	}

	@Nonnull
	@Override
	public String toString() {
		return "SQL{" +
				"class=" + this.getClass().getSimpleName() +
				", dataSource=" + dataSource +
				", connection=" + connection +
				'}';
	}
}
