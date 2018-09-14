package net.apartium.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class RandomUtils {

	private static final String ALPHABET = "1234567890abcdefghijklmnopkrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static volatile java.util.Random random = new java.util.Random();
	
	private RandomUtils() {}
	
	/**
	 * Choose a key from the map by percentage calculation (the value)
	 * 
	 * @param values
	 *            the map
	 * @return the chosen key from the map
	 */
	public static <T> T getKey(Map<T, Integer> values) {
		Validate.notEmpty(values, "values +-");

		int total = 0;
		for (int n : values.values())
			total += n;

		int r = randomInt(total - 1);

		T obj = null;
		List<Entry<T, Integer>> list = new ArrayList<Entry<T, Integer>>();
		for (Entry<T, Integer> e : values.entrySet())
			list.add(e);

		for (int i = 0; i < list.size(); i++) {
			int min = (i == 0 ? 0 : getValue(list, i));
			int max = ((i + 1) >= list.size() ? total : getValue(list, i + 1));

			if (r >= min && r < max) {
				obj = list.get(i).getKey();
				break;
			}
		}
		return obj;
	}

	/**
	 * Get the value of specified in the entry of a list in specified index
	 * 
	 * @param list
	 *            the list to take the value from
	 * @param place
	 *            the index of the entry
	 * @return the required value
	 */
	public static <T> int getValue(List<Entry<T, Integer>> list, int place) {
		Validate.notEmpty(list, "list +-");

		int total = 0;
		for (int i = 0; i < place; i++)
			total += list.get(i).getValue();
		return total;
	}

	/**
	 * Getting random value from a list.
	 * 
	 * @param array
	 *            the list.
	 * @return the random value.
	 */
	public static <T> T getRandom(T[] array) {
		Validate.notEmpty(array, "array +-");
		return array[randomInt(array.length - 1)];
	}

	/**
	 * Getting random value from a list.
	 * 
	 * @param array
	 *            the list.
	 * @return the random value.
	 */
	public static <T> T getRandom(List<T> list) {
		Validate.notEmpty(list, "array +-");
		return list.get(randomInt(list.size() - 1));
	}

	/**
	 * Getting random integer.
	 * 
	 * @param max
	 *            the maximum value.
	 * @return the random integer.
	 */
	public static int randomInt(int max) {
		if (max <= 0)
			return max;
		return random.nextInt(max);
	}

	/**
	 * Getting random integer.
	 * 
	 * @param max
	 *            the maximum value.
	 * @param min
	 *            the minimum value.
	 * @return the random integer.
	 */
	public static int randomInt(int min, int max) {
		int number = random.nextInt(max - min + 1) + min;
		return number;
	}

	/**
	 * Getting random double.
	 * 
	 * @param max
	 *            the maximum value.
	 * @param min
	 *            the minimum value.
	 * @return the random double.
	 */
	public static double randomDouble(double min, double max) {
		return randomInt((int) min * 100, (int) max * 100) / 100;
	}

	/**
	 * Generating a random string.
	 * 
	 * @param length
	 *            the size of the string.
	 * @return the random string value.
	 */
	public static String randomString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length() - 1)));

		return sb.toString();
	}
}