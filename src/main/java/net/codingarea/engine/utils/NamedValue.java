package net.codingarea.engine.utils;

import org.jetbrains.annotations.Contract;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
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

	public NamedValue(@Nonnull String key, Object value) {
		this(key, value == null ? null : value.toString());
	}

	public NamedValue(@Nonnull String key, String value) {
		this.key = key;
		this.value = value;
	}

	public NamedValue(@Nonnull String key) {
		this.key = key;
	}

	@Contract("null -> null; !null -> !null")
	public String setValue(final String value) {
		return this.value = value;
	}

	@Contract("null -> null; !null -> !null")
	public String setValue(final Object value) {
		return setValue(value == null ? null : value.toString());
	}

	@Nonnull
	@CheckReturnValue
	public String getKey() {
		return key;
	}

	@Nullable
	@CheckReturnValue
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "NamedValue{" +
				"key='" + key + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
