package net.codingarea.engine.lang;

import net.codingarea.engine.exceptions.NoSuchTranslationException;
import net.codingarea.engine.lang.support.LanguageSupport;
import net.codingarea.engine.utils.IAlias;
import net.codingarea.engine.utils.INamed;
import net.codingarea.engine.utils.Replacement;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public interface Language extends INamed, IAlias {

	@Nonnull
	@Override
	@CheckReturnValue
	String getName();

	@Nonnull
	@Override
	@CheckReturnValue
	String[] getAlias();

	@Nonnull
	@CheckReturnValue
	String translate(@Nonnull String key) throws NoSuchTranslationException;

	@Nonnull
	@CheckReturnValue
	default String translate(@Nonnull String key, @Nonnull String fallback, @Nonnull Replacement... replacements) {
		try {
			return translate(key, replacements);
		} catch (Exception ex) {
			return Replacement.replaceAll(fallback, replacements);
		}
	}

	@Nonnull
	@CheckReturnValue
	default String translate(@Nonnull String key, @Nonnull Replacement... replacements) throws NoSuchTranslationException {
		return Replacement.replaceAll(translate(key), replacements);
	}

	@Nonnull
	@CheckReturnValue
	static Language read(@Nonnull File file) throws IOException {
		return LanguageSupport.read(file);
	}

	@Nonnull
	@CheckReturnValue
	static Language read(@Nonnull URL url, @Nonnull String origin) throws IOException {
		return LanguageSupport.read(url, origin);
	}

	@Nonnull
	@CheckReturnValue
	static Language read(@Nonnull String path) throws IOException {
		return LanguageSupport.read(path);
	}

	@Nonnull
	@CheckReturnValue
	static Language read(@Nonnull InputStream input, @Nonnull String pathName) throws IOException {
		return LanguageSupport.read(input, pathName);
	}

}
