package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.discord.commandmanager.Command;
import net.codingarea.engine.utils.Replacement;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public class DefaultTeamRankCommand extends Command {

	private final DefaultTeamRankManager rankCache;

	public DefaultTeamRankCommand(@Nonnull String name, @Nonnull DefaultTeamRankManager rankCache,
	                              @Nonnull String... alias) {
		super(name, Permission.ADMINISTRATOR, alias);
		this.rankCache = rankCache;
	}

	public DefaultTeamRankCommand(@Nonnull DefaultTeamRankManager rankCache) {
		super("setteamrank", Permission.ADMINISTRATOR, "setteamrole");
		this.rankCache = rankCache;
	}

	@Override
	public void onCommand(@Nonnull CommandEvent event) throws Exception {

		if (event.getMentionedRoles().size() != 1) {
			event.replySyntax("<@role>");
			return;
		}

		Role role = event.getFirstMentionedRole();
		rankCache.setRole(role);

		event.reply(getMessage(event, "team-rank-set", "You set the team role to `%role%`",
					new Replacement("%role%", role.getName())));

	}

}
