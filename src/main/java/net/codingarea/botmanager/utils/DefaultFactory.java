package net.codingarea.botmanager.utils;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

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
	public static <T extends ISnowflake> Factory<String, T> mentionableToID() {
		return ISnowflake::getId;
	}

	@Nonnull
	@CheckReturnValue
	public static Factory<Integer, String> stringToInteger() {
		return Integer::parseInt;
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
	public static Factory<VoiceChannel, String> stringToVoiceChannel(Guild guild) {
		return guild::getVoiceChannelById;
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

}
