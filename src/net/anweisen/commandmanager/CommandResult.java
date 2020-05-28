package net.anweisen.commandmanager;

/**
 * @author anweisen
 * CommandManager developed on 05-25-2020
 * Website: www.anweisen.net
 */

public class CommandResult {

    public static final CommandResult NORMAL_RESULT = new CommandResult(ResultType.SUCCESS);

    public enum ResultType {

        INVALID_CHANNEL,
        WEBHOOK_MESSAGE_NO_REACT,
        PREFIX_NOT_USED,
        COMMAND_NOT_FOUND,
        SUCCESS,
        USER_SPECIFIED,
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
        this.message = "null";
    }

    public CommandResult(String message) {
        this.type = ResultType.USER_SPECIFIED;
        this.message = message;
    }

    public CommandResult() {
        this.type = ResultType.UNKNOWN;
        this.message = "null";
    }

    public ResultType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

}
