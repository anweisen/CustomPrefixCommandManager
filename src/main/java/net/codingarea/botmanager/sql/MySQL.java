package net.codingarea.botmanager.sql;

import net.codingarea.botmanager.sql.source.LinkAttachment;
import net.codingarea.botmanager.sql.source.URLDataSource;
import net.codingarea.botmanager.utils.NamedValueConfig;

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
	public static MySQL ofConfig(@Nonnull NamedValueConfig config) throws SQLException {
		return new MySQL(URLDataSource.ofConfig(config));
	}

	@Nonnull
	@CheckReturnValue
	public static MySQL defaultOfConfig(@Nonnull NamedValueConfig config) throws SQLException {
		return new MySQL(URLDataSource.ofConfig(config).addAttachment(LinkAttachment.DEFAULT));
	}

	@Nonnull
	@CheckReturnValue
	public static MySQL createDefault(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		return new MySQL(host, database, user, password, LinkAttachment.DEFAULT);
	}

	@Nonnull
	@CheckReturnValue
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
