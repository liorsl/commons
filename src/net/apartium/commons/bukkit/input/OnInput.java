package net.apartium.commons.bukkit.input;

import org.bukkit.entity.Player;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public interface OnInput {

	/**
	 * This method will be invoked when a input is received
	 * 
	 * @param player
	 *            the player
	 * @param input
	 *            the input
	 */
	void onInput(Player player, String input);
}
