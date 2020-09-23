package net.codingarea.botmanager.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public abstract class NamedValueConfig implements Iterable<NamedValue> {

	protected final ArrayList<NamedValue> values = new ArrayList<>();

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

	/**
	 * Gets a {@link NamedValue} using {@link #get(String)}.
	 * If this is not null, the given value will be set to it ({@link NamedValue#setValue(String)}) and return it.
	 * If it was null, well create a new {@link NamedValue} and add it to the value list and return it.
	 */
	@Nonnull
	protected NamedValue create(@Nonnull String key, Object value) {
		NamedValue entry = get(key);
		if (entry != null) {
			entry.setValue(value);
			return entry;
		}
		entry = new NamedValue(key, value);
		values.add(entry);
		return entry;
	}

	protected void load(@Nonnull Properties properties) {
		for (String key : properties.stringPropertyNames()) {
			String stringValue = properties.getProperty(key);
			create(key, stringValue);
		}
	}

	protected void store(@Nonnull Properties properties) {
		for (NamedValue value : values) {
			properties.setProperty(value.getKey(), value.getValue());
		}
	}

	@Nonnull
	@CheckReturnValue
	public Properties asProperties() {
		Properties properties = new Properties();
		store(properties);
		return properties;
	}

	@Nonnull
	public NamedValue add(@Nonnull NamedValue value) {
		values.add(value);
		return value;
	}

	public String getString(@Nonnull String key) {
		try {
			return get(key).value;
		} catch (Exception ignored) {
			return null;
		}
	}

	public int getInt(@Nonnull String key) {
		return NumberConversions.toInt(getString(key));
	}

	public float getFloat(@Nonnull String key) {
		return NumberConversions.toFloat(getString(key));
	}

	public double getDouble(@Nonnull String key) {
		return NumberConversions.toDouble(getString(key));
	}

	public long getLong(@Nonnull String key) {
		return NumberConversions.toLong(getString(key));
	}

	public short getShort(@Nonnull String key) {
		return NumberConversions.toShort(getString(key));
	}

	public byte getByte(@Nonnull String key) {
		return NumberConversions.toByte(getString(key));
	}

	public boolean getBoolean(@Nonnull String key) {
		return Boolean.getBoolean(getString(key));
	}

	public boolean isSet(@Nonnull String key) {
		return get(key) != null;
	}

	@Nonnull
	@CheckReturnValue
	public List<NamedValue> entries() {
		return values;
	}

	@Nonnull
	@Override
	public Iterator<NamedValue> iterator() {
		return values.parallelStream().iterator();
	}

	@Override
	public String toString() {
		return "NamedValueConfig{" +
				"values=" + values +
				'}';
	}

}
