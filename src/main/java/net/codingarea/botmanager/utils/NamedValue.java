package net.codingarea.botmanager.utils;

import javax.annotation.Nonnull;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
public class NamedValue implements Entry<String, String> {

	public static NamedValue[] ofStrings(String... values) {

		NamedValue[] namedValues = new NamedValue[values.length];
		for (int i = 0; i < values.length; i++) {
			namedValues[i] = new NamedValue(values[i]);
		}

		return namedValues;

	}

	protected final String key;
	protected String value;

	public NamedValue(@Nonnull String key, Object value) {
		this(key, String.valueOf(value));
	}

	public NamedValue(@Nonnull String key, String value) {
		this.key = key;
		this.value = value;
	}

	public NamedValue(@Nonnull String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	@Nonnull
	public String getKey() {
		return key;
	}

	public String setValue(String value) {
		return this.value = value;
	}

	@Override
	public String toString() {
		return "NameSpacedValue{" +
				"key='" + key + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
