# CustomPrefix JDA CommandManager
## A simple way to manage commands with custom prefix!

### Installation
Download the [CommandManager.jar](https://github.com/anweisen/CustomPrefixCommandManager/raw/master/out/artifacts/CommandManager_jar/CommandManager.jar) file and add it as library to your project. <br>
But make sure that you have [JDA](https://github.com/DV8FromTheWorld/JDA#download) and the corresponding sql drivers (MySQL/SQLite) imported as well <br>

### Using
You may also see into the [TestPackage](https://github.com/anweisen/JDACommandManager/tree/master/src/test/java) for more code examples <br>
<br>
**Adding commands** <br>
You can simply instanciate a CommandHandler with *new CommandHandler()* <br>
With *handler.registerCommands(new ExampleCommand())* you can register a command. <br>

**Handleing events** <br>
To use CommandHandler, you need to use *CommandHandler#hanldeCommand(String prefix, MessageReceived event)* in a listener for a *MessageReceivedEvent*. <br>
If you want to use a custom prefix for every guild, you can use something like this:
```java
public void onMessageReceived(MessageReceivedEvent event) {
  
  String prefix = "!";
  
  if (event.isFromGuild()) {
    prefix = PrefixManager.getPrefix(event.getGuild());
  }
  
  handler.handleCommand(prefix, event);
  
}
```

You could also use the built in *DefaultMessageListener* combined with the *DefaultPrefixCache* and *SQL*: <br>
```java
CommandHandler handler = new CommandHandler();
LiteSQL sql = LiteSQL.createDefault();
DefaultPrefixCache prefixCache = new DefaultPrefixCache(true, "!", "prefix", "guildID", "prefix", sql);
DefaultMessageListener listener = new DefaultMessageListener(handler, prefixCache);
jda.addEventListener(listener);
```

If you want to send a *command not found message*, you can use something like this: <br>
```java
public void onMessageReceived(MessageRecievedEvent event) {
  
  CommandResult result = hanlder.hanldeCommand("!", event);
  
  if (result == ResultType.COMMAND_NOT_FOUND) {
    event.getChannel().sendMessage("The command was not found").queue();
  } else if (result == ResultType.INVALID_CHANNEL_GUILD_COMMAND) {
    event.getChannel().sendMessage("Please use this command in a guild").queue();
  }
  
}
```

**Creating commands** <br>
A command has the following attributes:
- Command name: The name, the command listens to
- Alias: Extra command names which you can use to access the command
- CommandType:
  - GENERAL - You can access the command everywhere
  - GUILD - You can only use the command in a guild
  - PRIVATE - You can only use the command in the private chat with the bot
- ProccessInNewThread: This will start an extra thread to process the command
- ReactToMentionPrefix: The command will also be excecuted when you use @Bot as prefix
- ReactToBots
- ReactToWebhooks

**Using Commands & CommandEvents** <br>
```java
class ExampleCommand extends Command {

  public ExampleCommand() {
    super("command name", CommandType.GUILD, processInNewThread, reactToMentionPrefix, "alias1", "alias2");
  }
  
  @Override
  public void onCommand(CommandEvent event) {
    
    if (event.getArgs().lenght >= 1 && event.getArg(0).equals("hello")) {
      event.deleteMessage();
      event.queueReply("I like you too <2", message -> message.delete().queue());
    }
        
    if (!event.senderHasPermission(Permission.BAN_MEMBERS)) {
      event.queueReply("You do not have permissions to do this!");
      return;
    }
        
    if (event.getMentionedMembers().isEmpty()) {
      event.queueReply("Please use " + CommandEvent.syntax(event, "<@user>"));
      return;
    }
        
    Member member = event.getMentionedMembers().get(0);
    member.ban(0, "Banned by " + event.getMemberName()).queue();
    event.queueReply(member.getUser().getAsTag() + " was banned by " + event.getUserTag());

  }

}
```

**Using Listener and EventHandler** <br>
You can annotate methods with *@EventHandler* to get them triggered when the *JDA*, the *Listener* is registered to, fires the *Event* which the method has as their only parameter <br>
