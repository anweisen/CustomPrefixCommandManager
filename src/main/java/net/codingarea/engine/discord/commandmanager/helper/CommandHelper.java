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

	public static LanguageManager getLanguageManager() {
		return StaticBinder.get(LanguageManager.class);
	}

	@CheckReturnValue
	public static long parseTime(String string) {
		return Utils.parseTime(string);
	}

	@CheckReturnValue
	public static Role findRole(@Nonnull CommandEvent event, String search) {
		return findRole(event.getGuild(), search);
	}

	@CheckReturnValue
	public static Role findRole(@Nonnull Guild guild, String search) {
		search = search.trim();
		if (containsMention(search)) {
			try {
				String id = search.substring(3).substring(0, 18);
				return guild.getRoleById(id);
			} catch (Exception ignored) { }
			try {
				String id = search.substring(2).substring(0, 18);
				return guild.getRoleById(id);
			} catch (Exception ignored) { }
		}
		try {
			return guild.getRoleById(search);
		} catch (Exception ignored) { }
		try {
			return guild.getRolesByName(search, true).get(0);
		} catch (Exception ignored) { }
		return null;
	}

	@CheckReturnValue
	public static TextChannel findTextChannel(@Nonnull CommandEvent event, String search) {
		return findTextChannel(event.getGuild(), search);
	}

	@CheckReturnValue
	public static TextChannel findTextChannel(@Nonnull Guild guild, String search) {
		search = search.trim();
		if (containsMention(search)) {
			try {
				String id = search.substring(3).substring(0, 18);
				return guild.getTextChannelById(id);
			} catch (Exception ignored) { }
			try {
				String id = search.substring(2).substring(0, 18);
				return guild.getTextChannelById(id);
			} catch (Exception ignored) { }
		}
		try {
			return guild.getTextChannelById(search);
		} catch (Exception ignored) { }
		try {
			return guild.getTextChannelsByName(search, true).get(0);
		} catch (Exception ignored) { }
		return null;
	}

	@CheckReturnValue
	public static VoiceChannel findVoiceChannel(@Nonnull CommandEvent event, String search) {
		return findVoiceChannel(event.getGuild(), search);
	}

	@CheckReturnValue
	public static VoiceChannel findVoiceChannel(@Nonnull Guild guild, String search) {
		search = search.trim();
		try {
			return guild.getVoiceChannelById(search);
		} catch (Exception ignored) { }
		try {
			return guild.getVoiceChannelsByName(search, true).get(0);
		} catch (Exception ignored) { }
		return null;
	}

	@CheckReturnValue
	public static Category findCategory(@Nonnull CommandEvent event, String search) {
		return findCategory(event.getGuild(), search);
	}

	@CheckReturnValue
	public static Category findCategory(@Nonnull Guild guild, String search) {
		search = search.trim();
		try {
			return guild.getCategoryById(search);
		} catch (Exception ignored) { }
		try {
			return guild.getCategoriesByName(search, true).get(0);
		} catch (Exception ignored) { }
		return null;
	}

	@CheckReturnValue
	public static Member findMember(@Nonnull CommandEvent event, String search) {
		return findMember(event.getGuild(), search);
	}

	@CheckReturnValue
	public static Member findMember(@Nonnull Guild guild, String search) {
		search = search.trim();
		if (containsMention(search)) {
			try {
				String id = search.substring(3).substring(0, 18);
				return guild.getMemberById(id);
			} catch (Exception ignored) { }
			try {
				String id = search.substring(2).substring(0, 18);
				return guild.getMemberById(id);
			} catch (Exception ignored) { }
		}
		try {
			return guild.getMemberById(search);
		} catch (Exception ignored) { }
		try {
			return guild.getMemberByTag(search);
		} catch (Exception ignored) { }
		try {
			return guild.getMembersByName(search, true).get(0);
		} catch (Exception ignored) { }
		try {
			return guild.getMembersByEffectiveName(search, true).get(0);
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
