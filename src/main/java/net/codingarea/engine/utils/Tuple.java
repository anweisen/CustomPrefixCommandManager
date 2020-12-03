package net.codingarea.engine.utils;

import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public class Tuple<A, B> {

	public static <A, B> Tuple<A, B> ofA(@Nullable A a) {
		return new Tuple<>(a, null);
	}

	public static <A, B> Tuple<A, B> ofB(@Nullable B b) {
		return new Tuple<>(null, b);
	}

	protected A a;
	protected B b;

	public Tuple(@Nullable A a, @Nullable B b) {
		this.a = a;
		this.b = b;
	}

	@Nullable
	public A a() {
		return a;
	}

	@Nullable
	public B b() {
		return b;
	}

	public void a(@Nullable A a) {
		this.a = a;
	}

	public void b(@Nullable B b) {
		this.b = b;
	}

}
