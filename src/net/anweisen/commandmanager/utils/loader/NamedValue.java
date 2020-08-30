package net.anweisen.commandmanager.utils.loader;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */

public final class NamedValue {

	public static NamedValue[] ofStrings(String... values) {

		NamedValue[] namedValues = new NamedValue[values.length];
		for (int i = 0; i < values.length; i++) {
			namedValues[i] = new NamedValue(values[i]);
		}

		return namedValues;

	}

	private final String key;
	private String value;

	public NamedValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public NamedValue(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

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
