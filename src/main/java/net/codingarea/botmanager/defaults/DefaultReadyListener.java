package net.codingarea.botmanager.defaults;

import net.codingarea.botmanager.listener.EventHandler;
import net.codingarea.botmanager.listener.Listener;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class DefaultReadyListener implements Listener {

	private final Runnable online;

	public DefaultReadyListener(@Nonnull Runnable online) {
		this.online = online;
	}

	@EventHandler
	public void onReady(ReadyEvent event) {
		ShardManager shardManager = event.getJDA().getShardManager();
		if (shardManager == null) {
			online.run();
		} else if (shardManager.getShardsRunning() == shardManager.getShardsTotal()) {
			online.run();
		}
		event.getJDA().removeEventListener(this);
	}

}
