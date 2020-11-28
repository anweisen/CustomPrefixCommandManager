package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.5
 *
 * @see java.lang.Runnable
 */
@FunctionalInterface
public interface ThrowingRunnable extends Runnable {

	void runExceptionally() throws Exception;

	default void run() {
		try {
			runExceptionally();
		} catch (Throwable ex) {
			throw new ConsumeException(ex);
		}
	}

}
