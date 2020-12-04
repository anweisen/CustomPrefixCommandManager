package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.TeamRankChecker;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.sql.cache.SQLValueCache;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public class DefaultTeamRankManager extends SQLValueCache implements TeamRankChecker {

	public DefaultTeamRankManager(final boolean cacheValues, final @Nonnull String defaultValue,
	                              final @Nonnull SQL data, final @Nonnull String table,
	                              final @Nonnull String keyColumn, final @Nonnull String valueColumn,
	                              final int clearRate) {
		super(cacheValues, defaultValue, data, table, keyColumn, valueColumn, clearRate);
	}

	public DefaultTeamRankManager(final @Nonnull SQL data, final @Nonnull String table, final @Nonnull String keyColumn,
	                              final @Nonnull String valueColumn) {
		super(data, table, keyColumn, valueColumn, null);
	}

	public DefaultTeamRankManager(final @Nonnull SQL data) {
		super(data, "guilds", "guildID", "teamRole", null);
	}

	@Override
	public boolean hasTeamRank(final @Nonnull Member member) {
		Role role = getRole(member.getGuild());
		return member.hasPermission(Permission.ADMINISTRATOR) || (role != null && member.getRoles().contains(role));
	}

	public void setRole(final @Nonnull Role role) throws SQLException {
		set(role.getGuild().getId(), role.getId());
	}

	public void setRole(final @Nonnull Guild guild, final @Nullable Role role) throws SQLException {
		set(guild.getId(), role != null ? role.getId() : null);
	}

	@Nullable
	@CheckReturnValue
	public Role getRole(final @Nonnull Guild guild) {
		try {
			return guild.getRoleById(get(guild.getId()));
		} catch (Exception ignored) {
			return null;
		}
	}

}
