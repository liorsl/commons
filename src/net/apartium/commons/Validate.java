package net.apartium.commons;

import java.util.Collection;
import java.util.Map;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class Validate {
	
	private Validate() {}
	
	/**
	 * Validate if a String is empty or not
	 * 
	 * @param string
	 *            the String to validate on
	 * @param ex
	 *            the exception to throw if the String is empty
	 */
	public static void notEmpty(String string, Exception ex) {
		if (ex == null)
			ex = new NullPointerException();

		if (StringUtils.isEmpty(string))
			try {
				throw ex;
			} catch (Exception e) {
				ExceptionHandler.getInstance().handle(e);

			}
	}

	/**
	 * Check if an object is null.
	 * 
	 * @param obj
	 *            the object to check on
	 * @param ex
	 *            the exception to throw if the object is null
	 */
	public static void notNull(Object obj, RuntimeException ex) {
		if (ex == null)
			ex = new NullPointerException();

		if (obj == null)
			throw ex;

	}

	/**
	 * Make sure that the collection is not null or empty
	 * 
	 * @param collection
	 *            the collection to check
	 * @param ex
	 *            the exception to throw if the object is invalid
	 */
	public static void notEmpty(Collection<?> collection, RuntimeException ex) {
		Validate.notNull(collection, ex);
		if (collection.size() > 0)
			try {
				throw ex;
			} catch (Exception e) {
				ExceptionHandler.getInstance().handle(e);
			}

	}

	/**
	 * Make sure that the collection is not null or empty
	 * 
	 * @param collection
	 *            the collection to check
	 * @param message
	 *            the message, "+-" will be replaced to "cannot be empty"
	 */
	public static void notEmpty(Collection<?> collection, String message) {
		notEmpty(collection, new NullPointerException(message.replaceAll("\\+-", "cannot be empty")));
	}

	/**
	 * Make sure that the map is not null or empty
	 * 
	 * @param map
	 *            the map to check
	 * @param ex
	 *            the exception to throw if the object is invalid
	 */
	public static void notEmpty(Map<?, ?> map, RuntimeException ex) {
		Validate.notNull(map, ex);
		Validate.notNull(map.entrySet(), ex);

		if (map.size() > 0)
			try {
				throw ex;
			} catch (Exception e) {
				ExceptionHandler.getInstance().handle(e);
			}

	}

	/**
	 * Make sure that the map is not null or empty
	 * 
	 * @param map
	 *            the map to check
	 * @param message
	 *            the message, "+-" will be replaced to "cannot be empty"
	 */
	public static void notEmpty(Map<?, ?> map, String message) {
		notEmpty(map, new NullPointerException(message.replaceAll("\\+-", "cannot be empty")));
	}

	/**
	 * Make sure that the array is not null or empty
	 * 
	 * @param array
	 *            the array to check
	 * @param ex
	 *            the exception to throw if the object is invalid
	 */
	public static void notEmpty(Object[] array, RuntimeException ex) {
		Validate.notNull(array, ex);

		if (array.length > 0)
			try {
				throw ex;
			} catch (Exception e) {
				ExceptionHandler.getInstance().handle(e);
			}

	}

	/**
	 * Make sure that the array is not null or empty
	 * 
	 * @param array
	 *            the array to check
	 * @param message
	 *            the message, "+-" will be replaced to "cannot be empty"
	 */
	public static void notEmpty(Object[] array, String message) {
		notEmpty(array, new NullPointerException(message.replaceAll("\\+-", "cannot be empty")));
	}

	/**
	 * Check if an object is null.
	 * 
	 * @param obj
	 *            the object to check on
	 * @param message
	 *            the message to throw with the exception if the object is null
	 */
	public static void notNull(Object obj, String message) {
		notNull(obj, new NullPointerException(message.replaceAll("\\+-", "cannot be null")));
	}

	/**
	 * Validate if a String is empty or not
	 * 
	 * @param string
	 *            the String to validate on
	 * @param message
	 *            the message to throw with the exception if the String is empty
	 */
	public static void notEmpty(String string, String message) {
		notEmpty(string, new NullPointerException(message.replaceAll("\\+-", "cannot be empty / null")));
	}

	/**
	 * Check if a number is larger than another number
	 * 
	 * @param num1
	 *            the first number
	 * @param num2
	 *            the second number
	 * @param ex
	 *            the exception to throw if not larger than
	 * 
	 */
	public static void largerThan(int num1, int num2, Exception ex) {
		if (num1 <= num2)
			if (ex != null)
				try {
					throw ex;
				} catch (Exception e1) {
					ExceptionHandler.getInstance().handle(e1);
				}
	}

	/**
	 * Make sure that num1 is larger than num2
	 * 
	 * @param num1
	 *            the first number
	 * @param num2
	 *            the second number
	 * @param message
	 *            the message of the exception, "+-" will be replaced with " must be
	 *            larger than "
	 */
	public static void largerThan(int num1, int num2, String message) {
		largerThan(num1, num2, new NullPointerException(message.replaceAll("\\+-", " must be larger than ")));

	}
	/**
	 * Make sure that the boolean is true, else throw an exception
	 * 
	 * @param bool
	 *            the boolean
	 */
	public static void isTrue(boolean bool) {
		isTrue(bool, new NullPointerException());

	}

	/**
	 * 
	 * Make sure that the boolean is true, else throw an exception
	 * 
	 * @param bool
	 *            the boolean
	 * 
	 * @param message
	 *            the message to send with the exception, "+-" will be replaced with
	 *            "must be true"
	 */
	public static void isTrue(boolean bool, String message) {
		if (message.contains("+-")) message = message.replaceAll("+-", "must be true");
		
		isTrue(bool, new NullPointerException(message));

	}

	/**
	 * 
	 * Make sure that the boolean is true, else throw an exception
	 * 
	 * @param bool
	 *            the boolean
	 * 
	 * @param e
	 *            the exception to throw if the boolean is false
	 */
	public static void isTrue(boolean bool, Exception e) {
		if (e == null)
			e = new Exception();

		if (!bool)
			try {
				throw e;
			} catch (Exception e1) {
				ExceptionHandler.getInstance().handle(e1);
			}

	}


}
