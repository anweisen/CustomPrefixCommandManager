package net.anweisen.commandmanager.defaults;

import net.anweisen.commandmanager.utils.Bindable;
import net.anweisen.commandmanager.utils.Factory;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class DefaultStatusChanger implements Bindable {

	@Nonnull
	@CheckReturnValue
	public static Factory<String[], ShardManager> createNewFactory(@Nonnull String prefix) {
		return manager -> {

			List<String> status = new ArrayList<>();
			status.add(prefix + manager.getGuilds().size() + " Server");
			if (manager.getGatewayIntents().contains(GatewayIntent.GUILD_MEMBERS)) {
				status.add(prefix + manager.getUsers().size() + " User");
			}

			return status.toArray(new String[0]);

		};
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

	private final Timer timer = new Timer();

	private ActivityType type = ActivityType.DEFAULT;

	public DefaultStatusChanger(@Nonnull ShardManager shardManager) {
		this.shardManager = shardManager;
		this.status = createNewFactory("");
	}

	public DefaultStatusChanger(@Nonnull ShardManager shardManager, @Nonnull Factory<String[], ShardManager> status) {
		this.shardManager = shardManager;
		this.status = status;
	}

	public DefaultStatusChanger(int updateRate, @Nonnull ShardManager shardManager, @Nonnull Factory<String[], ShardManager> status) {
		this.updateRate = updateRate;
		this.shardManager = shardManager;
		this.status = status;
	}

	public DefaultStatusChanger(@Nonnull ShardManager shardManager, @Nonnull String prefix, @Nonnull String... suffix) {
		this(shardManager, ActivityType.DEFAULT, prefix, suffix);
	}

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
							.replace("%guilds%", String.valueOf(shardManager.getGuilds().size()))
							.replace("%user%", String.valueOf(shardManager.getUsers().size())));
				}
				return strings.toArray(new String[0]);
			};
		}
	}

	public void start() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				String[] messages = status.get(shardManager);

				if (messages.length == 0) throw new IllegalArgumentException();

				index++;
				if (index >= messages.length) {
					index = 0;
				}

				shardManager.setActivity(createActivity(type, messages[index], "https://twitch.tv/"));

			}
		}, 0, updateRate * 1000);
	}

	public void stop() {
		timer.cancel();
	}

	public void restart() {
		stop();
		start();
	}

	public void setType(@Nonnull ActivityType type) {
		this.type = type;
	}

	public void setUpdateRate(int updateRate) {
		this.updateRate = updateRate;
		restart();
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
