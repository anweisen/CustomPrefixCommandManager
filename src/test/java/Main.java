import net.codingarea.engine.discord.commandmanager.Command;
import net.codingarea.engine.discord.commandmanager.CommandEvent;
import net.codingarea.engine.discord.commandmanager.CommandHandler;
import net.codingarea.engine.discord.defaults.DefaultBuilder;
import net.codingarea.engine.discord.defaults.DefaultConfigLoader;
import net.codingarea.engine.discord.defaults.DefaultMessageListener;

import javax.annotation.Nonnull;

public class Main {

	public static void main(String[] args) throws Exception {

		CommandHandler handler = new CommandHandler().registerCommand(new Com());
		DefaultConfigLoader config = new DefaultConfigLoader();
		DefaultBuilder.createHeavyShardManager(config.getToken())
					  .addEventListeners(new DefaultMessageListener(handler, "!"))
					  .build();

	}

	static class Com extends Command {

		Com() {
			super("find");
		}

		@Override
		public void onCommand(@Nonnull final CommandEvent event) throws Throwable {

			event.queueReply(findMember(event, event.getArgsAsString()) + " | " + event.getArgsAsString());

		}
	}

}
