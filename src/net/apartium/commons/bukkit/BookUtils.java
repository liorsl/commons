package net.apartium.commons.bukkit;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;
import net.apartium.commons.bukkit.item.ItemUtils;
import net.apartium.commons.minecraft.NMSUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class BookUtils {

	static {
		try {
			openBook = NMSUtils.NMS_PLAYER.getMethod("a",
					new Class[] { NMSUtils.getClass(true, "ItemStack"), NMSUtils.getClass(true, "EnumHand") });
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			ExceptionHandler.getInstance().handle(e);
		}
	}
	
	private BookUtils() {}
	
	private static Method openBook;

	/**
	 * Open a book for a player.
	 * 
	 * @param player
	 *            The player to open the book to.
	 * @param book
	 *            The book to open.
	 */
	public static void openBook(Player player, ItemStack book) {
		Validate.notNull(player, "player +-");
		Validate.notNull(book, "book +-");

		if (!(book.getItemMeta() instanceof BookMeta))
			return;

		ItemStack held = player.getInventory().getItemInMainHand();

		try {
			player.getInventory().setItemInMainHand(book);
			Object entityPlayer = NMSUtils.getHandle(player);
			Class<?> enumHand = NMSUtils.getClass(true, "EnumHand");
			Object[] enumArray = enumHand.getEnumConstants();
			openBook.invoke(entityPlayer, new Object[] { ItemUtils.asNMSCopy(book), enumArray[0] });
		} catch (ReflectiveOperationException e) {
			ExceptionHandler.getInstance().handle(e);
		}

		player.getInventory().setItemInMainHand(held);
	}

	/**
	 * Set the content of a book
	 * 
	 * @param metadata
	 *            The book's meta.
	 * @param pages
	 *            The pages to set
	 */
	public static void setPages(BookMeta metadata, List<String> pages) {
		try {
			@SuppressWarnings("unchecked")
			List<Object> p = (List<Object>) FieldUtils
					.getField(NMSUtils.getClass(false, "inventory.CraftMetaBook"), "pages").get(metadata);
			for (String text : pages) {
				Object page = MethodUtils.invokeMethod(
						NMSUtils.getClass(true, "IChatBaseComponent$ChatSerializer").newInstance(), "a", text);
				p.add(page);
			}
		} catch (Exception e) {
			ExceptionHandler.getInstance().handle(e);
		}
	}
}
