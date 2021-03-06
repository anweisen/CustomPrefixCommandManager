package net.codingarea.engine.sql.constant;

import net.codingarea.engine.sql.LiteSQL;
import net.codingarea.engine.sql.MySQL;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.sql.helper.PreparedDeletion;
import net.codingarea.engine.sql.helper.PreparedInsertion;
import net.codingarea.engine.sql.helper.PreparedQuery;
import net.codingarea.engine.sql.helper.PreparedUpdate;
import net.codingarea.engine.sql.source.DataSource;
import net.codingarea.engine.utils.*;
import net.codingarea.engine.utils.config.Config;
import net.codingarea.engine.utils.log.LogHelper;
import net.codingarea.engine.utils.log.LogLevel;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.5
 *
 * @see SQL
 */
public final class ConstSQL {

	private static volatile SQL instance;

	private ConstSQL() {
		throw new UnsupportedOperationException();
	}

	public static void connect(@Nonnull DataSource dataSource) throws SQLException {
		closeCurrent();
		instance = SQL.anonymous(dataSource);
	}

	public static void connectWithoutException(@Nonnull DataSource dataSource) {
		try {
			connect(dataSource);
		} catch (SQLException ex) {
			LogHelper.log(LogLevel.ERROR, "Could not open database connection due to an exception: " + Utils.getStackTrace(ex));
		}
	}

	public static void connect(@Nonnull File file) throws IOException, SQLException {
		closeCurrent();
		instance = new LiteSQL(file);
	}

	public static void connectWithoutException(@Nonnull File file) {
		try {
			connect(file);
		} catch (SQLException | IOException ex) {
			LogHelper.log(LogLevel.ERROR, "Could not open database connection due to an exception: " + Utils.getStackTrace(ex));
		}
	}

	public static void connect(@Nonnull Config config) throws SQLException {
		closeCurrent();
		instance = MySQL.defaultOfConfig(config);
	}

	public static void connectWithoutException(@Nonnull Config config) {
		try {
			connect(config);
		} catch (SQLException ex) {
			LogHelper.log(LogLevel.ERROR, "Could not open database connection due to an exception: " + Utils.getStackTrace(ex));
		}
	}

	public static void connect(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		closeCurrent();
		instance = MySQL.createDefault(host, database, user, password);
	}

	public static void connectWithoutException(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) {
		try {
			connect(host, database, user, password);
		} catch (SQLException ex) {
			LogHelper.log(LogLevel.ERROR, "Could not open database connection due to an exception: " + Utils.getStackTrace(ex));
		}
	}

	public static void connect(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		closeCurrent();
		instance = MySQL.createDefault(host, port, database, user, password);
	}

	public static void connectWithoutException(@Nonnull String host, int port, @Nonnull String database,
	                                           @Nonnull String user, @Nonnull String password) {
		try {
			connect(host, port, database, user, password);
		} catch (SQLException ex) {
			LogHelper.log(LogLevel.ERROR, "Could not open database connection due to an exception: " + Utils.getStackTrace(ex));
		}
	}

	private static void closeCurrent() {
		if (instance != null) {
			instance.disconnectSafely();
		}
	}

	@CheckReturnValue
	public static SQL currentInstance() {
		return instance;
	}

	public static void setInstance(@Nullable SQL sql) {
		closeCurrent();
		instance = sql;
	}

	/*
	 * ===============================
	 * Begin of implementation methods
	 * ===============================
	 */

	public static void switchDatabase(@Nonnull String database) throws SQLException {
		instance.switchDatabase(database);
	}

	@CheckReturnValue
	public boolean connectionIsOpened() {
		return instance.connectionIsOpened();
	}

	public static void reconnect() throws SQLException {
		instance.connect();
	}

	public static void disconnect() throws SQLException {
		instance.disconnect();
	}

	public static void disconnectSafely() {
		instance.disconnectSafely();
	}

	public static void verifyConnection() throws SQLException {
		instance.verifyConnection();
	}

	@Nonnull
	@CheckReturnValue
	public static PreparedQuery query() {
		return instance.query();
	}

	@Nonnull
	@CheckReturnValue
	public static PreparedQuery select() {
		return instance.select();
	}

	@Nonnull
	@CheckReturnValue
	public static PreparedUpdate update() {
		return instance.update();
	}

	@Nonnull
	@CheckReturnValue
	public static PreparedInsertion insert() {
		return instance.insert();
	}

	@Nonnull
	@CheckReturnValue
	public static PreparedDeletion delete() {
		return instance.delete();
	}

	@Nonnull
	public static Statement createStatement() throws SQLException {
		return instance.createStatement();
	}

	@Nonnull
	public static CachedRowSet executeQuery(@Nonnull String sql) throws SQLException {
		return instance.executeQuery(sql);
	}

	@Nonnull
	public CachedRowSet executeQuery(@Nonnull PreparedStatement statement) throws SQLException {
		return instance.executeQuery(statement);
	}

	public static int executeUpdate(@Nonnull String sql) throws SQLException {
		return instance.executeUpdate(sql);
	}

	public static int executeUpdate(@Nonnull PreparedStatement statement) throws SQLException {
		return instance.executeUpdate(statement);
	}

	@Nonnull
	@CheckReturnValue
	public static PreparedStatement prepare(@Nonnull String sql) throws SQLException {
		return instance.prepare(sql);
	}

	@Nonnull
	@CheckReturnValue
	public static PreparedStatement prepare(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		return instance.prepare(sql, params);
	}

	public static CachedRowSet query(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		return instance.query(sql, params);
	}

	public static int update(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		return instance.update(sql, params);
	}

	@CheckReturnValue
	public static boolean isSet(@Nonnull String sql, @Nonnull Object... params) throws SQLException {
		return instance.isSet(sql, params);
	}

	@CheckReturnValue
	public static boolean isSet(@Nonnull ResultSet result) throws SQLException {
		return instance.isSet(result);
	}

	@CheckReturnValue
	public static boolean paramIsSet(@Nonnull String sql, @Nonnull String param, @Nonnull Object... params) throws SQLException {
		return instance.paramIsSet(sql, param, params);
	}

	@Nonnull
	@CheckReturnValue
	public static Connection getConnection() {
		return instance.getConnection();
	}

	@Nonnull
	@CheckReturnValue
	public static DataSource getDataSource() {
		return instance.getDataSource();
	}

	public static Logger getLogger() {
		return instance.getLogger();
	}

	public static void setLogger(Logger logger) {
		instance.setLogger(logger);
	}

}
