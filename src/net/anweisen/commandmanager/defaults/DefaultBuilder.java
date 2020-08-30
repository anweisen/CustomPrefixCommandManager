package net.anweisen.commandmanager.defaults;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * @author anweisen
 * @since 1.2.1
 */

public final class DefaultBuilder {

	private DefaultBuilder() { }

	/**
	 * @return Creates a {@link net.dv8tion.jda.api.JDABuilder} with every {@link net.dv8tion.jda.api.requests.GatewayIntent}
	 * @see net.dv8tion.jda.api.JDABuilder
	 * @see net.dv8tion.jda.api.requests.GatewayIntent
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

	/**
	 * @return Creates a {@link net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder} with every {@link net.dv8tion.jda.api.requests.GatewayIntent}
	 * @see net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
	 * @see net.dv8tion.jda.api.requests.GatewayIntent
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
	public static List<GatewayIntent> allIntents() {
		return Arrays.asList(GatewayIntent.values());
	}

}
