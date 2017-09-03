package org.phoenixctms.ctsms.web.jersey.index;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class IndexBase {

	private JsonElement index;
	public final static JsonElement EMPTY_INDEX_NODE = new JsonObject();

	// public final static JsonArray RESOURCE_METHODS = new JsonArray();
	// static {
	// RESOURCE_METHODS.add(new JsonPrimitive(HttpMethod.GET));
	// }
	public IndexBase(JsonElement indexNode) {
		index = indexNode;
	}

	public JsonElement getJson() {
		return index;
	}
}
