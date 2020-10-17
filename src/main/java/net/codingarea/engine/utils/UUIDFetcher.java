package net.codingarea.engine.utils;

import net.codingarea.engine.exceptions.ExecutionException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import javax.annotation.Nonnull;
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
	private final static ConcurrentHashMap<String, String> nameHistories = new ConcurrentHashMap<>();

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
	 * @return <code>null</code> if no player was found according to this name
	 * @throws ExecutionException
	 *         If something goes wrong
	 */
	public static String fetchUUID(@Nonnull String playerName) {
		String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;
		try {
			String content = IOUtils.toString(new URL(url), Charset.defaultCharset());
			if (content.isEmpty()) return null;
			JSONObject json = (JSONObject) JSONValue.parseWithException(content);
			return json.get("id").toString();
		} catch (ParseException | NullPointerException ex) {
			return null;
		} catch (Exception ex) {
			throw new ExecutionException(ex);
		}
	}

	/**
	 * Fetches a player's name history using the mojang api servers by the player name.
	 *
	 * @param uuid The player's uuid whose name history we are searching for
	 * @return <code>null</code> if no player was found according to this uuid
	 * @throws ExecutionException
	 *         If something goes wrong
	 */
	public static JSONArray fetchNameHistory(@Nonnull String uuid) {
		String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
		try {
			String content = IOUtils.toString(new URL(url), Charset.defaultCharset());
			if (content.isEmpty()) return null;
			return (JSONArray) JSONValue.parseWithException(content);
		} catch (ParseException | NullPointerException ex) {
			return null;
		} catch (Exception ex) {
			throw new ExecutionException(ex);
		}
	}

	public static String fetchName(@Nonnull String uuid) {
		JSONArray history = fetchNameHistory(uuid);
		if (history == null || history.isEmpty()) return null;
		JSONObject current = (JSONObject) history.get(history.size() - 1);
		if (current.isEmpty()) return null;
		return current.get("name").toString();
	}

	/**
	 * Fetches the last name of a player's name history using the mojang api servers by the player name.
	 * It is also caching the names so we don't have to do make a request every time
	 *
	 * @param uuid The player's uuid whose name we are searching for
	 * @return <code>null</code> if no user was found by that uuid
	 * @throws ExecutionException
	 *         If something goes wrong
	 *
	 * @see #fetchName(String)
	 */
	public static String getName(@Nonnull String uuid) {

		String name = nameHistories.get(uuid);
		if (name != null)
			return name;

		name = fetchName(uuid);
		if (name != null) {
			nameHistories.put(name, uuid);
		}
		return name;

	}

}
