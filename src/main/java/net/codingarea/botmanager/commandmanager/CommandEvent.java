package net.codingarea.botmanager.commandmanager;

import net.codingarea.botmanager.commandmanager.commands.ICommand;
import net.codingarea.botmanager.exceptions.MessageException;
import net.codingarea.botmanager.utils.Colors;
import net.codingarea.botmanager.utils.ThrowingConsumer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.internal.entities.MemberImpl;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @see CommandHandler
 * @see ICommand
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class CommandEvent {

	private final MessageReceivedEvent receivedEvent;
	private final String[] args;
	private final String prefix;
	private final String commandName;

	public CommandEvent(@Nonnull String prefix, @Nonnull String commandName, @Nonnull MessageReceivedEvent event) {

		receivedEvent = event;

		this.prefix = prefix;
		this.commandName = commandName;

		String raw = event.getMessage().getContentRaw().trim();

		if (raw.length() == (prefix + commandName).length()) {
			args = new String[0];
		} else {
			String argsRaw = raw.substring((prefix + commandName).length()).trim();
			args = argsRaw.split(" ");
		}

	}

	public ChannelType getChannelType() {
		return receivedEvent.getChannelType();
	}

	public String getCommandName() {
		return commandName;
	}

	public String getPrefix() {
		return prefix;
	}

	public boolean prefixContainsMention() {
		return containsMention(prefix.trim());
	}

	public String[] getArgs() {
		return args;
	}

	public int getArgsLength() {
		return args.length;
	}

	public String getMessageContentRaw() {
		return receivedEvent.getMessage().getContentRaw();
	}

	public Message getMessage() {
		return receivedEvent.getMessage();
	}

	public MessageReceivedEvent getReceivedEvent() {
		return receivedEvent;
	}

	public Guild getGuild() {
		try {
			return receivedEvent.getGuild();
		} catch (IllegalStateException ignored) {
			return null;
		}
	}

	public String getGuildID() {
		return getGuild().getId();
	}

	public String getGuildName() {
		return getGuild().getName();
	}

	public Member getMember() {
		return receivedEvent.getMember();
	}

	@Nullable
	public Color getMemberColor() {
		return getMember() == null ? null : getMember().getColor();
	}

	@Nullable
	public Color getSelfColor() {
		try {
			return getSelfMember().getColor();
		} catch (Exception ignored) {
			return null;
		}
	}

	@Nonnull
	public Color getSelfColorNotNull() {
		return Colors.getMemberColorNotNull(getSelfMember());
	}

	@Nonnull
	public User getUser() {
		return receivedEvent.getAuthor();
	}

	public String getUserID() {
		return getUser().getId();
	}

	@Nonnull
	public JDA getJDA() {
		return receivedEvent.getJDA();
	}

	public ShardManager getShardManager() {
		return getJDA().getShardManager();
	}

	public boolean isWebhookMessage() {
		return receivedEvent.isWebhookMessage();
	}

	public MessageChannel getChannel() {
		return receivedEvent.getChannel();
	}

	public TextChannel getTextChannel() {
		return receivedEvent.getTextChannel();
	}

	public PrivateChannel getPrivateChannel() {
		return receivedEvent.getPrivateChannel();
	}

	@Nonnull
	public String getMessageID() {
		return receivedEvent.getMessageId();
	}

	@Nonnull
	public String getMemberID() {
		return receivedEvent.getAuthor().getId();
	}

	@Nonnull
	public String getMemberAvatarURL() {
		return receivedEvent.getAuthor().getEffectiveAvatarUrl();
	}

	@Nonnull
	public String getMemberAvatarURL(int resolution) {
		return getMemberAvatarURL() + "?size=" + resolution;
	}

	@Nonnull
	public String getUserTag() {
		return getUser().getAsTag();
	}

	public String getMemberName() {
		return receivedEvent.isFromGuild() ? receivedEvent.getMember().getEffectiveName() : receivedEvent.getAuthor().getName();
	}

	public boolean isPrivate() {
		return !receivedEvent.isFromGuild();
	}

	public boolean isFromGuild() {
		return receivedEvent.isFromGuild();
	}

	public void queueReply(@Nonnull MessageEmbed message) {
		reply(message).queue(ignored -> {}, MessageException::create);
	}

	public void queueReply(@Nonnull CharSequence message) {
		reply(message).queue(ignored -> {}, MessageException::create);
	}

	public void queueReply(@Nonnull MessageEmbed message, @Nonnull ThrowingConsumer<Message> messageConsumer) {
		reply(message).queue(messageConsumer, MessageException::create);
	}

	public void queueReply(@Nonnull CharSequence message, @Nonnull ThrowingConsumer<Message> messageConsumer) {
		reply(message).queue(messageConsumer, MessageException::create);
	}

	@Nonnull
	@CheckReturnValue
	public MessageAction reply(@Nonnull MessageEmbed message) {
		return getChannel().sendMessage(message);
	}

	@Nonnull
	@CheckReturnValue
	public MessageAction reply(@Nonnull CharSequence message) {
		return getChannel().sendMessage(message);
	}

	public void replyPrivate(@Nonnull MessageEmbed embed) {
		getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(embed).queue(message -> {}, MessageException::create);
		});
	}

	public void replyPrivate(@Nonnull CharSequence message) {
		getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(message).queue(message1 -> {}, MessageException::create);
		});
	}

	public List<Member> getMentionedMembers() {
		List<Member> members = new ArrayList<>(receivedEvent.getMessage().getMentionedMembers());
		if (prefixContainsMention()) members.remove(0);
		return members;
	}

	public List<TextChannel> getMentionedChannels() {
		return receivedEvent.getMessage().getMentionedChannels();
	}

	public List<Role> getMentionedRoles() {
		return receivedEvent.getMessage().getMentionedRoles();
	}

	public TextChannel getFirstMentionedChannel() {
		if (!getMentionedChannels().isEmpty()) {
			return getMentionedChannels().get(0);
		} else {
			return null;
		}
	}

	public Role getFirstMentionedRole() {
		if (!getMentionedChannels().isEmpty()) {
			return getMentionedRoles().get(0);
		} else {
			return null;
		}
	}

	public Member getFirstMentionedMember() {
		if (!getMentionedChannels().isEmpty()) {
			return getMentionedMembers().get(0);
		} else {
			return null;
		}
	}

	public boolean senderHasPermission(@Nonnull Permission... permission) {
		return receivedEvent.getMember().hasPermission(permission);
	}

	public String getArg(int i) {
		return args[i];
	}

	public String getArgsAsString(int startIndex, int endIndex) {
		StringBuilder builder = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			builder.append(args[i] + " ");
		}
		return builder.toString().trim();
	}

	public String getArgsAsString(int startIndex) {
		return getArgsAsString(startIndex, args.length);
	}

	public String getArgsAsString() {
		return getArgsAsString(0);
	}

	public boolean memberHasRole(Role role) {
		return getMember().getRoles().contains(role);
	}

	public boolean memberHasRole(String roleID) {
		for (Role role : getMember().getRoles()) {
			if (role.getId().equals(roleID)) return true;
		}
		return false;
	}

	public boolean memberHasRole(long roleID) {
		for (Role role : getMember().getRoles()) {
			if (role.getIdLong() == roleID) return true;
		}
		return false;
	}

	public void deleteMessage() {
		getMessage().delete().queue();
	}

	public void sendTyping() {
		getChannel().sendTyping().queue();
	}

	public SelfUser getSelfUser() {
		return getJDA().getSelfUser();
	}

	public Member getSelfMember() {
		return getGuild().getSelfMember();
	}

	/**
	 * @see #syntax(CommandEvent, String)
	 */
	@Nonnull
	@CheckReturnValue
	public String syntax(String syntax) {
		return syntax(this, syntax);
	}

	/**
	 * Used in {@link CommandEvent#syntax(CommandEvent, String, boolean)}
	 * 
	 * @return returns if the given text mentions a {@link Member}
	 * @see IMentionable
	 * @since 1.2
	 */
	public static boolean containsMention(@Nonnull String text) {

		// n is a placeholder for any number
		char[] goal = "<@!nnnnnnnnnnnnnnnnnn>".toCharArray();
		int current = 0;
		for (char currentChar : text.toCharArray()) {

			boolean isInMention = false;
			char expected = goal[current];

			if (currentChar == expected) {
				isInMention = true;
			} else if (expected == 'n') {
				try {
					Integer.parseInt(String.valueOf(current));
					isInMention = true;
				} catch (NumberFormatException ignored) { }
			}

			if (isInMention) {
				current++;
			} else {
				current = 0;
			}

			if (current == goal.length) return true;

		}

		return false;

	}

	@Nonnull
	@CheckReturnValue
	public static String syntax(@Nonnull CommandEvent event, @Nonnull String syntax) {
		return syntax(event, syntax, true);
	}

	@Nonnull
	@CheckReturnValue
	public static String syntax(@Nonnull CommandEvent event, @Nonnull String syntax, boolean command) {
		String message = event.getPrefix() + (command ? event.getCommandName() + " " : "") + syntax;
		boolean mark = !containsMention(message);
		return (mark ? "`" : "*") + message + (mark ? "`" : "*");
	}

	@Nonnull
	@CheckReturnValue
	public static String removeMarkdown(@Nonnull String string) {
		return string.replace("`", "\\`")
					 .replace("_", "\\_")
					 .replace("*", "\\*");
	}

}
