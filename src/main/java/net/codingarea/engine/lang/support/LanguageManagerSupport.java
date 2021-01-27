package net.codingarea.engine.lang.support;

import net.codingarea.engine.lang.Language;
import net.codingarea.engine.lang.LanguageManager;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.sql.cache.SQLValueCache;
import net.codingarea.engine.utils.INamed;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public class LanguageManagerSupport extends SQLValueCache implements LanguageManager {

	protected final List<Language> languages = new ArrayList<>();
	protected Language defaultLanguage;

	public LanguageManagerSupport(@Nonnull SQL sql, @Nonnull String table, @Nonnull String idColumn, @Nonnull String languageColumn) {
		super(sql, table, idColumn, languageColumn, null);
	}

	@Nonnull
	@Override
	public Collection<Language> getLanguages() {
		return new ArrayList<>(languages);
	}

	@Nonnull
	@Override
	public LanguageManagerSupport registerLanguage(@Nonnull Language language) {
		languages.add(language);
		if (defaultLanguage == null) defaultLanguage = language;
		return this;
	}

	@Nonnull
	@Override
	public Optional<Language> findLanguage(@Nonnull String name) {
		return languages.stream().filter(language -> INamed.matches(language, name)).findFirst();
	}

	@Nonnull
	@Override
	public Language getDefaultLanguage() {
		return defaultLanguage;
	}

	@Nonnull
	@Override
	public LanguageManagerSupport setDefaultLanguage(@Nonnull Language defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
		this.defaultValue = defaultLanguage.getName();
		return this;
	}

	@Nonnull
	@Override
	public Language getLanguage(@Nonnull Guild guild) {
		String languageName = get(guild.getId());
		Optional<Language> language = findLanguage(languageName);
		return language.orElse(defaultLanguage);
	}

	@Override
	public void setLanguageExceptionally(@Nonnull Guild guild, @Nonnull Language language) throws SQLException {
		set(guild.getId(), language.getName());
	}


	@Nonnull
	@CheckReturnValue
	public static LanguageManagerSupport create(@Nonnull SQL sql, @Nonnull String table, @Nonnull String idColumn, @Nonnull String languageColumn) {
		return new LanguageManagerSupport(sql, table, idColumn, languageColumn);
	}

	@Nonnull
	@CheckReturnValue
	public static LanguageManagerSupport create(@Nonnull SQL sql) {
		return new LanguageManagerSupport(sql, "guilds", "guildID", "language");
	}

}
