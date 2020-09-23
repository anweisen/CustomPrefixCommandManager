package net.codingarea.botmanager.utils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public class PropertiesConfig extends ConfigLoader {

	public PropertiesConfig(@Nonnull File path) throws IOException {
		this(path.getPath(), new NamedValue[0]);
	}

	public PropertiesConfig(@Nonnull String path) throws IOException {
		super(path, false, new NamedValue[0]);
	}

	public PropertiesConfig(@Nonnull File path, @Nonnull String... defaults) throws IOException {
		this(path.getPath(), defaults);
	}

	public PropertiesConfig(@Nonnull File path, @Nonnull NamedValue... defaults) throws IOException {
		this(path.getPath(), defaults);
	}

	public PropertiesConfig(@Nonnull String path, @Nonnull String... defaults) throws IOException {
		super(path, false, defaults);
	}

	public PropertiesConfig(@Nonnull String path, @Nonnull NamedValue... values) throws IOException {
		super(path, false, values);
	}

	public void save() throws IOException {
		Properties properties = this.asProperties();
		PropertiesUtils.saveProperties(properties, file);
	}

	/**
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public PropertiesConfig set(String key, Object value) {
		create(key, value);
		return this;
	}

}
