package net.anweisen.commandmanager.utils.loader;

import net.anweisen.commandmanager.utils.PropertiesUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * This class allows you to easily load data from a file by keys
 * @see java.util.Properties
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
public class ConfigLoader {

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

	private final File file;
	private final NamedValue[] values;

	public ConfigLoader(String path, String... values) throws IOException {
		this(path, NamedValue.ofStrings(values));
	}

	public ConfigLoader(String path, NamedValue... values) throws IOException {

		if (values == null || values.length == 0) throw new IllegalArgumentException();

		this.values = values;

		if (!path.toLowerCase().endsWith(".properties")) {
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
			throw new FileNotFoundException();
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
	public String get(String key) {
		for (NamedValue value : values) {
			if (value.getKey() != null && value.getKey().equals(key)) {
				return value.getValue();
			}
		}
		return null;
	}

	public NamedValue[] getValues() {
		return values;
	}

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
