package etna.webservice.jersey.model;

import java.util.LinkedHashMap;

public class EntityBuilder {
	LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	
	
	public EntityBuilder set(boolean success, String message, String token, Object object) {
		map.put("success", success);
		map.put("message", message);
		if (token != null)
			map.put("token", token);
		if (object != null)
			map.put(object.getClass().getSimpleName(), object);
		return this;
	}
	
	public LinkedHashMap<String, Object> getMap() {
		return this.map;
	}
}
