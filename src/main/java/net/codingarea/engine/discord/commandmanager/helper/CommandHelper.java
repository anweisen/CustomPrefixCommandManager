package net.codingarea.engine.discord.commandmanager.helper;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.utils.Replacement;
import net.codingarea.engine.utils.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
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
public abstract class CommandHelper extends SearchHelper {

	@CheckReturnValue
	public static long parseTime(String string) {
		return Utils.parseTime(string);
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
	public static boolean isValidID(@Nullable String id) {
		if (id == null) return false;
		if (id.length() != 18) return false;
		try {
			Long.parseLong(id);
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

	@Nonnull
	@CheckReturnValue
	public static String getMessage(@Nonnull CommandEvent event, @Nonnull String key, @Nonnull Replacement... replacements) {
		return event.getMessage(key, replacements);
	}

	@Nonnull
	@CheckReturnValue
	public static String getMessage(@Nonnull CommandEvent event, @Nonnull String key, @Nonnull String defaultValue, Replacement... replacements) {
		return event.getMessage(key, defaultValue, replacements);
	}

	@Nonnull
	@CheckReturnValue
	public static String syntax(@Nonnull CommandEvent event, @Nonnull CharSequence syntax) {
		return event.getMessage("syntax", "Please use `%syntax%`",
						        new Replacement("%syntax%", event.syntax(syntax)));
	}

	@Nonnull
	@CheckReturnValue
	public static String removeMarkdown(@Nullable Object string) {
		return removeMarkdown(string, false);
	}

	@Nonnull
	@CheckReturnValue
	public static String removeMarkdown(@Nullable Object string, boolean inMarkDown) {
		return String.valueOf(string)
				.replace("`", inMarkDown ? "" : "\\`")
				.replace("_", inMarkDown ? "_" : "\\_")
				.replace("*", inMarkDown ? "*" : "\\*")
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
		try {
			if (event instanceof MessageUpdateEvent) {
				return ((MessageUpdateEvent) event).getMember();
			} else if (event instanceof MessageReceivedEvent) {
				return ((MessageReceivedEvent) event).getMember();
			} else if (event instanceof MessageReactionAddEvent) {
				return ((MessageReactionAddEvent) event).getMember();
			} else if (event instanceof MessageReactionRemoveEvent) {
				return ((MessageReactionRemoveEvent) event).getMember();
			}
		} catch (IllegalStateException ex) { }
		return null;
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
