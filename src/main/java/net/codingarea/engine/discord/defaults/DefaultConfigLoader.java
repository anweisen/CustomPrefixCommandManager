package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.utils.ConfigLoader;
import net.codingarea.engine.utils.NamedValue;

import java.io.IOException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.3
 */
public class DefaultConfigLoader extends ConfigLoader {

	public static final class DefaultNamedValue {

		private DefaultNamedValue() { }

		public static NamedValue token() {
			return new NamedValue("token", "secret");
		}

		public static NamedValue user() {
			return new NamedValue("user", "root");
		}

		public static NamedValue host() {
			return new NamedValue("host", "127.0.0.1");
		}

		public static NamedValue port() {
			return new NamedValue("port", "3306");
		}

		public static NamedValue password() {
			return new NamedValue("password", "secret");
		}

		public static NamedValue database() {
			return new NamedValue("database", "database");
		}

		public static NamedValue[] all() {
			return new NamedValue[] {
				token(),
				host(),
				port(),
				database(),
				user(),
				password()
			};
		}

	}

	public static DefaultConfigLoader create() {
		try {
			return new DefaultConfigLoader();
		} catch (IOException ex) {
			throw new NullPointerException(ex.getMessage());
		}
	}

	public static DefaultConfigLoader create(String path) {
		try {
			return new DefaultConfigLoader(path);
		} catch (IOException ex) {
			throw new NullPointerException(ex.getMessage());
		}
	}

	public DefaultConfigLoader() throws IOException {
		this("config");
	}

	public DefaultConfigLoader(String path) throws IOException {
		super(path, DefaultNamedValue.all());
	}

	public String getToken() {
		return getString("token");
	}

	public String getUser() {
		return getString("user");
	}

	public String getHost() {
		return getString("host");
	}

	public String getPassword() {
		return getString("password");
	}

	public String getDatabase() {
		return getString("database");
	}

	public int getPort() {
		return getInt("port");
	}

}
