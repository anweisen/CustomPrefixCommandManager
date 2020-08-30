package net.anweisen.commandmanager.commands;

import net.anweisen.commandmanager.CommandEvent;
import net.anweisen.commandmanager.CommandType;

/**
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 * @author anweisen
 * @since 1.2
 */

public abstract class Command implements ICommand {

	public Command(String name, String... alias) {
		this(name, false, alias);
	}

	public Command(String name, boolean processInNewThread, String... alias) {
		this.name = name;
		this.alias = alias;
		this.processInNewThread = processInNewThread;
	}

	public Command(String name, CommandType commandType, String... alias) {
		this(name, alias);
		this.type = commandType;
	}

	public Command(String name, CommandType commandType, boolean processInNewThread, String... alias) {
		this(name, processInNewThread, alias);
		this.type = commandType;
	}

	public Command(String name, CommandType commandType, boolean processInNewThread, boolean reactToMentionPrefix, String... alias) {
		this(name, commandType, processInNewThread, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	public Command(String name, boolean processInNewThread, boolean reactToMentionPrefix, String... alias) {
		this(name, processInNewThread, alias);
		this.reactToMentionPrefix = reactToMentionPrefix;
	}

	private final String name;
	private final String[] alias;

	private final boolean processInNewThread;

	private CommandType type = CommandType.GENERAL;
	private boolean reactToWebhooks = false;
	private boolean reactToBots = false;
	private boolean reactToMentionPrefix = false;

	public abstract void onCommand(CommandEvent event);

	@Override
	public final CommandType getType() {
		return type;
	}

	@Override
	public final String[] getAlias() {
		return alias;
	}

	@Override
	public final String getName() {
		return name;
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
