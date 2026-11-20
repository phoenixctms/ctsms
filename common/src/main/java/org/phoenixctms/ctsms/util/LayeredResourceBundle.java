package org.phoenixctms.ctsms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * The layers of one bundle locale (classpath copy, CTSMS_PROPERTIES overloads, overlay provider)
 * merged into a single bundle. The top layer wins. The bundle keeps no parent of its own, so
 * ResourceBundle attaches the locale fallback (e.g. "de" -> root) to it as for any other bundle.
 */
public class LayeredResourceBundle extends ResourceBundle {

	private final List<PropertyResourceBundle> layers; // top layer first

	/**
	 * @param layersBottomUp
	 *            the layers, lowest first
	 */
	public LayeredResourceBundle(List<PropertyResourceBundle> layersBottomUp) {
		layers = new ArrayList<PropertyResourceBundle>(layersBottomUp);
		Collections.reverse(layers);
	}

	@Override
	protected Object handleGetObject(String key) {
		if (key == null) {
			throw new NullPointerException();
		}
		for (PropertyResourceBundle layer : layers) {
			Object value = layer.handleGetObject(key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	@Override
	protected Set<String> handleKeySet() {
		HashSet<String> keys = new HashSet<String>();
		for (PropertyResourceBundle layer : layers) {
			keys.addAll(layer.keySet());
		}
		return keys;
	}

	@Override
	public Enumeration<String> getKeys() {
		HashSet<String> keys = new HashSet<String>(handleKeySet());
		if (parent != null) {
			Enumeration<String> parentKeys = parent.getKeys();
			while (parentKeys.hasMoreElements()) {
				keys.add(parentKeys.nextElement());
			}
		}
		return Collections.enumeration(keys);
	}
}
