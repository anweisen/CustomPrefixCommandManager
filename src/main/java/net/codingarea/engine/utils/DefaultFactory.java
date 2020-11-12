package net.codingarea.engine.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class DefaultFactory {

	private DefaultFactory() { }

	@Nonnull
	@CheckReturnValue
	public static <T> Function<T, String> objectToString() {
		return String::valueOf;
	}

	@Nonnull
	@CheckReturnValue
	public static <T> Function<T, Integer> objectToInt() {
		return NumberConversions::toInt;
	}

	@Nonnull
	@CheckReturnValue
	public static <T extends ISnowflake> Function<T, String> mentionableToID() {
		return ISnowflake::getId;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, Integer> stringToInteger() {
		return Integer::valueOf;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, Double> stringToDouble() {
		return Double::valueOf;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, String> stringToString() {
		return string -> string;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, Role> stringToRole(Guild guild) {
		return guild::getRoleById;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, TextChannel> stringToTextChannel(Guild guild) {
		return guild::getTextChannelById;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, TextChannel> stringToTextChannel(ShardManager shardManager) {
		return shardManager::getTextChannelById;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, VoiceChannel> stringToVoiceChannel(Guild guild) {
		return guild::getVoiceChannelById;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, Member> stringToMember(Guild guild) {
		return guild::getMemberById;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, User> stringToUser(ShardManager shardManager) {
		return shardManager::getUserById;
	}

	@Nonnull
	@CheckReturnValue
	public static <T extends GuildChannel> Function<T, String> guildChannelToName() {
		return GuildChannel::getName;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<IMentionable, String> mentionableToMention() {
		return IMentionable::getAsMention;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<Enum<?>, String> enumToName() {
		return Enum::name;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<Enum<?>, Integer> enumToOrdinal() {
		return Enum::ordinal;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<Role, String> roleToName() {
		return Role::getName;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<Class<?>, String> classToSimpleName() {
		return Class::getSimpleName;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<Class<?>, String> classToName() {
		return Class::getName;
	}

	@Nonnull
	@CheckReturnValue
	public static <T extends INamed> Function<T, String> namedToName() {
		return INamed::getName;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, GuildChannel> stringToGuildChannel(final @Nonnull Guild guild) {
		return guild::getGuildChannelById;
	}

	@Nonnull
	@CheckReturnValue
	public static Function<String, GuildChannel> stringToGuildChannel(final @Nonnull JDA jda) {
		return jda::getGuildChannelById;
	}

}
