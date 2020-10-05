package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.sql.cache.SQLValueCache;
import net.codingarea.engine.utils.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.sql.SQLException;
import java.time.OffsetDateTime;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class DefaultLogManager extends SQLValueCache {

	public DefaultLogManager(boolean cacheValues, @Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, int clearRate) {
		super(cacheValues, null, data, table, keyColumn, valueColumn, clearRate);
	}

	public DefaultLogManager(@Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn) {
		super(data, table, keyColumn, valueColumn, null);
	}

	public DefaultLogManager(@Nonnull SQL data) {
		this(data, "guilds", "guildID", "logChannel");
	}

	@CheckReturnValue
	public TextChannel getLogChannel(@Nonnull Guild guild) {
		try {
			return guild.getTextChannelById(get(guild.getId()));
		} catch (Exception ignored) {
			return null;
		}
	}

	public void setLogChannel(@Nonnull TextChannel channel) throws SQLException {
		set(channel.getGuild().getId(), channel.getId());
	}

	public void setLogChannel(@Nonnull Guild guild, @Nullable TextChannel channel) throws SQLException {
		set(guild.getId(), channel != null ? channel.getId() : null);
	}

	/*
	 * ==========================================
	 * Begin of methods to log messages, optional
	 * ==========================================
	 */

	public void log(@Nonnull Guild guild, @Nonnull EmbedBuilder embed) {
		TextChannel log = getLogChannel(guild);
		if (log == null) return;
		log.sendMessage(embed.build()).queue();
	}

	public void log(@Nonnull Guild guild, @Nonnull CharSequence message) {
		TextChannel log = getLogChannel(guild);
		if (log == null) return;
		log.sendMessage(message).queue();
	}

	public void log(@Nonnull Member member, String header, @Nonnull CharSequence description) {
		log(member.getGuild(), Embeds.construct(member, header).setDescription(description).setTimestamp(OffsetDateTime.now()));
	}

	public void log(@Nonnull Member member, @Nonnull Color color, String header, @Nonnull CharSequence description) {
		log(member.getGuild(), Embeds.construct(member, header).setDescription(description).setTimestamp(OffsetDateTime.now()).setColor(color));
	}

}
