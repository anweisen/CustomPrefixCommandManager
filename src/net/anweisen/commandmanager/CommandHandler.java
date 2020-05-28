package net.anweisen.commandmanager;

import net.anweisen.commandmanager.commandtype.GeneralCommand;
import net.anweisen.commandmanager.commandtype.GeneralCommand.GuildCommand;
import net.anweisen.commandmanager.commandtype.GeneralCommand.PrivateCommand;
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

    private HashMap<String, GeneralCommand> commands;

    public CommandHandler() {

        commands = new HashMap<>();

    }


    @Deprecated
    public void registerCommand(String name, GeneralCommand command) {

        if (name == null) throw new IllegalArgumentException("Command name cannot be null!");
        if (command == null) throw new IllegalArgumentException("Command cannot be null!");

        commands.put(name.toLowerCase(), command);

    }

    public void registerCommand(GeneralCommand command, String... name) {

        if (command == null) throw new IllegalArgumentException("Command cannot be null!");
        if (name == null) throw new IllegalArgumentException("Command name(s) cannot be null!");

        for (String currentCommandName : name) {
            commands.put(currentCommandName.toLowerCase(), command);
        }

    }

    public void addAlias(String commandName, String... alias) {

        if (commandName == null) throw new IllegalArgumentException("Command name cannot be null!");
        if (alias == null) throw new IllegalArgumentException("Alias cannot be null!");

        GeneralCommand command;

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
    public GeneralCommand getCommand(String name) {

        if (name == null) throw new IllegalArgumentException("Command name cannot be null!");

        if (!commands.containsKey(name.toLowerCase())) return null;

        return commands.get(name.toLowerCase());

    }

    /**
     * Returns a list with all commands registered.
     * It will return a empty list when there are no commands registered
     * @return a list with all commands
     */
    public Collection<GeneralCommand> getCommands() {

        Collection<GeneralCommand> commands = new ArrayList<>();

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

        if (!raw.startsWith(prefix)) return CommandResult.PREFIX_NOT_USED;

        String commandName = raw.substring(prefix.length());

        if (!commands.containsKey(commandName)) return CommandResult.COMMAND_NOT_FOUND;

        GeneralCommand command = commands.get(commandName);

        if (command instanceof GuildCommand && !event.isFromGuild()) {
            return CommandResult.INVALID_CHANNEL;
        } else if (command instanceof PrivateCommand && event.isFromGuild()) {
            return CommandResult.INVALID_CHANNEL;
        }

        command.onCommand(new CommandEvent(prefix, commandName, event));

        return CommandResult.SUCCESS;

    }

}
