package net.codingarea.engine.utils.config;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public interface Config {

	@CheckReturnValue
	boolean isSet(@Nonnull String key);

	@Nullable
	@CheckReturnValue
	String getString(@Nonnull String key);

	@Nonnull
	@CheckReturnValue
	default String getStringNonnull(@Nonnull String key) {
		return String.valueOf(getString(key));
	}

	@CheckReturnValue
	long getLong(@Nonnull String key);

	@CheckReturnValue
	int getInt(@Nonnull String key);

	@CheckReturnValue
	short getShort(@Nonnull String key);

	@CheckReturnValue
	byte getByte(@Nonnull String key);

	@CheckReturnValue
	double getDouble(@Nonnull String key);

	@CheckReturnValue
	float getFloat(@Nonnull String key);

	@CheckReturnValue
	boolean getBoolean(@Nonnull String key);

	@CheckReturnValue
	char getChar(@Nonnull String key);

	@Nonnull
	@CheckReturnValue
	Collection<String> keys();

	@Nonnull
	Config clear();

	@Nonnull
	Config remove(@Nullable String key);

	@CheckReturnValue
	int size();

	@CheckReturnValue
	default boolean isEmpty() {
		return size() == 0;
	}

}
