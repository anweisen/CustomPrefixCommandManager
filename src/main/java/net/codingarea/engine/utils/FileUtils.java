package net.codingarea.engine.utils;

import net.codingarea.engine.exceptions.MessageException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class FileUtils {

	private FileUtils() { }

	@Nonnull
	@CheckReturnValue
	public static File createTempFile(@Nonnull String format) throws IOException {
		File folder = new File("./temp");
		if (!folder.exists()) folder.mkdir();
		File file = new File(folder, UUID.randomUUID().toString() + "." + format);
		file.createNewFile();
		return file;
	}

	public static void send(@Nonnull MessageChannel channel, @Nonnull String fileName, @Nonnull String format,
	                        @Nonnull File file, boolean delete, @Nonnull Consumer<? super Message> sent) {
		fileName += "." + format;
		channel.sendFile(file, fileName).queue(message -> {
			sent.accept(message);
			if (delete) file.delete();
		}, MessageException::create);
	}

	public static void send(@Nonnull MessageChannel channel, @Nonnull String fileName, @Nonnull String format,
	                        @Nonnull File file, boolean delete) {
		send(channel, fileName, format, file, delete, m -> {});
	}

	public static void send(@Nonnull MessageChannel channel, @Nonnull CharSequence text, @Nonnull String fileName,
	                        @Nonnull String format, @Nonnull File file,
	                        boolean delete, @Nonnull Consumer<? super Message> sent) throws IOException {
		fileName += "." + format;
		channel.sendMessage(text).addFile(file, fileName).queue(message -> {
			sent.accept(message);
			if (delete) file.delete();
		}, MessageException::create);
	}

	public static void send(@Nonnull MessageChannel channel, @Nonnull CharSequence text, @Nonnull String fileName,
	                        @Nonnull String format, @Nonnull File file, boolean delete) throws IOException {
		send(channel, text, fileName, format, file, delete, m -> {});
	}

	public static void send(@Nonnull MessageChannel channel, @Nonnull MessageEmbed embed, @Nonnull String fileName,
	                        @Nonnull String format, @Nonnull File file, boolean delete, @Nonnull Consumer<? super Message> sent) throws IOException {
		fileName += "." + format;
		channel.sendMessage(embed).addFile(file, fileName).queue(message -> {
			sent.accept(message);
			if (delete) file.delete();
		}, MessageException::create);
	}

	public static void send(@Nonnull MessageChannel channel, @Nonnull MessageEmbed embed, @Nonnull String fileName,
	                        @Nonnull String format, @Nonnull File file, boolean delete) throws IOException {
		send(channel, embed, fileName, format, file, delete, m -> {});
	}

	@Nonnull
	@CheckReturnValue
	public static String getName(@Nonnull File file) {
		return getName(file.getName());
	}

	@Nonnull
	@CheckReturnValue
	public static String getName(@Nonnull String path) {
		int dot = path.indexOf(".");
		return (dot > 0) ? path.substring(0, dot) : path;
	}

}
