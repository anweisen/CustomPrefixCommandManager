package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.CommandAccessChecker;
import net.codingarea.engine.discord.commandmanager.ICommand;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.sql.cache.SQLValueCache;
import net.codingarea.engine.utils.function.Factory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public class DefaultTeamRankCache extends SQLValueCache implements CommandAccessChecker, Factory<Role, Guild> {

	public DefaultTeamRankCache(boolean cacheValues, @Nonnull String defaultValue, @Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, int clearRate) {
		super(cacheValues, defaultValue, data, table, keyColumn, valueColumn, clearRate);
	}

	public DefaultTeamRankCache(@Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn) {
		super(data, table, keyColumn, valueColumn, null);
	}

	public DefaultTeamRankCache(@Nonnull SQL data) {
		super(data, "guilds", "guildID", "teamRole", null);
	}

	@Override
	public boolean isAllowed(@Nonnull Member member, @Nonnull ICommand command) {

		Role teamRole = null;
		try {
			teamRole = get(member.getGuild());
		} catch (NoSuchElementException ignored) { }

		if (command.isTeamCommand() && teamRole != null) {
			return member.hasPermission(command.getPermissionNeeded() == null ? Permission.ADMINISTRATOR : command.getPermissionNeeded())
				|| member.getRoles().contains(teamRole);
		} else if (command.getPermissionNeeded() != null) {
			return member.hasPermission(command.getPermissionNeeded());
		} else {
			return true;
		}
	}

	public void set(@Nonnull Role role) throws SQLException {
		set(role.getGuild().getId(), role.getId());
	}

	@Nonnull
	@Override
	public Role get(@Nonnull Guild guild) {
		try {
			return guild.getRoleById(get(guild.getId()));
		} catch (Exception ignored) {
			throw new NoSuchElementException();
		}
	}

	public Role getRole(@Nonnull Guild guild) {
		try {
			return guild.getRoleById(get(guild.getId()));
		} catch (Exception ignored) {
			return null;
		}
	}

}
