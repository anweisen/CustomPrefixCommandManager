package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ConsumeException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

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


	@Nonnull
	@CheckReturnValue
	static ThrowingRunnable of(@Nonnull Runnable runnable) {
		return runnable::run;
	}

}
