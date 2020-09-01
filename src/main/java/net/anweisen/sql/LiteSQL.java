package net.anweisen.sql;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import net.anweisen.sql.source.FileDataSource;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Developed in the CommandManager project
 * on 09-01-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LiteSQL extends SQL {

	public static LiteSQL createDefault() throws SQLException, IOException {
		return new LiteSQL(new FileDataSource("config.db"));
	}

	public LiteSQL(@Nonnull String path) throws SQLException, IOException {
		this(new File(path));
	}

	public LiteSQL(@Nonnull File file) throws SQLException, IOException {
		this(new FileDataSource(file));
	}

	public LiteSQL(@Nonnull FileDataSource dataSource) throws SQLException, IOException {
		super(dataSource);
		if (!dataSource.getFile().exists()) dataSource.getFile().createNewFile();
		connect();
	}

}
