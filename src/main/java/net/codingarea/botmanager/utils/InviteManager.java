package net.codingarea.botmanager.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.InviteAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.3
 */
public final class InviteManager {

	private InviteManager() { }

	public static TextChannel getChannel(@Nonnull Guild guild, @Nullable TextChannel defaultChannel) {
		if (defaultChannel != null) return defaultChannel;
		defaultChannel = guild.getDefaultChannel();
		if (defaultChannel != null) return defaultChannel;
		return guild.getSystemChannel();
	}

	public static void getInvite(@Nonnull Guild guild, @Nonnull Consumer<String> invite) {
		getInvite(guild, null, invite);
	}

	public static void getInvite(@Nonnull Guild guild, @Nullable TextChannel channel, @Nonnull Consumer<String> invite) {

		// If the server has a vanity url, we'll use that one
		if (guild.getVanityUrl() != null) {
			invite.accept(guild.getVanityUrl());
			return;
		}

		// We'll set a text channel, if not one was given
		channel = getChannel(guild, channel);
		if (channel == null) {
			invite.accept(null);
			return;
		}

		try {

			// We iterate through all existing invites for this channel and if the invite is not temporary and has infinite uses we use it.
			// If we found none, we'll create a new one.
			TextChannel finalChannel = channel;
			channel.retrieveInvites().queue(invites -> {
				for (Invite current : invites) {
					if (current.getMaxAge() == 0 && !current.isTemporary() && current.getMaxUses() == 0) {
						invite.accept(current.getUrl());
						return;
					}
				}
				createInvite(finalChannel, invite);
			}, throwable -> createInvite(finalChannel, invite));

		 } catch (Exception ignored) {
			// When something goes wrong getting an existing invite, we'll just create a new one
			createInvite(channel, invite);
		}

	}

	public static void createInvite(@Nonnull TextChannel channel, @Nonnull Consumer<String> invite) {
		InviteAction action = channel.createInvite().setTemporary(false).setUnique(false).setMaxAge(0);
		action.queue(success -> invite.accept(success == null ? null : success.getUrl()), ex -> invite.accept(null));
	}

}
