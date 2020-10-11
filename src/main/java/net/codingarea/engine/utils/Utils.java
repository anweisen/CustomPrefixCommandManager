package net.codingarea.engine.utils;

import net.codingarea.engine.discord.commandmanager.CommandResult;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.TextChannel;
import sun.reflect.CallerSensitive;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
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

	static class SecManager extends SecurityManager {
		public Class<?>[] callerContext() {
			return getClassContext();
		}
	}

	public static String exceptionToString(@Nonnull Throwable ex) {
		StringBuilderPrintWriter writer = new StringBuilderPrintWriter();
		ex.printStackTrace(writer);
		return writer.toString();
	}

	public static List<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<>();
		while (clazz != Object.class) {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.isAnnotationPresent(annotation)) {
					methods.add(method);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return methods;
	}

	public static boolean subClassOf(Class<?> expected, Class<?> given) {

		if (given == null || expected == null) return false;

		while (given != Object.class) {

			if (given == expected) return true;

			if (given.getInterfaces() != null) {
				for (Class<?> currentInterface : given.getInterfaces()) {
					if (subClassOf(expected, currentInterface)) return true;
				}
			}

			given = given.getSuperclass();
			if (given == null) return false;

		}

		return false;

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
		return null;
	}

}
