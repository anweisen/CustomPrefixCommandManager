package net.codingarea.engine.utils.function;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
@FunctionalInterface
public interface TripleConsumer<A, B, C> {

	void accept(A a, B b, C c);

}
