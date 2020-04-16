package net.apartium.commons.bukkit;

import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;
import sun.reflect.Reflection;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class PluginUtils {

	private PluginUtils() {}
	
	/**
	 * Get PluginClassLoader instance for every loaded bukkit plugin NOTE: The
	 * method returns URLClassLoader because PluginClassLoader is a protected class
	 * 
	 * @return all of the PluginClassLoader of the loaded plugins
	 */
	public static URLClassLoader[] getAllPluginClassLoaders() {
		List<URLClassLoader> ret = new ArrayList<>();
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
			ret.add(getPluginClassLoader(plugin));

		return ret.toArray(new URLClassLoader[ret.size()]);
	}

	/**
	 * Get class loader of specified plugin.
	 * 
	 * @param plugin
	 *            the plugin that uses the required class loader.
	 * @return the class loader associated with the plugin
	 */
	public static URLClassLoader getPluginClassLoader(Plugin plugin) {
		Validate.notNull(plugin, "plugin +-");

		return (URLClassLoader) plugin.getClass().getClassLoader();
	}

	/**
	 * Get the plugin of a specific =class
	 * 
	 * @param clazz
	 *            the class to get the plugin of.
	 * @return the required plugin
	 */
	public static JavaPlugin getOwner(Class<?> clazz) {
		Validate.notNull(clazz, "clazz +-");

		try {
			ClassLoader loader = clazz.getClassLoader();
			Class<?> bLoader = Class.forName("org.bukkit.plugin.java.PluginClassLoader");
			if (!bLoader.equals(loader.getClass()))
				return null;

			Field field = bLoader.getDeclaredField("pluginInit");
			field.setAccessible(true);
			return (JavaPlugin) field.get(loader);

		} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException
				| SecurityException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Check whether or not a class was loaded as part of a plugin.
	 * 
	 * @param clazz
	 *            The class instance.
	 * @return true if the specified class is owned by plugin, else false
	 */
	public static boolean isOwnedByPlugin(Class<?> clazz) {
		Validate.notNull(clazz, "clazz +-");

		try {
			ClassLoader loader = clazz.getClassLoader();
			Class<?> bLoader = Class.forName("org.bukkit.plugin.java.PluginClassLoader");
			if (!bLoader.equals(loader.getClass()))
				return false;
		} catch (ClassNotFoundException | IllegalArgumentException | SecurityException e) {
			ExceptionHandler.getInstance().handle(e);
			return false;
		}

		return true;

	}

	/**
	 * Get the plugin which invoked the method
	 * 
	 * @param before
	 *            how much traces to go before
	 * @return the required plugin
	 */
	public static JavaPlugin getInvoker(int before) {
		return getOwner(Reflection.getCallerClass(before));
	}

	/**
	 * Get the plugin which invoked the method one trace before
	 * 
	 * @return the required plugin
	 */
	public static JavaPlugin getInvoker() {
		return getOwner(Reflection.getCallerClass());
	}

	public static Plugin getInvokerOtherThan(int startFromBefore, Plugin... plugins) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		List<Plugin> tryNot = new ArrayList<>(Arrays.asList(plugins)); // try to get plugins that are not in this list

		Plugin last = null;
		for (int i = startFromBefore; i < elements.length; i++) {
			StackTraceElement element = elements[i];
			try {
				Class<?> clazz = Class.forName(element.getClassName());
				if (!isOwnedByPlugin(clazz))
					return last;
				else {
					Plugin plugin = getOwner(clazz);
					if (tryNot.contains(plugin)) {
						last = plugin;
						continue;
					}

					return plugin;
				}
			} catch (ClassNotFoundException e) {
				ExceptionHandler.getInstance().handle(e);
				return null;
			}

		}

		return null;
	}

}
