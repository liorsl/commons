package net.apartium.commons;

import java.io.Serializable;

/**
* <a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.
* @author Apartium
* @version 1
* @since wip-unknownversion
*/
public class ID implements Serializable, CharSequence {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String
		id;
	
	public ID() {
		this(IDFactory.getDefaultFactory().getBase());
	}
		
	public ID(String id) {
		StringBuilder sb = new StringBuilder();
		for (char c : id.toCharArray()) 
			if (c == '_') 
				sb.append(StringUtils.ALPHABET.charAt(RandomUtils.randomInt(StringUtils.ALPHABET.length() - 1)));
			else
				sb.append(c);
		
		this.id = sb.toString();
		
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof String) && !(object instanceof ID))
			return false;
		
		return this.id.equals(object.toString());
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public static boolean isID(String arg) {
		return arg.matches("[0-9a-z]{4}-[0-9a-z]{8}-[0-9a-z]{8}-[0-9a-z]{8}-[0-9a-z]{8}");
	}

	public static ID parse(String arg) {
		//if (!isID(arg))
		//	return null;
		return new ID(arg);
	}

	@Override
	public char charAt(int index) {
		return id.charAt(index);
	}

	@Override
	public int length() {
		return id.length();
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++) 
			sb.append(charAt(i));
		
		return sb.toString();
	}
	
}