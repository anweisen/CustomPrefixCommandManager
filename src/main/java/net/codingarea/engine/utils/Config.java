package net.codingarea.engine.utils;

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
	 * @return The {@link NamedValue} containing a value, {@code null} if not set
	 *         Never {@code null} if {@link #isSet(String)} with the same key is {@code true}
	 *
	 * @see #isSet(String)
	 */
	@Nullable
	@CheckReturnValue
	NamedValue get(final @Nonnull String key);

	@CheckReturnValue
	boolean isSet(final @Nonnull String key);


	@Nullable
	@CheckReturnValue
	String getString(final @Nonnull String key);

	@CheckReturnValue
	long getLong(final @Nonnull String key);

	@CheckReturnValue
	int getInt(final @Nonnull String key);

	@CheckReturnValue
	short getShort(final @Nonnull String key);

	@CheckReturnValue
	byte getByte(final @Nonnull String key);

	@CheckReturnValue
	double getDouble(final @Nonnull String key);

	@CheckReturnValue
	float getFloat(final @Nonnull String key);

	@CheckReturnValue
	boolean getBoolean(final @Nonnull String key);

	@Nonnull
	@CheckReturnValue
	Collection<NamedValue> entries();

}
