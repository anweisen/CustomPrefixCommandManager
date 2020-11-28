package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ExecutionException;

import java.util.TimerTask;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 *
 * @see TimerTask
 */
public abstract class ThrowingTimerTask extends TimerTask {

	@Override
	public final void run() {
		try {
			runExceptionally();
		} catch (Throwable ex) {
			throw new ExecutionException(ex);
		}
	}

	public abstract void runExceptionally() throws Exception;

}
