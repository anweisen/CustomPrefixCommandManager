package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.Command;
import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.lang.Language;
import net.codingarea.engine.lang.LanguageManager;
import net.codingarea.engine.utils.Replacement;
import net.dv8tion.jda.api.Permission;

import javax.annotation.Nonnull;
import java.util.Optional;

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

		String givenName = event.getArgsAsString();
		if (givenName.isEmpty()) {
			event.replySyntax("<language>");
			return;
		}

		Optional<Language> language = LanguageManager.getInstance().findLanguage(givenName);
		if (!language.isPresent()) {
			event.reply(getMessage(event, "language-not-found", "The language **%language%** was not found",
						new Replacement("%language%", removeMarkdown(givenName))));
			return;
		}

		LanguageManager.getInstance().setLanguage(event.getGuild(), language.get());
		event.reply(getMessage(event, "language-changed", "The language was changed to **%language%**",
					new Replacement("%language%", removeMarkdown(language.get().getName()))));

	}

}
