package net.apartium.commons.bukkit.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
public class GUI implements Listener {

	List<Inventory> inventories = new ArrayList<>();

	Map<Player, Entry<Inventory, Integer>> views = new ConcurrentHashMap<>();

	Map<Integer, Map<ItemStack, ItemProperties>> content = new ConcurrentHashMap<>();

	Plugin plugin;

	int lines;
	String title;

	/**
	 * Create a new instance of GUI with the following parameters:
	 * 
	 * @param plugin
	 *            the plugin to register the listener to
	 * @param lines
	 *            the number of lines per page
	 * @param title
	 *            the title of the inventory
	 * 
	 *            placeholders: {PLAYER} - player's name {PLAYERDISPLAY} - player's
	 *            display name {PAGE} - the page
	 */
	public GUI(Plugin plugin, int lines, String title) {
		this.plugin = plugin;
		this.lines = lines;
		this.title = title;

		Bukkit.getPluginManager().registerEvents(this, this.plugin);

	}

	/**
	 * Get the number of lines per page
	 * 
	 * @return the number of lines per page for this GUI instance
	 */
	public int getLines() {
		return this.lines;
	}

	/**
	 * Get title for inventory
	 * 
	 * @param page
	 *            the page of the inventory
	 * @param player
	 *            the player that will view this page
	 * @return the processed title
	 */
	public String getTitle(int page, Player player) {
		return title.replaceAll("{PLAYER}", player.getName()).replaceAll("{PLAYERDISPLAY}", player.getDisplayName())
				.replaceAll("{PAGE}", page + "");
	}

	/**
	 * Return the clicked inventory The first element of the array is the view, the
	 * inventory shown to the player, the second is the source inventory
	 * 
	 * @param player
	 *            the player
	 * @param slot
	 *            the click slot
	 * @return array with 2 elements
	 */
	public Inventory[] getClickedInventory(Player player, int slot) {
		Validate.isTrue(slot <= getSize(), "slot can't be bigger than the size");
		Entry<Inventory, Integer> entry = views.get(player);

		if (entry == null)
			return null;

		return new Inventory[] { entry.getKey(), this.inventories.get(entry.getValue()) };
	}

	/**
	 * Get the combined size of all pages of this GUI instance
	 * 
	 * @return the combined size of all pages of this GUI instance
	 */
	public int getSize() {
		int i = 0;
		for (Inventory inv : this.inventories)
			i += inv.getSize();

		return i;
	}

	/**
	 * Get the number of pages for this GUI instance
	 * 
	 * @return the number of pages for this GUI instance
	 */
	public int getPages() {
		return this.inventories.size();
	}

	public Inventory createSimpleView(Player player, int page) {
		Validate.notNull(player, "player +-");
		Validate.largerThan(page, -1, "page must be 0 or bigger");

		Inventory view = Bukkit.createInventory(null, getLines() * 9, getTitle(page, player));

		for (Entry<ItemStack, ItemProperties> entry : getContent(page).entrySet())
			if (add(player, page, entry.getKey(), entry.getValue()))
				view.addItem(entry.getKey());

		return view;
	}

	/**
	 * Set item on this instance
	 * 
	 * @param page
	 *            the page of the item
	 * @param slot
	 *            the slot of the item in the page
	 * @param item
	 *            the item to set
	 * @param properties
	 *            the properties of the item
	 */
	public void setItem(int page, int slot, ItemStack item, ItemProperties properties) {
		Inventory inventory = this.inventories.get(page);

		inventory.setItem(slot, item);
		Map<ItemStack, ItemProperties> map = getContent(page);
		map.put(item, properties);

	}

	/**
	 * Add an item to the first available slot in specified page
	 * 
	 * @param page
	 *            the page
	 * @param item
	 *            the item
	 * @param properties
	 *            the item's properties
	 */
	public void addItem(int page, ItemStack item, ItemProperties properties) {
		setItem(page, this.inventories.get(page).firstEmpty(), item, properties);

	}

	/**
	 * Set specified page on this GUI instance to inventory
	 * 
	 * @param index
	 *            the index of the page
	 * @param inventory
	 *            the inventory to set to
	 */
	public void setPage(int index, Inventory inventory) {
		this.inventories.set(index, inventory);

	}

	/**
	 * Create new page built with specified inventory
	 * 
	 * @param inventory
	 *            the inventory
	 * @return the number of the new page
	 */
	public int createPage(Inventory inventory) {
		int cur = this.inventories.size();
		setPage(cur, inventory);
		this.content.put(cur, new ConcurrentHashMap<>());
		return cur;
	}

	/**
	 * Create a new page and returns its number
	 * @return the number of the page
	 */
	public int createPage() {
		return createPage(Bukkit.createInventory(null, getLines() * 9));
	}

	boolean add(Player player, int page, ItemStack itemStack, ItemProperties properties) {
		return true; // TODO
	}

	Map<ItemStack, ItemProperties> getContent(int index) {
		return this.content.get(index);
	}

	ItemProperties getProperties(int index, ItemStack item) {
		return getContent(index).get(item);
	}

	@EventHandler
	void onClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player) || !this.views.containsKey(event.getWhoClicked())
				|| !this.views.get(event.getWhoClicked()).getKey().equals(event.getInventory()))
			return;
		Player player = (Player) event.getWhoClicked();
		ItemProperties properties = getProperties(this.views.get(player).getValue(), event.getCurrentItem());
		if (properties != null && properties.getHandler() != null)
			properties.getHandler().click(this, event.getSlot(), event.getCurrentItem(), player, properties);

	}
}
