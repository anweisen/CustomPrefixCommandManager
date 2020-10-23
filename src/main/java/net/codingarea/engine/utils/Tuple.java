package net.codingarea.engine.utils;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public class Tuple<A, B> {

	protected A a;
	protected B b;

	public Tuple(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	public void a(A a) {
		this.a = a;
	}

	public void b(B b) {
		this.b = b;
	}

}
