package net.codingarea.engine.sql;

import net.codingarea.engine.sql.source.LinkAttachment;
import net.codingarea.engine.sql.source.URLDataSource;
import net.codingarea.engine.utils.config.Config;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.SQLException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class MySQL extends SQL {

	@Nonnull
	@CheckReturnValue
	public static MySQL ofConfig(final @Nonnull Config config) throws SQLException {
		return new MySQL(URLDataSource.ofConfig(config));
	}

	@Nonnull
	@CheckReturnValue
	public static MySQL defaultOfConfig(final @Nonnull Config config) throws SQLException {
		return new MySQL(URLDataSource.ofConfig(config).addAttachment(LinkAttachment.DEFAULT));
	}

	@Nonnull
	@CheckReturnValue
	public static MySQL createDefault(final @Nonnull String host, final @Nonnull String database, final @Nonnull String user,
	                                  final @Nonnull String password) throws SQLException {
		return new MySQL(host, database, user, password, LinkAttachment.DEFAULT);
	}

	@Nonnull
	@CheckReturnValue
	public static MySQL createDefault(final @Nonnull String host, final int port, final @Nonnull String database, final @Nonnull String user,
	                                  final @Nonnull String password) throws SQLException {
		return new MySQL(host, port, database, user, password, LinkAttachment.DEFAULT);
	}

	public MySQL(final @Nonnull String host, final @Nonnull String database, final @Nonnull String user, final @Nonnull String password) throws SQLException {
		this(new URLDataSource(host, database, user, password));
	}

	public MySQL(final @Nonnull String host, final int port, final @Nonnull String database, final @Nonnull String user, final @Nonnull String password) throws SQLException {
		this(new URLDataSource(host, database, user, password, port));
	}

	public MySQL(final @Nonnull String host, final @Nonnull String database, final @Nonnull String user, final @Nonnull String password, final @Nonnull LinkAttachment... attachments) throws SQLException {
		this(new URLDataSource(host, database, user, password).addAttachment(attachments));
	}

	public MySQL(final @Nonnull String host, final int port, final @Nonnull String database, final @Nonnull String user, final @Nonnull String password, final @Nonnull LinkAttachment... attachments) throws SQLException {
		this(new URLDataSource(host, database, user, password, port).addAttachment(attachments));
	}

	public MySQL(final @Nonnull URLDataSource dataSource) throws SQLException {
		super(dataSource);
		connect();
	}

}
