package org.phoenixctms.ctsms.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.phoenixctms.ctsms.domain.Password;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.vo.CharacterClassVO;
import org.phoenixctms.ctsms.vo.PasswordPolicyVO;

public class PasswordPolicy {

	public enum CharacterClasses {
		SMALL_LETTERS,
		CAPITAL_LETTERS,
		DIGITS,
		UMLAUTS,
		WHITESPACES,
		ALT_SYMBOLS,
		SYMBOLS
	}

	private final static String STRING_RANDOM_ALGORITHM = CoreUtil.RANDOM_ALGORITHM; // "SHA1PRNG";
	private final static HashMap<CharacterClasses, Character[]> CHARACTER_CLASSES = new HashMap<CharacterClasses, Character[]>();
	private final static HashMap<CharacterClasses, String> CHARACTER_CLASS_L10NKEYS = new HashMap<CharacterClasses, String>();
	private final static HashMap<CharacterClasses, String> CHARACTER_CLASS_NAME_DEFAULTS = new HashMap<CharacterClasses, String>();

	public final static PasswordPolicy USER;

	public final static PasswordPolicy DEPARTMENT;
	static {
		CHARACTER_CLASSES.put(CharacterClasses.SMALL_LETTERS, new Character[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
				'u', 'v', 'w', 'x', 'y', 'z' });
		CHARACTER_CLASSES.put(CharacterClasses.CAPITAL_LETTERS, new Character[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z' });
		CHARACTER_CLASSES.put(CharacterClasses.DIGITS, new Character[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' });
		CHARACTER_CLASSES.put(CharacterClasses.UMLAUTS, new Character[] { '\u00E4', '\u00F6', '\u00FC', '\u00C4', '\u00D6', '\u00DC', '\u00DF' });
		CHARACTER_CLASSES.put(CharacterClasses.WHITESPACES, new Character[] { ' ' });
		CHARACTER_CLASSES.put(CharacterClasses.ALT_SYMBOLS, new Character[] { '@', '\u20AC', '\u0181', '~' }); // \u00B5
		CHARACTER_CLASSES.put(CharacterClasses.SYMBOLS, new Character[] { '^', '\u00B0', '!', '"', '\u00A7', '$', '%', '&', '/', '{', '(', '[', ']', ')', '}', '=', '?', '\\',
				'\u00B4', '`', '+', '*', '-', '#', '\'', '-', '_', '.', ',', ':', ';', '|', '<', '>' });
		CHARACTER_CLASS_L10NKEYS.put(CharacterClasses.SMALL_LETTERS, MessageCodes.PASSWORD_SMALL_LETTERS);
		CHARACTER_CLASS_L10NKEYS.put(CharacterClasses.CAPITAL_LETTERS, MessageCodes.PASSWORD_CAPITAL_LETTERS);
		CHARACTER_CLASS_L10NKEYS.put(CharacterClasses.DIGITS, MessageCodes.PASSWORD_DIGITS);
		CHARACTER_CLASS_L10NKEYS.put(CharacterClasses.UMLAUTS, MessageCodes.PASSWORD_UMLAUTS);
		CHARACTER_CLASS_L10NKEYS.put(CharacterClasses.WHITESPACES, MessageCodes.PASSWORD_WHITESPACES);
		CHARACTER_CLASS_L10NKEYS.put(CharacterClasses.ALT_SYMBOLS, MessageCodes.PASSWORD_ALT_SYMBOLS);
		CHARACTER_CLASS_L10NKEYS.put(CharacterClasses.SYMBOLS, MessageCodes.PASSWORD_SYMBOLS);
		CHARACTER_CLASS_NAME_DEFAULTS.put(CharacterClasses.SMALL_LETTERS, DefaultMessages.PASSWORD_SMALL_LETTERS);
		CHARACTER_CLASS_NAME_DEFAULTS.put(CharacterClasses.CAPITAL_LETTERS, DefaultMessages.PASSWORD_CAPITAL_LETTERS);
		CHARACTER_CLASS_NAME_DEFAULTS.put(CharacterClasses.DIGITS, DefaultMessages.PASSWORD_DIGITS);
		CHARACTER_CLASS_NAME_DEFAULTS.put(CharacterClasses.UMLAUTS, DefaultMessages.PASSWORD_UMLAUTS);
		CHARACTER_CLASS_NAME_DEFAULTS.put(CharacterClasses.WHITESPACES, DefaultMessages.PASSWORD_WHITESPACES);
		CHARACTER_CLASS_NAME_DEFAULTS.put(CharacterClasses.ALT_SYMBOLS, DefaultMessages.PASSWORD_ALT_SYMBOLS);
		CHARACTER_CLASS_NAME_DEFAULTS.put(CharacterClasses.SYMBOLS, DefaultMessages.PASSWORD_SYMBOLS);
		USER = PasswordPolicy.createPasswordPolicy();
		DEPARTMENT = PasswordPolicy.createDepartmentPasswordPolicy();
	}
	private static String charactersToString(Character[] characters) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < characters.length; i++) {
			result.append(characters[i]);
		}
		return result.toString();
	}
	private static int countCharacters(String input, Character[] characters) {
		int count = 0;
		if (input != null) {
			for (int i = 0; i < characters.length; i++) {
				for (int j = 0; j < input.length(); j++) {
					if (characters[i] == input.charAt(j)) {
						count++;
					}
				}
			}
		}
		return count;
	}
	private static PasswordPolicy createDepartmentPasswordPolicy() {
		CharacterSet characterSet = new CharacterSet();
		int maxLength = Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MAX_LENGTH, Settings.Bundle.SETTINGS, DefaultSettings.MAX_LENGTH);
		if (Settings.getBoolean(SettingCodes.DEPARTMENT_PASSWORD_ALLOW_SMALL_LETTERS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_SMALL_LETTERS)) {
			characterSet.add(CharacterClasses.SMALL_LETTERS,
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MIN_SMALL_LETTERS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MAX_SMALL_LETTERS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.DEPARTMENT_PASSWORD_ALLOW_CAPITAL_LETTERS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_CAPITAL_LETTERS)) {
			characterSet.add(CharacterClasses.CAPITAL_LETTERS,
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MIN_CAPITAL_LETTERS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MAX_CAPITAL_LETTERS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.DEPARTMENT_PASSWORD_ALLOW_DIGITS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_DIGITS)) {
			characterSet.add(CharacterClasses.DIGITS,
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MIN_DIGITS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MAX_DIGITS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.DEPARTMENT_PASSWORD_ALLOW_UMLAUTS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_UMLAUTS)) {
			characterSet.add(CharacterClasses.UMLAUTS,
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MIN_UMLAUTS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MAX_UMLAUTS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.DEPARTMENT_PASSWORD_ALLOW_WHITESPACES, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_WHITESPACES)) {
			characterSet.add(CharacterClasses.WHITESPACES,
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MIN_WHITESPACES, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MAX_WHITESPACES, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.DEPARTMENT_PASSWORD_ALLOW_ALT_SYMBOLS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_ALT_SYMBOLS)) {
			characterSet.add(CharacterClasses.ALT_SYMBOLS,
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MIN_ALT_SYMBOLS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MAX_ALT_SYMBOLS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.DEPARTMENT_PASSWORD_ALLOW_SYMBOLS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_SYMBOLS)) {
			characterSet.add(CharacterClasses.SYMBOLS,
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MIN_SYMBOLS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MAX_SYMBOLS, Settings.Bundle.SETTINGS, maxLength));
		}
		return new PasswordPolicy(Settings.getInt(SettingCodes.DEPARTMENT_PASSWORD_MIN_LENGTH, Settings.Bundle.SETTINGS, DefaultSettings.MIN_LENGTH), maxLength, characterSet);
	}
	private static PasswordPolicy createPasswordPolicy() {
		CharacterSet characterSet = new CharacterSet();
		int maxLength = Settings.getInt(SettingCodes.PASSWORD_MAX_LENGTH, Settings.Bundle.SETTINGS, DefaultSettings.MAX_LENGTH);
		if (Settings.getBoolean(SettingCodes.PASSWORD_ALLOW_SMALL_LETTERS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_SMALL_LETTERS)) {
			characterSet.add(CharacterClasses.SMALL_LETTERS,
					Settings.getInt(SettingCodes.PASSWORD_MIN_SMALL_LETTERS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.PASSWORD_MAX_SMALL_LETTERS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.PASSWORD_ALLOW_CAPITAL_LETTERS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_CAPITAL_LETTERS)) {
			characterSet.add(CharacterClasses.CAPITAL_LETTERS,
					Settings.getInt(SettingCodes.PASSWORD_MIN_CAPITAL_LETTERS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.PASSWORD_MAX_CAPITAL_LETTERS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.PASSWORD_ALLOW_DIGITS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_DIGITS)) {
			characterSet.add(CharacterClasses.DIGITS,
					Settings.getInt(SettingCodes.PASSWORD_MIN_DIGITS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.PASSWORD_MAX_DIGITS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.PASSWORD_ALLOW_UMLAUTS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_UMLAUTS)) {
			characterSet.add(CharacterClasses.UMLAUTS,
					Settings.getInt(SettingCodes.PASSWORD_MIN_UMLAUTS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.PASSWORD_MAX_UMLAUTS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.PASSWORD_ALLOW_WHITESPACES, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_WHITESPACES)) {
			characterSet.add(CharacterClasses.WHITESPACES,
					Settings.getInt(SettingCodes.PASSWORD_MIN_WHITESPACES, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.PASSWORD_MAX_WHITESPACES, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.PASSWORD_ALLOW_ALT_SYMBOLS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_ALT_SYMBOLS)) {
			characterSet.add(CharacterClasses.ALT_SYMBOLS,
					Settings.getInt(SettingCodes.PASSWORD_MIN_ALT_SYMBOLS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.PASSWORD_MAX_ALT_SYMBOLS, Settings.Bundle.SETTINGS, maxLength));
		}
		if (Settings.getBoolean(SettingCodes.PASSWORD_ALLOW_SYMBOLS, Settings.Bundle.SETTINGS, DefaultSettings.ALLOW_SYMBOLS)) {
			characterSet.add(CharacterClasses.SYMBOLS,
					Settings.getInt(SettingCodes.PASSWORD_MIN_SYMBOLS, Settings.Bundle.SETTINGS, 0),
					Settings.getInt(SettingCodes.PASSWORD_MAX_SYMBOLS, Settings.Bundle.SETTINGS, maxLength));
		}
		PasswordPolicy policy = new PasswordPolicy(Settings.getInt(SettingCodes.PASSWORD_MIN_LENGTH, Settings.Bundle.SETTINGS, DefaultSettings.MIN_LENGTH), maxLength, characterSet);
		policy.minLevenshteinDistance = Settings.getInt(SettingCodes.PASSWORD_MIN_LEVENSHTEIN_DISTANCE, Settings.Bundle.SETTINGS, DefaultSettings.MIN_LEVENSHTEIN_DISTANCE);
		policy.distancePasswordHistory = Settings.getInt(SettingCodes.PASSWORD_DISTANCE_PASSWORD_HISTORY, Settings.Bundle.SETTINGS, DefaultSettings.DISTANCE_PASSWORD_HISTORY);
		policy.adminIgnorePolicy = Settings.getBoolean(SettingCodes.PASSWORD_ADMIN_IGNORE_POLICY, Settings.Bundle.SETTINGS, DefaultSettings.ADMIN_IGNORE_POLICY);
		return policy;
	}
	private static String getCharacterClassName(CharacterClasses characterClass) {
		return L10nUtil.getMessage(CHARACTER_CLASS_L10NKEYS.get(characterClass), CHARACTER_CLASS_NAME_DEFAULTS.get(characterClass),
				charactersToString(CHARACTER_CLASSES.get(characterClass)));
	}
	// http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
	private static int getLevenshteinDistance(String a, String b) {
		if (a == null && b == null) {
			return 0;
		} else if (a == null && b != null) {
			return b.length();
		} else if (a != null && b == null) {
			return a.length();
		} else {
			int[][] distance = new int[a.length() + 1][b.length() + 1];
			for (int i = 0; i <= a.length(); i++) {
				distance[i][0] = i;
			}
			for (int j = 0; j <= b.length(); j++) {
				distance[0][j] = j;
			}
			for (int i = 1; i <= a.length(); i++) {
				for (int j = 1; j <= b.length(); j++) {
					distance[i][j] = minimum(
							distance[i - 1][j] + 1,
							distance[i][j - 1] + 1,
							distance[i - 1][j - 1]
									+ ((a.charAt(i - 1) == b.charAt(j - 1)) ? 0
											: 1));
				}
			}
			return distance[a.length()][b.length()];
		}
	}
	private static Character getRandomCharacter(Character[] characters, SecureRandom random) {
		return characters[random.nextInt(characters.length)]; // - 1)];
	}
	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}
	private int maxLength;

	private int minLength;

	private int maxPossibleLength;

	private int minRequiredLength;

	private CharacterSet characterSet;

	private int minLevenshteinDistance;

	private int distancePasswordHistory; // <= 0 all

	private boolean adminIgnorePolicy;

	public PasswordPolicy(int minLength, int maxLength, CharacterSet characterSet) {
		if (minLength >= 0 && maxLength >= minLength) {
			minRequiredLength = characterSet.getMinTotalLength();
			if (minRequiredLength > maxLength) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_CHAR_MIN_OCCURRENCES_SANITY_ERROR,
						DefaultMessages.PASSWORD_CHAR_MIN_OCCURRENCES_SANITY_ERROR, minRequiredLength, maxLength));
			}
			maxPossibleLength = characterSet.getMaxTotalLength(maxLength);
			if (maxPossibleLength < minLength) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_CHAR_MAX_OCCURRENCES_SANITY_ERROR,
						DefaultMessages.PASSWORD_CHAR_MAX_OCCURRENCES_SANITY_ERROR, maxPossibleLength, minLength));
			}
			this.minLength = minLength;
			this.maxLength = maxLength;
			this.characterSet = characterSet;
			minLevenshteinDistance = 0;
			distancePasswordHistory = 0;
			adminIgnorePolicy = false;
		} else {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_INVALID_LENGTH_DEF, DefaultMessages.PASSWORD_INVALID_LENGTH_DEF, minLength, maxLength));
		}
	}

	public void checkDistance(String passwordA, String passwordB) throws IllegalArgumentException {
		if (getLevenshteinDistance(passwordA, passwordB) < minLevenshteinDistance) {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_TOO_SIMILAR_TO_PREVIOUS_PASSWORDS,
					DefaultMessages.PASSWORD_TOO_SIMILAR_TO_PREVIOUS_PASSWORDS, minLevenshteinDistance));
		}
	}

	public void checkHistoryDistance(String plainNewPassword, Password lastPassword, String plainDepartmentPassword) throws Exception {
		Password previousPassword = lastPassword;
		int passwordHistoryLength = 0;
		while (previousPassword != null && passwordHistoryLength < distancePasswordHistory || distancePasswordHistory < 0) {
			checkDistance(plainNewPassword, CryptoUtil.decryptPassword(previousPassword, plainDepartmentPassword));
			previousPassword = previousPassword.getPreviousPassword();
			passwordHistoryLength++;
		}
	}

	public void checkStrength(String input) throws IllegalArgumentException {
		int length = input == null ? 0 : input.length();
		if (length < minLength) {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_TOO_SHORT, DefaultMessages.PASSWORD_TOO_SHORT, minLength));
		} else if (length > maxLength) {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_TOO_LONG, DefaultMessages.PASSWORD_TOO_LONG, maxLength));
		}
		int validCharCount = 0;
		StringBuilder characterClassNames = new StringBuilder();
		Iterator<CharacterClasses> it = characterSet.getCharacterClasses().iterator();
		while (it.hasNext()) {
			CharacterClasses characterClass = it.next();
			Integer minOccurrence = characterSet.getMinOccurrence(characterClass);
			Integer maxOccurrence = characterSet.getMaxOccurrence(characterClass);
			int occurrenceCount = countCharacters(input, CHARACTER_CLASSES.get(characterClass));
			String characterClassName = getCharacterClassName(characterClass);
			validCharCount += occurrenceCount;
			if (minOccurrence != null && occurrenceCount < minOccurrence) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_TOO_FEW_OCCURRENCES, DefaultMessages.PASSWORD_TOO_FEW_OCCURRENCES, minOccurrence,
						characterClassName));
			} else if (maxOccurrence != null && occurrenceCount > maxOccurrence) {
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_TOO_MANY_OCCURRENCES, DefaultMessages.PASSWORD_TOO_MANY_OCCURRENCES, maxOccurrence,
						characterClassName));
			}
			if (characterClassNames.length() > 0) {
				characterClassNames.append(", ");
			}
			characterClassNames.append(characterClassName);
		}
		if (validCharCount < length) {
			throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.PASSWORD_INVALID_CHAR_FOUND, DefaultMessages.PASSWORD_INVALID_CHAR_FOUND,
					characterClassNames.toString()));
		}
	}

	public String getDummyPassword() throws NoSuchAlgorithmException {
		return getRandomString(minLength);
	}

	public PasswordPolicyVO getPasswordPolicyVO() {
		PasswordPolicyVO policyVO = new PasswordPolicyVO();
		policyVO.setMinLength(this.minLength);
		policyVO.setMaxLength(this.maxLength);
		policyVO.setMinRequiredLength(this.minRequiredLength);
		policyVO.setMaxPossibleLength(this.maxPossibleLength);
		policyVO.setDistancePasswordHistory(this.distancePasswordHistory);
		policyVO.setAdminIgnorePolicy(this.adminIgnorePolicy);
		ArrayList<CharacterClassVO> characterClassVOs = new ArrayList<CharacterClassVO>(characterSet.getCharacterClasses().size());
		Iterator<CharacterClasses> it = characterSet.getCharacterClasses().iterator();
		while (it.hasNext()) {
			CharacterClasses characterClass = it.next();
			CharacterClassVO characterClassVO = new CharacterClassVO();
			characterClassVO.setMinOccurrence(characterSet.getMinOccurrence(characterClass));
			characterClassVO.setMaxOccurrence(characterSet.getMaxOccurrence(characterClass));
			characterClassVO.setName(getCharacterClassName(characterClass));
			characterClassVO.setCharacters(CHARACTER_CLASSES.get(characterClass));
			characterClassVOs.add(characterClassVO);
		}
		policyVO.setCharacterSets(characterClassVOs);
		policyVO.setRequirements(getRequirements());
		return policyVO;
	}

	private String getRandomString(int desiredLength) throws NoSuchAlgorithmException {
		int length = desiredLength;
		if (length < minRequiredLength) {
			length = minRequiredLength;
		}
		if (length > maxPossibleLength) {
			length = maxPossibleLength;
		}
		SecureRandom random = SecureRandom.getInstance(STRING_RANDOM_ALGORITHM);
		ArrayList<Character> resultChars = new ArrayList<Character>(length);
		StringBuilder result = new StringBuilder(length);
		HashMap<CharacterClasses, Integer> used = new HashMap<CharacterClasses, Integer>();
		Iterator<CharacterClasses> it = characterSet.getCharacterClasses().iterator();
		while (it.hasNext()) {
			used.put(it.next(), 0);
		}
		HashMap<CharacterClasses, Character> randomChars = new HashMap<CharacterClasses, Character>();
		ArrayList<CharacterClasses> candidates = new ArrayList<CharacterClasses>();
		CharacterClasses selected;
		for (int i = 0; i < length; i++) {
			randomChars.clear();
			candidates.clear();
			it = characterSet.getCharacterClasses().iterator();
			if (i < minRequiredLength) {
				while (it.hasNext()) {
					CharacterClasses characterClass = it.next();
					Integer minOccurrence = characterSet.getMinOccurrence(characterClass);
					Integer maxOccurrence = characterSet.getMaxOccurrence(characterClass);
					if ((maxOccurrence == null || (maxOccurrence != null && used.get(characterClass) < maxOccurrence)) &&
							(minOccurrence == null || (minOccurrence != null && used.get(characterClass) < minOccurrence))) {
						randomChars.put(characterClass, getRandomCharacter(CHARACTER_CLASSES.get(characterClass), random));
						candidates.add(characterClass);
					}
				}
			} else {
				it = characterSet.getCharacterClasses().iterator();
				while (it.hasNext()) {
					CharacterClasses characterClass = it.next();
					Integer maxOccurrence = characterSet.getMaxOccurrence(characterClass);
					if (maxOccurrence == null || (maxOccurrence != null && used.get(characterClass) < maxOccurrence)) {
						randomChars.put(characterClass, getRandomCharacter(CHARACTER_CLASSES.get(characterClass), random));
						candidates.add(characterClass);
					}
				}
			}
			selected = candidates.get(random.nextInt(candidates.size())); // - 1));
			used.put(selected, used.get(selected) + 1);
			resultChars.add(randomChars.get(selected));
		}
		Collections.shuffle(resultChars);
		for (int i = 0; i < length; i++) {
			result.append(resultChars.get(i));
		}
		return result.toString();
	}

	public String getRequirements() {
		StringBuilder requirements = new StringBuilder(
				L10nUtil.getMessage(MessageCodes.PASSWORD_MIN_LENGTH_REQUIREMENT, DefaultMessages.PASSWORD_MIN_LENGTH_REQUIREMENT, minLength));
		requirements.append("\n");
		requirements.append(L10nUtil.getMessage(MessageCodes.PASSWORD_MAX_LENGTH_REQUIREMENT, DefaultMessages.PASSWORD_MAX_LENGTH_REQUIREMENT, maxLength));
		Iterator<CharacterClasses> it = characterSet.getCharacterClasses().iterator();
		while (it.hasNext()) {
			CharacterClasses characterClass = it.next();
			Integer minOccurrence = characterSet.getMinOccurrence(characterClass);
			Integer maxOccurrence = characterSet.getMaxOccurrence(characterClass);
			String characterClassName = getCharacterClassName(characterClass);
			if (minOccurrence != null && minOccurrence > 0) {
				requirements.append("\n");
				requirements.append(L10nUtil.getMessage(MessageCodes.PASSWORD_CHARACTER_CLASS_MIN_REQUIREMENT, DefaultMessages.PASSWORD_CHARACTER_CLASS_MIN_REQUIREMENT,
						minOccurrence, characterClassName));
			}
			if (maxOccurrence != null && maxOccurrence < maxLength) {
				requirements.append("\n");
				requirements.append(L10nUtil.getMessage(MessageCodes.PASSWORD_CHARACTER_CLASS_MAX_REQUIREMENT, DefaultMessages.PASSWORD_CHARACTER_CLASS_MAX_REQUIREMENT,
						maxOccurrence, characterClassName));
			}
		}
		if (distancePasswordHistory < 0) {
			requirements.append("\n");
			requirements.append(L10nUtil.getMessage(MessageCodes.PASSWORD_COMPLETE_PASSWORD_HISTORY_REQUIREMENT, DefaultMessages.PASSWORD_COMPLETE_PASSWORD_HISTORY_REQUIREMENT,
					minLevenshteinDistance));
		} else if (distancePasswordHistory > 0) {
			requirements.append("\n");
			requirements.append(L10nUtil.getMessage(MessageCodes.PASSWORD_DISTANCE_PASSWORD_HISTORY_REQUIREMENT, DefaultMessages.PASSWORD_DISTANCE_PASSWORD_HISTORY_REQUIREMENT,
					minLevenshteinDistance, distancePasswordHistory));
		}
		if (adminIgnorePolicy) {
			requirements.append("\n");
			requirements.append(L10nUtil.getMessage(MessageCodes.PASSWORD_ADMIN_IGNORE_POLICY_REQUIREMENT, DefaultMessages.PASSWORD_ADMIN_IGNORE_POLICY_REQUIREMENT));
		}
		return requirements.toString();
	}

	public boolean isAdminIgnorePolicy() {
		return adminIgnorePolicy;
	}
}
