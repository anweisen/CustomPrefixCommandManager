package net.codingarea.engine.utils.function;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
@FunctionalInterface
public interface BiFactory<R, A, B> {

	@Nonnull
	R get(A a, B b);

}
