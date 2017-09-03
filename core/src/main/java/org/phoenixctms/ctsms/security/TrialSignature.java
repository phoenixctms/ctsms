package org.phoenixctms.ctsms.security;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;

import org.phoenixctms.ctsms.domain.DutyRosterTurn;
import org.phoenixctms.ctsms.domain.ECRFFieldValue;
import org.phoenixctms.ctsms.domain.ECRFStatusEntry;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.Notification;
import org.phoenixctms.ctsms.domain.Signature;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

public class TrialSignature extends EntitySignature {

	private final static HashMap<Class, HashSet<String>> FIELDS_BLACKLIST_MAP = new HashMap<Class, HashSet<String>>();

	static {
		addFieldToMap(FIELDS_BLACKLIST_MAP, Signature.class, null);
		addFieldToMap(FIELDS_BLACKLIST_MAP, Notification.class, null);
		addFieldToMap(FIELDS_BLACKLIST_MAP, JournalEntry.class, null);
		addFieldToMap(FIELDS_BLACKLIST_MAP, DutyRosterTurn.class, null);
		addFieldToMap(FIELDS_BLACKLIST_MAP, ECRFFieldValue.class, null);
		addFieldToMap(FIELDS_BLACKLIST_MAP, ECRFStatusEntry.class, null);

	}

	public static byte[] sign(Trial trial, Timestamp now, StringBuilder comment) throws Exception {
		return sign(TrialSignature.class, trial, now, Settings.getInt(SettingCodes.TRIAL_SIGNATURE_ENTITY_DEPTH, Bundle.SETTINGS, DefaultSettings.TRIAL_SIGNATURE_ENTITY_DEPTH),
				comment);
	}

	public static boolean verify(Signature signature, StringBuilder comment) throws Exception {
		return verify(TrialSignature.class, signature.getSignatureData(), signature.getTrial(), signature.getSignee(), signature.getTimestamp(),
				Settings.getInt(SettingCodes.TRIAL_SIGNATURE_ENTITY_DEPTH, Bundle.SETTINGS, DefaultSettings.TRIAL_SIGNATURE_ENTITY_DEPTH), comment);
	}

	@Override
	protected boolean isFieldOmitted(Class graph, String field) {
		return fieldMapContains(FIELDS_BLACKLIST_MAP, graph, field);
		// if (Signature.class.isAssignableFrom(graph)) {
		// return true;
		// } else if (Notification.class.isAssignableFrom(graph)) {
		// return true;
		// } else if (JournalEntry.class.isAssignableFrom(graph)) {
		// return true;
		// } else if (DutyRosterTurn.class.isAssignableFrom(graph)) {
		// return true;
		// } else if (ECRFFieldValue.class.isAssignableFrom(graph)) {
		// return true;
		// } else if (ECRFStatusEntry.class.isAssignableFrom(graph)) {
		// return true;
		// }
		// return false;
		//		if (Trial.class.isAssignableFrom(graph)) {
		//			if ("signatures".equals(field) ||
		//					"notifications".equals(field) ||
		//					"journalEntries".equals(field)) {
		//				return true;
		//			}
		//		}
		//		return false;
	}
}
