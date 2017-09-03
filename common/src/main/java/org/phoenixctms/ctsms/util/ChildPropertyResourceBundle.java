package org.phoenixctms.ctsms.util;

import java.io.IOException;
import java.io.Reader;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ChildPropertyResourceBundle extends PropertyResourceBundle {

	public ChildPropertyResourceBundle(Reader reader, ResourceBundle parent) throws IOException {
		super(reader);
		if (parent != null) {
			this.setParent(parent);
		}
	}

}