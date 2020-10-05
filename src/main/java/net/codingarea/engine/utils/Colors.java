package net.codingarea.engine.utils;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.awt.*;

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
			STREAMING = decode("#573591"),

			EMBED = decode("#2F3136"),
			NO_RANK = decode("#CCD8DE"),

			TRANSPARENT = new Color(0, 0, 0, 0),
			LIGHT_BLACK = decode("#1c1c1c");

	@Nonnull
	@CheckReturnValue
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

	@Nonnull
	@CheckReturnValue
	public static String asHTML(@Nonnull Color color) {

		String red = Integer.toHexString(color.getRed());
		String green = Integer.toHexString(color.getGreen());
		String blue = Integer.toHexString(color.getBlue());

		return "#" + (red.length() == 1 ? "0" + red : red) +
					 (green.length() == 1 ? "0" + green : green) +
					 (blue.length() == 1 ? "0" + blue : blue);

	}

	@Nonnull
	@CheckReturnValue
	public static Color getMemberColorNotNull(Member member) {
		if (member == null) return EMBED;
		Color color = member.getColor();
		return color != null ? color : EMBED;
	}

}
