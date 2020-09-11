package net.anweisen.commandmanager.utils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * This class allows you to easily load data from a file by keys using {@link Properties}
 *
 * @see Properties
 * @see PropertiesUtils
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
	protected final NamedValue[] values;

	public ConfigLoader(String path, String... values) throws IOException {
		this(path, NamedValue.ofStrings(values));
	}

	public ConfigLoader(String path, NamedValue... values) throws IOException {

		if (values == null || values.length == 0) throw new IllegalArgumentException("Properties keys cannot be null or empty!");

		this.values = values;

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

			PropertiesUtils.saveProperties(properties, file);
			throw new FileNotFoundException("The config file " + file + " does not exists. Created a new one.");
		}

		// We'll read every value for the given keys and set it as value to the NamedValue
		Properties properties = PropertiesUtils.readProperties(file);
		for (NamedValue value : values) {
			value.setValue(properties.getProperty(value.getKey()));
		}

	}

	/**
	 * @return returns null when no value was found by the name
	 */
	public NamedValue get(String key) {
		for (NamedValue value : values) {
			if (value.getKey().equals(key)) {
				return value;
			}
		}
		return null;
	}

	public String getString(String key) {
		return get(key).value;
	}

	public Integer getInt(String key) {
		try {
			return Integer.parseInt(getString(key));
		} catch (Exception ignored) {
			return null;
		}
	}

	public Float getFloat(String key) {
		try {
			return Float.parseFloat(getString(key));
		} catch (Exception ignored) {
			return null;
		}
	}

	public Double getDouble(String key) {
		try {
			return Double.parseDouble(getString(key));
		} catch (Exception ignored) {
			return null;
		}
	}

	public Long getLong(String key) {
		try {
			return Long.parseLong(getString(key));
		} catch (Exception ignored) {
			return null;
		}
	}

	@Nonnull
	public NamedValue[] getValues() {
		return values;
	}

	@Nonnull
	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return "SecretsLoader{" +
				"path=" + file +
				", values=" + Arrays.toString(values) +
				'}';
	}
}
