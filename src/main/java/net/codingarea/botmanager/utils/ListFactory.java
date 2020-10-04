package net.codingarea.botmanager.utils;

import net.codingarea.botmanager.defaults.DefaultLogger;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class ListFactory {

	public static final String REGEX = ",";

	private ListFactory() { }

	public static <T> List<T> stringToList(String string, @Nonnull Factory<T, String> factory) {
		List<T> list = new ArrayList<>();
		if (string == null || string.isEmpty()) return list;
		for (String arg : string.split(REGEX)) {
			try {
				T t = factory.get(arg);
				list.add(t);
			} catch (Exception ex) {
				DefaultLogger.logDefault(LogLevel.WARNING, ListFactory.class, "Cannot generate value: " + ex.getMessage());
			}
		}
		return list;
	}

	@Nonnull
	@CheckReturnValue
	public static <T> String listToString(@Nonnull String split, @Nonnull Iterable<? extends T> list, @Nonnull Factory<String, T> factory) {
		StringBuilder builder = new StringBuilder();
		for (T current : list) {
			try {
				String string = factory.get(current);
				if (builder.length() != 0) builder.append(split);
				builder.append(string);
			} catch (Exception ex) {
				DefaultLogger.logDefault(Level.WARNING, ListFactory.class, "Cannot generate value: " + ex.getMessage());
			}
		}
		return builder.toString();
	}

	public static <T> String listToString(@Nonnull Iterable<? extends T> list, @Nonnull Factory<String, T> factory) {
		return listToString(REGEX, list, factory);
	}

	@Nonnull
	@CheckReturnValue
	public static <T> String listToFancyString(@Nonnull Iterable<? extends T> list, @Nonnull Factory<String, T> factory) {
		return listToString(", ", list, factory);
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> String arrayToString(@Nonnull String split, @Nonnull Factory<String, T> factory, @Nonnull T... array) {
		StringBuilder builder = new StringBuilder();
		for (T current : array) {
			try {
				String string = factory.get(current);
				if (builder.length() != 0) builder.append(split);
				builder.append(string);
			} catch (Exception ex) {
				DefaultLogger.logDefault(Level.WARNING, ListFactory.class, "Cannot generate value: " + ex.getMessage());
			}
		}
		return builder.toString();
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> String arrayToFancyString(@Nonnull Factory<String, T> factory, @Nonnull T... array) {
		return arrayToString(", ", factory, array);
	}

}
