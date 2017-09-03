package org.phoenixctms.ctsms.web.jersey.resource;

public class NamedParameter {

	private String name;
	private Class type;

	public NamedParameter(String name, Class type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final NamedParameter other = (NamedParameter) obj;
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}


	public String getName() {
		return name;
	}

	public Class getType() {
		return type;
	}
}
