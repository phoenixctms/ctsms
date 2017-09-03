package org.phoenixctms.ctsms.query.parser;

public class SelectValueType extends ValueType {

	public final static ValueType VALUE_TYPE = new SelectValueType();
	private final static String NAME = "SELECT";

	@Override
	public String getName() {
		return NAME;
	}
}
