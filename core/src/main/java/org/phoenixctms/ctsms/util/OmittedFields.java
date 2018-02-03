package org.phoenixctms.ctsms.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.phoenixctms.ctsms.vo.BankAccountInVO;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.DiagnosisInVO;
import org.phoenixctms.ctsms.vo.DiagnosisOutVO;
import org.phoenixctms.ctsms.vo.FileContentInVO;
import org.phoenixctms.ctsms.vo.FileContentOutVO;
import org.phoenixctms.ctsms.vo.FileInVO;
import org.phoenixctms.ctsms.vo.FileOutVO;
import org.phoenixctms.ctsms.vo.FileStreamInVO;
import org.phoenixctms.ctsms.vo.FileStreamOutVO;
import org.phoenixctms.ctsms.vo.JournalEntryInVO;
import org.phoenixctms.ctsms.vo.JournalEntryOutVO;
import org.phoenixctms.ctsms.vo.MedicationInVO;
import org.phoenixctms.ctsms.vo.MedicationOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferInVO;
import org.phoenixctms.ctsms.vo.MoneyTransferOutVO;
import org.phoenixctms.ctsms.vo.ProbandAddressInVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueInVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandImageInVO;
import org.phoenixctms.ctsms.vo.ProbandImageOutVO;
import org.phoenixctms.ctsms.vo.ProbandInVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProcedureInVO;
import org.phoenixctms.ctsms.vo.ProcedureOutVO;

import com.thoughtworks.xstream.XStream;

public final class OmittedFields {

