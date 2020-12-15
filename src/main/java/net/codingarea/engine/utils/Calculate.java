package net.codingarea.engine.utils;

import javax.annotation.CheckReturnValue;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.9
 */
public final class Calculate {

	private Calculate() { }

	@CheckReturnValue
	public static double percentage(double total, double proportion) {
		return (proportion * 100) / total;
	}

}
