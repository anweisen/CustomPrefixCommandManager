package net.anweisen.commandmanager.utils;

import javax.annotation.Nonnull;

/**
 * Developed in the CommandManager project
 * on 09-01-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@FunctionalInterface
public interface BiFactory<R, U, V> {

	@Nonnull
	R get(U u, V v);

}
