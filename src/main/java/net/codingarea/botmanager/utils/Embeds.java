package net.codingarea.botmanager.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public final class Embeds {

	private Embeds() { }

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Guild guild) {
		return construct(guild, null, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Guild guild, String suffix) {
		return construct(guild, null, suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Guild guild, String authorURL, String suffix) {
		return new EmbedBuilder().setColor(Colors.getMemberColorNotNull(guild.getSelfMember()))
								 .setAuthor(title(guild, suffix), authorURL, guild.getIconUrl());
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Member member) {
		return construct(member, null, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Member member, String suffix) {
		return construct(member, null, suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Member member, String authorURL, String suffix) {
		return new EmbedBuilder().setColor(Colors.getMemberColorNotNull(member))
								 .setAuthor(title(member, suffix), authorURL, member.getUser().getEffectiveAvatarUrl());
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull Guild guild, String suffix) {
		if (suffix == null) suffix = "";
		if (!suffix.isEmpty()) suffix = " • " + suffix;
		return (guild.getIconUrl() != null ? "» " : "") + guild.getName() + suffix;
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull Member member, String suffix) {
		return title(member.getUser(), suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull User user, String suffix) {
		if (suffix == null) suffix = "";
		if (!suffix.isEmpty()) suffix = " • " + suffix;
		return "» " + user.getName() + suffix;
	}

}
