package org.phoenixctms.ctsms.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.phoenixctms.ctsms.security.PasswordPolicy.CharacterClasses;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;

public class CharacterSet {

	private ArrayList<CharacterClasses> characterClasses;
	private HashMap<CharacterClasses, Integer> minOccurrences;
	private HashMap<CharacterClasses, Integer> maxOccurrences;

	public CharacterSet() {
		characterClasses = new ArrayList<CharacterClasses>();
		minOccurrences = new HashMap<CharacterClasses, Integer>();
		maxOccurrences = new HashMap<CharacterClasses, Integer>();
	}

	public void add(CharacterClasses characterClass, int minOccurrence, int maxOccurrence) {
		if (minOccurrence >= 0 && maxOccurrence >= minOccurrence && !characterClasses.contains(characterClass)) {
			characterClasses.add(characterClass);
			minOccurrences.put(characterClass, minOccurrence);
			maxOccurrences.put(characterClass, maxOccurrence);
		} else {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_INVALID_CHARSET_OCCURRENCE_DEF, DefaultMessages.PASSWORD_INVALID_CHARSET_OCCURRENCE_DEF,
					characterClass, minOccurrence, maxOccurrence));
		}
	}

	public ArrayList<CharacterClasses> getCharacterClasses() {
		return characterClasses;
	}

	public Integer getMaxOccurrence(CharacterClasses characterClass) {
		return maxOccurrences.get(characterClass);
	}

	public int getMaxTotalLength(int maxLength) {
		int result = 0;
		Iterator<Integer> it = maxOccurrences.values().iterator();
		while (it.hasNext()) {
			Integer maxOccurrence = it.next();
			if (maxOccurrence != null && maxOccurrence < maxLength) {
				result += maxOccurrence;
			} else {
				return maxLength;
			}
		}
		if (result > maxLength) {
			result = maxLength;
		}
		return result;
	}

	public Integer getMinOccurrence(CharacterClasses characterClass) {
		return minOccurrences.get(characterClass);
	}

	public int getMinTotalLength() {
		int result = 0;
		Iterator<Integer> it = minOccurrences.values().iterator();
		while (it.hasNext()) {
			Integer minOccurrence = it.next();
			if (minOccurrence != null && minOccurrence > 0) {
				result += minOccurrence;
			}
		}
		return result;
	}
}