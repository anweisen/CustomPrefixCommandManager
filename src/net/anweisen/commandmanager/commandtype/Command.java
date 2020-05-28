package net.anweisen.commandmanager.commandtype;

import net.anweisen.commandmanager.CommandEvent;
import net.anweisen.commandmanager.CommandResult;

/**
 * @author anweisen
 * CommandManager developed on 05-25-2020
 * Website: www.anweisen.net
 */

public abstract interface Command {

    public interface AdvancedCommand extends Command {

        public interface AdvancedGuildCommand extends AdvancedCommand { }

        public interface AdvancedPrivateCommand extends AdvancedCommand { }

        CommandResult onCommand(CommandEvent event);

    }

    public interface SimpleCommand extends Command {

        public interface SimpleGuildCommand extends SimpleCommand { }

        public interface SimplePrivateCommand extends SimpleCommand { }

        void onCommand(CommandEvent event);

    }

}
