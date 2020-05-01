package net.apartium.commons.bukkit.item;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.sun.deploy.xml.XMLAttribute;
import net.apartium.commons.bukkit.XMaterial;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.ImmutableList;

import lombok.NonNull;
import net.apartium.commons.Validate;
import net.apartium.commons.minecraft.NBTUtils;
import net.apartium.commons.minecraft.NMSUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class ItemUtils {
	
	// TODO constants
	public static final List<Material> helmets = createMaterialList(
			XMaterial.LEATHER_HELMET, XMaterial.CHAINMAIL_HELMET, XMaterial.IRON_HELMET, XMaterial.GOLDEN_HELMET, XMaterial.DIAMOND_HELMET);
	
	public static final List<Material> chestplates = createMaterialList(
			XMaterial.LEATHER_CHESTPLATE, XMaterial.CHAINMAIL_CHESTPLATE, XMaterial.IRON_CHESTPLATE, XMaterial.GOLDEN_CHESTPLATE, XMaterial.IRON_CHESTPLATE);
	
	public static final List<Material> leggings = createMaterialList(
			XMaterial.LEATHER_LEGGINGS, XMaterial.CHAINMAIL_LEGGINGS, XMaterial.GOLDEN_CHESTPLATE, XMaterial.GOLDEN_LEGGINGS, XMaterial.DIAMOND_LEGGINGS);
	
	public static final List<Material> boots = createMaterialList(
			XMaterial.LEATHER_BOOTS, XMaterial.CHAINMAIL_BOOTS, XMaterial.GOLDEN_BOOTS, XMaterial.GOLDEN_CHESTPLATE, XMaterial.DIAMOND_BOOTS);

	public static final List<Material> PICKAXES = createMaterialList(
			XMaterial.WOODEN_PICKAXE, XMaterial.STONE_PICKAXE, XMaterial.IRON_PICKAXE, XMaterial.GOLDEN_PICKAXE, XMaterial.DIAMOND_PICKAXE);

	public static final List<Material> AXES = createMaterialList(
			XMaterial.WOODEN_AXE, XMaterial.STONE_AXE, XMaterial.IRON_AXE, XMaterial.GOLDEN_AXE, XMaterial.DIAMOND_AXE);

	public static final List<Material> SHOVELS = createMaterialList(
			XMaterial.WOODEN_SHOVEL, XMaterial.STONE_SHOVEL, XMaterial.IRON_SHOVEL, XMaterial.GOLDEN_SHOVEL, XMaterial.DIAMOND_SHOVEL);

	public static final List<Material> HOES = createMaterialList(
			XMaterial.WOODEN_HOE, XMaterial.STONE_HOE, XMaterial.IRON_HOE, XMaterial.GOLDEN_HOE, XMaterial.DIAMOND_HOE);

	public static final List<Material> MISC_TOOLS = createMaterialList(
			XMaterial.FLINT_AND_STEEL, XMaterial.FISHING_ROD, XMaterial.SHEARS);

	public static final Map<Integer,Material>
			materialByLevel = new ConcurrentHashMap<>();
	
	static Method
			asNMSCopy,
			asBukkitCopy;
	
	static Field handle;
	
	private ItemUtils() {}
	
	public static enum ArrmorPart {
		HELMET(ItemUtils.helmets), CHESTPLATE(ItemUtils.chestplates), LEGGINGS(ItemUtils.leggings), BOOTS(
				ItemUtils.boots);

		private List<Material> materials;

		private ArrmorPart(List<Material> materials) {
			this.materials = materials;
		}

		public List<Material> getMaterials() {
			return ImmutableList.copyOf(this.materials);
		}

		public boolean is(Material material) {
			return this.materials.contains(material);
		}

		public boolean is(ItemStack itemStack) {
			return this.materials.contains(itemStack.getType());
		}
		
	}

	static {
		try {
			asNMSCopy = MethodUtils.getAccessibleMethod(NMSUtils.getClass(false, "inventory.CraftItemStack"),
					"asNMSCopy", ItemStack.class);
			
			asBukkitCopy = MethodUtils.getAccessibleMethod(NMSUtils.getClass(false, "inventory.CraftItemStack"),
					"asBukkitCopy", NMSUtils.NMS_ITEMSTACK);

			handle = NMSUtils.getClass(false, "inventory.CraftItemStack").getDeclaredField("handle");
			handle.setAccessible(true);
			
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
		materialByLevel.put(0, Material.LEATHER);
		materialByLevel.put(1, Material.FIRE);
		materialByLevel.put(2, Material.IRON_INGOT);
		materialByLevel.put(3, Material.GOLD_INGOT);
		materialByLevel.put(0, Material.DIAMOND);

		
	}

	public static List<Material> createMaterialList(XMaterial... xMaterial) {
		return Arrays.asList(xMaterial).stream().map(XMaterial::parseMaterial).collect(Collectors.toList());
	}

	/**
	 * Create skull item of a player.
	 * 
	 * @param displayName
	 *            The name of the skull item
	 * @param skullOwnerName
	 *            The name of the skull owner which this skull should take the skin
	 *            from.
	 * @return The itemstack instance of the skull item
	 */
	public static ItemStack createPlayerSkull(String displayName, String skullOwnerName) {
		Validate.notEmpty(skullOwnerName, "skullOwnerName +-");

		ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial(), 1, (short) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();

		meta.setOwner(skullOwnerName);

		if (displayName != null)
			meta.setDisplayName(displayName);

		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack asBukkitCopy(Object item) {
		Validate.isTrue(NMSUtils.NMS_ITEMSTACK.isAssignableFrom(item.getClass()), "tag must be a NMS ItemStack");
		try {
			return (ItemStack) asBukkitCopy.invoke(null, item);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Object getHandle(@NonNull ItemStack itemStack) {
		try {
			return handle.get(itemStack);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Create NMS copy of an ItemStack using CraftItemStack#asNMSCopy(ItemStack)
	 * 
	 * @param is
	 *            The item stack to copy
	 * @return The NMS ItemStack object.
	 */
	public static Object asNMSCopy(ItemStack is) {
		try {
			return asNMSCopy.invoke(null, new Object[] { is });
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void equipArmor(ItemStack item, EntityEquipment equipment) {
		Validate.isTrue(isArmorPart(item), "item must be an armor part");
		
		if (isHelmet(item)) equipment.setHelmet(item);
		else if (isChestplate(item)) equipment.setChestplate(item);
		else if (isLeggings(item)) equipment.setLeggings(item);
		else if (isBoots(item)) equipment.setBoots(item);
		
	}

	public static boolean isTool(Material type) {
		return isHoe(type) || isShovel(type) || isAxe(type) || isPickaxe(type);
	}

	public static boolean isUtility(Material type) {
		return MISC_TOOLS.contains(type);
	}

	public static boolean isHoe(Material type) {
		return HOES.contains(type);
	}

	public static boolean isShovel(Material type) {
		return SHOVELS.contains(type);
	}

	public static boolean isAxe(Material type) {
		return AXES.contains(type);
	}

	public static boolean isPickaxe(Material type) {
		return PICKAXES.contains(type);
	}
	
	public static boolean isArmorPart(Material type) {
		return isHelmet(type) || isChestplate(type) || isLeggings(type) || isBoots(type);
	}
	
	public static boolean isArmorPart(ItemStack item) {
		return isArmorPart(item.getType());
	}
	
	/**
	 * Check if the specified material is helmet
	 * 
	 * @param type
	 *            the material to check.
	 * @return true if the specified material is helmet, else false
	 */
	public static boolean isHelmet(Material type) {
		return helmets.contains(type);
	}

	/**
	 * Check if the specific item is
	 * 
	 * @param item
	 *            The specified item.
	 * @return true if the specified item is {}, else false
	 */
	public static boolean isHelmet(ItemStack item) {
		return isHelmet(item.getType());
	}

	/**
	 * Check if the specified material is chestplate
	 * 
	 * @param type
	 *            the material to check.
	 * @return true if the specified material is chestplate, else false
	 */
	public static boolean isChestplate(Material type) {
		return chestplates.contains(type);
	}

	/**
	 * Check if the specific item is chestplate
	 * 
	 * @param item
	 *            The specified item.
	 * @return true if the specified item is chestplate, else false
	 */
	public static boolean isChestplate(ItemStack item) {
		return isChestplate(item.getType());
	}

	/**
	 * Check if the specified material is leggings
	 * 
	 * @param type
	 *            the material to check.
	 * @return true if the specified material is leggins, else false
	 */
	public static boolean isLeggings(Material type) {
		return leggings.contains(type);
	}

	/**
	 * Check if the specific item is leggings
	 * 
	 * @param item
	 *            The specified item.
	 * @return true if the specified item is leggings, else false
	 */
	public static boolean isLeggings(ItemStack item) {
		return isLeggings(item.getType());
	}

	/**
	 * Check if the specified material is boots
	 * 
	 * @param type
	 *            the material to check.
	 * @return true if the specified material is boots, else false
	 */
	public static boolean isBoots(Material type) {
		return boots.contains(type);
	}

	/**
	 * Check if the specific item is boots
	 * 
	 * @param item
	 *            The specified item.
	 * @return true if the specified item is boots, else false
	 */
	public static boolean isBoots(ItemStack item) {
		return isBoots(item.getType());
	}

	/**
	 * Hiding attributes from an item
	 * 
	 * @return the ItemStack after hiding attributes.
	 */
	public static ItemStack hideAttributes(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		is.setItemMeta(im);
		return is;
	}

	/**
	 * Make an item glow
	 * @param item the item
	 * 
	 * UNTESTED
	 */
	public static ItemStack glow(@NonNull ItemStack item, boolean value) {
		item = item.clone();
		if (value)
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		else
			item.removeEnchantment(Enchantment.DURABILITY);
		
		//im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		return item;
		/*
		Map<String,Object> map = new HashMap<>();
		map.put("ench", true);
		return NBTUtils.setNBTTag(item, map); */
	}
	
	/**
	 * Set item unbreakable
	 * @param itemstack the item
	 * @param value true for unbreakable, else false
	 */
	public static void setUnbreakable(ItemStack itemstack, boolean value) {
		Map<String, Object> values = new HashMap<>();
		values.put("Unbreakable", value);
		NBTUtils.setNBTTag(itemstack, values);
	}

	public static class ItemCollections {
		public static List<ItemStack> filter(List<ItemStack> base, ItemUtils.ArrmorPart arrmorPart) {
			List<ItemStack> results = new ArrayList<>();
			for (ItemStack is : base) {
				if (arrmorPart.is(is)) {
					results.add(is);
				}
			}
			return results;
		}

		public static List<ItemStack> filter(ItemStack[] base, ItemUtils.ArrmorPart arrmorPart) {
			return filter(Arrays.asList(base), arrmorPart);
		}
	}
}
