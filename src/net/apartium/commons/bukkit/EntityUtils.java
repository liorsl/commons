package net.apartium.commons.bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import net.apartium.commons.Validate;
import net.apartium.commons.minecraft.NMSUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class EntityUtils {

	private EntityUtils() {}
	
	private static final Map<Entity, Listener> 
				harmListeners = new HashMap<>();

	/**
	 * Set this entity as invulnerable (Will not be able to take any damage)
	 * @param entity The entity to set mode to
	 * @param mode True for make the entity not able to take damage, false to reverse it
	 */
	public static void setInvulnerable(final Entity entity, boolean mode) {
		Validate.notNull(entity, "entity +-");

		try {
			setField(entity, true, "invulnerable", mode);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

	}

	public static void setSilent(final Entity entity, boolean mode) {

		Validate.notNull(entity, "entity +-");

		try {
			MethodUtils.invokeMethod(entity, "setSilent", mode);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set field of an entity
	 * @param entity The bukkit entity
	 * @param nms Should the field be set on NMS or OBC: true for NMS, false for OBC
	 * @param fieldName The name of the field
	 * @param value The new value to set
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 */
	public static void setField(Entity entity, boolean nms, String fieldName, Object value)
			throws IllegalArgumentException, NoSuchFieldException {
		Validate.notNull(entity, "entity +-");
		Validate.notNull(value, "value +-");

		Object instance = nms ? NMSUtils.getHandle(entity) : entity;

		Field field = NMSUtils.getField(instance, fieldName);

		try {
			field.set(instance, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get the value of a field of entity
	 * @param entity The bukkit entity
	 * @param nms Is the field from NMS or OBC: true for NMS, false for OBC
	 * @param fieldName The name of the field
	 * @return The field's value
	 * 
	 * @throws NoSuchFieldException
	 */
	public static Object getFieldValue(Entity entity, boolean nms, String fieldName) 
			throws NoSuchFieldException {
		Validate.notNull(entity, "player +-");
		Validate.notEmpty(fieldName, "fieldName +-");
		
		Object instance = nms ? NMSUtils.getHandle(entity) : entity;

		Field field = NMSUtils.getField(instance, fieldName);

		Object value = null;
		try {
			value = field.get(instance);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * Set if this entity can harm.
	 * NOTE: This will not modify the AI for the specified entity.
	 * @param plugin The plugin to register the required listener to. not required if the value is false.
	 * @param entity The entity.
	 * @param value True for can harm, else false.
	 */
	public static void setCanHarm(Plugin plugin, final Entity entity, boolean value) {
		Validate.notNull(entity, "entity +-");
		
		if (value) {
			Listener listener = (Listener) harmListeners.get(entity);
			if (listener == null) 
				return;
			
			EntityDamageByEntityEvent.getHandlerList().unregister(listener);
			
		} else {
			Validate.notNull(plugin, "plugin +-");

			Listener listener = new Listener() {
				@EventHandler
				public void onDamage(EntityDamageByEntityEvent e) {
					if (e.getDamager().equals(entity)) {
						e.setCancelled(true);
					}
				}
			};
			
			Bukkit.getPluginManager().registerEvents(listener, plugin);
		}
	}
	
}
