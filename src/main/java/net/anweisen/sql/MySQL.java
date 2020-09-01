package net.anweisen.sql;

import net.anweisen.config.ConfigLoader;
import net.anweisen.sql.source.LinkAttachment;
import net.anweisen.sql.source.URLDataSource;

import javax.annotation.Nonnull;
import java.sql.SQLException;

/**
 * Developed in the CommandManager project
 * on 08-31-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class MySQL extends SQL {

	@Nonnull
	public static MySQL ofConfig(ConfigLoader config) throws SQLException {
		return new MySQL(URLDataSource.ofConfig(config));
	}

	@Nonnull
	public static MySQL createDefault(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		return new MySQL(host, database, user, password, LinkAttachment.DEFAULT);
	}

	@Nonnull
	public static MySQL createDefault(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		return new MySQL(host, port, database, user, password, LinkAttachment.DEFAULT);
	}

	public MySQL(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		this(new URLDataSource(host, database, user, password));
	}

	public MySQL(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		this(new URLDataSource(host, database, user, password, port));
	}

	public MySQL(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password, @Nonnull LinkAttachment... attachments) throws SQLException {
		this(new URLDataSource(host, database, user, password).addAttachment(attachments));
	}

	public MySQL(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password, @Nonnull LinkAttachment... attachments) throws SQLException {
		this(new URLDataSource(host, database, user, password, port).addAttachment(attachments));
	}

	public MySQL(@Nonnull URLDataSource dataSource) throws SQLException {
		super(dataSource);
		connect();
	}

}
