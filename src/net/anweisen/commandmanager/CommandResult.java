package net.anweisen.commandmanager;

/**
 * @author anweisen
 * CommandManager developed on 05-25-2020
 * Website: www.anweisen.net
 */

public class CommandResult {

    public static final CommandResult NORMAL_RESULT = new CommandResult(CommandResult.ResultType.SUCCESS);

    public enum ResultType {

        INVALID_CHANNEL,
        PREFIX_NOT_USED,
        COMMAND_NOT_FOUND,
        SUCCESS,
        UNKNOWN;

    }

    private String message;
    private ResultType type;

    public CommandResult(ResultType type, String message) {
        this.type = type;
        this.message = message;
    }

    public CommandResult(ResultType type) {
        this.type = type;
        this.message = type.name();
    }

    public CommandResult(String message) {
        this.type = ResultType.UNKNOWN;
        this.message = message;
    }

    public CommandResult() {
        this.type = ResultType.UNKNOWN;
        this.message = "Unknown";
    }

    public ResultType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

}
