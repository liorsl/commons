package net.apartium.commons.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public interface ClickHandler {

	/**
	 * This method will be invoked when an item assinged with this ClickHandler
	 * instance will be clicked
	 * 
	 * @param gui
	 *            the clicked GUI
	 * @param slot
	 *            the clicked slot
	 * @param item
	 *            the clicked item
	 * @param player
	 *            the player
	 * @param properties
	 *            the item's properties
	 * @return should the event be cancelled or not
	 */
	boolean click(GUI gui, int slot, ItemStack item, Player player, ItemProperties properties);
}
