package net.codingarea.botmanager.utils;

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
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				if (OffsetDateTime.now().getSecond() == 0) {
					timer.cancel();
					sync.run();
				}

			}
		}, 0, 10);
	}
}
