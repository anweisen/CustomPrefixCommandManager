package net.codingarea.botmanager.utils;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class DefaultFactory {

	private DefaultFactory() { }

	@Nonnull
	@CheckReturnValue
	public static <T> Factory<String, T> objectToString() {
		return String::valueOf;
	}

	@Nonnull
	@CheckReturnValue
	public static <T> Factory<Integer, T> objectToInt() {
		return NumberConversions::toInt;
	}

	@Nonnull
	@CheckReturnValue
	public static <T extends ISnowflake> Factory<String, T> mentionableToID() {
		return ISnowflake::getId;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<Integer, String> stringToInteger() {
		return Integer::valueOf;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<Double, String> stringToDouble() {
		return Double::valueOf;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<String, String> stringToString() {
		return string -> string;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<Role, String> stringToRole(Guild guild) {
		return guild::getRoleById;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<TextChannel, String> stringToTextChannel(Guild guild) {
		return guild::getTextChannelById;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<TextChannel, String> stringToTextChannel(ShardManager shardManager) {
		return id -> {
			for (Guild guild : shardManager.getGuilds()) {
				for (TextChannel channel : guild.getTextChannels()) {
					if (channel.getId().equals(id)) {
						return channel;
					}
				}
			}
			throw new NoSuchElementException("There is no textchannel with the given id on the given guild");
		};
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<VoiceChannel, String> stringToVoiceChannel(Guild guild) {
		return guild::getVoiceChannelById;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<Member, String> stringToMember(Guild guild) {
		return guild::getMemberById;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<User, String> stringToUser(ShardManager shardManager) {
		return shardManager::getUserById;
	}

	@Nonnull
	@CheckReturnValue
	public static <T extends GuildChannel> Factory<String, T> guildChannelToName() {
		return GuildChannel::getName;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<String, IMentionable> mentionableToMention() {
		return IMentionable::getAsMention;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<String, Enum<?>> enumToName() {
		return Enum::name;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<Integer, Enum<?>> enumToOrdinal() {
		return Enum::ordinal;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<String, Role> roleToName() {
		return Role::getName;
	}

}
