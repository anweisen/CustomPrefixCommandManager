package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.CommandEvent;
import net.codingarea.engine.discord.commandmanager.events.ReceivedCommandEvent;
import net.codingarea.engine.discord.commandmanager.Command;
import net.codingarea.engine.exceptions.LanguageNotFoundException;
import net.codingarea.engine.lang.Language;
import net.codingarea.engine.utils.Replacement;
import net.dv8tion.jda.api.Permission;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public class DefaultLanguageCommand extends Command {

	public DefaultLanguageCommand() {
		this("setlanguage");
	}

	public DefaultLanguageCommand(@Nonnull String name, @Nonnull String... alias) {
		super(name, Permission.ADMINISTRATOR, alias);
	}

	public DefaultLanguageCommand(@Nonnull String name, @Nonnull Permission permission, @Nonnull String... alias) {
		super(name, permission, alias);
	}

	@Override
	public void onCommand(@Nonnull final CommandEvent event) throws Exception {

		if (getLanguageManager() == null) {
			throw new IllegalStateException("LanguageManager was not set via the StaticBinder#bindToClass!");
		}

		String givenName = event.getArgsAsString();
		if (givenName.isEmpty()) {
			sendSyntax(event, "<language>");
			return;
		}

		Language language;

		try {
			language = getLanguageManager().getLanguageByName(givenName);
		} catch (LanguageNotFoundException ex) {
			event.queueReply(getMessage(event, "language-not-found", "The language **%language%** was not found",
							 new Replacement("%language%", removeMarkdown(givenName))));
			return;
		}

		getLanguageManager().setLanguage(event.getGuild(), language);
		event.queueReply(getMessage(event, "language-changed", "The language was changed to **%language%**",
					     new Replacement("%language%", removeMarkdown(language.getName()))));

	}

}
