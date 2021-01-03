package net.codingarea.engine.discord.commandmanager.event;

import net.codingarea.engine.exceptions.MessageException;
import net.codingarea.engine.lang.LanguageManager;
import net.codingarea.engine.utils.Replacement;
import net.codingarea.engine.utils.function.ThrowingConsumer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.AttachmentOption;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public interface MessagePipeline {

	@Nonnull
	@CheckReturnValue
	static MessagePipeline create(@Nonnull Message message) {
		return new MessagePipeline() {
			@Nonnull
			@Override
			public Message getMessage() {
				return message;
			}

			@Nonnull
			@Override
			public MessageChannel getChannel() {
				return message.getChannel();
			}

			@Nonnull
			@Override
			public User getUser() {
				return message.getAuthor();
			}
		};
	}

	@Nonnull
	@CheckReturnValue
	Message getMessage();

	@Nonnull
	@CheckReturnValue
	MessageChannel getChannel();

	@Nonnull
	@CheckReturnValue
	User getUser();

	@Nonnull
	@CheckReturnValue
	default String getMessage(@Nonnull String key, @Nonnull Replacement... replacements) {
		return LanguageManager.getInstance().translate(this, key, replacements);
	}

	@Nonnull
	@CheckReturnValue
	default String getMessage(@Nonnull String key, @Nonnull String fallback, @Nonnull Replacement... replacements) {
		if (!LanguageManager.hasInstance()) return Replacement.replaceAll(fallback, replacements);
		return LanguageManager.getInstance().translate(this, key, fallback, replacements);
	}

	default void sendTyping() {
		getChannel().sendTyping().queue();
	}

	default void sendTyping(long timeout, @Nonnull TimeUnit unit) {
		getChannel().sendTyping().timeout(timeout, unit).queue();
	}

	default void deleteMessage(@Nullable Consumer<? super Void> success, @Nullable Consumer<? super Throwable> fail) {
		getMessage().delete().queue(success, fail);
	}

	default void deleteMessage(@Nullable Consumer<? super Void> success) {
		deleteMessage(success, MessageException::create);
	}

	default void deleteMessage() {
		deleteMessage(null);
	}

	default void openPrivateChannel(@Nullable Consumer<? super PrivateChannel> success, @Nullable Consumer<? super Throwable> fail) {
		queue(getUser().openPrivateChannel(), success, fail);
	}

	static <T> void queue(@Nonnull RestAction<T> action, @Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> fail) {
		action.queue(success, fail != null ? fail : MessageException::create);
	}


	default void reply(@Nonnull CharSequence message, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nullable ThrowingConsumer<? super Throwable> fail) {
		queue(getMessage().reply(message).mentionRepliedUser(false), success, fail);
	}

	default void reply(@Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nullable ThrowingConsumer<? super Throwable> fail) {
		queue(getMessage().reply(embed).mentionRepliedUser(false), success, fail);
	}

	default void reply(@Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nullable ThrowingConsumer<? super Throwable> fail) {
		queue(getMessage().reply(embed.build()).mentionRepliedUser(false), success, fail);
	}

	default void reply(@Nonnull CharSequence message, @Nullable ThrowingConsumer<? super Message> success) {
		reply(message, success, null);
	}

	default void reply(@Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success) {
		reply(embed, success, null);
	}

	default void reply(@Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success) {
		reply(embed, success, null);
	}

	default void reply(@Nonnull CharSequence message) {
		reply(message, null, null);
	}

	default void reply(@Nonnull MessageEmbed embed) {
		reply(embed, null, null);
	}

	default void reply(@Nonnull EmbedBuilder embed) {
		reply(embed, null, null);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(file, fileName, options).mentionRepliedUser(false), success, fail);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(message).mentionRepliedUser(false).addFile(file, fileName, options), success, fail);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(embed).addFile(file, fileName, options).mentionRepliedUser(false), success, fail);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		reply(file, fileName, embed.build(), success, fail, options);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(file, fileName, options).mentionRepliedUser(false), success, null);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(message).mentionRepliedUser(false).addFile(file, fileName, options), success, null);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(embed).mentionRepliedUser(false).addFile(file, fileName, options), success, null);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nonnull AttachmentOption... options) {
		reply(file, fileName, embed.build(), success, null, options);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(file, fileName, options).mentionRepliedUser(false), null, null);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(message).mentionRepliedUser(false).addFile(file, fileName, options), null, null);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nonnull AttachmentOption... options) {
		queue(getMessage().reply(embed).mentionRepliedUser(false).addFile(file, fileName, options), null, null);
	}

	default void reply(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nonnull AttachmentOption... options) {
		reply(file, fileName, embed.build(), null, null, options);
	}

	default void replyMessage(@Nonnull String key, @Nonnull Replacement... replacements) {
		reply(getMessage(key, replacements));
	}

	default void replyMessage(@Nonnull String key, @Nonnull String fallback, @Nonnull Replacement... replacements) {
		reply(getMessage(key, fallback, replacements));
	}

	default void send(@Nonnull CharSequence message, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nullable ThrowingConsumer<? super Throwable> fail) {
		queue(getChannel().sendMessage(message), success, fail);
	}

	default void send(@Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nullable ThrowingConsumer<? super Throwable> fail) {
		queue(getChannel().sendMessage(embed), success, fail);
	}

	default void send(@Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nullable ThrowingConsumer<? super Throwable> fail) {
		queue(getMessage().reply(embed.build()), success, fail);
	}

	default void send(@Nonnull CharSequence message, @Nullable ThrowingConsumer<? super Message> success) {
		send(message, success, null);
	}

	default void send(@Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success) {
		send(embed, success, null);
	}

	default void send(@Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success) {
		send(embed, success, null);
	}

	default void send(@Nonnull CharSequence message) {
		send(message, null, null);
	}

	default void send(@Nonnull MessageEmbed embed) {
		send(embed, null, null);
	}

	default void send(@Nonnull EmbedBuilder embed) {
		send(embed, null, null);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		queue(getChannel().sendFile(file, fileName, options), success, fail);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		queue(getChannel().sendMessage(message).addFile(file, fileName, options), success, fail);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		queue(getChannel().sendMessage(embed).addFile(file, fileName, options), success, fail);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		send(file, fileName, embed.build(), success, fail, options);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nullable ThrowingConsumer<? super Message> success,
	                   @Nonnull AttachmentOption... options) {
		queue(getChannel().sendFile(file, fileName, options), success, null);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nonnull AttachmentOption... options) {
		queue(getChannel().sendMessage(message).addFile(file, fileName, options), success, null);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nonnull AttachmentOption... options) {
		queue(getChannel().sendMessage(embed).addFile(file, fileName, options), success, null);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nonnull AttachmentOption... options) {
		send(file, fileName, embed.build(), success, null, options);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
		queue(getChannel().sendFile(file, fileName, options), null, null);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nonnull AttachmentOption... options) {
		queue(getChannel().sendMessage(message).addFile(file, fileName, options), null, null);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nonnull AttachmentOption... options) {
		queue(getChannel().sendMessage(embed).addFile(file, fileName, options), null, null);
	}

	default void send(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nonnull AttachmentOption... options) {
		send(file, fileName, embed.build(), null, null, options);
	}

	default void sendMessage(@Nonnull String key, @Nonnull Replacement... replacements) {
		send(getMessage(key, replacements));
	}


	default void sendPrivate(@Nonnull CharSequence message, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nullable ThrowingConsumer<? super Throwable> fail) {
		openPrivateChannel(channel -> {
			queue(channel.sendMessage(message), success, fail);
		}, fail);
	}

	default void sendPrivate(@Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nullable ThrowingConsumer<? super Throwable> fail) {
		openPrivateChannel(channel -> {
			queue(channel.sendMessage(embed), success, fail);
		}, fail);
	}

	default void sendPrivate(@Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                  @Nullable ThrowingConsumer<? super Throwable> fail) {
		sendPrivate(embed.build(), success, fail);
	}

	default void sendPrivate(@Nonnull CharSequence message, @Nullable ThrowingConsumer<? super Message> success) {
		sendPrivate(message, success, null);
	}

	default void sendPrivate(@Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success) {
		sendPrivate(embed, success, null);
	}

	default void sendPrivate(@Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success) {
		sendPrivate(embed, success, null);
	}

	default void sendPrivate(@Nonnull CharSequence message) {
		sendPrivate(message, null, null);
	}

	default void sendPrivate(@Nonnull MessageEmbed embed) {
		sendPrivate(embed, null, null);
	}

	default void sendPrivate(@Nonnull EmbedBuilder embed) {
		sendPrivate(embed, null, null);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendFile(file, fileName, options), success, fail);
		}, fail);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendMessage(message).addFile(file, fileName, options), success, fail);
		}, fail);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendMessage(embed).addFile(file, fileName, options), success, fail);
		}, fail);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nullable ThrowingConsumer<? super Throwable> fail, @Nonnull AttachmentOption... options) {
		sendPrivate(file, fileName, embed.build(), success, fail, options);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendFile(file, fileName, options), success, null);
		}, null);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendMessage(message).addFile(file, fileName, options), success, null);
		}, null);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendMessage(embed).addFile(file, fileName, options), success, null);
		}, null);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nullable ThrowingConsumer<? super Message> success,
	                         @Nonnull AttachmentOption... options) {
		sendPrivate(file, fileName, embed.build(), success, null, options);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendFile(file, fileName, options), null, null);
		}, null);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull String message, @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendMessage(message).addFile(file, fileName, options), null, null);
		}, null);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull MessageEmbed embed, @Nonnull AttachmentOption... options) {
		openPrivateChannel(channel -> {
			queue(channel.sendMessage(embed).addFile(file, fileName, options), null, null);
		}, null);
	}

	default void sendPrivate(@Nonnull File file, @Nonnull String fileName, @Nonnull EmbedBuilder embed, @Nonnull AttachmentOption... options) {
		sendPrivate(file, fileName, embed.build(), null, null, options);
	}

	default void sendMessagePrivate(@Nonnull String key, @Nonnull Replacement... replacements) {
		sendPrivate(getMessage(key, replacements));
	}

}
