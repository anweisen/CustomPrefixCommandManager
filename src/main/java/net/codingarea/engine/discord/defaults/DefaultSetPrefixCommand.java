package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.CommandEvent;
import net.codingarea.engine.discord.commandmanager.Command;
import net.codingarea.engine.utils.Replacement;
import net.dv8tion.jda.api.Permission;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public class DefaultSetPrefixCommand extends Command {

	private final DefaultPrefixCache cache;

	public DefaultSetPrefixCommand(@Nonnull DefaultPrefixCache cache) {
		this("setprefix", Permission.ADMINISTRATOR, cache);
	}

	public DefaultSetPrefixCommand(@Nonnull String name, @Nonnull Permission permission, @Nonnull DefaultPrefixCache cache, @Nonnull String... alias) {
		super(name, permission, true, alias);
		this.cache = cache;
	}

	@Override
	public void onCommand(@Nonnull final CommandEvent event) throws Throwable {

		String prefix = event.getArgsAsString().replace("`", "");
		if (prefix.isEmpty()) {
			sendSyntax(event, "<prefix>");
			return;
		}
		if (prefix.length() > 10) {
			event.queueReply(getMessage(event, "prefix-too-long", "The prefix cannot be longer than %max% characters",
							 new Replacement("%max%", 10)));
			return;
		}

		cache.set(event.getGuildID(), prefix);
		event.queueReply(getMessage(event, "prefix-set", "The prefix was set to `%prefix%`",
						 new Replacement("%prefix%", removeMarkdown(prefix))));

	}

}
