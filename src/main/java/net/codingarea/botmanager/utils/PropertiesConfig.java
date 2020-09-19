package net.codingarea.botmanager.utils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Properties;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public class PropertiesConfig extends ConfigLoader {

	public PropertiesConfig(String path, String... values) throws IOException {
		super(path, false, values);
	}

	public PropertiesConfig(String path, NamedValue... values) throws IOException {
		super(path, false, values);
	}

	public void save() throws IOException {
		
		Properties properties = new Properties();
		for (NamedValue value : values) {
			properties.setProperty(value.getKey(), value.getValue());
		}

		PropertiesUtils.saveProperties(properties, file);

	}

	/**
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public PropertiesConfig set(String key, Object value) {
		NamedValue entry = get(key);
		if (entry != null) {
			entry.setValue(String.valueOf(value));
			return this;
		}
		entry = new NamedValue(key, value);
		values.add(entry);
		return this;
	}

}
