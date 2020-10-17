package net.codingarea.engine.discord.defaults;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2.1
 */
public final class DefaultBuilder {

	private DefaultBuilder() { }

	/**
	 * @return Creates a {@link JDABuilder} using every {@link GatewayIntent}
	 * @see JDABuilder
	 */
	@Nonnull
	@CheckReturnValue
	public static JDABuilder createHeavyJDA(String token) {
		return JDABuilder.create(token, allIntents());
	}

	@Nonnull
	@CheckReturnValue
	public static JDABuilder createDefaultJDA(String token) {
		return JDABuilder.createDefault(token);
	}

	@Nonnull
	@CheckReturnValue
	public static JDABuilder createJDA(String token, GatewayIntent intent, GatewayIntent... intents) {
		return JDABuilder.create(token, intent, intents);
	}

	/**
	 * @return Creates a {@link DefaultShardManagerBuilder} using every {@link GatewayIntent}
	 * @see DefaultShardManagerBuilder
	 */
	@Nonnull
	@CheckReturnValue
	public static DefaultShardManagerBuilder createHeavyShardManager(String token) {
		return DefaultShardManagerBuilder.create(token, allIntents());
	}

	@Nonnull
	@CheckReturnValue
	public static DefaultShardManagerBuilder createDefaultShardManager(String token) {
		return DefaultShardManagerBuilder.createDefault(token);
	}

	@Nonnull
	@CheckReturnValue
	public static DefaultShardManagerBuilder createDefaultShardManager(String token, GatewayIntent gatewayIntent, GatewayIntent... gatewayIntents) {
		return DefaultShardManagerBuilder.createDefault(token, gatewayIntent, gatewayIntents);
	}
	@Nonnull
	@CheckReturnValue
	public static DefaultShardManagerBuilder createShardManager(String token, GatewayIntent gatewayIntent, GatewayIntent... gatewayIntents) {
		return DefaultShardManagerBuilder.create(token, gatewayIntent, gatewayIntents);
	}


	@Nonnull
	@CheckReturnValue
	public static List<GatewayIntent> allIntents() {
		return Arrays.asList(GatewayIntent.values());
	}

}
