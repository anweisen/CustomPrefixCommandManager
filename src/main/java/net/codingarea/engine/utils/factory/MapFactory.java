package net.codingarea.engine.utils.factory;

import net.codingarea.engine.utils.log.LogHelper;
import net.codingarea.engine.utils.log.LogLevel;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class MapFactory {

	public static final String REGEX_1 = ",",
							   REGEX_2 = "=";

	private MapFactory() { }

	@Nonnull
	@CheckReturnValue
	public static <K, V> String mapToString(@Nonnull Map<K, V> map, @Nonnull Function<K, String> key, @Nonnull Function<V, String> value) {
		StringBuilder builder = new StringBuilder();
		for (Entry<K, V> entry : map.entrySet()) {
			if (builder.length() != 0) builder.append(REGEX_1);
			builder.append(key.apply(entry.getKey()));
			builder.append(REGEX_2);
			builder.append(value.apply(entry.getValue()));
		}
		return builder.toString();
	}

	@Nonnull
	@CheckReturnValue
	public static <K, V> Map<K, V> stringToMap(@Nullable String string, @Nonnull Function<String, K> key, final @Nonnull Function<String, V> value) {

		Map<K, V> map = new HashMap<>();
		if (string == null) return map;

		String[] args = string.split(REGEX_1);
		for (String arg : args) {
			try {

				String[] elements = arg.split(REGEX_2);
				K keyElement = key.apply(elements[0]);
				V valueElement = value.apply(elements[1]);

				if (keyElement == null || valueElement == null)
					throw new NullPointerException();

				map.put(keyElement, valueElement);

			} catch (Exception ex) {
				LogHelper.log(LogLevel.WARNING, MapFactory.class, "Cannot generate key/value: " + ex.getMessage());
			}
		}

		return map;

	}

	@Nonnull
	@CheckReturnValue
	public static <K extends Comparable<K>, V> TreeMap<K, V> sort(@Nonnull Map<K, V> map) {
		TreeMap<K, V> sorted = new TreeMap<>(Collections.reverseOrder());
		for (Entry<K, V> entry : map.entrySet()) {
			sorted.put(entry.getKey(), entry.getValue());
		}
		return sorted;
	}

	@Nonnull
	@CheckReturnValue
	public static <K, V> Map<K, V> create(@Nonnull K[] keys, @Nonnull V[] values) {
		if (keys.length != values.length)
			throw new IllegalArgumentException();
		Map<K, V> map = new HashMap<>();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], values[i]);
		}
		return map;
	}

	@Nonnull
	public static <K, V, OldK, OldV> Map<K, V> map(@Nonnull Map<K, V> map, @Nonnull Map<OldK, OldV> input,
	                                               @Nonnull Function<? super OldK, ? extends K> keyMapper,
	                                               @Nonnull Function<? super OldV, ? extends V> valueMapper) {

		for (Entry<OldK, OldV> entry : input.entrySet()) {

			K k = keyMapper.apply(entry.getKey());
			V v = valueMapper.apply(entry.getValue());

			map.put(k, v);

		}

		return map;

	}

}
