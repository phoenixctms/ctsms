// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::proband::ProbandService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.proband;

import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.DiagnosisCollisionFinder;
import org.phoenixctms.ctsms.adapt.InquiryValueCollisionFinder;
import org.phoenixctms.ctsms.adapt.MaxCostTypesAdapter;
import org.phoenixctms.ctsms.adapt.MedicationCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandAddressTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.ProbandContactDetailTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.ProbandStatusEntryCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandTagAdapter;
import org.phoenixctms.ctsms.adapt.ProcedureCollisionFinder;
import org.phoenixctms.ctsms.compare.ComparatorFactory;
import org.phoenixctms.ctsms.compare.InquiryValueOutVOComparator;
import org.phoenixctms.ctsms.domain.AlphaId;
import org.phoenixctms.ctsms.domain.AnimalContactParticulars;
import org.phoenixctms.ctsms.domain.Asp;
import org.phoenixctms.ctsms.domain.AspDao;
import org.phoenixctms.ctsms.domain.AspSubstance;
import org.phoenixctms.ctsms.domain.AspSubstanceDao;
import org.phoenixctms.ctsms.domain.BankAccount;
import org.phoenixctms.ctsms.domain.BankAccountDao;
import org.phoenixctms.ctsms.domain.Department;
import org.phoenixctms.ctsms.domain.Diagnosis;
import org.phoenixctms.ctsms.domain.DiagnosisDao;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldValue;
import org.phoenixctms.ctsms.domain.Inquiry;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InquiryValue;
import org.phoenixctms.ctsms.domain.InquiryValueDao;
import org.phoenixctms.ctsms.domain.InventoryBookingDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.Medication;
import org.phoenixctms.ctsms.domain.MedicationDao;
import org.phoenixctms.ctsms.domain.MimeType;
import org.phoenixctms.ctsms.domain.MoneyTransfer;
import org.phoenixctms.ctsms.domain.MoneyTransferDao;
import org.phoenixctms.ctsms.domain.NotificationDao;
import org.phoenixctms.ctsms.domain.OpsCode;
import org.phoenixctms.ctsms.domain.PrivacyConsentStatusType;
import org.phoenixctms.ctsms.domain.PrivacyConsentStatusTypeDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandAddress;
import org.phoenixctms.ctsms.domain.ProbandAddressDao;
import org.phoenixctms.ctsms.domain.ProbandCategory;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValue;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao;
import org.phoenixctms.ctsms.domain.ProbandContactParticulars;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandStatusEntryDao;
import org.phoenixctms.ctsms.domain.ProbandStatusType;
import org.phoenixctms.ctsms.domain.ProbandTagValue;
import org.phoenixctms.ctsms.domain.ProbandTagValueDao;
import org.phoenixctms.ctsms.domain.Procedure;
import org.phoenixctms.ctsms.domain.ProcedureDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.VisitScheduleItem;
import org.phoenixctms.ctsms.domain.VisitScheduleItemDao;
import org.phoenixctms.ctsms.enumeration.ExportStatus;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.Sex;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.excel.VisitScheduleExcelWriter;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.pdf.PDFImprinter;
import org.phoenixctms.ctsms.pdf.ProbandLetterPDFPainter;
import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.util.date.DateInterval;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.BankAccountInVO;
import org.phoenixctms.ctsms.vo.BankAccountOutVO;
import org.phoenixctms.ctsms.vo.DiagnosisInVO;
import org.phoenixctms.ctsms.vo.DiagnosisOutVO;
import org.phoenixctms.ctsms.vo.InquiriesPDFVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueJsonVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValuesOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.MedicationInVO;
import org.phoenixctms.ctsms.vo.MedicationOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferInVO;
import org.phoenixctms.ctsms.vo.MoneyTransferOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandAddressInVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueInVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandImageInVO;
import org.phoenixctms.ctsms.vo.ProbandImageOutVO;
import org.phoenixctms.ctsms.vo.ProbandInVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProcedureInVO;
import org.phoenixctms.ctsms.vo.ProcedureOutVO;
import org.phoenixctms.ctsms.vo.ReimbursementsExcelVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.vocycle.ProbandReflexionGraph;

/**
 * @see org.phoenixctms.ctsms.service.proband.ProbandService
 */
