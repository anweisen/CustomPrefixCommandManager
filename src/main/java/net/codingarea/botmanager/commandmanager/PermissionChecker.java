package net.codingarea.botmanager.commandmanager;

import net.codingarea.botmanager.commandmanager.commands.ICommand;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
@FunctionalInterface
public interface PermissionChecker {

	boolean isAllowed(@Nonnull Member member, @Nonnull ICommand command);

}
