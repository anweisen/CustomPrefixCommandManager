package net.codingarea.engine.utils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public final class UUIDFetcher {

	private UUIDFetcher() { }

	private final static ConcurrentHashMap<String, String> playerUUIDs = new ConcurrentHashMap<>();

	/**
	 * Fetches a player's UUID using the mojang api servers by the player name.
	 * It is also caching the uuids so we don't have to do make a request every time
	 *
	 * @param playerName The player's name whose uuid we are searching for
	 * @return <code>null</code> if something goes wrong
	 *
	 * @see #fetchUUID(String)
	 */
	public static String getUUID(@Nonnull String playerName) {

		String uuid = playerUUIDs.get(playerName);
		if (uuid != null)
			return uuid;

		uuid = fetchUUID(playerName);
		if (uuid != null) {
			playerUUIDs.put(playerName, uuid);
		}
		return uuid;

	}

	/**
	 * Fetches a player's UUID using the mojang api servers by the player name.
	 *
	 * @param playerName The player's name whose uuid we are searching for
	 * @return <code>null</code> if something goes wrong
	 */
	public static String fetchUUID(@Nonnull String playerName) {
		String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
		try {
			String content = IOUtils.toString(new URL(url), Charset.defaultCharset());
			if (content.isEmpty()) return null;
			JSONObject json = (JSONObject) JSONValue.parseWithException(content);
			return json.get("id").toString();
		} catch (IOException | ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
