package net.codingarea.engine.discord.commandmanager.impl;

import net.codingarea.engine.discord.commandmanager.CommandEvent;
import net.codingarea.engine.discord.commandmanager.CommandHandler;
import net.codingarea.engine.discord.commandmanager.ICommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public class CommandEventImpl implements CommandEvent {

	@Nonnull
	@CheckReturnValue
	public static CommandEvent create(final @Nonnull GenericMessageEvent event, final @Nonnull String prefix, final @Nonnull String commandName,
	                                  final @Nonnull ICommand command, final @Nonnull CommandHandler handler) {
		if (event instanceof MessageReceivedEvent) {
			MessageReceivedEvent receivedEvent = (MessageReceivedEvent) event;
			return new CommandEventImpl(event, receivedEvent.getMessage(), prefix, commandName, command, handler);
		} else if (event instanceof MessageUpdateEvent) {
			MessageUpdateEvent updateEvent = (MessageUpdateEvent) event;
			return new CommandEventImpl(event, updateEvent.getMessage(), prefix, commandName, command, handler);
		} else {
			throw new IllegalArgumentException(event.getClass() + " is not supported yet.");
		}
	}

	protected final GenericMessageEvent event;
	protected final String[] args;
	protected final String prefix;
	protected final String commandName;
	protected final Message message;
	protected final ICommand command;
	protected final CommandHandler handler;

	public CommandEventImpl(final @Nonnull GenericMessageEvent event, final @Nonnull Message message, final @Nonnull String prefix,
	                        final @Nonnull String commandName, final @Nonnull ICommand command, final @Nonnull CommandHandler handler) {
		this.event = event;
		this.prefix = prefix;
		this.commandName = commandName;
		this.command = command;
		this.message = message;
		this.handler = handler;
		this.args = CommandEvent.parseArgs(message, prefix, commandName);
	}

	@Nonnull
	@Override
	public MessageReceivedEvent getReceiveEvent() {
		if (!(event instanceof MessageReceivedEvent))
			throw new IllegalStateException();
		return (MessageReceivedEvent) event;
	}

	@Nonnull
	@Override
	public MessageUpdateEvent getUpdateEvent() {
		if (!(event instanceof MessageUpdateEvent))
			throw new IllegalStateException();
		return (MessageUpdateEvent) event;
	}

	@Nonnull
	@Override
	public ICommand getCommand() {
		return command;
	}

	@Nonnull
	@Override
	public CommandHandler getHandler() {
		return handler;
	}

	@Nonnull
	@Override
	public String getCommandName() {
		return commandName;
	}

	@Nonnull
	@Override
	public String getPrefix() {
		return prefix;
	}

	@Nonnull
	@Override
	public Message getMessage() {
		return message;
	}

	@Nonnull
	@Override
	public String[] getArgs() {
		return args;
	}

	@Nonnull
	@Override
	public ChannelType getChannelType() {
		return event.getChannelType();
	}

	@Nullable
	@Override
	public Guild getGuild() {
		try {
			return event.getGuild();
		} catch (Exception ignored) {
			return null;
		}
	}

	@Nullable
	@Override
	public Member getMember() {
		return message.getMember();
	}

	@Nonnull
	@Override
	public User getUser() {
		return message.getAuthor();
	}

	@Nonnull
	@Override
	public JDA getJDA() {
		return event.getJDA();
	}

	@Nonnull
	@Override
	public MessageChannel getChannel() {
		return event.getChannel();
	}

	@Nonnull
	@Override
	public TextChannel getTextChannel() {
		return event.getTextChannel();
	}

	@Nonnull
	@Override
	public PrivateChannel getPrivateChannel() {
		return event.getPrivateChannel();
	}

	@Nonnull
	@Override
	public String getMessageID() {
		return event.getMessageId();
	}

	@Override
	public boolean isPrivate() {
		return !event.isFromGuild();
	}

	@Override
	public boolean isFromGuild() {
		return event.isFromGuild();
	}

	@Nonnull
	@Override
	public GenericMessageEvent getEvent() {
		return event;
	}

}
