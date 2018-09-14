package net.apartium.commons.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.NonNull;
import net.apartium.commons.ExceptionHandler;
import net.apartium.commons.Validate;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class InvocationUtils {
	
	private InvocationUtils() {}
	
	/**
	 * get PlusInvocationHandler instance by proxied object
	 * 
	 * @param object
	 *            the object of the instance
	 * @return the instance that the object handled by
	 */
	public static ApartiumInvocationHandler getHandler(Object object) {
		Validate.notNull(object, "object +-");

		return ApartiumInvocationHandler.handlers.get(object);
	}

	/**
	 * extract invocation extender from object
	 * 
	 * @param object
	 *            the object to get the extender from
	 * @param type
	 *            the type of the extender
	 * @return the field of the extender and its instance
	 */
	public static Entry<Field, Object> extractInvocationExtender(Object object, Class<?> type) {
		Validate.notNull(object, "object +-");
		Validate.notNull(type, "type +-");

		return extractInvocationExtender(getHandler(object), type);
	}

	/**
	 * extract invocation extender from object
	 * 
	 * @param handler
	 *            the PlusInvocationHandler instance
	 * @param type
	 *            the type of the extender
	 * @return the field of the extender and its instance
	 */
	public static Entry<Field, Object> extractInvocationExtender(ApartiumInvocationHandler handler, Class<?> type) {
		Validate.notNull(handler, "handler +-");
		Validate.notNull(type, "type +-");

		for (Field field : handler.extendInvocation)
			if (field.getType().getName().equals(type.getName()))
				try {
					field.setAccessible(true);
					return new AbstractMap.SimpleEntry<>(field, field.get(handler.invokerInstance));
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}

		return null;
	}

	/**
	 * extract invocation extender from object
	 * 
	 * @param handler
	 *            the PlusInvocationHandler instance
	 * @param fieldName
	 *            the name of the field
	 * @return the field of the extender and its instance
	 */
	public static Entry<Field, Object> extractInvocationExtender(ApartiumInvocationHandler handler, String fieldName) {
		Validate.notNull(handler, "handler +-");
		Validate.notEmpty(fieldName, "fieldName +-");

		for (Field field : handler.extendInvocation)
			if (field.getName().equals(fieldName))
				try {
					field.setAccessible(true);
					return new AbstractMap.SimpleEntry<>(field, field.get(handler.invokerInstance));
				} catch (IllegalArgumentException | IllegalAccessException e) {
				}
		return null;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface ExtendInvocationProxy {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface IgnoreInvocationProxy {
	}

	public static class ApartiumInvocationHandler implements InvocationHandler {

		protected final static Map<Object, ApartiumInvocationHandler> handlers = new HashMap<>();

		protected final List<Field> extendInvocation = new ArrayList<>();

		protected final Class<?> in, invoker;

		protected final Object invokerInstance;

		protected Object proxied;

		protected <I> ApartiumInvocationHandler(Class<?> in, Class<I> invoker, I instance)
				throws IllegalArgumentException, IllegalAccessException {
			Validate.notNull(in, "in +-");
			Validate.notNull(invoker, "invoker +-");
			Validate.notNull(instance, "instance +-");

			this.in = in;

			this.invoker = invoker;
			this.invokerInstance = instance;

			for (Field field : invoker.getFields())
				if (field.isAnnotationPresent(ExtendInvocationProxy.class)) {
					field.setAccessible(true);
					extendInvocation.add(field);
				}

			for (Field field : invoker.getDeclaredFields())
				if (field.isAnnotationPresent(ExtendInvocationProxy.class) && !extendInvocation.contains(field)) {
					field.setAccessible(true);
					extendInvocation.add(field);
				}

		}

		@Override
		public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			Method m = getMethod(invoker, method.getName(), method.getParameterTypes());
			if (m != null && !m.isAnnotationPresent(IgnoreInvocationProxy.class))
				return handleInvoke(invokerInstance, m, args);

			for (Field field : extendInvocation) {
				if (field == null)
					continue;

				Method met = getMethod(field, method.getName(), method.getParameterTypes());

				if (met != null)
					return handleExtenderInvoke(field, met, args);
			}

			return null;
		}

		/**
		 * invoke method with instance from classes field
		 * 
		 * @param instance
		 *            the invoking instance
		 * @param method
		 *            the invoked method
		 * @param arguments
		 *            the required arguments for the method
		 * @return the method return
		 */
		protected Object handleInvoke(Object instance, Method method, Object... arguments) {
			Validate.notNull(instance, "instance +-");
			Validate.notNull(method, "method +-");
			Validate.notEmpty(arguments, "arguments +-");

			if (method == null)
				throw new NullPointerException("Method can't be null");

			try {
				return method.invoke(instance, arguments == null ? new Object[0] : arguments);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				return null;
			}
		}

		/**
		 * 
		 * @param field
		 *            the extender field used to invoke this method
		 * @param method
		 *            the method to invoke
		 * @param arguments
		 *            the arguments to use
		 * @return the method's return
		 */
		protected Object handleExtenderInvoke(@NonNull Field field, @NonNull Method method, Object... arguments)
				throws IllegalArgumentException, IllegalAccessException {
			Validate.notEmpty(arguments, "arguments +-");
			
			if (!field.isAccessible())
				field.setAccessible(true);
			return handleInvoke(field.get(invokerInstance), method, arguments);
		}

		/**
		 * get method from a class
		 * 
		 * @param c
		 *            the class
		 * @param mName
		 *            the name of the method
		 * @param args
		 *            the paramaters of the method
		 * @return the requested method
		 */
		protected Method getMethod(Class<?> c, String mName, Class<?>... args) {
			Validate.notNull(c, "c +-");
			Validate.notEmpty(mName, "mName +-");
			Validate.notEmpty(args, "args +-");

			try {
				Method m = c.getMethod(mName, args);
				if (m != null && !m.isAnnotationPresent(IgnoreInvocationProxy.class))
					return m;
			} catch (NoSuchMethodException | SecurityException e) {
				try {
					Method m = c.getDeclaredMethod(mName, args);
					if (m != null && !m.isAnnotationPresent(IgnoreInvocationProxy.class))
						return m;
				} catch (NoSuchMethodException | SecurityException e1) {
					ExceptionHandler.getInstance().handle(e);

				}
			}

			return null;

		}

		/**
		 * get method from extender field
		 * 
		 * @param extender
		 *            the field to get the method from
		 * @param mName
		 *            the method's name
		 * @param args
		 *            the arguments to use
		 * @return the method, null if not exists / error
		 */
		protected Method getMethod(Field extender, String mName, Class<?>... args) {
			return getMethod(extender.getType(), mName, args);
		}

		/**
		 * get the interface of this invocation handler
		 * 
		 * @return the interface of this invocation handler
		 */
		public final Object getInterface() {
			if (proxied == null) {
				this.proxied = Proxy.newProxyInstance(in.getClassLoader(), new Class[] { in }, this);
				handlers.put(proxied, this);
			}

			return proxied;
		}
	}
}
