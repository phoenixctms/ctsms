package org.phoenixctms.ctsms.web.jersey.resource;

import java.util.Objects;

public class NamedParameter {

	private String name;
	private Class<?> type;

	public NamedParameter(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedParameter other = (NamedParameter) obj;
		return Objects.equals(name, other.name);
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}
}
