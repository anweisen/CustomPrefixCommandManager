package net.codingarea.engine.discord.commandmanager.event;

import net.codingarea.engine.discord.commandmanager.ICommand;
import net.codingarea.engine.discord.commandmanager.ICommandHandler;
import net.codingarea.engine.discord.commandmanager.helper.CommandHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public class CommandEventImpl implements CommandEvent {

	protected final ICommandHandler handler;
	protected final ICommand command;
	protected final String commandName, prefix;
	protected final boolean mentionPrefix;
	protected final boolean async, fromGuild, bot, webhook;
	protected final GenericMessageEvent event;
	protected final JDA jda;
	protected final User user;
	protected final Member member;
	protected final Guild guild;
	protected final MessageChannel channel;
	protected final Message message;
	protected final String[] args;

	public CommandEventImpl(@Nonnull ICommandHandler handler, @Nonnull ICommand command,
	                        @Nonnull String commandName, @Nonnull String prefix, boolean mentionPrefix,
	                        @Nonnull GenericMessageEvent event) {
		if (!(event instanceof MessageUpdateEvent || event instanceof MessageReceivedEvent))
			throw new IllegalStateException();
		this.handler = handler;
		this.command = command;
		this.commandName = commandName;
		this.prefix = prefix;
		this.mentionPrefix = mentionPrefix;
		this.async = command.isAsync();
		this.fromGuild = event.isFromGuild();
		this.event = event;
		this.jda = event.getJDA();
		this.guild = event.getGuild();
		this.channel = event.getChannel();
		this.message = CommandHelper.getMessage(event);
		this.member = CommandHelper.getMember(event);
		this.args = CommandEvent.parseArgs(message, prefix, commandName);
		this.user = message.getAuthor();
		this.bot = user.isBot();
		this.webhook = message.isWebhookMessage();
	}

	@Nonnull
	@Override
	public ICommandHandler getHandler() {
		return handler;
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

	@Override
	public boolean isMentionPrefix() {
		return mentionPrefix;
	}

	@Override
	public boolean isAsyncExecution() {
		return async;
	}

	@Override
	public boolean isGuild() {
		return fromGuild;
	}

	@Override
	public boolean isPrivate() {
		return !fromGuild;
	}

	@Override
	public boolean isBot() {
		return bot;
	}

	@Override
	public boolean isWebHook() {
		return webhook;
	}

	@Nonnull
	@Override
	public JDA getJDA() {
		return jda;
	}

	@Nonnull
	@Override
	public GenericMessageEvent getEvent() {
		return event;
	}

	@Nonnull
	@Override
	public User getUser() {
		return user;
	}

	@Nonnull
	@Override
	public Member getMember() {
		if (member == null)
			throw new IllegalStateException();
		return member;
	}

	@Nonnull
	@Override
	public Guild getGuild() {
		return guild;
	}

	@Nonnull
	@Override
	public MessageChannel getChannel() {
		return channel;
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

}
