package net.anweisen.commandmanager;

import net.anweisen.commandmanager.commandtype.Command;
import net.anweisen.commandmanager.commandtype.Command.CommandType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author anweisen
 * CommandManager developed on 05-25-2020
 * Website: www.anweisen.net
 */

public class CommandHandler {

    public enum MessageReactBehavior {
        REACT_NEVER,
        REACT_IF_COMMAND_WANTS,
        REACT_ALWAYS;
    }

    private MessageReactBehavior reactToWebhooks = MessageReactBehavior.REACT_IF_COMMAND_WANTS;
    private MessageReactBehavior reactToBots = MessageReactBehavior.REACT_IF_COMMAND_WANTS;
    private Collection<Command> commands;

    public CommandHandler() {

        commands = new ArrayList<>();

    }

    /**
     * @throws IllegalArgumentException if the command is null
     */
    public void registerCommand(Command command) {

        if (command == null) throw new IllegalArgumentException("Command cannot be null!");

        commands.add(command);

    }

    public void unregisterAllCommands() {

        commands.clear();

    }

    /**
     * @return returns null when no command is registered with this name
     */
    public Command getCommand(String name) {

        for (Command currentCommand : commands) {
            if (currentCommand.getName() == null) continue;
            if (name.toLowerCase().startsWith(currentCommand.getName().toLowerCase())) return currentCommand;
            if (currentCommand.getAlias() == null) continue;
            for (String currentAlias : currentCommand.getAlias()) {
                if (name.toLowerCase().startsWith(currentAlias.toLowerCase())) return currentCommand;
            }
        }

        return null;

    }

    private String getCommandName(Command command, String raw) {

        if (raw.toLowerCase().startsWith(command.getName().toLowerCase())) {
            return command.getName();
        }

        if (command.getAlias() != null) {
            for (String currentAlias : command.getAlias()) {
                if (currentAlias == null) continue;
                if (raw.toLowerCase().startsWith(currentAlias.toLowerCase())) return currentAlias;
            }
        }

        return null;

    }

    /**
     * Returns a list with all commands registered.
     * It will return a empty list when there are no commands registered
     * @return a list with all commands
     */
    public Collection<Command> getCommands() {
        return new ArrayList<>(this.commands);
    }

    /**
     * @return returns
     *    CommandResult.COMMAND_NOT_FOUND if no command was found
     *    CommandResult.INVALID_CHANNEL_GUILD_COMMAND if a guild command was triggered in a private channel
     *    CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND if a private command was triggered in a guild channel
     *    CommandResult.PREFIX_NOT_USED if the prefix was not used ^^
     *    CommandResult.WEBHOOK_MESSAGE_NO_REACT if the command was triggered by a webhook and react to webhooks is disabled
     *
     * @param prefix the prefix which should be in front of the command
     * @param event the command event the command was received
     */
    public CommandResult handleCommand(String prefix, MessageReceivedEvent event) {

        String raw = event.getMessage().getContentRaw().toLowerCase();

        if (!raw.startsWith(prefix)) return CommandResult.PREFIX_NOT_USED;

        String commandName = raw.substring(prefix.length());
        Command command = getCommand(commandName);

        if (command == null) return CommandResult.COMMAND_NOT_FOUND;

        if (reactToWebhooks != MessageReactBehavior.REACT_ALWAYS && event.isWebhookMessage() && (!command.shouldReactToWebhooks() || reactToWebhooks == MessageReactBehavior.REACT_NEVER)) {
            return CommandResult.WEBHOOK_MESSAGE_NO_REACT;
        } else if (reactToBots != MessageReactBehavior.REACT_ALWAYS && event.getAuthor().isBot() && (!command.shouldReactToBots() || reactToBots == MessageReactBehavior.REACT_NEVER)) {
            return CommandResult.BOT_MESSAGE_NO_REACT;
        } else if (command.getType() != null && command.getType() == CommandType.GUILD && !event.isFromGuild()) {
            return CommandResult.INVALID_CHANNEL_GUILD_COMMAND;
        } else if (command.getType() != null && command.getType() == CommandType.PRIVATE && event.isFromGuild()) {
            return CommandResult.INVALID_CHANNEL_PRIVATE_COMMAND;
        }

        command.onCommand(new CommandEvent(prefix, getCommandName(command, commandName), event));

        return CommandResult.SUCCESS;

    }


    public void setReactToWebhooks(MessageReactBehavior reactToWebhooks) {
        this.reactToWebhooks = reactToWebhooks;
    }

    public void setReactToBots(MessageReactBehavior reactToBots) {
        this.reactToBots = reactToBots;
    }

    public MessageReactBehavior getReactToBots() {
        return reactToBots;
    }

    public MessageReactBehavior getReactToWebhooks() {
        return reactToWebhooks;
    }
}
