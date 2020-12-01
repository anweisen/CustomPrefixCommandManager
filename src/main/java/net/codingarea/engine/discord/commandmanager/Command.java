package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.discord.commandmanager.event.CommandEvent;
import net.codingarea.engine.discord.commandmanager.helper.CommandHelper;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.Nullable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public abstract class Command extends CommandHelper implements ICommand {

	public Command() { }

	public Command(@Nonnull String name, @Nonnull String... alias) {
		this(name, false, alias);
	}

	public Command(@Nonnull String name, boolean async, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.async = async;
	}

	public Command(@Nonnull String name, @Nonnull CommandType commandType, @Nonnull String... alias) {
		this(name, alias);
		this.type = commandType;
	}

	public Command(@Nonnull String name, @Nonnull Permission permission, @Nonnull String... alias) {
		this(name, alias);
		this.permission = permission;
		this.type = CommandType.GUILD;
	}

	public Command(@Nonnull String name, @Nonnull CommandType commandType, boolean async, @Nonnull String... alias) {
		this(name, async, alias);
		this.type = commandType;
	}

	public Command(@Nonnull String name, boolean async, boolean reactToMentionPrefix, @Nonnull String... alias) {
		this(name, async, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	public Command(@Nonnull String name, @Nonnull CommandType commandType, boolean async, boolean reactToMentionPrefix,
	               @Nonnull String... alias) {
		this(name, commandType, async, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	public Command(@Nonnull String name, boolean async, boolean reactToMentionPrefix, @Nonnull Permission permission,
	               @Nonnull String... alias) {
		this(name, CommandType.GUILD, async, reactToMentionPrefix, alias);
		this.permission = permission;
	}

	public Command(@Nonnull String name, boolean async, Permission permission, boolean reactToWebhooks,
	               boolean reactToBots, boolean reactToMentionPrefix, boolean teamCommand, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.async = async;
		this.reactToWebhooks = reactToWebhooks;
		this.reactToBots = reactToBots;
		this.reactToMentionPrefix = reactToMentionPrefix;
		this.teamCommand = teamCommand;
		this.permission = permission;
		this.type = CommandType.GUILD;
	}

	public Command(@Nonnull String name, @Nonnull Permission permission, boolean teamCommand, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.permission = permission;
		this.teamCommand = teamCommand;
		this.type = CommandType.GUILD;
	}

	public Command(@Nonnull String name, boolean async, @Nonnull Permission permission, boolean teamCommand, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.async = async;
		this.permission = permission;
		this.teamCommand = teamCommand;
		this.type = CommandType.GUILD;
	}

	public Command(@Nonnull String name, boolean async, @Nonnull Permission permission, boolean reactToWebhooks,
	               boolean reactToBots, boolean reactToMentionPrefix, boolean teamCommand, boolean executeOnUpdate, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.async = async;
		this.permission = permission;
		this.type = CommandType.GUILD;
		this.reactToWebhooks = reactToWebhooks;
		this.reactToBots = reactToBots;
		this.reactToMentionPrefix = reactToMentionPrefix;
		this.teamCommand = teamCommand;
		this.executeOnUpdate = executeOnUpdate;
	}

	public Command(@Nonnull String name, boolean async, @Nonnull CommandType type,
	               boolean teamCommand, boolean executeOnUpdate, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.async = async;
		this.type = type;
		this.teamCommand = teamCommand;
		this.executeOnUpdate = executeOnUpdate;
	}

	private String name;
	private String description;
	private String[] alias;

	private boolean async;
	private Permission permission;

	private CommandType type = CommandType.GENERAL;
	private boolean reactToWebhooks = false;
	private boolean reactToBots = false;
	private boolean reactToMentionPrefix = true;
	private boolean teamCommand = false;
	private boolean executeOnUpdate = true;

	public abstract void onCommand(@Nonnull final CommandEvent event) throws Exception;

	@Nonnull
	@Override
	public final CommandType getType() {
		return type;
	}

	@Nonnull
	@Override
	public final String[] getAlias() {
		return alias;
	}

	@Nonnull
	@Override
	public final String getName() {
		return name;
	}

	@Nullable
	@Override
	public final Permission getPermissionNeeded() {
		return permission;
	}

	@Override
	public final boolean shouldReactToWebhooks() {
		return reactToWebhooks;
	}

	@Override
	public final boolean shouldReactToBots() {
		return reactToBots;
	}

	@Override
	public final boolean isAsync() {
		return async;
	}

	@Override
	public final boolean shouldReactToMentionPrefix() {
		return reactToMentionPrefix;
	}

	@Override
	public final boolean shouldReactOnEdit() {
		return executeOnUpdate;
	}

	@Override
	public final boolean isTeamCommand() {
		return teamCommand;
	}

	protected final void setReactToBots(boolean reactToBots) {
		this.reactToBots = reactToBots;
	}

	protected final void setType(@Nonnull CommandType type) {
		this.type = type;
		if (type == CommandType.PRIVATE || type == CommandType.GENERAL)
			permission = null;
	}

	protected final void setPermission(Permission permission) {
		this.permission = permission;
		if (permission != null) type = CommandType.GUILD;
	}

	protected final void setAlias(@Nonnull String[] alias) {
		this.alias = alias;
	}

	protected final void setName(@Nonnull String name) {
		this.name = name;
	}

	protected final void setAsync(boolean async) {
		this.async = async;
	}

	protected final void setReactToMentionPrefix(boolean reactToMentionPrefix) {
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	protected final void setReactToWebhooks(boolean reactToWebhooks) {
		this.reactToWebhooks = reactToWebhooks;
	}

	protected final void setTeamCommand(boolean teamCommand) {
		if (teamCommand && permission == null) permission = Permission.ADMINISTRATOR;
		this.teamCommand = teamCommand;
	}

	public final void setExecuteOnUpdate(boolean executeOnUpdate) {
		this.executeOnUpdate = executeOnUpdate;
	}

	@Nullable
	@Override
	@CheckReturnValue
	public String getDescription() {
		return description;
	}

	protected final void setDescription(@Nullable CharSequence description) {
		this.description = description == null ? null : String.valueOf(description);
	}

}
