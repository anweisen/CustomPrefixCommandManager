import net.anweisen.commandmanager.CommandEvent;
import net.anweisen.commandmanager.CommandHandler;
import net.anweisen.commandmanager.commands.Command;
import net.anweisen.commandmanager.defaults.DefaultBuilder;
import net.anweisen.commandmanager.defaults.DefaultConfigLoader;
import net.anweisen.commandmanager.defaults.DefaultMessageListener;
import net.anweisen.commandmanager.defaults.DefaultPrefixCache;
import net.anweisen.commandmanager.sql.LiteSQL;
import net.anweisen.commandmanager.utils.StaticBinder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.annotation.Nonnull;
import java.sql.ResultSet;

/**
 * Developed in the CommandManager project
 * on 08-30-2020
 *
 * @author anweisen | https://github.com/anweisen
 */
public class Main {

	public static void main(String[] args) throws Exception {

		LiteSQL.createDefault().bind(1);

		LiteSQL sql = StaticBinder.get(1);
		sql.update("CREATE TABLE IF NOT EXISTS prefix (guildID VARCHAR(18), prefix VARCHAR(100))");

		/*CommandHandler handler = new CommandHandler().registerCommand(new Command("test", true, "t") {
			@Override
			public void onCommand(@Nonnull CommandEvent event) {
				event.queueReply("Hello World!" + "\n" +
					             "Arguments: " + event.getArgsAsString() + " | Length: " + event.getArgs().length);
				throw new NullPointerException();
			}
		});
		DefaultConfigLoader loader = DefaultConfigLoader.create();
		DefaultPrefixCache prefixCache = new DefaultPrefixCache(true, "!", "prefix", "guildID", "prefix", sql).bindToClass();
		DefaultBuilder.createHeavyShardManager(loader.getToken())
					  .setMemberCachePolicy(MemberCachePolicy.ALL)
				      .addEventListeners(new DefaultMessageListener(handler, prefixCache))
					  .build();*/

	}

}
