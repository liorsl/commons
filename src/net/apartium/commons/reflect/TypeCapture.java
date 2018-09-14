package net.apartium.commons.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class TypeCapture<T> {
	
	public TypeCapture() {}
	
	public Type capture() {
	    return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * 
	 * @return The type of this TypeCapture instance
	 * @throws ClassNotFoundException thrown when you are invoking this method indirectly
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getType() throws ClassNotFoundException {
		return (Class<T>) Class.forName(capture().getTypeName());
	}
}
