package net.apartium.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.WordUtils;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class StringUtils {

	private StringUtils() {}
	
	public static final String ALPHABET = "1234567890abcdefghijklmnopkrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * Completing a string.
	 * 
	 * @param input
	 *            the base string.
	 * @param end
	 *            the suffix to complete.
	 * @return the new string.
	 */
	public static String complete(String input, String end) {
		Validate.notEmpty(input, "input +-");
		Validate.notEmpty(end, "end +-");

		StringBuilder sb = new StringBuilder();
		int last = input.length() - 1;
		for (int i = end.length() - 1; i >= 0; i--)
			if (input.charAt(last) != end.charAt(i))
				sb.append(end.charAt(i));
			else
				last--;
		return input + sb.reverse();
	}

	/**
	 * Making a string pretty.
	 * 
	 * @param arg
	 *            the string.
	 * @param ignores
	 *            ignores for the process.
	 * @return the string very very pretty.
	 */
	public static String pretty(String arg, PrettyIgnore... ignores) {
		Validate.notEmpty(arg, "arg +-");
		Validate.notEmpty(ignores, "ignores +-");

		List<PrettyIgnore> i = Arrays.asList(ignores);

		if (!i.contains(PrettyIgnore.REPLACE_UNDERLINE))
			arg = arg.replace("_", " ");

		if (!i.contains(PrettyIgnore.REPLACE_HYPEN))
			arg = arg.replace("-", " ");

		if (!i.contains(PrettyIgnore.CAPITALIZE))
			arg = WordUtils.capitalize(arg);

		// TODO: Space chars.

		return arg;
	}

	/**
	 * Check if an object is Integer
	 * 
	 * @param arg
	 *            the object to check on
	 * @return true if it is an Integer, false if not
	 */
	public static boolean isInteger(Object arg) {
		Validate.notNull(arg, "arg cannot be null");

		if (arg instanceof Integer)
			return true;

		try {
			Integer.parseInt(arg instanceof String ? (String) arg : arg.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Check if an object is Double
	 * 
	 * @param arg
	 *            the object to check on
	 * @return true if it is an Double, false if not
	 */
	public static boolean isDouble(Object arg) {
		Validate.notNull(arg, "arg cannot be null");
		if (arg instanceof Double)
			return true;

		try {
			Double.parseDouble(arg instanceof String ? (String) arg : arg.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}

	}

	public static boolean isFloat(Object arg) {
		Validate.notNull(arg, "arg +-");
		if (arg instanceof Float)
			return true;

		try {
			Float.parseFloat(arg instanceof String ? (String) arg : arg.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isShort(Object arg) {
		Validate.notNull(arg, "arg +-");
		if (arg instanceof Short)
			return true;

		try {
			Short.parseShort(arg instanceof String ? (String) arg : arg.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isLong(Object arg) {
		Validate.notNull(arg, "arg +-");
		if (arg instanceof Long)
			return true;

		try {
			Long.parseLong(arg instanceof String ? (String) arg : arg.toString());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Fix the length of strings to specified size. (Adding spaces if the String is
	 * shorter than the length)
	 * 
	 * @param length
	 *            the required length
	 * @param strings
	 *            the strings to fix their lengths
	 * @return the fixed strings
	 */
	public static String[] fixLength(int length, String... strings) {
		List<String> ret = new ArrayList<>();
		for (String string : strings) {
			StringBuilder sb = new StringBuilder();
			sb.append(string);

			int curLength = string.length();
			for (int i = curLength; i < length; i++)
				sb.append(" ");

			ret.add(sb.toString());
		}

		return ret.toArray(new String[ret.size()]);
	}

	/**
	 * Fix the length of strings to the length of the longest strings in addition to
	 * the lengthAfter.
	 * 
	 * @param lengthAfter
	 *            how much spaces need to be added after the longest string's length
	 * @param strings
	 *            the strings to fix their lengths
	 * @return the fixed strings
	 */
	public static String[] autoFixLength(int lengthAfter, String... strings) {
		List<String> ret = new ArrayList<>();
		int length = 0;
		for (String string : strings)
			if (length < string.length())
				length = string.length();

		length += lengthAfter;

		for (String string : strings) {
			StringBuilder sb = new StringBuilder();
			sb.append(string);

			int curLength = string.length();
			for (int i = curLength; i < length; i++)
				sb.append(" ");

			ret.add(sb.toString());
		}

		return ret.toArray(new String[ret.size()]);
	}

	/**
	 * Build a string from array of strings
	 * 
	 * @param seperator
	 *            the separator between each member of the array
	 * @param args
	 *            the array of the string
	 * @return the combination of all of the strings with separator.
	 */
	public static String build(String seperator, String... args) {

		return build(seperator, 0, args);
	}

	/**
	 * Build a string from array of strings from a specified point in the array
	 * 
	 * @param seperator
	 *            the separator between each member of the array
	 * @param from
	 *            the index of the array to string from, default: 0 (Begin building
	 *            from the start)
	 * @param args
	 *            the array of the string
	 * @return the combination of all of the strings from the specified point of the
	 *         array with separator.
	 */
	public static String build(String seperator, int from, String... args) {
		StringBuilder sb = new StringBuilder();
		for (int i = from; i < args.length; i++) {
			sb.append(args[i]);

			if (i < args.length)
				sb.append(seperator);
		}

		return sb.toString();
	}

	public static class UUIDUtils {

		/**
		 * Checking if a string is a UUID.
		 * 
		 * @param arg
		 *            the string argument.
		 * @return if the string is a UUID.
		 */
		public static boolean isUUID(String arg) {
			if (arg == null)
				return false;
			return arg.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
		}

		/**
		 * Adding hyphens to UUID.
		 * 
		 * @param input
		 *            the base UUID.
		 * @return the new UUID.
		 */
		public static String addHyphens(String input) {
			String uuid = input.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

			return uuid;
		}

	}

	/**
	 * Check if a String is empty.
	 * 
	 * @param string
	 *            the string to check on
	 * @return true if empty, else false
	 */
	public static boolean isEmpty(String string) {
		return string != null && string.isEmpty();
	}

	public enum PrettyIgnore {
		REPLACE_UNDERLINE, REPLACE_HYPEN, CAPITALIZE, SPACE_CHARS;
	}

}
