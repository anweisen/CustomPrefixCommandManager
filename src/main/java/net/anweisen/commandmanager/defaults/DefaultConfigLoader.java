package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.utils.loader.ConfigLoader;
import net.anweisen.commandmanager.utils.loader.NamedValue;

import java.io.IOException;

/**
 * Developed in the CommandManager project
 * on 08-31-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.3
 */
public final class DefaultConfigLoader extends ConfigLoader {

	public static final class DefaultNamedValue {

		private DefaultNamedValue() { }

		public static NamedValue token() {
			return new NamedValue("TOKEN", "secret");
		}

		public static NamedValue user() {
			return new NamedValue("USER", "root");
		}

		public static NamedValue host() {
			return new NamedValue("HOST", "127.0.0.1");
		}

		public static NamedValue password() {
			return new NamedValue("PASSWORD", "secret");
		}

		public static NamedValue database() {
			return new NamedValue("DATABASE", "database");
		}

		public static NamedValue[] all() {
			return new NamedValue[] {
				token(),
				host(),
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
			throw new RuntimeException(ex);
		}
	}

	public static DefaultConfigLoader create(String path) {
		try {
			return new DefaultConfigLoader(path);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private DefaultConfigLoader() throws IOException {
		this("config");
	}

	private DefaultConfigLoader(String path) throws IOException {
		super(path, DefaultNamedValue.all());
	}

	public String getToken() {
		return get("TOKEN");
	}

	public String getUser() {
		return get("USER");
	}

	public String getHost() {
		return get("HOST");
	}

	public String getPassword() {
		return get("PASSWORD");
	}

	public String getDatabase() {
		return get("DATABASE");
	}

}
