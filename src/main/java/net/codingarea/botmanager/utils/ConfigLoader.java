package net.codingarea.botmanager.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This class allows you to easily load data from a file by keys using {@link Properties}
 *
 * @see Properties
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
public class ConfigLoader extends NamedValueConfig implements Bindable {

	public static ConfigLoader withoutException(String path, NamedValue... values) {
		try {
			return new ConfigLoader(path, values);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ConfigLoader withoutException(String path, String... values) {
		try {
			return new ConfigLoader(path, values);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	protected final File file;

	public ConfigLoader(@Nonnull String path, @Nonnull String... defaults) throws IOException {
		this(path, NamedValue.ofStrings(defaults));
	}

	public ConfigLoader(@Nonnull String path, boolean exception, @Nonnull String... defaults) throws IOException {
		this(path, exception, NamedValue.ofStrings(defaults));
	}

	public ConfigLoader(@Nonnull String path, @Nonnull NamedValue... defaults) throws IOException {
		this(path, true, defaults);
	}

	public ConfigLoader(@Nonnull String path, boolean exception, @Nonnull NamedValue... defaults) throws IOException {

		this.values.addAll(Arrays.asList(defaults));

		if (!path.contains(".")) {
			path += ".properties";
		}

		file = new File(path);

		// If the file doesn't already exists, we'll create a new one and set the default values to it
		if (!file.exists()) {

			Properties properties = new Properties();
			for (NamedValue value : values) {
				properties.setProperty(value.getKey(), value.getValue() != null ? value.getValue() : "UNKNOWN");
			}

			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}

			PropertiesUtils.saveProperties(properties, file);
			if (exception) {
				throw new FileNotFoundException("The config file " + file + " does not exists. Created a new one.");
			}

		} else {

			// We'll read every value for the given keys and set it as value to the NamedValue
			Properties properties = PropertiesUtils.readProperties(file);
			load(properties);

		}

	}

	@Nonnull
	@CheckReturnValue
	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return "ConfigLoader{" +
				"file=" + file +
				", size=" + values.size() +
				", values=" + values +
				'}';
	}

}
