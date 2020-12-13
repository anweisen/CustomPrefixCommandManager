package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.exceptions.MessageException;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.sql.cache.SQLValueCache;
import net.codingarea.engine.utils.Embeds;
import net.codingarea.engine.utils.FileUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.function.Consumer;

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

	//
	// Methods to log messages
	//

	public void log(@Nonnull Guild guild, @Nonnull EmbedBuilder embed, @Nonnull Consumer<? super Message> sent) {
		TextChannel log = getLogChannel(guild);
		if (log == null) return;
		log.sendMessage(embed.build()).queue(sent, MessageException::create);
	}
	public void log(@Nonnull Guild guild, @Nonnull EmbedBuilder embed) {
		log(guild, embed, m -> {});
	}

	public void log(@Nonnull Guild guild, @Nonnull CharSequence message, @Nonnull Consumer<? super Message> sent) {
		TextChannel log = getLogChannel(guild);
		if (log == null) return;
		log.sendMessage(message).queue(sent, MessageException::create);
	}
	public void log(@Nonnull Guild guild, @Nonnull CharSequence message) {
		log(guild, message, m -> {});
	}

	public void log(@Nonnull Guild guild, @Nonnull File file, @Nonnull String fileName, @Nonnull String format, boolean delete, @Nonnull Consumer<? super Message> sent) {
		TextChannel log = getLogChannel(guild);
		if (log == null) return;
		FileUtils.send(log, fileName, format, file, delete, sent);
	}
	public void log(@Nonnull Guild guild, @Nonnull File file, @Nonnull String fileName, @Nonnull String format, boolean delete) {
		log(guild, file, fileName, format, delete, m -> {});
	}

	public void log(@Nonnull Guild guild, @Nonnull CharSequence text, @Nonnull File file, @Nonnull String fileName, @Nonnull String format, boolean delete, @Nonnull Consumer<? super Message> sent) throws IOException {
		TextChannel log = getLogChannel(guild);
		if (log == null) return;
		FileUtils.send(log, text, fileName, format, file, delete, sent);
	}
	public void log(@Nonnull Guild guild, @Nonnull CharSequence text, @Nonnull File file, @Nonnull String fileName, @Nonnull String format, boolean delete) throws IOException {
		log(guild, text, file, fileName, format, delete, m -> {});
	}

	public void log(@Nonnull Guild guild, @Nonnull MessageEmbed embed, @Nonnull File file, @Nonnull String fileName, @Nonnull String format, boolean delete, @Nonnull Consumer<? super Message> sent) throws IOException {
		TextChannel log = getLogChannel(guild);
		if (log == null) return;
		FileUtils.send(log, embed, fileName, format, file, delete);
	}
	public void log(@Nonnull Guild guild, @Nonnull MessageEmbed embed, @Nonnull File file, @Nonnull String fileName, @Nonnull String format, boolean delete) throws IOException {
		TextChannel log = getLogChannel(guild);
		if (log == null) return;
		FileUtils.send(log, embed, fileName, format, file, delete);
	}

	public void log(@Nonnull Member member, String header, @Nonnull CharSequence description, @Nonnull Consumer<? super Message> sent) {
		log(member.getGuild(), Embeds.construct(member, header).setDescription(description).setTimestamp(OffsetDateTime.now()), sent);
	}
	public void log(@Nonnull Member member, String header, @Nonnull CharSequence description) {
		log(member.getGuild(), Embeds.construct(member, header).setDescription(description).setTimestamp(OffsetDateTime.now()));
	}

	public void log(@Nonnull Member member, @Nonnull Color color, String header, @Nonnull CharSequence description, @Nonnull Consumer<? super Message> sent) {
		log(member.getGuild(), Embeds.construct(member, header).setDescription(description).setTimestamp(OffsetDateTime.now()).setColor(color), sent);
	}
	public void log(@Nonnull Member member, @Nonnull Color color, String header, @Nonnull CharSequence description) {
		log(member, color, header, description, m -> {});
	}

	public void log(@Nonnull Member member, String header, @Nonnull CharSequence description, @Nonnull File file,
	                @Nonnull String fileName, @Nonnull String format, @Nonnull Consumer<? super Message> sent) throws IOException {
		log(member.getGuild(), Embeds.construct(member, header).setDescription(description).setTimestamp(OffsetDateTime.now()).build(),
			file, fileName, format, true, sent);
	}
	public void log(@Nonnull Member member, String header, @Nonnull CharSequence description, @Nonnull File file,
	                @Nonnull String fileName, @Nonnull String format) throws IOException {
		log(member, header, description, file, fileName, format, m -> {});
	}

	public void log(@Nonnull Member member, @Nonnull Color color, String header, @Nonnull CharSequence description, @Nonnull File file,
	                @Nonnull String fileName, @Nonnull String format, Consumer<? super  Message> sent) throws IOException {
		log(member.getGuild(), Embeds.construct(member, header).setDescription(description).setTimestamp(OffsetDateTime.now()).setColor(color).build(),
		  	file, fileName, format, true, sent);
	}
	public void log(@Nonnull Member member, @Nonnull Color color, String header, @Nonnull CharSequence description, @Nonnull File file,
	                @Nonnull String fileName, @Nonnull String format) throws IOException {
		log(member, color, header, description, file, fileName, format, m -> {});
	}

}
