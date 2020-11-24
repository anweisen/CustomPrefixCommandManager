package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.utils.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public class DefaultStatusChanger<Client> implements Bindable {

	public static final String DEFAULT_STREAM_URL = "https://www.twitch.tv/#";

	public static final class Publishers {

		private Publishers() {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@CheckReturnValue
		public static BiConsumer<ShardManager, Activity> shardManager() {
			return ShardManager::setActivity;
		}

		@Nonnull
		@CheckReturnValue
		public static BiConsumer<JDA, Activity> jda() {
			return (jda, activity) -> jda.getPresence().setActivity(activity);
		}

	}

	public static final class Generators {

		private Generators() {
			throw new UnsupportedOperationException();
		}

		@Nonnull
		@CheckReturnValue
		public static Activity activity(final @Nonnull ActivityType type, final @Nonnull String content, final @Nullable String url) {
			switch (type) {
				case WATCHING:
					return Activity.watching(content);
				case LISTENING:
					return Activity.listening(content);
				case STREAMING:
					return Activity.streaming(content, url != null ? url : DEFAULT_STREAM_URL);
				default:
					return Activity.playing(content);
			}
		}

		@Nonnull
		@CheckReturnValue
		public static <Client> Function<Client, Activity[]> of(final @Nonnull String prefix, final @Nonnull Supplier<Activity[]> generator) {
			return client -> {
				Activity[] activities = generator.get();
				for (int i = 0; i < activities.length; i++) {
					Activity current = activities[i];
					String url = current.getUrl();
					Activity created = activity(current.getType(), prefix + current.getName(), url);
					activities[i] = created;
				}
				return activities;
			};
		}

		@Nonnull
		@SafeVarargs
		@CheckReturnValue
		public static <Client> Function<Client, Activity[]> of(final @Nonnull ActivityType type, final @Nonnull String prefix,
		                                                       final @Nonnull Supplier<String>... content) {
			return client -> {
				Activity[] activities = new Activity[content.length];
				for (int i = 0; i < activities.length; i++) {
					activities[i] = activity(type, prefix + content[i].get(), null);
				}
				return activities;
			};
		}

		@Nonnull
		@CheckReturnValue
		public static <Client> Function<Client, Activity[]> of(final @Nonnull ActivityType type, final @Nonnull String prefix,
		                                                       final @Nonnull String[] content, final @Nonnull Replacement... replacements) {
			return client -> {
				Activity[] activities = new Activity[content.length];
				for (int i = 0; i < activities.length; i++) {
					activities[i] = activity(type, prefix + Replacement.replaceAll(content[i], replacements), null);
				}
				return activities;
			};
		}

		@Nonnull
		@CheckReturnValue
		public static <Client> Function<Client, Activity[]> of(final @Nonnull ActivityType type, final @Nonnull String prefix,
		                                                       final @Nonnull String... content) {
			return client -> {
				Activity[] activities = new Activity[content.length];
				for (int i = 0; i < activities.length; i++) {
					activities[i] = activity(type, prefix + content[i], null);
				}
				return activities;
			};
		}

	}

	@Nonnull
	@CheckReturnValue
	public static DefaultStatusChanger<ShardManager> forShardManager(final @Nonnull ShardManager shardManager, final @Nonnull Function<ShardManager, Activity[]> generator) {
		return new DefaultStatusChanger<>(shardManager, Publishers.shardManager(), generator);
	}

	@Nonnull
	@CheckReturnValue
	public static DefaultStatusChanger<ShardManager> forShardManager(final @Nonnull ShardManager shardManager, final @Nonnull Supplier<Activity[]> generator) {
		return new DefaultStatusChanger<>(shardManager, Publishers.shardManager(), Utils.supplierToFunction(generator));
	}

	@Nonnull
	@CheckReturnValue
	public static DefaultStatusChanger<JDA> forJDA(final @Nonnull JDA jda, final @Nonnull Function<JDA, Activity[]> generator) {
		return new DefaultStatusChanger<>(jda, Publishers.jda(), generator);
	}

	@Nonnull
	@CheckReturnValue
	public static DefaultStatusChanger<JDA> forJDA(final @Nonnull JDA jda, final @Nonnull Supplier<Activity[]> generator) {
		return new DefaultStatusChanger<>(jda, Publishers.jda(), Utils.supplierToFunction(generator));
	}

	private final BiConsumer<Client, Activity> publisher;
	private final Function<Client, Activity[]> generator;

	private final Client client;

	/**
	 * Number of seconds between changes
	 * Defaulting to 15
	 */
	private int rate = 15;

	/**
	 * Current status, counts up when the status is changed,
	 * reset to zero when it is greater or equal to the length of the array of the {@code generator}
	 */
	private int current;

	private Timer timer;

	@CheckReturnValue
	public DefaultStatusChanger(final @Nonnull Client client, final @Nonnull BiConsumer<Client, Activity> publisher, final @Nonnull Function<Client, Activity[]> generator) {
		this.client = client;
		this.publisher = publisher;
		this.generator = generator;
	}

	@Nonnull
	@CheckReturnValue
	public TimerTask createTimerTask() {
		return new TimerTask() {
			@Override
			public void run() {

				Activity[] activities = generator.apply(client);
				Validate.notEmpty(activities, "Activities cannot be empty");

				if (current >= activities.length) {
					current = 0;
				}

				Activity activity = activities[current];
				publish(activity);

				current++;

			}
		};
	}

	public void publish(final @Nullable Activity activity) {
		publisher.accept(client, activity);
	}

	/**
	 * Stops the timer using {@link #stop()}
	 * Uses the given timer and schedules the {@link #createTimerTask() created TimerTask}
	 * at {@code rate * 1000} with a delay of {@code 0}
	 *
	 * @param timer The {@link Timer} which should be used for scheduling
	 */
	public void start(final @Nonnull Timer timer) {
		stop();
		this.timer = timer;
		timer.schedule(createTimerTask(), 0, rate * 1000);
	}

	/**
	 * Executes {@link #start(Timer)} using with a new {@link Timer} instance
	 *
	 * @see #start(Timer)
	 */
	public void start() {
		start(new Timer());
	}

	/**
	 * Stops updating the status and sets the timer to {@code null}
	 */
	public void stop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * Sets the number of seconds between updating the status to the given {@code rate}
	 * To make the changes work, call the {@link #start()} method
	 *
	 * @param rate The number of seconds between updating the status
	 * @return {@code this} for chaining
	 */
	@Nonnull
	@CheckReturnValue
	public DefaultStatusChanger<Client> setRate(final int rate) {
		this.rate = rate;
		return this;
	}

	/**
	 * Sets the number of seconds between updating the status to given {@code rate}
	 * The timer will be stopped and will be started when the next optional period of time is starting
	 *
	 * For example if you set it to {@code 60}, the timer will start when the next minute begins,
	 * so that the status will be updated in the begin of every minute.
	 * If you set to {@code 5}, the timer will start when {@code 5} seconds since beginning of the current minute are over (eg.: after 5, 10, 15, ..),
	 * the status will be updated every {@code 5} seconds than
	 *
	 * @param rate The number of seconds between updating the status
	 */
	public void sync(final int rate) {
		this.rate = rate;
		stop();
		publish(null);
		new ScheduleTimer(this::start, Math.max(Math.min(rate, 60), 0));
	}

	@Nonnull
	@CheckReturnValue
	public BiConsumer<Client, Activity> getPublisher() {
		return publisher;
	}

	@Nonnull
	@CheckReturnValue
	public Function<Client, Activity[]> getGenerator() {
		return generator;
	}

	@CheckReturnValue
	public int getRate() {
		return rate;
	}

	@Nonnull
	@CheckReturnValue
	public Client getClient() {
		return client;
	}

	@Nullable
	@CheckReturnValue
	public Timer getTimer() {
		return timer;
	}

}
