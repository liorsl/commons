package net.apartium.commons.reflect;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class StackTraceUtils {

	private StackTraceUtils() {}
	
	/**
	 * Get class at stack trace
	 * 
	 * @param before
	 *            how much invokes ago
	 * @return the required class at the stack trace
	 */
	public static Class<?> getClassAtStacktrace(int before) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		try {
			return Class.forName(elements[before + 1].getClassName());
		} catch (ClassNotFoundException ex) {
			return null;
		}
	}

	/**
	 * Get the invoker of the current method
	 * 
	 * @return the invoker of the current method.
	 */
	public static Class<?> getClassAtStacktrace() {
		return getClassAtStacktrace(1);
	}

}
