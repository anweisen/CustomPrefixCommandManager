package net.codingarea.engine.utils.config;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public interface Config extends Iterable<NamedValue> {

	/**
	 * @param key The of the value
	 * @return The {@link NamedValue} containing a value, {@code null} if not set.
	 *         Never {@code null} if {@link #isSet(String)} with the same key is {@code true}
	 *
	 * @see #isSet(String)
	 */
	@Nullable
	@CheckReturnValue
	NamedValue get(@Nonnull String key);

	@CheckReturnValue
	boolean isSet(@Nonnull String key);

	@Nullable
	@CheckReturnValue
	String getString(@Nonnull String key);

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

	@Nonnull
	@CheckReturnValue
	Collection<NamedValue> entries();

	void clear();

}
