package net.codingarea.botmanager.defaults.commands;

import net.codingarea.botmanager.commandmanager.CommandEvent;
import net.codingarea.botmanager.commandmanager.commands.Command;
import net.codingarea.botmanager.defaults.DefaultTeamRankCache;
import net.codingarea.botmanager.utils.Replacement;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public final class DefaultTeamRankCommand extends Command {

	private final DefaultTeamRankCache rankCache;

	public DefaultTeamRankCommand(@Nonnull String name, @Nonnull DefaultTeamRankCache rankCache, @Nonnull String... alias) {
		super(name, Permission.ADMINISTRATOR, true, alias);
		this.rankCache = rankCache;
	}

	public DefaultTeamRankCommand(@Nonnull DefaultTeamRankCache rankCache) {
		super("setteamrank", Permission.ADMINISTRATOR, true, "setteamrole");
		this.rankCache = rankCache;
	}

	@Override
	public void onCommand(@Nonnull final CommandEvent event) throws Throwable {

		if (event.getMentionedRoles().size() != 1) {
			sendSyntax(event, "<@role>");
			return;
		}

		Role role = event.getMentionedRoles().get(0);
		rankCache.set(role);

		event.queueReply(getMessage(event, "team-rank-set", "You set the team role to `%role%`",
						 new Replacement("%role%", role.getName())));

	}

}
