package net.apartium.commons.reflect;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import lombok.experimental.UtilityClass;
import net.apartium.commons.Validate;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
@UtilityClass
public class Primitives {

	private static final BiMap<Class<?>, Class<?>>
			PRIMITIVES = HashBiMap.create();
	
	static {
		PRIMITIVES.put(int.class, Integer.class);
		PRIMITIVES.put(long.class, Long.class);
		PRIMITIVES.put(double.class, Double.class);
		PRIMITIVES.put(float.class, Float.class);
		PRIMITIVES.put(boolean.class, Boolean.class);
		PRIMITIVES.put(char.class, Character.class);
		PRIMITIVES.put(byte.class, Byte.class);
		PRIMITIVES.put(short.class, Short.class);
		PRIMITIVES.put(void.class, Void.class);
		
	}
	
	public static boolean isWrapped(Class<?> type) {
		return PRIMITIVES.containsValue(type);
	}
	
	public static Class<?> getWrappedType(Class<?> primitiveType) {
		Validate.notNull(primitiveType, "primitiveType +-");
		Validate.isTrue(PRIMITIVES.containsKey(primitiveType), "primitiveType must be primitive!");
		
		return PRIMITIVES.get(primitiveType);
	}
	
}
