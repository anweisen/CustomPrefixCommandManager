package net.codingarea.botmanager.lang;

import net.codingarea.botmanager.exceptions.LanguageNotFoundException;
import net.codingarea.botmanager.sql.SQL;
import net.codingarea.botmanager.sql.cache.SQLValueCache;
import net.codingarea.botmanager.utils.BiFactory;
import net.codingarea.botmanager.utils.Factory;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public class LanguageManager extends SQLValueCache implements BiFactory<String, Guild, String> {

	@Nonnull
	@CheckReturnValue
	public static LanguageManager createDefault(SQL data, String defaultLanguageName, String table, String guildColumn, String languageColumn) {
		return new LanguageManager(true, defaultLanguageName, data, table, guildColumn, languageColumn, SQLValueCache.DEFAULT_CLEAR_RATE);
	}

	protected final ArrayList<Language> languages = new ArrayList<>();

	public LanguageManager(boolean cacheValues, @Nonnull String defaultValue, @Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, int clearRate) {
		super(cacheValues, defaultValue, data, table, keyColumn, valueColumn, clearRate);
	}

	public LanguageManager(@Nonnull SQL data, @Nonnull String table, @Nonnull String keyColumn, @Nonnull String valueColumn, @Nonnull String defaultValue) {
		super(data, table, keyColumn, valueColumn, defaultValue);
	}

	/**
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public LanguageManager loadLanguagesFromFolder(File folder) {
		if (!folder.exists()) throw new IllegalArgumentException("Directory does not exist");
		if (!folder.isDirectory()) throw new IllegalArgumentException("File is not a directory");
		for (File file : folder.listFiles()) {
			try {
				Language language = new Language(file);
				registerLanguage(language);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return this;
	}

	/**
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public LanguageManager loadLanguageFromResource(String path) throws IOException {
		Language language = new Language(this.getClass().getClassLoader().getResourceAsStream(path), path);
		registerLanguage(language);
		return this;
	}

	/**
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public LanguageManager loadLanguageFromFile(String path) throws IOException {
		Language language = new Language(new File(path));
		registerLanguage(language);
		return this;
	}

	public void registerLanguage(Language language) {
		languages.add(language);
	}

	public void setLanguage(Guild guild, String language) throws SQLException, LanguageNotFoundException {
		Language lang = getLanguageByName(language);
		setLanguage(guild, lang);
	}

	public void setLanguage(Guild guild, Language language) throws SQLException {
		set(guild.getId(), language.getName());
	}

	@Nonnull
	public Language getLanguageByName(String name) {
		for (Language language : languages) {
			if (language.getName().equalsIgnoreCase(name)) return language;
			for (String alias : language.getAlias()) {
				if (alias != null && alias.equalsIgnoreCase(name)) {
					return language;
				}
			}
		}
		throw new LanguageNotFoundException("No language found for name " + name);
	}

	public Language getDefaultLanguage() {
		return getLanguageByName(defaultValue);
	}

	@Nonnull
	public Language getLanguageForGuild(Guild guild) {
		if (guild == null) return getDefaultLanguage();
		String name = getValue(guild.getId());
		return getLanguageByName(name);
	}

	@Nonnull
	@CheckReturnValue
	public String getMessage(Guild guild, String key) {
		return get(guild, key);
	}

	@Nonnull
	@Override
	@CheckReturnValue
	public String get(Guild guild, String key) {
		return getLanguageForGuild(guild).get(key);
	}

}