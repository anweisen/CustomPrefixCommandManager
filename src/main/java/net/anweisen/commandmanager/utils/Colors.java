package net.anweisen.commandmanager.utils;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

import static java.awt.Color.decode;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.2
 */
public final class Colors {

	private Colors() { }

	public static final Color

			ONLINE = decode("#40AC7B"),
			DO_NOT_DISTURB = decode("#E84444"),
			IDLE = decode("#F09F19"),
			OFFLINE = decode("#747F8D"),

			EMBED = decode("#2F3136");

	public static Color getColorForStatus(OnlineStatus status) {
		switch (status) {
			case ONLINE:
				return ONLINE;
			case DO_NOT_DISTURB:
				return DO_NOT_DISTURB;
			case IDLE:
				return IDLE;
			default:
				return OFFLINE;
		}
	}

	public static Color getColorForMember(@Nonnull Member member) {
		List<Role> roles = member.getRoles();
		if (roles.isEmpty()) return null;
		for (Role role : roles) {
			if (role == null) continue;
			Color color = role.getColor();
			if (color != null) return color;
		}
		return null;
	}

	@Nonnull
	@CheckReturnValue
	public static String toHTML(@Nonnull Color color) {

		String red = Integer.toHexString(color.getRed());
		String green = Integer.toHexString(color.getGreen());
		String blue = Integer.toHexString(color.getBlue());

		return "#" +
				(red.length() == 1 ? "0" + red : red) +
				(green.length() == 1 ? "0" + green : green) +
				(blue.length() == 1 ? "0" + blue : blue);

	}

}
