package net.anweisen.sql.source;

import net.anweisen.config.NamedValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Developed in the CommandManager project
 * on 09-01-2020
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LinkAttachment extends NamedValue {

	public static final LinkAttachment[] DEFAULT = {
		new LinkAttachment("autoReconnect", true),
		new LinkAttachment("serverTimezone", "UTC")	,
		new LinkAttachment("useJDBCCompliantTimezoneShift", true),
		new LinkAttachment("useLegacyDatetimeCode", true)
	};

	public LinkAttachment(@Nonnull String label, boolean value) {
		this(label, String.valueOf(value));
	}

	public LinkAttachment(@Nonnull String label, @Nonnull String value) {
		super(label, value);
	}

	@Nonnull
	public static String list(@Nonnull Iterable<LinkAttachment> attachments) {
		StringBuilder string = new StringBuilder();
		for (LinkAttachment attachment : attachments) {
			if (string.length() == 0) {
				string.append("?");
			} else {
				string.append("&");
			}
			string.append(attachment.toString());
		}
		return string.toString();
	}

	@Override
	public String toString() {
		return key + '=' + value;
	}

}
