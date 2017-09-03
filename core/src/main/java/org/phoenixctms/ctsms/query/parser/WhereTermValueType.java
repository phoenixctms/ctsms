package org.phoenixctms.ctsms.query.parser;

public class WhereTermValueType extends ValueType {

	public final static ValueType VALUE_TYPE = new WhereTermValueType();
	private final static String NAME = "WHERE_TERM";

	@Override
	public String getName() {
		return NAME;
	}
}
