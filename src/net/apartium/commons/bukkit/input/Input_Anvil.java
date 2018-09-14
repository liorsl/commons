package net.apartium.commons.bukkit.input;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.apartium.commons.Validate;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class Input_Anvil implements Listener {

	Plugin plugin;

	Player player;

	Inventory inventory;

	ItemStack item;

	OnInput onInput;

	/**
	 * Create a new instance of Input_Anvil with the following parameters:
	 * 
	 * @param plugin
	 *            the plugin to register the listener to
	 * @param player
	 *            the player to input from
	 * @param onInput
	 *            the action that will be invoked when the input is received
	 */
	public Input_Anvil(Plugin plugin, Player player, OnInput onInput) {
		Validate.notNull(plugin, "plugin +-");
		Validate.notNull(player, "player +-");
		Validate.notNull(onInput, "onInput +-");

		this.player = player;
		this.plugin = plugin;

		this.item = new ItemStack(Material.NAME_TAG);

		this.inventory = Bukkit.createInventory(player, InventoryType.ANVIL);
		this.inventory.setItem(0, this.item);

		player.openInventory(this.inventory);

		Bukkit.getPluginManager().registerEvents(this, this.plugin);

	}

	@EventHandler
	void onClick(InventoryClickEvent event) {
		if (!event.getWhoClicked().equals(player))
			return;
		Player player = (Player) event.getWhoClicked();

		String name = event.getCurrentItem().getItemMeta().getDisplayName();
		this.onInput.onInput(player, name);

	}

}
