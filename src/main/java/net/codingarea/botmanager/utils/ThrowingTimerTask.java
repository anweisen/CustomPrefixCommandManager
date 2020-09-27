package net.codingarea.botmanager.utils;

import net.codingarea.botmanager.exceptions.ExecutionException;

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

	public abstract void runThrowing() throws Throwable;

}
