package net.codingarea.botmanager.utils;

import net.dv8tion.jda.api.OnlineStatus;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class Icon {

	private Icon() { }

	public static final String

			ONLINE = "https://cdn.discordapp.com/emojis/739897757556408442.png?v=1",
			DO_NOT_DISTURB = "https://cdn.discordapp.com/emojis/739897765336973344.png?v=1",
			IDLE = "https://cdn.discordapp.com/emojis/739897763902521415.png?v=1",
			OFFLINE = "https://cdn.discordapp.com/emojis/739897759057969353.png?v=1";

	public static String getIconByStatus(OnlineStatus status) {
		switch (status) {
			case IDLE:
				return IDLE;
			case ONLINE:
				return ONLINE;
			case DO_NOT_DISTURB:
				return DO_NOT_DISTURB;
			default:
				return OFFLINE;
		}
	}

}
