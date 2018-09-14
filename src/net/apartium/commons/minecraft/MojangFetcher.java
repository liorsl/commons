package net.apartium.commons.minecraft;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.StringUtils;
import net.apartium.commons.Validate;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class MojangFetcher {

	private static final String PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/";

	private static final JSONParser jsonParser = new JSONParser();
	
	private MojangFetcher() {}
	
	/**
	 * Get UUID by player name
	 * 
	 * @param name
	 *            the name of the player
	 * @return the UUID associated with the name
	 * @throws IOException
	 */
	public static UUID getUUID(String name) throws IOException {
		Validate.notEmpty(name, "name +-");

		// if (Bukkit.getPlayer(name) != null)
		// return Bukkit.getPlayer(name).getUniqueId();

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(PROFILE_URL + name).openConnection();
			JSONObject response = (JSONObject) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
			String uuid = (String) response.get("id");
			if (uuid == null)
				return null;

			return UUID.fromString(StringUtils.UUIDUtils.addHyphens(uuid));
		} catch (ParseException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Get the name history of a player.
	 * 
	 * @param uuid
	 *            the UUID of the player.
	 * @return the name history of the player.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static List<Entry<String, Instant>> getNames(UUID uuid) throws IOException {
		Validate.notNull(uuid, "uuid +-");

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(
					PROFILE_URL + uuid.toString().replace("-", "") + "/names").openConnection();
			List<JSONObject> response = (List<JSONObject>) jsonParser
					.parse(new InputStreamReader(connection.getInputStream()));

			List<Entry<String, Instant>> names = new ArrayList<>();
			for (JSONObject obj : response) {
				String name = (String) obj.get("name");
				Instant date = null;
				if (obj.get("changedToAt") != null)
					date = Instant.parse(obj.get("changedToAt").toString());

				names.add(new SimpleEntry<>(name, date));
			}

			return names;
		} catch (ParseException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		} catch (MalformedURLException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Get the current name of a player.
	 * 
	 * @param uuid
	 *            the UUID of the player.
	 * @return the current name of the player.
	 * @throws IOException
	 */
	public static String getName(UUID uuid) throws IOException {
		Validate.notNull(uuid, "uuid +-");

		List<Entry<String, Instant>> names = getNames(uuid);
		if (names.size() == 0)
			return null;
		return names.get(names.size() - 1).getKey();

	}

}