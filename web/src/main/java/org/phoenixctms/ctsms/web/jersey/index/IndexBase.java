package org.phoenixctms.ctsms.web.jersey.index;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class IndexBase {

	private JsonElement index;
	public final static JsonElement EMPTY_INDEX_NODE = new JsonObject();

	public IndexBase(JsonElement indexNode) {
		index = indexNode;
	}

	public JsonElement getJson() {
		return index;
	}
}
