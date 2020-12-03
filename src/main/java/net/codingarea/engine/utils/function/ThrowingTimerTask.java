package net.codingarea.engine.utils.function;

import net.codingarea.engine.exceptions.UnexpectedExecutionException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.TimerTask;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 *
 * @see TimerTask
 */
public abstract class ThrowingTimerTask extends TimerTask {

	public abstract void runExceptionally() throws Exception;

	@Override
	public final void run() {
		try {
			runExceptionally();
		} catch (Throwable ex) {
			throw new UnexpectedExecutionException(ex);
		}
	}

	@Nonnull
	@CheckReturnValue
	public static ThrowingTimerTask of(@Nonnull TimerTask timerTask) {
		return new ThrowingTimerTask() {
			@Override
			public void runExceptionally() throws Exception {
				timerTask.run();
			}
		};
	}

}
