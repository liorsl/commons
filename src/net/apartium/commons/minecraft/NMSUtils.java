package net.apartium.commons.minecraft;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Bukkit;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;
import org.bukkit.entity.Entity;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class NMSUtils {

	private final static String PACKAGENAME = Bukkit.getServer().getClass().getPackage().getName(),
			VERSION = PACKAGENAME.substring(PACKAGENAME.lastIndexOf(".") + 1);

	private final static Map<String, Class<?>> nms = new HashMap<>(), obc = new HashMap<>();

	static {
		try {
			for (ClassInfo ci : ClassPath.from(ClassLoader.getSystemClassLoader())
					.getTopLevelClassesRecursive("net.minecraft.server." + getVersion()))
				NMSUtils.nms.put(ci.getSimpleName(), ci.load());
			for (ClassInfo ci : ClassPath.from(ClassLoader.getSystemClassLoader())
					.getTopLevelClassesRecursive("org.bukkit.craftbukkit." + getVersion()))
				NMSUtils.obc.put(ci.getSimpleName(), ci.load());
		} catch (IOException e) {
			ExceptionHandler.getInstance().handle(e);
		}
	}
	
	private NMSUtils() {}

	/**
	 * @deprecated Each class should load its own requirements
	 */
	@Deprecated
	public static final Class<?>
			// NMS
			NMS_ENTITY = getClassInternal(true, "Entity"),
			NMS_HUMAN = getClassInternal(true, "EntityHuman"),
			NMS_PLAYER = getClassInternal(true, "EntityPlayer"),
			NMS_PACKET = getClassInternal(true, "Packet"),
			NMS_PLAYERCONNECTION = getClassInternal(true, "PlayerConnection"),
			NMS_CHATSERIALIZER = getClassInternal(true, "ChatSerializer"),
			NMS_MINECRAFTSERVER = getClassInternal(true, "MinecraftServer"),
			NMS_PLAYERINTERACTMANAGER = getClassInternal(true, "PlayerInteractManager"),
			NMS_NBTTAGCOMPOUND = getClassInternal(true, "NBTTagCompound"),
			NMS_ENUMPLAYERINFOACTION = getClassInternal(true, "PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),
			NMS_PLAYERSELECTOR = getClassInternal(true, "PlayerSelector"),
			NMS_ICOMMANDLISTENER = getClassInternal(true, "ICommandListener"),
			NMS_ITEMSTACK = getClassInternal(true, "ItemStack"),
			NMS_TILEENTITY = getClassInternal(true, "TileEntity"),
			NMS_ENUMHAND = getClassInternal(true, "EnumHand"),
			
			// Craft
			CRAFT_ENTITY = getClassInternal(false, "entity.CraftEntity"),
			CRAFT_HUMAN = getClassInternal(false, "entity.CraftHumanEntity"),
			CRAFT_PLAYER = getClassInternal(false, "entity.CraftPlayer"),
			CRAFT_OFFLINEPLAYER = getClassInternal(false, "CraftOfflinePlayer"),
			CRAFT_SERVER = getClassInternal(false, "CraftServer"), OBC_WORLD = getClassInternal(false, "CraftWorld");

	public static Object getMinecraftServer() {
		try {
			return MethodUtils.invokeStaticMethod(NMS_MINECRAFTSERVER, "getServer", new Object[0]);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Get minecraft version. Ex: v_1_10_R1
	 *
	 * @return the minecraft version
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * get class of packet
	 *
	 * @param name
	 *            name of the packet, packet prefix is not needed
	 * @return Class variable of the packet
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getPacketClass(String name) throws ClassNotFoundException {
		Validate.notEmpty(name, "name +-");

		if (!name.startsWith("Packet"))
			name = "Packet" + name;

		return getClass(true, name);
	}

	static Class<?> getClassInternal(boolean nms, String name) {
		try {
			return getClass(nms, name);
		} catch (ClassNotFoundException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Get non/nms class
	 *
	 * @param nms
	 *            determine if the required class is for NMS or OBC - true for NMS,
	 *            false for OBC
	 * @param name
	 *            the name of the class (including sub packages if any)
	 * @return the required class
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(boolean useIndex, boolean nms, String name) throws ClassNotFoundException {
		Validate.notEmpty(name, "name +-");

		String pack = (nms ? "net.minecraft.server." : "org.bukkit.craftbukkit.") + getVersion();

		if (!useIndex)
			return Class.forName(pack + "." + name);
		else {
			try {
				Class<?> clazz = Class.forName(pack + "." + name);
				return clazz;
			} catch (ClassNotFoundException e) {
				Class<?> clazz = getIndex(nms).get(name);
				// if (clazz == null)
				// throw e;
				return clazz;
			}
		}
	}

	/**
	 * Get index of classes (get classes only by name, not package)
	 *
	 * @param nms
	 *            true to get nms' index, false to get obc's one
	 * @return the required index
	 */
	public static Map<String, Class<?>> getIndex(boolean nms) {
		return nms ? NMSUtils.nms : NMSUtils.obc;
	}

	/**
	 * Get non/nms class
	 *
	 * @param nms
	 *            determine if the required class is for NMS or OBC - true for NMS,
	 *            false for OBC
	 * @param name
	 *            the name of the class (including sub packages if any)
	 * @return the required class
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(boolean nms, String name) throws ClassNotFoundException {
		return getClass(true, nms, name);
	}

	/**
	 * Use the method getHandle to get NMS version of Bukkit classes
	 *
	 * @param object
	 *            the object to try to invoke getHandle on
	 * @return the NMS version of the object (if any)
	 */
	public static Object getHandle(Object object) {
		Validate.notNull(object, "object +-");

		Class<?> clazz = object.getClass();
		Method method = null;
		try {
			method = clazz.getMethod("getHandle");
		} catch (NoSuchMethodException | SecurityException e) {
			ExceptionHandler.getInstance().handle(e);
		}

		try {
			return method.invoke(object);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	public static Field getField(Entity entity, boolean nms, String fieldName) throws NoSuchFieldException {
		Object instance;

		if (nms)
			instance = NMSUtils.getHandle(entity);
		else
			instance = entity;

		return getField(instance, fieldName);
	}

	public static Field getField(Object object, String fieldName) throws NoSuchFieldException {
		Validate.notNull(object, "object +-");
		Validate.notEmpty(fieldName, "fieldName +-");

		Class<?> clazz = object.getClass();

		Field field;

		try {
			field = clazz.getField(fieldName);
		} catch (NoSuchFieldException e) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch(NoSuchFieldException e1) {
				throw e1;
			}
		}

		boolean acc = field.isAccessible();
		field.setAccessible(true);

		return field;
	}

	public static class NMSText {

		/**
		 * get instance of chat componment of the specified text
		 * 
		 * @param string
		 *            the text
		 * @return instance of chat componment with the specified text
		 */
		public static Object ofString(String string) {
			Validate.notNull(string, "string +-");

			if (!string.startsWith("\"{\\\"text\\\": \\\"\" + "))
				string = "{\"text\": \"" + string;
			if (!string.endsWith("\"}"))
				string = string + "\"}";

			try {
				return MethodUtils.invokeStaticMethod(NMSUtils.NMS_CHATSERIALIZER, "a", string);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				ExceptionHandler.getInstance().handle(e);
			}

			return null;
		}
	}

}