package net.anweisen.commandmanager.commands;

import net.anweisen.commandmanager.CommandEvent;
import net.anweisen.commandmanager.CommandType;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public abstract class Command implements ICommand {

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

	public Command(@Nonnull String name, @Nonnull CommandType commandType, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull String... alias) {
		this(name, commandType, processInNewThread, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	public Command(@Nonnull String name, boolean processInNewThread, boolean reactToMentionPrefix, @Nonnull Permission permission, @Nonnull String... alias) {
		this(name, CommandType.GUILD, processInNewThread, reactToMentionPrefix, alias);
		this.permission = permission;
	}

	private final String name;
	private final String[] alias;

	private final boolean processInNewThread;
	private Permission permission;

	private CommandType type = CommandType.GENERAL;
	private boolean reactToWebhooks = false;
	private boolean reactToBots = false;
	private boolean reactToMentionPrefix = true;

	public abstract void onCommand(@Nonnull final CommandEvent event);

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

}
