package net.codingarea.engine.utils.function;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 *
 * @see java.util.function.BiConsumer
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface TripleConsumer<A, B, C> {

	void accept(A a, B b, C c);

}
