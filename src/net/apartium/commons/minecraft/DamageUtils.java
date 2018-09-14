package net.apartium.commons.minecraft;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class DamageUtils {
	
	private final static Class<?>
			NMS_DAMAGESOURCE = getClassInternal(true, "DamageSource");
	
	private DamageUtils() {}
	
	static Class<?> getClassInternal(boolean nms, String name) {
		try {
			return NMSUtils.getClass(nms, name);
		} catch (ClassNotFoundException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Get damage source of NMS.
	 * @param source the name of the damage source
	 * @return the object presenting the damage source
	 */
	public static Object getNMSDamageSource(String source) {
		Validate.notEmpty(source, "source +-");
		try {
			Field field = NMS_DAMAGESOURCE.getField(source.toUpperCase());
			return field.get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}
	
	/**
	 * Kill a player using NMS (EntityPlayer#die)
	 * @param player specified player
	 */
	public static void kill(Player player) {
		Validate.notNull(player, "player +-");
		Object source = getNMSDamageSource("GENERIC");
		if (source == null)
			return;
		
		try {
			Method kill = NMSUtils.NMS_PLAYER.getMethod("die", NMS_DAMAGESOURCE);
			kill.invoke(NMSUtils.getHandle(player), source);

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
		}
		
	}

}
