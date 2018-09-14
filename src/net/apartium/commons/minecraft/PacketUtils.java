package net.apartium.commons.minecraft;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.reflect.ConstructorUtils;

import net.apartium.commons.Validate;
/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class PacketUtils {

	private PacketUtils() {}
	
	/**
	 * Check if the specified object is packet
	 * 
	 * @param packet
	 *            the packet
	 * @return true the specified object is packet, else if not
	 */
	public static boolean isPacket(Object packet) {
		Validate.notNull(packet, "packet +-");

		return NMSUtils.NMS_PACKET.isInstance(packet);
	}

	/**
	 * Create a packet instance with the specified paramaters
	 * 
	 * @param name
	 *            the name of the packet class (Ex. PacketPlayOutPosition)
	 * @param params
	 *            the paramaters of the constructor
	 * @return new packet instance
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
	public static Object create(String name, Object... params) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException, ClassNotFoundException {
		Validate.notNull(name, "name +-");

		return ConstructorUtils.invokeConstructor(NMSUtils.getPacketClass(name), params);
	}

}