	public final static String OBFUSCATED_STRING = "********";
	private static HashMap<Class, HashSet<String>> protectedOutVOFieldMap = new HashMap<Class, HashSet<String>>();
	private static HashMap<Class, HashSet<String>> protectedInVOFieldMap = new HashMap<Class, HashSet<String>>();
	private static HashMap<Class, HashSet<String>> lcProtectedOutVOFieldMap = new HashMap<Class, HashSet<String>>();
	private static HashMap<Class, HashSet<String>> lcProtectedInVOFieldMap = new HashMap<Class, HashSet<String>>();
	private static HashSet<String> protectedCriterionProperties = new HashSet<String>();
	// private static final boolean IGNORE_CASE = true;
	static {
		addProtectedField(protectedOutVOFieldMap, BankAccountOutVO.class, "accountHolderName");
		addProtectedField(protectedOutVOFieldMap, BankAccountOutVO.class, "accountNumber");
		addProtectedField(protectedOutVOFieldMap, BankAccountOutVO.class, "bankCodeNumber");
		addProtectedField(protectedOutVOFieldMap, BankAccountOutVO.class, "bankName");
		addProtectedField(protectedOutVOFieldMap, BankAccountOutVO.class, "bic");
		addProtectedField(protectedOutVOFieldMap, BankAccountOutVO.class, "iban");
		addProtectedField(protectedOutVOFieldMap, BankAccountOutVO.class, "name");
		addProtectedField(protectedOutVOFieldMap, FileOutVO.class, "comment");
		addProtectedField(protectedOutVOFieldMap, FileOutVO.class, "fileName");
		addProtectedField(protectedOutVOFieldMap, FileOutVO.class, "title");
		addProtectedField(protectedOutVOFieldMap, FileContentOutVO.class, "datas");
		addProtectedField(protectedOutVOFieldMap, FileContentOutVO.class, "fileName");
		addProtectedField(protectedOutVOFieldMap, FileStreamOutVO.class, "fileName");
		addProtectedField(protectedOutVOFieldMap, FileStreamOutVO.class, "stream");
		addProtectedField(protectedOutVOFieldMap, JournalEntryOutVO.class, "comment");
		addProtectedField(protectedOutVOFieldMap, JournalEntryOutVO.class, "title");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "careOf");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "cityName");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "countryName");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "doorNumber");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "entrance");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "houseNumber");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "name");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "streetName");
		addProtectedField(protectedOutVOFieldMap, ProbandAddressOutVO.class, "zipCode");
		addProtectedField(protectedOutVOFieldMap, ProbandContactDetailValueOutVO.class, "value");
		addProtectedField(protectedOutVOFieldMap, ProbandContactDetailValueOutVO.class, "comment");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "citizenship");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "dateOfBirth");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "firstName");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "lastName");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "nameWithTitles");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "nameSortable");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "name");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "postpositionedTitle1");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "postpositionedTitle2");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "postpositionedTitle3");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "prefixedTitle1");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "prefixedTitle2");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "prefixedTitle3");
		addProtectedField(protectedOutVOFieldMap, ProbandOutVO.class, "comment");
		addProtectedField(protectedOutVOFieldMap, ProbandImageOutVO.class, "datas");
		addProtectedField(protectedOutVOFieldMap, ProbandImageOutVO.class, "fileName");
		addProtectedField(protectedOutVOFieldMap, ProbandListStatusEntryOutVO.class, "reason");
		addProtectedField(protectedOutVOFieldMap, ProbandStatusEntryOutVO.class, "comment");
		addProtectedField(protectedOutVOFieldMap, ProbandTagValueOutVO.class, "value");
		addProtectedField(protectedOutVOFieldMap, DiagnosisOutVO.class, "comment");
		addProtectedField(protectedOutVOFieldMap, ProcedureOutVO.class, "comment");
		addProtectedField(protectedOutVOFieldMap, MedicationOutVO.class, "comment");
		addProtectedField(protectedOutVOFieldMap, MoneyTransferOutVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, BankAccountInVO.class, "accountHolderName");
		addProtectedField(protectedInVOFieldMap, BankAccountInVO.class, "accountNumber");
		addProtectedField(protectedInVOFieldMap, BankAccountInVO.class, "bankCodeNumber");
		addProtectedField(protectedInVOFieldMap, BankAccountInVO.class, "bankName");
		addProtectedField(protectedInVOFieldMap, BankAccountInVO.class, "bic");
		addProtectedField(protectedInVOFieldMap, BankAccountInVO.class, "iban");
		addProtectedField(protectedInVOFieldMap, FileInVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, FileInVO.class, "title");
		addProtectedField(protectedInVOFieldMap, FileContentInVO.class, "datas");
		addProtectedField(protectedInVOFieldMap, FileContentInVO.class, "fileName");
		addProtectedField(protectedInVOFieldMap, FileStreamInVO.class, "fileName");
		addProtectedField(protectedInVOFieldMap, FileStreamInVO.class, "stream");
		addProtectedField(protectedInVOFieldMap, JournalEntryInVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, JournalEntryInVO.class, "title");
		addProtectedField(protectedInVOFieldMap, ProbandAddressInVO.class, "careOf");
		addProtectedField(protectedInVOFieldMap, ProbandAddressInVO.class, "cityName");
		addProtectedField(protectedInVOFieldMap, ProbandAddressInVO.class, "countryName");
		addProtectedField(protectedInVOFieldMap, ProbandAddressInVO.class, "doorNumber");
		addProtectedField(protectedInVOFieldMap, ProbandAddressInVO.class, "entrance");
		addProtectedField(protectedInVOFieldMap, ProbandAddressInVO.class, "houseNumber");
		addProtectedField(protectedInVOFieldMap, ProbandAddressInVO.class, "streetName");
		addProtectedField(protectedInVOFieldMap, ProbandAddressInVO.class, "zipCode");
		addProtectedField(protectedInVOFieldMap, ProbandContactDetailValueInVO.class, "value");
		addProtectedField(protectedInVOFieldMap, ProbandContactDetailValueInVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "citizenship");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "dateOfBirth");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "firstName");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "lastName");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "postpositionedTitle1");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "postpositionedTitle2");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "postpositionedTitle3");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "prefixedTitle1");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "prefixedTitle2");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "prefixedTitle3");
		addProtectedField(protectedInVOFieldMap, ProbandInVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, ProbandImageInVO.class, "datas");
		addProtectedField(protectedInVOFieldMap, ProbandImageInVO.class, "fileName");
		addProtectedField(protectedInVOFieldMap, ProbandListStatusEntryInVO.class, "reason");
		addProtectedField(protectedInVOFieldMap, ProbandStatusEntryInVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, ProbandTagValueInVO.class, "value");
		addProtectedField(protectedInVOFieldMap, DiagnosisInVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, ProcedureInVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, MedicationInVO.class, "comment");
		addProtectedField(protectedInVOFieldMap, MoneyTransferInVO.class, "comment");
		Iterator<Class> it = protectedOutVOFieldMap.keySet().iterator();
		while (it.hasNext()) {
			Class vo = it.next();
			Iterator<String> fieldNamesIt = protectedOutVOFieldMap.get(vo).iterator();
			while (fieldNamesIt.hasNext()) {
				addProtectedField(lcProtectedOutVOFieldMap, vo, fieldNamesIt.next().toLowerCase());
			}
		}
		it = protectedInVOFieldMap.keySet().iterator();
		while (it.hasNext()) {
			Class vo = it.next();
			Iterator<String> fieldNamesIt = protectedInVOFieldMap.get(vo).iterator();
			while (fieldNamesIt.hasNext()) {
				addProtectedField(lcProtectedInVOFieldMap, vo, fieldNamesIt.next().toLowerCase());
			}
		}
		protectedCriterionProperties.add("proband.personParticulars.firstNameHash");
		protectedCriterionProperties.add("proband.personParticulars.lastNameHash");
		protectedCriterionProperties.add("proband.personParticulars.dateOfBirthHash");
		protectedCriterionProperties.add("proband.tagValues.valueHash");
		protectedCriterionProperties.add("proband.contactDetailValues.valueHash");
		protectedCriterionProperties.add("proband.addresses.houseNumberHash");
		protectedCriterionProperties.add("proband.addresses.entranceHash");
		protectedCriterionProperties.add("proband.addresses.doorNumberHash");
		protectedCriterionProperties.add("proband.bankAccounts.accountHolderName");
		protectedCriterionProperties.add("proband.bankAccounts.accountNumber");
		protectedCriterionProperties.add("proband.bankAccounts.bic");
		protectedCriterionProperties.add("proband.bankAccounts.iban");
		protectedCriterionProperties.add("proband.files.fileName");
		protectedCriterionProperties.add("proband.files.title");
		protectedCriterionProperties.add("proband.files.comment");
	}

	private static void addProtectedField(HashMap<Class, HashSet<String>> fieldMap, Class vo, String fieldName) {
		if (fieldMap.containsKey(vo)) {
			fieldMap.get(vo).add(fieldName);
		} else {
			HashSet<String> fieldNames = new HashSet<String>();
			fieldMap.put(vo, fieldNames);
			fieldNames.add(fieldName);
		}
	}

	public static boolean isOmitted(Class vo, String fieldName, boolean ignoreCase) {
		if (ignoreCase) {
			if (fieldName != null) {
				return isOmitted(lcProtectedOutVOFieldMap, vo, fieldName.toLowerCase()) || isOmitted(lcProtectedInVOFieldMap, vo, fieldName.toLowerCase());
			}
			return false;
		} else {
			return isOmitted(protectedOutVOFieldMap, vo, fieldName) || isOmitted(protectedInVOFieldMap, vo, fieldName);
		}
	}

	private static boolean isOmitted(HashMap<Class, HashSet<String>> fieldMap, Class vo, String fieldName) {
		return fieldMap.containsKey(vo) ? fieldMap.get(vo).contains(fieldName) : false;
	}

	public static boolean isOmitted(String property) {
		return protectedCriterionProperties.contains(property);
	}

	private static void omitFields(HashMap<Class, HashSet<String>> fieldMap, XStream xstream) {
		Iterator<Class> it = fieldMap.keySet().iterator();
		while (it.hasNext()) {
			Class vo = it.next();
			Iterator<String> fieldNamesIt = fieldMap.get(vo).iterator();
			while (fieldNamesIt.hasNext()) {
				xstream.omitField(vo, fieldNamesIt.next());
			}
		}
	}

	public static void omitFields(XStream xstream) {
		omitFields(protectedOutVOFieldMap, xstream);
		omitFields(protectedInVOFieldMap, xstream);
	}

	private OmittedFields() {
	}
}
