package net.codingarea.botmanager.lang;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class Language {

	public static Language load(String path) throws IOException {
		if (!path.contains(".")) path += ".lang";
		return new Language(new File(path));
	}

	private final String name;
	private final String[] alias;
	private final Properties value = new Properties();

	public Language(@Nonnull File file) throws IOException {
		this(file.toURI().toURL().openStream(), file.getName());
	}

	public Language(@Nonnull InputStream input, @Nonnull String path) throws IOException {
		value.load(input);
		String name = value.getProperty("NAME");
		String alias = value.getProperty("ALIAS");
		if (name != null) {
			this.name = name;
		} else {
			this.name = path;
		}
		if (alias != null) {
			this.alias = alias.split(",");
		} else {
			this.alias = new String[0];
		}
	}

	@CheckReturnValue
	public String get(String key) {
		return value.getProperty(key);
	}

	@Nonnull
	@CheckReturnValue
	public String getName() {
		return name;
	}

	@Nonnull
	@CheckReturnValue
	public String[] getAlias() {
		return alias;
	}

	@Nonnull
	@Override
	public String toString() {
		return "Language{" +
				"name='" + name + '\'' +
				", alias=" + Arrays.toString(alias) +
				", value=" + value +
				'}';
	}
}
