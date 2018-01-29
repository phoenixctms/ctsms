package org.phoenixctms.ctsms.compare;

import java.util.Comparator;
import java.util.Map.Entry;

public class KeyValueLengthComparator implements Comparator<Entry<String, String>> {

	private int lengthDesc;

	public KeyValueLengthComparator(boolean lengthDesc) {
		this.lengthDesc = (lengthDesc ? -1 : 1);
	}

	@Override
	public int compare(Entry<String, String> a, Entry<String, String> b) {
		if (a != null && b != null) {
			String valueA = a.getValue();
			String valueB = b.getValue();
			if (valueA != null && valueB != null) {
				int lengthA = valueA.length();
				int lengthB = valueB.length();
				if (lengthA > lengthB) {
					return 1 * lengthDesc;
				} else if (lengthA < lengthB) {
					return -1 * lengthDesc;
				} else {
					return 0;
				}
			} else if (valueA == null && valueB != null) {
				return -1;
			} else if (valueA != null && valueB == null) {
				return 1;
			} else {
				return 0;
			}
		} else if (a == null && b != null) {
			return -1;
		} else if (a != null && b == null) {
			return 1;
		} else {
			return 0;
		}
	}
}