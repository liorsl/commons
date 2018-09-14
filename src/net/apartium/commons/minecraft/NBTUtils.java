package net.apartium.commons.minecraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.reflect.ConstructorUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.inventory.ItemStack;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;
import net.apartium.commons.bukkit.item.ItemUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class NBTUtils {

	static Map<Class<?>, Method> nbtTagSetMethodIndex = new HashMap<>();

	static {
		for (Method method : NMSUtils.NMS_NBTTAGCOMPOUND.getDeclaredMethods()) {
			if (!method.getName().startsWith("set"))
				continue;
			
			nbtTagSetMethodIndex.put(method.getParameterTypes()[1], method);
		}

	}
	
	private NBTUtils() {}

	/**
	 * Set the NBT of an item to specified value
	 * 
	 * @param itemstack
	 *            the itemstack
	 * @param value
	 *            the instance of nbt tag interacter
	 */
	public static ItemStack setNBTTag(ItemStack itemstack, NBTTagInteracter value) {
		Validate.notNull(value, "value +-");
		
		try {
			return setNBTTag(itemstack, value.tag);
		} catch (NoSuchMethodException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}

	}

	/**
	 * Set the NBT of an item to specified value
	 * 
	 * @param itemstack
	 *            the itemstack
	 * @param values
	 *            the values
	 */
	public static ItemStack setNBTTag(ItemStack itemstack, Map<String, Object> values) {
		try {
			return setNBTTag(itemstack, createNBTTag(values));
		} catch (NoSuchMethodException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}

	}

	/**
	 * Set tag for specified item
	 * 
	 * @param itemstack
	 *            the item to set the tag to
	 * @param tag
	 *            the nbt tag
	 */
	public static ItemStack setNBTTag(ItemStack itemStack, Object tag) throws NoSuchMethodException {
		Validate.isTrue(NMSUtils.NMS_NBTTAGCOMPOUND.isAssignableFrom(tag.getClass()), "tag must be a NBTTagCompound");

		try {
			Object nms = ItemUtils.asNMSCopy(itemStack);
			MethodUtils.invokeMethod(nms, "setTag", tag);
			return ItemUtils.asBukkitCopy(nms);
		} catch (IllegalAccessException | InvocationTargetException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}

	}

	/**
	 * Create empty NBT tag
	 * 
	 * @return new empty NBT tag
	 */
	public static Object createNBTTag() {
		return createNBTTag(null);
	}

	/**
	 * Create NBT tag with the specified values
	 * 
	 * @param values
	 *            the values to add to the NBT tag
	 * @return new NBT tag with the specified values
	 */
	public static Object createNBTTag(Map<String, Object> values) {
		Object tag = null;
		try {
			tag = ConstructorUtils.invokeConstructor(NMSUtils.getClass(true, "NBTTagCompound"), new Object[0]);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException
				| ClassNotFoundException e) {
			ExceptionHandler.getInstance().handle(e);
			return null;
		}
		
		NBTTagInteracter interacter = new NBTTagInteracter(tag);
		if (values != null && !values.isEmpty()) 
			for (Entry<String, Object> entry : values.entrySet()) 
				interacter.set(entry.getKey(), entry.getValue());
			
		return tag;
	}

	public static Object getNBTTag(ItemStack item) {
		Validate.notNull(item, "item +-");
		
		try {
			return NMSUtils.NMS_ITEMSTACK.getMethod("getTag").invoke(ItemUtils.asNMSCopy(item));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	public static class NBTTagInteracter {
	
		private final Object
				tag;
		
		public NBTTagInteracter(Object tag) {
			Validate.notNull(tag, "tag +-");
			Validate.isTrue(NMSUtils.NMS_NBTTAGCOMPOUND.isAssignableFrom(tag.getClass()), "tag must be a NBTTagCompound");

			this.tag = tag;
		}
		
		public NBTTagInteracter(ItemStack item) {
			this(getNBTTag(item));
			
		}
		
		public void set(String key, Object value) {
			Validate.notEmpty(key, "key +-");
			Validate.notNull(value, "value +-");

			if (!nbtTagSetMethodIndex.containsKey(value.getClass()))
				return;

			Method method = nbtTagSetMethodIndex.get(value.getClass());
			try {
				method.invoke(tag, key, value);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				ExceptionHandler.getInstance().handle(e);
			}

		}
		
		public Object get(String key, Class<?> valueType) {
			Validate.notEmpty(key, "key +-");
			Validate.notNull(valueType, "valueType +-");

			if (!nbtTagSetMethodIndex.containsKey(valueType))
				return null;

			Method method = nbtTagSetMethodIndex.get(valueType);
			try {
				return method.invoke(tag, key);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				ExceptionHandler.getInstance().handle(e);
			}
			
			return null;
		}
		
	}
}
