package org.phoenixctms.ctsms.compare;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class RegexpEntryLengthComparator implements Comparator<Entry<String, Pattern>> {

	private int lengthDesc;

	public RegexpEntryLengthComparator(boolean lengthDesc) {
		this.lengthDesc = (lengthDesc ? -1 : 1);
	}

	@Override
	public int compare(Entry<String, Pattern> a, Entry<String, Pattern> b) {
		if (a != null && b != null) {
			Pattern regexpA = a.getValue();
			Pattern regexpB = b.getValue();
			if (regexpA != null && regexpB != null) {
				int patternLengthA = regexpA.pattern().length();
				int patternLengthB = regexpB.pattern().length();
				if (patternLengthA > patternLengthB) {
					return 1 * lengthDesc;
				} else if (patternLengthA < patternLengthB) {
					return -1 * lengthDesc;
				} else {
					return 0;
				}
			} else if (regexpA == null && regexpB != null) {
				return -1;
			} else if (regexpA != null && regexpB == null) {
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