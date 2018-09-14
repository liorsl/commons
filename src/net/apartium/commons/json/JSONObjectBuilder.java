package net.apartium.commons.json;

import org.json.simple.JSONObject;

public class JSONObjectBuilder {

	
	public static JSONObjectBuilder getNewBuilder(JSONObject object) {
		return new JSONObjectBuilder(object == null ? new JSONObject() : object);
	}
	
	public static JSONObjectBuilder getNewBuilder() {
		return getNewBuilder(null);
	}
	
	final JSONObject
			object;
	
	private JSONObjectBuilder(JSONObject object) {
		this.object = object;
		
	}
	
	public final JSONObject getObject() {
		return this.object;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObjectBuilder put(Object key, Object value) {
		this.getObject().put(key, value);
		return this;
	}
}
