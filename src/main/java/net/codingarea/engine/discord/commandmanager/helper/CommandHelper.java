package net.codingarea.engine.discord.commandmanager.helper;

import net.codingarea.engine.discord.commandmanager.CommandEvent;
import net.codingarea.engine.lang.LanguageManager;
import net.codingarea.engine.utils.LogHelper;
import net.codingarea.engine.utils.Replacement;
import net.codingarea.engine.utils.StaticBinder;
import net.codingarea.engine.utils.Utils;
import net.dv8tion.jda.api.entities.*;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public abstract class CommandHelper extends LogHelper {

	public static <T> T getInstance(Class<T> clazz) {
		return StaticBinder.getNullAble(clazz);
	}

	public static LanguageManager getLanguageManager() {
		return getInstance(LanguageManager.class);
	}

	@CheckReturnValue
	public static long parseTime(String string) {
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
				}

				seconds += current * multiplier;
				current = 0;

			}
		}
		seconds += current;
		return seconds;
	}

	@CheckReturnValue
	public static TextChannel findTextChannel(CommandEvent event, String search) {
		if (containsMention(search.trim())) {
			try {
				String id = search.substring(3).substring(0, 18);
				return event.getGuild().getTextChannelById(id);
			} catch (Exception ignored) { }
			try {
				String id = search.substring(2).substring(0, 18);
				return event.getGuild().getTextChannelById(id);
			} catch (Exception ignored) { }
		}
		try {
			return event.getGuild().getTextChannelById(search);
		} catch (Exception ignored) { }
		try {
			return event.getGuild().getTextChannelsByName(search, true).get(0);
		} catch (Exception ignored) { }
		return null;
	}

	@CheckReturnValue
	public static VoiceChannel findVoiceChannel(CommandEvent event, String search) {
		try {
			return event.getGuild().getVoiceChannelById(search);
		} catch (Exception ignored) { }
		try {
			return event.getGuild().getVoiceChannelsByName(search, true).get(0);
		} catch (Exception ignored) { }
		return null;
	}

	@CheckReturnValue
	public static Category findCategory(CommandEvent event, String search) {
		try {
			return event.getGuild().getCategoryById(search);
		} catch (Exception ignored) { }
		try {
			return event.getGuild().getCategoriesByName(search, true).get(0);
		} catch (Exception ignored) { }
		return null;
	}

	@CheckReturnValue
	public static Member findMember(CommandEvent event, String search) {
		if (containsMention(search.trim())) {
			try {
				String id = search.substring(3).substring(0, 18);
				return event.getGuild().getMemberById(id);
			} catch (Exception ignored) { }
			try {
				String id = search.substring(2).substring(0, 18);
				return event.getGuild().getMemberById(id);
			} catch (Exception ignored) { }
		}
		try {
			return event.getGuild().getMemberById(search);
		} catch (Exception ignored) { }
		try {
			return event.getGuild().getMemberByTag(search);
		} catch (Exception ignored) { }
		try {
			return event.getGuild().getMembersByName(search, true).get(0);
		} catch (Exception ignored) { }
		try {
			return event.getGuild().getMembersByEffectiveName(search, true).get(0);
		} catch (Exception ignored) { }
		return null;
	}

	@Nonnull
	@CheckReturnValue
	public static String fancyEnumName(@Nonnull Enum<?> enun) {
		return Utils.getEnumName(enun);
	}

	@Nonnull
	@CheckReturnValue
	public static String fancyEnumName(@Nonnull String enun) {
		return Utils.getEnumName(enun);
	}

	@CheckReturnValue
	public static boolean isValidID(String id) {
		if (id == null) return false;
		if (id.length() != 18) return false;
		try {
			Integer.parseInt(id);
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

	@CheckReturnValue
	public static String getMessage(@Nonnull Guild guild, @Nonnull String key, Replacement... replacements) {
		return getMessage(guild, key, "Message: `" + key + "`", replacements);
	}

	@Nonnull
	@CheckReturnValue
	public static String getMessage(@Nonnull Guild guild, @Nonnull String key, @Nonnull String defaultValue, Replacement... replacements) {
		if (getLanguageManager() == null) {
			return Replacement.replaceAll(defaultValue, replacements);
		} else {
			try {
				return Replacement.replaceAll(getLanguageManager().getMessage(guild, key), replacements);
			} catch (Exception ignored) {
				return Replacement.replaceAll(defaultValue, replacements);
			}
		}
	}

	@Nonnull
	@CheckReturnValue
	public static String getMessage(@Nonnull CommandEvent event, @Nonnull String key, @Nonnull String defaultValue, Replacement... replacements) {
		return getMessage(event.getGuild(), key, defaultValue, replacements);
	}

	@Nonnull
	@CheckReturnValue
	public static String getMessage(@Nonnull CommandEvent event, @Nonnull String key, Replacement... replacements) {
		return getMessage(event.getGuild(), key, replacements);
	}

	@Nonnull
	@CheckReturnValue
	public static String syntax(@Nonnull CommandEvent event, @Nonnull String syntax) {
		return getMessage(event, "syntax", "Please use `%syntax%`",
						  new Replacement("%syntax%", event.syntax(syntax)));
	}

	public static void sendSyntax(@Nonnull CommandEvent event, @Nonnull String syntax) {
		event.queueReply(syntax(event, syntax));
	}

	@Nonnull
	@CheckReturnValue
	public static String removeMarkdown(@Nonnull String string) {
		return string.replace("`", "\\`")
				.replace("_", "\\_")
				.replace("*", "\\*");
	}

	/**
	 * @return returns if the given text mentions a {@link Member}
	 * @see IMentionable
	 * @since 1.2
	 */
	@CheckReturnValue
	public static boolean containsMention(@Nonnull String text) {

		// n is a placeholder for any number
		char[] goal = "<@!nnnnnnnnnnnnnnnnnn>".toCharArray();
		int current = 0;
		for (char currentChar : text.toCharArray()) {

			boolean isInMention = false;
			char expected = goal[current];

			if (currentChar == expected) {
				isInMention = true;
			} else if (expected == 'n') {
				try {
					Integer.parseInt(String.valueOf(current));
					isInMention = true;
				} catch (NumberFormatException ignored) { }
			}

			if (isInMention) {
				current++;
			} else {
				current = 0;
			}

			if (current == goal.length) return true;

		}

		return false;

	}

}
