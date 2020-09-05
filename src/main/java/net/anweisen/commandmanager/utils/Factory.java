package net.anweisen.commandmanager.utils;

import javax.annotation.Nonnull;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
@FunctionalInterface
public interface Factory<R, P> {

	@Nonnull
	R get(P p);

}
