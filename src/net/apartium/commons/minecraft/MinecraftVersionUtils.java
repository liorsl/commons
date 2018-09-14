package net.apartium.commons.minecraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.bukkit.PlayerUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class MinecraftVersionUtils {

	private final static String PACKAGENAME = Bukkit.getServer().getClass().getPackage().getName(),
			VERSION = PACKAGENAME.substring(PACKAGENAME.lastIndexOf(".") + 1);

	private static Method GET_VERSION;

	static {
		try {
			GET_VERSION = NMSUtils.NMS_PLAYERCONNECTION.getMethod("getVersion");
		} catch (NoSuchMethodException | SecurityException e) {
			ExceptionHandler.getInstance().handle(e);

		}
	}
	
	private MinecraftVersionUtils() {}

	/**
	 * Get the minecraft version
	 * 
	 * @return The minecraft version of the server.
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * Get the protocol version of a specific player.
	 * 
	 * @param player
	 *            The specified player.
	 * @return The protcol version number of the player.
	 */
	public static int getProtocolVersion(Player player) {
		try {
			Object ins = PlayerUtils.getNetworkManager(player);
			if (ins == null)
				return 0;
			if (GET_VERSION == null)
				return 0;
			return (int) GET_VERSION.invoke(ins);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
			return 0;
		}
	}

}
