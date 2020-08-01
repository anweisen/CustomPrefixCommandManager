# CustomPrefix JDA CommandManager
## A simple way to manage commands with custom prefix!

### Installation
Download the [CommandManager.jar](https://github.com/anweisen/CustomPrefixCommandManager/raw/master/out/artifacts/CommandManager_jar/CommandManager.jar) file and add it as libary to your project. <br>
But make sure that you have jda imported as well <br>

### Using
**Adding commands** <br>
You can simply instanciate a CommandHandler with *new CommandHandler()* <br>
With *handler.registerCommands(new HelpCommand())* you can register a command. <br>

**Handleing events** <br>
To fully activate the CommandHanlder, you need to use *handler#hanldeCommand(prefix, event)* in  a *MessageReceivedEvent*. <br>
If you want to use a custom prefix for every guild, you use this codeexample: <br>
```java
public void onMessageReceived(MessageReceivedEvent event) {
  
  String prefix = "!";
  
  if (event.isFromGuild()) {
    prefix = PrefixManager.getPrefix(event.getGuild());
  }
  
  handler.handleCommand(prefix, event);
  
}
```
If you want to send a *Command not found message*, you can use the following codeexample: <br>
```java
public void onMessageReceived(MessageRecievedEvent event) {
  
  CommandResult result = hanlder.hanldeCommand(...);
  
  if (result == ResultType.COMMAND_NOT_FOUND) {
    //TODO Send command not found message
  }
  
}
```

**Defineing commands** <br>
There are some variables you should know:
- Command name: The commandname?
- Alias: Extra command names you can use to access the command
- CommandType: GENERAL - You can access the command everywhere; GUILD - You can only use the command in a guild; PRIVATE - You can only use the command in the private chat with   the bot
- ProccessInNewThread: This will start an extra thread to process the command in
- ReactToMentionPrefix: The command will also be excecuted when you use @Bot as prefix. You can use; You can set a default value with Command.REACT_TO_MENTION_PREFIX_DEFAULT = true;


**Using commands** <br>
```java
class ExampleCommand extends Command {
  
  public ExampleCommand() {
    super("command name", commandyType, processInNewThread, reactToMentionPrefix, "alias1", "alias2", "alias...");
  }
  
  @Override
  public void onCommand(CommandEvent event) {
    ...
  }

}
```

**Using CommandEvents** <br>
```java
class ExampleCommand extends Command {

  public ExampleCommand() {
    ...
  }

  @Override
  public void onCommand(CommandEvent event) {
    
    if (event.getArgs().lenght >= 1 && event.getArg(0).equals(...)) {
      ...
    }
    
    if (event.getMentionedMembers().isEmpty()) {
      event.queueReply("Please use `" + event.getPrefix() + "ban <@User>`");
    }
    
  }

}
```

**EventHandler Annotation and Listener interface** <br>
You can implement *ListenerAdapter* (JDA ListenerAdapter as interface lol) and override the methods or you can implement *Listener* and use the *@EventHandler* annotation to annotate a listener method. <br> You still have to register them in the JDA.
