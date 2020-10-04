package net.codingarea.botmanager.utils;

import net.codingarea.botmanager.exceptions.ConsumeException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.5
 */
public interface ThrowingRunnable extends Runnable {

	void runThrowing() throws Throwable;

	default void run() {
		try {
			runThrowing();
		} catch (Throwable ex) {
			throw new ConsumeException(ex);
		}
	}

}
