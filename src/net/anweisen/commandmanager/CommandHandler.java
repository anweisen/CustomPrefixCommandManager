package net.anweisen.commandmanager;

import net.anweisen.commandmanager.commandtype.GeneralCommand;
import net.anweisen.commandmanager.commandtype.GuildCommand;
import net.anweisen.commandmanager.commandtype.PrivateCommand;
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

    /**
     * @param name the command's name
     * @param command The command which should be executed by calling its name
     */
    public void registerCommand(String name, GeneralCommand command) {

        commands.put(name.toLowerCase(), command);

    }

    /**
     * @param name The command's name which should be removed
     */
    public void unregisterCommand(String name) {

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

        if (!commands.containsKey(name.toLowerCase())) return null;

        return commands.get(name.toLowerCase());

    }

    /**
     * Returns a list with all commands registered.
     * @return a list with all commands
     */
    public Collection<GeneralCommand> getCommands() {

        Collection<GeneralCommand> commands = new ArrayList<>();

        this.commands.values().forEach(currentCommand -> {
            commands.add(currentCommand);
        });

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
