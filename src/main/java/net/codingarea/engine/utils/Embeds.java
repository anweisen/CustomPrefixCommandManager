package net.codingarea.engine.utils;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.discord.commandmanager.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.User;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.4
 */
public final class Embeds {

	private Embeds() { }

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull String author, String suffix, String icon, String url) {
		return new EmbedBuilder().setAuthor(title(icon, author, suffix), url, icon);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull String author, String suffix, String icon) {
		return construct(author, suffix, icon, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull String author, String suffix) {
		return construct(author, suffix, null, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull String author) {
		return construct(author, null, null, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Guild guild) {
		return construct(guild, null, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Guild guild, String suffix) {
		return construct(guild, null, suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Guild guild, String authorURL, String suffix) {
		return new EmbedBuilder().setColor(Colors.getMemberColorNotNull(guild.getSelfMember()))
								 .setAuthor(title(guild, suffix), authorURL, guild.getIconUrl());
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Member member) {
		return construct(member, null, null);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Member member, String suffix) {
		return construct(member, null, suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static EmbedBuilder construct(@Nonnull Member member, String authorURL, String suffix) {
		return new EmbedBuilder().setColor(Colors.getMemberColorNotNull(member.getGuild().getSelfMember()))
								 .setAuthor(title(member, suffix), authorURL, member.getUser().getEffectiveAvatarUrl());
	}

	@Nonnull
	@CheckReturnValue
	public static String title(String icon, String content, String suffix) {
		if (content == null) content = "";
		if (suffix == null) suffix = "";
		if (!suffix.isEmpty()) suffix = " • " + suffix;
		return (icon != null ? "» " : "") + content + suffix;
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull Guild guild, String suffix) {
		return title(guild.getIconUrl(), guild.getName(), suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull Member member, String suffix) {
		return title(member.getUser(), suffix);
	}

	@Nonnull
	@CheckReturnValue
	public static String title(@Nonnull User user, String suffix) {
		return title(user.getEffectiveAvatarUrl(), user.getName(), suffix);
	}

	public static class HelpMessageBuilder {

		public static class EmbedField {

			private final String title;
			private final List<String> lines = new ArrayList<>();

			public EmbedField(final @Nonnull String title) {
				this.title = title;
			}

			@Nonnull
			public EmbedField add(final @Nonnull String content) {
				lines.add(content);
				return this;
			}

			@Nonnull
			public EmbedField add(@Nonnull String command, @Nullable String description) {
				return add("» " + command + (description != null ? " • " + description : ""));
			}

			@Nonnull
			public EmbedField add(@Nonnull String command, @Nonnull String description,
			                      @Nonnull CommandEvent event, @Nonnull Permission... permission) {
				return event.memberHasPermission(permission) ? add(command, description) : this;
			}

			@Nonnull
			public EmbedField add(@Nonnull CommandEvent event, @Nonnull Class<? extends ICommand> commandClass) {
				ICommand command = event.getHandler().findCommand(commandClass).get();
				return add(event, command);
			}

			@Nonnull
			public EmbedField add(@Nonnull CommandEvent event, @Nonnull ICommand command) {
				return event.getHandler().hasAccess(event.getMember(), command) ?
						add(CommandEvent.syntax(event, command.getName(), false), command.getDescription()) : this;
			}

			@Nonnull
			@SafeVarargs
			public final EmbedField add(@Nonnull CommandEvent event, @Nonnull Class<? extends ICommand>... commandClasses) {
				for (Class<? extends ICommand> commandClass : commandClasses) {
					add(event, commandClass);
				}
				return this;
			}

			@Nonnull
			@CheckReturnValue
			public Field toEmbedField() {
				StringBuilder content = new StringBuilder();
				for (String line : lines) {
					content.append("\n" + line);
				}
				return new Field(title + " (" + lines.size() + ")", content.toString(), false);
			}

		}

		private final String avatar;
		private final String botName;
		private final List<EmbedField> fields = new ArrayList<>();

		private String authorURL;
		private Color color;
		private CharSequence description;

		public HelpMessageBuilder(@Nonnull CommandEvent event) {
			this(event.getJDA());
			this.color = event.getSelfColorNotNull();
		}

		public HelpMessageBuilder(@Nonnull CommandEvent event, final @Nonnull Permission... permissions) {
			this(event);
			setAuthorURL(event.getJDA(), permissions);
		}

		public HelpMessageBuilder(@Nonnull JDA jda) {
			avatar = jda.getSelfUser().getEffectiveAvatarUrl();
			botName = jda.getSelfUser().getName();
		}

		public HelpMessageBuilder(@Nonnull Member selfMember, @Nonnull Permission... permissions) {
			this(selfMember.getJDA());
			setAuthorURL(selfMember.getJDA(), permissions);
		}

		@Nonnull
		public HelpMessageBuilder setColor(@Nullable Color color) {
			this.color = color;
			return this;
		}

		@Nonnull
		public HelpMessageBuilder setColor(@Nullable String hex) {
			this.color = hex == null ? null : Color.decode(hex.startsWith("#") ? hex : "#" + hex);
			return this;
		}

		@Nonnull
		HelpMessageBuilder setAuthorURL(@Nullable String url) {
			this.authorURL = url;
			return this;
		}

		@Nonnull
		HelpMessageBuilder setAuthorURL(@Nonnull JDA jda, final @Nonnull Permission... permissions) {
			this.authorURL = jda.getInviteUrl(permissions);
			return this;
		}

		@Nonnull
		public HelpMessageBuilder setDescription(@Nonnull CharSequence description) {
			this.description = description;
			return this;
		}

		@Nonnull
		public HelpMessageBuilder addField(@Nonnull EmbedField field) {
			fields.add(field);
			return this;
		}

		@Nonnull
		public HelpMessageBuilder addField(@Nonnull EmbedField field, boolean add) {
			if (add) fields.add(field);
			return this;
		}


		@Nonnull
		@CheckReturnValue
		public EmbedBuilder embed() {

			EmbedBuilder embed = new EmbedBuilder();

			embed.setColor(color);
			embed.setDescription(description);
			embed.setAuthor(title(avatar, botName, "Information"), authorURL, avatar);

			for (EmbedField field : fields) {
				Field embedField = field.toEmbedField();
				if (embedField.getValue() != null && embedField.getValue().trim().length() > 1)
					embed.addField(embedField);
			}

			return embed;

		}

		@Nonnull
		@CheckReturnValue
		public MessageEmbed build() {
			return embed().build();
		}

	}

}
