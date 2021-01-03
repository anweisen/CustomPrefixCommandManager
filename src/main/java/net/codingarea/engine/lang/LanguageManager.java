package net.codingarea.engine.lang;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.discord.commandmanager.event.MessagePipeline;
import net.codingarea.engine.exceptions.UnexpectedExecutionException;
import net.codingarea.engine.lang.support.ConstantLanguageManagerSupport;
import net.codingarea.engine.lang.support.LanguageManagerSupport;
import net.codingarea.engine.lang.support.StaticLanguageManagerHolder;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.utils.Replacement;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public interface LanguageManager {

	@Nonnull
	@CheckReturnValue
	static LanguageManager getInstance() {
		return StaticLanguageManagerHolder.getInstance();
	}

	@CheckReturnValue
	static boolean hasInstance() {
		return StaticLanguageManagerHolder.hasInstance();
	}

	@Nonnull
	@CheckReturnValue
	Optional<Language> findLanguage(@Nonnull String name);

	@Nonnull
	@CheckReturnValue
	Collection<Language> getLanguages();

	@Nonnull
	LanguageManager registerLanguage(@Nonnull Language language);

	@Nonnull
	default LanguageManager registerLanguages(@Nonnull Language... languages) {
		Arrays.stream(languages).forEach(this::registerLanguage);
		return this;
	}

	@Nonnull
	@CheckReturnValue
	Language getDefaultLanguage();

	@Nonnull
	@CheckReturnValue
	LanguageManager setDefaultLanguage(@Nonnull Language language);

	@Nonnull
	@CheckReturnValue
	Language getLanguage(@Nonnull Guild guild);

	@Nonnull
	@CheckReturnValue
	default Language getLanguage(@Nonnull CommandEvent event) {
		return event.isGuild() ? getLanguage(event.getGuild()) : getDefaultLanguage();
	}

	@Nonnull
	@CheckReturnValue
	default Language getLanguage(@Nonnull MessagePipeline pipeline) {
		return pipeline.getChannel() instanceof GuildChannel ? getLanguage(((GuildChannel)pipeline.getChannel()).getGuild()) : getDefaultLanguage();
	}

	@Nonnull
	default LanguageManager setAsCurrent() {
		return StaticLanguageManagerHolder.setInstance(this);
	}

	@Nonnull
	@CheckReturnValue
	default String translate(@Nonnull CommandEvent event, @Nonnull String key, @Nonnull Replacement... replacements) {
		return getLanguage(event).translate(key, replacements);
	}

	@Nonnull
	@CheckReturnValue
	default String translate(@Nonnull CommandEvent event, @Nonnull String key, @Nonnull String fallback, @Nonnull Replacement... replacements) {
		return getLanguage(event).translate(key, fallback, replacements);
	}

	@Nonnull
	@CheckReturnValue
	default String translate(@Nonnull MessagePipeline pipeline, @Nonnull String key, @Nonnull Replacement... replacements) {
		return getLanguage(pipeline).translate(key, replacements);
	}

	@Nonnull
	@CheckReturnValue
	default String translate(@Nonnull MessagePipeline pipeline, @Nonnull String key, @Nonnull String fallback, @Nonnull Replacement... replacements) {
		return getLanguage(pipeline).translate(key, fallback, replacements);
	}

	default void setLanguage(@Nonnull Guild guild, @Nonnull Language language) {
		try {
			setLanguageExceptionally(guild, language);
		} catch (Exception ex) {
			throw new UnexpectedExecutionException(ex);
		}
	}

	void setLanguageExceptionally(@Nonnull Guild guild, @Nonnull Language language) throws Exception;


	@Nonnull
	@CheckReturnValue
	static LanguageManager constant(@Nonnull Language language) {
		return ConstantLanguageManagerSupport.create(language).setAsCurrent();
	}

	@Nonnull
	@CheckReturnValue
	static LanguageManager constant() {
		return ConstantLanguageManagerSupport.create().setAsCurrent();
	}

	@Nonnull
	@CheckReturnValue
	static LanguageManager create(@Nonnull SQL sql, @Nonnull String table, @Nonnull String idColumn, @Nonnull String languageColumn) {
		return LanguageManagerSupport.create(sql, table, idColumn, languageColumn).setAsCurrent();
	}

	@Nonnull
	@CheckReturnValue
	static LanguageManager create(@Nonnull SQL sql) {
		return LanguageManagerSupport.create(sql, "guilds", "guildID", "language").setAsCurrent();
	}

}
