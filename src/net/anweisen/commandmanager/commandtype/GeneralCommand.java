package net.anweisen.commandmanager.commandtype;

import net.anweisen.commandmanager.CommandEvent;

/**
 * @author anweisen
 * CommandManager developed on 05-25-2020
 * Website: www.anweisen.net
 */

public interface GeneralCommand {

    public interface GuildCommand extends GeneralCommand { }

    public interface PrivateCommand extends GeneralCommand { }

    void onCommand(CommandEvent event);

}
