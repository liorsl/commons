package net.apartium.commons.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class CloneUtils {

	private CloneUtils() {}
	
	public static <T> T clone(Class<T> clazz, T base, T instance)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		Validate.notNull(clazz, "clazz +-");
		Validate.notNull(base, "base +-");
		Validate.notNull(instance, "instance +-");

		if (instance == null)
			instance = clazz.newInstance();
		
		if (instance instanceof Cloneable) {
			try {
				Method clone = instance.getClass().getMethod("clone");
				clone.invoke(instance);
			} catch (NoSuchMethodException | SecurityException | InvocationTargetException e) {
				ExceptionHandler.getInstance().handle(e);
			}
		}
		List<Field> fields = new ArrayList<>();
		fields.addAll(Arrays.asList(clazz.getFields()));
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		for (Field field : fields)
			field.set(instance, field.get(base));

		return instance;
	}
}
