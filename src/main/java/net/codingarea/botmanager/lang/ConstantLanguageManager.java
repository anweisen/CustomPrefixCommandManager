package net.codingarea.botmanager.lang;

import net.codingarea.botmanager.exceptions.LanguageNotFoundException;
import net.codingarea.botmanager.sql.SQL;
import net.dv8tion.jda.api.entities.Guild;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public class ConstantLanguageManager extends LanguageManager {

	protected final Language language;

	public ConstantLanguageManager(@Nonnull Language language) {
		this.language = language;
	}

	@Nonnull
	@Override
	public LanguageManager loadLanguagesFromFolder(@Nonnull File folder) {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public LanguageManager loadLanguageFromResource(@Nonnull String path) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public LanguageManager loadLanguageFromFile(@Nonnull String path) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void registerLanguage(Language language) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLanguage(Guild guild, String language) throws SQLException, LanguageNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLanguage(Guild guild, Language language) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public Language getLanguageByName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void setCached(@Nonnull String key, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCached(@Nonnull String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getFromDatabase(String key) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public synchronized String load(String key) {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public String getValue(@Nonnull String key) {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public String get(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void set(@Nonnull String key, @Nonnull String value) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void setCacheValues(boolean cacheValues) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void setClearRate(int clearRate) {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void resetCache() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getClearRate() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public String getDefaultValue() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public SQL getDataSource() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public String getKeyColumn() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public String getValueColumn() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public String getTable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean shouldCacheValues() {
		throw new UnsupportedOperationException();
	}

	@Nonnull
	@Override
	public Map<String, String> getCache() {
		throw new UnsupportedOperationException();
	}

	/*
	 * =============================
	 * Begin of supported operations
	 * =============================
	 */

	@Nonnull
	@Override
	public Language getDefaultLanguage() {
		return language;
	}

	@Nonnull
	@Override
	public Language getLanguageForGuild(Guild guild) {
		return getDefaultLanguage();
	}

	@Nonnull
	@Override
	public String getMessage(Guild guild, String key) {
		return super.getMessage(guild, key);
	}

	@Nonnull
	@Override
	public String get(Guild guild, String key) {
		return super.get(guild, key);
	}

}
