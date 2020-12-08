package net.codingarea.engine.discord.commandmanager.helper;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.lang.LanguageManager;
import net.codingarea.engine.utils.LogHelper;
import net.codingarea.engine.utils.Replacement;
import net.codingarea.engine.utils.StaticBinder;
import net.codingarea.engine.utils.Utils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	@Nullable
	@CheckReturnValue
	public static Role findRole(@Nonnull CommandEvent event, String search) {
		return findRole(event.getGuild(), search);
	}

	@Nullable
	@CheckReturnValue
	public static Role findRole(@Nonnull Guild guild, String search) {
		search = search.trim();
		try {
			String id = search.substring(3).substring(0, 18);
			Role role = guild.getRoleById(id);
			if (role != null)
				return role;
		} catch (Exception ignored) { }
		try {
			String id = search.substring(2).substring(0, 18);
			Role role = guild.getRoleById(id);
			if (role != null)
				return role;
		} catch (Exception ignored) { }
		try {
			Role role = guild.getRoleById(search);
			if (role != null)
				return role;
		} catch (Exception ignored) { }
		try {
			Role role = guild.getRolesByName(search, true).get(0);
			if (role != null)
				return role;
		} catch (Exception ignored) { }
		return null;
	}

	@Nullable
	@CheckReturnValue
	public static TextChannel findTextChannel(@Nonnull CommandEvent event, String search) {
		return findTextChannel(event.getGuild(), search);
	}

	@Nullable
	@CheckReturnValue
	public static TextChannel findTextChannel(@Nonnull Guild guild, String search) {
		search = search.trim();
		try {
			String id = search.substring(3).substring(0, 18);
			TextChannel channel = guild.getTextChannelById(id);
			if (channel != null)
				return channel;
		} catch (Exception ignored) { }
		try {
			String id = search.substring(2).substring(0, 18);
			TextChannel channel = guild.getTextChannelById(id);
			if (channel != null)
				return channel;
		} catch (Exception ignored) { }
		try {
			TextChannel channel = guild.getTextChannelById(search);
			if (channel != null)
				return channel;
		} catch (Exception ignored) { }
		try {
			TextChannel channel = guild.getTextChannelsByName(search, true).get(0);
			if (channel != null)
				return channel;;
		} catch (Exception ignored) { }
		return null;
	}

	@Nullable
	@CheckReturnValue
	public static VoiceChannel findVoiceChannel(@Nonnull CommandEvent event, String search) {
		return findVoiceChannel(event.getGuild(), search);
	}

	@Nullable
	@CheckReturnValue
	public static VoiceChannel findVoiceChannel(@Nonnull Guild guild, String search) {
		search = search.trim();
		try {
			VoiceChannel channel = guild.getVoiceChannelById(search);
			if (channel != null)
				return channel;
		} catch (Exception ignored) { }
		try {
			VoiceChannel channel = guild.getVoiceChannelsByName(search, true).get(0);
			if (channel != null)
				return channel;
		} catch (Exception ignored) { }
		return null;
	}

	@Nullable
	@CheckReturnValue
	public static Category findCategory(@Nonnull CommandEvent event, String search) {
		return findCategory(event.getGuild(), search);
	}

	@Nullable
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

	@Nullable
	@CheckReturnValue
	public static Member findMember(@Nonnull CommandEvent event, String search) {
		return findMember(event.getGuild(), search);
	}

	@Nullable
	@CheckReturnValue
	public static Member findMember(@Nonnull Guild guild, String search) {
		search = search.trim();
		try {
			String id = search.substring(3).substring(0, 18);
			return guild.getMemberById(id);
		} catch (Exception ignored) { }
		try {
			String id = search.substring(2).substring(0, 18);
			return guild.getMemberById(id);
		} catch (Exception ignored) { }
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

	@Nullable
	@CheckReturnValue
	public static Message findMessage(@Nonnull MessageChannel channel, @Nonnull String search) {
		try {
			return channel.retrieveMessageById(search).complete();
		} catch (Exception ignored) { }
		return null;
	}

	@Nullable
	@CheckReturnValue
	public static GuildChannel findGuildChannel(@Nonnull Guild guild, String search) {
		search = search.trim();
		try {
			String id = search.substring(3).substring(0, 18);
			GuildChannel channel = guild.getGuildChannelById(id);
			if (channel != null)
				return channel;
		} catch (Exception ignored) { }
		try {
			String id = search.substring(2).substring(0, 18);
			GuildChannel channel = guild.getGuildChannelById(id);
			if (channel != null)
				return channel;
		} catch (Exception ignored) { }
		try {
			GuildChannel channel = guild.getGuildChannelById(search);
			if (channel != null)
				return channel;
		} catch (Exception ignored) { }
		try {
			for (GuildChannel channel : guild.getChannels()) {
				if (channel.getName().equalsIgnoreCase(search))
					return channel;
			}
		} catch (Exception ignored) { }
		return null;
	}

	@Nullable
	@CheckReturnValue
	public static GuildChannel findGuildChannel(@Nonnull CommandEvent event, String search) {
		return findGuildChannel(event.getGuild(), search);
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
			Long.parseLong(id);
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
	public static String syntax(@Nonnull CommandEvent event, @Nonnull CharSequence syntax) {
		return getMessage(event, "syntax", "Please use `%syntax%`",
						  new Replacement("%syntax%", event.syntax(syntax)));
	}

	public static void sendSyntax(@Nonnull CommandEvent event, @Nonnull CharSequence syntax) {
		event.reply(syntax(event, syntax));
	}

	@Nonnull
	@CheckReturnValue
	public static String removeMarkdown(@Nonnull String string) {
		return removeMarkdown(string, false);
	}

	@Nonnull
	@CheckReturnValue
	public static String removeMarkdown(@Nonnull String string, boolean inMarkDown) {
		return string.replace("`", inMarkDown ? "" : "\\`")
				.replace("_", inMarkDown ? "_" : "\\_")
				.replace("*", inMarkDown ? "?" : "\\*")
				.replace("@", inMarkDown ? "@" : "");
	}

	/**
	 * @return returns if the given text mentions a {@link Member}
	 *
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

	@Nonnull
	@CheckReturnValue
	public static String mentionJDA(@Nonnull Event event) {
		return "<@!" + event.getJDA().getSelfUser().getId() + ">";
	}
	@CheckReturnValue
	public static Member getMember(@Nonnull GenericMessageEvent event) {
		if (event instanceof MessageUpdateEvent) {
			return ((MessageUpdateEvent) event).getMember();
		} else if (event instanceof MessageReceivedEvent) {
			return ((MessageReceivedEvent) event).getMember();
		} else if (event instanceof MessageReactionAddEvent) {
			return ((MessageReactionAddEvent) event).getMember();
		} else if (event instanceof MessageReactionRemoveEvent) {
			return ((MessageReactionRemoveEvent) event).getMember();
		} else {
			return null;
		}
	}

	@CheckReturnValue
	public static Message getMessage(@Nonnull GenericMessageEvent event) {
		if (event instanceof MessageUpdateEvent) {
			return ((MessageUpdateEvent) event).getMessage();
		} else if (event instanceof MessageReceivedEvent) {
			return ((MessageReceivedEvent) event).getMessage();
		} else {
			return null;
		}
	}

}
