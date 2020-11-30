package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.8
 */
@FunctionalInterface
public interface IAlias {

	@Nonnull
	@CheckReturnValue
	String[] getAlias();

}
