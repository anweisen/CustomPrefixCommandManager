package net.codingarea.botmanager.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

}
