package net.codingarea.engine.discord.commandmanager.event;

import net.codingarea.engine.discord.commandmanager.ICommand;
import net.codingarea.engine.discord.commandmanager.ICommandHandler;
import net.codingarea.engine.discord.commandmanager.helper.CommandHelper;
import net.codingarea.engine.utils.Colors;
import net.codingarea.engine.utils.InviteManager;
import net.codingarea.engine.utils.Utils;
import net.codingarea.engine.utils.function.ThrowingConsumer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.ShardInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.User.UserFlag;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public interface CommandEvent extends MessagePipeline {


	//
	// General information about the execution
	// (handler, command, prefix, async, ...)
	//

	@Nonnull
	@CheckReturnValue
	ICommandHandler getHandler();

	@Nonnull
	@CheckReturnValue
	ICommand getCommand();

	@Nonnull
	@CheckReturnValue
	String getCommandName();

	@Nonnull
	@CheckReturnValue
	String getPrefix();

	@CheckReturnValue
	boolean isMentionPrefix();

	@CheckReturnValue
	boolean isAsyncExecution();

	@CheckReturnValue
	boolean isGuild();

	@CheckReturnValue
	boolean isPrivate();

	@CheckReturnValue
	boolean isBot();

	@CheckReturnValue
	boolean isWebHook();

	//
	// General info about the bot
	// (jda, shardmanager, shard, ...)
	//

	@Nonnull
	@CheckReturnValue
	JDA getJDA();

	@Nonnull
	@CheckReturnValue
	default SelfUser getSelfUser() {
		return getJDA().getSelfUser();
	}

	@Nonnull
	@CheckReturnValue
	default Member getSelfMember() {
		return getGuild().getSelfMember();
	}

	@Nullable
	@CheckReturnValue
	default Member getSelfMemberNullable() {
		try {
			return getSelfMember();
		} catch (IllegalStateException ex) {
			return null;
		}
	}

	@Nullable
	@CheckReturnValue
	default ShardManager getShardManager() {
		return getJDA().getShardManager();
	}

	@Nonnull
	@CheckReturnValue
	default ShardInfo getShardInfo() {
		return getJDA().getShardInfo();
	}

	@CheckReturnValue
	default int getShardID() {
		return getShardInfo().getShardId();
	}

	@CheckReturnValue
	default int getTotalShards() {
		return getShardInfo().getShardTotal();
	}

	//
	// Information about the event
	//

	@Nonnull
	@CheckReturnValue
	GenericMessageEvent getEvent();

	@Nonnull
	@CheckReturnValue
	default MessageUpdateEvent getUpdateEvent() {
		if (!(getEvent() instanceof MessageUpdateEvent))
			throw new IllegalStateException();
		return (MessageUpdateEvent) getEvent();
	}

	@Nonnull
	@CheckReturnValue
	default MessageReceivedEvent getReceivedEvent() {
		if (!(getEvent() instanceof MessageReceivedEvent))
			throw new IllegalStateException();
		return (MessageReceivedEvent) getEvent();
	}

	//
	// Information about the sender
	// (user, member, color, id, ...)
	//

	@Nonnull
	@Override
	@CheckReturnValue
	User getUser();

	/**
	 * @throws IllegalStateException
	 *         If the command was not triggered by a {@link Member} / not in a {@link Guild}
	 */
	@Nonnull
	@CheckReturnValue
	Member getMember();

	@Nullable
	@CheckReturnValue
	default Member getMemberNullable() {
		try {
			return getMember();
		} catch (IllegalStateException ex) {
			return null;
		}
	}

	@CheckReturnValue
	default boolean memberHasPermission(@Nonnull Permission... permission) {
		return getMember().hasPermission(permission);
	}

	@CheckReturnValue
	default boolean memberHasRole(@Nonnull Role role) {
		return getMember().getRoles().contains(role);
	}

	@CheckReturnValue
	default boolean memberHasRole(long roleID) {
		return getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == roleID);
	}

	@CheckReturnValue
	default boolean memberHasRole(@Nonnull String roleID) {
		return getMember().getRoles().stream().anyMatch(role -> role.getId().equals(roleID));
	}

	@Nonnull
	@CheckReturnValue
	default String getUserID() {
		return getUser().getId();
	}

	@CheckReturnValue
	default long getUserIDLong() {
		return getUser().getIdLong();
	}

	@Nonnull
	@CheckReturnValue
	default String getUserTag() {
		return getUser().getAsTag();
	}

	@Nonnull
	@CheckReturnValue
	default String getUserAsMention() {
		return getUser().getAsMention();
	}

	@Nonnull
	@CheckReturnValue
	default String getUserName() {
		return getUser().getName();
	}

	@Nonnull
	@CheckReturnValue
	default String getEffectiveUserName() {
		return getMemberNullable() != null ? getMember().getEffectiveName() : getUserName();
	}

	@Nonnull
	@CheckReturnValue
	default String getUserAvatarURL() {
		return getUser().getEffectiveAvatarUrl();
	}

	@Nonnull
	@CheckReturnValue
	default EnumSet<UserFlag> getUserFlags() {
		return getUser().getFlags();
	}

	//
	// Information about guild
	// (guild, id, name, icon, ...)
	//

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a guild
	 */
	@Nonnull
	@CheckReturnValue
	Guild getGuild();

	@Nullable
	@CheckReturnValue
	default Guild getGuildNullable() {
		try {
			return getGuild();
		} catch (IllegalStateException ex) {
			return null;
		}
	}

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a guild
	 */
	@Nonnull
	@CheckReturnValue
	default String getGuildID() {
		return getGuild().getId();
	}

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a guild
	 */
	@CheckReturnValue
	default long getGuildIDLong() {
		return getGuild().getIdLong();
	}

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a guild
	 */
	@Nonnull
	@CheckReturnValue
	default String getGuildName() {
		return getGuild().getName();
	}

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a guild
	 */
	@Nullable
	@CheckReturnValue
	default String getGuildIconURL() {
		return getGuild().getIconUrl();
	}

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a guild
	 */
	@Nullable
	@CheckReturnValue
	default String getGuildBannerURL() {
		return getGuild().getBannerUrl();
	}

	/**
	 * @throws IllegalStateException
	 *         If the command was not triggered in a guild
	 */
	default void createGuildInvite(@Nonnull ThrowingConsumer<? super String> created) {
		InviteManager.getInvite(getGuild(), getTextChannel(), created);
	}

	//
	// Channel information
	// (channel, guildchannel, privatechannel, ...)
	//

	@Nonnull
	@Override
	@CheckReturnValue
	MessageChannel getChannel();

	@Nonnull
	@CheckReturnValue
	default ChannelType getChannelType() {
		return getChannel().getType();
	}

	@CheckReturnValue
	default boolean isFrom(@Nonnull ChannelType type) {
		return getEvent().isFromType(type);
	}

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a private chat
	 */
	@Nonnull
	@CheckReturnValue
	default PrivateChannel getPrivateChannel() {
		if (!(getChannel() instanceof PrivateChannel))
			throw new IllegalStateException();
		return (PrivateChannel) getChannel();
	}

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a guild
	 */
	@Nonnull
	@CheckReturnValue
	default GuildChannel getGuildChannel() {
		if (!(getChannel() instanceof GuildChannel))
			throw new IllegalStateException();
		return (GuildChannel) getChannel();
	}

	/**
	 * @throws IllegalStateException
	 *         If the event was not triggered in a guild
	 */
	@Nonnull
	@CheckReturnValue
	default TextChannel getTextChannel() {
		if (!(getChannel() instanceof TextChannel))
			throw new IllegalStateException();
		return (TextChannel) getChannel();
	}

	//
	// Message information
	// (message, id, content, args, ...)
	//

	@Nonnull
	@Override
	@CheckReturnValue
	Message getMessage();

	@Nonnull
	@CheckReturnValue
	default String getMessageID() {
		return getMessage().getId();
	}

	@CheckReturnValue
	default long getMessageIDLong() {
		return getMessage().getIdLong();
	}

	@Nonnull
	@CheckReturnValue
	default String getContentDisplay()  {
		return getMessage().getContentDisplay();
	}

	@Nonnull
	@CheckReturnValue
	default String getContentRaw()  {
		return getMessage().getContentRaw();
	}

	@Nonnull
	@CheckReturnValue
	default String getContentStripped()  {
		return getMessage().getContentStripped();
	}

	@Nonnull
	@CheckReturnValue
	String[] getArgs();

	@CheckReturnValue
	default int getArgsLength() {
		return getArgs().length;
	}

	@Nonnull
	@CheckReturnValue
	default String[] getArgs(int startIndex) {
		return getArgs(startIndex, getArgsLength());
	}

	@Nonnull
	@CheckReturnValue
	default String[] getArgs(int startIndex, int endIndex) {
		List<String> args = new ArrayList<>();
		for (int i = startIndex; i < endIndex; i++) {
			args.add(getArg(i));
		}
		return args.toArray(new String[0]);
	}

	@Nonnull
	@CheckReturnValue
	default String getArg(int index) {
		return getArgs()[index];
	}

	@Nonnull
	@CheckReturnValue
	default String getArgsAsString() {
		return Utils.arrayToString(getArgs());
	}

	@Nonnull
	@CheckReturnValue
	default String getArgsAsString(int startIndex) {
		return Utils.arrayToString(getArgs(), startIndex);
	}

	@Nonnull
	@CheckReturnValue
	default String getArgsAsString(int startIndex, int endIndex) {
		return Utils.arrayToString(getArgs(), startIndex, endIndex);
	}

	@Nonnull
	@CheckReturnValue
	default List<Member> getMentionedMembers() {
		List<Member> members = new ArrayList<>(getMessage().getMentionedMembers());
		if (isMentionPrefix()) members.remove(0);
		return members;
	}

	@Nonnull
	@CheckReturnValue
	default List<TextChannel> getMentionedChannels() {
		return getMessage().getMentionedChannels();
	}

	@Nonnull
	@CheckReturnValue
	default List<Role> getMentionedRoles() {
		return getMessage().getMentionedRoles();
	}

	/**
	 * @return Returns the first {@link TextChannel} of the {@link #getMentionedChannels() mentioned channels}.
	 *         {@code null} if empty
	 *
	 * @see #getMentionedChannels()
	 */
	@Nullable
	@CheckReturnValue
	default TextChannel getFirstMentionedChannel() {
		if (getMentionedChannels().isEmpty()) {
			return null;
		} else {
			return getMentionedChannels().get(0);
		}
	}

	/**
	 * @return Returns the first {@link Role} of the {@link #getMentionedRoles() mentioned roles}.
	 *          {@code null} if empty
	 *
	 * @see #getMentionedRoles()
	 */
	@Nullable
	@CheckReturnValue
	default Role getFirstMentionedRole() {
		if (getMentionedRoles().isEmpty()) {
			return null;
		} else {
			return getMentionedRoles().get(0);
		}
	}

	/**
	 * @return Returns the first {@link Member} of the {@link #getMentionedMembers() mentioned members}
	 *         {@code null} if empty
	 *
	 * @see #getMentionedMembers()
	 */
	@Nullable
	@CheckReturnValue
	default Member getFirstMentionedMember() {
		if (getMentionedMembers().isEmpty()) {
			return null;
		} else {
			return getMentionedMembers().get(0);
		}
	}

	//
	// Message actions
	//

	default void replySyntax(@Nonnull CharSequence syntax) {
		reply(CommandHelper.syntax(this, syntax));
	}

	default void deleteMessage() {
		getMessage().delete().queue();
	}

	//
	// Util methods
	// (colors)
	//

	@Nullable
	@CheckReturnValue
	default Color getMemberColor() {
		return getMemberNullable() == null ? null : getMember().getColor();
	}

	@Nonnull
	@CheckReturnValue
	default Color getMemberColorNotNull() {
		return Colors.getMemberColorNotNull(getSelfMember());
	}

	@Nullable
	@CheckReturnValue
	default Color getSelfColor() {
		try {
			return getSelfMember().getColor();
		} catch (IllegalStateException ignored) {
			return null;
		}
	}

	@Nonnull
	@CheckReturnValue
	default Color getSelfColorNotNull() {
		return Colors.getMemberColorNotNull(getSelfMember());
	}

	//
	// Static methods
	// (utils)
	//

	@Nonnull
	@CheckReturnValue
	static String[] parseArgs(@Nonnull Message message, @Nonnull String prefix, @Nonnull String command) {

		String raw = message.getContentRaw().trim();
		if (raw.length() == (prefix + command).length()) {
			return new String[0];
		} else {
			String argsRaw = raw.substring((prefix + command).length()).trim();
			return argsRaw.split(" ");
		}

	}

	@Nonnull
	@CheckReturnValue
	default String syntax(@Nonnull CharSequence syntax) {
		return syntax(this, syntax);
	}

	@Nonnull
	@CheckReturnValue
	static String syntax(@Nonnull CommandEvent event, @Nonnull CharSequence syntax) {
		return syntax(event, syntax, true);
	}

	@Nonnull
	@CheckReturnValue
	static String syntax(@Nonnull CommandEvent event, @Nonnull CharSequence syntax, boolean command) {
		String prefix = event.getHandler().getPrefixProvider().getPrefix(event);
		String message = prefix + (command ? event.getCommandName() + " " : "") + syntax;
		boolean mark = !CommandHelper.containsMention(message);
		return (mark ? "`" : "*") + CommandHelper.removeMarkdown(message, mark) + (mark ? "`" : "*");
	}

}
