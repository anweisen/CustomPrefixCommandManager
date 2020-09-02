package net.anweisen.commons;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.2
 */
@FunctionalInterface
public interface Factory<R, P> {

	R get(P p);

}
