package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.PermissionChecker;
import net.codingarea.engine.discord.commandmanager.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public class DefaultPermissionChecker implements PermissionChecker {

	@Override
	public boolean isAllowed(@Nonnull Member member, @Nonnull ICommand command) {
		if (command.getPermissionNeeded() == null && !command.isTeamCommand()) {
			return true;
		} else {
			return member.hasPermission(command.getPermissionNeeded() == null ? Permission.ADMINISTRATOR : command.getPermissionNeeded());
		}
	}
}
