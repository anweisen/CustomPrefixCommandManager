# CustomPrefix JDA CommandManager
## A simple way to manage commands with custom prefix!

### Installation
Download the CommandManager.jar file and add it as libary to your project.
You will no longer need to import JDA, because it's already built-in.
The current built-in JDA version is **4.1.1_154**

### Using
**Adding commands** <br>
You can simply instanciate a CommandHandler with *new CommandHandler()*
With *handler#registerCommand(new HelpCommand(), "help")* you can register a command.
You can also use *hanlder#registerCommand(new HelpCommand(), "help", "hilfe")* to register the command directly with aliases
You can use *handler#addAlias("help", "hilfe")* to add 1 or more aliases to a command later on.

**Handleing events** <br>
To fully activate the CommandHanlder, you need to use *handler#hanldeCommand(prefix, event)* in  a *MessageReceivedEvent*.
If you want to use a custom prefix for every guild, you use this codeexample:
```java
public void onMessageReceived(MessageReceivedEvent event) {
  
  String prefix = "!";
  
  if (event.isFromGuild()) {
    prefix = PrefixManager.getGuildPrefix(event.getGuild());
  }
  
  handler.handleCommand(prefix, event);
  
}
```
If you want to send a *Command not found message*, you can use the following codeexample:
```java
public void onMessageReceived(MessageRecievedEvent event) {
  
  CommandResult result = hanlder.hanldeCommand(..);
  
  if (result.getType() == ResultType.COMMAND_NOT_FOUND) {
    //TODO Send command not found message
  }
  
}
```

**Defineing commands** <br>
There are different types of commands
- GuildCommand (SimpleGuildCommand / AdvancedGuildCommand)
- PrivateCommand (SimplePrivateCommand / AdvancedPrivateCommand)

If you are trying to access a PrivateCommand in a guild (or a GuildCommand in a private chat)
*hanlder#handleCommand(...)* will return a CommandResult with the ResultType *INVALID_CHANNEL* and wont execute the command

Using simple commands
```java
class HelpCommand implements SimpleCommand {
  
  @Override
  public void onCommand(CommandEvent event) {
    ...
  }

}
```

Using advanced commands:
```java
class BanCommand implements AdvancedGuildCommand {

  @Override
  public CommandResult onCommand(CommandEvent event) {
    ...
    return new CommandResult(ResultType.CLIENT_NO_PERMISSIONS);
    OR
    return new CommandResult("No permissions!");
    OR
    return new CommandResult(ResultType.CLIENT_NO_PERMISSIONS, "No permissions!");
 
  }

}
```
