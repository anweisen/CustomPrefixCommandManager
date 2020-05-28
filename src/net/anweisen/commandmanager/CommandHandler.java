package net.anweisen.commandmanager;

import net.anweisen.commandmanager.commandtype.Command;
import net.anweisen.commandmanager.commandtype.Command.AdvancedCommand;
import net.anweisen.commandmanager.commandtype.Command.AdvancedCommand.AdvancedGuildCommand;
import net.anweisen.commandmanager.commandtype.Command.AdvancedCommand.AdvancedPrivateCommand;
import net.anweisen.commandmanager.commandtype.Command.SimpleCommand;
import net.anweisen.commandmanager.commandtype.Command.SimpleCommand.SimpleGuildCommand;
import net.anweisen.commandmanager.commandtype.Command.SimpleCommand.SimplePrivateCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author anweisen
 * CommandManager developed on 05-25-2020
 * Website: www.anweisen.net
 */

public class CommandHandler {

    private HashMap<String, Command> commands;

    public CommandHandler() {

        commands = new HashMap<>();

    }


    @Deprecated
    public void registerCommand(String name, Command command) {

        if (name == null) throw new IllegalArgumentException("Command name cannot be null!");
        if (command == null) throw new IllegalArgumentException("Command cannot be null!");
        if (!commandIsValid(command)) throw new IllegalArgumentException("The command is not simple and not advanced!");

        commands.put(name.toLowerCase(), command);

    }

    public void registerCommand(Command command, String... name) {

        if (name == null) throw new IllegalArgumentException("Command name(s) cannot be null!");
        if (command == null) throw new IllegalArgumentException("Command cannot be null!");
        if (!commandIsValid(command)) throw new IllegalArgumentException("The command is not simple and not advanced command!");

        for (String currentCommandName : name) {
            commands.put(currentCommandName.toLowerCase(), command);
        }

    }

    public void addAlias(String commandName, String... alias) {

        if (commandName == null) throw new IllegalArgumentException("Command name cannot be null!");
        if (alias == null) throw new IllegalArgumentException("Alias cannot be null!");

        Command command;

        if ((command = getCommand(commandName.toLowerCase())) == null) throw new IllegalStateException("No command registered :: " + commandName.toLowerCase());

        for (String currentAlias : alias) {
            commands.put(currentAlias, command);
        }

    }

    public void unregisterCommand(String name) {

        if (name == null) throw new IllegalArgumentException("Command name cannot be null!");

        if (!commands.containsKey(name.toLowerCase()));

        commands.remove(name.toLowerCase());

    }

    public void unregisterAllCommands() {

        commands.clear();

    }

    /**
     * @param name The command's name you are searching for
     * @return The command registered by the name. Returns null when no command is registered by the name
     */
    public Command getCommand(String name) {

        if (name == null) throw new IllegalArgumentException("Command name cannot be null!");

        if (!commands.containsKey(name.toLowerCase())) return null;

        return commands.get(name.toLowerCase());

    }

    /**
     * Returns a list with all commands registered.
     * It will return a empty list when there are no commands registered
     * @return a list with all commands
     */
    public Collection<Command> getCommands() {

        Collection<Command> commands = new ArrayList<>();

        commands.addAll(this.commands.values());

        return commands;

    }

    /**
     * @param prefix the prefix which should be in front of the command
     * @param event the command event the command was received
     * @return the command result the command returns
     */
    public CommandResult handleCommand(String prefix, MessageReceivedEvent event) {

        String raw = event.getMessage().getContentRaw().toLowerCase();

        if (!raw.startsWith(prefix)) return new CommandResult(CommandResult.ResultType.PREFIX_NOT_USED);

        String commandName = raw.substring(prefix.length());

        if (!commands.containsKey(commandName)) return new CommandResult(CommandResult.ResultType.COMMAND_NOT_FOUND);

        Command command = commands.get(commandName);

        if ((command instanceof SimpleGuildCommand || command instanceof AdvancedGuildCommand) && !event.isFromGuild()) {
            return new CommandResult(CommandResult.ResultType.INVALID_CHANNEL);
        } else if ((command instanceof SimplePrivateCommand || command instanceof AdvancedPrivateCommand) && event.isFromGuild()) {
            return new CommandResult(CommandResult.ResultType.INVALID_CHANNEL);
        }

        boolean advanced = command instanceof AdvancedCommand;

        CommandResult result = null;
        CommandEvent commandEvent = new CommandEvent(prefix, commandName, event);

        if (advanced) {
            AdvancedCommand advancedCommand = (AdvancedCommand) command;
            result = advancedCommand.onCommand(commandEvent);
        } else {
            SimpleCommand simpleCommand = (SimpleCommand) command;
            simpleCommand.onCommand(commandEvent);
        }

        return result != null ? result : CommandResult.NORMAL_RESULT;

    }

    private boolean commandIsValid(Command command) {
        return (command instanceof SimpleCommand || command instanceof AdvancedCommand);
    }

}
