package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class ListFactory {

	public static final String REGEX = ",";

	private ListFactory() { }

	public static <T> List<T> stringToList(final @Nullable String string, final @Nonnull Function<String, T> factory) {
		List<T> list = new ArrayList<>();
		if (string == null || string.isEmpty()) return list;
		for (String arg : string.split(REGEX)) {
			try {
				T t = factory.apply(arg);
				if (t == null) throw new NullPointerException();
				list.add(t);
			} catch (Exception ex) {
				LogHelper.log(LogLevel.WARNING, ListFactory.class, "Cannot generate value: " + ex.getMessage());
			}
		}
		return list;
	}

	@Nonnull
	@CheckReturnValue
	public static <T> String listToString(@Nonnull String split, @Nonnull Iterable<? extends T> list, @Nonnull Function<T, String> factory) {
		StringBuilder builder = new StringBuilder();
		for (T current : list) {
			try {
				String string = factory.apply(current);
				if (builder.length() != 0) builder.append(split);
				builder.append(string);
			} catch (Exception ex) {
				LogHelper.log(Level.WARNING, ListFactory.class, "Cannot generate value: " + ex.getMessage());
			}
		}
		return builder.toString();
	}

	public static <T> String listToString(@Nonnull Iterable<? extends T> list, final @Nonnull Function<T, String> factory) {
		return listToString(REGEX, list, factory);
	}

	@Nonnull
	@CheckReturnValue
	public static <T> String listToFancyString(@Nonnull Iterable<? extends T> list, @Nonnull Function<T, String> factory) {
		return listToString(", ", list, factory);
	}

	@Nonnull
	@CheckReturnValue
	public static <T> String listToFancyString(@Nonnull Iterable<? extends T> list) {
		return listToString(", ", list, DefaultFactory.objectToString());
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> String arrayToString(final @Nonnull String split, final @Nonnull Function<T, String> factory, final @Nonnull T... array) {
		StringBuilder builder = new StringBuilder();
		for (T current : array) {
			try {
				String string = factory.apply(current);
				if (builder.length() != 0) builder.append(split);
				builder.append(string);
			} catch (Exception ex) {
				LogHelper.log(Level.WARNING, ListFactory.class, "Cannot generate value: " + ex.getMessage());
			}
		}
		return builder.toString();
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> String arrayToFancyString(@Nonnull Function<T, String> mapper, @Nonnull T... array) {
		return arrayToString(", ", mapper, array);
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> String arrayToString(final @Nonnull Function<T, String> mapper, final @Nonnull T... array) {
		return arrayToString(",", mapper, array);
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> List<T> merge(final @Nonnull Collection<? extends T>... collections) {
		List<T> list = new ArrayList<>();
		for (Collection<? extends T> current : collections) {
			list.addAll(current);
		}
		return list;
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> T[] toArray(final @Nonnull T[] array, final @Nonnull Collection<? extends T>... collections) {
		return merge(collections).toArray(array);
	}

	@Nonnull
	@CheckReturnValue
	public static Object[] toArray(final @Nonnull Collection<?>... collections) {
		return merge(collections).toArray();
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T, R> List<R> list(final @Nonnull Function<? super T, ? extends R> mapper, final @Nonnull Collection<? extends T>... collections) {
		return list(new ArrayList<>(), mapper, collections);
	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T, R> List<R> list(final @Nonnull List<R> list, final @Nonnull Function<? super T, ? extends R> mapper,
	                                  final @Nonnull Collection<? extends T>... collections) {

		for (Collection<? extends T> collection : collections) {
			for (T t : collection) {
				R r = mapper.apply(t);
				list.add(r);
			}
		}

		return list;

	}

}
