package net.codingarea.engine.utils;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public final class ScheduleTimer {

	public ScheduleTimer(@Nonnull Runnable sync) {
		this(sync, 60);
	}

	public ScheduleTimer(@Nonnull Runnable sync, int rate) {
		if (rate <= 0 || rate > 60) throw new IllegalArgumentException();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				OffsetDateTime now = OffsetDateTime.now();

				if ((now.getSecond() % rate) == 0
				  || now.getSecond() == 0 && rate == 60) {
					timer.cancel();
					sync.run();
				}

			}
		}, 0, 1);
	}
}
