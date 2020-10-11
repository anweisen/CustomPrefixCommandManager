package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
@FunctionalInterface
public interface INamed {

	@Nonnull
	@CheckReturnValue
	String getName();

}
