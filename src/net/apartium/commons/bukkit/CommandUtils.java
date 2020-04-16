package net.apartium.commons.bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;
import net.apartium.commons.minecraft.NMSUtils;

public class CommandUtils {

	private CommandUtils() {}
	
	private static Constructor<PluginCommand>
			PLUGIN_COMMAND_CONS;
	
	private static Method
			GET_COMMAND_MAP;

	@Getter
	private static CommandMap
			commandMap;

	static {
		try {
			PLUGIN_COMMAND_CONS = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			PLUGIN_COMMAND_CONS.setAccessible(true);
			
			GET_COMMAND_MAP = NMSUtils.CRAFT_SERVER.getMethod("getCommandMap");

			commandMap = loadCommandMap();

		} catch (NoSuchMethodException | SecurityException e) {
			ExceptionHandler.getInstance().handle(e);

		}

	}

	/**
	 * Load command map from bukkit, via invoking CraftServer#getCommandMap again, for whatever reason you might want to do it.
	 * @return A command map instance
	 */
	public static CommandMap loadCommandMap() {
		try {
			return (CommandMap) GET_COMMAND_MAP.invoke(Bukkit.getServer());
		} catch (IllegalAccessException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Create a new instance of plugin command and register it to the command map
	 * @param name the name of the command
	 * @param plugin the parent of the command
	 * @return new instance of PluginCommand
	 */
	public static PluginCommand createPluginCommand(String name, Plugin plugin) 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return createPluginCommand(name, plugin, true, true);
	}
	
	/**
	 * Create a new instance of plugin command
	 * @param name the name of the command
	 * @param plugin the parent of the command
	 * @param register whether or not the command should be registered on the CommandMap
	 * @param unregisterOnDisable whether or not to unregister the command when the associated plugin is disabled
	 * @return new instance of PluginCommand
	 */
	public static PluginCommand createPluginCommand(String name, Plugin plugin, boolean register, 
			boolean unregisterOnDisable) 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Validate.notEmpty(name, "name +-");
		Validate.notNull(plugin, "plugin +-");

		PluginCommand command = PLUGIN_COMMAND_CONS.newInstance(name, plugin);
		if (register) {
			getCommandMap().register(plugin.getDescription().getName().toLowerCase() + ":" + name.toLowerCase(), command);
			if (unregisterOnDisable)
				Bukkit.getPluginManager().registerEvents(new Listener() {

					@EventHandler
					public void onPluginDisable(PluginDisableEvent event) {
						if (!event.getPlugin().equals(plugin))
							return;

						if (command.isRegistered())
							command.unregister(getCommandMap());

					}

				}, plugin);
		}
		
		return command;
	}

}
