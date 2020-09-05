package net.anweisen.commandmanager;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.List;
import java.util.function.Consumer;

/**
 * Developed in the CommandManager project
 * on 07-12-2020
 *
 * @see net.anweisen.commandmanager.CommandHandler
 * @see net.anweisen.commandmanager.commands.ICommand
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class CommandEvent {

	private final MessageReceivedEvent receivedEvent;
	private final String[] args;
	private final String prefix;
	private final String commandName;

	public CommandEvent(String prefix, String commandName, MessageReceivedEvent event) {

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
		return receivedEvent.getGuild();
	}

	public Member getMember() {
		return receivedEvent.getMember();
	}

	public User getUser() {
		return receivedEvent.getAuthor();
	}

	public JDA getJDA() {
		return receivedEvent.getJDA();
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

	public String getMessageID() {
		return receivedEvent.getMessageId();
	}

	public String getMemberID() {
		return receivedEvent.getAuthor().getId();
	}

	public String getMemberAvatarURL() {
		return receivedEvent.getAuthor().getEffectiveAvatarUrl();
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

	public void queueReply(MessageEmbed message) {
		reply(message).queue(ignored -> {}, exception -> {});
	}

	public void queueReply(CharSequence message) {
		reply(message).queue(ignored -> {}, exception -> {});
	}

	public void queueReply(MessageEmbed message, Consumer<Message> messageConsumer) {
		reply(message).queue(messageConsumer, exception -> {});
	}

	public void queueReply(CharSequence message, Consumer<Message> messageConsumer) {
		reply(message).queue(messageConsumer, exception -> {});
	}

	public MessageAction reply(MessageEmbed message) {
		return getChannel().sendMessage(message);
	}

	public MessageAction reply(CharSequence message) {
		return getChannel().sendMessage(message);
	}

	public void replyPrivate(MessageEmbed embed) {
		getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(embed).queue();
		});
	}

	public void replyPrivate(CharSequence sequence) {
		getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(sequence).queue();
		});
	}

	public List<Member> getMentionedMembers() {
		List<Member> mentioned = receivedEvent.getMessage().getMentionedMembers();
		try {
			mentioned.remove(receivedEvent.getGuild().getSelfMember());
		} catch (Throwable ignored) { }
		return mentioned;
	}

	public List<TextChannel> getMentionedChannels() {
		return receivedEvent.getMessage().getMentionedChannels();
	}

	public List<Role> getMentionedRoles() {
		return receivedEvent.getMessage().getMentionedRoles();
	}

	public boolean senderHasPermission(Permission... permission) {
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

	/**
	 * Used in {@link CommandEvent#syntax(CommandEvent, String, boolean)}
	 * 
	 * @return returns if the given text mentions a {@link net.dv8tion.jda.api.entities.Member}
	 * @see net.dv8tion.jda.api.entities.IMentionable
	 * @since 1.2
	 */
	public static boolean containsMention(String text) {

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

	public static String syntax(CommandEvent event, String syntax) {
		return syntax(event, syntax, true);
	}

	public static String syntax(CommandEvent event, String syntax, boolean command) {
		String message = event.getPrefix() + (command ? event.getCommandName() + " " : "") + syntax;
		boolean mark = !containsMention(message);
		return (mark ? "`" : "*") + message + (mark ? "`" : "*");
	}

}
