package net.codingarea.engine.utils;

import net.codingarea.engine.exceptions.IllegalTypeException;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import sun.reflect.CallerSensitive;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class Utils {

	private Utils() { }

	public static String getEnumName(Enum<?> enun) {
		return getEnumName(enun.name());
	}

	public static String getEnumName(String enumName) {

		if (enumName == null) return "";

		StringBuilder builder = new StringBuilder();
		String[] chars = enumName.split("");
		chars[0] = chars[0].toUpperCase();
		boolean nextUp = true;
		for (String currentChar : chars) {
			if (currentChar.equals("_")) {
				nextUp = true;
				builder.append(" ");
				continue;
			}
			if (nextUp) {
				builder.append(currentChar.toUpperCase());
				nextUp = false;
			} else {
				builder.append(currentChar.toLowerCase());
			}
		}

		return builder.toString()
				.replace(" And ", " and ")
				.replace(" The ", " the ")
				.replace(" Or ", " or ")
				.replace(" Of " , " of")
				.replace(" In ", " in ")
				.replace(" On " , " on ")
				.replace(" Off ", " off ");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter yearTimeDateTime() {
		return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter yearDateTime() {
		return DateTimeFormatter.ofPattern("dd.MM.yyyy");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter dayDateTime() {
		return DateTimeFormatter.ofPattern("dd.MM");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter minuteDateTime() {
		return DateTimeFormatter.ofPattern("HH:mm");
	}

	@Nonnull
	@CheckReturnValue
	public static DateTimeFormatter secondDateTime() {
		return DateTimeFormatter.ofPattern("HH:mm:ss");
	}

	@Nonnull
	@CheckReturnValue
	public static OffsetDateTime centralEuropeTime() {
		return OffsetDateTime.now(ZoneId.of("Europe/Paris"));
	}

	@Nonnull
	@CheckReturnValue
	public static ZoneId centralEuropeZoneId() {
		return ZoneId.of("Europe/Paris");
	}

	@CheckReturnValue
	public static Category findNearestParent(@Nonnull TextChannel channel) {

		if (channel.getParent() != null) return channel.getParent();

		if (!channel.getGuild().getCategories().isEmpty()) {
			return channel.getGuild().getCategories().get(0);
		} else {
			return null;
		}

	}

	@Nonnull
	@CheckReturnValue
	public static String emoteToString(@Nonnull ReactionEmote emote) {
		try {
			return emote.getEmote().getAsMention();
		} catch (Exception ignored) {
			return emote.getEmoji();
		}
	}

	@Nonnull
	@CallerSensitive
	@CheckReturnValue
	public static Class<?>[] callerContext() {
		return new SecManager().callerContext();
	}

	@CallerSensitive
	@CheckReturnValue
	public static Class<?> caller(int index) {
		try {
			return callerContext()[index];
		} catch (Exception ignored) {
			return null;
		}
	}

	@CallerSensitive
	@CheckReturnValue
	public static Class<?> caller() {
		try {
			return caller(2);
		} catch (Exception ignored) {
			return null;
		}
	}

	private static class SecManager extends SecurityManager {
		public Class<?>[] callerContext() {
			return getClassContext();
		}
	}

	public static String exceptionToString(@Nonnull Throwable ex) {
		StringBuilderPrintWriter writer = new StringBuilderPrintWriter();
		ex.printStackTrace(writer);
		return writer.toString();
	}

	@Nonnull
	@CheckReturnValue
	public static List<Method> getMethodsAnnotatedWith(@Nonnull Class<?> clazz, Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				methods.add(method);
			}
		}
		return methods;
	}

	@Nonnull
	@CheckReturnValue
	public static String arrayToString(@Nonnull String[] array) {
		return arrayToString(array, 0, array.length);
	}

	@Nonnull
	@CheckReturnValue
	public static String arrayToString(@Nonnull String[] array, int start) {
		return arrayToString(array, start, array.length);
	}

	@Nonnull
	@CheckReturnValue
	public static String arrayToString(@Nonnull String[] array, int start, int end) {
		StringBuilder builder = new StringBuilder();
		for (int i = start; i < end; i++) {
			builder.append(array[i] + " ");
		}
		return builder.toString().trim();
	}

	@CheckReturnValue
	public static <T extends Enum<?>> T findEnum(@Nonnull T[] enums, @Nonnull String search) {

		for (T current : enums) {
			if (current.name().equalsIgnoreCase(search))
				return current;
			if (current instanceof INamed && ((INamed)current).getName().equalsIgnoreCase(search))
				return current;
		}

		int ordinal = NumberConversions.toInt(search);
		if (ordinal > 0 && ordinal <= enums.length)
			return enums[ordinal - 1];

		return null;

	}

	@Nonnull
	@SafeVarargs
	@CheckReturnValue
	public static <T> T[] array(T... content) {
		return content;
	}

	@CheckReturnValue
	public static long parseTime(@Nonnull String string) {
		long current = 0;
		long seconds = 0;
		for (String c : string.split("")) {
			try {

				int i = Integer.parseInt(c);
				current *= 10;
				current += i;

			} catch (Exception ignored) {

				int multiplier = 1;
				switch (c.toLowerCase()) {
					case "m":
						multiplier = 60;
						break;
					case "h":
						multiplier = 60*60;
						break;
					case "d":
						multiplier = 24*60*60;
						break;
					case "w":
						multiplier = 7*24*60*60;
						break;
					case "y":
						multiplier = 365*24*60*60;
						break;
				}

				seconds += current * multiplier;
				current = 0;

			}
		}
		seconds += current;
		return seconds;
	}

	@Nonnull
	@CheckReturnValue
	public static <T> T nullPrimitive(final @Nonnull Class<T> clazz) {
		if (clazz == boolean.class || clazz == Boolean.class) {
			return (T) Boolean.valueOf(false);
		} else if (clazz == byte.class || clazz == Byte.class) {
			return (T) Byte.valueOf((byte) 0);
		} else if (clazz == short.class || clazz == Short.class) {
			return (T) Short.valueOf((short) 0);
		} else if (clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(0);
		} else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(0);
		} else if (clazz == float.class || clazz == Float.class) {
			return (T) Float.valueOf(0);
		} else if (clazz == double.class || clazz == Double.class) {
			return (T) Double.valueOf(0);
		} else if (clazz == char.class || clazz == Character.class) {
			return (T) Character.valueOf(' ');
		} else {
			throw new IllegalTypeException();
		}
	}

	@Nonnull
	@CheckReturnValue
	public static <T> T notNull(final @Nullable T given, final @Nonnull T defauld) {
		return given != null ? given : defauld;
	}

	@Nonnull
	@CheckReturnValue
	public static String trimString(final @Nonnull String string, final int size) {
		if (size < 0)
			throw new IllegalArgumentException("Size cannot be less than zero");
		if (string.length() == size) {
			return string;
		} else if (string.length() > size) {
			return string.substring(0, size);
		} else {
			StringBuilder builder = new StringBuilder();
			Action.repeat(size - string.length(), (Runnable) () -> builder.append(" "));
			return string + builder;
		}
	}

	@Nonnull
	@CheckReturnValue
	public static String trimSmaller(final @Nonnull String string, final int size, final @Nullable String ending) {
		if (size < 0) {
			throw new IllegalArgumentException("Size cannot be less than zero");
		} else if (string.length() <= size) {
			return string;
		} else {
			return string.substring(0, size) + (ending != null ? ending : "");
		}
	}

}
