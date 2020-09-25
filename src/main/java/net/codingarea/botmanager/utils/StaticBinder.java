package net.codingarea.botmanager.utils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.1
 */
public final class StaticBinder {

	private StaticBinder() { }

	private static final HashMap<String, Object> values = new HashMap<>();

	public static <T> T getNullAble(@Nonnull String key) {
		try {
			return get(key);
		} catch (Exception ignored) {
			return null;
		}
	}

	public static <T> T getNullAble(@Nonnull Class<T> clazz) {
		try {
			return get(clazz);
		} catch (Exception ignored) {
			return null;
		}
	}

	@Nonnull
	public static <T> T get(@Nonnull String key) {
		return (T) values.get(key);
	}

	@Nonnull
	public static <T> T get(long key) {
		return get(String.valueOf(key));
	}

	@Nonnull
	public static <T> T get(double key) {
		return get(String.valueOf(key));
	}

	@Nonnull
	public static <T> T get(char key) {
		return get(String.valueOf(key));
	}

	@Nonnull
	public static <T> T get(@Nonnull Class<T> key) {
		return get(key.getName());
	}

	public static <T> void set(@Nonnull String key, @Nonnull T value) {
		values.put(key, value);
	}

	@Nonnull
	public static <T> Collection<String> keys(@Nonnull T value) {
		List<String> keys = new ArrayList<>();
		for (Entry<String, Object> currentEntry : values.entrySet()) {
			if (currentEntry.getValue() == value) {
				keys.add(currentEntry.getKey());
			}
		}
		return keys;
	}

	@Nonnull
	public static <T> Collection<T> byClass(Class<T> clazz) {
		List<T> list = new ArrayList<>();
		for (Entry<String, Object> currentEntry : values.entrySet()) {
			if (currentEntry.getValue().getClass() == clazz) {
				list.add((T) currentEntry.getValue());
			}
		}
		return list;
	}

	public static void remove(String key) {
		values.remove(key);
	}

	public static int boundObjects() {
		return values.size();
	}

}
