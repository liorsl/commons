package net.apartium.commons.conditionals;

import java.util.Map;
import java.util.function.Function;

/**
 * A safe getter setter util built specificly for maps
 * @author voigo
 *
 */
public class MapConditions {
	
	public static void main(String[] args) {
		
		try {
			args[0].length();
		} catch(Exception e) {
			System.out.println("Some code here");

		}
		
		System.out.println("More code here");
	}
	
	private MapConditions() {}
	
	public static <K,V> V match(Map<K,V> map, K key, Function<V, Boolean> function, RuntimeException exception) {
		if (!map.containsKey(key)) return null;
		V value = map.get(key);
		boolean result = function.apply(value);
		if (!result) throw exception;
		return value;
	}
	
	public static <K,V> V match(Map<K,V> map, K key, Function<V, Boolean> function, String message) {
		return match(map, key, function, new IllegalArgumentException(message));
	}
	
	public static <K,V> V match(Map<K,V> map, K key, Class<?> valueType, RuntimeException exception) {
		return match(map, key, new Function<V, Boolean>() {

			@Override
			public Boolean apply(V t) {
				
				return t.getClass().equals(valueType);
			}
		}, exception);
	}
	
	public static <K,V> V match(Map<K,V> map, K key, Class<?> valueType, String message) {
		return match(map, key, valueType, new IllegalArgumentException(message));
	}
	
	public static class ClassTypeCondition {
		
	}
	
}
