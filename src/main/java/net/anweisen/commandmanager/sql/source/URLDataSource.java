package net.anweisen.commandmanager.sql.source;

import net.anweisen.commandmanager.utils.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Developed in the CommandManager project
 * on 09-01-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class URLDataSource implements DataSource {

	@Nonnull
	public static URLDataSource ofConfig(@Nonnull ConfigLoader config) {
		Integer port = config.getInt("PORT");
		String host = config.getString("HOST");
		String user = config.getString("USER");
		String password = config.getString("PASSWORD");
		String database = config.getString("DATABASE");
		if (port != null) {
			return new URLDataSource(host, database, user, password, port);
		} else {
			return new URLDataSource(host, database, user, password);
		}
	}

	private final List<LinkAttachment> linkAttachments = new ArrayList<>();
	private final String host, database, user, password;
	private final Integer port;

	public URLDataSource(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = null;
	}

	public URLDataSource(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password, int port) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = port;
	}

	@Nonnull
	@Override
	public String getURL() {
		return MYSQL_URL.replace("%host", host + (port != null ? ":" + port : "")).replace("%database", database) + LinkAttachment.list(linkAttachments);
	}

	@NotNull
	@Override
	public Connection createConnection() throws SQLException {
		return DriverManager.getConnection(getURL(), user, password);
	}

	@Nonnull
	public String getUser() {
		return user;
	}

	@Nonnull
	public String getPassword() {
		return password;
	}

	public Integer getPort() {
		return port;
	}

	@Nonnull
	public String getHost() {
		return host;
	}

	@Nonnull
	public String getDatabase() {
		return database;
	}

	@Nonnull
	public List<LinkAttachment> getLinkAttachments() {
		return linkAttachments;
	}

	@Nonnull
	public URLDataSource addAttachment(@Nonnull LinkAttachment... attachment) {
		linkAttachments.addAll(Arrays.asList(attachment));
		return this;
	}

	@Override
	public String toString() {
		return "URLDataSource{" +
				"linkAttachments=" + linkAttachments +
				", host='" + host + '\'' +
				", database='" + database + '\'' +
				", user='" + user + '\'' +
				", password='" + password + '\'' +
				", port=" + port +
				'}';
	}
}
