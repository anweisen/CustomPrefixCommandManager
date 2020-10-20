import net.codingarea.engine.discord.commandmanager.CommandEvent;
import net.codingarea.engine.discord.commandmanager.CommandHandler;
import net.codingarea.engine.discord.commandmanager.SubCommand;
import net.codingarea.engine.discord.commandmanager.SubCommandHandler;
import net.codingarea.engine.discord.defaults.DefaultBuilder;
import net.codingarea.engine.discord.defaults.DefaultCommandListener;
import net.codingarea.engine.discord.defaults.DefaultConfigLoader;
import net.dv8tion.jda.api.entities.Member;

public class Main {

	public static void main(String[] args) throws Exception {

		CommandHandler handler = new CommandHandler().registerCommand(new Com());
		DefaultBuilder.createHeavyJDA(new DefaultConfigLoader().getToken())
					  .addEventListeners(new DefaultCommandListener(handler, "-"))
					  .build();

	}

	public static class Com extends SubCommandHandler {

		public Com() {
			super("help");
		}

		@SubCommand
		public void subCommand(CommandEvent event, String string, int asd) {
			event.queueReply("this is sub command 1 :) " + string + asd);
		}

		@SubCommand(name = {"2", "3"})
		public void subCommand2(CommandEvent event) {
			event.queueReply("hey, wie gehts");
		}

		@SubCommand(name = "ban")
		public void saehozf(CommandEvent event, Member member) {
			event.queueReply(member.getAsMention() + "is weg alda");
		}

	}

}
