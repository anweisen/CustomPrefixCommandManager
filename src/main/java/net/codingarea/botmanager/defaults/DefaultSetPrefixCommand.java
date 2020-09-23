package net.codingarea.botmanager.defaults;

import net.codingarea.botmanager.commandmanager.CommandEvent;
import net.codingarea.botmanager.commandmanager.commands.Command;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class DefaultSetPrefixCommand extends Command {

	private final DefaultPrefixCache cache;

	public DefaultSetPrefixCommand(@Nonnull DefaultPrefixCache cache) {
		this("setprefix", Permission.ADMINISTRATOR, cache);
	}

	public DefaultSetPrefixCommand(@Nonnull String name, @Nonnull Permission permission, @Nonnull DefaultPrefixCache cache, @Nonnull String... alias) {
		super(name, permission, alias);
		this.cache = cache;
	}

	@Override
	public void onCommand(@Nonnull final CommandEvent event) throws Throwable {

		String prefix = event.getArgsAsString().replace("`", "");
		if (prefix.isEmpty()) {
			event.queueReply("Please use " + event.syntax("<prefix>"));
			return;
		}
		if (prefix.length() > 10) {
			event.queueReply("Your prefix cannot be longer than 10 characters.");
			return;
		}

		cache.set(event.getGuildID(), prefix);
		event.queueReply("Your prefix was set to `" + prefix + "`");

	}

}
