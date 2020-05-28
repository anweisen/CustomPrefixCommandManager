package net.anweisen.commandmanager;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * @author anweisen
 * CommandManager developed on 05-25-2020
 * Website: www.anweisen.net
 */

public class CommandEvent {

    private MessageReceivedEvent receivedEvent;
    private String[] args;
    private String prefix;
    private String commandName;

    protected CommandEvent(String prefix, String commandName, MessageReceivedEvent event) {

        receivedEvent = event;

        this.prefix = prefix;
        this.commandName = commandName;

        String raw = event.getMessage().getContentRaw();

        if (raw.length() == (prefix + commandName).length()) {
            args = new String[0];
        } else {
            String argsRaw = raw.substring((prefix + commandName).length());
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
        return receivedEvent.getMember().getId();
    }

    public String getMemberAvatarURL() {
        return receivedEvent.getAuthor().getEffectiveAvatarUrl();
    }

    public String getMemberName() {
        return receivedEvent.getMember().getEffectiveName();
    }

    public boolean isPrivate() {
        return !receivedEvent.isFromGuild();
    }

    public boolean isFromGuild() {
        return receivedEvent.isFromGuild();
    }

    public void reply(String message) {
        receivedEvent.getChannel().sendMessage(message).queue();
    }

    public void reply(MessageEmbed embed) {
        receivedEvent.getChannel().sendMessage(embed).queue();
    }

    public void reply(CharSequence sequence) {
        receivedEvent.getChannel().sendMessage(sequence).queue();
    }

    public void replyPrivate(String message) {
        getUser().openPrivateChannel().queue(channel -> {
            channel.sendMessage(message).queue();
        });
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
        return receivedEvent.getMessage().getMentionedMembers();
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

}
