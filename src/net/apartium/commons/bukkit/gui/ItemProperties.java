package net.apartium.commons.bukkit.gui;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class ItemProperties {

	ClickHandler handler;

	/**
	 * Create new instance of ItemProperties with the following paramaters:
	 * 
	 * @param handler
	 *            the click handler
	 */
	public ItemProperties(ClickHandler handler) {
		this.handler = handler;
	}

	/**
	 * Get the click handler of this properties instance
	 * @return the click handler, null if none
	 */
	public ClickHandler getHandler() {
		return this.handler;
	}
}
