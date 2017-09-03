package org.phoenixctms.ctsms.security;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusEntry;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao;
import org.phoenixctms.ctsms.domain.ECRFFieldValue;
import org.phoenixctms.ctsms.domain.ECRFFieldValueDao;
import org.phoenixctms.ctsms.domain.ECRFStatusEntry;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldValue;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.Signature;
import org.phoenixctms.ctsms.domain.Visit;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;

public class EcrfSignature extends EntitySignature {

	static class EcrfFieldValues implements Serializable {

		private ECRF ecrf;
		private Proband proband;
		private Collection<ECRFFieldValue> values;
		private Collection<ECRFFieldStatusEntry> statusEntries;

		public EcrfFieldValues(ECRF ecrf, Proband proband, Collection<ECRFFieldValue> values, Collection<ECRFFieldStatusEntry> statusEntries) {
			this.ecrf = ecrf;
			this.proband = proband;
			this.values = values;
			this.statusEntries = statusEntries;
		}

		public ECRF getEcrf() {
			return ecrf;
		}

		public Proband getProband() {
			return proband;
		}

		public Collection<ECRFFieldStatusEntry> getStatusEntries() {
			return statusEntries;
		}

		public Collection<ECRFFieldValue> getValues() {
			return values;
		}
	}

	private final static HashMap<Class, HashSet<String>> FIELDS_WHITELIST_MAP = new HashMap<Class, HashSet<String>>();
	static {
		addFieldToMap(FIELDS_WHITELIST_MAP, EcrfFieldValues.class, "values");
		addFieldToMap(FIELDS_WHITELIST_MAP, EcrfFieldValues.class, "statusEntries");
		addFieldToMap(FIELDS_WHITELIST_MAP, EcrfFieldValues.class, "proband");
		addFieldToMap(FIELDS_WHITELIST_MAP, EcrfFieldValues.class, "ecrf");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRF.class, "name");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRF.class, "title");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRF.class, "description");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRF.class, "visit");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRF.class, "group");
		addFieldToMap(FIELDS_WHITELIST_MAP, Visit.class, "title");
		addFieldToMap(FIELDS_WHITELIST_MAP, Visit.class, "token");
		addFieldToMap(FIELDS_WHITELIST_MAP, ProbandGroup.class, "title");
		addFieldToMap(FIELDS_WHITELIST_MAP, ProbandGroup.class, "token");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldValue.class, "index");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldValue.class, "value");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldValue.class, "modifiedTimestamp");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldValue.class, "version");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldValue.class, "modifiedUser");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldValue.class, "reasonForChange");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldValue.class, "changeComment");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldValue.class, "stringValue");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldValue.class, "booleanValue");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldValue.class, "longValue");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldValue.class, "floatValue");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldValue.class, "dateValue");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldValue.class, "timestampValue");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldValue.class, "inkValue");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldValue.class, "selectionValues");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldSelectionSetValue.class, "value");
		addFieldToMap(FIELDS_WHITELIST_MAP, InputFieldSelectionSetValue.class, "nameL10nKey");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldStatusEntry.class, "index");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldStatusEntry.class, "modifiedTimestamp");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldStatusEntry.class, "version");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldStatusEntry.class, "modifiedUser");
		addFieldToMap(FIELDS_WHITELIST_MAP, ECRFFieldStatusEntry.class, "comment");
	}



	private static EcrfFieldValues getEntity(ECRFStatusEntry statusEntry, ECRFFieldValueDao ecrfFieldValueDao, ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao) {
		return new EcrfFieldValues(statusEntry.getEcrf(), statusEntry.getListEntry().getProband(),
				ecrfFieldValueDao.findByListEntryEcrf(statusEntry.getListEntry().getId(), statusEntry.getEcrf().getId(), true, null),
				ecrfFieldStatusEntryDao.findByListEntryEcrf(statusEntry.getListEntry().getId(), statusEntry.getEcrf().getId(), true, null));
	}

	public static byte[] sign(ECRFStatusEntry statusEntry, Timestamp now, StringBuilder comment, ECRFFieldValueDao ecrfFieldValueDao,
			ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao) throws Exception {
		return sign(EcrfSignature.class, getEntity(statusEntry, ecrfFieldValueDao, ecrfFieldStatusEntryDao), now,
				Settings.getInt(SettingCodes.ECRF_SIGNATURE_ENTITY_DEPTH, Bundle.SETTINGS, DefaultSettings.ECRF_SIGNATURE_ENTITY_DEPTH), comment);
	}

	public static boolean verify(Signature signature, StringBuilder comment, ECRFFieldValueDao ecrfFieldValueDao, ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao) throws Exception {
		return verify(EcrfSignature.class, signature.getSignatureData(), getEntity(signature.getEcrfStatusEntry(), ecrfFieldValueDao, ecrfFieldStatusEntryDao),
				signature.getSignee(), signature.getTimestamp(),
				Settings.getInt(SettingCodes.ECRF_SIGNATURE_ENTITY_DEPTH, Bundle.SETTINGS, DefaultSettings.ECRF_SIGNATURE_ENTITY_DEPTH), comment);
	}


	@Override
	protected boolean isFieldOmitted(Class graph, String field) {
		return !fieldMapContains(FIELDS_WHITELIST_MAP, graph, field);
	}
}
