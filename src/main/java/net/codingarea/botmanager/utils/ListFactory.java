package net.codingarea.botmanager.utils;

import net.codingarea.botmanager.defaults.DefaultLogger;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class ListFactory {

	public static final String REGEX = ",";

	private ListFactory() { }

	public static <T> List<T> stringToList(@Nonnull String string, @Nonnull Factory<T, String> factory) {
		List<T> list = new ArrayList<>();
		if (string.isEmpty()) return list;
		for (String arg : string.split(REGEX)) {
			try {
				T t = factory.get(arg);
				list.add(t);
			} catch (Exception ex) {
				DefaultLogger.logDefault(LogLevel.WARNING, ListFactory.class, "Cannot generate entry: " + ex.getMessage());
			}
		}
		return list;
	}

	public static <T> String listToString(@Nonnull List<? extends T> list, @Nonnull Factory<String, T> factory) {
		StringBuilder builder = new StringBuilder();
		for (T current : list) {
			if (builder.length() != 0) builder.append(REGEX);
			builder.append(factory.get(current));
		}
		return builder.toString();
	}

	@Nonnull
	@CheckReturnValue
	public static <T> String listToFancyString(@Nonnull List<? extends T> list, @Nonnull Factory<String, T> factory) {
		StringBuilder builder = new StringBuilder();
		for (T current : list) {
			if (builder.length() != 0) builder.append(", ");
			builder.append(factory.get(current));
		}
		return builder.toString();
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> String arrayToFancyString(@Nonnull Factory<String, T> factory, @Nonnull T... array) {
		StringBuilder builder = new StringBuilder();
		for (T current : array) {
			if (builder.length() != 0) builder.append(", ");
			builder.append(factory.get(current));
		}
		return builder.toString();
	}

}
