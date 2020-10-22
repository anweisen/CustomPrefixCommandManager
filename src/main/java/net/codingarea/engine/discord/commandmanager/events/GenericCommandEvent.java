package net.codingarea.engine.discord.commandmanager.events;

import com.sun.org.apache.bcel.internal.generic.ICONST;
import net.codingarea.engine.discord.commandmanager.CommandEvent;
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
public abstract class GenericCommandEvent implements CommandEvent {

	@Nonnull
	@CheckReturnValue
	public static CommandEvent ofEvent(@Nonnull GenericMessageEvent event, @Nonnull String prefix,
	                                   @Nonnull String commandName, @Nonnull ICommand command) {
		if (event instanceof MessageUpdateEvent) {
			return new UpdatedCommandEvent((MessageUpdateEvent) event, prefix, commandName, command);
		} else if (event instanceof MessageReceivedEvent) {
			return new ReceivedCommandEvent((MessageReceivedEvent) event, prefix, commandName, command);
		} else {
			throw new IllegalArgumentException(event.getClass().getName() + " is not supported!");
		}
	}

	protected final GenericMessageEvent event;
	protected final String[] args;
	protected final String prefix;
	protected final String commandName;
	protected final Message message;
	protected final ICommand command;

	public GenericCommandEvent(@Nonnull GenericMessageEvent event, @Nonnull Message message, @Nonnull String prefix,
	                           @Nonnull String commandName, @Nonnull ICommand command) {
		this.event = event;
		this.prefix = prefix;
		this.commandName = commandName;
		this.command = command;
		this.message = message;
		this.args = CommandEvent.parseArgs(message, prefix, commandName);
	}

	@Nonnull
	@Override
	public ICommand getCommand() {
		return command;
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

	@Override
	public TextChannel getTextChannel() {
		return event.getTextChannel();
	}

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