public class ProbandServiceImpl
		extends ProbandServiceBase {

	private static JournalEntry logSystemMessage(Proband proband, ProbandAddressOutVO addressVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		boolean journalEncrypted = CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null);
		return journalEntryDao.addSystemMessage(proband, now, modified, systemMessageCode, journalEncrypted ? new Object[] { addressVO.getName() } : null,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !journalEncrypted) });
	}

	private static JournalEntry logSystemMessage(Trial trial, ProbandOutVO probandVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		boolean journalEncrypted = CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null);
		return journalEntryDao.addSystemMessage(trial, now, modified, systemMessageCode, journalEncrypted ? new Object[] { CommonUtil.probandOutVOToString(probandVO) }
				: new Object[] { Long.toString(probandVO.getId()) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !journalEncrypted) });
	}

	private void addUpdateInquiryValue(InquiryValueInVO inquiryValueIn, Proband proband, Inquiry inquiry, Timestamp now, User user, boolean logTrial,
			boolean logProband, ArrayList<InquiryValueOutVO> outInquiryValues,
			ArrayList<InquiryValueJsonVO> outJsInquiryValues) throws Exception {
		InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
		Long id = inquiryValueIn.getId();
		InquiryValueOutVO result = null;
		InquiryValueJsonVO resultJs = null;
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (id == null) {
			if (inquiry.isDisabled()) {
				inquiryValueIn = ServiceUtil.createPresetInquiryInValue(inquiry, proband.getId(), this.getInputFieldSelectionSetValueDao());
			}
			checkInquiryValueInput(inquiryValueIn, proband, inquiry);
			ServiceUtil.addAutocompleteSelectionSetValue(inquiry.getField(), inquiryValueIn.getTextValue(), now, user, this.getInputFieldSelectionSetValueDao(), journalEntryDao);
			InquiryValue inquiryValue = inquiryValueDao.inquiryValueInVOToEntity(inquiryValueIn);
			CoreUtil.modifyVersion(inquiryValue, now, user);
			InputFieldValue inputFieldValue = inquiryValue.getValue();
			this.getInputFieldValueDao().create(inputFieldValue);
			inquiryValue = inquiryValueDao.create(inquiryValue);
			if (outInquiryValues != null || logTrial || logProband) {
				result = inquiryValueDao.toInquiryValueOutVO(inquiryValue);
			}
			if (outJsInquiryValues != null && !CommonUtil.isEmptyString(inquiry.getJsVariableName())) {
				resultJs = inquiryValueDao.toInquiryValueJsonVO(inquiryValue);
			}
			if (logProband) {
				ServiceUtil.logSystemMessage(proband, result.getInquiry().getTrial(), now, user, SystemMessageCodes.INQUIRY_VALUE_CREATED, result, null, journalEntryDao);
			}
			// ServiceUtil.logSystemMessage(inquiry.getTrial(), result.getInquiry().getTrial(), now, user, SystemMessageCodes.INQUIRY_VALUE_CREATED, result, null, journalEntryDao);
			if (logTrial) {
				ServiceUtil.logSystemMessage(inquiry.getTrial(), result.getProband(), now, user, SystemMessageCodes.INQUIRY_VALUE_CREATED, result, null, journalEntryDao);
			}
		} else {
			InquiryValue originalInquiryValue = CheckIDUtil.checkInquiryValueId(id, inquiryValueDao);
			if (!inquiry.isDisabled()
					&& !ServiceUtil.inquiryValueEquals(inquiryValueIn, originalInquiryValue.getValue())) {
				checkInquiryValueInput(inquiryValueIn, proband, inquiry); // access original associations before evict
				ServiceUtil.addAutocompleteSelectionSetValue(inquiry.getField(), inquiryValueIn.getTextValue(), now, user, this.getInputFieldSelectionSetValueDao(),
						journalEntryDao);
				InquiryValueOutVO original = null;
				if (logProband || logTrial) {
					original = inquiryValueDao.toInquiryValueOutVO(originalInquiryValue);
				}
				inquiryValueDao.evict(originalInquiryValue);
				InquiryValue inquiryValue = inquiryValueDao.inquiryValueInVOToEntity(inquiryValueIn);
				CoreUtil.modifyVersion(originalInquiryValue, inquiryValue, now, user);
				inquiryValueDao.update(inquiryValue);
				if (outInquiryValues != null || logTrial || logProband) {
					result = inquiryValueDao.toInquiryValueOutVO(inquiryValue);
				}
				if (outJsInquiryValues != null && !CommonUtil.isEmptyString(inquiry.getJsVariableName())) {
					resultJs = inquiryValueDao.toInquiryValueJsonVO(inquiryValue);
				}
				if (logProband) {
					ServiceUtil.logSystemMessage(proband, result.getInquiry().getTrial(), now, user, SystemMessageCodes.INQUIRY_VALUE_UPDATED, result, original, journalEntryDao);
				}
				// ServiceUtil
				// .logSystemMessage(inquiry.getTrial(), result.getInquiry().getTrial(), now, user, SystemMessageCodes.INQUIRY_VALUE_UPDATED, result, original,
				// journalEntryDao);
				if (logTrial) {
					ServiceUtil
							.logSystemMessage(inquiry.getTrial(), result.getProband(), now, user, SystemMessageCodes.INQUIRY_VALUE_UPDATED, result, original,
									journalEntryDao);
				}
			} else {
				if (outInquiryValues != null) {
					result = inquiryValueDao.toInquiryValueOutVO(originalInquiryValue);
				}
				if (outJsInquiryValues != null && !CommonUtil.isEmptyString(inquiry.getJsVariableName())) {
					resultJs = inquiryValueDao.toInquiryValueJsonVO(originalInquiryValue);
				}
			}
		}
		if (outInquiryValues != null) {
			outInquiryValues.add(result);
		}
		if (resultJs != null) {
			outJsInquiryValues.add(resultJs);
		}
	}

	private void checkBankAccountInput(BankAccountInVO bankAccountIn) throws ServiceException {
		ProbandDao probandDao = this.getProbandDao();
		// referential checks
		Proband proband = CheckIDUtil.checkProbandId(bankAccountIn.getProbandId(), probandDao);
		if (!probandDao.toProbandOutVO(proband).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		if (!proband.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.BANK_ACCOUNT_PROBAND_NOT_PERSON);
		}
		ServiceUtil.checkProbandLocked(proband);
		String iban = bankAccountIn.getIban();
		String bic = bankAccountIn.getBic();
		String accountNumber = bankAccountIn.getAccountNumber();
		String bankCodeNumber = bankAccountIn.getBankCodeNumber();
		if (bankAccountIn.getNa()) {
			if (bankAccountIn.getAccountHolderName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ACCOUNT_HOLDER_NAME_NOT_NULL);
			}
			if (bankAccountIn.getBankName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.BANK_NAME_NOT_NULL);
			}
			if (iban != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.IBAN_NOT_NULL);
			}
			if (bic != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.BIC_NOT_NULL);
			}
			if (accountNumber != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ACCOUNT_NUMBER_NOT_NULL);
			}
			if (bankCodeNumber != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.BANK_CODE_NUMBER_NOT_NULL);
			}
		} else {
			if (CommonUtil.isEmptyString(bankAccountIn.getAccountHolderName())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ACCOUNT_HOLDER_NAME_REQUIRED);
			}
			// String iban = bankAccountIn.getIban();
			// String bic = bankAccountIn.getBic();
			if (CommonUtil.isEmptyString(iban) != CommonUtil.isEmptyString(bic)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.BANK_ACCOUNT_IBAN_AND_BIC_REQUIRED);
			}
			if (!CommonUtil.isEmptyString(iban) && !CommonUtil.checkIban(iban)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_IBAN);
			}
			if (!CommonUtil.isEmptyString(bic) && !CommonUtil.checkBic(bic)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_BIC);
			}
			// String accountNumber = bankAccountIn.getAccountNumber();
			// String bankCodeNumber = bankAccountIn.getBankCodeNumber();
			if (CommonUtil.isEmptyString(accountNumber) != CommonUtil.isEmptyString(bankCodeNumber)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.BANK_ACCOUNT_ACCOUNT_NUMBER_AND_BANK_CODE_NUMBER_REQUIRED);
			}
			if (CommonUtil.isEmptyString(iban) && CommonUtil.isEmptyString(accountNumber)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.IBAN_OR_BANK_ACCOUNT_ACCOUNT_NUMBER_REQUIRED);
			}
		}
	}

	private void checkDiagnosisInput(DiagnosisInVO diagnosisIn) throws ServiceException {
		ProbandDao probandDao = this.getProbandDao();
		// referential checks
		Proband proband = CheckIDUtil.checkProbandId(diagnosisIn.getProbandId(), probandDao);
		AlphaId alphaId = CheckIDUtil.checkAlphaIdId(diagnosisIn.getCodeId(), this.getAlphaIdDao());
		if (!probandDao.toProbandOutVO(proband).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(proband);
		if (diagnosisIn.getStart() == null && diagnosisIn.getStop() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DIAGNOSIS_START_DATE_REQUIRED);
		}
		// other input checks
		if (diagnosisIn.getStart() != null && diagnosisIn.getStop() != null && diagnosisIn.getStop().compareTo(diagnosisIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DIAGNOSIS_END_DATE_LESS_THAN_OR_EQUAL_TO_START_DATE);
		}
		if ((new DiagnosisCollisionFinder(probandDao, this.getDiagnosisDao())).collides(diagnosisIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.DIAGNOSIS_OVERLAPPING);
		}
	}

	private void checkInquiryValueInput(InquiryValueInVO inquiryValueIn, Proband proband, Inquiry inquiry) throws ServiceException {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = inquiry.getField();
		inputFieldDao.lock(inputField, LockMode.PESSIMISTIC_WRITE);
		// InputField inputField = ServiceUtil.checkInputFieldId(inquiry.getField().getId(), inputFieldDao, LockMode.PESSIMISTIC_WRITE);
		// InputFieldOutVO inputFieldVO = this.getInputFieldDao().toInputFieldOutVO(inquiry.getField());
		ServiceUtil.checkInputFieldTextValue(inputField, inquiry.isOptional(), inquiryValueIn.getTextValue(), inputFieldDao, this.getInputFieldSelectionSetValueDao());
		ServiceUtil.checkInputFieldBooleanValue(inputField, inquiry.isOptional(), inquiryValueIn.getBooleanValue(), inputFieldDao);
		ServiceUtil.checkInputFieldLongValue(inputField, inquiry.isOptional(), inquiryValueIn.getLongValue(), inputFieldDao);
		ServiceUtil.checkInputFieldFloatValue(inputField, inquiry.isOptional(), inquiryValueIn.getFloatValue(), inputFieldDao);
		ServiceUtil.checkInputFieldDateValue(inputField, inquiry.isOptional(), inquiryValueIn.getDateValue(), inputFieldDao);
		ServiceUtil.checkInputFieldTimeValue(inputField, inquiry.isOptional(), inquiryValueIn.getTimeValue(), inputFieldDao);
		ServiceUtil.checkInputFieldTimestampValue(inputField, inquiry.isOptional(), inquiryValueIn.getTimestampValue(), inputFieldDao);
		ServiceUtil.checkInputFieldInkValue(inputField, inquiry.isOptional(), inquiryValueIn.getInkValues(), inputFieldDao);
		ServiceUtil.checkInputFieldSelectionSetValues(inputField, inquiry.isOptional(), inquiryValueIn.getSelectionValueIds(), inputFieldDao,
				this.getInputFieldSelectionSetValueDao());
		if ((new InquiryValueCollisionFinder(this.getProbandDao(), this.getInquiryValueDao())).collides(inquiryValueIn)) {
			throw L10nUtil
					.initServiceException(ServiceExceptionCodes.INQUIRY_VALUE_ALREADY_EXISTS, CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(inputField)));
		}
	}

	private void checkMedicationInput(MedicationInVO medicationIn) throws ServiceException {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(medicationIn.getProbandId(), probandDao, LockMode.PESSIMISTIC_WRITE);
		if (!probandDao.toProbandOutVO(proband).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(proband);
		AspDao aspDao = this.getAspDao();
		Asp asp = null;
		if (medicationIn.getAspId() != null) {
			asp = CheckIDUtil.checkAspId(medicationIn.getAspId(), aspDao);
		}
		AspSubstanceDao aspSubstanceDao = this.getAspSubstanceDao();
		Collection<Long> substanceIds = medicationIn.getSubstanceIds();
		if (substanceIds != null && substanceIds.size() > 0) {
			Iterator<Long> it = substanceIds.iterator();
			HashSet<Long> dupeCheck = new HashSet<Long>(substanceIds.size());
			HashSet<Long> aspSubstanceIds;
			Collection<AspSubstance> aspSubstances;
			if (asp != null && ((aspSubstances = asp.getSubstances()) != null) && aspSubstances.size() > 0) {
				aspSubstanceIds = new HashSet<Long>(aspSubstances.size());
				Iterator<AspSubstance> aspSubstancesIt = aspSubstances.iterator();
				while (aspSubstancesIt.hasNext()) {
					aspSubstanceIds.add(aspSubstancesIt.next().getId());
				}
			} else {
				aspSubstanceIds = new HashSet<Long>();
			}
			while (it.hasNext()) {
				Long id = it.next();
				if (id == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_SUBSTANCE_ID_IS_NULL);
				}
				AspSubstance substance = CheckIDUtil.checkAspSubstanceId(id, aspSubstanceDao);
				if (!dupeCheck.add(substance.getId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_DUPLICATE_SUBSTANCE,
							aspSubstanceDao.toAspSubstanceVO(substance).getName());
				}
				if (asp != null && !aspSubstanceIds.remove(id)) { // aspSubstances.size() > 0
					throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_SUBSTANCE_NOT_CONTAINED,
							aspDao.toAspVO(asp).getName(),
							aspSubstanceDao.toAspSubstanceVO(substance).getName());
				}
			}
			if (asp != null && aspSubstanceIds.size() > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_SUBSTANCE_MISSING,
						aspDao.toAspVO(asp).getName(),
						ServiceUtil.aspSubstanceIDsToString(aspSubstanceIds, this.getAspSubstanceDao()));
			}
		} else {
			if (asp == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_SUBSTANCES_REQUIRED);
			}
		}
		Diagnosis diagnosis = null;
		if (medicationIn.getDiagnosisId() != null) {
			diagnosis = CheckIDUtil.checkDiagnosisId(medicationIn.getDiagnosisId(), this.getDiagnosisDao());
			if (!proband.equals(diagnosis.getProband())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_WRONG_DIAGNOSIS, proband.getId().toString()); // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)));
			}
		}
		Procedure procedure = null;
		if (medicationIn.getProcedureId() != null) {
			procedure = CheckIDUtil.checkProcedureId(medicationIn.getProcedureId(), this.getProcedureDao());
			if (!proband.equals(procedure.getProband())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_WRONG_PROCEDURE, proband.getId().toString()); // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)));
			}
		}
		if (medicationIn.getDoseValue() != null) {
			if (medicationIn.getDoseValue() <= 0.0f) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_DOSE_VALUE_LESS_THAN_OR_EQUAL_ZERO);
			}
			if (CommonUtil.isEmptyString(medicationIn.getDoseUnit())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_DOSE_UNIT_REQUIRED);
			}
		} else {
			if (medicationIn.getDoseUnit() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_DOSE_UNIT_NOT_NULL);
			}
		}
		// if (diagnosis == null && procedure == null) {
		if (medicationIn.getStart() == null && medicationIn.getStop() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_START_DATE_REQUIRED);
		}
		// other input checks
		if (medicationIn.getStart() != null && medicationIn.getStop() != null && medicationIn.getStop().compareTo(medicationIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_END_DATE_LESS_THAN_OR_EQUAL_TO_START_DATE);
		}
		if (// diagnosis == null && procedure == null &&
		(new MedicationCollisionFinder(probandDao, this.getMedicationDao())).collides(medicationIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_OVERLAPPING);
		}
		// } else {
		// if (medicationIn.getStart() != null) {
		// x
		// }
		// if (medicationIn.getStop() != null) {
		// y
		// }
		// }
	}

	private void checkMoneyTransferInput(MoneyTransferInVO moneyTransferIn, Long maxAllowedCostTypes) throws ServiceException {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(moneyTransferIn.getProbandId(), probandDao, LockMode.PESSIMISTIC_WRITE);
		if (!probandDao.toProbandOutVO(proband).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		if (!proband.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PROBAND_NOT_PERSON);
		}
		ServiceUtil.checkProbandLocked(proband);
		if (moneyTransferIn.getTrialId() != null) {
			Trial trial = CheckIDUtil.checkTrialId(moneyTransferIn.getTrialId(), this.getTrialDao());
			ServiceUtil.checkTrialLocked(trial);
			(new MaxCostTypesAdapter(maxAllowedCostTypes, this.getTrialDao(), this.getMoneyTransferDao())).checkCategoryInput(moneyTransferIn);
		}
		BankAccount bankAccount = null;
		if (moneyTransferIn.getBankAccountId() != null) {
			bankAccount = CheckIDUtil.checkBankAccountId(moneyTransferIn.getBankAccountId(), this.getBankAccountDao());
			if (!proband.equals(bankAccount.getProband())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_WRONG_BANK_ACCOUNT, proband.getId().toString()); // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)));
			}
		}
		if (PaymentMethod.WIRE_TRANSFER.equals(moneyTransferIn.getMethod())) {
			if (bankAccount == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_BANK_ACCOUNT_REQUIRED);
			}
		} else {
			if (bankAccount != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_BANK_ACCOUNT_NOT_NULL);
			}
			if (moneyTransferIn.getReasonForPayment() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_REASON_FORM_PAYMENT_NOT_NULL);
			}
			if (moneyTransferIn.getReference() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_REFERENCE_NOT_NULL);
			}
		}
		if (PaymentMethod.VOUCHER.equals(moneyTransferIn.getMethod())) {
			if (moneyTransferIn.getAmount() < 0.0f) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_AMOUNT_NEGATIVE);
			}
		} else {
			if (moneyTransferIn.getVoucherCode() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_VOUCHER_CODE_NOT_NULL);
			}
		}
		if (moneyTransferIn.getShowComment() && CommonUtil.isEmptyString(moneyTransferIn.getComment())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_COMMENT_REQUIRED);
		}
	}

	private void checkParents(ProbandInVO probandIn, Proband child) throws ServiceException {
		Iterator<Proband> parentsIt = child.getParents().iterator();
		int parentCount = 0;
		HashSet<Sex> parentGenders = new HashSet<Sex>(Sex.literals().size());
		boolean isParent = false;
		while (parentsIt.hasNext()) {
			Proband parent = parentsIt.next();
			if (parent.getId().equals(probandIn.getId())) {
				isParent = true;
				break;
			}
			if (parent.isPerson()) {
				ProbandContactParticulars personParticlars = parent.getPersonParticulars();
				if (personParticlars != null && personParticlars.getGender() != null) {
					parentGenders.add(personParticlars.getGender());
					// } else {
					// parentGenders.add(null);
				}
			} else {
				AnimalContactParticulars animalParticlars = parent.getAnimalParticulars();
				if (animalParticlars != null && animalParticlars.getGender() != null) {
					parentGenders.add(animalParticlars.getGender());
				}
			}
			parentCount++;
		}
		if (!isParent) {
			ProbandDao probandDao = this.getProbandDao();
			if (parentCount >= 2) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CHILD_TWO_PARENTS, child.getId().toString()); // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(child)));
			}
			// if (!probandIn.getBlinded() && !parentGenders.add(probandIn.getGender())) {
			if (probandIn.getGender() != null && !parentGenders.add(probandIn.getGender())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CHILD_PARENT_WITH_SAME_SEX, child.getId().toString(), // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(child)),
						L10nUtil.getSexName(Locales.USER, probandIn.getGender().name()));
			}
		}
	}

	private void checkProbandAddressInput(ProbandAddressInVO addressIn) throws ServiceException {
		(new ProbandAddressTypeTagAdapter(this.getProbandDao(), this.getAddressTypeDao())).checkTagValueInput(addressIn);
	}

	private void checkProbandContactDetailValueInput(ProbandContactDetailValueInVO contactValueIn) throws ServiceException {
		(new ProbandContactDetailTypeTagAdapter(this.getProbandDao(), this.getContactDetailTypeDao())).checkTagValueInput(contactValueIn);
	}

	private void checkProbandImageInput(ProbandImageInVO probandImage) throws ServiceException {
		if (probandImage.getDatas() != null && probandImage.getDatas().length > 0) {
			Integer probandImageSizeLimit = Settings.getIntNullable(SettingCodes.PROBAND_IMAGE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.PROBAND_IMAGE_SIZE_LIMIT);
			if (probandImageSizeLimit != null && probandImage.getDatas().length > probandImageSizeLimit) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_IMAGE_SIZE_LIMIT_EXCEEDED, CommonUtil.humanReadableByteCount(probandImageSizeLimit));
			}
			if (probandImage.getMimeType() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_IMAGE_MIME_TYPE_REQUIRED);
			}
			Iterator<MimeType> it = this.getMimeTypeDao().findByMimeTypeModule(probandImage.getMimeType(), FileModule.PROBAND_IMAGE).iterator();
			if (!it.hasNext()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_IMAGE_MIME_TYPE_UNKNOWN, probandImage.getMimeType());
			}
			if (!it.next().isImage()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_IMAGE_MIME_TYPE_NO_IMAGE, probandImage.getMimeType());
			}
			Dimension imageDimension = CoreUtil.getImageDimension(probandImage.getDatas());
			if (imageDimension == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_IMAGE_CANNOT_READ_DIMENSIONS);
			} else {
				Integer probandImageMinWidth = Settings.getIntNullable(SettingCodes.PROBAND_IMAGE_MIN_WIDTH, Bundle.SETTINGS, DefaultSettings.PROBAND_IMAGE_MIN_WIDTH);
				Integer probandImageMinHeight = Settings.getIntNullable(SettingCodes.PROBAND_IMAGE_MIN_HEIGHT, Bundle.SETTINGS, DefaultSettings.PROBAND_IMAGE_MIN_HEIGHT);
				if (probandImageMinWidth != null && imageDimension.getWidth() < (double) probandImageMinWidth) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_IMAGE_WIDTH_LESS_THAN_LIMIT, probandImageMinWidth);
				}
				if (probandImageMinHeight != null && imageDimension.getHeight() < (double) probandImageMinHeight) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_IMAGE_HEIGHT_LESS_THAN_LIMIT, probandImageMinHeight);
				}
			}
		}
	}

	private void checkProbandInput(ProbandInVO probandIn) throws ServiceException {
		// referential checks
		CheckIDUtil.checkDepartmentId(probandIn.getDepartmentId(), this.getDepartmentDao());
		ProbandCategory category = CheckIDUtil.checkProbandCategoryId(probandIn.getCategoryId(), this.getProbandCategoryDao());
		if (probandIn.getPhysicianId() != null) {
			CheckIDUtil.checkStaffId(probandIn.getPhysicianId(), this.getStaffDao());
		}
		if (probandIn.getChildIds() != null && probandIn.getChildIds().size() > 0) {
			ProbandDao probandDao = this.getProbandDao();
			ArrayList<Long> childIds = new ArrayList<Long>(probandIn.getChildIds());
			Collections.sort(childIds);
			Iterator<Long> it = childIds.iterator();
			HashSet<Long> dupeCheck = new HashSet<Long>(childIds.size());
			while (it.hasNext()) {
				Long id = it.next();
				if (id == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CHILD_NULL);
				}
				Proband child = CheckIDUtil.checkProbandId(id, probandDao, LockMode.PESSIMISTIC_WRITE);
				if (!dupeCheck.add(child.getId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.DUPLICATE_PROBAND_CHILD, child.getId().toString()); // CommonUtil.probandOutVOToString(
					// probandDao.toProbandOutVO(child)));
				}
				// if (probandVO.isDecrypted()) {
				//
				// }
				checkParents(probandIn, child);
			}
		}
		// other input checks
		if (probandIn.isPerson()) {
			if (!category.isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CATEGORY_NOT_FOR_PERSON_ENTRIES,
						L10nUtil.getProbandCategoryName(Locales.USER, category.getNameL10nKey()));
			}
			if (!probandIn.isBlinded()) {
				if (CommonUtil.isEmptyString(probandIn.getFirstName())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_FIRST_NAME_REQUIRED);
				}
				if (CommonUtil.isEmptyString(probandIn.getLastName())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LAST_NAME_REQUIRED);
				}
				if (probandIn.getDateOfBirth() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_REQUIRED);
				} else if (DateCalc.getStartOfDay(probandIn.getDateOfBirth()).compareTo(new Date()) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_IN_THE_FUTURE);
				}
				if (probandIn.getGender() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GENDER_REQUIRED);
				}
				if (probandIn.getAlias() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_ALIAS_NOT_NULL);
				}
			} else {
				if (probandIn.getPrefixedTitle1() != null || probandIn.getPrefixedTitle2() != null || probandIn.getPrefixedTitle3() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_PREFIXED_TITLES_NOT_NULL);
				}
				if (probandIn.getFirstName() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_FIRST_NAME_NOT_NULL);
				}
				if (probandIn.getLastName() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LAST_NAME_NOT_NULL);
				}
				if (probandIn.getDateOfBirth() != null && DateCalc.getStartOfDay(probandIn.getDateOfBirth()).compareTo(new Date()) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_IN_THE_FUTURE);
				}
				// if (probandIn.getDateOfBirth() != null) {
				// throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_NOT_NULL);
				// }
				// if (probandIn.getGender() != null) {
				// throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GENDER_NOT_NULL);
				// }
				if (probandIn.getPostpositionedTitle1() != null || probandIn.getPostpositionedTitle2() != null || probandIn.getPostpositionedTitle3() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_POSTPOSITIONED_TITLES_NOT_NULL);
				}
				if (probandIn.getCitizenship() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CITIZENSHIP_NOT_NULL);
				}
			}
			if (probandIn.getAnimalName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ANIMAL_NAME_NOT_NULL);
			}
		} else {
			if (!category.isAnimal()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CATEGORY_NOT_FOR_ANIMAL_ENTRIES,
						L10nUtil.getProbandCategoryName(Locales.USER, category.getNameL10nKey()));
			}
			if (!probandIn.isBlinded()) {
				if (CommonUtil.isEmptyString(probandIn.getAnimalName())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ANIMAL_NAME_REQUIRED);
				}
				if (probandIn.getDateOfBirth() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_REQUIRED);
				} else if (DateCalc.getStartOfDay(probandIn.getDateOfBirth()).compareTo(new Date()) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_IN_THE_FUTURE);
				}
				if (probandIn.getGender() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GENDER_REQUIRED);
				}
				if (probandIn.getAlias() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_ALIAS_NOT_NULL);
				}
			} else {
				if (probandIn.getAnimalName() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ANIMAL_NAME_NOT_NULL);
				}
				if (probandIn.getDateOfBirth() != null && DateCalc.getStartOfDay(probandIn.getDateOfBirth()).compareTo(new Date()) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_IN_THE_FUTURE);
				}
				// if (probandIn.getDateOfBirth() != null) {
				// throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_NOT_NULL);
				// }
				// if (probandIn.getGender() != null) {
				// throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GENDER_NOT_NULL);
				// }
			}
			if (probandIn.getPrefixedTitle1() != null || probandIn.getPrefixedTitle2() != null || probandIn.getPrefixedTitle3() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_PREFIXED_TITLES_NOT_NULL);
			}
			if (probandIn.getFirstName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_FIRST_NAME_NOT_NULL);
			}
			if (probandIn.getLastName() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LAST_NAME_NOT_NULL);
			}
			if (probandIn.getPostpositionedTitle1() != null || probandIn.getPostpositionedTitle2() != null || probandIn.getPostpositionedTitle3() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_POSTPOSITIONED_TITLES_NOT_NULL);
			}
			if (probandIn.getCitizenship() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CITIZENSHIP_NOT_NULL);
			}
		}
		if (probandIn.getRatingMax() != null) {
			if (probandIn.getRatingMax() <= 0l) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_RATING_MAX_LESS_THAN_OR_EQUAL_ZERO);
			} else if (probandIn.getRating() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_RATING_REQUIRED);
			} else {
				if (probandIn.getRating() < 0l) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_RATING_LESS_THAN_ZERO);
				} else if (probandIn.getRating() > probandIn.getRatingMax()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_RATING_GREATER_THAN_RATING_MAX);
				}
			}
		} else if (probandIn.getRating() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_RATING_NOT_NULL);
		}
		// if (DateCalc.getStartOfDay(probandIn.getDateOfBirth()).compareTo(new Date()) > 0) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DATE_OF_BIRTH_IN_THE_FUTURE);
		// }
	}

	private void checkProbandLoop(Proband proband) throws ServiceException {
		(new ProbandReflexionGraph(this.getProbandDao())).checkGraphLoop(proband, false, true);
	}

	private void checkProbandStatusEntryInput(ProbandStatusEntryInVO statusEntryIn) throws ServiceException {
		ProbandDao probandDao = this.getProbandDao();
		// referential checks
		Proband proband = CheckIDUtil.checkProbandId(statusEntryIn.getProbandId(), probandDao);
		ProbandStatusType statusType = CheckIDUtil.checkProbandStatusTypeId(statusEntryIn.getTypeId(), this.getProbandStatusTypeDao());
		if (!probandDao.toProbandOutVO(proband).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(proband);
		if (proband.isPerson() && !statusType.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_STATUS_NOT_FOR_PERSON_ENTRIES,
					L10nUtil.getProbandStatusTypeName(Locales.USER, statusType.getNameL10nKey()));
		}
		if (!proband.isPerson() && !statusType.isAnimal()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_STATUS_NOT_FOR_ANIMAL_ENTRIES,
					L10nUtil.getProbandStatusTypeName(Locales.USER, statusType.getNameL10nKey()));
		}
		// other input checks
		if (statusEntryIn.getStop() != null && statusEntryIn.getStop().compareTo(statusEntryIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_STATUS_ENTRY_END_DATE_LESS_THAN_OR_EQUAL_TO_START_DATE);
		}
		if ((new ProbandStatusEntryCollisionFinder(probandDao, this.getProbandStatusEntryDao())).collides(statusEntryIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_STATUS_ENTRY_OVERLAPPING);
		}
	}

	private void checkProbandTagValueInput(ProbandTagValueInVO tagValueIn) throws ServiceException {
		(new ProbandTagAdapter(this.getProbandDao(), this.getProbandTagDao())).checkTagValueInput(tagValueIn);
	}

	private void checkProcedureInput(ProcedureInVO procedureIn) throws ServiceException {
		ProbandDao probandDao = this.getProbandDao();
		// referential checks
		Proband proband = CheckIDUtil.checkProbandId(procedureIn.getProbandId(), probandDao);
		OpsCode opsCode = CheckIDUtil.checkOpsCodeId(procedureIn.getCodeId(), this.getOpsCodeDao());
		if (!probandDao.toProbandOutVO(proband).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(proband);
		if (procedureIn.getStart() == null && procedureIn.getStop() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROCEDURE_START_DATE_REQUIRED);
		}
		// other input checks
		if (procedureIn.getStart() != null && procedureIn.getStop() != null && procedureIn.getStop().compareTo(procedureIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROCEDURE_END_DATE_LESS_THAN_OR_EQUAL_TO_START_DATE);
		}
		if ((new ProcedureCollisionFinder(probandDao, this.getProcedureDao())).collides(procedureIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROCEDURE_OVERLAPPING);
		}
	}

	private InquiryValuesOutVO getInquiryValues(Trial trial, String category, ProbandOutVO probandVO, Boolean active, Boolean activeSignup, boolean jsValues,
			boolean loadAllJsValues, boolean sort, PSFVO psf) throws Exception {
		InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
		InquiryValuesOutVO result = new InquiryValuesOutVO();
		Collection<Map> inquiryValues = inquiryValueDao.findByProbandTrialCategoryActiveJs(probandVO.getId(), trial.getId(), category, active, activeSignup, sort, null, psf);
		result.setPageValues(ServiceUtil.getInquiryValues(probandVO, inquiryValues, null, this.getInquiryDao(), inquiryValueDao)); // this.getInputFieldSelectionSetValueDao()
		if (jsValues) {
			if (loadAllJsValues) {
				result.setJsValues(ServiceUtil.getInquiryJsonValues(
						// inquiryValueDao.findByProbandTrialCategoryActiveJs(proband.getId(), trialId, category, active, sort, true, null),
						inquiryValueDao.findByProbandTrialActiveJs(probandVO.getId(), trial.getId(), active, activeSignup, sort, true, null),
						false, inquiryValueDao, this.getInputFieldSelectionSetValueDao()));
			} else {
				result.setJsValues(ServiceUtil.getInquiryJsonValues(inquiryValues,
						true, inquiryValueDao, this.getInputFieldSelectionSetValueDao()));
			}
		}
		return result;
	}

	@Override
	protected BankAccountOutVO handleAddBankAccount(
			AuthenticationVO auth, BankAccountInVO newBankAccount) throws Exception {
		checkBankAccountInput(newBankAccount);
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		BankAccount bankAccount = bankAccountDao.bankAccountInVOToEntity(newBankAccount);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(bankAccount, now, user);
		bankAccount = bankAccountDao.create(bankAccount);
		BankAccountOutVO result = bankAccountDao.toBankAccountOutVO(bankAccount);
		ServiceUtil.logSystemMessage(bankAccount.getProband(), result.getProband(), now, user, SystemMessageCodes.BANK_ACCOUNT_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected DiagnosisOutVO handleAddDiagnosis(AuthenticationVO auth,
			DiagnosisInVO newDiagnosis) throws Exception {
		checkDiagnosisInput(newDiagnosis);
		DiagnosisDao diagnosisDao = this.getDiagnosisDao();
		Diagnosis diagnosis = diagnosisDao.diagnosisInVOToEntity(newDiagnosis);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(diagnosis, now, user);
		diagnosis = diagnosisDao.create(diagnosis);
		DiagnosisOutVO result = diagnosisDao.toDiagnosisOutVO(diagnosis);
		ServiceUtil.logSystemMessage(diagnosis.getProband(), result.getProband(), now, user, SystemMessageCodes.DIAGNOSIS_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected MedicationOutVO handleAddMedication(AuthenticationVO auth, MedicationInVO newMedication) throws Exception {
		checkMedicationInput(newMedication);
		MedicationDao medicationDao = this.getMedicationDao();
		Medication medication = medicationDao.medicationInVOToEntity(newMedication);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		// moneyTransfer.setExportStatus(ExportStatus.NOT_EXPORTED);
		CoreUtil.modifyVersion(medication, now, user);
		medication = medicationDao.create(medication);
		MedicationOutVO result = medicationDao.toMedicationOutVO(medication);
		ServiceUtil.logSystemMessage(medication.getProband(), result.getProband(), now, user, SystemMessageCodes.MEDICATION_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected MoneyTransferOutVO handleAddMoneyTransfer(
			AuthenticationVO auth, MoneyTransferInVO newMoneyTransfer, Long maxAllowedCostTypes) throws Exception {
		checkMoneyTransferInput(newMoneyTransfer, maxAllowedCostTypes);
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		MoneyTransfer moneyTransfer = moneyTransferDao.moneyTransferInVOToEntity(newMoneyTransfer);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		moneyTransfer.setExportStatus(ExportStatus.NOT_EXPORTED);
		CoreUtil.modifyVersion(moneyTransfer, now, user);
		moneyTransfer = moneyTransferDao.create(moneyTransfer);
		Trial trial = moneyTransfer.getTrial();
		MoneyTransferOutVO result = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
		if (trial != null) {
			logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.MONEY_TRANSFER_CREATED, result, null, this.getJournalEntryDao());
		}
		ServiceUtil
				.logSystemMessage(moneyTransfer.getProband(), result.getProband(), now, user, SystemMessageCodes.MONEY_TRANSFER_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandOutVO handleAddProband(AuthenticationVO auth, ProbandInVO newProband, Integer maxInstances, Integer maxParentsDepth, Integer maxChildrenDepth)
			throws Exception {
		checkProbandInput(newProband);
		User user = CoreUtil.getUser();
		this.getUserDao().lock(user, LockMode.PESSIMISTIC_WRITE);
		if (!user.getDepartment().getId().equals(newProband.getDepartmentId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DEPARTMENT_NOT_EQUAL_TO_USER_DEPARTMENT);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Proband proband = ServiceUtil.createProband(newProband,
				now, user, this.getProbandDao(), this.getPrivacyConsentStatusTypeDao(), this.getProbandContactParticularsDao(), this.getAnimalContactParticularsDao(),
				this.getNotificationDao());
		ProbandOutVO result = this.getProbandDao().toProbandOutVO(proband, maxInstances, maxParentsDepth, maxChildrenDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(proband, result, now, user, SystemMessageCodes.PROBAND_CREATED, result, null, journalEntryDao);
		Staff physician = proband.getPhysician();
		if (physician != null) {
			ServiceUtil.logSystemMessage(physician, result, now, user, SystemMessageCodes.PROBAND_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected ProbandAddressOutVO handleAddProbandAddress(
			AuthenticationVO auth, ProbandAddressInVO newProbandAddress) throws Exception {
		checkProbandAddressInput(newProbandAddress);
		ProbandAddressDao addressDao = this.getProbandAddressDao();
		ProbandAddress address = addressDao.probandAddressInVOToEntity(newProbandAddress);
		// if (addressDao.findByProband(address.getProband().getId(), null, null, true, null).size() == 0) {
		if (addressDao.getCount(address.getProband().getId(), null, null, true) == 0) {
			address.setWireTransfer(true);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(address, now, user);
		address = addressDao.create(address);
		ProbandAddressOutVO result = addressDao.toProbandAddressOutVO(address);
		ServiceUtil.logSystemMessage(address.getProband(), result.getProband(), now, user, SystemMessageCodes.PROBAND_ADDRESS_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandContactDetailValueOutVO handleAddProbandContactDetailValue(
			AuthenticationVO auth, ProbandContactDetailValueInVO newProbandContactDetailValue)
			throws Exception {
		checkProbandContactDetailValueInput(newProbandContactDetailValue);
		ProbandContactDetailValueDao contactValueDao = this.getProbandContactDetailValueDao();
		ProbandContactDetailValue contactValue = contactValueDao.probandContactDetailValueInVOToEntity(newProbandContactDetailValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(contactValue, now, user);
		contactValue = contactValueDao.create(contactValue);
		ProbandContactDetailValueOutVO result = contactValueDao.toProbandContactDetailValueOutVO(contactValue);
		ServiceUtil.logSystemMessage(contactValue.getProband(), result.getProband(), now, user, SystemMessageCodes.PROBAND_CONTACT_DETAIL_VALUE_CREATED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandStatusEntryOutVO handleAddProbandStatusEntry(
			AuthenticationVO auth, ProbandStatusEntryInVO newProbandStatusEntry) throws Exception {
		checkProbandStatusEntryInput(newProbandStatusEntry);
		ProbandStatusEntryDao statusEntryDao = this.getProbandStatusEntryDao();
		ProbandStatusEntry statusEntry = statusEntryDao.probandStatusEntryInVOToEntity(newProbandStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(statusEntry, now, user);
		statusEntry = statusEntryDao.create(statusEntry);
		notifyProbandInactive(statusEntry, now);
		ProbandStatusEntryOutVO result = statusEntryDao.toProbandStatusEntryOutVO(statusEntry);
		ServiceUtil.logSystemMessage(statusEntry.getProband(), result.getProband(), now, user, SystemMessageCodes.PROBAND_STATUS_ENTRY_CREATED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandTagValueOutVO handleAddProbandTagValue(
			AuthenticationVO auth, ProbandTagValueInVO newProbandTagValue) throws Exception {
		checkProbandTagValueInput(newProbandTagValue);
		ProbandTagValueDao tagValueDao = this.getProbandTagValueDao();
		ProbandTagValue tagValue = tagValueDao.probandTagValueInVOToEntity(newProbandTagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(tagValue, now, user);
		tagValue = tagValueDao.create(tagValue);
		ProbandTagValueOutVO result = tagValueDao.toProbandTagValueOutVO(tagValue);
		ServiceUtil.logSystemMessage(tagValue.getProband(), result.getProband(), now, user, SystemMessageCodes.PROBAND_TAG_VALUE_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProcedureOutVO handleAddProcedure(AuthenticationVO auth,
			ProcedureInVO newProcedure) throws Exception {
		checkProcedureInput(newProcedure);
		ProcedureDao procedureDao = this.getProcedureDao();
		Procedure procedure = procedureDao.procedureInVOToEntity(newProcedure);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(procedure, now, user);
		procedure = procedureDao.create(procedure);
		ProcedureOutVO result = procedureDao.toProcedureOutVO(procedure);
		ServiceUtil.logSystemMessage(procedure.getProband(), result.getProband(), now, user, SystemMessageCodes.PROCEDURE_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<String> handleCompleteCostTypes(AuthenticationVO auth,
			Long trialDepartmentId, Long trialId, Long probandDepartmentId, Long probandId,
			String costTypePrefix, Integer limit) throws Exception {
		if (trialDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(trialDepartmentId, this.getDepartmentDao());
		}
		if (probandDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(probandDepartmentId, this.getDepartmentDao());
		}
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getMoneyTransferDao().findCostTypes(trialDepartmentId, trialId, probandDepartmentId, probandId, costTypePrefix, limit);
	}

	@Override
	protected BankAccountOutVO handleDeleteBankAccount(AuthenticationVO auth, Long bankAccountId)
			throws Exception {
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		BankAccount bankAccount = CheckIDUtil.checkBankAccountId(bankAccountId, bankAccountDao);
		Proband proband = bankAccount.getProband();
		BankAccountOutVO result = bankAccountDao.toBankAccountOutVO(bankAccount);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_BANK_ACCOUNT);
		}
		ServiceUtil.checkProbandLocked(proband);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		proband.removeBankAccounts(bankAccount);
		bankAccount.setProband(null);
		// if (deleteCascade) {
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Iterator<MoneyTransfer> moneyTransfersIt = bankAccount.getMoneyTransfers().iterator();
		while (moneyTransfersIt.hasNext()) {
			MoneyTransfer moneyTransfer = moneyTransfersIt.next();
			MoneyTransferOutVO moneyTransferVO = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
			Trial trial = moneyTransfer.getTrial();
			if (trial != null) {
				ServiceUtil.checkTrialLocked(trial);
				logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.BANK_ACCOUNT_DELETED_MONEY_TRANSFER_DELETED, moneyTransferVO, null, journalEntryDao);
				trial.removePayoffs(moneyTransfer);
			}
			moneyTransfer.setBankAccount(null);
			moneyTransferDao.remove(moneyTransfer);
			moneyTransfer.setProband(null);
			proband.removeMoneyTransfers(moneyTransfer);
			ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.BANK_ACCOUNT_DELETED_MONEY_TRANSFER_DELETED, moneyTransferVO, null,
					journalEntryDao);
		}
		bankAccount.getMoneyTransfers().clear();
		// }
		bankAccountDao.remove(bankAccount);
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.BANK_ACCOUNT_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected DiagnosisOutVO handleDeleteDiagnosis(AuthenticationVO auth,
			Long diagnosisId) throws Exception {
		DiagnosisDao diagnosisDao = this.getDiagnosisDao();
		Diagnosis diagnosis = CheckIDUtil.checkDiagnosisId(diagnosisId, diagnosisDao);
		Proband proband = diagnosis.getProband();
		DiagnosisOutVO result = diagnosisDao.toDiagnosisOutVO(diagnosis);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_DIAGNOSIS);
		}
		ServiceUtil.checkProbandLocked(proband);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		AlphaId alphaId = diagnosis.getCode();
		alphaId.removeDiagnoses(diagnosis);
		diagnosis.setCode(null);
		proband.removeDiagnoses(diagnosis);
		diagnosis.setProband(null);
		// if (deleteCascade) {
		MedicationDao medicationDao = this.getMedicationDao();
		Iterator<Medication> medicationsIt = diagnosis.getMedications().iterator();
		while (medicationsIt.hasNext()) {
			Medication medication = medicationsIt.next();
			MedicationOutVO originalMedicationVO = medicationDao.toMedicationOutVO(medication);
			medication.setDiagnosis(null);
			CoreUtil.modifyVersion(medication, medication.getVersion(), now, user);
			medicationDao.update(medication);
			MedicationOutVO medicationVO = medicationDao.toMedicationOutVO(medication);
			ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.DIAGNOSIS_DELETED_MEDICATION_UPDATED, medicationVO, originalMedicationVO,
					journalEntryDao);
		}
		diagnosis.getMedications().clear();
		// }
		diagnosisDao.remove(diagnosis);
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.DIAGNOSIS_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected MedicationOutVO handleDeleteMedication(AuthenticationVO auth, Long medicationId) throws Exception {
		MedicationDao medicationDao = this.getMedicationDao();
		Medication medication = CheckIDUtil.checkMedicationId(medicationId, medicationDao);
		Proband proband = medication.getProband();
		MedicationOutVO result = medicationDao.toMedicationOutVO(medication);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_MEDICATION);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		Diagnosis diagnosis = medication.getDiagnosis();
		Procedure procedure = medication.getProcedure();
		proband.removeMedications(medication);
		medication.setProband(null);
		if (diagnosis != null) {
			diagnosis.removeMedications(medication);
			medication.setDiagnosis(null);
		}
		if (procedure != null) {
			procedure.removeMedications(medication);
			medication.setProcedure(null);
		}
		medicationDao.remove(medication);
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.MEDICATION_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected MoneyTransferOutVO handleDeleteMoneyTransfer(AuthenticationVO auth, Long moneyTransferId)
			throws Exception {
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		MoneyTransfer moneyTransfer = CheckIDUtil.checkMoneyTransferId(moneyTransferId, moneyTransferDao);
		Proband proband = moneyTransfer.getProband();
		MoneyTransferOutVO result = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_MONEY_TRANSFER);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		BankAccount bankAccount = moneyTransfer.getBankAccount();
		Trial trial = moneyTransfer.getTrial();
		if (trial != null) {
			ServiceUtil.checkTrialLocked(trial);
			trial.removePayoffs(moneyTransfer);
			moneyTransfer.setTrial(null);
		}
		proband.removeMoneyTransfers(moneyTransfer);
		moneyTransfer.setProband(null);
		if (bankAccount != null) {
			bankAccount.removeMoneyTransfers(moneyTransfer);
			moneyTransfer.setBankAccount(null);
		}
		moneyTransferDao.remove(moneyTransfer);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (trial != null) {
			logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.MONEY_TRANSFER_DELETED, result, null, journalEntryDao);
		}
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.MONEY_TRANSFER_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected ProbandOutVO handleDeleteProband(AuthenticationVO auth, Long probandId,
			boolean defer, boolean force, String deferredDeleteReason, Integer maxInstances, Integer maxParentsDepth, Integer maxChildrenDepth) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ProbandOutVO result;
		if (!force && defer) {
			Proband originalProband = CheckIDUtil.checkProbandId(probandId, probandDao);
			ProbandOutVO original = probandDao.toProbandOutVO(originalProband, maxInstances, maxParentsDepth, maxChildrenDepth);
			if (original.getBlinded()) {
				if (!user.getDepartment().getId().equals(originalProband.getDepartment().getId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DEPARTMENT_NOT_EQUAL_TO_USER_DEPARTMENT);
				}
			} else {
				if (!original.isDecrypted()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
				}
			}
			probandDao.evict(originalProband);
			Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao, LockMode.PESSIMISTIC_WRITE);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			proband.setDeferredDelete(true);
			proband.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(proband, proband.getVersion(), now, user); // no opt. locking
			probandDao.update(proband);
			result = probandDao.toProbandOutVO(proband, maxInstances, maxParentsDepth, maxChildrenDepth);
			ServiceUtil.logSystemMessage(proband, result, now, user, SystemMessageCodes.PROBAND_MARKED_FOR_DELETION, result, original, journalEntryDao);
			// Iterator<ProbandOutVO> childrenIt = original.getChildren().iterator();
			// while (childrenIt.hasNext()) {
			// ProbandOutVO child = childrenIt.next();
			// ServiceUtil.logSystemMessage(probandDao.load(child.getId()), result, now, user, SystemMessageCodes.PROBAND_MARKED_FOR_DELETION, result, original, journalEntryDao);
			// }
			Iterator<ProbandOutVO> parentsIt = original.getParents().iterator();
			while (parentsIt.hasNext()) {
				ProbandOutVO parent = parentsIt.next();
				ServiceUtil.logSystemMessage(probandDao.load(parent.getId()), result, now, user, SystemMessageCodes.PROBAND_MARKED_FOR_DELETION, result, original, journalEntryDao);
			}
		} else {
			Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao, LockMode.PESSIMISTIC_WRITE);
			result = probandDao.toProbandOutVO(proband, maxInstances, maxParentsDepth, maxChildrenDepth);
			if (result.getBlinded()) {
				if (!user.getDepartment().getId().equals(result.getDepartment().getId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DEPARTMENT_NOT_EQUAL_TO_USER_DEPARTMENT);
				}
			} else {
				if (!result.isDecrypted()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
				}
			}
			ServiceUtil.removeProband(proband, result, true,
					user,
					now,
					this.getProbandDao(),
					this.getProbandContactParticularsDao(),
					this.getAnimalContactParticularsDao(),
					journalEntryDao,
					this.getNotificationDao(),
					this.getNotificationRecipientDao(),
					this.getProbandTagValueDao(),
					this.getProbandContactDetailValueDao(),
					this.getProbandAddressDao(),
					this.getProbandStatusEntryDao(),
					this.getDiagnosisDao(),
					this.getProcedureDao(),
					this.getMedicationDao(),
					this.getInventoryBookingDao(),
					this.getMoneyTransferDao(),
					this.getBankAccountDao(),
					this.getProbandListStatusEntryDao(),
					this.getProbandListEntryDao(),
					this.getProbandListEntryTagValueDao(),
					this.getInputFieldValueDao(),
					this.getInquiryValueDao(),
					this.getECRFFieldValueDao(),
					this.getECRFFieldStatusEntryDao(),
					this.getSignatureDao(),
					this.getECRFStatusEntryDao(),
					this.getMassMailRecipientDao(),
					this.getFileDao());
		}
		return result;
	}

	@Override
	protected ProbandAddressOutVO handleDeleteProbandAddress(
			AuthenticationVO auth, Long probandAddressId) throws Exception {
		ProbandAddressDao addressDao = this.getProbandAddressDao();
		ProbandAddress address = CheckIDUtil.checkProbandAddressId(probandAddressId, addressDao);
		Proband proband = address.getProband();
		this.getProbandDao().lock(proband, LockMode.PESSIMISTIC_WRITE);
		// Proband proband = ServiceUtil.checkProbandId(address.getProband().getId(), this.getProbandDao(), LockMode.PESSIMISTIC_WRITE); // address.getProband();
		ProbandAddressOutVO result = addressDao.toProbandAddressOutVO(address);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_ADDRESS);
		}
		ServiceUtil.checkProbandLocked(proband);
		if (address.isWireTransfer() && addressDao.getCount(address.getProband().getId(), null, null, null) > 1) { // proband.getAddresses().Xsize() > 1) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_WIRE_TRANSFER_PROBAND_ADDRESS);
		}
		proband.removeAddresses(address);
		address.setProband(null);
		addressDao.remove(address);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.PROBAND_ADDRESS_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandContactDetailValueOutVO handleDeleteProbandContactDetailValue(
			AuthenticationVO auth, Long probandContactDetailValueId) throws Exception {
		ProbandContactDetailValueDao contactValueDao = this.getProbandContactDetailValueDao();
		ProbandContactDetailValue contactValue = CheckIDUtil.checkProbandContactDetailValueId(probandContactDetailValueId, contactValueDao);
		Proband proband = contactValue.getProband();
		ProbandContactDetailValueOutVO result = contactValueDao.toProbandContactDetailValueOutVO(contactValue);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_CONTACT_DETAIL_VALUE);
		}
		ServiceUtil.checkProbandLocked(proband);
		proband.removeContactDetailValues(contactValue);
		contactValue.setProband(null);
		contactValueDao.remove(contactValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.PROBAND_CONTACT_DETAIL_VALUE_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandStatusEntryOutVO handleDeleteProbandStatusEntry(
			AuthenticationVO auth, Long probandStatusEntryId) throws Exception {
		ProbandStatusEntryDao statusEntryDao = this.getProbandStatusEntryDao();
		ProbandStatusEntry statusEntry = CheckIDUtil.checkProbandStatusEntryId(probandStatusEntryId, statusEntryDao);
		Proband proband = statusEntry.getProband();
		ProbandStatusEntryOutVO result = statusEntryDao.toProbandStatusEntryOutVO(statusEntry);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_STATUS_ENTRY);
		}
		ServiceUtil.checkProbandLocked(proband);
		proband.removeStatusEntries(statusEntry);
		statusEntry.setProband(null);
		ServiceUtil.removeNotifications(statusEntry.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		statusEntryDao.remove(statusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.PROBAND_STATUS_ENTRY_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandTagValueOutVO handleDeleteProbandTagValue(AuthenticationVO auth, Long probandTagValueId)
			throws Exception {
		ProbandTagValueDao tagValueDao = this.getProbandTagValueDao();
		ProbandTagValue tagValue = CheckIDUtil.checkProbandTagValueId(probandTagValueId, tagValueDao);
		Proband proband = tagValue.getProband();
		ProbandTagValueOutVO result = tagValueDao.toProbandTagValueOutVO(tagValue);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_TAG_VALUE);
		}
		ServiceUtil.checkProbandLocked(proband);
		proband.removeTagValues(tagValue);
		tagValue.setProband(null);
		tagValueDao.remove(tagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.PROBAND_TAG_VALUE_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProcedureOutVO handleDeleteProcedure(AuthenticationVO auth,
			Long procedureId) throws Exception {
		ProcedureDao procedureDao = this.getProcedureDao();
		Procedure procedure = CheckIDUtil.checkProcedureId(procedureId, procedureDao);
		Proband proband = procedure.getProband();
		ProcedureOutVO result = procedureDao.toProcedureOutVO(procedure);
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROCEDURE);
		}
		ServiceUtil.checkProbandLocked(proband);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		OpsCode opsCode = procedure.getCode();
		opsCode.removeProcedures(procedure);
		procedure.setCode(null);
		proband.removeProcedures(procedure);
		procedure.setProband(null);
		// if (deleteCascade) {
		MedicationDao medicationDao = this.getMedicationDao();
		Iterator<Medication> medicationsIt = procedure.getMedications().iterator();
		while (medicationsIt.hasNext()) {
			Medication medication = medicationsIt.next();
			MedicationOutVO originalMedicationVO = medicationDao.toMedicationOutVO(medication);
			medication.setProcedure(null);
			CoreUtil.modifyVersion(medication, medication.getVersion(), now, user);
			medicationDao.update(medication);
			MedicationOutVO medicationVO = medicationDao.toMedicationOutVO(medication);
			ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.PROCEDURE_DELETED_MEDICATION_UPDATED, medicationVO, originalMedicationVO,
					journalEntryDao);
		}
		procedure.getMedications().clear();
		// }
		procedureDao.remove(procedure);
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.PROCEDURE_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected ReimbursementsExcelVO handleExportReimbursements(
			AuthenticationVO auth, Long probandId, String costType,
			PaymentMethod method, Boolean paid) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		if (!proband.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PROBAND_NOT_PERSON);
		}
		ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes = moneyTransferDao.getCostTypes(null, null, null, probandVO.getId(), method);
		Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, null, null, probandVO.getId(), method, costType, paid, null,
				null);
		ReimbursementsExcelVO result = ServiceUtil.createReimbursementsExcel(moneyTransfers, costTypes, null, probandVO, costType, method, paid,
				moneyTransferDao,
				this.getBankAccountDao(),
				this.getProbandAddressDao(),
				this.getAddressTypeDao(),
				this.getUserDao());
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(proband, result.getProband(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
				SystemMessageCodes.REIMBURSEMENTS_EXPORTED, result, null, this.getJournalEntryDao());
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected VisitScheduleExcelVO handleExportVisitSchedule(
			AuthenticationVO auth, Long probandId, Long trialId) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
		TrialDao trialDao = this.getTrialDao();
		TrialOutVO trialVO = null;
		if (trialId != null) {
			trialVO = trialDao.toTrialOutVO(CheckIDUtil.checkTrialId(trialId, trialDao));
		}
		VisitScheduleExcelWriter.Styles style = trialVO == null ? VisitScheduleExcelWriter.Styles.PROBAND_VISIT_SCHEDULE
				: VisitScheduleExcelWriter.Styles.PROBAND_TRIAL_VISIT_SCHEDULE;
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		Collection<VisitScheduleItem> visitScheduleItems;
		switch (style) {
			case PROBAND_VISIT_SCHEDULE:
				visitScheduleItems = visitScheduleItemDao.findByTrialGroupVisitProbandTravel(null, null, null, probandVO.getId(), null, null);
				break;
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				visitScheduleItems = visitScheduleItemDao.findByTrialGroupVisitProbandTravel(trialVO.getId(), null, null, probandVO.getId(), null, null);
				break;
			default:
				visitScheduleItems = null;
		}
		VisitScheduleExcelVO result = ServiceUtil.creatVisitScheduleExcel(visitScheduleItems, style, probandVO, trialVO,
				visitScheduleItemDao,
				this.getProbandListStatusEntryDao(),
				this.getProbandAddressDao(),
				this.getUserDao());
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		switch (style) {
			case PROBAND_VISIT_SCHEDULE:
				ServiceUtil.logSystemMessage(proband, result.getProband(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.VISIT_SCHEDULE_EXPORTED, result, null, this.getJournalEntryDao());
				break;
			case PROBAND_TRIAL_VISIT_SCHEDULE:
				ServiceUtil.logSystemMessage(proband, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.VISIT_SCHEDULE_EXPORTED,
						result, null, this.getJournalEntryDao());
				break;
			default:
		}
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected Collection<ProbandOutVO> handleGetAutoDeletionProbands(
			AuthenticationVO auth, Date today, Long departmentId, Long probandCategoryId, VariablePeriod reminderPeriod,
			Long reminderPeriodDays, PSFVO psf) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (probandCategoryId != null) {
			CheckIDUtil.checkProbandCategoryId(probandCategoryId, this.getProbandCategoryDao());
		}
		ServiceUtil.checkReminderPeriod(reminderPeriod, reminderPeriodDays);
		ProbandDao probandDao = this.getProbandDao();
		Collection autoDeletionProbands = probandDao.findToBeAutoDeleted(today, departmentId, probandCategoryId, reminderPeriod, reminderPeriodDays, null, true, psf);
		probandDao.toProbandOutVOCollection(autoDeletionProbands);
		return autoDeletionProbands;
	}

	@Override
	protected BankAccountOutVO handleGetBankAccount(AuthenticationVO auth, Long bankAccountId)
			throws Exception {
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		BankAccount bankAccount = CheckIDUtil.checkBankAccountId(bankAccountId, bankAccountDao);
		BankAccountOutVO result = bankAccountDao.toBankAccountOutVO(bankAccount);
		return result;
	}

	@Override
	protected long handleGetBankAccountCount(AuthenticationVO auth, Long probandId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getBankAccountDao().getCount(probandId);
	}

	@Override
	protected Collection<BankAccountOutVO> handleGetBankAccountList(
			AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		Collection bankAccounts = bankAccountDao.findByProband(probandId, null, null, psf);
		bankAccountDao.toBankAccountOutVOCollection(bankAccounts);
		return bankAccounts;
	}

	@Override
	protected Collection<BankAccountOutVO> handleGetBankAccounts(
			AuthenticationVO auth, Long probandId, Boolean active, Long bankAccountId)
			throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		if (bankAccountId != null) {
			CheckIDUtil.checkBankAccountId(bankAccountId, bankAccountDao);
		}
		Collection bankAccounts = bankAccountDao.findByProbandActiveId(probandId, active, bankAccountId);
		bankAccountDao.toBankAccountOutVOCollection(bankAccounts);
		return bankAccounts;
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetCollidingProbandInventoryBookings(
			AuthenticationVO auth, Long probandStatusEntryId, Boolean isRelevantForProbandAppointments) throws Exception {
		ProbandStatusEntry probandStatus = CheckIDUtil.checkProbandStatusEntryId(probandStatusEntryId, this.getProbandStatusEntryDao());
		Collection collidingInventoryBookings;
		if (!probandStatus.getType().isProbandActive()) {
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			collidingInventoryBookings = inventoryBookingDao.findByProbandCalendarInterval(probandStatus.getProband().getId(), null, probandStatus.getStart(),
					probandStatus.getStop(), isRelevantForProbandAppointments);
			inventoryBookingDao.toInventoryBookingOutVOCollection(collidingInventoryBookings);
		} else {
			collidingInventoryBookings = new ArrayList<InventoryBookingOutVO>();
		}
		return collidingInventoryBookings;
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> handleGetCollidingVisitScheduleItems(
			AuthenticationVO auth, Long probandStatusEntryId, boolean allProbandGroups) throws Exception {
		ProbandStatusEntry probandStatusEntry = CheckIDUtil.checkProbandStatusEntryId(probandStatusEntryId, this.getProbandStatusEntryDao());
		if (!probandStatusEntry.getType().isProbandActive()) {
			Collection collidingVisitScheduleItems = new HashSet(); // new ArrayList();
			VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
			Iterator<ProbandListEntry> trialParticipationsIt = probandStatusEntry.getProband().getTrialParticipations().iterator();
			while (trialParticipationsIt.hasNext()) {
				ProbandListEntry probandListEntry = trialParticipationsIt.next();
				ProbandGroup probandGroup = probandListEntry.getGroup();
				if (probandGroup != null) {
					collidingVisitScheduleItems
							.addAll(visitScheduleItemDao.findByInterval(probandListEntry.getTrial().getId(), probandGroup.getId(), probandStatusEntry.getStart(),
									probandStatusEntry.getStop()));
				} else {
					if (allProbandGroups) {
						collidingVisitScheduleItems.addAll(visitScheduleItemDao.findByInterval(probandListEntry.getTrial().getId(), null, probandStatusEntry.getStart(),
								probandStatusEntry.getStop()));
					}
				}
			}
			visitScheduleItemDao.toVisitScheduleItemOutVOCollection(collidingVisitScheduleItems);
			return new ArrayList<VisitScheduleItemOutVO>(collidingVisitScheduleItems);
		} else {
			return new ArrayList<VisitScheduleItemOutVO>();
		}
	}

	@Override
	protected Collection<String> handleGetCostTypes(AuthenticationVO auth,
			Long trialDepartmentId, Long trialId, Long probandDepartmentId, Long probandId,
			PaymentMethod method) throws Exception {
		if (trialDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(trialDepartmentId, this.getDepartmentDao());
		}
		if (probandDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(probandDepartmentId, this.getDepartmentDao());
		}
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getMoneyTransferDao().getCostTypes(trialDepartmentId, trialId, probandDepartmentId, probandId, method);
	}

	@Override
	protected DiagnosisOutVO handleGetDiagnosis(AuthenticationVO auth,
			Long diagnosisId) throws Exception {
		DiagnosisDao diagnosisDao = this.getDiagnosisDao();
		Diagnosis diagnosis = CheckIDUtil.checkDiagnosisId(diagnosisId, diagnosisDao);
		DiagnosisOutVO result = diagnosisDao.toDiagnosisOutVO(diagnosis);
		return result;
	}

	@Override
	protected long handleGetDiagnosisCount(
			AuthenticationVO auth, Long probandId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getDiagnosisDao().getCount(probandId);
	}

	@Override
	protected Collection<DiagnosisOutVO> handleGetDiagnosisList(
			AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		DiagnosisDao diagnosisDao = this.getDiagnosisDao();
		Collection diagnoses = diagnosisDao.findByProband(probandId, psf);
		diagnosisDao.toDiagnosisOutVOCollection(diagnoses);
		return diagnoses;
	}

	@Override
	protected long handleGetInquiryCount(AuthenticationVO auth, Long trialId, Boolean active, Boolean activeSignup) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getInquiryDao().getCount(trialId, active, activeSignup);
	}

	@Override
	protected long handleGetInquiryCount(AuthenticationVO auth, Long trialId,
			String category, Boolean active, Boolean activeSignup) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getInquiryDao().getCount(trialId, category, active, activeSignup);
	}

	@Override
	protected Collection<InquiryValueOutVO> handleGetInquiryInputFieldValues(
			AuthenticationVO auth, Long trialId, Boolean active, Boolean activeSignup,
			Long probandId, Long inputFieldId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		CheckIDUtil.checkInputFieldId(inputFieldId, this.getInputFieldDao());
		InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
		Collection inquiryFieldValues = inquiryValueDao.findByTrialActiveProbandField(trialId, active, activeSignup, probandId, inputFieldId);
		inquiryValueDao.toInquiryValueOutVOCollection(inquiryFieldValues);
		return inquiryFieldValues;
	}

	@Override
	protected Collection<TrialOutVO> handleGetInquiryTrials(AuthenticationVO auth, Long probandId, Boolean active, Boolean activeSignup) throws Exception {
		CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		TrialDao trialDao = this.getTrialDao();
		Collection trials = trialDao.findByInquiryValuesProbandSorted(null, probandId, active, activeSignup);
		trialDao.toTrialOutVOCollection(trials);
		return trials;
	}

	@Override
	protected InquiryValuesOutVO handleGetInquiryValue(AuthenticationVO auth, Long probandId,
			Long inquiryId) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		InquiryDao inquiryDao = this.getInquiryDao();
		Inquiry inquiry = CheckIDUtil.checkInquiryId(inquiryId, inquiryDao);
		InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
		InquiryValuesOutVO result = new InquiryValuesOutVO();
		Iterator<InquiryValue> it = inquiryValueDao.findByProbandInquiry(probandId, inquiryId).iterator();
		if (it.hasNext()) {
			InquiryValue inquiryValue = it.next();
			result.getPageValues().add(inquiryValueDao.toInquiryValueOutVO(inquiryValue));
			if (!CommonUtil.isEmptyString(inquiryValue.getInquiry().getJsVariableName())
					&& Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues().add(inquiryValueDao.toInquiryValueJsonVO(inquiryValue));
			}
		} else {
			result.getPageValues().add(
					ServiceUtil.createPresetInquiryOutValue(probandDao.toProbandOutVO(proband), inquiryDao.toInquiryOutVO(inquiry), null));
			if (!CommonUtil.isEmptyString(inquiry.getJsVariableName()) && Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
					DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues().add(ServiceUtil.createPresetInquiryJsonValue(inquiry, this.getInputFieldSelectionSetValueDao()));
			}
		}
		return result;
	}

	@Override
	protected InquiryValueOutVO handleGetInquiryValueById(AuthenticationVO auth, Long inquiryValueId) throws Exception {
		InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
		return inquiryValueDao.toInquiryValueOutVO(CheckIDUtil.checkInquiryValueId(inquiryValueId, inquiryValueDao));
	}

	@Override
	protected long handleGetInquiryValueCount(AuthenticationVO auth, Long trialId, Boolean active, Boolean activeSignup,
			Long probandId) throws Exception {
		Trial trial = CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		return this.getInquiryValueDao().getCount(trialId, active, activeSignup, probandId);
	}

	@Override
	protected long handleGetInquiryValueCount(AuthenticationVO auth, Long trialId, String category,
			Boolean active, Boolean activeSignup, Long probandId) throws Exception {
		Trial trial = CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		return this.getInquiryValueDao().getCount(trialId, category, active, activeSignup, probandId);
	}

	@Override
	protected InquiryValuesOutVO handleGetInquiryValues(
			AuthenticationVO auth, Long trialId, Boolean active, Boolean activeSignup, Long probandId,
			boolean sort, boolean loadAllJsValues, PSFVO psf) throws Exception {
		Trial trial = CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		ProbandDao probandDao = this.getProbandDao();
		ProbandOutVO probandVO = probandDao.toProbandOutVO(CheckIDUtil.checkProbandId(probandId, probandDao));
		return ServiceUtil.getInquiryValues(trial, probandVO, active, activeSignup,
				Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
						DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION),
				loadAllJsValues, sort, psf, this.getInquiryDao(), this.getInquiryValueDao(), this.getInputFieldSelectionSetValueDao());
	}

	@Override
	protected InquiryValuesOutVO handleGetInquiryValues(
			AuthenticationVO auth, Long trialId, String category, Boolean active, Boolean activeSignup, Long probandId,
			boolean sort, boolean loadAllJsValues, PSFVO psf) throws Exception {
		Trial trial = CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		ProbandDao probandDao = this.getProbandDao();
		ProbandOutVO probandVO = probandDao.toProbandOutVO(CheckIDUtil.checkProbandId(probandId, probandDao));
		return getInquiryValues(trial, category, probandVO, active, activeSignup, Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION,
				Bundle.SETTINGS, DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION), loadAllJsValues, sort, psf);
	}

	@Override
	protected MedicationOutVO handleGetMedication(AuthenticationVO auth, Long medicationId) throws Exception {
		MedicationDao medicationDao = this.getMedicationDao();
		Medication medication = CheckIDUtil.checkMedicationId(medicationId, medicationDao);
		MedicationOutVO result = medicationDao.toMedicationOutVO(medication);
		return result;
	}

	@Override
	protected long handleGetMedicationCount(AuthenticationVO auth, Long probandId, Long diagnosisId, Long procedureId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		if (diagnosisId != null) {
			CheckIDUtil.checkDiagnosisId(diagnosisId, this.getDiagnosisDao());
		}
		if (procedureId != null) {
			CheckIDUtil.checkProcedureId(procedureId, this.getProcedureDao());
		}
		return this.getMedicationDao().getCount(probandId, diagnosisId, procedureId);
	}

	@Override
	protected Collection<MedicationOutVO> handleGetMedicationList(AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		MedicationDao medicationDao = this.getMedicationDao();
		Collection medications = medicationDao.findByProband(probandId, psf);
		medicationDao.toMedicationOutVOCollection(medications);
		return medications;
	}

	@Override
	protected MoneyTransferOutVO handleGetMoneyTransfer(AuthenticationVO auth, Long moneyTransferId)
			throws Exception {
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		MoneyTransfer moneyTransfer = CheckIDUtil.checkMoneyTransferId(moneyTransferId, moneyTransferDao);
		MoneyTransferOutVO result = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
		return result;
	}

	@Override
	protected long handleGetMoneyTransferCount(AuthenticationVO auth, Long probandId, Long bankAccountId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		if (bankAccountId != null) {
			CheckIDUtil.checkBankAccountId(bankAccountId, this.getBankAccountDao());
		}
		return this.getMoneyTransferDao().getCount(null, probandId, bankAccountId, null, null, null);
	}

	@Override
	protected Collection<MoneyTransferOutVO> handleGetMoneyTransferList(
			AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, null, null, probandId, null, null, null, null, psf);
		moneyTransferDao.toMoneyTransferOutVOCollection(moneyTransfers);
		return moneyTransfers;
	}

	@Override
	protected String handleGetNewPaymentReference(AuthenticationVO auth,
			MoneyTransferInVO newMoneyTransfer)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection<TrialOutVO> handleGetParticipationTrials(AuthenticationVO auth, Long probandId)
			throws Exception {
		CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		TrialDao trialDao = this.getTrialDao();
		Collection trials = trialDao.findByParticipatingProbandSorted(probandId);
		trialDao.toTrialOutVOCollection(trials);
		return trials;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.proband.ProbandService#getProband(Long)
	 */
	@Override
	protected ProbandOutVO handleGetProband(AuthenticationVO auth, Long probandId, Integer maxInstances, Integer maxParentsDepth, Integer maxChildrenDepth) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO result = probandDao.toProbandOutVO(proband, maxInstances, maxParentsDepth, maxChildrenDepth);
		return result;
	}

	@Override
	protected ProbandAddressOutVO handleGetProbandAddress(AuthenticationVO auth, Long probandAddressId)
			throws Exception {
		ProbandAddressDao addressDao = this.getProbandAddressDao();
		ProbandAddress address = CheckIDUtil.checkProbandAddressId(probandAddressId, addressDao);
		ProbandAddressOutVO result = addressDao.toProbandAddressOutVO(address);
		return result;
	}

	@Override
	protected long handleGetProbandAddressCount(
			AuthenticationVO auth, Long probandId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getProbandAddressDao().getCount(probandId, null, null, null);
	}

	@Override
	protected Collection<ProbandAddressOutVO> handleGetProbandAddressList(
			AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandAddressDao addressDao = this.getProbandAddressDao();
		Collection probandAddresses = addressDao.findByProband(probandId, null, null, null, psf);
		addressDao.toProbandAddressOutVOCollection(probandAddresses);
		return probandAddresses;
	}

	@Override
	protected ProbandContactDetailValueOutVO handleGetProbandContactDetailValue(
			AuthenticationVO auth, Long probandContactDetailValueId) throws Exception {
		ProbandContactDetailValueDao contactValueDao = this.getProbandContactDetailValueDao();
		ProbandContactDetailValue contactValue = CheckIDUtil.checkProbandContactDetailValueId(probandContactDetailValueId, contactValueDao);
		ProbandContactDetailValueOutVO result = contactValueDao.toProbandContactDetailValueOutVO(contactValue);
		return result;
	}

	@Override
	protected long handleGetProbandContactDetailValueCount(
			AuthenticationVO auth, Long probandId, Boolean na) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getProbandContactDetailValueDao().getCount(probandId, null, na, null, null);
	}

	@Override
	protected Collection<ProbandContactDetailValueOutVO> handleGetProbandContactDetailValueList(
			AuthenticationVO auth, Long probandId, Boolean na, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandContactDetailValueDao contactValueDao = this.getProbandContactDetailValueDao();
		Collection probandContactValues = contactValueDao.findByProband(probandId, null, na, null, null, psf);
		contactValueDao.toProbandContactDetailValueOutVOCollection(probandContactValues);
		return probandContactValues;
	}

	@Override
	protected Collection<ProbandGroupOutVO> handleGetProbandGroupList(
			AuthenticationVO auth, Long probandId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
		Collection probandGroups = probandGroupDao.findByProbandSorted(probandId);
		probandGroupDao.toProbandGroupOutVOCollection(probandGroups);
		return probandGroups;
	}

	@Override
	protected ProbandImageOutVO handleGetProbandImage(AuthenticationVO auth,
			Long probandId) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandImageOutVO result = probandDao.toProbandImageOutVO(proband);
		return result;
	}

	@Override
	protected long handleGetProbandInventoryBookingCount(
			AuthenticationVO auth, Long probandId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getInventoryBookingDao().getCount(null, probandId, null, null, null);
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetProbandInventoryBookingList(
			AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection inventoryBookings = inventoryBookingDao.findByProband(probandId, psf);
		inventoryBookingDao.toInventoryBookingOutVOCollection(inventoryBookings);
		return inventoryBookings;
	}

	@Override
	protected Collection<ProbandOutVO> handleGetProbandList(AuthenticationVO auth, Long probandId, Long departmentId, Integer maxInstances, PSFVO psf) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, probandDao);
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Collection probands = probandDao.findByIdDepartment(probandId, departmentId, psf);
		// probandDao.toProbandOutVOCollection(probands);
		ArrayList<ProbandOutVO> result = new ArrayList<ProbandOutVO>(probands.size());
		Iterator<Proband> probandIt = probands.iterator();
		while (probandIt.hasNext()) {
			result.add(probandDao.toProbandOutVO(probandIt.next(), maxInstances));
		}
		return result;
	}

	@Override
	protected Collection<ProbandStatusEntryOutVO> handleGetProbandStatus(
			AuthenticationVO auth, Date now, Long probandId, Long departmentId, Long probandCategoryId, Boolean probandActive, Boolean hideAvailability, PSFVO psf)
			throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (probandCategoryId != null) {
			CheckIDUtil.checkProbandCategoryId(probandCategoryId, this.getProbandCategoryDao());
		}
		ProbandStatusEntryDao statusEntryDao = this.getProbandStatusEntryDao();
		Collection probandStatusEntries = statusEntryDao.findProbandStatus(CommonUtil.dateToTimestamp(now), probandId, departmentId, probandCategoryId, probandActive,
				hideAvailability, psf);
		statusEntryDao.toProbandStatusEntryOutVOCollection(probandStatusEntries);
		return probandStatusEntries;
	}

	@Override
	protected ProbandStatusEntryOutVO handleGetProbandStatusEntry(
			AuthenticationVO auth, Long probandStatusEntryId) throws Exception {
		ProbandStatusEntryDao statusEntryDao = this.getProbandStatusEntryDao();
		ProbandStatusEntry statusEntry = CheckIDUtil.checkProbandStatusEntryId(probandStatusEntryId, statusEntryDao);
		ProbandStatusEntryOutVO result = statusEntryDao.toProbandStatusEntryOutVO(statusEntry);
		return result;
	}

	@Override
	protected long handleGetProbandStatusEntryCount(AuthenticationVO auth, Long probandId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getProbandStatusEntryDao().getCount(probandId);
	}

	@Override
	protected Collection<ProbandStatusEntryOutVO> handleGetProbandStatusEntryInterval(AuthenticationVO auth, Long departmentId, Long probandCategoryId, Boolean hideAvailability,
			Date from, Date to, boolean sort) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (probandCategoryId != null) {
			CheckIDUtil.checkProbandCategoryId(probandCategoryId, this.getProbandCategoryDao());
		}
		ProbandStatusEntryDao statusEntryDao = this.getProbandStatusEntryDao();
		Collection probandStatusEntries = statusEntryDao.findByDepartmentCategoryInterval(departmentId, probandCategoryId, CommonUtil.dateToTimestamp(from),
				CommonUtil.dateToTimestamp(to), null, null, hideAvailability); // false,true,hideAvailability);
		statusEntryDao.toProbandStatusEntryOutVOCollection(probandStatusEntries);
		if (sort) {
			probandStatusEntries = new ArrayList<ProbandStatusEntryOutVO>(probandStatusEntries);
			Collections.sort((ArrayList<ProbandStatusEntryOutVO>) probandStatusEntries, ComparatorFactory.PROBAND_STATUS_ENTRY_OUT_VO_INTERVAL_COMP);
		}
		return probandStatusEntries;
	}

	@Override
	protected Collection<ProbandStatusEntryOutVO> handleGetProbandStatusEntryList(
			AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandStatusEntryDao statusEntryDao = this.getProbandStatusEntryDao();
		Collection probandStatusEntries = statusEntryDao.findByProband(probandId, psf);
		statusEntryDao.toProbandStatusEntryOutVOCollection(probandStatusEntries);
		return probandStatusEntries;
	}

	@Override
	protected ProbandTagValueOutVO handleGetProbandTagValue(AuthenticationVO auth, Long probandTagValueId)
			throws Exception {
		ProbandTagValueDao tagValueDao = this.getProbandTagValueDao();
		ProbandTagValue tagValue = CheckIDUtil.checkProbandTagValueId(probandTagValueId, tagValueDao);
		ProbandTagValueOutVO result = tagValueDao.toProbandTagValueOutVO(tagValue);
		return result;
	}

	@Override
	protected long handleGetProbandTagValueCount(AuthenticationVO auth, Long probandId)
			throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getProbandTagValueDao().getCount(probandId);
	}

	@Override
	protected Collection<ProbandTagValueOutVO> handleGetProbandTagValueList(
			AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandTagValueDao tagValueDao = this.getProbandTagValueDao();
		Collection probandTagValues = tagValueDao.findByProband(probandId, psf);
		tagValueDao.toProbandTagValueOutVOCollection(probandTagValues);
		return probandTagValues;
	}

	@Override
	protected ProcedureOutVO handleGetProcedure(AuthenticationVO auth,
			Long procedureId) throws Exception {
		ProcedureDao procedureDao = this.getProcedureDao();
		Procedure procedure = CheckIDUtil.checkProcedureId(procedureId, procedureDao);
		ProcedureOutVO result = procedureDao.toProcedureOutVO(procedure);
		return result;
	}

	@Override
	protected long handleGetProcedureCount(
			AuthenticationVO auth, Long probandId) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getProcedureDao().getCount(probandId);
	}

	@Override
	protected Collection<ProcedureOutVO> handleGetProcedureList(
			AuthenticationVO auth, Long probandId, PSFVO psf) throws Exception {
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProcedureDao procedureDao = this.getProcedureDao();
		Collection procedures = procedureDao.findByProband(probandId, psf);
		procedureDao.toProcedureOutVOCollection(procedures);
		return procedures;
	}

	@Override
	protected Collection<TrialOutVO> handleGetReimbursementTrials(AuthenticationVO auth, Long probandId, String costType, PaymentMethod method, Boolean paid)
			throws Exception {
		CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		TrialDao trialDao = this.getTrialDao();
		Collection trials = trialDao.findByReimbursementProbandSorted(probandId, method, costType, paid);
		trialDao.toTrialOutVOCollection(trials);
		return trials;
	}

	@Override
	protected ProbandAddressOutVO handleGetWireTransferProbandAddress(AuthenticationVO auth, Long probandId) throws Exception {
		CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		ProbandAddressDao probandAddressDao = this.getProbandAddressDao();
		return probandAddressDao.toProbandAddressOutVO(probandAddressDao.findByProbandWireTransfer(probandId));
	}

	@Override
	protected InquiriesPDFVO handleRenderInquiries(AuthenticationVO auth, Long trialId, Long probandId, Boolean active, Boolean activeSignup, boolean blank) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
		TrialDao trialDao = this.getTrialDao();
		Trial trial = null;
		TrialOutVO trialVO = null;
		Collection<Trial> trials = new ArrayList<Trial>();
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
			trialVO = trialDao.toTrialOutVO(trial);
			trials.add(trial);
		} else {
			trials = trialDao.findByInquiryValuesProbandSorted(null, probandId, active, activeSignup);
			// Iterator<Trial> trialIt = trialDao.findByIdDepartment(null, proband.getDepartment().getId(), null).iterator();
			// while (trialIt.hasNext()) {
			// trial = trialIt.next();
			// if (((!trial.getStatus().isLockdown() && trial.getStatus().isInquiryValueInputEnabled())
			// || this.getInquiryValueDao().getCount(trial.getId(), active, activeSignup, proband.getId()) > 0)
			// && this.getInquiryDao().getCount(trial.getId(), active, activeSignup) > 0) {
			// trials.add(trial);
			// }
			// }
			// trial = null;
		}
		InquiriesPDFVO result = ServiceUtil.renderInquiries(proband, probandVO,
				trials, active,
				activeSignup, blank, this.getTrialDao(), this.getInquiryDao(), this.getInquiryValueDao(), this.getInputFieldDao(), this.getInputFieldSelectionSetValueDao(),
				this.getUserDao());
		// ArrayList<ProbandOutVO> probandVOs = new ArrayList<ProbandOutVO>();
		// HashMap<Long, Collection<TrialOutVO>> trialVOMap = new HashMap<Long, Collection<TrialOutVO>>();
		// HashMap<Long, HashMap<Long, Collection<InquiryValueOutVO>>> valueVOMap = new HashMap<Long, HashMap<Long,Collection<InquiryValueOutVO>>>();
		//
		// HashMap<Long, InputFieldImageVO> imageVOMap = new HashMap<Long, InputFieldImageVO>();
		// HashSet<Long> trialIds=new HashSet<Long>();
		//
		//
		// if (trial != null) {
		// ServiceUtil.populateInquiriesPDFVOMaps(proband, probandVO, trial, trialVO,
		// ServiceUtil.getInquiryValues(trial, probandVO, active, activeSignup, false, false, true, null,
		// this.getInquiryDao(), this.getInquiryValueDao(), this.getInputFieldSelectionSetValueDao()).getPageValues(),
		// probandVOs, trialVOMap, valueVOMap, imageVOMap, trialIds, this.getInputFieldDao());
		//
		//
		// } else {
		// Iterator<Trial> trialIt = trialDao.findByIdDepartment(null, proband.getDepartment().getId(), null).iterator();
		// while (trialIt.hasNext()) {
		// trial = trialIt.next();
		// if (((!trial.getStatus().isLockdown() && trial.getStatus().isInquiryValueInputEnabled())
		// || this.getInquiryValueDao().getCount(trial.getId(), active, activeSignup, proband.getId()) > 0)
		// && this.getInquiryDao().getCount(trial.getId(), active, activeSignup) > 0) {
		// trialVO = trialDao.toTrialOutVO(trial);
		// ServiceUtil.populateInquiriesPDFVOMaps(proband, probandVO, trial, trialVO,
		// ServiceUtil.getInquiryValues(trial, probandVO, active, activeSignup, false, false, true, null,
		// this.getInquiryDao(), this.getInquiryValueDao(), this.getInputFieldSelectionSetValueDao()).getPageValues(),
		// probandVOs, trialVOMap, valueVOMap, imageVOMap, trialIds, this.getInputFieldDao());
		// }
		// }
		// trial = null;
		// trialVO = null;
		// }
		//
		//
		//
		//
		//
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		//
		// InquiriesPDFPainter painter = new InquiriesPDFPainter();
		//
		//
		//
		// painter.setProbandVOs(probandVOs);
		// painter.setTrialVOMap(trialVOMap);
		// painter.setValueVOMap(valueVOMap);
		// painter.setImageVOMap(imageVOMap);
		// painter.setBlank(blank);
		//
		// User user = CoreUtil.getUser();
		// painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		// (new PDFImprinter(painter, painter)).render();
		// InquiriesPDFVO result = painter.getPdfVO();
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		if (trial != null) {
			ServiceUtil.logSystemMessage(trial, probandVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(), SystemMessageCodes.INQUIRY_PDF_RENDERED,
					result, null, journalEntryDao);
		}
		ServiceUtil.logSystemMessage(proband, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
				trial != null ? SystemMessageCodes.INQUIRY_PDF_RENDERED
						: SystemMessageCodes.INQUIRIES_PDF_RENDERED,
				result, null,
				journalEntryDao);
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected InquiriesPDFVO handleRenderInquiriesSignup(AuthenticationVO auth, Long departmentId, Long probandId, Boolean activeSignup) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
		Department department = null;
		if (departmentId != null) {
			department = CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Collection<Trial> trials = new ArrayList<Trial>();
		Iterator<Trial> trialIt = this.getTrialDao().findBySignup(department != null ? department.getId() : null, true, null).iterator();
		while (trialIt.hasNext()) {
			Trial trial = trialIt.next();
			if (this.getInquiryValueDao().getCount(trial.getId(), null, activeSignup, proband.getId()) > 0) {
				trials.add(trial);
			}
		}
		InquiriesPDFVO result = ServiceUtil.renderInquiries(proband, probandVO,
				trials,
				null, activeSignup, false, this.getTrialDao(), this.getInquiryDao(), this.getInquiryValueDao(), this.getInputFieldDao(), this.getInputFieldSelectionSetValueDao(),
				this.getUserDao());
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(proband, (TrialOutVO) null, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
				SystemMessageCodes.INQUIRIES_SIGNUP_PDF_RENDERED,
				result, null,
				this.getJournalEntryDao());
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected InquiriesPDFVO handleRenderInquiry(AuthenticationVO auth, Long trialId, String category, Long probandId, Boolean active, Boolean activeSignup, boolean blank)
			throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialOutVO trialVO = trialDao.toTrialOutVO(trial);
		Collection<Trial> trials = new ArrayList<Trial>();
		trials.add(trial);
		InquiriesPDFVO result = ServiceUtil.renderInquiries(proband, probandVO,
				trials, active,
				activeSignup, blank, this.getTrialDao(), this.getInquiryDao(), this.getInquiryValueDao(), this.getInputFieldDao(), this.getInputFieldSelectionSetValueDao(),
				this.getUserDao());
		//
		//
		//
		// ArrayList<ProbandOutVO> probandVOs = new ArrayList<ProbandOutVO>();
		// HashMap<Long, Collection<TrialOutVO>> trialVOMap = new HashMap<Long, Collection<TrialOutVO>>();
		// HashMap<Long, HashMap<Long, Collection<InquiryValueOutVO>>> valueVOMap = new HashMap<Long, HashMap<Long,Collection<InquiryValueOutVO>>>();
		//
		// HashMap<Long, InputFieldImageVO> imageVOMap = new HashMap<Long, InputFieldImageVO>();
		// HashSet<Long> trialIds=new HashSet<Long>();
		//
		//
		//
		// ServiceUtil.populateInquiriesPDFVOMaps(proband, probandVO, trial, trialVO,
		// getInquiryValues(trial, category, probandVO, active, activeSignup, false, false, true, null).getPageValues(),
		// probandVOs, trialVOMap, valueVOMap, imageVOMap, trialIds, this.getInputFieldDao());
		//
		//
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		//
		// InquiriesPDFPainter painter = new InquiriesPDFPainter();
		//
		//
		//
		// painter.setProbandVOs(probandVOs);
		// painter.setTrialVOMap(trialVOMap);
		// painter.setValueVOMap(valueVOMap);
		// painter.setImageVOMap(imageVOMap);
		// painter.setBlank(blank);
		//
		// User user = CoreUtil.getUser();
		// painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		// (new PDFImprinter(painter, painter)).render();
		// InquiriesPDFVO result = painter.getPdfVO();
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(trial, probandVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(), SystemMessageCodes.INQUIRY_PDF_RENDERED,
				result, null, journalEntryDao);
		ServiceUtil.logSystemMessage(proband, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(), SystemMessageCodes.INQUIRY_PDF_RENDERED,
				result, null,
				journalEntryDao);
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected ProbandLetterPDFVO handleRenderProbandLetterPDF(
			AuthenticationVO auth, Long probandAddressId) throws Exception {
		ProbandAddressDao probandAddressDao = this.getProbandAddressDao();
		ProbandAddress address = CheckIDUtil.checkProbandAddressId(probandAddressId, probandAddressDao);
		if (!address.getProband().isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LETTER_NOT_FOR_ANIMAL_ENTRIES);
		}
		ProbandAddressOutVO addressVO = probandAddressDao.toProbandAddressOutVO(address);
		ProbandLetterPDFPainter painter = ServiceUtil.createProbandLetterPDFPainter(addressVO);
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		ProbandLetterPDFVO result = painter.getPdfVO();
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		logSystemMessage(address.getProband(), addressVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
				SystemMessageCodes.PROBAND_ADDRESS_PROBAND_LETTER_PDF_RENDERED, result, null, this.getJournalEntryDao());
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected ProbandLetterPDFVO handleRenderProbandLettersPDF(
			AuthenticationVO auth, Long probandId) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		if (!proband.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LETTER_NOT_FOR_ANIMAL_ENTRIES);
		}
		ArrayList<ProbandOutVO> probandVOs = new ArrayList<ProbandOutVO>();
		ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
		probandVOs.add(probandVO);
		ProbandLetterPDFPainter painter = ServiceUtil.createProbandLetterPDFPainter(probandVOs, this.getProbandAddressDao());
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		ProbandLetterPDFVO result = painter.getPdfVO();
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(proband, probandVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.PROBAND_LETTER_PDF_RENDERED, result,
				null, this.getJournalEntryDao());
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected ProbandOutVO handleResetAutoDeleteDeadline(
			AuthenticationVO auth, Long probandId, Long version) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO original = probandDao.toProbandOutVO(proband);
		ServiceUtil.checkProbandLocked(proband);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(proband, version.longValue(), now, user);
		ServiceUtil.resetAutoDeleteDeadline(proband, now);
		probandDao.update(proband);
		ServiceUtil.notifyExpiringProbandAutoDelete(proband, now, this.getNotificationDao());
		ProbandOutVO result = probandDao.toProbandOutVO(proband);
		ServiceUtil.logSystemMessage(proband, result, now, user, SystemMessageCodes.PROBAND_AUTO_DELETE_DEADLINE_RESET, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<MoneyTransferOutVO> handleSetAllMoneyTransfersPaid(
			AuthenticationVO auth, Long probandId, Long trialId, boolean paid) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao, LockMode.PESSIMISTIC_WRITE);
		if (!probandDao.toProbandOutVO(proband).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		if (!proband.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PROBAND_NOT_PERSON);
		}
		ServiceUtil.checkProbandLocked(proband);
		TrialDao trialDao = this.getTrialDao();
		Trial trial = null;
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trial == null ? null : trial.getId(), null, proband.getId(),
				null,
				null, !paid, null, null);
		if (moneyTransfers.size() == 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PAID_NOT_CHANGED);
		}
		ArrayList<MoneyTransferOutVO> results = new ArrayList<MoneyTransferOutVO>(moneyTransfers.size());
		Iterator<MoneyTransfer> moneyTransfersIt = moneyTransfers.iterator();
		while (moneyTransfersIt.hasNext()) {
			MoneyTransfer moneyTransfer = moneyTransfersIt.next();
			MoneyTransferOutVO original = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
			if (!original.isDecrypted()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_MONEY_TRANSFER);
			}
			Trial moneyTransferTrial = moneyTransfer.getTrial();
			if (moneyTransferTrial != null) {
				ServiceUtil.checkTrialLocked(moneyTransferTrial);
			}
			moneyTransfer.setPaid(paid);
			CoreUtil.modifyVersion(moneyTransfer, moneyTransfer, now, user);
			moneyTransferDao.update(moneyTransfer);
			MoneyTransferOutVO result = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
			if (moneyTransferTrial != null) {
				logSystemMessage(moneyTransferTrial, result.getProband(), now, user, paid ? SystemMessageCodes.MONEY_TRANSFER_PAID_SET
						: SystemMessageCodes.MONEY_TRANSFER_PAID_UNSET, result, original, journalEntryDao);
			}
			ServiceUtil.logSystemMessage(moneyTransfer.getProband(), original.getProband(), now, user, paid ? SystemMessageCodes.MONEY_TRANSFER_PAID_SET
					: SystemMessageCodes.MONEY_TRANSFER_PAID_UNSET, result, original, journalEntryDao);
			results.add(result);
		}
		return results;
	}

	@Override
	protected InquiryValuesOutVO handleSetInquiryValues(
			AuthenticationVO auth, Set<InquiryValueInVO> inquiryValuesIn)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		InquiryValuesOutVO result = new InquiryValuesOutVO();
		ServiceException firstException = null;
		HashMap<Long, String> errorMessagesMap = new HashMap<Long, String>();
		Proband proband = null;
		if (inquiryValuesIn != null && inquiryValuesIn.size() > 0) {
			Trial trial = null;
			ArrayList<InquiryValueOutVO> inquiryValues = new ArrayList<InquiryValueOutVO>(inquiryValuesIn.size());
			ArrayList<InquiryValueJsonVO> jsInquiryValues = null;
			if (Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
					DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				jsInquiryValues = new ArrayList<InquiryValueJsonVO>(inquiryValuesIn.size());
			}
			Iterator<InquiryValueInVO> inquiryValuesInIt = inquiryValuesIn.iterator();
			while (inquiryValuesInIt.hasNext()) {
				InquiryValueInVO inquiryValueIn = inquiryValuesInIt.next();
				Inquiry inquiry = CheckIDUtil.checkInquiryId(inquiryValueIn.getInquiryId(), this.getInquiryDao());
				if (trial == null) {
					trial = inquiry.getTrial();
					ServiceUtil.checkTrialLocked(trial);
					if (!trial.getStatus().isInquiryValueInputEnabled()) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_VALUE_INPUT_DISABLED_FOR_TRIAL,
								CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
					}
				} else if (!trial.equals(inquiry.getTrial())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_VALUES_FOR_DIFFERENT_TRIALS);
				}
				if (proband == null) {
					proband = CheckIDUtil.checkProbandId(inquiryValueIn.getProbandId(), this.getProbandDao(), LockMode.PESSIMISTIC_WRITE);
					ServiceUtil.checkProbandLocked(proband);
				} else if (!proband.getId().equals(inquiryValueIn.getProbandId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_VALUES_FOR_DIFFERENT_PROBANDS);
				}
				try {
					addUpdateInquiryValue(inquiryValueIn, proband, inquiry, now, user, ServiceUtil.LOG_INQUIRY_VALUE_TRIAL, ServiceUtil.LOG_INQUIRY_VALUE_PROBAND, inquiryValues,
							jsInquiryValues);
				} catch (ServiceException e) {
					if (firstException == null) {
						firstException = e;
					}
					errorMessagesMap.put(inquiry.getId(), e.getMessage());
				}
			}
			if (firstException != null) {
				firstException.setData(errorMessagesMap);
				throw firstException;
			}
			Collections.sort(inquiryValues, new InquiryValueOutVOComparator());
			result.setPageValues(inquiryValues);
			if (jsInquiryValues != null) {
				result.setJsValues(jsInquiryValues);
			}
		}
		return result;
	}

	@Override
	protected MoneyTransferOutVO handleSetMoneyTransferPaid(
			AuthenticationVO auth, Long moneyTransferId, Long version, boolean paid) throws Exception {
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		MoneyTransfer moneyTransfer = CheckIDUtil.checkMoneyTransferId(moneyTransferId, moneyTransferDao);
		MoneyTransferOutVO original = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_MONEY_TRANSFER);
		}
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = moneyTransfer.getProband();
		if (!proband.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PROBAND_NOT_PERSON);
		}
		probandDao.lock(proband, LockMode.PESSIMISTIC_WRITE);
		// Proband proband = ServiceUtil.checkProbandId(moneyTransfer.getProband().getId(), probandDao, LockMode.PESSIMISTIC_WRITE);
		if (!probandDao.toProbandOutVO(proband).isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(proband);
		Trial trial = moneyTransfer.getTrial();
		if (trial != null) {
			ServiceUtil.checkTrialLocked(trial);
		}
		if (paid == original.getPaid()) { // unboxed, ok
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PAID_NOT_CHANGED);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(moneyTransfer, version.longValue(), now, user);
		moneyTransfer.setPaid(paid);
		moneyTransferDao.update(moneyTransfer);
		MoneyTransferOutVO result = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (trial != null) {
			logSystemMessage(trial, result.getProband(), now, user, paid ? SystemMessageCodes.MONEY_TRANSFER_PAID_SET : SystemMessageCodes.MONEY_TRANSFER_PAID_UNSET, result,
					original, journalEntryDao);
		}
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, paid ? SystemMessageCodes.MONEY_TRANSFER_PAID_SET : SystemMessageCodes.MONEY_TRANSFER_PAID_UNSET,
				result, original, journalEntryDao);
		return result;
	}

	@Override
	protected ProbandAddressOutVO handleSetProbandAddressWireTransfer(
			AuthenticationVO auth, Long probandAddressId, Long version)
			throws Exception {
		ProbandAddressDao addressDao = this.getProbandAddressDao();
		ProbandAddress address = CheckIDUtil.checkProbandAddressId(probandAddressId, addressDao);
		Proband proband = address.getProband();
		this.getProbandDao().lock(proband, LockMode.PESSIMISTIC_WRITE);
		// Proband proband = ServiceUtil.checkProbandId(address.getProband().getId(), this.getProbandDao(), LockMode.PESSIMISTIC_WRITE);
		ProbandAddressOutVO original = addressDao.toProbandAddressOutVO(address);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_ADDRESS);
		}
		ServiceUtil.checkProbandLocked(proband);
		if (address.isWireTransfer()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_ADDRESS_WIRE_TRANSFER_NOT_CHANGED);
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		Iterator<ProbandAddress> addressesIt = addressDao.findByProband(proband.getId(), null, null, true, null).iterator();
		while (addressesIt.hasNext()) {
			ProbandAddress oldWireTransferAddress = addressesIt.next();
			ProbandAddressOutVO oldWireTransferAddressOriginal = addressDao.toProbandAddressOutVO(address);
			oldWireTransferAddress.setWireTransfer(false);
			CoreUtil.modifyVersion(oldWireTransferAddress, oldWireTransferAddress, now, user);
			addressDao.update(oldWireTransferAddress);
			ProbandAddressOutVO oldWireTransferAddressResult = addressDao.toProbandAddressOutVO(address);
			ServiceUtil.logSystemMessage(oldWireTransferAddress.getProband(), oldWireTransferAddressOriginal.getProband(), now, user,
					SystemMessageCodes.PROBAND_ADDRESS_WIRE_TRANSFER_UNSET, oldWireTransferAddressResult, oldWireTransferAddressOriginal, journalEntryDao);
		}
		address.setWireTransfer(true);
		CoreUtil.modifyVersion(address, version.longValue(), now, user);
		addressDao.update(address);
		ProbandAddressOutVO result = addressDao.toProbandAddressOutVO(address);
		ServiceUtil.logSystemMessage(proband, result.getProband(), now, user, SystemMessageCodes.PROBAND_ADDRESS_WIRE_TRANSFER_SET, result, original, journalEntryDao);
		return result;
	}

	@Override
	protected ProbandImageOutVO handleSetProbandImage(AuthenticationVO auth,
			ProbandImageInVO probandImage) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband originalProband = CheckIDUtil.checkProbandId(probandImage.getId(), probandDao);
		ProbandImageOutVO original = probandDao.toProbandImageOutVO(originalProband);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ServiceUtil.checkProbandLocked(originalProband);
		checkProbandImageInput(probandImage);
		boolean hasImage = original.getHasImage();
		boolean cleared = probandImage.getDatas() == null || probandImage.getDatas().length == 0;
		probandDao.evict(originalProband);
		Proband proband = probandDao.probandImageInVOToEntity(probandImage);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalProband, proband, now, user);
		probandDao.update(proband);
		ProbandImageOutVO result = probandDao.toProbandImageOutVO(proband);
		ServiceUtil.logSystemMessage(proband, probandDao.toProbandOutVO(proband), now, user, cleared ? SystemMessageCodes.PROBAND_IMAGE_CLEARED
				: hasImage ? SystemMessageCodes.PROBAND_IMAGE_UPDATED : SystemMessageCodes.PROBAND_IMAGE_CREATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected BankAccountOutVO handleUpdateBankAccount(
			AuthenticationVO auth, BankAccountInVO modifiedBankAccount) throws Exception {
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		BankAccount originalBankAccount = CheckIDUtil.checkBankAccountId(modifiedBankAccount.getId(), bankAccountDao);
		BankAccountOutVO original = bankAccountDao.toBankAccountOutVO(originalBankAccount);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_BANK_ACCOUNT);
		}
		checkBankAccountInput(modifiedBankAccount);
		if (!modifiedBankAccount.getProbandId().equals(originalBankAccount.getProband().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.BANK_ACCOUNT_PROBAND_CHANGED);
		}
		bankAccountDao.evict(originalBankAccount);
		BankAccount bankAccount = bankAccountDao.bankAccountInVOToEntity(modifiedBankAccount);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalBankAccount, bankAccount, now, user);
		bankAccountDao.update(bankAccount);
		BankAccountOutVO result = bankAccountDao.toBankAccountOutVO(bankAccount);
		ServiceUtil
				.logSystemMessage(bankAccount.getProband(), result.getProband(), now, user, SystemMessageCodes.BANK_ACCOUNT_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected DiagnosisOutVO handleUpdateDiagnosis(AuthenticationVO auth,
			DiagnosisInVO modifiedDiagnosis) throws Exception {
		DiagnosisDao diagnosisDao = this.getDiagnosisDao();
		Diagnosis originalDiagnosis = CheckIDUtil.checkDiagnosisId(modifiedDiagnosis.getId(), diagnosisDao);
		DiagnosisOutVO original = diagnosisDao.toDiagnosisOutVO(originalDiagnosis);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_DIAGNOSIS);
		}
		checkDiagnosisInput(modifiedDiagnosis);
		diagnosisDao.evict(originalDiagnosis);
		Diagnosis diagnosis = diagnosisDao.diagnosisInVOToEntity(modifiedDiagnosis);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalDiagnosis, diagnosis, now, user);
		diagnosisDao.update(diagnosis);
		DiagnosisOutVO result = diagnosisDao.toDiagnosisOutVO(diagnosis);
		ServiceUtil.logSystemMessage(diagnosis.getProband(), result.getProband(), now, user, SystemMessageCodes.DIAGNOSIS_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected MedicationOutVO handleUpdateMedication(AuthenticationVO auth, MedicationInVO modifiedMedication) throws Exception {
		MedicationDao medicationDao = this.getMedicationDao();
		Medication originalMedication = CheckIDUtil.checkMedicationId(modifiedMedication.getId(), medicationDao);
		MedicationOutVO original = medicationDao.toMedicationOutVO(originalMedication);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_MEDICATION);
		}
		checkMedicationInput(modifiedMedication);
		if (!modifiedMedication.getProbandId().equals(originalMedication.getProband().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MEDICATION_PROBAND_CHANGED);
		}
		medicationDao.evict(originalMedication);
		Medication medication = medicationDao.medicationInVOToEntity(modifiedMedication);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalMedication, medication, now, user);
		medicationDao.update(medication);
		MedicationOutVO result = medicationDao.toMedicationOutVO(medication);
		ServiceUtil.logSystemMessage(medication.getProband(), result.getProband(), now, user, SystemMessageCodes.MEDICATION_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected MoneyTransferOutVO handleUpdateMoneyTransfer(
			AuthenticationVO auth, MoneyTransferInVO modifiedMoneyTransfer, Long maxAllowedCostTypes) throws Exception {
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		MoneyTransfer originalMoneyTransfer = CheckIDUtil.checkMoneyTransferId(modifiedMoneyTransfer.getId(), moneyTransferDao);
		MoneyTransferOutVO original = moneyTransferDao.toMoneyTransferOutVO(originalMoneyTransfer);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_MONEY_TRANSFER);
		}
		checkMoneyTransferInput(modifiedMoneyTransfer, maxAllowedCostTypes);
		if (!modifiedMoneyTransfer.getProbandId().equals(originalMoneyTransfer.getProband().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PROBAND_CHANGED);
		}
		moneyTransferDao.evict(originalMoneyTransfer);
		MoneyTransfer moneyTransfer = moneyTransferDao.moneyTransferInVOToEntity(modifiedMoneyTransfer);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalMoneyTransfer, moneyTransfer, now, user);
		moneyTransferDao.update(moneyTransfer);
		Trial trial = moneyTransfer.getTrial();
		MoneyTransferOutVO result = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
		if (trial != null) {
			logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.MONEY_TRANSFER_UPDATED, result, original, this.getJournalEntryDao());
		}
		ServiceUtil.logSystemMessage(moneyTransfer.getProband(), result.getProband(), now, user, SystemMessageCodes.MONEY_TRANSFER_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandOutVO handleUpdatePrivacyConsentStatus(
			AuthenticationVO auth, Long probandId, Long version, Long privacyConsentStatusTypeId) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO original = probandDao.toProbandOutVO(proband);
		PrivacyConsentStatusTypeDao privacyConsentStatusTypeDao = this.getPrivacyConsentStatusTypeDao();
		PrivacyConsentStatusType state = CheckIDUtil.checkPrivacyConsentStatusTypeId(privacyConsentStatusTypeId, privacyConsentStatusTypeDao);
		ServiceUtil.checkProbandLocked(proband);
		boolean validState = false;
		Iterator<PrivacyConsentStatusType> statesIt = privacyConsentStatusTypeDao.findTransitions(proband.getPrivacyConsentStatus().getId()).iterator();
		while (statesIt.hasNext()) {
			if (state.equals(statesIt.next())) {
				validState = true;
				break;
			}
		}
		if (!validState) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_NEW_PRIVACY_CONSENT_STATUS_TYPE,
					L10nUtil.getPrivacyConsentStatusTypeName(Locales.USER, state.getNameL10nKey()));
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(proband, version.longValue(), now, user);
		proband.setPrivacyConsentStatus(state);
		probandDao.update(proband);
		ServiceUtil.notifyExpiringProbandAutoDelete(proband, now, this.getNotificationDao());
		ProbandOutVO result = probandDao.toProbandOutVO(proband);
		ServiceUtil.logSystemMessage(proband, result, now, user, SystemMessageCodes.PRIVACY_CONSENT_STATUS_TYPE_UPDATED, result, original, this.getJournalEntryDao());
		return probandDao.toProbandOutVO(proband);
	}

	@Override
	protected ProbandOutVO handleUpdateProband(AuthenticationVO auth, ProbandInVO modifiedProband, Integer maxInstances, Integer maxParentsDepth, Integer maxChildrenDepth)
			throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		User user = CoreUtil.getUser();
		this.getUserDao().lock(user, LockMode.PESSIMISTIC_WRITE);
		Proband originalProband = CheckIDUtil.checkProbandId(modifiedProband.getId(), probandDao, LockMode.PESSIMISTIC_WRITE);
		ProbandOutVO original = probandDao.toProbandOutVO(originalProband, maxInstances, maxParentsDepth, maxChildrenDepth);
		if (modifiedProband.getBlinded()) {
			if (!user.getDepartment().getId().equals(modifiedProband.getDepartmentId())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DEPARTMENT_NOT_EQUAL_TO_USER_DEPARTMENT);
			}
			if (!modifiedProband.getDepartmentId().equals(originalProband.getDepartment().getId())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_DEPARTMENT_CHANGED);
			}
		} else {
			if (!original.isDecrypted()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
			}
		}
		checkProbandInput(modifiedProband);
		if (originalProband.isPerson() != modifiedProband.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_PERSON_FLAG_CHANGED);
		}
		boolean originalPrivacyConsentControl = originalProband.getCategory().isPrivacyConsentControl();
		probandDao.evict(originalProband);
		Proband proband = probandDao.probandInVOToEntity(modifiedProband);
		checkProbandLoop(proband);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		CoreUtil.modifyVersion(originalProband, proband, now, user);
		if (!originalPrivacyConsentControl && proband.getCategory().isPrivacyConsentControl()) {
			ServiceUtil.resetAutoDeleteDeadline(proband, now);
			proband.setPrivacyConsentStatus(this.getPrivacyConsentStatusTypeDao().findInitialStates().iterator().next());
		}
		probandDao.update(proband);
		ServiceUtil.notifyExpiringProbandAutoDelete(proband, now, this.getNotificationDao());
		ProbandOutVO result = probandDao.toProbandOutVO(proband, maxInstances, maxParentsDepth, maxChildrenDepth);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(proband, result, now, user, SystemMessageCodes.PROBAND_UPDATED, result, original, journalEntryDao);
		Staff physician = proband.getPhysician();
		if (physician != null) {
			ServiceUtil.logSystemMessage(physician, result, now, user, SystemMessageCodes.PROBAND_UPDATED, result, original, journalEntryDao);
		}
		Iterator<ProbandOutVO> parentsIt = original.getParents().iterator();
		while (parentsIt.hasNext()) {
			ProbandOutVO parent = parentsIt.next();
			ServiceUtil.logSystemMessage(probandDao.load(parent.getId()), result, now, user, SystemMessageCodes.PROBAND_UPDATED, result, original, journalEntryDao);
		}
		return result;
	}

	@Override
	protected ProbandAddressOutVO handleUpdateProbandAddress(
			AuthenticationVO auth, ProbandAddressInVO modifiedProbandAddress) throws Exception {
		ProbandAddressDao addressDao = this.getProbandAddressDao();
		ProbandAddress originalAddress = CheckIDUtil.checkProbandAddressId(modifiedProbandAddress.getId(), addressDao);
		ProbandAddressOutVO original = addressDao.toProbandAddressOutVO(originalAddress);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_ADDRESS);
		}
		checkProbandAddressInput(modifiedProbandAddress);
		addressDao.evict(originalAddress);
		ProbandAddress address = addressDao.probandAddressInVOToEntity(modifiedProbandAddress);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalAddress, address, now, user);
		addressDao.update(address);
		ProbandAddressOutVO result = addressDao.toProbandAddressOutVO(address);
		ServiceUtil.logSystemMessage(address.getProband(), result.getProband(), now, user, SystemMessageCodes.PROBAND_ADDRESS_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandOutVO handleUpdateProbandCategory(
			AuthenticationVO auth, Long probandId, Long version, Long categoryId, String comment) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO original = probandDao.toProbandOutVO(proband);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		ProbandCategory category = CheckIDUtil.checkProbandCategoryId(categoryId, this.getProbandCategoryDao());
		if (proband.isPerson()) {
			if (!category.isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CATEGORY_NOT_FOR_PERSON_ENTRIES,
						L10nUtil.getProbandCategoryName(Locales.USER, category.getNameL10nKey()));
			}
		} else {
			if (!category.isAnimal()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_CATEGORY_NOT_FOR_ANIMAL_ENTRIES,
						L10nUtil.getProbandCategoryName(Locales.USER, category.getNameL10nKey()));
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(proband, version.longValue(), now, user);
		proband.setCategory(category);
		if (proband.isPerson()) {
			ProbandContactParticulars personParticulars = proband.getPersonParticulars();
			if (personParticulars != null) {
				CipherText cipherText = CryptoUtil.encryptValue(comment);
				personParticulars.setCommentIv(cipherText.getIv());
				personParticulars.setEncryptedComment(cipherText.getCipherText());
				personParticulars.setCommentHash(CryptoUtil.hashForSearch(comment));
			}
		} else {
			AnimalContactParticulars animalParticulars = proband.getAnimalParticulars();
			if (animalParticulars != null) {
				animalParticulars.setComment(comment);
			}
		}
		probandDao.update(proband);
		ProbandOutVO result = probandDao.toProbandOutVO(proband);
		ServiceUtil.logSystemMessage(proband, result, now, user, SystemMessageCodes.PROBAND_CATEGORY_UPDATED, result, original, this.getJournalEntryDao());
		return probandDao.toProbandOutVO(proband);
	}

	@Override
	protected ProbandContactDetailValueOutVO handleUpdateProbandContactDetailValue(
			AuthenticationVO auth, ProbandContactDetailValueInVO modifiedProbandContactDetailValue)
			throws Exception {
		ProbandContactDetailValueDao contactValueDao = this.getProbandContactDetailValueDao();
		ProbandContactDetailValue originalContactValue = CheckIDUtil.checkProbandContactDetailValueId(modifiedProbandContactDetailValue.getId(), contactValueDao);
		ProbandContactDetailValueOutVO original = contactValueDao.toProbandContactDetailValueOutVO(originalContactValue);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_CONTACT_DETAIL_VALUE);
		}
		checkProbandContactDetailValueInput(modifiedProbandContactDetailValue);
		contactValueDao.evict(originalContactValue);
		ProbandContactDetailValue contactValue = contactValueDao.probandContactDetailValueInVOToEntity(modifiedProbandContactDetailValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalContactValue, contactValue, now, user);
		contactValueDao.update(contactValue);
		ProbandContactDetailValueOutVO result = contactValueDao.toProbandContactDetailValueOutVO(contactValue);
		ServiceUtil.logSystemMessage(contactValue.getProband(), result.getProband(), now, user, SystemMessageCodes.PROBAND_CONTACT_DETAIL_VALUE_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandStatusEntryOutVO handleUpdateProbandStatusEntry(
			AuthenticationVO auth, ProbandStatusEntryInVO modifiedProbandStatusEntry) throws Exception {
		ProbandStatusEntryDao statusEntryDao = this.getProbandStatusEntryDao();
		ProbandStatusEntry originalStatusEntry = CheckIDUtil.checkProbandStatusEntryId(modifiedProbandStatusEntry.getId(), statusEntryDao);
		ProbandStatusEntryOutVO original = statusEntryDao.toProbandStatusEntryOutVO(originalStatusEntry);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_STATUS_ENTRY);
		}
		checkProbandStatusEntryInput(modifiedProbandStatusEntry);
		statusEntryDao.evict(originalStatusEntry);
		ProbandStatusEntry statusEntry = statusEntryDao.probandStatusEntryInVOToEntity(modifiedProbandStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalStatusEntry, statusEntry, now, user);
		statusEntryDao.update(statusEntry);
		notifyProbandInactive(statusEntry, now);
		ProbandStatusEntryOutVO result = statusEntryDao.toProbandStatusEntryOutVO(statusEntry);
		ServiceUtil.logSystemMessage(statusEntry.getProband(), result.getProband(), now, user, SystemMessageCodes.PROBAND_STATUS_ENTRY_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandTagValueOutVO handleUpdateProbandTagValue(
			AuthenticationVO auth, ProbandTagValueInVO modifiedProbandTagValue) throws Exception {
		ProbandTagValueDao tagValueDao = this.getProbandTagValueDao();
		ProbandTagValue originalTagValue = CheckIDUtil.checkProbandTagValueId(modifiedProbandTagValue.getId(), tagValueDao);
		ProbandTagValueOutVO original = tagValueDao.toProbandTagValueOutVO(originalTagValue);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_TAG_VALUE);
		}
		checkProbandTagValueInput(modifiedProbandTagValue);
		tagValueDao.evict(originalTagValue);
		ProbandTagValue tagValue = tagValueDao.probandTagValueInVOToEntity(modifiedProbandTagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalTagValue, tagValue, now, user);
		tagValueDao.update(tagValue);
		ProbandTagValueOutVO result = tagValueDao.toProbandTagValueOutVO(tagValue);
		ServiceUtil.logSystemMessage(tagValue.getProband(), result.getProband(), now, user, SystemMessageCodes.PROBAND_TAG_VALUE_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProcedureOutVO handleUpdateProcedure(AuthenticationVO auth,
			ProcedureInVO modifiedProcedure) throws Exception {
		ProcedureDao procedureDao = this.getProcedureDao();
		Procedure originalProcedure = CheckIDUtil.checkProcedureId(modifiedProcedure.getId(), procedureDao);
		ProcedureOutVO original = procedureDao.toProcedureOutVO(originalProcedure);
		if (!original.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROCEDURE);
		}
		checkProcedureInput(modifiedProcedure);
		procedureDao.evict(originalProcedure);
		Procedure procedure = procedureDao.procedureInVOToEntity(modifiedProcedure);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalProcedure, procedure, now, user);
		procedureDao.update(procedure);
		ProcedureOutVO result = procedureDao.toProcedureOutVO(procedure);
		ServiceUtil.logSystemMessage(procedure.getProband(), result.getProband(), now, user, SystemMessageCodes.PROCEDURE_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	private void notifyProbandInactive(ProbandStatusEntry statusEntry, Date now) throws Exception {
		NotificationDao notificationDao = this.getNotificationDao();
		ServiceUtil.cancelNotifications(statusEntry.getNotifications(), notificationDao, null); // clears inventory_active AND inventory inactive booking notifications
		if (!statusEntry.getType().isProbandActive()) {
			if ((new DateInterval(statusEntry.getStart(), statusEntry.getStop())).contains(now)) {
				notificationDao.addNotification(statusEntry, now, null);
			}
			if (!(new DateInterval(statusEntry.getStart(), statusEntry.getStop())).isOver(now)) {
				VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
				Proband proband = statusEntry.getProband();
				Iterator<ProbandListEntry> trialParticipationsIt = proband.getTrialParticipations().iterator();
				while (trialParticipationsIt.hasNext()) {
					ProbandListEntry probandListEntry = trialParticipationsIt.next();
					ProbandGroup probandGroup = probandListEntry.getGroup();
					if (probandGroup != null) {
						Iterator<VisitScheduleItem> it = visitScheduleItemDao.findByInterval(probandListEntry.getTrial().getId(), probandGroup.getId(), statusEntry.getStart(),
								statusEntry.getStop()).iterator();
						while (it.hasNext()) {
							notificationDao.addNotification(it.next(), proband, statusEntry, now, null);
						}
					}
				}
			}
		}
	}
}