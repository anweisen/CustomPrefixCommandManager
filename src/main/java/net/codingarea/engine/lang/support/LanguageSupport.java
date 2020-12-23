package net.codingarea.engine.lang.support;

import net.codingarea.engine.exceptions.NoSuchTranslationException;
import net.codingarea.engine.lang.Language;
import net.codingarea.engine.utils.FileUtils;
import net.codingarea.engine.utils.PropertiesUtils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public class LanguageSupport implements Language {

	protected final Map<String, String> messages = new HashMap<>();
	protected final String name;
	protected final String[] alias;

	public LanguageSupport(@Nonnull String name, @Nonnull String[] alias) {
		this.name = name;
		this.alias = alias;
	}

	@Nonnull
	public synchronized LanguageSupport setMessage(@Nonnull String key, @Nonnull String value) {
		messages.put(key, value);
		return this;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public String getName() {
		return name;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public String[] getAlias() {
		return alias;
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public String translate(@Nonnull String key) throws NoSuchTranslationException {
		String translation = messages.get(key);
		if (translation == null)
			throw new NoSuchTranslationException(key);
		return translation;
	}

	@Nonnull
	@CheckReturnValue
	public static LanguageSupport read(@Nonnull File file) throws IOException {
		return read(file.toURI().toURL(), file.getName());
	}

	@Nonnull
	@CheckReturnValue
	public static LanguageSupport read(@Nonnull URL url, @Nonnull String origin) throws IOException {
		return read(url.openStream(), origin);
	}

	@Nonnull
	@CheckReturnValue
	public static LanguageSupport read(@Nonnull String path) throws IOException {
		return read(ClassLoader.getSystemResource(path), path);
	}

	@Nonnull
	@CheckReturnValue
	public static LanguageSupport read(@Nonnull InputStream input, @Nonnull String pathName) throws IOException {

		Properties properties = PropertiesUtils.readProperties(input);

		String name = properties.getProperty("name", FileUtils.getName(pathName));
		String[] alias = properties.getProperty("alias", "").split(",");

		LanguageSupport language = new LanguageSupport(name, alias);

		for (Entry<Object, Object> entry : properties.entrySet()) {
			language.setMessage(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}

		return language;

	}

}
