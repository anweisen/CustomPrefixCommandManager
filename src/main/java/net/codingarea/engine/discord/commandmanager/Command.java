package net.codingarea.engine.discord.commandmanager;

import net.codingarea.engine.discord.commandmanager.helper.CommandHelper;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public abstract class Command extends CommandHelper implements ICommand {

	public Command(@Nonnull String name, @Nonnull String... alias) {
		this(name, false, alias);
	}

	public Command(@Nonnull String name, boolean processInNewThread, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.processInNewThread = processInNewThread;
	}

	public Command(@Nonnull String name, @Nonnull CommandType commandType, @Nonnull String... alias) {
		this(name, alias);
		this.type = commandType;
	}

	public Command(@Nonnull String name, @Nonnull Permission permission, @Nonnull String... alias) {
		this(name, alias);
		this.permission = permission;
	}

	public Command(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread, @Nonnull String... alias) {
		this(name, processInNewThread, alias);
		this.type = commandType;
	}

	public Command(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull String... alias) {
		this(name, processInNewThread, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	public Command(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread, boolean reactToMentionPrefix,
	               @Nonnull String... alias) {
		this(name, commandType, processInNewThread, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	public Command(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull Permission permission,
	               @Nonnull String... alias) {
		this(name, CommandType.GUILD, processInNewThread, reactToMentionPrefix, alias);
		this.permission = permission;
	}

	public Command(@Nonnull String name, boolean processInNewThread, Permission permission, boolean reactToWebhooks,
	               boolean reactToBots, boolean reactToMentionPrefix, boolean teamCommand, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.processInNewThread = processInNewThread;
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

	public Command(@Nonnull String name, boolean processInNewThread, @Nonnull Permission permission, boolean teamCommand, @Nonnull String... alias) {
		this.name = name;
		this.alias = alias;
		this.processInNewThread = processInNewThread;
		this.permission = permission;
		this.teamCommand = teamCommand;
		this.type = CommandType.GUILD;
	}

	private String name;
	private String[] alias;

	private boolean processInNewThread;
	private Permission permission;

	private CommandType type = CommandType.GENERAL;
	private boolean reactToWebhooks = false;
	private boolean reactToBots = false;
	private boolean reactToMentionPrefix = true;
	private boolean teamCommand;

	public abstract void onCommand(@Nonnull final CommandEvent event) throws Throwable;

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
	public Permission getPermissionNeeded() {
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
	public final boolean shouldProcessInNewThread() {
		return processInNewThread;
	}

	@Override
	public final boolean shouldReactToMentionPrefix() {
		return reactToMentionPrefix;
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
		if (type == CommandType.PRIVATE || type == CommandType.GENERAL) permission = null;
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

	protected final void setProcessInNewThread(boolean processInNewThread) {
		this.processInNewThread = processInNewThread;
	}

	protected final void setReactToMentionPrefix(boolean reactToMentionPrefix) {
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	protected final void setReactToWebhooks(boolean reactToWebhooks) {
		this.reactToWebhooks = reactToWebhooks;
	}

	protected final void setTeamCommand(boolean teamCommand) {
		this.teamCommand = teamCommand;
	}
}
