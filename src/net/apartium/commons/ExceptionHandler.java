package net.apartium.commons;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class ExceptionHandler {
	
	private static ExceptionHandler instance = new ExceptionHandler();
	
	public static ExceptionHandler getInstance() {
		return instance;
	}
	
	public static void setInstance(ExceptionHandler handler) {
		instance = handler;
		
	}
	
	public ExceptionHandler() {}
	
	public void handle(Exception e) {
		e.printStackTrace();
		
	}
	
}
