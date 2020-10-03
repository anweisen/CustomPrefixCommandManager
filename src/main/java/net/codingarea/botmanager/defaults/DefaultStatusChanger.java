package net.codingarea.botmanager.defaults;

import net.codingarea.botmanager.utils.Bindable;
import net.codingarea.botmanager.utils.Factory;
import net.codingarea.botmanager.utils.NumberFormatter;
import net.codingarea.botmanager.utils.ScheduleTimer;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public class DefaultStatusChanger implements Bindable {

	public static final String DEFAULT_STREAM_URL = "https://www.twitch.tv/#";

	@Nonnull
	@CheckReturnValue
	public static Factory<String[], ShardManager> createNewFactory(@Nonnull String prefix) {
		return manager -> {

			List<String> status = new ArrayList<>();
			status.add(prefix + NumberFormatter.MIDDLE_NUMBER.format(manager.getGuilds().size()) + " Server");
			if (manager.getGatewayIntents().contains(GatewayIntent.GUILD_MEMBERS)) {
				status.add(prefix + NumberFormatter.MIDDLE_NUMBER.format(manager.getUsers().size()) + " User");
			}

			return status.toArray(new String[0]);

		};
	}

	@Nonnull
	@CheckReturnValue
	public static Activity createActivity(ActivityType type, @Nonnull String text) {
		return createActivity(type, text, DEFAULT_STREAM_URL);
	}

	@Nonnull
	@CheckReturnValue
	public static Activity createActivity(ActivityType type, @Nonnull String text, String optional) {
		switch (type) {
			case WATCHING:
				return Activity.watching(text);
			case STREAMING:
				return Activity.streaming(text, optional);
			case LISTENING:
				return Activity.listening(text);
			default:
				return Activity.playing(text);
		}
	}

	private int index = 0;
	private int updateRate = 15;

	private final ShardManager shardManager;
	private final Factory<String[], ShardManager> status;

	private Timer timer;

	private ActivityType type = ActivityType.DEFAULT;

	@CheckReturnValue
	public DefaultStatusChanger(@Nonnull ShardManager shardManager) {
		this.shardManager = shardManager;
		this.status = createNewFactory("");
	}

	@CheckReturnValue
	public DefaultStatusChanger(@Nonnull ShardManager shardManager, @Nonnull Factory<String[], ShardManager> status) {
		this.shardManager = shardManager;
		this.status = status;
	}

	@CheckReturnValue
	public DefaultStatusChanger(int updateRate, @Nonnull ShardManager shardManager, @Nonnull Factory<String[], ShardManager> status) {
		this.updateRate = updateRate;
		this.shardManager = shardManager;
		this.status = status;
	}

	@CheckReturnValue
	public DefaultStatusChanger(@Nonnull ShardManager shardManager, @Nonnull String prefix, @Nonnull String... suffix) {
		this(shardManager, ActivityType.DEFAULT, prefix, suffix);
	}

	@CheckReturnValue
	public DefaultStatusChanger(@Nonnull ShardManager shardManager, @Nonnull ActivityType type, @Nonnull String prefix, @Nonnull String... suffix) {
		this.shardManager = shardManager;
		this.type = type;
		if (suffix.length == 0) {
			this.status = createNewFactory(prefix);
		} else {
			this.status = manager -> {
				List<String> strings = new ArrayList<>();
				for (String s : suffix) {
					strings.add((prefix + s)
							.replace("%guilds%", NumberFormatter.MIDDLE_NUMBER.format(shardManager.getGuilds().size()))
							.replace("%user%", NumberFormatter.MIDDLE_NUMBER.format(shardManager.getUsers().size())));
				}
				return strings.toArray(new String[0]);
			};
		}
	}

	public void start() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				String[] messages = status.get(shardManager);

				if (messages.length == 0) throw new IllegalArgumentException();

				index++;
				if (index >= messages.length) {
					index = 0;
				}

				shardManager.setActivity(createActivity(type, messages[index], DEFAULT_STREAM_URL));

			}
		}, 0, updateRate * 1000);
	}

	public void stop() {
		try {
			timer.cancel();
		} catch (Exception ignored) { }
	}

	public void restart() {
		stop();
		start();
	}

	public DefaultStatusChanger setType(@Nonnull ActivityType type) {
		this.type = type;
		return this;
	}

	/**
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public DefaultStatusChanger setUpdateRate(int updateRate) {
		this.updateRate = updateRate;
		restart();
		return this;
	}

	/**
	 * @return <code>this</code> for chaining
	 */
	@Nonnull
	public DefaultStatusChanger sync(int updateRate) {
		this.updateRate = updateRate;
		stop();
		shardManager.setActivity(null);
		new ScheduleTimer(this::start, Math.max(Math.min(updateRate, 60), 0));
		return this;
	}

	public ActivityType getType() {
		return type;
	}

	public ShardManager getShardManager() {
		return shardManager;
	}

	public Factory<String[], ShardManager> getStatus() {
		return status;
	}

	public int getUpdateRate() {
		return updateRate;
	}

}
