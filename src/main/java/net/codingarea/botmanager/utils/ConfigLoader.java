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
public class ConfigLoader implements Bindable {

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
	protected final List<NamedValue> values;

	public ConfigLoader(final String path, final String... defaults) throws IOException {
		this(path, NamedValue.ofStrings(defaults));
	}

	public ConfigLoader(final String path, final boolean exception, final String... defaults) throws IOException {
		this(path, exception, NamedValue.ofStrings(defaults));
	}

	public ConfigLoader(String path, final NamedValue... defaults) throws IOException {
		this(path, true, defaults);
	}

	public ConfigLoader(String path, final boolean exception, final NamedValue... defaults) throws IOException {

		if (defaults == null) throw new IllegalArgumentException("Properties keys cannot be null or empty!");

		this.values = new ArrayList<>(Arrays.asList(defaults));

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
			for (NamedValue value : values) {
				value.setValue(properties.getProperty(value.getKey()));
			}

		}


	}

	/**
	 * @return returns null when no value was found by the name
	 */
	public NamedValue get(@Nonnull String key) {
		for (NamedValue value : values) {
			if (value.getKey().equals(key)) {
				return value;
			}
		}
		return null;
	}

	public String getString(@Nonnull String key) {
		return get(key).value;
	}

	public Integer getInt(@Nonnull String key) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Exception ignored) {
			return null;
		}
	}

	public Float getFloat(@Nonnull String key) {
		try {
			return Float.parseFloat(getString(key));
		} catch (Exception ignored) {
			return null;
		}
	}

	public Double getDouble(@Nonnull String key) {
		try {
			return Double.parseDouble(getString(key));
		} catch (Exception ignored) {
			return null;
		}
	}

	public Long getLong(@Nonnull String key) {
		try {
			return Long.parseLong(getString(key));
		} catch (Exception ignored) {
			return null;
		}
	}

	@Nonnull
	@CheckReturnValue
	public List<NamedValue> getValues() {
		return values;
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
