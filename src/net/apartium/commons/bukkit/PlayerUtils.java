package net.apartium.commons.bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_11_R1.EntityPlayer;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.mojang.authlib.GameProfile;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;
import net.apartium.commons.minecraft.DamageUtils;
import net.apartium.commons.minecraft.NMSUtils;
import net.apartium.commons.minecraft.PacketUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class PlayerUtils {

	private PlayerUtils() {}
	
	private static final Map<Player, Listener> vanishListeners = new HashMap<>();

	/**
	 * Setting the tab prefix of a player
	 * 
	 * @param player
	 *            the player.
	 * @param prefix
	 *            the prefix.
	 */
	@SuppressWarnings("deprecation")
	public static void setTabPrefix(Player player, String prefix) {
		Validate.notNull(player, "player +-");
		Validate.notEmpty(prefix, "prefix +-");

		Scoreboard board = player.getScoreboard();
		Objective obj = board.registerNewObjective("tablist", "dummy");
		obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		Team test = board.registerNewTeam(prefix);
		test.addPlayer(player);
		test.setPrefix(prefix);
		player.setScoreboard(board);
	}

	/**
	 * Set a field of a player
	 * 
	 * @param player
	 *            the specified player
	 * @param nms
	 *            if true, will use EntityPlayer (NMS), else, will use CraftPlayer
	 *            (CraftBukkit)
	 * @param fieldName
	 *            the name of the field to set
	 * @param value
	 *            the new value of the field to set
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void setField(Player player, boolean nms, String fieldName, Object value)
			throws SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		Validate.notNull(player, "player +-");
		Validate.notNull(value, "value +-");
		Validate.notEmpty(fieldName, "fieldName +-");

		Class<?> clazz;
		Object instance;
		if (nms) {
			clazz = NMSUtils.NMS_PLAYER;
			instance = NMSUtils.getHandle(player);
		} else {
			clazz = NMSUtils.CRAFT_PLAYER;
			instance = player;
		}

		Field field;
		try {
			field = clazz.getField(fieldName);
		} catch (NoSuchFieldException e) {
			field = clazz.getDeclaredField(fieldName);
		}

		boolean acc = field.isAccessible();
		field.setAccessible(true);
		field.set(instance, value);
		if (!acc)
			field.setAccessible(false);

	}

	/**
	 * disable coordination lines on debug screen for specific player
	 * 
	 * @param player
	 *            a player
	 *
	 */
	public static void disableCordinationShow(Player player) {
		Validate.notNull(player, "player +-");

		try {
			sendPacket(player, PacketUtils.create("PacketPlayOutEntityStatus", NMSUtils.getHandle(player), (byte) 22));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException
				| ClassNotFoundException e) {
			ExceptionHandler.getInstance().handle(e);
		}
	}

	/**
	 * enable coordination lines on debug screen for specific player
	 * 
	 * @param player
	 *            a player
	 */
	public static void enableCordinationShow(Player player) {
		Validate.notNull(player, "player +-");

		try {
			sendPacket(player, PacketUtils.create("PacketPlayOutEntityStatus", NMSUtils.getHandle(player), (byte) 23));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException
				| ClassNotFoundException e) {
			ExceptionHandler.getInstance().handle(e);
		}
	}

	/**
	 * Get the value of a field of player
	 * 
	 * @param player
	 *            the specified player
	 * @param nms
	 *            if true, will use EntityPlayer (NMS), else, will use CraftPlayer
	 *            (CraftBukkit)
	 * @param fieldName
	 *            the name of the field to get from
	 * 
	 * @return the value of the field, null if error / field value equals to null
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object getFieldValue(Player player, boolean nms, String fieldName) throws ClassNotFoundException,
			NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Validate.notNull(player, "player +-");
		Validate.notEmpty(fieldName, "fieldName +-");

		Class<?> clazz;
		Object instance;
		if (nms) {
			clazz = NMSUtils.getClass(true, "EntityPlayer");
			instance = NMSUtils.getHandle(player);
		} else {
			clazz = NMSUtils.getClass(false, "entity.CraftPlayer");
			instance = player;
		}

		Field field = clazz.getField(fieldName);
		if (!field.isAccessible())
			field.setAccessible(true);
		Object value = field.get(instance);

		field.setAccessible(false);

		return value;

	}

	/**
	 * Get player connection (NMS)
	 * 
	 * @param player
	 *            bukkit's player
	 * @return playerconnection of the specified player
	 */
	public static Object getConnection(Player player) {
		Validate.notNull(player, "player +-");

		try {
			return getFieldValue(player, true, "playerConnection");
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Send a packet to a player
	 * 
	 * @param player
	 *            the player
	 * @param packet
	 *            the packet
	 */
	public static void sendPacket(Player player, Object packet) {
		Validate.notNull(player, "player +-");
		Validate.notNull(packet, "packet +-");

		if (!PacketUtils.isPacket(packet))
			return;

		Object connection = getConnection(player);
		try {
			Method packetSend = NMSUtils.NMS_PLAYERCONNECTION.getMethod("sendPacket", NMSUtils.NMS_PACKET);
			packetSend.invoke(connection, packet);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
		}

	}

	/**
	 * Get the locale of the player's client
	 * 
	 * @param player
	 *            the specified player
	 * @return the locale of the player's client, Ex: en_US
	 */
	public static String getLocale(Player player) {
		Validate.notNull(player, "player +-");

		try {
			return (String) getFieldValue(player, true, "locale");
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Get the ping of a player
	 * 
	 * @param player
	 *            the specified player
	 * @return the ping of the player
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws ClassNotFoundException
	 */
	public static int getPing(Player player) throws ClassNotFoundException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		Validate.notNull(player, "player +-");
		return (int) getFieldValue(player, true, "ping");
	}

	/**
	 * Toggle player flying state NOTE: For this to work the player must be allowed
	 * to flight
	 * 
	 * @param players
	 *            player to toggle fly to
	 * @return the current fly status
	 */
	public static boolean toggleFly(Player... players) {
		Validate.notNull(players, "players +-");

		for (Player player : players)
			player.setFlying(!player.isFlying());

		return players[0].isFlying();
	}

	/**
	 * Toggle the allow flight mode of a player (if player can fly)
	 * 
	 * @param players
	 *            the players to toggle allow flight to
	 * @return the new allow flight mode of the first player
	 */
	public static boolean toggleAllowFlight(Player... players) {
		Validate.notNull(players, "players +-");

		for (Player player : players)
			player.setAllowFlight(!player.getAllowFlight());

		return players[0].getAllowFlight();
	}

	/**
	 * Set allow flight of players to specific mode if they don't have it currently.
	 * 
	 * @param mode
	 *            the mode to apply
	 * @param players
	 *            the players to apply the mode on
	 * @return the new mode
	 */
	public static boolean setAllowFlight(boolean mode, Player... players) {
		Validate.notNull(players, "players +-");

		for (Player player : players)
			if (!player.getAllowFlight() == mode)
				player.setAllowFlight(mode);

		return mode;
	}

	/**
	 * Toggle op for a player
	 * 
	 * @param player
	 *            the player to toggle op to
	 * @return the current op state (true / false)
	 */
	public static boolean toggleOP(Player player) {
		Validate.notNull(player, "player +-");

		player.setOp(!player.isOp());
		return player.isOp();
	}

	/**
	 * Kill a player using NMS (EntityPlayer#die)
	 * 
	 * @param player
	 *            specified player
	 */
	public static void kill(Player player) {
		DamageUtils.kill(player);

	}

	/**
	 * Set tablist header and footer for player
	 * 
	 * @param player
	 *            The player to set the header and footer to
	 * @param header
	 *            The header
	 * @param footer
	 *            The footer
	 */
	public static void setTabHeaderAndFooter(Player player, String header, String footer) {
		Object packet = null;
		try {
			packet = PacketUtils.create("PacketPlayOutPlayerListHeaderFooter", new Object[0]);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException
				| ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		Object headerChat = NMSUtils.NMSText.ofString(ChatColor.translateAlternateColorCodes('&', header));
		Object footerChat = NMSUtils.NMSText.ofString(ChatColor.translateAlternateColorCodes('&', footer));
		try {
			Field headerField = packet.getClass().getDeclaredField("a");
			headerField.setAccessible(true);
			headerField.set(packet, headerChat);
			headerField.setAccessible(!headerField.isAccessible());

			Field footerField = packet.getClass().getDeclaredField("b");
			footerField.setAccessible(true);
			footerField.set(packet, footerChat);
			footerField.setAccessible(!footerField.isAccessible());
		} catch (Exception e1) {
			ExceptionHandler.getInstance().handle(e1);
		}
		sendPacket(player, packet);
	}

	/**
	 * Vanish a player
	 * 
	 * @param plugin
	 *            The plugin to register the required listener to
	 * @param player
	 *            The player to vanish
	 * @param tab
	 *            Should the player be hidden on tab?
	 * @param effect
	 *            Should the player receive invisibility effect? (Only opped players
	 *            will see it if hard is true)
	 * @param hard
	 *            Should the player be hidden from all non-op players using
	 *            {@link Player#hidePlayer(Player)}
	 */
	public static void vanish(Plugin plugin, final Player player, boolean tab, boolean effect, boolean hard) {
		Validate.notNull(player, "player +-");

		Listener vanish = new Listener() {
			@EventHandler
			public void onJoin(PlayerJoinEvent e) {
				Player playerE = e.getPlayer();
				if (!playerE.isOp()) {
					playerE.hidePlayer(player);
				}
			}
		};
		if (tab)
			player.setPlayerListName(null);

		if (effect)
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 2));

		if (hard) {
			for (Player playerO : Bukkit.getOnlinePlayers())
				if (!playerO.isOp())
					playerO.hidePlayer(player);

			if (plugin != null)
				Bukkit.getPluginManager().registerEvents(vanish, plugin);
		}
	}

	/**
	 * Unvanish a player. NOTE: This method should only work on our method for
	 * vanish.
	 * 
	 * @param player
	 *            the player to unvanish
	 */
	public static void unvanish(Player player) {
		Validate.notNull(player, "player +-");

		Listener listener = (Listener) vanishListeners.get(player);
		if (listener != null) {
			PlayerJoinEvent.getHandlerList().unregister(listener);
		}
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		if (player.getPlayerListName() == null) {
			player.setPlayerListName(player.getName());
		}
	}

	/**
	 * Send action bar to a player. (The text bar that sometimes appears above the
	 * hotbar)
	 * 
	 * @param player
	 *            the player to send the action bar to.
	 * @param message
	 *            the message that the actionbar will contain.
	 */
	public static void sendActionBar(Player player, String message) {
		Validate.notNull(player, "player +-");
		Validate.notEmpty(message, "message +-");

		Object iChat = NMSUtils.NMSText.ofString(message);
		Object packet = null;

		try {
			packet = PacketUtils.create("PacketPlayOutChat", iChat, (byte) 2);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException
				| ClassNotFoundException e) {
			ExceptionHandler.getInstance().handle(e);

		}

		PlayerUtils.sendPacket(player, packet);
	}

	/**
	 * Get the NMS NetworkManager instance of
	 * 
	 * @param player
	 * @return
	 */
	public static Object getNetworkManager(Player player) {
		Validate.notNull(player, "player +-");

		Object con = getConnection(player);
		try {
			Field field = con.getClass().getDeclaredField("networkManager");
			return field.get(con);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Get the selected item of a player. With two hands this method will invoke
	 * {@link Player#getItemOnCursor()} while for one hand
	 * {@link Player#getItemInHand()} will be invoked
	 * 
	 * @param player
	 *            the player to get the selected item from
	 * @return the slected item, null for error
	 */
	public static ItemStack getSelectedItem(HumanEntity player) {
		Validate.notNull(player, "player +-");

		Method method = null;
		try {
			method = HumanEntity.class.getMethod("getItemOnCursor");
		} catch (NoSuchMethodException | SecurityException e) {
			try {
				method = HumanEntity.class.getMethod("getItemInHand");
			} catch (NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		}

		if (method == null)
			return null;

		try {
			return (ItemStack) method.invoke(player);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Check if one player equals
	 * 
	 * @param player1
	 *            the source player
	 * @param players
	 *            the player/s that should be equal to player1.
	 * @return true if they are all equal, else false
	 */
	public static boolean equals(Player player1, Player... players) {
		Validate.notNull(player1, "player1 +-");
		Validate.notNull(players, "players +-");
		for (Player player : players)
			if (!player.getUniqueId().equals(player1.getUniqueId()))
				return false;

		return true;
	}

	/**
	 * Add entry to the tab list of a specific player.
	 * 
	 * @param player
	 *            the player
	 * @param entries
	 *            the entries to add to the tab list
	 */
	public static void addTabListEntries(Player player, List<String> entries) {
		Validate.notNull(player, "player +-");
		Validate.notEmpty(entries, "entries +-");

		try {
			modifyTab(player,
					FieldUtils.readDeclaredStaticField(
							NMSUtils.getClass(true, "PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), "ADD_PLAYER"),
					entries.size(), entries);
		} catch (IllegalAccessException | ClassNotFoundException e) {
			ExceptionHandler.getInstance().handle(e);
		}

	}

	/**
	 * Modify the tab list of a player
	 * 
	 * @param player
	 *            the player to modify the tab to
	 * @param action
	 *            the action of EnumPlayerInfoAction
	 * @param length
	 * @param entries
	 *            the tab entries to add
	 */
	public static void modifyTab(Player player, Object action, int length, List<String> entries) {
		Validate.notNull(player, "player +-");
		Validate.notNull(action, "action +-");
		Validate.isTrue(NMSUtils.NMS_ENUMPLAYERINFOACTION.equals(action.getClass()),
				"action must be a EnumPlayerInfoAction");

		List<Object> nmsEP = new ArrayList<>();

		int i = 0;
		for (String entry : entries) {
			if (entry.length() > 16)
				throw new IllegalArgumentException("Entry in index " + i + " is larger than 16");

			nmsEP.add(createEntityPlayer(entry, UUID.randomUUID()));
			i++;
		}

		try {
			Object packet = PacketUtils.create("PacketPlayOutPlayerInfo", action, nmsEP);
			sendPacket(player, packet);
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException
				| NoSuchMethodException e) {
			ExceptionHandler.getInstance().handle(e);
		}

	}

	/**
	 * Create new entity player with the specified name and UUID
	 * 
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 * @return new entityplayer (nms) with the specified parameters
	 */
	public static Object createEntityPlayer(String name, UUID uuid) {
		Validate.notNull(name, "name +-");
		Validate.notNull(uuid, "uuid +-");

		try {
			return ConstructorUtils.invokeConstructor(NMSUtils.NMS_PLAYER,
					new Object[] { NMSUtils.getMinecraftServer(), NMSUtils.getHandle(Bukkit.getWorlds().get(0)),
							new GameProfile(uuid, name),
							ConstructorUtils.invokeConstructor(NMSUtils.NMS_PLAYERINTERACTMANAGER,
									new Object[] { NMSUtils.getHandle(Bukkit.getWorlds().get(0)) }) });
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
				| InstantiationException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

	/**
	 * Get protocol version of a specified player
	 * 
	 * @param player
	 *            the player
	 * @return the protocol version of the specified player
	 */
	public static int getProtocolVersion(Player player) {
		Validate.notNull(player, "player +-");

		try {
			Object ins = PlayerUtils.getNetworkManager(player);
			if (ins == null)
				return 0;
			return (int) MethodUtils.invokeMethod(ins, "getVersion", new Object[0]);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException e) {
			ExceptionHandler.getInstance().handle(e);
			return 0;
		}
	}

	/**
	 * Get the name of an object
	 * 
	 * @param object
	 *            the object
	 * @return the name of the object
	 */
	public static String getName(Object object) {
		if (object instanceof CommandSender)
			return ((CommandSender) object).getName();
		
		try {
			return MethodUtils.invokeMethod(object, "getName", new Object[0]).toString();
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
	}

}
