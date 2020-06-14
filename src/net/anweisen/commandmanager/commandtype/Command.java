package net.anweisen.commandmanager.commandtype;

import net.anweisen.commandmanager.CommandEvent;

/**
 * @author anweisen
 * CommandManager developed on 06-04-2020
 * https://github.com/anweisen
 */

public abstract class Command {

	public enum CommandType {
		GENERAL,
		PRIVATE,
		GUILD;
	}

	protected String name;
	protected String[] alias;

	protected CommandType type = CommandType.GENERAL;
	protected boolean reactToWebhooks = false;

	public abstract void onCommand(CommandEvent event);

	public CommandType getType() {
		return type;
	}

	public boolean shouldReactToWebhooks() {
		return reactToWebhooks;
	}

	public String getName() {
		return name;
	}

	public String[] getAlias() {
		return alias;
	}
}
