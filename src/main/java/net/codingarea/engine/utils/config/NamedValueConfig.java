package net.codingarea.engine.utils.config;

import net.codingarea.engine.utils.NumberConversions;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public abstract class NamedValueConfig implements Config {

	protected final List<NamedValue> values = new ArrayList<>();

	/**
	 * @return returns {@code null} when no value was found by the name
	 */
	@Nullable
	@Override
	@CheckReturnValue
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
	 * If this is not {@code null}, the given value will be set to it ({@link NamedValue#setValue(String)}) and return it.
	 * If it was {@code null}, well create a new {@link NamedValue} and add it to the value list and return it.
	 */
	@Nonnull
	protected NamedValue create(@Nonnull String key, @Nullable Object value) {
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

	public void store(@Nonnull Map<? super String, ? super String> properties) {
		for (NamedValue value : values) {
			properties.put(value.getKey(), value.getValue());
		}
	}

	@Nonnull
	@CheckReturnValue
	public Properties asProperties() {
		Properties properties = new Properties();
		store(properties);
		return properties;
	}

	@Nullable
	@Override
	@CheckReturnValue
	public String getString(@Nonnull String key) {
		NamedValue value = get(key);
		return value != null ? value.getValue() : null;
	}

	@Override
	@CheckReturnValue
	public int getInt(@Nonnull String key) {
		return NumberConversions.toInt(getString(key));
	}

	@Override
	@CheckReturnValue
	public float getFloat(@Nonnull String key) {
		return NumberConversions.toFloat(getString(key));
	}

	@Override
	@CheckReturnValue
	public double getDouble(@Nonnull String key) {
		return NumberConversions.toDouble(getString(key));
	}

	@Override
	@CheckReturnValue
	public long getLong(@Nonnull String key) {
		return NumberConversions.toLong(getString(key));
	}

	@Override
	@CheckReturnValue
	public short getShort(@Nonnull String key) {
		return NumberConversions.toShort(getString(key));
	}

	@Override
	@CheckReturnValue
	public byte getByte(@Nonnull String key) {
		return NumberConversions.toByte(getString(key));
	}

	@Override
	@CheckReturnValue
	public boolean getBoolean(@Nonnull String key) {
		return Boolean.getBoolean(getString(key));
	}

	@Override
	@CheckReturnValue
	public boolean isSet(@Nonnull String key) {
		return get(key) != null;
	}

	@Override
	public void clear() {
		values.clear();
	}

	@Nonnull
	@Override
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
