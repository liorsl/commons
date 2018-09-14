package net.apartium.commons;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class IDFactory {

	private static volatile Map<String,IDFactory>
			ids = new ConcurrentHashMap<>();
	
	@Getter
	private static IDFactory defaultFactory = new IDFactory("____________-________-________-________");
	
	@Getter
	protected String
			base;
	
	public IDFactory(String base) {
		this.base = base;
		
		ids.put(base, this);
	}
		
}
