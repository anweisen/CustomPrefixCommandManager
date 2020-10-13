package net.codingarea.engine.utils.function;

import javax.annotation.Nonnull;

/**
 * This is used to generate a object by using another object
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
@FunctionalInterface
public interface Factory<R, P> {

	@Nonnull
	R get(P p);

}
