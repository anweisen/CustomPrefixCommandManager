import net.codingarea.engine.discord.listener.Listener;
import net.codingarea.engine.sql.MySQL;
import net.codingarea.engine.sql.SQL;
import net.codingarea.engine.sql.helper.SQLHelper;

public class Main implements Listener {

	public static void main(String[] args) throws Exception {
		new Main();
	}

	public Main() throws Exception {
		SQL sql = MySQL.createDefault("localhost", "ticket", "root", "");
		//sql.insert().table("tickets").insert("guildID", null).execute();
		SQLHelper.logResult(sql.query().table("tickets").where("guildID", "asd").select("*").distinct(true).execute());
	}

}
