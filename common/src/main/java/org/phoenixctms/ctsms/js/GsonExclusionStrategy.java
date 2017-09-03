package org.phoenixctms.ctsms.js;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonExclusionStrategy implements ExclusionStrategy {

	private final Class<?> typeToSkip;
	private final String fieldName;

	public GsonExclusionStrategy(Class<?> typeToSkip) {
		this.typeToSkip = typeToSkip;
		this.fieldName = null;
	}

	public GsonExclusionStrategy(Class<?> typeToSkip, String fieldName) {
		this.typeToSkip = typeToSkip;
		this.fieldName = fieldName;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		if (fieldName != null) {
			return false;
		}
		return (clazz.equals(typeToSkip));
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		if (fieldName != null) {
			return (f.getDeclaringClass().equals(typeToSkip)
					&& f.getName().equals(fieldName));
		}
		return false;
	}
}
