package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.listener.Listener;
import net.codingarea.engine.utils.log.LogHelper;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.6
 */
public class DefaultServerCountDisplay implements Listener {

	private final String channelID, name;
	private final ShardManager shardManager;
	private Timer timer;

	@CheckReturnValue
	public DefaultServerCountDisplay(@Nonnull ShardManager shardManager, @Nonnull String channelID, @Nonnull String name) {
		this.channelID = channelID;
		this.name = name;
		this.shardManager = shardManager;
	}

	public void stop() {
		try {
			timer.cancel();
		} catch (Exception ignored) { }
		timer = null;
	}

	public void start() {
		stop();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					GuildChannel channel = shardManager.getVoiceChannelById(channelID);
					String name = DefaultServerCountDisplay.this.name.replace("%server%", String.valueOf(shardManager.getGuilds().size()));
					if (!channel.getName().equals(name))
						channel.getManager().setName(name).queue();
				} catch (Exception ex) {
					LogHelper.warning("Could not update server count due to an exception: " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
				}
			}
		}, 5*1000, 10*60*1000 + 60*1000);
	}

}
