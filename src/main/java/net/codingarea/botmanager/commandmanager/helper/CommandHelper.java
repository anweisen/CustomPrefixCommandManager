package net.codingarea.botmanager.commandmanager.helper;

import net.codingarea.botmanager.commandmanager.CommandEvent;
import net.codingarea.botmanager.lang.LanguageManager;
import net.codingarea.botmanager.utils.Replacement;
import net.codingarea.botmanager.utils.StaticBinder;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public abstract class CommandHelper {

	public static LanguageManager getLanguageManager() {
		return StaticBinder.getNullAble(LanguageManager.class);
	}

	@CheckReturnValue
	public final String getMessage(@Nonnull CommandEvent event, @Nonnull String key, Replacement... replacements) {
		return Replacement.replaceAll(getLanguageManager().getMessage(event.getGuild(), key), replacements);
	}

	@Nonnull
	@CheckReturnValue
	public final String getMessage(@Nonnull CommandEvent event, @Nonnull String key, @Nonnull String defaultValue, Replacement... replacements) {
		if (getLanguageManager() == null) {
			return Replacement.replaceAll(defaultValue, replacements);
		} else {
			try {
				return getMessage(event, key, replacements);
			} catch (Exception ignored) {
				return Replacement.replaceAll(defaultValue, replacements);
			}
		}
	}

	@Nonnull
	@CheckReturnValue
	public final String syntax(@Nonnull CommandEvent event, @Nonnull String syntax) {
		return getMessage(event, "syntax", "Please use **%syntax%**",
						  new Replacement("%syntax%", event.syntax(syntax)));
	}

	public final void sendSyntax(@Nonnull CommandEvent event, @Nonnull String syntax) {
		event.queueReply(syntax(event, syntax));
	}

	@Nonnull
	@CheckReturnValue
	public final String removeMarkdown(@Nonnull String string) {
		return CommandEvent.removeMarkdown(string);
	}

}
