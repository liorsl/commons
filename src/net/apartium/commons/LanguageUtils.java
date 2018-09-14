package net.apartium.commons;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class LanguageUtils {
	
	private LanguageUtils() {}
	
	public static Map<String, Map<Integer, String>>
		KEYBOARD_KEYS = new HashMap<>();
	
	public static String getLetter(String language, int key) {
		Validate.notEmpty(language, "langauge +-");
		
		if (KEYBOARD_KEYS.get(language) == null)
			return null;
		return KEYBOARD_KEYS.get(language).get(key);
	}
	
	public static Entry<String, Integer> getKey(String letter) {
		Validate.notEmpty(letter, "letter +-");

		for (Entry<String, Map<Integer, String>> e : KEYBOARD_KEYS.entrySet())
			for (Entry<Integer, String> e2 : e.getValue().entrySet())
				if (e2.getValue().equals(letter))
					return new SimpleEntry<>(e.getKey(), e2.getKey());
		return null;
	}
	
	public static void addKey(String language, int key, String letter){
		Validate.notEmpty(letter, "letter +-");
		Validate.notEmpty(language, "language +-");

		if (KEYBOARD_KEYS.get(language) == null)
			KEYBOARD_KEYS.put(language, new HashMap<Integer, String>());
		KEYBOARD_KEYS.get(language).put(key, letter);
	}
	
	static {
		addKey("English", 1, "Esc");
		addKey("English", 2, "1");
		addKey("English", 3, "2");
		addKey("English", 4, "3");
		addKey("English", 5, "4");
		addKey("English", 6, "5");
		addKey("English", 7, "6");
		addKey("English", 8, "7");
		addKey("English", 9, "8");
		addKey("English", 10, "9");
		addKey("English", 11, "0");
		addKey("English", 12, "-");
		addKey("English", 13, "=");
		addKey("English", 14, "BkSpc");
		addKey("English", 15, "Tab");
		addKey("English", 16, "Q");
		addKey("English", 17, "W");
		addKey("English", 18, "E");
		addKey("English", 19, "R");
		addKey("English", 20, "T");
		addKey("English", 21, "Y");
		addKey("English", 22, "U");
		addKey("English", 23, "I");
		addKey("English", 24, "O");
		addKey("English", 25, "P");
		addKey("English", 26, "[");
		addKey("English", 27, "]");
		addKey("English", 28, "Enter");
		addKey("English", 29, "Left Ctrl");
		addKey("English", 30, "A");
		addKey("English", 31, "S");
		addKey("English", 32, "D");
		addKey("English", 33, "F");
		addKey("English", 34, "G");
		addKey("English", 35, "H");
		addKey("English", 36, "J");
		addKey("English", 37, "K");
		addKey("English", 38, "L");
		addKey("English", 39, ";");
		addKey("English", 40, "'");
		addKey("English", 41, "`");
		addKey("English", 42, "Left Shift");
		addKey("English", 43, "\\");
		addKey("English", 44, "Z");
		addKey("English", 45, "X");
		addKey("English", 46, "C");
		addKey("English", 47, "V");
		addKey("English", 48, "B");
		addKey("English", 49, "N");
		addKey("English", 50, "M");
		addKey("English", 51, ",");
		addKey("English", 52, ".");
		addKey("English", 53, "/");
		
		addKey("Hebrew", 1, "Esc");
		addKey("Hebrew", 2, "1");
		addKey("Hebrew", 3, "2");
		addKey("Hebrew", 4, "3");
		addKey("Hebrew", 5, "4");
		addKey("Hebrew", 6, "5");
		addKey("Hebrew", 7, "6");
		addKey("Hebrew", 8, "7");
		addKey("Hebrew", 9, "8");
		addKey("Hebrew", 10, "9");
		addKey("Hebrew", 11, "0");
		addKey("Hebrew", 12, "-");
		addKey("Hebrew", 13, "=");
		addKey("Hebrew", 14, "BkSpc");
		addKey("Hebrew", 15, "Tab");
		addKey("Hebrew", 16, "/");
		addKey("Hebrew", 17, "'");
		addKey("Hebrew", 18, "ק");
		addKey("Hebrew", 19, "ר");
		addKey("Hebrew", 20, "א");
		addKey("Hebrew", 21, "ט");
		addKey("Hebrew", 22, "ו");
		addKey("Hebrew", 23, "ן");
		addKey("Hebrew", 24, "ם");
		addKey("Hebrew", 25, "פ");
		addKey("Hebrew", 26, "[");
		addKey("Hebrew", 27, "]");
		addKey("Hebrew", 28, "Enter");
		addKey("Hebrew", 29, "Left Ctrl");
		addKey("Hebrew", 30, "ש");
		addKey("Hebrew", 31, "ד");
		addKey("Hebrew", 32, "ג");
		addKey("Hebrew", 33, "כ");
		addKey("Hebrew", 34, "ע");
		addKey("Hebrew", 35, "י");
		addKey("Hebrew", 36, "ח");
		addKey("Hebrew", 37, "ל");
		addKey("Hebrew", 38, "ך");
		addKey("Hebrew", 39, "ף");
		addKey("Hebrew", 40, ";");
		addKey("Hebrew", 41, "`");
		addKey("Hebrew", 42, "Left Shift");
		addKey("Hebrew", 43, "\\");
		addKey("Hebrew", 44, "ז");
		addKey("Hebrew", 45, "ס");
		addKey("Hebrew", 46, "ב");
		addKey("Hebrew", 47, "ה");
		addKey("Hebrew", 48, "נ");
		addKey("Hebrew", 49, "מ");
		addKey("Hebrew", 50, "צ");
		addKey("Hebrew", 51, "ת");
		addKey("Hebrew", 52, "ץ");
		addKey("Hebrew", 53, ".");
	}
}