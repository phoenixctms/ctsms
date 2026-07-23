package org.phoenixctms.ctsms.util;

public final class SelectMessageFormatResolved {

	private final String format;
	private final String argument;

	public SelectMessageFormatResolved(String format, String argument) {
		this.format = format;
		this.argument = argument;
	}

	public String getFormat() {
		return format;
	}

	public String getArgument() {
		return argument;
	}
}
