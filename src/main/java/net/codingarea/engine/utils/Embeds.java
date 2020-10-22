package net.codingarea.engine.utils;

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
	public static EmbedBuilder construct(@Nonnull String author, String suffix, String icon, String url) {
		return new EmbedBuilder().setAuthor(title(icon, author, suffix), url, icon);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull String author, String suffix, String icon) {
		return construct(author, suffix, icon, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull String author, String suffix) {
		return construct(author, suffix, null, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull String author) {
		return construct(author, null, null, null);
	}

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
		return new EmbedBuilder().setColor(Colors.getMemberColorNotNull(member.getGuild().getSelfMember()))
								 .setAuthor(title(member, suffix), authorURL, member.getUser().getEffectiveAvatarUrl());
	}

	@Nonnull
	@CheckReturnValue
	public static String title(String icon, String content, String suffix) {
		if (content == null) content = "";
		if (suffix == null) suffix = "";
		if (!suffix.isEmpty()) suffix = " • " + suffix;
		return (icon != null ? "» " : "") + content + suffix;
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull Guild guild, String suffix) {
		return title(guild.getIconUrl(), guild.getName(), suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull Member member, String suffix) {
		return title(member.getUser(), suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull User user, String suffix) {
		return title(user.getEffectiveAvatarUrl(), user.getName(), suffix);
	}

}
