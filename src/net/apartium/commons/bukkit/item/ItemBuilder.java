package net.apartium.commons.bukkit.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.apartium.commons.Validate;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class ItemBuilder implements Cloneable {

	private ItemStack item;

	/**
	 * Create new item builder
	 * 
	 * @param item
	 *            basic item stack
	 */
	public ItemBuilder(ItemStack item) {

		Validate.notNull(item, "item +-");
		this.item = item;

	}

	/**
	 * Create new item builder
	 * 
	 * @param material
	 *            the material to use
	 * @param amount
	 *            amount of the items in the stack
	 */
	public ItemBuilder(Material material, int amount, String name, List<String> lore,
			Map<Enchantment, Integer> enchantments) {
		Validate.notNull(material, "material +-");

		ItemStack item = new ItemStack(material, amount <= 0 ? 1 : amount);

		this.item = item;
		if (name != null)
			this.setName(name);
		if (enchantments != null)
			this.addEnchantments(enchantments);
		if (lore != null)
			this.setLore(lore);

	}

	/**
	 * Create new item builder
	 * 
	 * @param material
	 *            the material to use
	 * @param amount
	 *            amount of the items in the stack
	 */
	public ItemBuilder(Material material, int amount) {
		this(material, amount, null, null, null);

	}

	/**
	 * Create new item builder
	 * 
	 * @param material
	 *            the material to use
	 * @param amount
	 *            amount of the items in the stack
	 * @param name
	 *            the name of the item
	 */
	public ItemBuilder(Material material, int amount, String name) {
		this(material, amount, name, null, null);

	}

	/**
	 * Create new item builder
	 * 
	 * @param material
	 *            the material to use
	 */
	public ItemBuilder(Material material) {
		this(material, 0, null, null, null);

	}

	/**
	 * Set name of the item
	 * 
	 * @param name
	 *            new name
	 * 
	 */
	public ItemBuilder setName(String name) {
		Validate.notEmpty(name, "name +-");
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		item.setItemMeta(im);

		return this;
	}

	/**
	 * Set durablity (or data in blocks) of item
	 * 
	 * @param data
	 *            the durability
	 * 
	 */
	public ItemBuilder setDurability(short data) {
		item.setDurability(data);
		return this;
	}

	/**
	 * Add an enchantment to the item builder
	 * 
	 * @param enchantment
	 *            the enchantment
	 * @param level
	 *            the level of the enchantment
	 * @param unsafe
	 *            is the enchant unsafe? (Level is higher than the maximum level)
	 */
	public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean unsafe) {
		Validate.notNull(enchantment, "enchantment +-");
		Validate.largerThan(level, 0, "level +- 0");
		if (!unsafe)
			item.addEnchantment(enchantment, level);
		else
			item.addUnsafeEnchantment(enchantment, level);

		return this;
	}

	/**
	 * Add an enchantment to the item builder
	 * 
	 * @param enchantment
	 *            the enchantment
	 * @param level
	 *            the level of the enchantment
	 */
	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		return addEnchantment(enchantment, level, false);
	}

	/**
	 * Add enchantment to this item builder
	 * 
	 * @param enchantments
	 *            the map of enchantment
	 * @param unsafe
	 *            allow unsafe or not
	 */
	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments, boolean unsafe) {
		if (!unsafe)
			this.item.addEnchantments(enchantments);
		else
			this.item.addUnsafeEnchantments(enchantments);
		return this;
	}

	/**
	 * Add enchantment to this item builder
	 * 
	 * @param enchantments
	 *            the map of enchantment
	 */
	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
		return addEnchantments(enchantments, false);
	}

	/**
	 * Set the lore for this item builder
	 * 
	 * @param lore
	 *            the lore to set
	 */
	public ItemBuilder setLore(List<String> lore) {
		ItemMeta im = item.getItemMeta();
		im.setLore(lore);
		return this;
	}

	/**
	 * Add line to the lore of this item
	 * 
	 * @param lines
	 *            the lines to add
	 * 
	 */
	public ItemBuilder addLoreLine(String... lines) {
		List<String> lore = getLore();
		lore.addAll(Arrays.asList(lines));
		setLore(lore);
		return this;
	}

	/**
	 * Set a lore line for this item builder on specified index
	 * 
	 * @param index
	 *            the index
	 * @param element
	 *            the element to set
	 * 
	 */
	public ItemBuilder setLore(int index, String element) {
		List<String> lore = getLore();
		lore.set(index, element);
		return this;
	}

	List<String> getLore() {
		if (!this.item.hasItemMeta() || !this.item.getItemMeta().hasLore())
			setLore(new ArrayList<>());

		return this.item.getItemMeta().getLore();
	}

	/**
	 * Clone this item builder
	 * 
	 * @return cloned item builder instance with the same parameters
	 */
	@Override
	public ItemBuilder clone() {
		try {
			return (ItemBuilder) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return the result of this builder
	 */
	public ItemStack getItem() {

		return item.clone();
	}

}
