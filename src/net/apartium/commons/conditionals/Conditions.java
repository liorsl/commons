package net.apartium.commons.conditionals;

import java.util.function.Function;

import net.apartium.commons.StringUtils;
import net.apartium.commons.Validate;

/**
 * 
 * @author voigo
 *
 */
public class Conditions {

	private Conditions() {}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <V> Function<V, Boolean> clt(Class<?> clazz) {
		Validate.notNull(clazz, "clazz +-");
		
		return new ClassTypeCondition<>(clazz);
	}
	
	public static <V> Function<V, Boolean> eq(V value) {
		Validate.notNull(value, "value +-");
		
		return new EqualsCondition<>(value);
	}
	
	public static <V> Function<V, Boolean> noeq(V value) {
		return new EqualsCondition<>(value, false);
	}
	
	public static <V> Function<V, Boolean> isInt() {
		return new IsIntegerCondition<>(true);
	}
	
	public static <V> Function<V, Boolean> isntInt() {
		return new IsIntegerCondition<>(false);
	}
	
	public static <V> Function<V, Boolean> isdbl() {
		return new IsDoubleCondition<>(true);
	}
	
	public static <V> Function<V, Boolean> isntdbl() {
		return new IsDoubleCondition<>(false);
	}
	
	@SafeVarargs
	public static <V> Function<V, Boolean> and(Function<V, Boolean>... functions) {
		return new AndOperatorCondition<>(functions);
	}
	
	static interface Condition<V> extends Function<V,Boolean> {}
	
	static class ClassTypeCondition<V> implements Condition<V>{

		final Class<?>
				clazz;
			
		public ClassTypeCondition(Class<?> clazz) { this.clazz = clazz; }
		
		@Override public Boolean apply(V object) { return object.getClass().equals(clazz); }
		
	}
	
	static class EqualsCondition<V> implements Condition<V> {

		final V
				equalsTo;
		
		final boolean
				equals;
		
		public EqualsCondition(V value) { this(value, true); }
		
		public EqualsCondition(V value, boolean eq) {
			this.equalsTo = value;
			this.equals = eq;
		}

		@Override public Boolean apply(V object) { return object.equals(equalsTo) == equals; }
		
	}
	
	static class IsIntegerCondition<V> implements Condition<V> {
		
		boolean 
				value;
		
		public IsIntegerCondition(boolean value) { this.value = value; }
		
		@Override public Boolean apply(V t) { return StringUtils.isInteger(t) == this.value; }
		
	}

	static class IsDoubleCondition<V> implements Condition<V> {
		
		boolean 
				value;
		
		public IsDoubleCondition(boolean value) { this.value = value; }
		
		@Override public Boolean apply(V t) { return StringUtils.isDouble(t) == this.value; }
		
	}
	
	static class AndOperatorCondition<V> implements Condition<V> {
		
		final Function<V, Boolean>[] 
				functions;
		
		public AndOperatorCondition(Function<V, Boolean>[] functions) { this.functions = functions; }
		
		@Override
		public Boolean apply(V t) {
			for (Function<V, Boolean> function : this.functions)
				if (!function.apply(t))
					return false;
			
			return true;
		}
		
	}

}
