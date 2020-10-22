package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.ExecutionException;

import java.util.TimerTask;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public abstract class ThrowingTimerTask extends TimerTask {

	@Override
	public final void run() {
		try {
			runThrowing();
		} catch (Throwable ex) {
			throw new ExecutionException(ex);
		}
	}

	public abstract void runThrowing() throws Exception;

}
