package net.codingarea.engine.discord.commandmanager.events;

import net.codingarea.engine.discord.commandmanager.CommandHandler;
import net.codingarea.engine.discord.commandmanager.ICommand;
import net.codingarea.engine.discord.commandmanager.helper.CommandHelper;
import net.codingarea.engine.exceptions.MessageException;
import net.codingarea.engine.utils.Colors;
import net.codingarea.engine.utils.function.ThrowingConsumer;
import net.codingarea.engine.utils.UtilMethod;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public interface CommandEvent {

	/*
	 * Begin of CommandInfo section
	 */

	@Nonnull
	@CheckReturnValue
	CommandHandler getHandler();

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
	default boolean isMentionPrefix() {
		return CommandHelper.containsMention(getPrefix().trim());
	}

	/*
	 * Begin of message section
	 */

	@Nonnull
	@CheckReturnValue
	Message getMessage();

	@Nonnull
	@CheckReturnValue
	default String getMessageContentRaw() {
		return getMessage().getContentRaw();
	}

	@Nonnull
	@CheckReturnValue
	default String getMessageContentDisplay() {
		return getMessage().getContentDisplay();
	}

	@Nonnull
	@CheckReturnValue
	default String getMessageContentStripped() {
		return getMessage().getContentStripped();
	}

	@Nonnull
	@CheckReturnValue
	String[] getArgs();

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

	@CheckReturnValue
	default int getArgsLength() {
		return getArgs().length;
	}

	@Nonnull
	@CheckReturnValue
	ChannelType getChannelType();

	/*
	 * Begin of guild section
	 */

	@CheckReturnValue
	Guild getGuild();

	@CheckReturnValue
	default String getGuildID() {
		return getGuild() == null ? null : getGuild().getId();
	}

	@CheckReturnValue
	default String getGuildName() {
		return getGuild() == null ? null : getGuild().getName();
	}

	@CheckReturnValue
	Member getMember();

	@UtilMethod
	@CheckReturnValue
	default Color getMemberColor() {
		return getMember() == null ? null : getMember().getColor();
	}

	@UtilMethod
	@CheckReturnValue
	default Color getSelfColor() {
		try {
			return getSelfMember().getColor();
		} catch (Exception ignored) {
			return null;
		}
	}

	@Nonnull
	@UtilMethod
	@CheckReturnValue
	default Color getSelfColorNotNull() {
		return Colors.getMemberColorNotNull(getSelfMember());
	}

	@Nonnull
	@CheckReturnValue
	User getUser();

	@Nonnull
	@CheckReturnValue
	default String getUserID() {
		return getUser().getId();
	}

	@Nonnull
	@CheckReturnValue
	JDA getJDA();

	@Nullable
	@CheckReturnValue
	default ShardManager getShardManager() {
		return getJDA().getShardManager();
	}

	@CheckReturnValue
	default boolean isWebhookMessageEvent() {
		return getMessage().isWebhookMessage();
	}

	@CheckReturnValue
	default boolean isBotMessageEvent() {
		return getMessage().getAuthor().isBot();
	}

	/**
	 * @return The {@link MessageChannel} the command was accessed from
	 */
	@Nonnull
	@CheckReturnValue
	MessageChannel getChannel();

	/**
	 * @return The {@link TextChannel} the command was access from
	 *
	 * @throws IllegalStateException
	 *         If the command was not accessed from a {@link TextChannel}
	 *
	 * @see #getChannel()
	 */
	@Nonnull
	@CheckReturnValue
	TextChannel getTextChannel();

	/**
	 * @return The {@link PrivateChannel} the command was access from
	 *
	 * @throws IllegalStateException
	 *         If the command was not accessed from a {@link PrivateChannel}
	 *
	 * @see #getChannel()
	 */
	@Nonnull
	@CheckReturnValue
	PrivateChannel getPrivateChannel();

	@Nonnull
	@CheckReturnValue
	String getMessageID();

	@Nonnull
	@CheckReturnValue
	default String getMemberID() {
		return getUserID();
	}

	@Nonnull
	@CheckReturnValue
	default String getMemberAvatarURL() {
		return getUser().getEffectiveAvatarUrl();
	}

	@Nonnull
	@CheckReturnValue
	default String getUserTag() {
		return getUser().getAsTag();
	}

	@Nonnull
	@CheckReturnValue
	default String getMemberName() {
		return isFromGuild() && getMember() != null ? getMember().getEffectiveName() : getUserName();
	}

	@Nonnull
	@CheckReturnValue
	default String getUserName() {
		return getUser().getName();
	}

	@CheckReturnValue
	boolean isPrivate();

	@CheckReturnValue
	boolean isFromGuild();

	default void queueReply(@Nonnull EmbedBuilder message) {
		queueReply(message, sent -> {});
	}

	default void queueReply(@Nonnull MessageEmbed message) {
		queueReply(message, sent -> {});
	}

	default void queueReply(@Nonnull CharSequence message) {
		queueReply(message, sent -> {});
	}

	default void queueReply(@Nonnull EmbedBuilder message, @Nonnull ThrowingConsumer<Message> sent) {
		reply(message.build()).queue(sent, MessageException::create);
	}

	default void queueReply(@Nonnull MessageEmbed message, @Nonnull ThrowingConsumer<Message> sent) {
		reply(message).queue(sent, MessageException::create);
	}

	default void queueReply(@Nonnull CharSequence message, @Nonnull ThrowingConsumer<Message> sent) {
		reply(message).queue(sent, MessageException::create);
	}

	default void queueSyntax(final @Nonnull String syntax) {
		queueReply(CommandHelper.syntax(this, syntax));
	}

	@Nonnull
	@CheckReturnValue
	default MessageAction reply(@Nonnull MessageEmbed message) {
		return getChannel().sendMessage(message);
	}

	@Nonnull
	@CheckReturnValue
	default MessageAction reply(@Nonnull CharSequence message) {
		return getChannel().sendMessage(message);
	}

	default void replyPrivate(@Nonnull EmbedBuilder embed) {
		replyPrivate(embed.build());
	}

	default void replyPrivate(@Nonnull MessageEmbed embed) {
		getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(embed).queue(message -> {}, MessageException::create);
		}, MessageException::create);
	}

	default void replyPrivate(@Nonnull CharSequence embed) {
		getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(embed).queue(message -> {}, MessageException::create);
		}, MessageException::create);
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
	 * @return Returns the first {@link TextChannel} of the {@link #getMentionedChannels() mentioned channels}
	 *         Null if empty
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
	 * @return Returns the first {@link Role} of the {@link #getMentionedRoles() mentioned roles}
	 *         Null if empty
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

	@CheckReturnValue
	default boolean senderHasPermission(@Nonnull Permission... permission) {
		if (getMember() == null) {
			return false;
		} else {
			return getMember().hasPermission(permission);
		}
	}

	@CheckReturnValue
	default String getArg(int index) {
		return getArgs()[index];
	}

	@Nonnull
	@CheckReturnValue
	default String getArgsAsString(int startIndex, int endIndex) {
		StringBuilder builder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			builder.append(getArg(i) + " ");
		}
		return builder.toString().trim();
	}

	@Nonnull
	@CheckReturnValue
	default String getArgsAsString(int startIndex) {
		return getArgsAsString(startIndex, getArgsLength());
	}

	@Nonnull
	@CheckReturnValue
	default String getArgsAsString() {
		return getArgsAsString(0);
	}

	@CheckReturnValue
	default boolean memberHasRole(@Nonnull Role role) {
		return getMember().getRoles().contains(role);
	}

	@CheckReturnValue
	default boolean memberHasRole(@Nonnull String roleID) {
		for (Role role : getMember().getRoles()) {
			if (role.getId().equals(roleID)) return true;
		}
		return false;
	}

	@CheckReturnValue
	default boolean memberHasRole(long roleID) {
		for (Role role : getMember().getRoles()) {
			if (role.getIdLong() == roleID) return true;
		}
		return false;
	}

	default void deleteMessage() {
		getMessage().delete().queue();
	}

	default void sendTyping() {
		getChannel().sendTyping().queue();
	}

	@Nonnull
	@CheckReturnValue
	default SelfUser getSelfUser() {
		return getJDA().getSelfUser();
	}

	@Nullable
	@CheckReturnValue
	default Member getSelfMember() {
		try {
			return getGuild().getSelfMember();
		} catch (Exception ignored) {
			return null;
		}
	}

	@Nonnull
	@UtilMethod
	@CheckReturnValue
	default String syntax(String syntax) {
		return syntax(this, syntax);
	}

	@Nonnull
	@UtilMethod
	@CheckReturnValue
	static String syntax(@Nonnull CommandEvent event, @Nonnull String syntax) {
		return syntax(event, syntax, true);
	}

	@Nonnull
	@UtilMethod
	@CheckReturnValue
	static String syntax(@Nonnull CommandEvent event, @Nonnull String syntax, boolean command) {
		String message = event.getPrefix() + (command ? event.getCommandName() + " " : "") + syntax;
		boolean mark = !CommandHelper.containsMention(message);
		return (mark ? "`" : "*") + message + (mark ? "`" : "*");
	}

	/**
	 * @return The {@link MessageReceivedEvent} this {@link CommandEvent} was triggered by
	 *
	 * @throws IllegalStateException
	 *         If this {@link CommandEvent} was not triggered by a {@link MessageReceivedEvent}
	 */
	@Nonnull
	@CheckReturnValue
	MessageReceivedEvent getReceiveEvent();

	/**
	 * @return The {@link MessageUpdateEvent} this {@link CommandEvent} was triggered by
	 *
	 * @throws IllegalStateException
	 *         If this {@link CommandEvent} was not triggered by a {@link MessageUpdateEvent}
	 */
	@Nonnull
	@CheckReturnValue
	MessageUpdateEvent getUpdateEvent();

	@Nonnull
	@CheckReturnValue
	GenericMessageEvent getEvent();

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

}
