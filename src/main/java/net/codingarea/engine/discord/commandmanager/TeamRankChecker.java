package net.codingarea.engine.discord.commandmanager;

import net.dv8tion.jda.api.entities.Member;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
@FunctionalInterface
public interface TeamRankChecker {

	boolean hasTeamRank(@Nonnull Member member);

}
