package net.codingarea.botmanager.utils;

import javax.annotation.Nonnull;

/**
 * This class is used to generate a object by using another object
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
@FunctionalInterface
public interface Factory<R, P> {

	@Nonnull
	R get(P p);

}
