package net.codingarea.engine.lang.support;

import net.codingarea.engine.lang.Language;
import net.codingarea.engine.lang.LanguageManager;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public class ConstantLanguageManagerSupport implements LanguageManager {

	protected Language language;

	@Nonnull
	@Override
	public Optional<Language> findLanguage(@Nonnull String name) {
		return Optional.ofNullable(language);
	}

	@Nonnull
	@Override
	public Collection<Language> getLanguages() {
		return Collections.singletonList(language);
	}

	@Nonnull
	@Override
	public ConstantLanguageManagerSupport registerLanguage(@Nonnull Language language) {
		return setDefaultLanguage(language);
	}

	@Nonnull
	@Override
	public Language getDefaultLanguage() {
		return language;
	}

	@Nonnull
	@Override
	public ConstantLanguageManagerSupport setDefaultLanguage(@Nonnull Language language) {
		this.language = language;
		return this;
	}

	@Nonnull
	@Override
	public Language getLanguage(@Nonnull Guild guild) {
		return language;
	}

	@Override
	public void setLanguage(@Nonnull Guild guild, @Nonnull Language language) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLanguageExceptionally(@Nonnull Guild guild, @Nonnull Language language) throws Exception {
		throw new UnsupportedOperationException();
	}



	@CheckReturnValue
	@Nonnull
	public static ConstantLanguageManagerSupport create() {
		return new ConstantLanguageManagerSupport();
	}

	@Nonnull
	@CheckReturnValue
	public static ConstantLanguageManagerSupport create(@Nonnull Language language) {
		return new ConstantLanguageManagerSupport().setDefaultLanguage(language);
	}

}
