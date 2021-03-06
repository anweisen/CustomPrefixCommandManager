package net.codingarea.engine.utils;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
public final class PropertiesUtils {

	private PropertiesUtils() { }

	@Nonnull
	public static Properties readProperties(@Nonnull File file) throws IOException {
		return readProperties(file.toURI().toURL());
	}

	@Nonnull
	public static Properties readProperties(@Nonnull URL url) throws IOException {
		return readProperties(url.openStream());
	}

	@Nonnull
	public static Properties readProperties(@Nonnull InputStream input) throws IOException {
		Properties properties = new Properties();
		properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
		return properties;
	}

	public static void copyProperties(@Nonnull Properties source, @Nonnull Properties destination, @Nonnull File destinationFile) throws IOException {
		for (String currentKey : source.stringPropertyNames()) {
			String currentValue = source.getProperty(currentKey);
			destination.setProperty(currentKey, currentValue);
		}
		saveProperties(destination, destinationFile);
	}

	public static void saveProperties(@Nonnull Properties properties, @Nonnull File file) throws IOException {
		if (!file.exists()) file.createNewFile();
		Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
		properties.store(writer, null);
		writer.flush();
		writer.close();
	}

}
