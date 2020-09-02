package net.anweisen.config;

import javax.annotation.Nonnull;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
public class NamedValue {

	public static NamedValue[] ofStrings(String... values) {

		NamedValue[] namedValues = new NamedValue[values.length];
		for (int i = 0; i < values.length; i++) {
			namedValues[i] = new NamedValue(values[i]);
		}

		return namedValues;

	}

	protected final String key;
	protected String value;

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

	void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "NameSpacedValue{" +
				"key='" + key + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
