// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::trial::TrialService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.trial;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.EcrfFieldJsVariableNameCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfFieldMoveAdapter;
import org.phoenixctms.ctsms.adapt.EcrfFieldPositionCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfFieldSeriesCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfFieldValueCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfNameRevisionCollisionFinder;
import org.phoenixctms.ctsms.adapt.InquiryJsVariableNameCollisionFinder;
import org.phoenixctms.ctsms.adapt.InquiryMoveAdapter;
import org.phoenixctms.ctsms.adapt.InquiryPositionCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandGroupTitleCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandGroupTokenCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandListEntryMoveAdapter;
import org.phoenixctms.ctsms.adapt.ProbandListEntryPositionCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandListEntryProbandCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandListEntryStatusCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandListEntryTagJsVariableNameCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandListEntryTagMoveAdapter;
import org.phoenixctms.ctsms.adapt.ProbandListEntryTagPositionCollisionFinder;
import org.phoenixctms.ctsms.adapt.ProbandListEntryTagValueCollisionFinder;
import org.phoenixctms.ctsms.adapt.ReminderEntityAdapter;
import org.phoenixctms.ctsms.adapt.TeamMemberRoleCollisionFinder;
import org.phoenixctms.ctsms.adapt.TeamMemberRoleTagAdapter;
import org.phoenixctms.ctsms.adapt.TimelineEventTypeTagAdapter;
import org.phoenixctms.ctsms.adapt.TrialTagAdapter;
import org.phoenixctms.ctsms.adapt.VisitScheduleItemCollisionFinder;
import org.phoenixctms.ctsms.adapt.VisitTitleCollisionFinder;
import org.phoenixctms.ctsms.adapt.VisitTokenCollisionFinder;
import org.phoenixctms.ctsms.adapt.VisitTypeTagAdapter;
import org.phoenixctms.ctsms.compare.DutyRosterTurnIntervalComparator;
import org.phoenixctms.ctsms.compare.EcrfFieldValueOutVOComparator;
import org.phoenixctms.ctsms.compare.EcrfOutVONameComparator;
import org.phoenixctms.ctsms.compare.EcrfStatusActionComparator;
import org.phoenixctms.ctsms.compare.InquiryValueOutVOComparator;
import org.phoenixctms.ctsms.compare.InventoryBookingIntervalComparator;
import org.phoenixctms.ctsms.compare.ProbandListEntryTagValueOutVOComparator;
import org.phoenixctms.ctsms.compare.ProbandListStatusEntryOutVOComparator;
import org.phoenixctms.ctsms.compare.TeamMemberOutVOComparator;
import org.phoenixctms.ctsms.compare.TrialStatusActionComparator;
import org.phoenixctms.ctsms.compare.VisitOutVOTokenComparator;
import org.phoenixctms.ctsms.compare.VisitScheduleAppointmentIntervalComparator;
import org.phoenixctms.ctsms.compare.VisitScheduleItemIntervalComparator;
import org.phoenixctms.ctsms.domain.*;
import org.phoenixctms.ctsms.email.NotificationMessageTemplateParameters;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.ECRFValidationStatus;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.PositionMovement;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.enumeration.RandomizationMode;
import org.phoenixctms.ctsms.enumeration.SignatureModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.enumeration.VisitScheduleDateMode;
import org.phoenixctms.ctsms.excel.AuditTrailExcelDefaultSettings;
import org.phoenixctms.ctsms.excel.AuditTrailExcelSettingCodes;
import org.phoenixctms.ctsms.excel.AuditTrailExcelWriter;
import org.phoenixctms.ctsms.excel.ExcelCellFormat;
import org.phoenixctms.ctsms.excel.ExcelExporter;
import org.phoenixctms.ctsms.excel.ExcelUtil;
import org.phoenixctms.ctsms.excel.ExcelWriterFactory;
import org.phoenixctms.ctsms.excel.ProbandListExcelDefaultSettings;
import org.phoenixctms.ctsms.excel.ProbandListExcelSettingCodes;
import org.phoenixctms.ctsms.excel.ProbandListExcelWriter;
import org.phoenixctms.ctsms.excel.TeamMembersExcelDefaultSettings;
import org.phoenixctms.ctsms.excel.TeamMembersExcelSettingCodes;
import org.phoenixctms.ctsms.excel.TeamMembersExcelWriter;
import org.phoenixctms.ctsms.excel.VisitScheduleExcelWriter;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.js.FieldCalculation;
import org.phoenixctms.ctsms.js.ValidationError;
import org.phoenixctms.ctsms.pdf.EcrfPDFPainter;
import org.phoenixctms.ctsms.pdf.PDFImprinter;
import org.phoenixctms.ctsms.pdf.PDFPainterFactory;
import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.EntitySignature;
import org.phoenixctms.ctsms.security.TrialSignature;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultProbandListStatusReasons;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.JavaScriptCompressor;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ProbandListStatusReasonCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.util.diff_match_patch;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.util.randomization.Randomization;
import org.phoenixctms.ctsms.util.randomization.Randomization.RandomizationType;
import org.phoenixctms.ctsms.vo.AddressTypeVO;
import org.phoenixctms.ctsms.vo.AuditTrailExcelVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ContactDetailTypeVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.DepartmentVO;
import org.phoenixctms.ctsms.vo.DutyRosterTurnOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusQueueCountVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValuesOutVO;
import org.phoenixctms.ctsms.vo.ECRFInVO;
import org.phoenixctms.ctsms.vo.ECRFOutVO;
import org.phoenixctms.ctsms.vo.ECRFPDFVO;
import org.phoenixctms.ctsms.vo.ECRFProgressSummaryVO;
import org.phoenixctms.ctsms.vo.ECRFProgressVO;
import org.phoenixctms.ctsms.vo.ECRFStatusEntryVO;
import org.phoenixctms.ctsms.vo.ECRFVisitVO;
import org.phoenixctms.ctsms.vo.InputFieldImageVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;
import org.phoenixctms.ctsms.vo.InquiryInVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueJsonVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValuesOutVO;
import org.phoenixctms.ctsms.vo.InventoryBookingDurationSummaryVO;
import org.phoenixctms.ctsms.vo.InventoryBookingOutVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferSummaryVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
import org.phoenixctms.ctsms.vo.ProbandInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueJsonVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValuesOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagsPDFVO;
import org.phoenixctms.ctsms.vo.ProbandListExcelVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryInVO;
import org.phoenixctms.ctsms.vo.ProbandListStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ProbandTagVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueOutVO;
import org.phoenixctms.ctsms.vo.RandomizationInfoVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodeInVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodeOutVO;
import org.phoenixctms.ctsms.vo.RandomizationListCodesVO;
import org.phoenixctms.ctsms.vo.RandomizationListInfoVO;
import org.phoenixctms.ctsms.vo.ReimbursementsExcelVO;
import org.phoenixctms.ctsms.vo.ReimbursementsPDFVO;
import org.phoenixctms.ctsms.vo.ShiftDurationSummaryVO;
import org.phoenixctms.ctsms.vo.ShuffleInfoVO;
import org.phoenixctms.ctsms.vo.SignatureVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.StaffTagVO;
import org.phoenixctms.ctsms.vo.StaffTagValueOutVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListInVO;
import org.phoenixctms.ctsms.vo.StratificationRandomizationListOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberInVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TeamMembersExcelVO;
import org.phoenixctms.ctsms.vo.TimelineEventInVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TrialECRFProgressSummaryVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialRandomizationListVO;
import org.phoenixctms.ctsms.vo.TrialTagValueInVO;
import org.phoenixctms.ctsms.vo.TrialTagValueOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitInVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleAppointmentVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;
import org.phoenixctms.ctsms.vocycle.TimelineEventReflexionGraph;

/**
 * @see org.phoenixctms.ctsms.service.trial.TrialService
 */
public class TrialServiceImpl
		extends TrialServiceBase {

	private final static String SHUFFLE_SEED_RANDOM_ALGORITHM = CoreUtil.RANDOM_ALGORITHM;
	private final static java.util.regex.Pattern JS_VARIABLE_NAME_REGEXP = Pattern.compile("^[A-Za-z0-9_]+$");
	private final static int PROBAND_ALIAS_FORMAT_MAX_ALIAS_0BASED_INDEX = 6;
	private final static int PROBAND_ALIAS_FORMAT_MAX_ALIAS_1BASED_INDEX = 7;
	private final static int PROBAND_ALIAS_FORMAT_PROBAND_COUNT_0BASED_INDEX = 8;
	private final static int PROBAND_ALIAS_FORMAT_PROBAND_COUNT_1BASED_INDEX = 9;

	private static void copyInquiryValueInToOut(InquiryValueOutVO out, InquiryValueInVO in, InquiryOutVO inquiryVO, ProbandOutVO probandVO, UserOutVO modifiedUserVO, Date now) {
		if (in != null && out != null) {
			out.setId(in.getId()); // nullable!
			out.setBooleanValue(in.getBooleanValue());
			out.setDateValue(in.getDateValue());
			out.setTimeValue(in.getTimeValue());
			out.setFloatValue(in.getFloatValue());
			out.setTextValue(in.getTextValue());
			out.setTimestampValue(in.getTimestampValue());
			out.setLongValue(in.getLongValue());
			out.setInkValues(in.getInkValues());
			out.setModifiedTimestamp(now);
			out.setVersion(in.getVersion());
			out.setProband(probandVO);
			out.setModifiedUser(modifiedUserVO);
			out.getSelectionValues().clear();
			if (inquiryVO != null) {
				out.setInquiry(inquiryVO);
				InputFieldOutVO inputField = inquiryVO.getField();
				if (inputField != null) {
					HashSet<Long> ids = new HashSet<Long>(in.getSelectionValueIds());
					Iterator<InputFieldSelectionSetValueOutVO> it = inputField.getSelectionSetValues().iterator();
					while (it.hasNext()) {
						InputFieldSelectionSetValueOutVO selectionValue = it.next();
						if (ids.contains(selectionValue.getId())) {
							out.getSelectionValues().add(selectionValue);
						}
					}
				}
			}
		}
	}

	private final static boolean hasEcrfStatusAction(ECRFStatusType statusType, org.phoenixctms.ctsms.enumeration.ECRFStatusAction action) {
		Iterator<ECRFStatusAction> actionIt = statusType.getActions().iterator();
		while (actionIt.hasNext()) {
			if (action.equals(actionIt.next().getAction())) {
				return true;
			}
		}
		return false;
	}

	private final static boolean hasTrialStatusAction(TrialStatusType statusType, org.phoenixctms.ctsms.enumeration.TrialStatusAction action) {
		Iterator<TrialStatusAction> actionIt = statusType.getActions().iterator();
		while (actionIt.hasNext()) {
			if (action.equals(actionIt.next().getAction())) {
				return true;
			}
		}
		return false;
	}

	private static JournalEntry logSystemMessage(Course course, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(course, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.COURSE_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Inventory inventory, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inventory, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.INVENTORY_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(MassMail massMail, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(massMail, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.MASS_MAIL_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Proband proband, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			RandomizationInfoVO randomizationInfo, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(proband, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null)),
						CoreUtil.getSystemMessageCommentContent(randomizationInfo, null, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null))
				});
	}

	private static JournalEntry logSystemMessage(Trial trial, ProbandOutVO probandVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			RandomizationInfoVO randomizationInfo, JournalEntryDao journalEntryDao) throws Exception {
		// we don't print proband name etc...
		boolean journalEncrypted = CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null);
		return journalEntryDao.addSystemMessage(trial, now, modified, systemMessageCode, journalEncrypted ? new Object[] { CommonUtil.probandOutVOToString(probandVO) }
				: new Object[] { Long.toString(probandVO.getId()) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !journalEncrypted),
						CoreUtil.getSystemMessageCommentContent(randomizationInfo, null, !journalEncrypted)
				});
	}

	private static JournalEntry logSystemMessage(User user, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null)) });
	}

	private ECRFFieldOutVO addEcrfField(ECRFFieldInVO newEcrfField, Timestamp now, User user) throws Exception {
		checkAddEcrfFieldInput(newEcrfField);
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		ECRFField ecrfField = ecrfFieldDao.eCRFFieldInVOToEntity(newEcrfField);
		CoreUtil.modifyVersion(ecrfField, now, user);
		ecrfField = ecrfFieldDao.create(ecrfField);
		ECRFFieldOutVO result = ecrfFieldDao.toECRFFieldOutVO(ecrfField);
		ServiceUtil.logSystemMessage(ecrfField.getTrial(), result.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	private ECRFFieldStatusEntryOutVO addEcrfFieldStatusEntry(ECRFFieldStatusEntryInVO newEcrfFieldStatusEntry, ECRFStatusEntry statusEntry, ECRFFieldStatusQueue queue,
			Timestamp now, User user,
			boolean logTrial,
			boolean logProband, boolean action) throws Exception {
		Object[] resultItems = checkAddEcrfFieldStatusEntryInput(newEcrfFieldStatusEntry, statusEntry, queue, now, user, action);
		ECRFFieldStatusEntry lastStatus = (ECRFFieldStatusEntry) resultItems[0];
		statusEntry = (ECRFStatusEntry) resultItems[1];
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ECRFFieldStatusEntry fieldStatus = ecrfFieldStatusEntryDao.eCRFFieldStatusEntryInVOToEntity(newEcrfFieldStatusEntry);
		fieldStatus.setQueue(queue);
		CoreUtil.modifyVersion(fieldStatus, now, user);
		fieldStatus = ecrfFieldStatusEntryDao.create(fieldStatus);
		notifyEcrfFieldStatus(statusEntry, lastStatus, fieldStatus, queue, now);
		ECRFFieldStatusEntryOutVO result = ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVO(fieldStatus);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (logProband) {
			ServiceUtil.logSystemMessage(fieldStatus.getListEntry().getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_STATUS_ENTRY_CREATED,
					result, null, journalEntryDao);
		}
		if (logTrial) {
			ServiceUtil.logSystemMessage(fieldStatus.getListEntry().getTrial(), result.getListEntry().getProband(), now, user,
					SystemMessageCodes.ECRF_FIELD_STATUS_ENTRY_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	private Object[] addEcrfStatusEntry(ProbandListEntry listEntry, ECRF ecrf, Visit visit, ECRFStatusType statusType, Long probandListStatusTypeId, Timestamp now, User user)
			throws Exception {
		checkAddEcrfStatusEntry(listEntry, ecrf, visit, statusType, user);
		ECRFStatusEntry statusEntry = ECRFStatusEntry.Factory.newInstance();
		statusEntry.setValidationStatus(ECRFValidationStatus.NOT_VALIDATED);
		statusEntry.setStatus(statusType);
		statusEntry.setEcrf(ecrf);
		statusEntry.setVisit(visit);
		ecrf.addEcrfStatusEntries(statusEntry);
		statusEntry.setListEntry(listEntry);
		listEntry.addEcrfStatusEntries(statusEntry);
		if (visit != null) {
			visit.addEcrfStatusEntries(statusEntry);
		}
		CoreUtil.modifyVersion(statusEntry, now, user);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		statusEntry = ecrfStatusEntryDao.create(statusEntry);
		execEcrfStatusActions(statusEntry, probandListStatusTypeId, now, user);
		ECRFStatusEntryVO result = ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry);
		ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.ECRF_STATUS_ENTRY_CREATED, result, null,
				journalEntryDao);
		ServiceUtil.logSystemMessage(listEntry.getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.ECRF_STATUS_ENTRY_CREATED, result, null,
				journalEntryDao);
		return new Object[] { statusEntry, result };
	}

	private InquiryOutVO addInquiry(InquiryInVO newInquiry, Timestamp now, User user) throws Exception {
		checkAddInquiryInput(newInquiry);
		InquiryDao inquiryDao = this.getInquiryDao();
		Inquiry inquiry = inquiryDao.inquiryInVOToEntity(newInquiry);
		CoreUtil.modifyVersion(inquiry, now, user);
		inquiry = inquiryDao.create(inquiry);
		InquiryOutVO result = inquiryDao.toInquiryOutVO(inquiry);
		ServiceUtil.logSystemMessage(inquiry.getTrial(), result.getTrial(), now, user, SystemMessageCodes.INQUIRY_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	private ProbandListEntryOutVO addProbandListEntry(ProbandListEntryInVO newProbandListEntry, boolean createProband, boolean signup, Randomization randomization, Trial trial,
			Timestamp now, User user) throws Exception {
		checkProbandListEntryInput(newProbandListEntry, createProband, signup, now);
		ProbandDao probandDao = this.getProbandDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Proband proband = null;
		if (createProband) {
			ProbandInVO newProband = new ProbandInVO();
			if (!CommonUtil.isEmptyString(trial.getProbandAliasFormat())) {
				newProband.setAlias(getNewProbandAlias(trial, user));
			}
			newProband.setBlinded(true);
			newProband.setCategoryId(this.getProbandCategoryDao().findPreset(signup, trial.getType().isPerson()).getId());
			newProband.setDepartmentId(user.getDepartment().getId());
			newProband.setPerson(trial.getType().isPerson());
			proband = ServiceUtil.createProband(newProband,
					now, user,
					probandDao,
					this.getPrivacyConsentStatusTypeDao(),
					this.getProbandContactParticularsDao(),
					this.getAnimalContactParticularsDao(),
					this.getNotificationDao());
			ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
			ServiceUtil.logSystemMessage(proband, probandVO, now, user, SystemMessageCodes.PROBAND_CREATED, probandVO, null, journalEntryDao);
		}
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = probandListEntryDao.probandListEntryInVOToEntity(newProbandListEntry);
		if (proband != null) {
			listEntry.setProband(proband);
			proband.addTrialParticipations(listEntry);
		} else {
			proband = listEntry.getProband();
		}
		ProbandListStatusType statusType = this.getProbandListStatusTypeDao().findInitialStates(signup, proband.isPerson()).iterator().next();
		boolean randomized = false;
		if (randomization != null) {
			if (RandomizationType.GROUP.equals(randomization.getType())) {
				ProbandGroup group = randomization.getRandomizedProbandGroup(trial, null);
				if (listEntry.getGroup() != null) {
					listEntry.getGroup().removeProbandListEntries(listEntry);
				}
				listEntry.setGroup(group);
				group.addProbandListEntries(listEntry);
				randomized = true;
			}
		}
		if (signup) {
			Long position = probandListEntryDao.findMaxPosition(trial.getId());
			if (position == null) {
				position = CommonUtil.LIST_INITIAL_POSITION;
			} else {
				position += 1L;
			}
			listEntry.setPosition(position);
		}
		CoreUtil.modifyVersion(listEntry, now, user);
		listEntry = probandListEntryDao.create(listEntry);
		if (randomization != null) {
			if (!RandomizationType.GROUP.equals(randomization.getType())) { // when created, not possible..
				applyListEntryTagRandomization(trial, listEntry, null, randomization, now, user);
				randomized = true;
			}
		}
		ProbandListStatusEntryOutVO probandListStatusEntryVO = null;
		if (statusType != null && statusType.isInitial()) {
			if (statusType.isSignup() && !trial.isSignupProbandList()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_SIGNUP_DISABLED);
			}
			ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
			String reason = (listEntry.getGroup() != null ? L10nUtil.getProbandListStatusReason(Locales.PROBAND_LIST_STATUS_ENTRY_REASON,
					randomized ? ProbandListStatusReasonCodes.LIST_ENTRY_RANDOMIZED_AND_CREATED : ProbandListStatusReasonCodes.LIST_ENTRY_CREATED,
					randomized ? DefaultProbandListStatusReasons.LIST_ENTRY_RANDOMIZED_AND_CREATED : DefaultProbandListStatusReasons.LIST_ENTRY_CREATED,
					new Object[] { CommonUtil.ENCRPYTED_PROBAND_LIST_STATUS_ENTRY_REASON ? CommonUtil.probandOutVOToString(probandVO)
							: CommonUtil.getProbandAlias(probandVO, null, L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)),
							listEntry.getGroup().getTitle() })
					: L10nUtil.getProbandListStatusReason(Locales.PROBAND_LIST_STATUS_ENTRY_REASON,
							randomized ? ProbandListStatusReasonCodes.LIST_ENTRY_RANDOMIZED_AND_CREATED_NO_GROUP : ProbandListStatusReasonCodes.LIST_ENTRY_CREATED_NO_GROUP,
							randomized ? DefaultProbandListStatusReasons.LIST_ENTRY_RANDOMIZED_AND_CREATED_NO_GROUP : DefaultProbandListStatusReasons.LIST_ENTRY_CREATED_NO_GROUP,
							new Object[] { CommonUtil.ENCRPYTED_PROBAND_LIST_STATUS_ENTRY_REASON ? CommonUtil.probandOutVOToString(probandVO)
									: CommonUtil.getProbandAlias(probandVO, null, L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)) }));
			if (!statusType.isReasonRequired() || !CommonUtil.isEmptyString(reason)) {
				ProbandListStatusEntry statusEntry = ProbandListStatusEntry.Factory.newInstance();
				statusEntry.setListEntry(listEntry);
				listEntry.addStatusEntries(statusEntry);
				if (CommonUtil.ENCRPYTED_PROBAND_LIST_STATUS_ENTRY_REASON) {
					statusEntry.setReason(null);
					CipherText cipherText = CryptoUtil.encryptValue(reason);
					statusEntry.setReasonIv(cipherText.getIv());
					statusEntry.setEncryptedReason(cipherText.getCipherText());
					statusEntry.setReasonHash(CryptoUtil.hashForSearch(reason));
				} else {
					statusEntry.setReasonIv(null);
					statusEntry.setEncryptedReason(null);
					statusEntry.setReasonHash(null);
					statusEntry.setReason(reason);
				}
				statusEntry.setRealTimestamp(CommonUtil.dateToTimestamp(DateCalc.getMillisCleared(now)));
				statusEntry.setStatus(statusType);
				CoreUtil.modifyVersion(statusEntry, now, user);
				ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
				statusEntry = probandListStatusEntryDao.create(statusEntry);
				listEntry.setLastStatus(statusEntry);
				probandListEntryDao.update(listEntry);
				probandListStatusEntryVO = probandListStatusEntryDao.toProbandListStatusEntryOutVO(statusEntry);
				MassMailDao massMailDao = this.getMassMailDao();
				TrialDao trialDao = this.getTrialDao();
				MassMailRecipientDao massMailRecipientDao = this.getMassMailRecipientDao();
				Iterator<MassMail> massMailsIt = this.getMassMailDao().findByTrialProbandListStatusTypeLocked(trial.getId(), statusType.getId(), false, null).iterator();
				while (massMailsIt.hasNext()) {
					ServiceUtil.addResetMassMailRecipient(massMailsIt.next(), proband, now, user, massMailDao, probandDao, trialDao,
							massMailRecipientDao, journalEntryDao);
				}
			}
		}
		ProbandListEntryOutVO result = probandListEntryDao.toProbandListEntryOutVO(listEntry);
		if (randomization != null) {
			logSystemMessage(proband, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_RANDOMIZED_AND_CREATED, result, null,
					randomization.getRandomizationInfoVO(), journalEntryDao);
			logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_RANDOMIZED_AND_CREATED, result, null,
					randomization.getRandomizationInfoVO(), journalEntryDao);
		} else {
			ServiceUtil.logSystemMessage(proband, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_CREATED, result, null, journalEntryDao);
			ServiceUtil.logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_CREATED, result, null, journalEntryDao);
		}
		if (probandListStatusEntryVO != null) {
			ServiceUtil.logSystemMessage(proband, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_STATUS_ENTRY_CREATED, probandListStatusEntryVO, null,
					journalEntryDao);
			ServiceUtil
					.logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_STATUS_ENTRY_CREATED, probandListStatusEntryVO, null, journalEntryDao);
		}
		return result;
	}

	private ProbandListEntryTagOutVO addProbandListEntryTag(ProbandListEntryTagInVO newProbandListEntryTag, Timestamp now, User user) throws Exception {
		checkAddProbandListEntryTagInput(newProbandListEntryTag);
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		ProbandListEntryTag probandListEntryTag = probandListEntryTagDao.probandListEntryTagInVOToEntity(newProbandListEntryTag);
		CoreUtil.modifyVersion(probandListEntryTag, now, user);
		probandListEntryTag = probandListEntryTagDao.create(probandListEntryTag);
		ProbandListEntryTagOutVO result = probandListEntryTagDao.toProbandListEntryTagOutVO(probandListEntryTag);
		ServiceUtil.logSystemMessage(probandListEntryTag.getTrial(), result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_CREATED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	private ProbandListStatusEntryOutVO addProbandListEntryUpdatedProbandListStatusEntry(String reasonL10nKey, String reasonNoGroupL10nKey, ProbandListEntry probandListEntry,
			Long probandListStatusTypeId, Timestamp now, User user) throws Exception {
		Object[] args;
		String l10nKey;
		ProbandGroup group = probandListEntry.getGroup();
		ProbandOutVO probandVO = this.getProbandDao().toProbandOutVO(probandListEntry.getProband());
		if (group != null) {
			args = new Object[] {
					CommonUtil.ENCRPYTED_PROBAND_LIST_STATUS_ENTRY_REASON ? CommonUtil.probandOutVOToString(probandVO)
							: CommonUtil.getProbandAlias(probandVO, null, L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME)),
					group.getTitle()
			};
			l10nKey = reasonL10nKey;
		} else {
			args = new Object[] {
					CommonUtil.ENCRPYTED_PROBAND_LIST_STATUS_ENTRY_REASON ? CommonUtil.probandOutVOToString(probandVO)
							: CommonUtil.getProbandAlias(probandVO, null, L10nUtil.getString(MessageCodes.BLINDED_PROBAND_NAME, DefaultMessages.BLINDED_PROBAND_NAME))
			};
			l10nKey = reasonNoGroupL10nKey;
		}
		return ServiceUtil.addProbandListStatusEntry(probandListEntry, null, l10nKey, args, now, probandListStatusTypeId, now, user,
				this.getProbandDao(),
				this.getProbandListEntryDao(),
				this.getProbandListStatusEntryDao(),
				this.getProbandListStatusTypeDao(),
				this.getTrialDao(),
				this.getMassMailDao(),
				this.getMassMailRecipientDao(),
				this.getJournalEntryDao());
	}

	private TeamMemberOutVO addTeamMember(TeamMemberInVO newTeamMember, Timestamp now, User user) throws Exception {
		checkTeamMemberInput(newTeamMember);
		TeamMemberDao teamMemberDao = this.getTeamMemberDao();
		TeamMember teamMember = teamMemberDao.teamMemberInVOToEntity(newTeamMember);
		CoreUtil.modifyVersion(teamMember, now, user);
		teamMember = teamMemberDao.create(teamMember);
		TeamMemberOutVO result = teamMemberDao.toTeamMemberOutVO(teamMember);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(teamMember.getTrial(), result.getStaff(), now, user, SystemMessageCodes.TEAM_MEMBER_CREATED, result, null, journalEntryDao);
		ServiceUtil.logSystemMessage(teamMember.getStaff(), result.getTrial(), now, user, SystemMessageCodes.TEAM_MEMBER_CREATED, result, null, journalEntryDao);
		return result;
	}

	private void addUpdateEcrfFieldValue(ECRFFieldValueInVO ecrfFieldValueIn, ECRFStatusEntry ecrfStatusEntry, ProbandListEntryOutVO listEntryVO, ECRFField ecrfField,
			Timestamp now,
			User user,
			boolean force,
			boolean logTrial,
			boolean logProband,
			LinkedHashMap<String, LinkedHashSet<Long>> sectionIndexMap, ArrayList<ECRFFieldValueOutVO> outEcrfFieldValues, ArrayList<ECRFFieldValueJsonVO> outJsEcrfFieldValues)
			throws Exception {
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		Long id = ecrfFieldValueIn.getId();
		ECRFFieldValueOutVO result = null;
		ECRFFieldValueJsonVO resultJs = null;
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Visit visit = ecrfStatusEntry.getVisit();
		checkEcrfFieldValueIndex(ecrfField, ecrfFieldValueIn.getListEntryId(), visit != null ? visit.getId() : null, ecrfFieldValueIn.getEcrfFieldId(),
				ecrfFieldValueIn.getIndex());
		ProbandListEntry listEntry = ecrfStatusEntry.getListEntry();
		boolean isAuditTrail = ecrfField.isAuditTrail() && ecrfStatusEntry.getStatus().isAuditTrail();
		if (id == null) {
			if (ecrfField.isDisabled()) {
				ecrfFieldValueIn = ServiceUtil.createPresetEcrfFieldInValue(ecrfField, listEntry.getId(), visit != null ? visit.getId() : null, ecrfFieldValueIn.getIndex(),
						this.getInputFieldSelectionSetValueDao());
			}
			checkEcrfFieldValueInputUnlockedForFieldStatus(ecrfFieldValueIn, ecrfStatusEntry, visit, ecrfField);
			checkEcrfFieldValueInput(ecrfFieldValueIn, ecrfStatusEntry, ecrfField);
			ServiceUtil.addAutocompleteSelectionSetValue(ecrfField.getField(), ecrfFieldValueIn.getTextValue(), now, user, this.getInputFieldSelectionSetValueDao(),
					journalEntryDao);
			ECRFFieldValue ecrfFieldValue = ecrfFieldValueDao.eCRFFieldValueInVOToEntity(ecrfFieldValueIn);
			CoreUtil.modifyVersion(ecrfFieldValue, now, user);
			if (isAuditTrail) {
				ecrfFieldValue.setChangeComment(CoreUtil.getAuditTrailChangeCommentContent(ecrfFieldValueDao.toECRFFieldValueOutVO(ecrfFieldValue), null, true));
			}
			InputFieldValue inputFieldValue = ecrfFieldValue.getValue();
			this.getInputFieldValueDao().create(inputFieldValue);
			ecrfFieldValue = ecrfFieldValueDao.create(ecrfFieldValue);
			if (outEcrfFieldValues != null || logProband || logTrial) {
				result = ecrfFieldValueDao.toECRFFieldValueOutVO(ecrfFieldValue);
			}
			if (outJsEcrfFieldValues != null && !CommonUtil.isEmptyString(ecrfField.getJsVariableName())) {
				resultJs = ecrfFieldValueDao.toECRFFieldValueJsonVO(ecrfFieldValue);
			}
			if (logProband) {
				ServiceUtil.logSystemMessage(listEntry.getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_VALUE_CREATED, result, null,
						journalEntryDao);
			}
			if (logTrial) {
				ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.ECRF_FIELD_VALUE_CREATED, result, null,
						journalEntryDao);
			}
		} else {
			ECRFFieldValue originalEcrfFieldValue = null;
			if (!CheckIDUtil.checkEcrfFieldValueId(id, ecrfFieldValueDao).equals( // modified check when new record was created meanwhile
					originalEcrfFieldValue = ecrfFieldValueDao.getByListEntryVisitEcrfFieldIndex(ecrfFieldValueIn.getListEntryId(), visit != null ? visit.getId() : null,
							ecrfFieldValueIn.getEcrfFieldId(),
							ecrfFieldValueIn.getIndex()))) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ENTITY_WAS_MODIFIED_SINCE, originalEcrfFieldValue.getModifiedUser().getName());
			}
			if (!ecrfField.isDisabled()
					&& (!CommonUtil.isEmptyString(ecrfFieldValueIn.getReasonForChange())
							|| !ServiceUtil.ecrfFieldValueEquals(ecrfFieldValueIn, originalEcrfFieldValue.getValue(), force))) {
				checkEcrfFieldValueInputUnlockedForFieldStatus(ecrfFieldValueIn, ecrfStatusEntry, visit, ecrfField);
				checkEcrfFieldValueInput(ecrfFieldValueIn, ecrfStatusEntry, ecrfField);
				ServiceUtil.addAutocompleteSelectionSetValue(ecrfField.getField(), ecrfFieldValueIn.getTextValue(), now, user, this.getInputFieldSelectionSetValueDao(),
						journalEntryDao);
				ECRFFieldValueOutVO original = null;
				if (isAuditTrail || logProband || logTrial) {
					original = ecrfFieldValueDao.toECRFFieldValueOutVO(originalEcrfFieldValue);
				}
				ecrfFieldValueDao.evict(originalEcrfFieldValue);
				ECRFFieldValue ecrfFieldValue;
				if (isAuditTrail) {
					ecrfFieldValueIn = new ECRFFieldValueInVO(ecrfFieldValueIn);
					ecrfFieldValueIn.setId(null); // arg must not be manipulated
				}
				ecrfFieldValue = ecrfFieldValueDao.eCRFFieldValueInVOToEntity(ecrfFieldValueIn);
				CoreUtil.modifyVersion(originalEcrfFieldValue, ecrfFieldValue, now, user); // modified check when record was updated meanwhile
				if (isAuditTrail) {
					ecrfFieldValue.setChangeComment(CoreUtil.getAuditTrailChangeCommentContent(ecrfFieldValueDao.toECRFFieldValueOutVO(ecrfFieldValue), original, true));
					InputFieldValue inputFieldValue = ecrfFieldValue.getValue();
					this.getInputFieldValueDao().create(inputFieldValue);
					ecrfFieldValue = ecrfFieldValueDao.create(ecrfFieldValue);
				} else {
					ecrfFieldValue.setChangeComment(null);
					ecrfFieldValueDao.update(ecrfFieldValue);
				}
				if (outEcrfFieldValues != null || logProband || logTrial) {
					result = ecrfFieldValueDao.toECRFFieldValueOutVO(ecrfFieldValue);
				}
				if (outJsEcrfFieldValues != null && !CommonUtil.isEmptyString(ecrfField.getJsVariableName())) {
					resultJs = ecrfFieldValueDao.toECRFFieldValueJsonVO(ecrfFieldValue);
				}
				if (logProband) {
					ServiceUtil.logSystemMessage(listEntry.getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_VALUE_UPDATED, result, original,
							journalEntryDao);
				}
				if (logTrial) {
					ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.ECRF_FIELD_VALUE_UPDATED, result, original,
							journalEntryDao);
				}
			} else {
				if (outEcrfFieldValues != null) {
					result = ecrfFieldValueDao.toECRFFieldValueOutVO(originalEcrfFieldValue);
				}
				if (outJsEcrfFieldValues != null && !CommonUtil.isEmptyString(ecrfField.getJsVariableName())) {
					resultJs = ecrfFieldValueDao.toECRFFieldValueJsonVO(originalEcrfFieldValue);
				}
			}
		}
		if (sectionIndexMap != null && ecrfFieldValueIn.getIndex() != null) {
			if (sectionIndexMap.containsKey(ecrfField.getSection())) {
				sectionIndexMap.get(ecrfField.getSection()).add(ecrfFieldValueIn.getIndex());
			} else {
				LinkedHashSet<Long> indexes = new LinkedHashSet<Long>();
				indexes.add(ecrfFieldValueIn.getIndex());
				sectionIndexMap.put(ecrfField.getSection(), indexes);
			}
		}
		if (outEcrfFieldValues != null) {
			outEcrfFieldValues.add(result);
		}
		if (resultJs != null) {
			outJsEcrfFieldValues.add(resultJs);
		}
	}

	private void addUpdateProbandListEntryTagValue(ProbandListEntryTagValueInVO probandListEntryTagValueIn, ProbandListEntry listEntry, ProbandListEntryTag listEntryTag,
			Timestamp now, User user, boolean force, boolean logTrial,
			boolean logProband, ArrayList<ProbandListEntryTagValueOutVO> tagValues, ArrayList<ProbandListEntryTagValueJsonVO> jsTagValues) throws Exception {
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		Long id = probandListEntryTagValueIn.getId();
		ProbandListEntryTagValueOutVO result = null;
		ProbandListEntryTagValueJsonVO resultJs = null;
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (id == null) {
			if (listEntryTag.isDisabled()) {
				probandListEntryTagValueIn = ServiceUtil.createPresetProbandListEntryTagInValue(listEntryTag, listEntry.getId(), this.getInputFieldSelectionSetValueDao());
			}
			checkProbandListEntryTagValueInput(probandListEntryTagValueIn, listEntry, listEntryTag);
			ServiceUtil.addAutocompleteSelectionSetValue(listEntryTag.getField(), probandListEntryTagValueIn.getTextValue(), now, user, this.getInputFieldSelectionSetValueDao(),
					journalEntryDao);
			ProbandListEntryTagValue listEntryTagValue = probandListEntryTagValueDao.probandListEntryTagValueInVOToEntity(probandListEntryTagValueIn);
			CoreUtil.modifyVersion(listEntryTagValue, now, user);
			InputFieldValue inputFieldValue = listEntryTagValue.getValue();
			this.getInputFieldValueDao().create(inputFieldValue);
			listEntryTagValue = probandListEntryTagValueDao.create(listEntryTagValue);
			notifyVisitScheduleItemReminder(listEntry, listEntryTag, now);
			if (tagValues != null || logTrial || logProband) {
				result = probandListEntryTagValueDao.toProbandListEntryTagValueOutVO(listEntryTagValue);
			}
			if (jsTagValues != null && !CommonUtil.isEmptyString(listEntryTag.getJsVariableName())) {
				resultJs = probandListEntryTagValueDao.toProbandListEntryTagValueJsonVO(listEntryTagValue);
			}
			if (logProband) {
				ServiceUtil.logSystemMessage(listEntry.getProband(), result.getTag().getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_CREATED, result, null,
						journalEntryDao);
			}
			if (logTrial) {
				ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_CREATED, result,
						null,
						journalEntryDao);
			}
		} else {
			ProbandListEntryTagValue originalListEntryTagValue = CheckIDUtil.checkProbandListEntryTagValueId(id, probandListEntryTagValueDao);
			if (!listEntryTag.isDisabled()
					&& !ServiceUtil.probandListEntryTagValueEquals(probandListEntryTagValueIn, originalListEntryTagValue.getValue(), force)) {
				boolean isEqual = ServiceUtil.probandListEntryTagValueEquals(probandListEntryTagValueIn, originalListEntryTagValue.getValue(), false);
				checkProbandListEntryTagValueInput(probandListEntryTagValueIn, listEntry, listEntryTag); // access original associations before evict
				ServiceUtil.addAutocompleteSelectionSetValue(listEntryTag.getField(), probandListEntryTagValueIn.getTextValue(), now, user,
						this.getInputFieldSelectionSetValueDao(),
						journalEntryDao);
				ProbandListEntryTagValueOutVO original = null;
				if (logProband || logTrial) {
					original = probandListEntryTagValueDao.toProbandListEntryTagValueOutVO(originalListEntryTagValue);
				}
				probandListEntryTagValueDao.evict(originalListEntryTagValue);
				ProbandListEntryTagValue listEntryTagValue = probandListEntryTagValueDao.probandListEntryTagValueInVOToEntity(probandListEntryTagValueIn);
				CoreUtil.modifyVersion(originalListEntryTagValue, listEntryTagValue, now, user);
				probandListEntryTagValueDao.update(listEntryTagValue);
				if (!isEqual) {
					notifyVisitScheduleItemReminder(listEntry, listEntryTag, now);
				}
				if (tagValues != null || logTrial || logProband) {
					result = probandListEntryTagValueDao.toProbandListEntryTagValueOutVO(listEntryTagValue);
				}
				if (jsTagValues != null && !CommonUtil.isEmptyString(listEntryTag.getJsVariableName())) {
					resultJs = probandListEntryTagValueDao.toProbandListEntryTagValueJsonVO(listEntryTagValue);
				}
				if (logProband) {
					ServiceUtil.logSystemMessage(listEntry.getProband(), result.getTag().getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_UPDATED, result,
							original,
							journalEntryDao);
				}
				if (logTrial) {
					ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_UPDATED,
							result,
							original,
							journalEntryDao);
				}
			} else {
				if (tagValues != null) {
					result = probandListEntryTagValueDao.toProbandListEntryTagValueOutVO(originalListEntryTagValue);
				}
				if (jsTagValues != null && !CommonUtil.isEmptyString(listEntryTag.getJsVariableName())) {
					resultJs = probandListEntryTagValueDao.toProbandListEntryTagValueJsonVO(originalListEntryTagValue);
				}
			}
		}
		if (tagValues != null) {
			tagValues.add(result);
		}
		if (resultJs != null) {
			jsTagValues.add(resultJs);
		}
	}

	private int addValidationEcrfFieldStatusEntries(ECRFStatusEntry statusEntry, boolean addSeries, Timestamp now, User user) throws Exception {
		ProbandListEntry listEntry = statusEntry.getListEntry();
		ECRF ecrf = statusEntry.getEcrf();
		Visit visit = statusEntry.getVisit();
		ProbandListEntryOutVO listEntryVO = this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry);
		// edit checks based on subject PII are discouraged, if present we raise a mismatch etc. as on screen:
		// if (!listEntryVO.getProband().getDecrypted()) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		// }
		UserOutVO userVO = this.getUserDao().toUserOutVO(user);
		Collection visitScheduleItems = null;
		Collection probandGroups = null;
		if (listEntry.getGroup() != null) {
			VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
			visitScheduleItems = visitScheduleItemDao.findByTrialGroupVisitProbandTravel(listEntry.getTrial().getId(), listEntry.getGroup().getId(),
					visit != null ? visit.getId() : null, //narrow ecrf visit?
					listEntry.getProband().getId(), null, true, null);
			visitScheduleItemDao.toVisitScheduleItemOutVOCollection(visitScheduleItems);
		}
		ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
		probandGroups = probandGroupDao.findByTrial(listEntry.getTrial().getId(), null);
		probandGroupDao.toProbandGroupOutVOCollection(probandGroups);
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		Collection<ProbandListEntryTagValueJsonVO> probandListEntryTagValues = ServiceUtil.getProbandListEntryTagJsonValues(
				probandListEntryTagValueDao.findByListEntryJs(listEntryVO.getId(), true, null, null),
				false, probandListEntryTagValueDao, inputFieldSelectionSetValueDao);
		HashMap<String, Long> maxSeriesIndexMap = null;
		HashMap<String, Long> fieldMaxPositionMap = null;
		HashMap<String, Long> fieldMinPositionMap = null;
		HashMap<String, Set<ECRFField>> seriesEcrfFieldMap = null;
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		if (addSeries) {
			maxSeriesIndexMap = new HashMap<String, Long>();
			fieldMaxPositionMap = new HashMap<String, Long>();
			fieldMinPositionMap = new HashMap<String, Long>();
			seriesEcrfFieldMap = new HashMap<String, Set<ECRFField>>();
			ServiceUtil.initSeriesEcrfFieldMaps(
					this.getECRFFieldDao().findByTrialEcrfSeriesJs(null, ecrf.getId(), true, true, true, null),
					listEntryVO.getId(),
					ecrf.getId(),
					visit != null ? visit.getId() : null,
					maxSeriesIndexMap,
					fieldMaxPositionMap,
					fieldMinPositionMap,
					seriesEcrfFieldMap,
					ecrfFieldValueDao);
		}
		Collection<ECRFFieldValueJsonVO> jsValues = ServiceUtil.getEcrfFieldJsonValues(visit != null ? visit.getId() : null,
				ecrfFieldValueDao.findByListEntryEcrfVisitJsField(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null, true, true, null, null),
				maxSeriesIndexMap, fieldMaxPositionMap, fieldMinPositionMap, seriesEcrfFieldMap,
				false, ecrfFieldValueDao, inputFieldSelectionSetValueDao);
		FieldCalculation fieldCalculation = new FieldCalculation();
		fieldCalculation.setProbandListEntry(listEntryVO);
		fieldCalculation.setActiveUser(userVO);
		fieldCalculation.setLocale(Locales.AUDIT_TRAIL);
		fieldCalculation.setProbandListEntryTagValues(probandListEntryTagValues);
		fieldCalculation.setVisitScheduleItems(visitScheduleItems);
		fieldCalculation.setProbandGroups(probandGroups);
		fieldCalculation.setECRFFieldInputFieldVariableValues(jsValues);
		Exception scriptException = null;
		int errorCount = 0;
		HashMap<Long, HashMap<Long, ValidationError>> validationErrorMap = new HashMap<Long, HashMap<Long, ValidationError>>();
		try {
			Iterator<ValidationError> it = fieldCalculation.initInputFieldVariables().iterator();
			while (it.hasNext()) {
				ValidationError msg = it.next();
				if (validationErrorMap.containsKey(msg.getEcrfFieldId())) {
					validationErrorMap.get(msg.getEcrfFieldId()).put(msg.getIndex(), msg);
				} else {
					HashMap<Long, ValidationError> indexErrorMap = new HashMap<Long, ValidationError>();
					indexErrorMap.put(msg.getIndex(), msg);
					validationErrorMap.put(msg.getEcrfFieldId(), indexErrorMap);
				}
				errorCount++;
			}
		} catch (Exception e) {
			scriptException = e;
		}
		Iterator<Map> ecrfFieldValuesIt = ecrfFieldValueDao
				.findByListEntryEcrfVisitJsField(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null, true, null, null, null).iterator();
		while (ecrfFieldValuesIt.hasNext()) {
			Map<String, Object> entities = ecrfFieldValuesIt.next();
			ECRFFieldValue ecrfFieldValue = (ECRFFieldValue) entities.get(ServiceUtil.ECRF_FIELD_VALUE_DAO_ECRF_FIELD_VALUE_ALIAS);
			ECRFField ecrfField;
			Long index;
			if (ecrfFieldValue == null) {
				ecrfField = (ECRFField) entities.get(ServiceUtil.ECRF_FIELD_VALUE_DAO_ECRF_FIELD_ALIAS);
				index = (ecrfField.isSeries() ? 0l : null);
			} else {
				ecrfField = ecrfFieldValue.getEcrfField();
				index = ecrfFieldValue.getIndex();
			}
			ECRFFieldStatusEntryInVO newEcrfFieldStatusEntry = new ECRFFieldStatusEntryInVO();
			newEcrfFieldStatusEntry.setVisitId(visit != null ? visit.getId() : null);
			newEcrfFieldStatusEntry.setEcrfFieldId(ecrfField.getId());
			newEcrfFieldStatusEntry.setListEntryId(listEntry.getId());
			newEcrfFieldStatusEntry.setIndex(index);
			ValidationError msg;
			HashMap<Long, ValidationError> indexErrorMap;
			if (scriptException != null) {
				ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(ECRFFieldStatusQueue.VALIDATION, listEntry.getId(),
						visit != null ? visit.getId() : null, ecrfField.getId(), index);
				if (lastStatus == null || lastStatus.getStatus().getTransitions().contains(this.getECRFFieldStatusTypeDao().getValidationFailed())) {
					newEcrfFieldStatusEntry.setStatusId(this.getECRFFieldStatusTypeDao().getValidationFailed().getId());
					newEcrfFieldStatusEntry.setComment(scriptException.getMessage());
				} else {
					continue;
				}
			} else if ((indexErrorMap = validationErrorMap.get(ecrfField.getId())) != null && (msg = indexErrorMap.get(index)) != null) {
				ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(ECRFFieldStatusQueue.VALIDATION, listEntry.getId(),
						visit != null ? visit.getId() : null, ecrfField.getId(), index);
				if (lastStatus == null || lastStatus.getStatus().getTransitions().contains(this.getECRFFieldStatusTypeDao().getValidationError())) {
					newEcrfFieldStatusEntry.setStatusId(this.getECRFFieldStatusTypeDao().getValidationError().getId());
					newEcrfFieldStatusEntry.setComment(msg.getOutput());
				} else {
					continue;
				}
			} else {
				ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(ECRFFieldStatusQueue.VALIDATION, listEntry.getId(),
						visit != null ? visit.getId() : null, ecrfField.getId(), index);
				if (lastStatus != null && lastStatus.getStatus().getTransitions().contains(this.getECRFFieldStatusTypeDao().getValidationSuccess())) {
					// or write always?
					newEcrfFieldStatusEntry.setStatusId(this.getECRFFieldStatusTypeDao().getValidationSuccess().getId());
				} else {
					continue;
				}
			}
			addEcrfFieldStatusEntry(newEcrfFieldStatusEntry, statusEntry, ECRFFieldStatusQueue.VALIDATION, now, user, false, false, true);
		}
		statusEntry.setValidationTimestamp(now);
		if (scriptException != null) {
			statusEntry.setValidationStatus(ECRFValidationStatus.FAILED);
			statusEntry.setValidationResponseMsg(L10nUtil.getMessage(Locales.NOTIFICATION, MessageCodes.ECRF_VALIDATION_FAILED_RESPONSE,
					DefaultMessages.ECRF_VALIDATION_FAILED_RESPONSE,
					scriptException.getMessage()));
		} else if (errorCount > 0) {
			statusEntry.setValidationStatus(ECRFValidationStatus.OK);
			statusEntry.setValidationResponseMsg(L10nUtil.getMessage(Locales.NOTIFICATION, MessageCodes.ECRF_VALIDATION_OK_ERRORS_RESPONSE,
					DefaultMessages.ECRF_VALIDATION_OK_ERRORS_RESPONSE,
					errorCount));
		} else {
			statusEntry.setValidationStatus(ECRFValidationStatus.OK);
			statusEntry.setValidationResponseMsg(L10nUtil.getMessage(Locales.NOTIFICATION, MessageCodes.ECRF_VALIDATION_OK_NO_ERROR_RESPONSE,
					DefaultMessages.ECRF_VALIDATION_OK_NO_ERROR_RESPONSE));
		}
		this.getECRFStatusEntryDao().update(statusEntry);
		return errorCount;
	}

	private void applyListEntryTagRandomization(Trial trial, ProbandListEntry probandListEntry, ProbandListEntry originalProbandListEntry, Randomization randomization,
			Timestamp now, User user) throws Exception {
		ProbandListEntryTag randomizationTag = randomization.checkRandomizeProbandListEntryTag(trial);
		ProbandListEntryTagValueInVO tagIn;
		ProbandListEntryTagValue originalTagValue = null;
		if (originalProbandListEntry != null) {
			try {
				originalTagValue = this.getProbandListEntryTagValueDao().findByListEntryListEntryTag(originalProbandListEntry.getId(), this.getProbandListEntryTagDao()
						.findByTrialFieldStratificationRandomize(trial.getId(), null, null, true).iterator().next().getId()).iterator().next();
			} catch (NoSuchElementException e) {
			}
		}
		switch (randomization.getType()) {
			case TAG_SELECT:
				if (originalTagValue != null) {
					Collection<InputFieldSelectionSetValue> values = originalTagValue.getValue().getSelectionValues();
					if (values != null && values.size() > 0) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.RANDOMIZE_INPUT_FIELD_SELECTION_SET_VALUE_NOT_EMPTY);
					}
				}
				InputFieldSelectionSetValue selectionSetValue = randomization.getRandomizedInputFieldSelectionSetValue(trial, originalProbandListEntry);
				tagIn = ServiceUtil.createPresetProbandListEntryTagInValue(randomizationTag, probandListEntry.getId(), this.getInputFieldSelectionSetValueDao());
				tagIn.getSelectionValueIds().clear();
				tagIn.getSelectionValueIds().add(selectionSetValue.getId());
				if (originalTagValue != null) {
					tagIn.setId(originalTagValue.getId());
					tagIn.setVersion(originalTagValue.getVersion());
				}
				addUpdateProbandListEntryTagValue(tagIn, probandListEntry, randomizationTag, now, user, true,
						Settings.getBoolean(SettingCodes.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_TRIAL, Bundle.SETTINGS, DefaultSettings.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_TRIAL),
						Settings.getBoolean(SettingCodes.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_PROBAND, Bundle.SETTINGS, DefaultSettings.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_PROBAND), null,
						null);
				break;
			case TAG_TEXT:
				if (originalTagValue != null) {
					String textValue = originalTagValue.getValue().getStringValue();
					if (textValue != null && textValue.length() > 0) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.RANDOMIZE_INPUT_FIELD_TEXT_VALUE_NOT_EMPTY);
					}
				}
				String textValue = randomization.getRandomizedInputFieldTextValue(trial, originalProbandListEntry);
				tagIn = ServiceUtil.createPresetProbandListEntryTagInValue(randomizationTag, probandListEntry.getId(), this.getInputFieldSelectionSetValueDao());
				tagIn.setTextValue(textValue);
				if (originalTagValue != null) {
					tagIn.setId(originalTagValue.getId());
					tagIn.setVersion(originalTagValue.getVersion());
				}
				addUpdateProbandListEntryTagValue(tagIn, probandListEntry, randomizationTag, now, user, true,
						Settings.getBoolean(SettingCodes.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_TRIAL, Bundle.SETTINGS, DefaultSettings.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_TRIAL),
						Settings.getBoolean(SettingCodes.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_PROBAND, Bundle.SETTINGS, DefaultSettings.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_PROBAND), null,
						null);
				break;
			default:
				throw new IllegalArgumentException(
						L10nUtil.getMessage(MessageCodes.UNSUPPORTED_RANDOMIZATION_TYPE, DefaultMessages.UNSUPPORTED_RANDOMIZATION_TYPE, randomization.getType()));
		}
	}

	private void checkAddEcrfFieldInput(ECRFFieldInVO ecrfFieldIn) throws ServiceException {
		InputField field = CheckIDUtil.checkInputFieldId(ecrfFieldIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(ecrfFieldIn.getTrialId(), this.getTrialDao());
		ServiceUtil.checkTrialLocked(trial);
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfFieldIn.getEcrfId(), this.getECRFDao());
		if (!trial.equals(ecrf.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_WRONG_ECRF, CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
		}
		checkEcrfFieldInput(ecrfFieldIn);
		ServiceUtil.checkLockedEcrfs(ecrf, this.getECRFStatusEntryDao(), this.getECRFDao());
		if (ecrfFieldIn.getSeries()
				&& this.getECRFFieldDao().getCount(null, ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection(), null, null) > 0 // optimization, bulk inserts
		) {
			if (this.getECRFFieldValueDao().getCount(ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection()) > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_SECTION_WITH_VALUES, ecrfFieldIn.getSection());
			}
			if (this.getECRFFieldStatusEntryDao().getCount(ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection()) > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_SECTION_WITH_STATUS_ENTRIES, ecrfFieldIn.getSection());
			}
		}
	}

	private Object[] checkAddEcrfFieldStatusEntryInput(ECRFFieldStatusEntryInVO ecrfFieldStatusEntryIn, ECRFStatusEntry ecrfStatusEntry, ECRFFieldStatusQueue queue, Timestamp now,
			User user, boolean action)
			throws Exception {
		// referential checks
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(ecrfFieldStatusEntryIn.getListEntryId(), this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
		ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldStatusEntryIn.getEcrfFieldId(), this.getECRFFieldDao());
		Visit visit = null;
		if (ecrfFieldStatusEntryIn.getVisitId() != null) {
			visit = CheckIDUtil.checkVisitId(ecrfFieldStatusEntryIn.getVisitId(), this.getVisitDao());
		}
		ECRFFieldStatusType state = CheckIDUtil.checkEcrfFieldStatusTypeId(ecrfFieldStatusEntryIn.getStatusId(), this.getECRFFieldStatusTypeDao());
		if (!listEntry.getTrial().equals(ecrfField.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_FOR_WRONG_TRIAL);
		}
		ECRF ecrf = ecrfField.getEcrf();
		ServiceUtil.checkTrialLocked(ecrf.getTrial());
		if (!ecrf.getTrial().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_TRIAL,
					L10nUtil.getTrialStatusTypeName(Locales.USER, ecrf.getTrial().getStatus().getNameL10nKey()));
		}
		if (ecrf.isDisabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_ECRF, ecrf.getName());
		}
		ServiceUtil.checkProbandLocked(listEntry.getProband());
		if (ecrfStatusEntry == null) {
			ecrfStatusEntry = this.getECRFStatusEntryDao().findByListEntryEcrfVisit(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null);
			if (ecrfStatusEntry == null) {
				ECRFStatusType statusType = this.getECRFStatusTypeDao().findInitialStates().iterator().next();
				this.getECRFDao().lock(ecrf, LockMode.PESSIMISTIC_WRITE);
				Object[] resultItems = addEcrfStatusEntry(listEntry, ecrf, visit, statusType, null, now, user);
				ecrfStatusEntry = (ECRFStatusEntry) resultItems[0];
			}
		}
		if (visit != null) {
			if (!visit.equals(ecrfStatusEntry.getVisit())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INVALID_VISIT);
			}
		} else {
			if (ecrfStatusEntry.getVisit() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INVALID_VISIT);
			}
		}
		if (ecrfStatusEntry.getStatus().isFieldStatusLockdown()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_INPUT_LOCKED_FOR_ECRF_STATUS,
					L10nUtil.getEcrfStatusTypeName(Locales.USER, ecrfStatusEntry.getStatus().getNameL10nKey()));
		}
		if (listEntry.getLastStatus() != null && !listEntry.getLastStatus().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
					L10nUtil.getProbandListStatusTypeName(Locales.USER, listEntry.getLastStatus().getStatus().getNameL10nKey()));
		}
		checkEcrfFieldStatusIndex(ecrfField, listEntry.getId(), visit != null ? visit.getId() : null, ecrfField.getId(), ecrfFieldStatusEntryIn.getIndex());
		if (!action
				&& (state.isResolved() && Settings.getEcrfFieldStatusQueueList(SettingCodes.RESOLVE_ECRF_FIELD_STATUS_RESTRICTION_QUEUES, Bundle.SETTINGS,
						DefaultSettings.RESOLVE_ECRF_FIELD_STATUS_RESTRICTION_QUEUES).contains(state.getQueue()))) {
			checkTeamMemberResolve(listEntry.getTrial(), user);
		}
		ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(queue, listEntry.getId(), visit != null ? visit.getId() : null,
				ecrfField.getId(), ecrfFieldStatusEntryIn.getIndex());
		Boolean system = (action ? null : false);
		boolean validState = false;
		if (lastStatus == null) {
			Iterator<ECRFFieldStatusType> statesIt = this.getECRFFieldStatusTypeDao().findInitialStates(queue, system).iterator();
			while (statesIt.hasNext()) {
				if (state.equals(statesIt.next())) {
					validState = true;
					break;
				}
			}
			if (!validState) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INITIAL_ECRF_FIELD_STATUS_TYPE,
						L10nUtil.getEcrfFieldStatusTypeName(Locales.USER, state.getNameL10nKey()));
			}
		} else {
			ECRFFieldStatusType lastState = lastStatus.getStatus();
			Iterator<ECRFFieldStatusType> statesIt = this.getECRFFieldStatusTypeDao().findTransitions(lastState.getId(), system).iterator();
			while (statesIt.hasNext()) {
				if (state.equals(statesIt.next())) {
					validState = true;
					break;
				}
			}
			if (!validState) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_NEW_ECRF_FIELD_STATUS_TYPE,
						L10nUtil.getEcrfFieldStatusTypeName(Locales.USER, state.getNameL10nKey()));
			}
		}
		String comment = ecrfFieldStatusEntryIn.getComment();
		if (CommonUtil.isEmptyString(comment) && state.isCommentRequired()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_COMMENT_REQUIRED);
		}
		return new Object[] { lastStatus, ecrfStatusEntry };
	}

	private void checkAddEcrfInput(ECRFInVO ecrfIn) throws ServiceException {
		checkEcrfInput(ecrfIn);
	}

	private void checkAddEcrfStatusEntry(ProbandListEntry listEntry, ECRF ecrf, Visit visit, ECRFStatusType statusType, User user) throws ServiceException {
		if (!ecrf.getTrial().equals(listEntry.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_STATUS_ENTRY_DIFFERENT_TRIALS);
		}
		ServiceUtil.checkTrialLocked(listEntry.getTrial());
		if (!listEntry.getTrial().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_TRIAL,
					L10nUtil.getTrialStatusTypeName(Locales.USER, listEntry.getTrial().getStatus().getNameL10nKey()));
		}
		ServiceUtil.checkProbandLocked(listEntry.getProband());
		if (listEntry.getLastStatus() != null && !listEntry.getLastStatus().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
					L10nUtil.getProbandListStatusTypeName(Locales.USER, listEntry.getLastStatus().getStatus().getNameL10nKey()));
		}
		checkEcrfVisit(ecrf, visit);
		boolean validState = false;
		Iterator<ECRFStatusType> statesIt = this.getECRFStatusTypeDao().findInitialStates().iterator();
		while (statesIt.hasNext()) {
			if (statusType.equals(statesIt.next())) {
				validState = true;
				break;
			}
		}
		if (!validState) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INITIAL_ECRF_STATUS_TYPE, L10nUtil.getEcrfStatusTypeName(Locales.USER, statusType.getNameL10nKey()));
		}
		// entering signed:
		if (hasEcrfStatusAction(statusType, org.phoenixctms.ctsms.enumeration.ECRFStatusAction.SIGN_ECRF)) {
			checkTeamMemberSign(listEntry.getTrial(), user);
			// entering verified:
		} else if (statusType.isVerified()) {
			checkTeamMemberVerify(listEntry.getTrial(), user);
		}
	}

	private void checkAddInquiryInput(InquiryInVO inquiryIn) throws ServiceException {
		InputField field = CheckIDUtil.checkInputFieldId(inquiryIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(inquiryIn.getTrialId(), this.getTrialDao());
		ServiceUtil.checkTrialLocked(trial);
		checkInquiryInput(inquiryIn);
	}

	private void checkAddProbandListEntryTagInput(ProbandListEntryTagInVO listTagIn) throws ServiceException {
		InputField field = CheckIDUtil.checkInputFieldId(listTagIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(listTagIn.getTrialId(), this.getTrialDao());
		ServiceUtil.checkTrialLocked(trial);
		checkProbandListEntryTagInput(listTagIn, trial, field);
	}

	private ArrayList<RandomizationListCodeInVO> checkAddTrialInput(TrialInVO trialIn, Collection<RandomizationListCodeInVO> codes) throws ServiceException {
		checkTrialInput(trialIn);
		TrialStatusTypeDao trialStatusTypeDao = this.getTrialStatusTypeDao();
		TrialStatusType state = CheckIDUtil.checkTrialStatusTypeId(trialIn.getStatusId(), trialStatusTypeDao);
		boolean validState = false;
		Iterator<TrialStatusType> statesIt = trialStatusTypeDao.findInitialStates().iterator();
		while (statesIt.hasNext()) {
			if (state.equals(statesIt.next())) {
				validState = true;
				break;
			}
		}
		if (!validState) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_INITIAL_TRIAL_STATUS_TYPE, L10nUtil.getTrialStatusTypeName(Locales.USER, state.getNameL10nKey()));
		}
		return Randomization.checkTrialInput(null, trialIn, codes, this.getTrialDao(), this.getProbandGroupDao(), this.getProbandListEntryDao(),
				this.getStratificationRandomizationListDao(),
				this.getProbandListEntryTagDao(), this.getInputFieldSelectionSetValueDao(), this.getProbandListEntryTagValueDao(),
				this.getRandomizationListCodeDao());
	}

	private ProbandListEntry checkClearEcrfFieldValues(Long probandListEntryId, Long ecrfId, Long visitId, Timestamp now, User user) throws Exception {
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		Visit visit = null;
		if (visitId != null) {
			visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		ServiceUtil.checkTrialLocked(ecrf.getTrial());
		if (!ecrf.getTrial().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_TRIAL,
					L10nUtil.getTrialStatusTypeName(Locales.USER, ecrf.getTrial().getStatus().getNameL10nKey()));
		}
		if (ecrf.isDisabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_ECRF, ecrf.getName());
		}
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
		if (!ecrf.getTrial().equals(listEntry.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_FOR_WRONG_TRIAL);
		}
		ServiceUtil.checkProbandLocked(listEntry.getProband());
		ECRFStatusEntry statusEntry = this.getECRFStatusEntryDao().findByListEntryEcrfVisit(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null);
		if (statusEntry == null) {
			ECRFStatusType statusType = this.getECRFStatusTypeDao().findInitialStates().iterator().next();
			this.getECRFDao().lock(ecrf, LockMode.PESSIMISTIC_WRITE);
			Object[] resultItems = addEcrfStatusEntry(listEntry, ecrf, visit, statusType, null, now, user);
			statusEntry = (ECRFStatusEntry) resultItems[0];
		}
		if (statusEntry.getStatus().isValueLockdown()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_LOCKED_FOR_ECRF_STATUS,
					L10nUtil.getEcrfStatusTypeName(Locales.USER, statusEntry.getStatus().getNameL10nKey()));
		}
		if (listEntry.getLastStatus() != null && !listEntry.getLastStatus().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
					L10nUtil.getProbandListStatusTypeName(Locales.USER, listEntry.getLastStatus().getStatus().getNameL10nKey()));
		}
		return listEntry;
	}

	private void checkEcrfFieldInput(ECRFFieldInVO ecrfFieldIn) throws ServiceException {
		if (ecrfFieldIn.getSection() != null && !ecrfFieldIn.getSection().trim().equals(ecrfFieldIn.getSection())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.WHITESPACE_ECRF_FIELD_SECTION, ecrfFieldIn.getSection());
		}
		boolean hasJsValueExpression = !CommonUtil.isEmptyString(JavaScriptCompressor.compress(ecrfFieldIn.getJsValueExpression()));
		boolean hasJsOutputExpression = !CommonUtil.isEmptyString(JavaScriptCompressor.compress(ecrfFieldIn.getJsOutputExpression()));
		if (CommonUtil.isEmptyString(ecrfFieldIn.getJsVariableName())) {
			if (hasJsValueExpression || hasJsOutputExpression) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_JS_VARIABLE_NAME_REQUIRED);
			}
		} else {
			if (!JS_VARIABLE_NAME_REGEXP.matcher(ecrfFieldIn.getJsVariableName()).find()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_JS_VARIABLE_NAME_INVALID, ecrfFieldIn.getJsVariableName());
			}
			if ((new EcrfFieldJsVariableNameCollisionFinder(this.getECRFDao(), this.getECRFFieldDao())).collides(ecrfFieldIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_JS_VARIABLE_NAME_NOT_UNIQUE, ecrfFieldIn.getJsVariableName());
			}
		}
		if (ecrfFieldIn.getNotify() && !hasJsValueExpression && !hasJsOutputExpression) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_NO_EXPRESSION_FOR_NOTIFY);
		}
		if (ecrfFieldIn.getReasonForChangeRequired() && !ecrfFieldIn.getAuditTrail()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_AUDIT_TRAIL_FALSE);
		}
		if ((new EcrfFieldSeriesCollisionFinder(this.getECRFDao(), this.getECRFFieldDao())).collides(ecrfFieldIn)) {
			// all fields within section must have same "series" flag
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_FLAG_INCONSISTENT, ecrfFieldIn.getSection());
		}
		if ((new EcrfFieldPositionCollisionFinder(this.getECRFDao(), this.getECRFFieldDao())).collides(ecrfFieldIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_POSITION_NOT_UNIQUE);
		}
	}

	private void checkEcrfFieldStatusIndex(ECRFField ecrfField, Long probandListEntryId, Long visitId, Long ecrfFieldId, Long index) throws ServiceException {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		if (!ecrfField.isSeries()) {
			if (index != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_NOT_NULL,
						CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
			}
		} else {
			if (index == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_NULL,
						CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
			}
			Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(probandListEntryId, visitId, ecrfFieldId);
			if (maxIndex == null) {
				if (!index.equals(0l)) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_NOT_ZERO,
							CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
				}
			} else {
				if (index < 0l) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_LESS_THAN_ZERO,
							CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
				} else if (index > (maxIndex + 1l)) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_GAP,
							CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())), maxIndex + 1l);
				}
			}
		}
	}

	private void checkEcrfFieldValueIndex(ECRFField ecrfField, Long probandListEntryId, Long visitId, Long ecrfFieldId, Long index) throws ServiceException {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		if (!ecrfField.isSeries()) {
			if (index != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NOT_NULL,
						CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
			}
		} else {
			if (index == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NULL,
						CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
			}
			Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(probandListEntryId, visitId, ecrfFieldId);
			if (maxIndex == null) {
				if (!index.equals(0l)) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NOT_ZERO,
							CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
				}
			} else {
				if (index < 0l) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_LESS_THAN_ZERO,
							CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
				} else if (index > (maxIndex + 1l)) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_GAP,
							CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())), maxIndex + 1l);
				}
			}
		}
	}

	private void checkEcrfFieldValueInput(ECRFFieldValueInVO ecrfFieldValueIn, ECRFStatusEntry ecrfStatusEntry, ECRFField ecrfField) throws ServiceException {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = ecrfField.getField();
		inputFieldDao.lock(inputField, LockMode.PESSIMISTIC_WRITE);
		ServiceUtil.checkInputFieldTextValue(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getTextValue(), inputFieldDao, this.getInputFieldSelectionSetValueDao());
		ServiceUtil.checkInputFieldBooleanValue(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getBooleanValue(), inputFieldDao);
		ServiceUtil.checkInputFieldLongValue(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getLongValue(), inputFieldDao);
		ServiceUtil.checkInputFieldFloatValue(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getFloatValue(), inputFieldDao);
		ServiceUtil.checkInputFieldDateValue(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getDateValue(), inputFieldDao);
		ServiceUtil.checkInputFieldTimeValue(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getTimeValue(), inputFieldDao);
		ServiceUtil.checkInputFieldTimestampValue(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getTimestampValue(), inputFieldDao);
		ServiceUtil.checkInputFieldInkValue(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getInkValues(), inputFieldDao);
		ServiceUtil.checkInputFieldSelectionSetValues(inputField, ecrfField.isOptional(), ecrfFieldValueIn.getSelectionValueIds(), inputFieldDao,
				this.getInputFieldSelectionSetValueDao());
		if (ecrfFieldValueIn.getId() == null) {
			if (ecrfFieldValueIn.getReasonForChange() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_REASON_FOR_CHANGE_NOT_NULL,
						CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(inputField)));
			}
		} else {
			ECRFStatusType statusType;
			if (ecrfField.isAuditTrail() && (statusType = ecrfStatusEntry.getStatus()).isAuditTrail()) {
				if (CommonUtil.isEmptyString(ecrfFieldValueIn.getReasonForChange()) && ecrfField.isReasonForChangeRequired() && statusType.isReasonForChangeRequired()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_REASON_FOR_CHANGE_EMPTY,
							CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(inputField)));
				}
			} else {
				if (ecrfFieldValueIn.getReasonForChange() != null) { // hidden form field yields null.. or not ?
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_REASON_FOR_CHANGE_NOT_NULL,
							CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(inputField)));
				}
			}
		}
		if ((new EcrfFieldValueCollisionFinder(this.getProbandListEntryDao(), this.getECRFFieldValueDao())).collides(ecrfFieldValueIn)) {
			if (ecrfFieldValueIn.getIndex() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_ALREADY_EXISTS,
						CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(inputField)));
			} else {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_ALREADY_EXISTS,
						CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(inputField)), ecrfFieldValueIn.getIndex());
			}
		}
	}

	private void checkEcrfFieldValueInputUnlockedForFieldStatus(ECRFFieldValueInVO ecrfFieldValueIn, ECRFStatusEntry statusEntry, Visit visit, ECRFField ecrfField)
			throws Exception {
		if (statusEntry.getStatus().isValueLockdown()) {
			if (ecrfFieldValueIn.getId() == null && ecrfField.isSeries()
					&& getEcrfSectionUnlockValue(statusEntry.getListEntry().getId(), ecrfField.getEcrf().getId(), visit != null ? visit.getId() : null,
							ecrfField.getSection(), ecrfFieldValueIn.getIndex())) {
				return;
			} else {
				ECRFFieldStatusQueue[] queues = ECRFFieldStatusQueue.values();
				for (int i = 0; i < queues.length; i++) {
					ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(queues[i], statusEntry.getListEntry().getId(),
							visit != null ? visit.getId() : null, ecrfField.getId(),
							ecrfFieldValueIn.getIndex());
					if (lastStatus != null && lastStatus.getStatus().isUnlockValue()) {
						return;
					}
				}
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_LOCKED_FOR_ECRF_STATUS,
						L10nUtil.getEcrfStatusTypeName(Locales.USER, statusEntry.getStatus().getNameL10nKey()));
			}
		}
	}

	private void checkEcrfInput(ECRFInVO ecrfIn) throws ServiceException {
		if (ecrfIn.getName() != null && !ecrfIn.getName().trim().equals(ecrfIn.getName())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.WHITESPACE_ECRF_NAME, ecrfIn.getName());
		}
		if (ecrfIn.getRevision() != null && !ecrfIn.getRevision().trim().equals(ecrfIn.getRevision())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.WHITESPACE_ECRF_REVISION, ecrfIn.getRevision());
		}
		Trial trial = CheckIDUtil.checkTrialId(ecrfIn.getTrialId(), this.getTrialDao());
		Collection<Long> groupIds = ecrfIn.getGroupIds();
		if (groupIds != null && groupIds.size() > 0) {
			Iterator<Long> it = groupIds.iterator();
			HashSet<Long> dupeCheck = new HashSet<Long>(groupIds.size());
			while (it.hasNext()) {
				Long id = it.next();
				if (id == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_PROBAND_GROUP_ID_IS_NULL);
				}
				ProbandGroup group = CheckIDUtil.checkProbandGroupId(id, this.getProbandGroupDao());
				if (!dupeCheck.add(group.getId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_DUPLICATE_PROBAND_GROUP, group.getTitle());
				}
				if (!trial.equals(group.getTrial())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_WRONG_PROBAND_GROUP, group.getTitle(),
							CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
				}
			}
		}
		Collection<Long> visitIds = ecrfIn.getVisitIds();
		if (visitIds != null && visitIds.size() > 0) {
			Iterator<Long> it = visitIds.iterator();
			HashSet<Long> dupeCheck = new HashSet<Long>(visitIds.size());
			while (it.hasNext()) {
				Long id = it.next();
				if (id == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_VISIT_ID_IS_NULL);
				}
				Visit visit = CheckIDUtil.checkVisitId(id, this.getVisitDao());
				if (!dupeCheck.add(visit.getId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_DUPLICATE_VISIT, visit.getTitle());
				}
				if (!trial.equals(visit.getTrial())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_WRONG_VISIT, visit.getTitle(),
							CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
				}
			}
		}
		if (ecrfIn.getProbandListStatusId() != null) {
			CheckIDUtil.checkProbandListStatusTypeId(ecrfIn.getProbandListStatusId(), this.getProbandListStatusTypeDao());
		}
		ServiceUtil.checkTrialLocked(trial);
		if ((new EcrfNameRevisionCollisionFinder(this.getTrialDao(), this.getECRFDao())).collides(ecrfIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_NAME_REVISION_NOT_UNIQUE, ecrfIn.getName(), ecrfIn.getRevision());
		}
		if (ecrfIn.getCharge() < 0.0f) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_CHARGE_NEGATIVE);
		}
	}

	private ECRFStatusEntry checkEcrfStatusEntry(Long probandListEntryId, Long ecrfId, Long visitId) throws ServiceException {
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		Visit visit = null;
		if (visitId != null) {
			visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		ECRFStatusEntry statusEntry = this.getECRFStatusEntryDao().findByListEntryEcrfVisit(probandListEntryId, ecrfId, visitId);
		if (statusEntry == null) {
			ProbandListEntryOutVO listEntryVO = this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry);
			ServiceException e = L10nUtil.initServiceException(ServiceExceptionCodes.NO_ECRF_STATUS_ENTRY,
					ECRFDaoImpl.getUniqueEcrfName(this.getECRFDao().toECRFOutVO(ecrf), this.getVisitDao().toVisitOutVO(visit)),
					CommonUtil.trialOutVOToString(listEntryVO.getTrial()), Long.toString(listEntryVO.getProband().getId()));
			e.setLogError(false);
			throw e;
		}
		return statusEntry;
	}

	private void checkInquiryInput(InquiryInVO inquiryIn) throws ServiceException {
		if (CommonUtil.isEmptyString(inquiryIn.getJsVariableName())) {
			if (!CommonUtil.isEmptyString(JavaScriptCompressor.compress(inquiryIn.getJsValueExpression()))) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_JS_VARIABLE_NAME_REQUIRED);
			}
			if (!CommonUtil.isEmptyString(JavaScriptCompressor.compress(inquiryIn.getJsOutputExpression()))) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_JS_VARIABLE_NAME_REQUIRED);
			}
		} else {
			if (!JS_VARIABLE_NAME_REGEXP.matcher(inquiryIn.getJsVariableName()).find()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_JS_VARIABLE_NAME_INVALID, inquiryIn.getJsVariableName());
			}
			if ((new InquiryJsVariableNameCollisionFinder(this.getTrialDao(), this.getInquiryDao())).collides(inquiryIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_JS_VARIABLE_NAME_NOT_UNIQUE);
			}
		}
		if ((new InquiryPositionCollisionFinder(this.getTrialDao(), this.getInquiryDao())).collides(inquiryIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_POSITION_NOT_UNIQUE);
		}
	}

	private void checkMissingEcrfFieldValues(ProbandListEntry listEntry, ECRF ecrf, Visit visit) throws Exception {
		ECRFProgressVO ecrfProgress = ServiceUtil.populateEcrfProgress(this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry), this.getECRFDao().toECRFOutVO(ecrf),
				this.getVisitDao().toVisitOutVO(visit),
				false, false, null, null,
				this.getECRFStatusEntryDao(), this.getECRFStatusTypeDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao(),
				this.getVisitScheduleItemDao());
		if (ecrfProgress.getMandatoryFieldCount() > ecrfProgress.getMandatorySavedValueCount()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_MISSING, ecrfProgress.getMandatorySavedValueCount(), ecrfProgress.getMandatoryFieldCount());
		}
	}

	private void checkMissingEcrfFieldValuesDeeply(ProbandListEntry listEntry, ECRF ecrf, Visit visit) throws Exception {
		checkMissingEcrfFieldValues(listEntry, ecrf, visit);
		Integer maxMissingCount = Settings.getIntNullable(SettingCodes.MAX_MISSING_ECRF_FIELD_VALUES, Bundle.SETTINGS, DefaultSettings.MAX_MISSING_ECRF_FIELD_VALUES);
		if (maxMissingCount == null || maxMissingCount > 0) {
			ServiceException firstException = null;
			HashMap<Long, HashMap<Long, String>> errorMessagesMap = new HashMap<Long, HashMap<Long, String>>();
			int missingCount = 0;
			InputFieldDao inputFieldDao = this.getInputFieldDao();
			Iterator<Map> ecrfFieldValuesIt = this.getECRFFieldValueDao()
					.findByListEntryEcrfVisitJsField(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null, true, null, null, null).iterator();
			while (ecrfFieldValuesIt.hasNext() && (maxMissingCount == null || missingCount < maxMissingCount)) {
				Map<String, Object> entities = ecrfFieldValuesIt.next();
				ECRFFieldValue ecrfFieldValue = (ECRFFieldValue) entities.get(ServiceUtil.ECRF_FIELD_VALUE_DAO_ECRF_FIELD_VALUE_ALIAS);
				ECRFField ecrfField;
				if (ecrfFieldValue == null) {
					ecrfField = (ECRFField) entities.get(ServiceUtil.ECRF_FIELD_VALUE_DAO_ECRF_FIELD_ALIAS);
					if (!ecrfField.isSeries() && !ecrfField.isOptional()) {
						try {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_MISSING,
									CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
						} catch (ServiceException e) {
							missingCount++;
							if (firstException == null) {
								firstException = e;
							}
							if (errorMessagesMap.containsKey(ecrfField.getId())) {
								errorMessagesMap.get(ecrfField.getId()).put(null, e.getMessage());
							} else {
								HashMap<Long, String> indexErrorMap = new HashMap<Long, String>();
								indexErrorMap.put(null, e.getMessage());
								errorMessagesMap.put(ecrfField.getId(), indexErrorMap);
							}
						}
					}
				} else {
					ecrfField = ecrfFieldValue.getEcrfField();
					InputField inputField = ecrfField.getField();
					InputFieldValue value = ecrfFieldValue.getValue();
					try {
						ServiceUtil.checkInputFieldTextValue(inputField, ecrfField.isOptional(), value.getStringValue(), inputFieldDao,
								this.getInputFieldSelectionSetValueDao());
						ServiceUtil.checkInputFieldBooleanValue(inputField, ecrfField.isOptional(), value.getBooleanValue() == null ? false
								: value
										.getBooleanValue()
										.booleanValue(),
								inputFieldDao);
						ServiceUtil.checkInputFieldLongValue(inputField, ecrfField.isOptional(), value.getLongValue(), inputFieldDao);
						ServiceUtil.checkInputFieldFloatValue(inputField, ecrfField.isOptional(), value.getFloatValue(), inputFieldDao);
						ServiceUtil.checkInputFieldDateValue(inputField, ecrfField.isOptional(), value.getDateValue(), inputFieldDao);
						ServiceUtil.checkInputFieldTimeValue(inputField, ecrfField.isOptional(), value.getTimeValue(), inputFieldDao);
						ServiceUtil.checkInputFieldTimestampValue(inputField, ecrfField.isOptional(), value.getTimestampValue(), inputFieldDao);
						ServiceUtil.checkInputFieldInkValue(inputField, ecrfField.isOptional(), value.getInkValue(), inputFieldDao);
						ServiceUtil.checkInputFieldSelectionSetValues(inputField, ecrfField.isOptional(),
								ServiceUtil.toInputFieldSelectionSetValueIdCollection(value.getSelectionValues()), inputFieldDao,
								this.getInputFieldSelectionSetValueDao());
					} catch (ServiceException e) {
						missingCount++;
						if (firstException == null) {
							firstException = e;
						}
						if (errorMessagesMap.containsKey(ecrfField.getId())) {
							errorMessagesMap.get(ecrfField.getId()).put(ecrfField.isSeries() ? ecrfFieldValue.getIndex() : null, e.getMessage());
						} else {
							HashMap<Long, String> indexErrorMap = new HashMap<Long, String>();
							indexErrorMap.put(ecrfField.isSeries() ? ecrfFieldValue.getIndex() : null, e.getMessage());
							errorMessagesMap.put(ecrfField.getId(), indexErrorMap);
						}
					}
				}
			}
			if (firstException != null) {
				firstException.setData(errorMessagesMap);
				throw firstException;
			}
		}
	}

	private void checkProbandGroupInput(ProbandGroupInVO probandGroupIn) throws ServiceException {
		Trial trial = CheckIDUtil.checkTrialId(probandGroupIn.getTrialId(), this.getTrialDao());
		ServiceUtil.checkTrialLocked(trial);
		ServiceUtil.checkProbandGroupToken(probandGroupIn.getToken());
		Randomization.checkProbandGroupInput(trial, probandGroupIn);
		if ((new ProbandGroupTitleCollisionFinder(this.getTrialDao(), this.getProbandGroupDao())).collides(probandGroupIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GROUP_TITLE_ALREADY_EXISTS);
		}
		if ((new ProbandGroupTokenCollisionFinder(this.getTrialDao(), this.getProbandGroupDao())).collides(probandGroupIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GROUP_TOKEN_ALREADY_EXISTS);
		}
	}

	private void checkProbandListEntryInput(ProbandListEntryInVO probandListEntryIn, boolean createProband, boolean signup, Timestamp now) throws ServiceException {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(probandListEntryIn.getTrialId(), trialDao, LockMode.PESSIMISTIC_WRITE); // no position collision check for signup
		ServiceUtil.checkTrialLocked(trial);
		ProbandGroup group = null;
		if (probandListEntryIn.getGroupId() != null) {
			group = CheckIDUtil.checkProbandGroupId(probandListEntryIn.getGroupId(), this.getProbandGroupDao());
			if (!trial.equals(group.getTrial())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_WRONG_PROBAND_GROUP, CommonUtil.trialOutVOToString(trialDao.toTrialOutVO(trial)));
			}
		}
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		if (createProband) {
			if (probandListEntryIn.getProbandId() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_PROBAND_ID_NOT_NULL);
			}
		} else {
			ProbandDao probandDao = this.getProbandDao();
			Proband proband = CheckIDUtil.checkProbandId(probandListEntryIn.getProbandId(), probandDao);
			ServiceUtil.checkProbandLocked(proband);
			if (proband.isPerson() && !trial.getType().isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_PROBAND_NOT_ANIMAL, proband.getId().toString());
			} else if (!proband.isPerson() && trial.getType().isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_PROBAND_NOT_PERSON, proband.getId().toString());
			}
			if ((new ProbandListEntryProbandCollisionFinder(trialDao, probandListEntryDao)).collides(probandListEntryIn)) {
				throw L10nUtil
						.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_ALREADY_PARTICIPATING, proband.getId().toString());
			}
			if (now != null && (new ProbandListEntryStatusCollisionFinder(probandDao, trialDao, probandListEntryDao, now)).collides(probandListEntryIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_PROBAND_BLOCKED, proband.getId().toString());
			}
		}
		if (signup) {
			if (probandListEntryIn.getPosition() != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_POSITION_NOT_NULL);
			}
		} else {
			if (probandListEntryIn.getPosition() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_POSITION_REQUIRED);
			}
			if ((new ProbandListEntryPositionCollisionFinder(trialDao, probandListEntryDao)).collides(probandListEntryIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_POSITION_NOT_UNIQUE);
			}
		}
		if (probandListEntryIn.getRatingMax() != null) {
			if (probandListEntryIn.getRatingMax() <= 0l) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_RATING_MAX_LESS_THAN_OR_EQUAL_ZERO);
			} else if (probandListEntryIn.getRating() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_RATING_REQUIRED);
			} else {
				if (probandListEntryIn.getRating() < 0l) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_RATING_LESS_THAN_ZERO);
				} else if (probandListEntryIn.getRating() > probandListEntryIn.getRatingMax()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_RATING_GREATER_THAN_RATING_MAX);
				}
			}
		} else if (probandListEntryIn.getRating() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_RATING_NOT_NULL);
		}
	}

	private void checkProbandListEntryTagInput(ProbandListEntryTagInVO listTagIn, Trial trial, InputField field) throws ServiceException {
		if (CommonUtil.isEmptyString(listTagIn.getJsVariableName())) {
			if (!CommonUtil.isEmptyString(JavaScriptCompressor.compress(listTagIn.getJsValueExpression()))) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_JS_VARIABLE_NAME_REQUIRED);
			}
			if (!CommonUtil.isEmptyString(JavaScriptCompressor.compress(listTagIn.getJsOutputExpression()))) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_JS_VARIABLE_NAME_REQUIRED);
			}
		} else {
			if (!JS_VARIABLE_NAME_REGEXP.matcher(listTagIn.getJsVariableName()).find()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_JS_VARIABLE_NAME_INVALID, listTagIn.getJsVariableName());
			}
			if ((new ProbandListEntryTagJsVariableNameCollisionFinder(this.getTrialDao(), this.getProbandListEntryTagDao())).collides(listTagIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_JS_VARIABLE_NAME_NOT_UNIQUE);
			}
		}
		if ((new ProbandListEntryTagPositionCollisionFinder(this.getTrialDao(), this.getProbandListEntryTagDao())).collides(listTagIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_POSITION_NOT_UNIQUE);
		}
		Randomization.checkProbandListEntryTagInput(trial, field, listTagIn, this.getTrialDao(), this.getProbandGroupDao(), this.getProbandListEntryDao(),
				this.getStratificationRandomizationListDao(), this.getProbandListEntryTagDao(), this.getInputFieldSelectionSetValueDao(), this.getProbandListEntryTagValueDao(),
				this.getRandomizationListCodeDao());
	}

	private void checkProbandListEntryTagValueInput(ProbandListEntryTagValueInVO probandListEntryTagValueIn, ProbandListEntry listEntry, ProbandListEntryTag listEntryTag)
			throws ServiceException {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = listEntryTag.getField();
		inputFieldDao.lock(inputField, LockMode.PESSIMISTIC_WRITE);
		ServiceUtil.checkInputFieldTextValue(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getTextValue(), inputFieldDao,
				this.getInputFieldSelectionSetValueDao());
		ServiceUtil.checkInputFieldBooleanValue(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getBooleanValue(), inputFieldDao);
		ServiceUtil.checkInputFieldLongValue(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getLongValue(), inputFieldDao);
		ServiceUtil.checkInputFieldFloatValue(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getFloatValue(), inputFieldDao);
		ServiceUtil.checkInputFieldDateValue(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getDateValue(), inputFieldDao);
		ServiceUtil.checkInputFieldTimeValue(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getTimeValue(), inputFieldDao);
		ServiceUtil.checkInputFieldTimestampValue(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getTimestampValue(), inputFieldDao);
		ServiceUtil.checkInputFieldInkValue(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getInkValues(), inputFieldDao);
		ServiceUtil.checkInputFieldSelectionSetValues(inputField, listEntryTag.isOptional(), probandListEntryTagValueIn.getSelectionValueIds(), inputFieldDao,
				this.getInputFieldSelectionSetValueDao());
		if ((new ProbandListEntryTagValueCollisionFinder(this.getProbandListEntryDao(), this.getProbandListEntryTagValueDao())).collides(probandListEntryTagValueIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_VALUE_ALREADY_EXISTS,
					CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(inputField)));
		}
	}

	private ArrayList<RandomizationListCodeInVO> checkStratificationRandomizationListInput(StratificationRandomizationListInVO randomizationListIn,
			Collection<RandomizationListCodeInVO> codes, boolean checkCollision) throws ServiceException {
		Trial trial = CheckIDUtil.checkTrialId(randomizationListIn.getTrialId(), this.getTrialDao(), LockMode.PESSIMISTIC_WRITE);
		ServiceUtil.checkTrialLocked(trial);
		return Randomization.checkStratificationRandomizationListInput(trial, randomizationListIn, codes, checkCollision,
				this.getTrialDao(), this.getProbandGroupDao(), this.getProbandListEntryDao(),
				this.getStratificationRandomizationListDao(), this.getProbandListEntryTagDao(), this.getInputFieldSelectionSetValueDao(), this.getProbandListEntryTagValueDao(),
				this.getRandomizationListCodeDao());
	}

	private void checkTeamMemberInput(TeamMemberInVO teamMemberIn) throws ServiceException {
		Staff staff = CheckIDUtil.checkStaffId(teamMemberIn.getStaffId(), this.getStaffDao());
		if (teamMemberIn.getSign()) {
			if (!teamMemberIn.getAccess()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TEAM_MEMBER_ACCESS_FALSE);
			}
			if (!staff.isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TEAM_MEMBER_SIGNEE_NOT_PERSON);
			}
		}
		if (teamMemberIn.getResolve()) {
			if (!teamMemberIn.getAccess()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TEAM_MEMBER_ACCESS_FALSE);
			}
			if (!staff.isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TEAM_MEMBER_RESOLVER_NOT_PERSON);
			}
		}
		if (teamMemberIn.getVerify()) {
			if (!teamMemberIn.getAccess()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TEAM_MEMBER_ACCESS_FALSE);
			}
			if (!staff.isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TEAM_MEMBER_VERIFIER_NOT_PERSON);
			}
		}
		(new TeamMemberRoleTagAdapter(this.getTrialDao(), this.getTeamMemberRoleDao())).checkTagValueInput(teamMemberIn);
		if ((new TeamMemberRoleCollisionFinder(this.getTrialDao(), this.getTeamMemberDao())).collides(teamMemberIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.TEAM_MEMBER_ALREADY_MEMBER_WITH_SAME_ROLE, staff.getId().toString());
		}
	}

	private void checkTeamMemberResolve(Trial trial, User user) throws ServiceException {
		Staff identity = user.getIdentity();
		if (identity == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.RESOLVER_NO_ACTIVE_IDENTITY);
		}
		Iterator<TeamMember> it = this.getTeamMemberDao().findByTrialStaffRole(trial.getId(), identity.getId(), null, null, null).iterator();
		while (it.hasNext()) {
			if (it.next().isResolve()) {
				return;
			}
		}
		throw L10nUtil.initServiceException(ServiceExceptionCodes.RESOLVER_NO_RESOLVE_TEAM_MEMBER);
	}

	private void checkTeamMemberSign(Trial trial, User user) throws ServiceException {
		Staff identity = user.getIdentity();
		if (identity == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.SIGNEE_NO_ACTIVE_IDENTITY);
		}
		Iterator<TeamMember> it = this.getTeamMemberDao().findByTrialStaffRole(trial.getId(), identity.getId(), null, null, null).iterator();
		while (it.hasNext()) {
			if (it.next().isSign()) {
				return;
			}
		}
		throw L10nUtil.initServiceException(ServiceExceptionCodes.SIGNEE_NO_SIGN_TEAM_MEMBER);
	}

	private void checkTeamMemberVerify(Trial trial, User user) throws ServiceException {
		Staff identity = user.getIdentity();
		if (identity == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VERIFIER_NO_ACTIVE_IDENTITY);
		}
		Iterator<TeamMember> it = this.getTeamMemberDao().findByTrialStaffRole(trial.getId(), identity.getId(), null, null, null).iterator();
		while (it.hasNext()) {
			if (it.next().isVerify()) {
				return;
			}
		}
		throw L10nUtil.initServiceException(ServiceExceptionCodes.VERIFIER_NO_VERIFY_TEAM_MEMBER);
	}

	private void checkTimelineEventInput(TimelineEventInVO timelineEventIn) throws ServiceException {
		(new TimelineEventTypeTagAdapter(this.getTrialDao(), this.getTimelineEventTypeDao(), this.getTimelineEventDao())).checkTagValueInput(timelineEventIn);
	}

	private void checkTrialInput(TrialInVO trialIn) throws ServiceException {
		CheckIDUtil.checkDepartmentId(trialIn.getDepartmentId(), this.getDepartmentDao());
		CheckIDUtil.checkTrialTypeId(trialIn.getTypeId(), this.getTrialTypeDao());
		CheckIDUtil.checkSponsoringTypeId(trialIn.getSponsoringId(), this.getSponsoringTypeDao());
		CheckIDUtil.checkSurveyStatusTypeId(trialIn.getSurveyStatusId(), this.getSurveyStatusTypeDao());
		if (trialIn.getExclusiveProbands()) {
			if (trialIn.getBlockingPeriod() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_BLOCKING_PERIOD_REQUIRED);
			} else if (VariablePeriod.EXPLICIT.equals(trialIn.getBlockingPeriod())) {
				if (trialIn.getBlockingPeriodDays() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_BLOCKING_PERIOD_EXPLICIT_DAYS_REQUIRED);
				} else if (trialIn.getBlockingPeriodDays() < 1) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_BLOCKING_PERIOD_EXPLICIT_DAYS_LESS_THAN_ONE);
				}
			}
		}
		if (!CommonUtil.isEmptyString(trialIn.getProbandAliasFormat())) {
			try {
				Format[] argFormats = (new MessageFormat(trialIn.getProbandAliasFormat())).getFormatsByArgumentIndex();
				if (argFormats.length != PROBAND_ALIAS_FORMAT_MAX_ALIAS_0BASED_INDEX + 1
						&& argFormats.length != PROBAND_ALIAS_FORMAT_MAX_ALIAS_1BASED_INDEX + 1
						&& argFormats.length != PROBAND_ALIAS_FORMAT_PROBAND_COUNT_0BASED_INDEX + 1
						&& argFormats.length != PROBAND_ALIAS_FORMAT_PROBAND_COUNT_1BASED_INDEX + 1) {
					throw new IllegalArgumentException();
				}
			} catch (IllegalArgumentException e) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_MALFORMED_PROBAND_ALIAS_PATTERN);
			}
		}
		if ((trialIn.getSignupProbandList() || trialIn.getSignupInquiries()) && CommonUtil.isEmptyString(trialIn.getSignupDescription())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_SIGNUP_DESCRIPTION_EMPTY);
		}
		if (trialIn.getSignupRandomize()) {
			if (!trialIn.getSignupProbandList()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_SIGNUP_PROBAND_LIST_DISABLED);
			}
			if (trialIn.getRandomization() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_SIGNUP_RANDOMIZATION_NOT_DEFINED);
			}
		}
	}

	private String getNewEcrfRevision(Long trialId, String ecrfName, String revisionFormat) throws Exception {
		try {
			MessageFormat likeFormat = new MessageFormat(CommonUtil.escapeSqlLikeWildcards(revisionFormat));
			likeFormat.setFormatByArgumentIndex(0, null);
			String likePattern = likeFormat.format(
					new Object[] {
							CommonUtil.SQL_LIKE_PERCENT_WILDCARD, // {0}
					},
					new StringBuffer(),
					null).toString();
			long maxRevision = 1l;
			ECRF maxRevisionEcrf = this.getECRFDao().findByMaxRevision(trialId, ecrfName, likePattern);
			if (maxRevisionEcrf != null) {
				maxRevision = parseFormatLongAndIncrement(maxRevisionEcrf.getRevision(), revisionFormat, 0, maxRevision);
			}
			String revision = MessageFormat.format(revisionFormat,
					maxRevision // {0}
			);
			if (this.getECRFDao().findCollidingTrialNameRevision(trialId, ecrfName, revision).size() > 0l) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_NAME_REVISION_NOT_UNIQUE, ecrfName, revision);
			}
			return revision;
		} catch (IllegalArgumentException e) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_MALFORMED_REVISION_PATTERN);
		}
	}

	private String getNewProbandAlias(Trial trial, User user) throws Exception {
		try {
			MessageFormat likeFormat = new MessageFormat(CommonUtil.escapeSqlLikeWildcards(trial.getProbandAliasFormat()));
			likeFormat.setFormatByArgumentIndex(PROBAND_ALIAS_FORMAT_MAX_ALIAS_0BASED_INDEX, null);
			likeFormat.setFormatByArgumentIndex(PROBAND_ALIAS_FORMAT_MAX_ALIAS_1BASED_INDEX, null);
			likeFormat.setFormatByArgumentIndex(PROBAND_ALIAS_FORMAT_PROBAND_COUNT_0BASED_INDEX, null);
			likeFormat.setFormatByArgumentIndex(PROBAND_ALIAS_FORMAT_PROBAND_COUNT_1BASED_INDEX, null);
			String likePattern = likeFormat.format(
					new Object[] {
							CommonUtil.escapeSqlLikeWildcards(user.getDepartment().getNameL10nKey()), // {0}
							null, // {1}
							null, // {2}
							null, // {3}
							null, // {4}
							null, // {5}
							CommonUtil.SQL_LIKE_PERCENT_WILDCARD, // {6}
							CommonUtil.SQL_LIKE_PERCENT_WILDCARD, // {7}
							CommonUtil.SQL_LIKE_PERCENT_WILDCARD, // {8}
							CommonUtil.SQL_LIKE_PERCENT_WILDCARD // {9}
					},
					new StringBuffer(),
					null).toString();
			long count0based = this.getProbandDao().getCountByAlias(trial.getType().isPerson(), likePattern);
			long count1based = count0based + 1l;
			long maxAlias0based = 0l;
			long maxAlias1based = 1l;
			Proband maxAliasProband = this.getProbandDao().findByMaxAlias(trial.getType().isPerson(), likePattern);
			if (maxAliasProband != null) {
				String alias;
				if (maxAliasProband.isPerson()) {
					alias = maxAliasProband.getPersonParticulars().getAlias();
				} else {
					alias = maxAliasProband.getAnimalParticulars().getAlias();
				}
				maxAlias0based = parseFormatLongAndIncrement(alias, trial.getProbandAliasFormat(),
						PROBAND_ALIAS_FORMAT_MAX_ALIAS_0BASED_INDEX, maxAlias0based);
				maxAlias1based = parseFormatLongAndIncrement(alias, trial.getProbandAliasFormat(),
						PROBAND_ALIAS_FORMAT_MAX_ALIAS_1BASED_INDEX, maxAlias1based);
			}
			String alias = MessageFormat.format(trial.getProbandAliasFormat(),
					user.getDepartment().getNameL10nKey(), // {0}
					null, // {1}
					null, // {2}
					null, // {3}
					null, // {4}
					null, // {5}
					maxAlias0based, // {6}
					maxAlias1based, // {7}
					count0based, // {8}
					count1based); // {9}
			if (this.getProbandDao().getCountByAlias(trial.getType().isPerson(), CommonUtil.escapeSqlLikeWildcards(alias)) > 0l) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_PROBAND_ALIAS_ALREADY_EXISTS, alias);
			}
			return alias;
		} catch (IllegalArgumentException e) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_MALFORMED_PROBAND_ALIAS_PATTERN);
		}
	}

	private static long parseFormatLongAndIncrement(String input, String format, int index, long defaultValue) {
		MessageFormat messageFormat = new MessageFormat(format);
		Object[] args = messageFormat.parse(input, new ParsePosition(0));
		long result;
		try {
			result = (Long) args[index];
		} catch (Exception e1) {
			try {
				result = Long.parseLong((String) args[index]);
			} catch (Exception e2) {
				return defaultValue;
			}
		}
		result += 1l;
		return result;
	}

	private void checkTrialTagValueInput(TrialTagValueInVO tagValueIn) throws ServiceException {
		(new TrialTagAdapter(this.getTrialDao(), this.getTrialTagDao())).checkTagValueInput(tagValueIn);
	}

	private void checkUnresolvedEcrfFieldStatus(ProbandListEntry listEntry, ECRF ecrf, Visit visit) throws Exception {
		ECRFProgressVO ecrfProgress = ServiceUtil.populateEcrfProgress(
				this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry),
				this.getECRFDao().toECRFOutVO(ecrf),
				this.getVisitDao().toVisitOutVO(visit),
				false, false, null, null,
				this.getECRFStatusEntryDao(), this.getECRFStatusTypeDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao(),
				this.getVisitScheduleItemDao());
		Iterator<ECRFFieldStatusQueueCountVO> it = ecrfProgress.getEcrfFieldStatusQueueCounts().iterator();
		long unresolved = 0l;
		while (it.hasNext()) {
			unresolved += it.next().getUnresolved();
		}
		if (unresolved > 0l) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.UNRESOLVED_ECRF_FIELD_ISSUES, unresolved);
		}
	}

	private void checkUpdateEcrfFieldInput(ECRFField originalEcrfField, ECRFFieldInVO ecrfFieldIn) throws ServiceException {
		InputField field = CheckIDUtil.checkInputFieldId(ecrfFieldIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(ecrfFieldIn.getTrialId(), this.getTrialDao());
		if (!trial.equals(originalEcrfField.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_TRIAL_CHANGED);
		}
		ServiceUtil.checkTrialLocked(trial);
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfFieldIn.getEcrfId(), this.getECRFDao());
		if (!trial.equals(ecrf.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_WRONG_ECRF, CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
		}
		checkEcrfFieldInput(ecrfFieldIn); // lock
		boolean sectionChanged = this.getECRFFieldDao().getCount(ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection(), ecrfFieldIn.getId()) == 0l;
		if (!ecrf.equals(originalEcrfField.getEcrf())
				|| sectionChanged
				|| originalEcrfField.isSeries() != ecrfFieldIn.getSeries()
				|| !field.equals(originalEcrfField.getField())
				|| originalEcrfField.getPosition() != ecrfFieldIn.getPosition()) {
			ServiceUtil.checkLockedEcrfs(ecrf, this.getECRFStatusEntryDao(), this.getECRFDao());
		}
		long valueCount = this.getECRFFieldValueDao().getCount(ecrfFieldIn.getId(), false);
		if (!field.equals(originalEcrfField.getField()) && valueCount > 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_INPUT_FIELD_CHANGED);
		}
		long statusEntryCount = this.getECRFFieldStatusEntryDao().getCount(ecrfFieldIn.getId(), false);
		if (originalEcrfField.isSeries() != ecrfFieldIn.getSeries()) {
			if (valueCount > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_WITH_VALUES_SERIES_FLAG_CHANGED);
			}
			if (statusEntryCount > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_WITH_STATUS_ENTRIES_SERIES_FLAG_CHANGED);
			}
		}
		if (ecrfFieldIn.getSeries() && sectionChanged) {
			if (valueCount > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_WITH_VALUES_SECTION_CHANGED);
			}
			if (statusEntryCount > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_WITH_STATUS_ENTRIES_SECTION_CHANGED);
			}
			if (this.getECRFFieldValueDao().getCount(ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection()) > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_SECTION_WITH_VALUES, ecrfFieldIn.getSection());
			}
			if (this.getECRFFieldStatusEntryDao().getCount(ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection()) > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_SECTION_WITH_STATUS_ENTRIES, ecrfFieldIn.getSection());
			}
		}
	}

	private ArrayList<ECRFStatusEntry> checkUpdateEcrfInput(ECRFInVO modifiedEcrf, ECRF originalEcrf) throws ServiceException {
		checkEcrfInput(modifiedEcrf);
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ArrayList<ECRFStatusEntry> ecrfStatusEntriesToRemove = new ArrayList<ECRFStatusEntry>();
		Iterator<Visit> it = this.getVisitDao().findByEcrfStatusEntry(modifiedEcrf.getId()).iterator();
		while (it.hasNext()) {
			Visit visit = it.next();
			if (!modifiedEcrf.getVisitIds().contains(visit.getId())) {
				ServiceUtil.checkLockedEcrfs(originalEcrf, visit, ecrfStatusEntryDao, this.getECRFDao(), this.getVisitDao());
				//delete ecrf status entries of empty ecrfs
				if (ecrfFieldValueDao.getCount(modifiedEcrf.getId(), visit.getId()) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_VISIT_WITH_VALUES,
							CommonUtil.getEcrfVisitName(modifiedEcrf.getName(), visit.getToken(), modifiedEcrf.getVisitIds().size()));
				} else if (ecrfFieldStatusEntryDao.getCount(modifiedEcrf.getId(), visit.getId()) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_VISIT_WITH_STATUS_ENTRIES,
							CommonUtil.getEcrfVisitName(modifiedEcrf.getName(), visit.getToken(), modifiedEcrf.getVisitIds().size()));
				} else {
					ecrfStatusEntriesToRemove.addAll(ecrfStatusEntryDao.findByTrialListEntryEcrfVisitValidationStatus(null, null, modifiedEcrf.getId(), visit.getId(), null, null));
				}
			}
		}
		if (!modifiedEcrf.getTrialId().equals(originalEcrf.getTrial().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_TRIAL_CHANGED);
		}
		if (!originalEcrf.getTitle().equals(modifiedEcrf.getTitle())) {
			ServiceUtil.checkLockedEcrfs(originalEcrf, this.getECRFStatusEntryDao(), this.getECRFDao());
		}
		return ecrfStatusEntriesToRemove;
	}

	private void checkUpdateEcrfStatusEntry(ECRFStatusEntry originalStatusEntry, ECRFStatusType statusType, Long version, User user) throws Exception {
		ProbandListEntry listEntry = originalStatusEntry.getListEntry();
		ServiceUtil.checkTrialLocked(listEntry.getTrial());
		if (!listEntry.getTrial().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_TRIAL,
					L10nUtil.getTrialStatusTypeName(Locales.USER, listEntry.getTrial().getStatus().getNameL10nKey()));
		}
		ServiceUtil.checkProbandLocked(listEntry.getProband());
		if (listEntry.getLastStatus() != null && !listEntry.getLastStatus().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
					L10nUtil.getProbandListStatusTypeName(Locales.USER, listEntry.getLastStatus().getStatus().getNameL10nKey()));
		}
		ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
		CoreUtil.getNewVersionChecked(originalStatusEntry, version.longValue());
		//visit cannot be changed.
		boolean validState = false;
		Iterator<ECRFStatusType> statesIt = ecrfStatusTypeDao.findTransitions(originalStatusEntry.getStatus().getId()).iterator();
		while (statesIt.hasNext()) {
			if (statusType.equals(statesIt.next())) {
				validState = true;
				break;
			}
		}
		if (!validState) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_NEW_ECRF_STATUS_TYPE, L10nUtil.getEcrfStatusTypeName(Locales.USER, statusType.getNameL10nKey()));
		}
		// entering signed:
		if (hasEcrfStatusAction(statusType, org.phoenixctms.ctsms.enumeration.ECRFStatusAction.SIGN_ECRF)) {
			checkTeamMemberSign(listEntry.getTrial(), user);
			// leaving signed:
		} else if (hasEcrfStatusAction(originalStatusEntry.getStatus(), org.phoenixctms.ctsms.enumeration.ECRFStatusAction.SIGN_ECRF)) {
			checkTeamMemberSign(listEntry.getTrial(), user);
			// entering verified:
		} else if (statusType.isVerified()) {
			checkTeamMemberVerify(listEntry.getTrial(), user);
			// leaving verified:
		} else if (originalStatusEntry.getStatus().isVerified()) {
			checkTeamMemberVerify(listEntry.getTrial(), user);
		}
	}

	private void checkUpdateInquiryInput(Inquiry originalInquiry, InquiryInVO inquiryIn) throws ServiceException {
		InputField field = CheckIDUtil.checkInputFieldId(inquiryIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(inquiryIn.getTrialId(), this.getTrialDao());
		if (!trial.equals(originalInquiry.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_TRIAL_CHANGED);
		}
		ServiceUtil.checkTrialLocked(trial);
		checkInquiryInput(inquiryIn);
		if (!field.equals(originalInquiry.getField()) && this.getInquiryValueDao().getCount(originalInquiry.getId()) > 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_INPUT_FIELD_CHANGED);
		}
	}

	private void checkUpdateProbandListEntryTagInput(ProbandListEntryTag originalProbandListEntryTag, ProbandListEntryTagInVO listTagIn) throws ServiceException {
		InputField field = CheckIDUtil.checkInputFieldId(listTagIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(listTagIn.getTrialId(), this.getTrialDao());
		if (!trial.equals(originalProbandListEntryTag.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_TRIAL_CHANGED);
		}
		ServiceUtil.checkTrialLocked(trial);
		checkProbandListEntryTagInput(listTagIn, trial, field);
		if (!field.equals(originalProbandListEntryTag.getField())) {
			if (this.getProbandListEntryTagValueDao().getCount(null, originalProbandListEntryTag.getId()) > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_INPUT_FIELD_CHANGED);
			}
			if (!InputFieldType.TIMESTAMP.equals(field.getFieldType())) {
				HashSet<VisitScheduleDateMode> modes = new HashSet<VisitScheduleDateMode>();
				modes.add(VisitScheduleDateMode.TAGS);
				Iterator<VisitScheduleItem> it = this.getVisitScheduleItemDao().findByTagsModes(null, listTagIn.getId(), modes).iterator();
				StringBuilder sb = new StringBuilder();
				boolean used = it.hasNext();
				while (it.hasNext()) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(this.getVisitScheduleItemDao().toVisitScheduleItemOutVO(it.next()).getName());
				}
				if (used) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_INPUT_FIELD_NOT_TIMESTAMP, sb.toString());
				}
				modes.add(VisitScheduleDateMode.TAG_DURATION);
				it = this.getVisitScheduleItemDao().findByTagsModes(listTagIn.getId(), null, modes).iterator();
				sb = new StringBuilder();
				used = it.hasNext();
				while (it.hasNext()) {
					if (sb.length() > 0) {
						sb.append(", ");
					}
					sb.append(this.getVisitScheduleItemDao().toVisitScheduleItemOutVO(it.next()).getName());
				}
				if (used) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_INPUT_FIELD_NOT_TIMESTAMP, sb.toString());
				}
			}
		}
	}

	private ArrayList<RandomizationListCodeInVO> checkUpdateTrialInput(Trial originalTrial, TrialInVO trialIn, Collection<RandomizationListCodeInVO> codes, User user)
			throws ServiceException {
		checkTrialInput(trialIn);
		TrialStatusTypeDao trialStatusTypeDao = this.getTrialStatusTypeDao();
		TrialStatusType state = CheckIDUtil.checkTrialStatusTypeId(trialIn.getStatusId(), trialStatusTypeDao);
		boolean validState = false;
		Iterator<TrialStatusType> statesIt = trialStatusTypeDao.findTransitions(originalTrial.getStatus().getId()).iterator();
		while (statesIt.hasNext()) {
			if (state.equals(statesIt.next())) {
				validState = true;
				break;
			}
		}
		if (!validState) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_NEW_TRIAL_STATUS_TYPE, L10nUtil.getTrialStatusTypeName(Locales.USER, state.getNameL10nKey()));
		}
		// entering signed:
		if (hasTrialStatusAction(state, org.phoenixctms.ctsms.enumeration.TrialStatusAction.SIGN_TRIAL)) {
			checkTeamMemberSign(originalTrial, user);
			// leaving signed:
		} else if (hasTrialStatusAction(originalTrial.getStatus(), org.phoenixctms.ctsms.enumeration.TrialStatusAction.SIGN_TRIAL)) {
			checkTeamMemberSign(originalTrial, user);
		}
		return Randomization.checkTrialInput(originalTrial, trialIn, codes, this.getTrialDao(), this.getProbandGroupDao(), this.getProbandListEntryDao(),
				this.getStratificationRandomizationListDao(), this.getProbandListEntryTagDao(), this.getInputFieldSelectionSetValueDao(), this.getProbandListEntryTagValueDao(),
				this.getRandomizationListCodeDao());
	}

	private void checkVisitInput(VisitInVO visitIn) throws ServiceException {
		(new VisitTypeTagAdapter(this.getTrialDao(), this.getVisitTypeDao())).checkTagValueInput(visitIn);
		ServiceUtil.checkVisitToken(visitIn.getToken());
		//restrict when linked to ecrf with locked status?
		if ((new VisitTitleCollisionFinder(this.getTrialDao(), this.getVisitDao())).collides(visitIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_TITLE_ALREADY_EXISTS);
		}
		if ((new VisitTokenCollisionFinder(this.getTrialDao(), this.getVisitDao())).collides(visitIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_TOKEN_ALREADY_EXISTS);
		}
		if (visitIn.getReimbursement() < 0.0f) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_REIMBURSEMENT_NEGATIVE);
		}
	}

	private void checkVisitScheduleItemInput(VisitScheduleItemInVO visitScheduleItemIn) throws ServiceException {
		Trial trial = CheckIDUtil.checkTrialId(visitScheduleItemIn.getTrialId(), this.getTrialDao());
		ProbandGroup group = null;
		if (visitScheduleItemIn.getGroupId() != null) {
			group = CheckIDUtil.checkProbandGroupId(visitScheduleItemIn.getGroupId(), this.getProbandGroupDao());
			if (!trial.equals(group.getTrial())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_WRONG_PROBAND_GROUP,
						CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
			}
		}
		Visit visit = null;
		if (visitScheduleItemIn.getVisitId() != null) {
			visit = CheckIDUtil.checkVisitId(visitScheduleItemIn.getVisitId(), this.getVisitDao());
			if (!trial.equals(visit.getTrial())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_WRONG_VISIT, CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
			}
		}
		ServiceUtil.checkTrialLocked(trial);
		if ((new VisitScheduleItemCollisionFinder(this.getTrialDao(), this.getVisitScheduleItemDao())).collides(visitScheduleItemIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_TOKEN_EXISTS_ALREADY, group == null ? null : group.getToken(), visit == null ? null
					: visit.getToken(), visitScheduleItemIn.getToken());
		}
		ProbandListEntryTag startTag = null;
		ProbandListEntryTag stopTag = null;
		switch (visitScheduleItemIn.getMode()) {
			case STATIC:
				if (visitScheduleItemIn.getStart() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TIMESTAMP_REQUIRED);
				}
				if (visitScheduleItemIn.getStop() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_END_TIMESTAMP_REQUIRED);
				}
				if (visitScheduleItemIn.getStop().compareTo(visitScheduleItemIn.getStart()) <= 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_END_TIMESTAMP_LESS_THAN_OR_EQUAL_TO_START_TIMESTAMP);
				}
				if (visitScheduleItemIn.getStartTagId() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TAG_NOT_NULL);
				}
				if (visitScheduleItemIn.getStopTagId() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_END_TAG_NOT_NULL);
				}
				if (visitScheduleItemIn.getOffsetSeconds() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_OFFSET_SECONDS_NOT_NULL);
				}
				if (visitScheduleItemIn.getDuration() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_DURATION_NOT_NULL);
				}
				break;
			case TAGS:
				if (visitScheduleItemIn.getStart() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TIMESTAMP_NOT_NULL);
				}
				if (visitScheduleItemIn.getStop() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_END_TIMESTAMP_NOT_NULL);
				}
				if (visitScheduleItemIn.getStartTagId() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TAG_REQUIRED);
				} else {
					startTag = CheckIDUtil.checkProbandListEntryTagId(visitScheduleItemIn.getStartTagId(), this.getProbandListEntryTagDao());
					if (!InputFieldType.TIMESTAMP.equals(startTag.getField().getFieldType())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TAG_FIELD_NOT_TIMESTAMP);
					}
					if (!trial.equals(startTag.getTrial())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_WRONG_START_TAG,
								CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
					}
				}
				if (visitScheduleItemIn.getStopTagId() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_STOP_TAG_REQUIRED);
				} else {
					stopTag = CheckIDUtil.checkProbandListEntryTagId(visitScheduleItemIn.getStopTagId(), this.getProbandListEntryTagDao());
					if (!InputFieldType.TIMESTAMP.equals(stopTag.getField().getFieldType())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_END_TAG_FIELD_NOT_TIMESTAMP);
					}
					if (!trial.equals(stopTag.getTrial())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_WRONG_END_TAG,
								CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
					}
				}
				if (startTag.equals(stopTag)) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TAG_EQUALS_END_TAG);
				}
				if (visitScheduleItemIn.getDuration() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_DURATION_NOT_NULL);
				}
				break;
			case TAG_DURATION:
				if (visitScheduleItemIn.getStart() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TIMESTAMP_NOT_NULL);
				}
				if (visitScheduleItemIn.getStop() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_END_TIMESTAMP_NOT_NULL);
				}
				if (visitScheduleItemIn.getStartTagId() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TAG_REQUIRED);
				} else {
					startTag = CheckIDUtil.checkProbandListEntryTagId(visitScheduleItemIn.getStartTagId(), this.getProbandListEntryTagDao());
					if (!InputFieldType.TIMESTAMP.equals(startTag.getField().getFieldType())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_START_TAG_FIELD_NOT_TIMESTAMP);
					}
					if (!trial.equals(startTag.getTrial())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_WRONG_START_TAG,
								CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
					}
				}
				if (visitScheduleItemIn.getStopTagId() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_END_TAG_NOT_NULL);
				}
				if (visitScheduleItemIn.getDuration() == null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_DURATION_REQUIRED);
				} else if (visitScheduleItemIn.getDuration() < 1) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_DURATION_LESS_THAN_ONE);
				}
				break;
			case STALE:
				//allow any nonsense.
				break;
			default:
				throw new IllegalArgumentException(
						L10nUtil.getMessage(MessageCodes.UNSUPPORTED_VISIT_SCHEDULE_DATE_MODE, DefaultMessages.UNSUPPORTED_VISIT_SCHEDULE_DATE_MODE,
								visitScheduleItemIn.getMode()));
		}
	}

	private ArrayList<ECRFFieldStatusEntryOutVO> clearEcrfFieldStatusEntries(ProbandListEntry listEntry, Collection<ECRFFieldStatusEntry> statusEntries,
			Timestamp now, User user) throws Exception {
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ArrayList<ECRFFieldStatusEntryOutVO> result = new ArrayList<ECRFFieldStatusEntryOutVO>(statusEntries.size());
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		NotificationDao notificationDao = this.getNotificationDao();
		NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
		Iterator<ECRFFieldStatusEntry> ecrfFieldStatusEntriesIt = statusEntries.iterator();
		while (ecrfFieldStatusEntriesIt.hasNext()) {
			ECRFFieldStatusEntry fieldStatus = ecrfFieldStatusEntriesIt.next();
			fieldStatus.getListEntry().removeEcrfFieldStatusEntries(fieldStatus);
			fieldStatus.getEcrfField().removeEcrfFieldStatusEntries(fieldStatus);
			result.add(ServiceUtil.removeEcrfFieldStatusEntry(fieldStatus, now, user,
					Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL),
					Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND),
					ecrfFieldStatusEntryDao, journalEntryDao, notificationDao, notificationRecipientDao));
		}
		return result;
	}

	private ArrayList<ECRFFieldValueOutVO> clearEcrfFieldValues(ProbandListEntry listEntry, Collection<ECRFFieldValue> values,
			Timestamp now, User user) throws Exception {
		ProbandListEntryOutVO listEntryVO = this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry);
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		InputFieldValueDao inputFieldValueDao = this.getInputFieldValueDao();
		ArrayList<ECRFFieldValueOutVO> result = new ArrayList<ECRFFieldValueOutVO>(values.size());
		Iterator<ECRFFieldValue> ecrfFieldValuesIt = values.iterator();
		while (ecrfFieldValuesIt.hasNext()) {
			ECRFFieldValue fieldValue = ecrfFieldValuesIt.next();
			result.add(ecrfFieldValueDao.toECRFFieldValueOutVO(fieldValue));
			fieldValue.getListEntry().removeEcrfValues(fieldValue);
			fieldValue.getEcrfField().removeFieldValues(fieldValue);
			ServiceUtil.removeEcrfFieldValue(fieldValue, now, user, false, false, inputFieldValueDao, ecrfFieldValueDao, journalEntryDao);
		}
		ServiceUtil.logSystemMessage(listEntry.getProband(), listEntryVO.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_VALUES_CLEARED, result,
				null, journalEntryDao);
		ServiceUtil.logSystemMessage(listEntry.getTrial(), listEntryVO.getProband(), now, user, SystemMessageCodes.ECRF_FIELD_VALUES_CLEARED, result, null,
				journalEntryDao);
		return result;
	}

	private ECRFProgressSummaryVO createEcrfProgessSummary(ProbandListEntryOutVO listEntryVO, boolean ecrfDetail, boolean sectionDetail, boolean dueDetail, Date from, Date to)
			throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		VisitDao visitDao = this.getVisitDao();
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		ECRFProgressSummaryVO result = new ECRFProgressSummaryVO();
		result.setListEntry(listEntryVO);
		result.setEcrfTotalCount(0l);
		result.setEcrfDoneCount(0l);
		result.setEcrfOverdueCount(0l);
		result.setCharge(0.0f);
		if (from == null && to == null) {
			result.setEcrfStatusEntryCount(ecrfStatusEntryDao.getCount(listEntryVO.getId(), null, null, null, null, null, null, null, null));
		}
		if (ecrfDetail) {
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			Iterator<ECRF> ecrfIt = ecrfDao.findByTrialGroupVisitActiveSorted(listEntryVO.getTrial().getId(),
					listEntryVO.getGroup() != null ? listEntryVO.getGroup().getId() : null, null, true, true, null).iterator();
			while (ecrfIt.hasNext()) {
				ECRF ecrf = ecrfIt.next();
				Iterator<Visit> visitIt = ecrf.getVisits().iterator();
				if (visitIt.hasNext()) {
					ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(ecrf);
					while (visitIt.hasNext()) {
						Visit visit = visitIt.next();
						ECRFProgressVO ecrfProgress = ServiceUtil.populateEcrfProgress(listEntryVO, ecrfVO, visitDao.toVisitOutVO(visit), dueDetail, sectionDetail,
								from, to,
								ecrfStatusEntryDao, ecrfStatusTypeDao, ecrfFieldDao, ecrfFieldValueDao, ecrfFieldStatusEntryDao, visitScheduleItemDao);
						if (ecrfProgress != null) {
							result.setEcrfTotalCount(result.getEcrfTotalCount() + 1l);
							if (ecrfProgress.getStatus() != null) {
								if (ecrfProgress.getStatus().getDone()) {
									result.setEcrfDoneCount(result.getEcrfDoneCount() + 1l);
								}
							}
							if (ecrfProgress.getOverdue()) {
								result.setEcrfOverdueCount(result.getEcrfOverdueCount() + 1l);
							}
							result.setCharge(result.getCharge() + ecrfProgress.getCharge());
							result.setEcrfFieldStatusQueueCounts(ServiceUtil.addEcrfFieldStatusEntryCounts(result.getEcrfFieldStatusQueueCounts(),
									ecrfProgress.getEcrfFieldStatusQueueCounts()));
							result.getEcrfs().add(ecrfProgress);
						}
					}
				} else {
					ECRFProgressVO ecrfProgress = ServiceUtil.populateEcrfProgress(listEntryVO, ecrfDao.toECRFOutVO(ecrf), null, dueDetail, sectionDetail,
							from, to,
							ecrfStatusEntryDao, ecrfStatusTypeDao, ecrfFieldDao, ecrfFieldValueDao, ecrfFieldStatusEntryDao, visitScheduleItemDao);
					if (ecrfProgress != null) {
						result.setEcrfTotalCount(result.getEcrfTotalCount() + 1l);
						if (ecrfProgress.getStatus() != null) {
							if (ecrfProgress.getStatus().getDone()) {
								result.setEcrfDoneCount(result.getEcrfDoneCount() + 1l);
							}
						}
						if (ecrfProgress.getOverdue()) {
							result.setEcrfOverdueCount(result.getEcrfOverdueCount() + 1l);
						}
						result.setCharge(result.getCharge() + ecrfProgress.getCharge());
						result.setEcrfFieldStatusQueueCounts(ServiceUtil.addEcrfFieldStatusEntryCounts(result.getEcrfFieldStatusQueueCounts(),
								ecrfProgress.getEcrfFieldStatusQueueCounts()));
						result.getEcrfs().add(ecrfProgress);
					}
				}
			}
		} else {
			if (from == null && to == null) {
				ServiceUtil.populateEcrfFieldStatusEntryCount(result.getEcrfFieldStatusQueueCounts(), listEntryVO.getId(), ecrfFieldStatusEntryDao);
			}
			Iterator<ECRF> ecrfIt = ecrfDao.findByTrialGroupVisitActiveSorted(listEntryVO.getTrial().getId(),
					listEntryVO.getGroup() != null ? listEntryVO.getGroup().getId() : null, null, true, true, null).iterator();
			while (ecrfIt.hasNext()) {
				ECRF ecrf = ecrfIt.next();
				ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(ecrf);
				Iterator<Visit> visitIt = ecrf.getVisits().iterator();
				if (visitIt.hasNext()) {
					while (visitIt.hasNext()) {
						Visit visit = visitIt.next();
						ECRFProgressVO ecrfProgress = ServiceUtil.createEcrfProgress(listEntryVO, ecrfVO, visitDao.toVisitOutVO(visit), dueDetail, from,
								to, ecrfStatusEntryDao, ecrfStatusTypeDao,
								visitScheduleItemDao);
						if (ecrfProgress != null) {
							result.setEcrfTotalCount(result.getEcrfTotalCount() + 1l);
							if (ecrfProgress.getStatus() != null && ecrfProgress.getStatus().getDone()) {
								result.setEcrfDoneCount(result.getEcrfDoneCount() + 1l);
							}
							if (ecrfProgress.getOverdue()) {
								result.setEcrfOverdueCount(result.getEcrfOverdueCount() + 1l);
							}
							result.setCharge(result.getCharge() + ecrfProgress.getCharge());
							result.getEcrfs().add(ecrfProgress);
						}
					}
				} else {
					ECRFProgressVO ecrfProgress = ServiceUtil.createEcrfProgress(listEntryVO, ecrfVO, null, dueDetail, from,
							to, ecrfStatusEntryDao, ecrfStatusTypeDao,
							visitScheduleItemDao);
					if (ecrfProgress != null) {
						result.setEcrfTotalCount(result.getEcrfTotalCount() + 1l);
						if (ecrfProgress.getStatus() != null && ecrfProgress.getStatus().getDone()) {
							result.setEcrfDoneCount(result.getEcrfDoneCount() + 1l);
						}
						if (ecrfProgress.getOverdue()) {
							result.setEcrfOverdueCount(result.getEcrfOverdueCount() + 1l);
						}
						result.setCharge(result.getCharge() + ecrfProgress.getCharge());
						result.getEcrfs().add(ecrfProgress);
					}
				}
			}
		}
		return result;
	}

	private ECRFFieldOutVO deleteEcrfFieldHelper(ECRF ecrf, Long ecrfFieldId, boolean deleteCascade, boolean checkTrialLocked, boolean checkEcrfLocked,
			Timestamp now, User user) throws Exception {
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldId, ecrfFieldDao);
		Trial trial = ecrfField.getTrial();
		if (checkTrialLocked) {
			ServiceUtil.checkTrialLocked(trial);
		}
		if (ecrf == null) {
			ecrf = ecrfField.getEcrf();
		}
		if (checkEcrfLocked) {
			ServiceUtil.checkLockedEcrfs(ecrf, this.getECRFStatusEntryDao(), this.getECRFDao());
		}
		ECRFFieldOutVO result = ecrfFieldDao.toECRFFieldOutVO(ecrfField);
		InputField field = ecrfField.getField();
		trial.removeEcrfFields(ecrfField);
		ecrf.removeEcrfFields(ecrfField);
		field.removeEcrfFields(ecrfField);
		ServiceUtil.removeEcrfField(ecrfField, deleteCascade, Settings.getBoolean(SettingCodes.REMOVE_ECRF_CHECK_PROBAND_LOCKED, Bundle.SETTINGS,
				DefaultSettings.REMOVE_ECRF_CHECK_PROBAND_LOCKED), now, user, true, true, this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao(),
				this.getInputFieldValueDao(),
				journalEntryDao,
				ecrfFieldDao, this.getNotificationDao(), this.getNotificationRecipientDao());
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_DELETED, result, null, journalEntryDao);
		return result;
	}

	private void execEcrfStatusActions(ECRFStatusEntry statusEntry, Long probandListStatusTypeId, Timestamp now, User user) throws Exception {
		ECRFStatusType newState = statusEntry.getStatus();
		ArrayList<ECRFStatusAction> sortedActions = new ArrayList<ECRFStatusAction>(newState.getActions());
		Collections.sort(sortedActions, new EcrfStatusActionComparator());
		Iterator<ECRFStatusAction> sortedActionsIt = sortedActions.iterator();
		ProbandListEntry listEntry = statusEntry.getListEntry();
		ECRF ecrf = statusEntry.getEcrf();
		Visit visit = statusEntry.getVisit();
		Integer scheduleValidationLimit = Settings.getIntNullable(SettingCodes.ECRF_FIELD_VALUES_SCHEDULE_VALIDATION_LIMIT, Bundle.SETTINGS,
				DefaultSettings.ECRF_FIELD_VALUES_SCHEDULE_VALIDATION_LIMIT);
		while (sortedActionsIt.hasNext()) {
			ECRFStatusAction ecrfStatusAction = sortedActionsIt.next();
			switch (ecrfStatusAction.getAction()) {
				case CLEAR_VALUES:
					// defer?
					clearEcrfFieldValues(listEntry,
							this.getECRFFieldValueDao().findByListEntryEcrfVisit(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null, true, null), now, user);
					break;
				case CLEAR_STATUS_ENTRIES:
					// defer?
					clearEcrfFieldStatusEntries(listEntry,
							this.getECRFFieldStatusEntryDao().findByListEntryEcrfVisit(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null, true, null), now,
							user);
					break;
				case CREATE_PROBAND_LIST_STATUS_ENTRY:
					try {
						ServiceUtil.addProbandListStatusEntry(listEntry, null, ProbandListStatusReasonCodes.ECRF_STATUS, new Object[] {
								this.getECRFDao().toECRFOutVO(ecrf).getUniqueName(),
								L10nUtil.getEcrfStatusTypeName(Locales.PROBAND_LIST_STATUS_ENTRY_REASON, newState.getNameL10nKey())
						}, now, probandListStatusTypeId, ecrf, newState, now, user,
								this.getProbandDao(),
								this.getProbandListEntryDao(),
								this.getProbandListStatusEntryDao(),
								this.getProbandListStatusTypeDao(),
								this.getTrialDao(),
								this.getMassMailDao(),
								this.getMassMailRecipientDao(),
								this.getJournalEntryDao());
					} catch (ServiceException e) {
						// already end state
					}
					break;
				case SCHEDULE_EXPORT_VALUES:
				case EXPORT_VALUES:
					throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_ECRF_STATUS_ACTION, ecrfStatusAction.getAction());
				case SCHEDULE_VALIDATE_VALUES:
					statusEntry.setValidationStatus(ECRFValidationStatus.PENDING);
					this.getECRFStatusEntryDao().update(statusEntry);
					break;
				case VALIDATE_VALUES:
					if (scheduleValidationLimit == null
							|| (scheduleValidationLimit > 0 && this.getECRFFieldValueDao().getCountField(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null,
									null) <= scheduleValidationLimit)) {
						addValidationEcrfFieldStatusEntries(statusEntry, true, now, user);
					} else {
						statusEntry.setValidationStatus(ECRFValidationStatus.PENDING);
						this.getECRFStatusEntryDao().update(statusEntry);
					}
					break;
				case NOTIFY_ECRF_STATUS:
					this.getNotificationDao().addNotification(statusEntry, now, null);
					break;
				case CANCEL_NOTIFICATIONS:
					ServiceUtil.cancelNotifications(statusEntry.getNotifications(), this.getNotificationDao(),
							org.phoenixctms.ctsms.enumeration.NotificationType.ECRF_STATUS_UPDATED);
					break;
				case NO_MISSING_VALUES:
					if (scheduleValidationLimit == null
							|| (scheduleValidationLimit > 0 && this.getECRFFieldValueDao().getCountField(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null,
									null) <= scheduleValidationLimit)) {
						checkMissingEcrfFieldValuesDeeply(listEntry, ecrf, visit);
					} else {
						checkMissingEcrfFieldValues(listEntry, ecrf, visit);
					}
					break;
				case NO_UNRESOLVED_FIELD_STATUS_ENTRIES:
					checkUnresolvedEcrfFieldStatus(listEntry, ecrf, visit);
					break;
				case SIGN_ECRF:
					this.getSignatureDao().addEcrfSignature(statusEntry, now);
					break;
				default:
					throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_ECRF_STATUS_ACTION, ecrfStatusAction.getAction());
			}
		}
	}

	private void execTrialStatusActions(TrialStatusType oldStatus, Trial trial, Date now, User user) throws Exception {
		TrialStatusType newState = trial.getStatus();
		if (oldStatus == null || !oldStatus.equals(newState)) {
			ArrayList<TrialStatusAction> sortedActions = new ArrayList<TrialStatusAction>(newState.getActions());
			Collections.sort(sortedActions, new TrialStatusActionComparator());
			Iterator<TrialStatusAction> sortedActionsIt = sortedActions.iterator();
			while (sortedActionsIt.hasNext()) {
				TrialStatusAction trialStatusAction = sortedActionsIt.next();
				switch (trialStatusAction.getAction()) {
					case EXPORT_PROBAND_LIST:
						throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_TRIAL_STATUS_ACTION, trialStatusAction.getAction());
					case SIGN_TRIAL:
						this.getSignatureDao().addTrialSignature(trial, CommonUtil.dateToTimestamp(now));
						break;
					case CLEAR_PROBAND_LIST:
						throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_TRIAL_STATUS_ACTION, trialStatusAction.getAction());
					case NOTIFY_TRIAL_STATUS:
						this.getNotificationDao().addNotification(trial, now, null);
						break;
					case NOTIFY_MISSING_TRIAL_TAG:
						Iterator<TrialTag> requiredTrialTagsIt = this.getTrialTagDao().findByNotifyMissing(true).iterator();
						Collection<TrialTagValue> tagValues = trial.getTagValues();
						HashSet<Long> tagIds = new HashSet<Long>(tagValues.size());
						Iterator<TrialTagValue> tagValuesIt = tagValues.iterator();
						while (tagValuesIt.hasNext()) {
							tagIds.add(tagValuesIt.next().getTag().getId());
						}
						NotificationDao notificationDao = this.getNotificationDao();
						ServiceUtil.cancelNotifications(trial.getNotifications(), notificationDao, org.phoenixctms.ctsms.enumeration.NotificationType.TRIAL_TAG_MISSING);
						while (requiredTrialTagsIt.hasNext()) {
							TrialTag requiredTrialTag = requiredTrialTagsIt.next();
							if (!tagIds.contains(requiredTrialTag.getId())) {
								notificationDao.addNotification(trial, requiredTrialTag, now, null);
							}
						}
						break;
					default:
						throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_TRIAL_STATUS_ACTION, trialStatusAction.getAction());
				}
			}
		}
	}

	private ECRFFieldValuesOutVO getEcrfFieldValues(ProbandListEntryOutVO listEntryVO, ECRFOutVO ecrfVO, VisitOutVO visitVO, String section, Long index, boolean addSeries,
			boolean jsValues,
			boolean loadAllJsValues,
			String fieldQuery, PSFVO psf) throws Exception {
		ECRFFieldValuesOutVO result = new ECRFFieldValuesOutVO();
		if (listEntryVO != null && ecrfVO != null) {
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			index = limitEcrfFieldValueIndex(ecrfVO.getId(), visitVO != null ? visitVO.getId() : null, section, index, listEntryVO.getId());
			Collection<Map> ecrfFieldValues = ecrfFieldValueDao.findByListEntryEcrfVisitSectionIndexJsField(listEntryVO.getId(), ecrfVO.getId(),
					visitVO != null ? visitVO.getId() : null,
					section, index, true, null, fieldQuery,
					psf);
			HashMap<String, Long> maxSeriesIndexMap = null;
			HashMap<String, Long> fieldMaxPositionMap = null;
			HashMap<String, Long> fieldMinPositionMap = null;
			HashMap<String, Set<ECRFField>> seriesEcrfFieldMap = null;
			if (addSeries && CommonUtil.isEmptyString(fieldQuery)) {
				maxSeriesIndexMap = new HashMap<String, Long>();
				fieldMaxPositionMap = new HashMap<String, Long>();
				fieldMinPositionMap = new HashMap<String, Long>();
				seriesEcrfFieldMap = new HashMap<String, Set<ECRFField>>();
				ServiceUtil.initSeriesEcrfFieldMaps(
						ecrfFieldDao.findByTrialEcrfSectionSeriesJs(null, ecrfVO.getId(), section, true, true, null, null),
						listEntryVO.getId(),
						ecrfVO.getId(),
						visitVO != null ? visitVO.getId() : null,
						maxSeriesIndexMap,
						fieldMaxPositionMap,
						fieldMinPositionMap,
						seriesEcrfFieldMap,
						ecrfFieldValueDao);
			}
			result.setPageValues(ServiceUtil.getEcrfFieldValues(listEntryVO, visitVO, ecrfFieldValues, maxSeriesIndexMap, fieldMaxPositionMap,
					fieldMinPositionMap, seriesEcrfFieldMap,
					null,
					ecrfFieldDao,
					ecrfFieldValueDao,
					this.getECRFFieldStatusEntryDao(),
					this.getECRFFieldStatusTypeDao()));
			if (jsValues) {
				if (addSeries && CommonUtil.isEmptyString(fieldQuery)) {
					maxSeriesIndexMap.clear();
					fieldMaxPositionMap.clear();
					fieldMinPositionMap.clear();
					seriesEcrfFieldMap.clear();
					ServiceUtil.initSeriesEcrfFieldMaps(
							(loadAllJsValues ? ecrfFieldDao.findByTrialEcrfSeriesJs(null, ecrfVO.getId(), true, true, true, null)
									: ecrfFieldDao.findByTrialEcrfSectionSeriesJs(null, ecrfVO.getId(), section, true, true, true, null)),
							listEntryVO.getId(),
							ecrfVO.getId(),
							visitVO != null ? visitVO.getId() : null,
							maxSeriesIndexMap,
							fieldMaxPositionMap,
							fieldMinPositionMap,
							seriesEcrfFieldMap,
							ecrfFieldValueDao);
				}
				if (loadAllJsValues) {
					result.setJsValues(ServiceUtil.getEcrfFieldJsonValues(visitVO != null ? visitVO.getId() : null,
							ecrfFieldValueDao.findByListEntryEcrfVisitJsField(listEntryVO.getId(), ecrfVO.getId(), visitVO != null ? visitVO.getId() : null, true, true, null,
									null),
							maxSeriesIndexMap, fieldMaxPositionMap,
							fieldMinPositionMap, seriesEcrfFieldMap,
							false, ecrfFieldValueDao,
							this.getInputFieldSelectionSetValueDao()));
				} else {
					result.setJsValues(ServiceUtil.getEcrfFieldJsonValues(visitVO != null ? visitVO.getId() : null, ecrfFieldValues,
							maxSeriesIndexMap, fieldMaxPositionMap,
							fieldMinPositionMap, seriesEcrfFieldMap,
							true, ecrfFieldValueDao,
							this.getInputFieldSelectionSetValueDao()));
				}
			}
		}
		return result;
	}

	private boolean getEcrfSectionUnlockValue(Long probandListEntryId, Long ecrfId, Long visitId, String section, Long index) throws Exception {
		Iterator<ECRFField> ecrfFieldIt = this.getECRFFieldDao().findByTrialEcrfSectionSeriesJs(null, ecrfId, section, false, true, null, null).iterator();
		ECRFFieldStatusQueue[] queues = ECRFFieldStatusQueue.values();
		while (ecrfFieldIt.hasNext()) {
			ECRFField ecrfField = ecrfFieldIt.next();
			for (int i = 0; i < queues.length; i++) {
				ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(queues[i], probandListEntryId, visitId, ecrfField.getId(), index);
				if (lastStatus != null && lastStatus.getStatus().isUnlockValue()) {
					return true;
				}
			}
		}
		return false;
	}

	private Randomization getRandomization(Trial trial, boolean randomize, Long groupId) throws ServiceException {
		if (randomize) {
			if (trial.getRandomization() != null) {
				Randomization randomization = Randomization.getInstance(trial.getRandomization(), this.getTrialDao(), this.getProbandGroupDao(), this.getProbandListEntryDao(),
						this.getStratificationRandomizationListDao(), this.getProbandListEntryTagDao(), this.getInputFieldSelectionSetValueDao(),
						this.getProbandListEntryTagValueDao(), this.getRandomizationListCodeDao());
				switch (randomization.getType()) {
					case GROUP:
						break;
					case TAG_SELECT:
						break;
					case TAG_TEXT:
						break;
					default:
						throw new IllegalArgumentException(
								L10nUtil.getMessage(MessageCodes.UNSUPPORTED_RANDOMIZATION_MODE, DefaultMessages.UNSUPPORTED_RANDOMIZATION_TYPE, randomization.getType()));
				}
				return randomization;
			} else {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.RANDOMIZATION_NOT_DEFINED_FOR_TRIAL);
			}
		}
		return null;
	}

	private SignatureVO getVerifiedTrialSignatureVO(Signature signature) throws Exception {
		SignatureVO result = this.getSignatureDao().toSignatureVO(signature);
		StringBuilder comment = new StringBuilder();
		result.setVerificationTimestamp(new Date());
		result.setValid(TrialSignature.verify(signature, comment));
		result.setVerified(true);
		result.setComment(comment.toString());
		result.setDescription(EntitySignature.getDescription(result));
		return result;
	}

	@Override
	protected ECRFOutVO handleAddEcrf(AuthenticationVO auth, ECRFInVO newEcrf) throws Exception {
		checkAddEcrfInput(newEcrf);
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF ecrf = ecrfDao.eCRFInVOToEntity(newEcrf);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(ecrf, now, user);
		ecrf = ecrfDao.create(ecrf);
		ECRFOutVO result = ecrfDao.toECRFOutVO(ecrf);
		ServiceUtil.logSystemMessage(ecrf.getTrial(), result.getTrial(), now, user, SystemMessageCodes.ECRF_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ECRFFieldOutVO handleAddEcrfField(AuthenticationVO auth, ECRFFieldInVO newEcrfField) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return addEcrfField(newEcrfField, now, user);
	}

	@Override
	protected Collection<ECRFFieldOutVO> handleAddEcrfFields(AuthenticationVO auth, Long ecrfId, String section, boolean series, boolean optional, boolean auditTrail,
			Set<Long> inputFieldIds)
			throws Exception {
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		Long position = this.getECRFFieldDao().findMaxPosition(ecrfId, section);
		if (position == null) {
			position = CommonUtil.LIST_INITIAL_POSITION;
		} else {
			position += 1L;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<ECRFFieldOutVO> result = new ArrayList<ECRFFieldOutVO>();
		if (inputFieldIds != null) {
			Iterator<Long> inputFieldIt = inputFieldIds.iterator();
			while (inputFieldIt.hasNext()) {
				Long inputFieldId = inputFieldIt.next();
				ECRFFieldInVO newEcrfField = new ECRFFieldInVO();
				newEcrfField.setPosition(position);
				newEcrfField.setFieldId(inputFieldId);
				newEcrfField.setSection(section);
				newEcrfField.setOptional(optional);
				newEcrfField.setAuditTrail(auditTrail);
				newEcrfField.setReasonForChangeRequired(auditTrail);
				newEcrfField.setNotify(false);
				newEcrfField.setSeries(series);
				newEcrfField.setEcrfId(ecrf.getId());
				newEcrfField.setTrialId(ecrf.getTrial().getId());
				try {
					result.add(addEcrfField(newEcrfField, now, user));
				} catch (ServiceException e) {
					// ignore; due to existent field....
				}
				position += 1L;
			}
		}
		return result;
	}

	@Override
	protected ECRFFieldStatusEntryOutVO handleAddEcrfFieldStatusEntry(
			AuthenticationVO auth, ECRFFieldStatusQueue queue, ECRFFieldStatusEntryInVO newEcrfFieldStatusEntry) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return addEcrfFieldStatusEntry(newEcrfFieldStatusEntry, null, queue, now, user,
				Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL),
				Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND),
				false);
	}

	@Override
	protected Collection<InquiryOutVO> handleAddInquiries(
			AuthenticationVO auth, Long trialId, String category, boolean optional, boolean excel, Set<Long> inputFieldIds)
			throws Exception {
		Trial trial = CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		Long position = this.getInquiryDao().findMaxPosition(trialId, category);
		if (position == null) {
			position = CommonUtil.LIST_INITIAL_POSITION;
		} else {
			position += 1L;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<InquiryOutVO> result = new ArrayList<InquiryOutVO>();
		if (inputFieldIds != null) {
			Iterator<Long> inputFieldIt = inputFieldIds.iterator();
			while (inputFieldIt.hasNext()) {
				Long inputFieldId = inputFieldIt.next();
				InquiryInVO newInquiry = new InquiryInVO();
				newInquiry.setPosition(position);
				newInquiry.setFieldId(inputFieldId);
				newInquiry.setCategory(category);
				newInquiry.setOptional(optional);
				newInquiry.setExcelValue(excel);
				newInquiry.setExcelDate(excel);
				newInquiry.setActive(true);
				newInquiry.setActiveSignup(true);
				newInquiry.setTrialId(trial.getId());
				try {
					result.add(addInquiry(newInquiry, now, user));
				} catch (ServiceException e) {
					// ignore; due to existent proband....
				}
				position += 1L;
			}
		}
		return result;
	}

	@Override
	protected InquiryOutVO handleAddInquiry(AuthenticationVO auth, InquiryInVO newInquiry)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return addInquiry(newInquiry, now, user);
	}

	@Override
	protected ProbandGroupOutVO handleAddProbandGroup(
			AuthenticationVO auth, ProbandGroupInVO newProbandGroup) throws Exception {
		checkProbandGroupInput(newProbandGroup);
		ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
		ProbandGroup probandGroup = probandGroupDao.probandGroupInVOToEntity(newProbandGroup);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(probandGroup, now, user);
		probandGroup = probandGroupDao.create(probandGroup);
		ProbandGroupOutVO result = probandGroupDao.toProbandGroupOutVO(probandGroup);
		ServiceUtil.logSystemMessage(probandGroup.getTrial(), result.getTrial(), now, user, SystemMessageCodes.PROBAND_GROUP_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<ProbandListEntryOutVO> handleAddProbandListEntries(
			AuthenticationVO auth, Long trialId, boolean randomize, Long groupId, Long rating, Long ratingMax, Set<Long> probandIds, boolean shuffle, Long limit) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		Long position = this.getProbandListEntryDao().findMaxPosition(trialId);
		if (position == null) {
			position = CommonUtil.LIST_INITIAL_POSITION;
		} else {
			position += 1L;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<ProbandListEntryOutVO> result = new ArrayList<ProbandListEntryOutVO>();
		ShuffleInfoVO shuffleInfo = new ShuffleInfoVO();
		shuffleInfo.setLimit(limit);
		Randomization randomization = getRandomization(trial, randomize, groupId);
		if (probandIds != null) {
			Iterator<Long> probandIt;
			ArrayList<Long> ids = new ArrayList<Long>(probandIds);
			shuffleInfo.setInputIds(ids);
			if (shuffle) {
				long seed = SecureRandom.getInstance(SHUFFLE_SEED_RANDOM_ALGORITHM).nextLong();
				Random random = new Random(seed); // reproducable
				shuffleInfo.setPrngClass(CoreUtil.getPrngClassDescription(random));
				shuffleInfo.setSeed(seed);
				Collections.shuffle(ids, random);
				probandIt = ids.iterator();
			} else {
				probandIt = probandIds.iterator();
			}
			while (probandIt.hasNext() && (limit == null || result.size() < limit)) {
				Long probandId = probandIt.next();
				ProbandListEntryInVO newProbandListEntry = new ProbandListEntryInVO();
				newProbandListEntry.setPosition(position);
				newProbandListEntry.setProbandId(probandId);
				newProbandListEntry.setTrialId(trial.getId());
				newProbandListEntry.setGroupId(groupId);
				newProbandListEntry.setRating(rating);
				newProbandListEntry.setRatingMax(ratingMax);
				try {
					result.add(addProbandListEntry(newProbandListEntry, false, false, randomization, trial, now, user));
					shuffleInfo.getResultIds().add(probandId);
				} catch (ServiceException e) {
					// ignore; due to existent proband....
				}
				position += 1L;
			}
		}
		logSystemMessage(trial, trialDao.toTrialOutVO(trial), now, user, shuffleInfo,
				shuffleInfo != null ? SystemMessageCodes.PROBAND_LIST_ENTRIES_SHUFFLED_AND_CREATED : SystemMessageCodes.PROBAND_LIST_ENTRIES_CREATED);
		return result;
	}

	@Override
	protected ProbandListEntryOutVO handleAddProbandListEntry(
			AuthenticationVO auth, boolean createProband, boolean signup, Boolean randomize, ProbandListEntryInVO newProbandListEntry) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		Trial trial = CheckIDUtil.checkTrialId(newProbandListEntry.getTrialId(), this.getTrialDao());
		Randomization randomization = getRandomization(trial, randomize != null ? randomize : signup && trial.isSignupRandomize(),
				newProbandListEntry.getGroupId());
		return addProbandListEntry(newProbandListEntry, createProband, signup, randomization, trial, now, user);
	}

	@Override
	protected ProbandListEntryTagOutVO handleAddProbandListEntryTag(
			AuthenticationVO auth, ProbandListEntryTagInVO newProbandListEntryTag) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return addProbandListEntryTag(newProbandListEntryTag, now, user);
	}

	@Override
	protected Collection<ProbandListEntryTagOutVO> handleAddProbandListEntryTags(
			AuthenticationVO auth, Long trialId, boolean optional, boolean excel, boolean ecrf, boolean stratification, boolean randomize, Set<Long> inputFieldIds)
			throws Exception {
		Trial trial = CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		Long position = this.getProbandListEntryTagDao().findMaxPosition(trialId);
		if (position == null) {
			position = CommonUtil.LIST_INITIAL_POSITION;
		} else {
			position += 1L;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<ProbandListEntryTagOutVO> result = new ArrayList<ProbandListEntryTagOutVO>();
		if (inputFieldIds != null) {
			Iterator<Long> inputFieldIt = inputFieldIds.iterator();
			while (inputFieldIt.hasNext()) {
				Long inputFieldId = inputFieldIt.next();
				ProbandListEntryTagInVO newProbandListEntryTag = new ProbandListEntryTagInVO();
				newProbandListEntryTag.setPosition(position);
				newProbandListEntryTag.setOptional(optional);
				newProbandListEntryTag.setExcelValue(excel);
				newProbandListEntryTag.setExcelDate(excel);
				newProbandListEntryTag.setEcrfValue(ecrf);
				newProbandListEntryTag.setStratification(stratification);
				newProbandListEntryTag.setRandomize(randomize);
				newProbandListEntryTag.setFieldId(inputFieldId);
				newProbandListEntryTag.setTrialId(trial.getId());
				try {
					result.add(addProbandListEntryTag(newProbandListEntryTag, now, user));
				} catch (ServiceException e) {
					// ignore; due to existent proband....
				}
				position += 1L;
			}
		}
		return result;
	}

	@Override
	protected ProbandListStatusEntryOutVO handleAddProbandListStatusEntry(
			AuthenticationVO auth, boolean signup, ProbandListStatusEntryInVO newProbandListStatusEntry) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return ServiceUtil.addProbandListStatusEntry(newProbandListStatusEntry, signup, now, user, true, true,
				this.getProbandDao(), this.getProbandListEntryDao(), this.getProbandListStatusEntryDao(), this.getProbandListStatusTypeDao(),
				this.getTrialDao(), this.getMassMailDao(), this.getMassMailRecipientDao(),
				this.getJournalEntryDao());
	}

	@Override
	protected StratificationRandomizationListOutVO handleAddStratificationRandomizationList(AuthenticationVO auth,
			StratificationRandomizationListInVO newStratificationRandomizationList, RandomizationListCodesVO newCodes) throws Exception {
		ArrayList<RandomizationListCodeInVO> codes = checkStratificationRandomizationListInput(newStratificationRandomizationList, newCodes != null ? newCodes.getCodes() : null,
				true);
		StratificationRandomizationListDao stratificationRandomizationListDao = this.getStratificationRandomizationListDao();
		StratificationRandomizationList randomizationList = stratificationRandomizationListDao.stratificationRandomizationListInVOToEntity(newStratificationRandomizationList);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(randomizationList, now, user);
		randomizationList = stratificationRandomizationListDao.create(randomizationList);
		ServiceUtil.createRandomizationListCodes(randomizationList, codes, false, now, user, this.getRandomizationListCodeDao(), this.getRandomizationListCodeValueDao());
		StratificationRandomizationListOutVO result = stratificationRandomizationListDao.toStratificationRandomizationListOutVO(randomizationList);
		ServiceUtil.logSystemMessage(randomizationList.getTrial(), result.getTrial(), now, user, SystemMessageCodes.STRATIFICATION_RANDOMIZATION_LIST_CREATED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TeamMemberOutVO handleAddTeamMember(AuthenticationVO auth, TeamMemberInVO newTeamMember)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return addTeamMember(newTeamMember, now, user);
	}

	@Override
	protected Collection<TeamMemberOutVO> handleAddTeamMembers(
			AuthenticationVO auth, Long trialId, Long roleId, boolean access,
			boolean sign, boolean resolve, boolean verify, Set<Long> staffIds) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<TeamMemberOutVO> result = new ArrayList<TeamMemberOutVO>();
		if (staffIds != null) {
			Iterator<Long> staffIt = staffIds.iterator();
			while (staffIt.hasNext()) {
				Long staffId = staffIt.next();
				TeamMemberInVO newTeamMember = new TeamMemberInVO();
				newTeamMember.setAccess(access);
				newTeamMember.setNotifyTimelineEvent(false);
				newTeamMember.setNotifyEcrfValidatedStatus(false);
				newTeamMember.setNotifyEcrfReviewStatus(false);
				newTeamMember.setNotifyEcrfVerifiedStatus(false);
				newTeamMember.setNotifyEcrfFieldStatus(false);
				newTeamMember.setNotifyOther(false);
				newTeamMember.setSign(sign);
				newTeamMember.setResolve(resolve);
				newTeamMember.setVerify(verify);
				newTeamMember.setRoleId(roleId);
				newTeamMember.setStaffId(staffId);
				newTeamMember.setTrialId(trialId);
				try {
					result.add(addTeamMember(newTeamMember, now, user));
				} catch (ServiceException e) {
					// ignore; due to existent participations....
				}
			}
		}
		return result;
	}

	@Override
	protected TimelineEventOutVO handleAddTimelineEvent(
			AuthenticationVO auth, TimelineEventInVO newTimelineEvent, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth) throws Exception {
		checkTimelineEventInput(newTimelineEvent);
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent timelineEvent = timelineEventDao.timelineEventInVOToEntity(newTimelineEvent);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(timelineEvent, now, user);
		timelineEvent.setDismissedTimestamp(now);
		timelineEvent = timelineEventDao.create(timelineEvent);
		notifyTimelineEventReminder(timelineEvent, now);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent, maxInstances, maxParentDepth, maxChildrenDepth);
		ServiceUtil.logSystemMessage(timelineEvent.getTrial(), result.getTrial(), now, user, SystemMessageCodes.TIMELINE_EVENT_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TrialOutVO handleAddTrial(AuthenticationVO auth, TrialInVO newTrial, RandomizationListCodesVO newCodes) throws Exception {
		ArrayList<RandomizationListCodeInVO> codes = checkAddTrialInput(newTrial, newCodes != null ? newCodes.getCodes() : null);
		TrialDao trialDao = this.getTrialDao();
		Trial trial = trialDao.trialInVOToEntity(newTrial);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(trial, now, user);
		trial = trialDao.create(trial);
		ServiceUtil.createRandomizationListCodes(trial, codes, false, now, user, this.getRandomizationListCodeDao(), this.getRandomizationListCodeValueDao());
		execTrialStatusActions(null, trial, now, user);
		TrialOutVO result = trialDao.toTrialOutVO(trial);
		ServiceUtil.logSystemMessage(trial, result, now, user, SystemMessageCodes.TRIAL_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TrialTagValueOutVO handleAddTrialTagValue(
			AuthenticationVO auth, TrialTagValueInVO newTrialTagValue) throws Exception {
		checkTrialTagValueInput(newTrialTagValue);
		TrialTagValueDao tagValueDao = this.getTrialTagValueDao();
		TrialTagValue tagValue = tagValueDao.trialTagValueInVOToEntity(newTrialTagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(tagValue, now, user);
		tagValue = tagValueDao.create(tagValue);
		TrialTagValueOutVO result = tagValueDao.toTrialTagValueOutVO(tagValue);
		ServiceUtil.logSystemMessage(tagValue.getTrial(), result.getTrial(), now, user, SystemMessageCodes.TRIAL_TAG_VALUE_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ECRFOutVO handleAddUpdateEcrf(AuthenticationVO auth, ECRFInVO modifiedEcrf, Set<ECRFFieldInVO> modifiedEcrfFields) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		HashSet<Long> originalEcrfFieldIds;
		ECRFOutVO original;
		ECRF ecrf;
		boolean update;
		if (modifiedEcrf.getId() != null) {
			SignatureDao signatureDao = this.getSignatureDao();
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			NotificationDao notificationDao = this.getNotificationDao();
			NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
			ECRF originalEcrf = CheckIDUtil.checkEcrfId(modifiedEcrf.getId(), ecrfDao, LockMode.PESSIMISTIC_WRITE);
			Iterator<ECRFStatusEntry> ecrfStatusEntriesIt = checkUpdateEcrfInput(modifiedEcrf, originalEcrf).iterator();
			original = ecrfDao.toECRFOutVO(originalEcrf);
			Collection<ECRFField> originalEcrfFields = originalEcrf.getEcrfFields();
			originalEcrfFieldIds = new HashSet<Long>(originalEcrfFields.size());
			Iterator<ECRFField> it = originalEcrfFields.iterator();
			while (it.hasNext()) {
				originalEcrfFieldIds.add(it.next().getId());
			}
			ecrfDao.evict(originalEcrf);
			ecrf = ecrfDao.eCRFInVOToEntity(modifiedEcrf);
			while (ecrfStatusEntriesIt.hasNext()) {
				ECRFStatusEntry ecrfStatusEntry = ecrfStatusEntriesIt.next();
				ProbandListEntry listEntry = ecrfStatusEntry.getListEntry();
				listEntry.removeEcrfStatusEntries(ecrfStatusEntry);
				ecrf.removeEcrfStatusEntries(ecrfStatusEntry);
				ServiceUtil.removeEcrfStatusEntry(ecrfStatusEntry, true,
						signatureDao, ecrfStatusEntryDao, notificationDao, notificationRecipientDao);
			}
			CoreUtil.modifyVersion(originalEcrf, ecrf, now, user);
			ecrfDao.update(ecrf);
			update = true;
		} else {
			checkAddEcrfInput(modifiedEcrf);
			originalEcrfFieldIds = new HashSet<Long>();
			original = null;
			ecrf = ecrfDao.eCRFInVOToEntity(modifiedEcrf);
			CoreUtil.modifyVersion(ecrf, now, user);
			ecrf = ecrfDao.create(ecrf, LockMode.PESSIMISTIC_WRITE); // required here, as load(..,lockMode) will not work with created entities
			update = false;
		}
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Iterator<Long> originalEcrfFieldIdsIt;
		if (modifiedEcrfFields != null && modifiedEcrfFields.size() > 0) {
			HashSet<Long> ecrfFieldIdDupeCheck = new HashSet<Long>(modifiedEcrfFields.size());
			Iterator<ECRFFieldInVO> it = modifiedEcrfFields.iterator();
			ArrayList<ECRFFieldInVO> ecrfFieldIns = new ArrayList<ECRFFieldInVO>(modifiedEcrfFields.size());
			while (it.hasNext()) {
				ECRFFieldInVO modifiedEcrfField = it.next();
				if (modifiedEcrfField != null) {
					if (modifiedEcrfField.getId() != null) {
						if (!update) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.ENTITY_ID_NOT_NULL, modifiedEcrfField.getId().toString());
						}
						if (!ecrfFieldIdDupeCheck.add(modifiedEcrfField.getId())) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_DUPLICATE_ID, modifiedEcrfField.getId().toString());
						}
						originalEcrfFieldIds.remove(modifiedEcrfField.getId());
						ecrfFieldIns.add(modifiedEcrfField);
					} else {
						if (update) {
							ECRF ecrfFieldEcrf = CheckIDUtil.checkEcrfId(modifiedEcrfField.getEcrfId(), ecrfDao);
							if (!ecrf.equals(ecrfFieldEcrf)) {
								throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_FOR_WRONG_ECRF, ecrfDao.toECRFOutVO(ecrfFieldEcrf).getUniqueName());
							}
							ecrfFieldIns.add(modifiedEcrfField);
						} else {
							if (modifiedEcrfField.getEcrfId() != null) {
								throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_ECRF_ID_NOT_NULL, modifiedEcrfField.getEcrfId().toString());
							}
							ECRFFieldInVO newEcrfField = new ECRFFieldInVO(modifiedEcrfField);
							newEcrfField.setEcrfId(ecrf.getId());
							ecrfFieldIns.add(newEcrfField);
						}
					}
				}
			}
			originalEcrfFieldIdsIt = originalEcrfFieldIds.iterator();
			while (originalEcrfFieldIdsIt.hasNext()) {
				deleteEcrfFieldHelper(ecrf, originalEcrfFieldIdsIt.next(), true, false, false, now, user);
			}
			it = ecrfFieldIns.iterator();
			while (it.hasNext()) {
				ECRFFieldInVO modifiedEcrfField = it.next();
				ECRFField ecrfField;
				ECRFFieldOutVO ecrfFieldVO;
				if (modifiedEcrfField.getId() != null) {
					ECRFField originalEcrfField = CheckIDUtil.checkEcrfFieldId(modifiedEcrfField.getId(), ecrfFieldDao);
					checkUpdateEcrfFieldInput(originalEcrfField, modifiedEcrfField); // access original associations before evict
					ECRFFieldOutVO originalEcrfFieldVO = ecrfFieldDao.toECRFFieldOutVO(originalEcrfField);
					ecrfFieldDao.evict(originalEcrfField);
					ecrfField = ecrfFieldDao.eCRFFieldInVOToEntity(modifiedEcrfField);
					CoreUtil.modifyVersion(originalEcrfField, ecrfField, now, user);
					ecrfFieldDao.update(ecrfField);
					ecrfFieldVO = ecrfFieldDao.toECRFFieldOutVO(ecrfField);
					if (ServiceUtil.LOG_ADD_UPDATE_ECRF_NO_DIFF
							|| !ECRFFieldOutVO.equalsExcluding(originalEcrfFieldVO, ecrfFieldVO, CoreUtil.VO_VERSION_EQUALS_EXCLUDES, true, true)) {
						ServiceUtil.logSystemMessage(ecrfField.getTrial(), ecrfFieldVO.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_UPDATED, ecrfFieldVO,
								originalEcrfFieldVO,
								this.getJournalEntryDao());
					}
				} else {
					checkAddEcrfFieldInput(modifiedEcrfField);
					ecrfField = ecrfFieldDao.eCRFFieldInVOToEntity(modifiedEcrfField);
					CoreUtil.modifyVersion(ecrfField, now, user);
					ecrfField = ecrfFieldDao.create(ecrfField);
					ecrfFieldVO = ecrfFieldDao.toECRFFieldOutVO(ecrfField);
					ServiceUtil
							.logSystemMessage(ecrfField.getTrial(), ecrfFieldVO.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_CREATED, ecrfFieldVO, null, journalEntryDao);
				}
			}
		} else {
			originalEcrfFieldIdsIt = originalEcrfFieldIds.iterator();
			while (originalEcrfFieldIdsIt.hasNext()) {
				deleteEcrfFieldHelper(ecrf, originalEcrfFieldIdsIt.next(), true, false, false, now, user);
			}
		}
		CoreUtil.getUserContext().voMapClear();
		ECRFOutVO result = ecrfDao.toECRFOutVO(ecrf);
		if (!update || ServiceUtil.LOG_ADD_UPDATE_ECRF_NO_DIFF || !ECRFOutVO.equalsExcluding(original, result, CoreUtil.VO_VERSION_EQUALS_EXCLUDES, true, true)) {
			ServiceUtil.logSystemMessage(ecrf.getTrial(), result.getTrial(), now, user, update ? SystemMessageCodes.ECRF_UPDATED : SystemMessageCodes.ECRF_CREATED, result,
					original,
					journalEntryDao);
		}
		return result;
	}

	@Override
	protected VisitOutVO handleAddVisit(AuthenticationVO auth, VisitInVO newVisit) throws Exception {
		checkVisitInput(newVisit);
		VisitDao visitDao = this.getVisitDao();
		Visit visit = visitDao.visitInVOToEntity(newVisit);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(visit, now, user);
		visit = visitDao.create(visit);
		VisitOutVO result = visitDao.toVisitOutVO(visit);
		ServiceUtil.logSystemMessage(visit.getTrial(), result.getTrial(), now, user, SystemMessageCodes.VISIT_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected VisitScheduleItemOutVO handleAddVisitScheduleItem(
			AuthenticationVO auth, VisitScheduleItemInVO newVisitScheduleItem) throws Exception {
		checkVisitScheduleItemInput(newVisitScheduleItem);
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		VisitScheduleItem visitScheduleItem = visitScheduleItemDao.visitScheduleItemInVOToEntity(newVisitScheduleItem);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(visitScheduleItem, now, user);
		visitScheduleItem = visitScheduleItemDao.create(visitScheduleItem);
		notifyVisitScheduleItemReminder(visitScheduleItem, null, now);
		VisitScheduleItemOutVO result = visitScheduleItemDao.toVisitScheduleItemOutVO(visitScheduleItem);
		ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getTrial(), now, user, SystemMessageCodes.VISIT_SCHEDULE_ITEM_CREATED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected InquiryValuesOutVO handleCheckInquiryValues(
			AuthenticationVO auth, Set<InquiryValueInVO> dummyInquiryValues)
			throws Exception {
		UserOutVO userVO = this.getUserDao().toUserOutVO(CoreUtil.getUser());
		Date now = new Date();
		InquiryValuesOutVO result = new InquiryValuesOutVO();
		ServiceException firstException = null;
		HashMap<Long, String> errorMessagesMap = new HashMap<Long, String>();
		Trial trial = null;
		if (dummyInquiryValues != null && dummyInquiryValues.size() > 0) {
			ArrayList<InquiryValueOutVO> inquiryValues = new ArrayList<InquiryValueOutVO>(dummyInquiryValues.size());
			ArrayList<InquiryValueJsonVO> jsInquiryValues = null;
			if (Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
					DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				jsInquiryValues = new ArrayList<InquiryValueJsonVO>(dummyInquiryValues.size());
			}
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
			InquiryDao inquiryDao = this.getInquiryDao();
			InputFieldDao inputFieldDao = this.getInputFieldDao();
			Iterator<InquiryValueInVO> inquiryValuesInIt = dummyInquiryValues.iterator();
			while (inquiryValuesInIt.hasNext()) {
				InquiryValueInVO inquiryValueIn = inquiryValuesInIt.next();
				Inquiry inquiry = CheckIDUtil.checkInquiryId(inquiryValueIn.getInquiryId(), inquiryDao);
				Trial nextTrial = inquiry.getTrial();
				if (trial == null) {
					trial = nextTrial;
				} else if (!trial.equals(nextTrial)) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_VALUES_FOR_DIFFERENT_TRIALS);
				}
				InputField inputField = inquiry.getField();
				try {
					ServiceUtil.checkInputFieldTextValue(inputField, inquiry.isOptional(), inquiryValueIn.getTextValue(), inputFieldDao, inputFieldSelectionSetValueDao);
					ServiceUtil.checkInputFieldBooleanValue(inputField, inquiry.isOptional(), inquiryValueIn.getBooleanValue(), inputFieldDao);
					ServiceUtil.checkInputFieldLongValue(inputField, inquiry.isOptional(), inquiryValueIn.getLongValue(), inputFieldDao);
					ServiceUtil.checkInputFieldFloatValue(inputField, inquiry.isOptional(), inquiryValueIn.getFloatValue(), inputFieldDao);
					ServiceUtil.checkInputFieldDateValue(inputField, inquiry.isOptional(), inquiryValueIn.getDateValue(), inputFieldDao);
					ServiceUtil.checkInputFieldTimeValue(inputField, inquiry.isOptional(), inquiryValueIn.getTimeValue(), inputFieldDao);
					ServiceUtil.checkInputFieldTimestampValue(inputField, inquiry.isOptional(), inquiryValueIn.getTimestampValue(), inputFieldDao);
					ServiceUtil.checkInputFieldInkValue(inputField, inquiry.isOptional(), inquiryValueIn.getInkValues(), inputFieldDao);
					ServiceUtil.checkInputFieldSelectionSetValues(inputField, inquiry.isOptional(), inquiryValueIn.getSelectionValueIds(), inputFieldDao,
							inputFieldSelectionSetValueDao);
					InquiryOutVO inquiryVO = inquiryDao.toInquiryOutVO(inquiry);
					InquiryValueOutVO inquiryValueVO = new InquiryValueOutVO();
					copyInquiryValueInToOut(inquiryValueVO, inquiryValueIn, inquiryVO, null, userVO, now);
					inquiryValues.add(inquiryValueVO);
					if (jsInquiryValues != null && !CommonUtil.isEmptyString(inquiryVO.getJsVariableName())) {
						InquiryValueJsonVO inquiryValueJsonVO = new InquiryValueJsonVO();
						CommonUtil.copyInquiryValueInToJson(inquiryValueJsonVO, inquiryValueIn, inquiryVO);
						jsInquiryValues.add(inquiryValueJsonVO);
					}
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
	protected Collection<ECRFFieldValueOutVO> handleClearEcrfFieldValues(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ProbandListEntry listEntry = checkClearEcrfFieldValues(probandListEntryId, ecrfId, visitId, now, user);
		clearEcrfFieldStatusEntries(listEntry,
				this.getECRFFieldStatusEntryDao().findByListEntryEcrfVisit(probandListEntryId, ecrfId, visitId, true, null), now, user);
		return clearEcrfFieldValues(listEntry,
				this.getECRFFieldValueDao().findByListEntryEcrfVisit(probandListEntryId, ecrfId, visitId, true, null), now, user);
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> handleClearEcrfFieldValues(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, String section)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ProbandListEntry listEntry = checkClearEcrfFieldValues(probandListEntryId, ecrfId, visitId, now, user);
		clearEcrfFieldStatusEntries(listEntry,
				this.getECRFFieldStatusEntryDao().findByListEntryEcrfVisitSection(probandListEntryId, ecrfId, visitId, section, true, null), now, user);
		return clearEcrfFieldValues(this.getProbandListEntryDao().load(probandListEntryId),
				this.getECRFFieldValueDao().findByListEntryEcrfVisitSection(probandListEntryId, ecrfId, visitId, section, true, null), now, user);
	}

	@Override
	protected ECRFOutVO handleCloneEcrf(AuthenticationVO auth, Long ecrfId, Long trialId) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF originalEcrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
		ECRFOutVO originalEcrfVO = ecrfDao.toECRFOutVO(originalEcrf);
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao, LockMode.PESSIMISTIC_WRITE);
		ServiceUtil.checkTrialLocked(trial);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ECRF newEcrf = ECRF.Factory.newInstance();
		newEcrf.setActive(originalEcrf.isActive());
		newEcrf.setDescription(originalEcrf.getDescription());
		newEcrf.setDisabled(originalEcrf.isDisabled());
		newEcrf.setEnableBrowserFieldCalculation(originalEcrf.isEnableBrowserFieldCalculation());
		newEcrf.setCharge(originalEcrf.getCharge());
		newEcrf.setName(originalEcrf.getName());
		newEcrf.setExternalId(originalEcrf.getExternalId());
		newEcrf.setProbandListStatus(originalEcrf.getProbandListStatus());
		newEcrf.setRevision(
				getNewEcrfRevision(trialId, newEcrf.getName(),
						Settings.getString(SettingCodes.ECRF_COPY_REVISION_PATTERN, Bundle.SETTINGS, DefaultSettings.ECRF_COPY_REVISION_PATTERN)));
		newEcrf.setTitle(originalEcrf.getTitle());
		newEcrf.setTrial(trial);
		trial.addEcrfs(newEcrf);
		CoreUtil.modifyVersion(newEcrf, now, user);
		newEcrf = ecrfDao.create(newEcrf);
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Iterator<ECRFField> originalEcrfFieldsIt = originalEcrf.getEcrfFields().iterator();
		while (originalEcrfFieldsIt.hasNext()) {
			ECRFField originalEcrfField = originalEcrfFieldsIt.next();
			ECRFFieldOutVO originalEcrfFieldVO = ecrfFieldDao.toECRFFieldOutVO(originalEcrfField);
			ECRFField newEcrfField = ECRFField.Factory.newInstance();
			newEcrfField.setAuditTrail(originalEcrfField.isAuditTrail());
			newEcrfField.setComment(originalEcrfField.getComment());
			newEcrfField.setTitleL10nKey(originalEcrfField.getTitleL10nKey());
			newEcrfField.setDeferredDelete(originalEcrfField.isDeferredDelete());
			newEcrfField.setDeferredDeleteReason(originalEcrfField.getDeferredDeleteReason());
			newEcrfField.setDisabled(originalEcrfField.isDisabled());
			newEcrfField.setJsOutputExpression(originalEcrfField.getJsOutputExpression());
			newEcrfField.setJsValueExpression(originalEcrfField.getJsValueExpression());
			newEcrfField.setJsVariableName(originalEcrfField.getJsVariableName());
			newEcrfField.setOptional(originalEcrfField.isOptional());
			newEcrfField.setPosition(originalEcrfField.getPosition());
			newEcrfField.setReasonForChangeRequired(originalEcrfField.isReasonForChangeRequired());
			newEcrfField.setNotify(originalEcrfField.isNotify());
			newEcrfField.setSection(originalEcrfField.getSection());
			newEcrfField.setSeries(originalEcrfField.isSeries());
			newEcrfField.setExternalId(originalEcrfField.getExternalId());
			newEcrfField.setEcrf(newEcrf);
			newEcrf.addEcrfFields(newEcrfField);
			InputField field = originalEcrfField.getField();
			newEcrfField.setField(field);
			field.addEcrfFields(newEcrfField);
			newEcrfField.setTrial(trial);
			trial.addEcrfFields(newEcrfField);
			CoreUtil.modifyVersion(newEcrfField, now, user);
			newEcrfField = ecrfFieldDao.create(newEcrfField);
			ECRFFieldOutVO newEcrfFieldVO = ecrfFieldDao.toECRFFieldOutVO(newEcrfField);
			ServiceUtil.logSystemMessage(newEcrfField.getTrial(), newEcrfFieldVO.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_CLONED, newEcrfFieldVO, originalEcrfFieldVO,
					journalEntryDao);
		}
		ECRFOutVO result = ecrfDao.toECRFOutVO(newEcrf);
		ServiceUtil.logSystemMessage(newEcrf.getTrial(), result.getTrial(), now, user, SystemMessageCodes.ECRF_CLONED, result, originalEcrfVO, journalEntryDao);
		return result;
	}

	@Override
	protected ECRFOutVO handleDeleteEcrf(AuthenticationVO auth, Long ecrfId, boolean defer, boolean force, String deferredDeleteReason) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ECRFOutVO result;
		if (!force && (defer
				|| this.getECRFStatusEntryDao().getCount(null, ecrfId, null, null, null, null, null, null) > 0)) {
			ECRF originalEcrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
			Trial trial = originalEcrf.getTrial();
			ServiceUtil.checkTrialLocked(trial);
			ECRFOutVO original = ecrfDao.toECRFOutVO(originalEcrf);
			ecrfDao.evict(originalEcrf);
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			ecrf.setDeferredDelete(true);
			ecrf.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(ecrf, ecrf.getVersion(), now, user); // no opt. locking
			ecrfDao.update(ecrf);
			result = ecrfDao.toECRFOutVO(ecrf);
			ServiceUtil.logSystemMessage(ecrf.getTrial(), result.getTrial(), now, user, SystemMessageCodes.ECRF_MARKED_FOR_DELETION, result, original, journalEntryDao);
		} else {
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
			Trial trial = ecrf.getTrial();
			ServiceUtil.checkTrialLocked(trial);
			result = ecrfDao.toECRFOutVO(ecrf);
			trial.removeEcrfs(ecrf);
			Iterator<ProbandGroup> groupsIt = ecrf.getGroups().iterator();
			while (groupsIt.hasNext()) {
				groupsIt.next().removeEcrfs(ecrf);
			}
			ecrf.getGroups().clear();
			Iterator<Visit> visitsIt = ecrf.getVisits().iterator();
			while (visitsIt.hasNext()) {
				visitsIt.next().removeEcrfs(ecrf);
			}
			ecrf.getVisits().clear();
			boolean checkProbandLocked = Settings.getBoolean(SettingCodes.REMOVE_ECRF_CHECK_PROBAND_LOCKED, Bundle.SETTINGS,
					DefaultSettings.REMOVE_ECRF_CHECK_PROBAND_LOCKED);
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
			InputFieldValueDao inputFieldValueDao = this.getInputFieldValueDao();
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			NotificationDao notificationDao = this.getNotificationDao();
			NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
			Iterator<ECRFField> ecrfFieldsIt = ecrf.getEcrfFields().iterator();
			while (ecrfFieldsIt.hasNext()) {
				ECRFField ecrfField = ecrfFieldsIt.next();
				ecrfField.getField().removeEcrfFields(ecrfField);
				trial.removeEcrfFields(ecrfField);
				ServiceUtil.removeEcrfField(ecrfField, true, checkProbandLocked, now, user, true, true, ecrfFieldValueDao, ecrfFieldStatusEntryDao, inputFieldValueDao,
						journalEntryDao,
						ecrfFieldDao, notificationDao, notificationRecipientDao);
			}
			ecrf.getEcrfFields().clear();
			SignatureDao signatureDao = this.getSignatureDao();
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			Iterator<ECRFStatusEntry> ecrfStatusStatusEntriesIt = ecrf.getEcrfStatusEntries().iterator();
			while (ecrfStatusStatusEntriesIt.hasNext()) {
				ECRFStatusEntry ecrfStatusEntry = ecrfStatusStatusEntriesIt.next();
				ProbandListEntry listEntry = ecrfStatusEntry.getListEntry();
				listEntry.removeEcrfStatusEntries(ecrfStatusEntry);
				ServiceUtil.removeEcrfStatusEntry(ecrfStatusEntry, true, signatureDao, ecrfStatusEntryDao, notificationDao, notificationRecipientDao);
			}
			ecrf.getEcrfStatusEntries().clear();
			ecrfDao.remove(ecrf);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.ECRF_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected ECRFFieldOutVO handleDeleteEcrfField(AuthenticationVO auth, Long ecrfFieldId, boolean defer, boolean force, String deferredDeleteReason) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ECRFFieldOutVO result;
		if (!force && (defer
				|| this.getECRFFieldValueDao().getCount(ecrfFieldId, false) > 0
				|| this.getECRFFieldStatusEntryDao().getCount(ecrfFieldId, false) > 0)) {
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			ECRFField originalEcrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldId, ecrfFieldDao);
			Trial trial = originalEcrfField.getTrial();
			ServiceUtil.checkTrialLocked(trial);
			ECRFFieldOutVO original = ecrfFieldDao.toECRFFieldOutVO(originalEcrfField);
			ecrfFieldDao.evict(originalEcrfField);
			ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldId, ecrfFieldDao);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			ecrfField.setDeferredDelete(true);
			ecrfField.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(ecrfField, ecrfField.getVersion(), now, user); // no opt. locking
			ecrfFieldDao.update(ecrfField);
			result = ecrfFieldDao.toECRFFieldOutVO(ecrfField);
			ServiceUtil.logSystemMessage(ecrfField.getTrial(), result.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_MARKED_FOR_DELETION, result, original,
					this.getJournalEntryDao());
		} else {
			result = deleteEcrfFieldHelper(null, ecrfFieldId, true, true, true, now, user);
		}
		return result;
	}

	@Override
	protected ECRFFieldStatusEntryOutVO handleDeleteEcrfFieldStatusEntry(
			AuthenticationVO auth, Long ecrfFieldStatusEntryId) throws Exception {
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ECRFFieldStatusEntry fieldStatus = CheckIDUtil.checkEcrfFieldStatusEntryId(ecrfFieldStatusEntryId, ecrfFieldStatusEntryDao);
		ProbandListEntry listEntry = fieldStatus.getListEntry();
		this.getProbandListEntryDao().lock(listEntry, LockMode.PESSIMISTIC_WRITE);
		ECRFField ecrfField = fieldStatus.getEcrfField();
		ECRF ecrf = ecrfField.getEcrf();
		Visit visit = fieldStatus.getVisit();
		ServiceUtil.checkTrialLocked(ecrf.getTrial());
		if (!ecrf.getTrial().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_TRIAL,
					L10nUtil.getTrialStatusTypeName(Locales.USER, ecrf.getTrial().getStatus().getNameL10nKey()));
		}
		if (ecrf.isDisabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_ECRF, ecrf.getName());
		}
		ServiceUtil.checkProbandLocked(listEntry.getProband());
		ECRFStatusEntry ecrfStatusEntry = this.getECRFStatusEntryDao().findByListEntryEcrfVisit(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null);
		if (ecrfStatusEntry != null && ecrfStatusEntry.getStatus().isFieldStatusLockdown()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_INPUT_LOCKED_FOR_ECRF_STATUS,
					L10nUtil.getEcrfStatusTypeName(Locales.USER, ecrfStatusEntry.getStatus().getNameL10nKey()));
		}
		if (listEntry.getLastStatus() != null && !listEntry.getLastStatus().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
					L10nUtil.getProbandListStatusTypeName(Locales.USER, listEntry.getLastStatus().getStatus().getNameL10nKey()));
		}
		if (!fieldStatus.equals(ecrfFieldStatusEntryDao.findLastStatus(fieldStatus.getQueue(), listEntry.getId(), visit != null ? visit.getId() : null, ecrfField.getId(),
				fieldStatus.getIndex()))) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_NOT_LAST_ENTRY);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		if (fieldStatus.getStatus().isResolved()
				&& Settings.getEcrfFieldStatusQueueList(SettingCodes.RESOLVE_ECRF_FIELD_STATUS_RESTRICTION_QUEUES, Bundle.SETTINGS,
						DefaultSettings.RESOLVE_ECRF_FIELD_STATUS_RESTRICTION_QUEUES).contains(fieldStatus.getStatus().getQueue())) {
			checkTeamMemberResolve(listEntry.getTrial(), user);
		}
		ECRFFieldStatusEntryOutVO result = ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVO(fieldStatus);
		listEntry.removeEcrfFieldStatusEntries(fieldStatus);
		fieldStatus.getEcrfField().removeEcrfFieldStatusEntries(fieldStatus);
		ServiceUtil.removeEcrfFieldStatusEntry(fieldStatus, now, user,
				Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL),
				Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND),
				ecrfFieldStatusEntryDao, this.getJournalEntryDao(), this.getNotificationDao(),
				this.getNotificationRecipientDao());
		return result;
	}

	@Override
	protected InquiryOutVO handleDeleteInquiry(AuthenticationVO auth, Long inquiryId, boolean defer, boolean force, String deferredDeleteReason) throws Exception {
		InquiryDao inquiryDao = this.getInquiryDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		InquiryOutVO result;
		if (!force && (defer
				|| this.getInquiryValueDao().getCount(inquiryId) > 0)) {
			Inquiry originalInquiry = CheckIDUtil.checkInquiryId(inquiryId, inquiryDao);
			Trial trial = originalInquiry.getTrial();
			ServiceUtil.checkTrialLocked(trial);
			InquiryOutVO original = inquiryDao.toInquiryOutVO(originalInquiry);
			inquiryDao.evict(originalInquiry);
			Inquiry inquiry = CheckIDUtil.checkInquiryId(inquiryId, inquiryDao);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			inquiry.setDeferredDelete(true);
			inquiry.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(inquiry, inquiry.getVersion(), now, user); // no opt. locking
			inquiryDao.update(inquiry);
			result = inquiryDao.toInquiryOutVO(inquiry);
			ServiceUtil.logSystemMessage(inquiry.getTrial(), result.getTrial(), now, user, SystemMessageCodes.INQUIRY_MARKED_FOR_DELETION, result, original, journalEntryDao);
		} else {
			Inquiry inquiry = CheckIDUtil.checkInquiryId(inquiryId, inquiryDao);
			Trial trial = inquiry.getTrial();
			ServiceUtil.checkTrialLocked(trial);
			result = inquiryDao.toInquiryOutVO(inquiry);
			InputField field = inquiry.getField();
			trial.removeInquiries(inquiry);
			field.removeInquiries(inquiry);
			ServiceUtil.removeInquiry(inquiry, true, Settings.getBoolean(SettingCodes.REMOVE_INQUIRY_CHECK_PROBAND_LOCKED, Bundle.SETTINGS,
					DefaultSettings.REMOVE_INQUIRY_CHECK_PROBAND_LOCKED), now, user, true, true, this.getInquiryValueDao(), this.getInputFieldValueDao(),
					journalEntryDao, inquiryDao);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.INQUIRY_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected ProbandGroupOutVO handleDeleteProbandGroup(AuthenticationVO auth, Long probandGroupId, Long probandListStatusTypeId)
			throws Exception {
		ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
		ProbandGroup probandGroup = CheckIDUtil.checkProbandGroupId(probandGroupId, probandGroupDao);
		Trial trial = probandGroup.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		ProbandGroupOutVO result = probandGroupDao.toProbandGroupOutVO(probandGroup);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ECRFDao ecrfDao = this.getECRFDao();
		Iterator<ECRF> ecrfsIt = probandGroup.getEcrfs().iterator();
		while (ecrfsIt.hasNext()) {
			ECRF ecrf = ecrfsIt.next();
			ECRFOutVO original = ecrfDao.toECRFOutVO(ecrf);
			ecrf.removeGroups(probandGroup);
			CoreUtil.modifyVersion(ecrf, ecrf.getVersion(), now, user);
			ecrfDao.update(ecrf);
			ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(ecrf);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_GROUP_DELETED_ECRF_UPDATED, ecrfVO, original, journalEntryDao);
		}
		probandGroup.getEcrfs().clear();
		NotificationDao notificationDao = this.getNotificationDao();
		NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		Iterator<VisitScheduleItem> visitScheduleItemsIt = probandGroup.getVisitScheduleItems().iterator();
		while (visitScheduleItemsIt.hasNext()) {
			VisitScheduleItem visitScheduleItem = visitScheduleItemsIt.next();
			VisitScheduleItemOutVO visitScheduleItemVO = visitScheduleItemDao.toVisitScheduleItemOutVO(visitScheduleItem);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_GROUP_DELETED_VISIT_SCHEDULE_ITEM_DELETED, visitScheduleItemVO, null,
					journalEntryDao);
			ServiceUtil.removeNotifications(visitScheduleItem.getNotifications(), notificationDao, notificationRecipientDao);
			visitScheduleItem.setGroup(null);
			Iterator<DutyRosterTurn> dutyRosterTurnsIt = visitScheduleItem.getDutyRosterTurns().iterator();
			while (dutyRosterTurnsIt.hasNext()) {
				DutyRosterTurn dutyRosterTurn = dutyRosterTurnsIt.next();
				dutyRosterTurn.setVisitScheduleItem(null);
				CoreUtil.modifyVersion(dutyRosterTurn, dutyRosterTurn.getVersion(), now, user);
				dutyRosterTurnDao.update(dutyRosterTurn);
			}
			visitScheduleItem.getDutyRosterTurns().clear();
			visitScheduleItem.setTrial(null);
			trial.removeVisitScheduleItems(visitScheduleItem);
			visitScheduleItemDao.remove(visitScheduleItem);
		}
		probandGroup.getVisitScheduleItems().clear();
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		Iterator<ProbandListEntry> probandListEntriesIt = probandGroup.getProbandListEntries().iterator();
		while (probandListEntriesIt.hasNext()) {
			ProbandListEntry probandListEntry = probandListEntriesIt.next();
			ProbandListEntryOutVO original = probandListEntryDao.toProbandListEntryOutVO(probandListEntry);
			try {
				addProbandListEntryUpdatedProbandListStatusEntry(ProbandListStatusReasonCodes.GROUP_DELETED, null, probandListEntry, probandListStatusTypeId, now, user);
			} catch (ServiceException e) {
				// already end state
			}
			probandListEntry.setGroup(null);
			CoreUtil.modifyVersion(probandListEntry, probandListEntry.getVersion(), now, user);
			probandListEntryDao.update(probandListEntry);
			ProbandListEntryOutVO probandListEntryVO = probandListEntryDao.toProbandListEntryOutVO(probandListEntry);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_GROUP_DELETED_PROBAND_LIST_ENTRY_UPDATED, probandListEntryVO, original,
					journalEntryDao);
		}
		probandGroup.getProbandListEntries().clear();
		trial.removeGroups(probandGroup);
		probandGroup.setTrial(null);
		probandGroupDao.remove(probandGroup);
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_GROUP_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected Collection<ProbandListEntryOutVO> handleDeleteProbandListEntries(
			AuthenticationVO auth, Long trialId, Set<Long> probandIds, boolean shuffle, Long limit) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao, LockMode.PESSIMISTIC_WRITE);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ArrayList<ProbandListEntryOutVO> result = new ArrayList<ProbandListEntryOutVO>();
		ShuffleInfoVO shuffleInfo = new ShuffleInfoVO();
		shuffleInfo.setLimit(limit);
		if (probandIds != null) {
			Iterator<Long> probandIt;
			ArrayList<Long> ids = new ArrayList<Long>(probandIds);
			shuffleInfo.setInputIds(ids);
			if (shuffle) {
				long seed = SecureRandom.getInstance(SHUFFLE_SEED_RANDOM_ALGORITHM).nextLong();
				Random random = new Random(seed); // reproducable
				shuffleInfo.setPrngClass(CoreUtil.getPrngClassDescription(random));
				shuffleInfo.setSeed(seed);
				Collections.shuffle(ids, random);
				probandIt = ids.iterator();
			} else {
				probandIt = probandIds.iterator();
			}
			while (probandIt.hasNext() && (limit == null || result.size() < limit)) {
				Long probandId = probandIt.next();
				try {
					ProbandListEntry probandListEntry = probandListEntryDao.findByTrialProband(trialId, probandId);
					if (probandListEntry != null) {
						result.add(removeProbandListEntry(probandListEntry, now, user, true));
						shuffleInfo.getResultIds().add(probandId);
					}
				} catch (ServiceException e) {
				}
			}
		}
		logSystemMessage(trial, trialDao.toTrialOutVO(trial), now, user, shuffleInfo,
				shuffleInfo != null ? SystemMessageCodes.PROBAND_LIST_ENTRIES_SHUFFLED_AND_DELETED : SystemMessageCodes.PROBAND_LIST_ENTRIES_DELETED);
		return result;
	}

	@Override
	protected ProbandListEntryOutVO handleDeleteProbandListEntry(
			AuthenticationVO auth, Long probandListEntryId) throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry probandListEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return removeProbandListEntry(probandListEntry, now, user, true);
	}

	@Override
	protected ProbandListEntryTagOutVO handleDeleteProbandListEntryTag(
			AuthenticationVO auth, Long probandListEntryTagId) throws Exception {
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		ProbandListEntryTag probandListEntryTag = CheckIDUtil.checkProbandListEntryTagId(probandListEntryTagId, probandListEntryTagDao);
		Trial trial = probandListEntryTag.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		ProbandListEntryTagOutVO result = probandListEntryTagDao.toProbandListEntryTagOutVO(probandListEntryTag);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		InputField field = probandListEntryTag.getField();
		trial.removeListEntryTags(probandListEntryTag);
		field.removeListEntryTags(probandListEntryTag);
		ServiceUtil.removeProbandListEntryTag(probandListEntryTag, true, Settings.getBoolean(SettingCodes.REMOVE_LIST_ENTRY_TAG_CHECK_PROBAND_LOCKED, Bundle.SETTINGS,
				DefaultSettings.REMOVE_LIST_ENTRY_TAG_CHECK_PROBAND_LOCKED), now, user, true, true, this.getProbandListEntryTagValueDao(),
				this.getInputFieldValueDao(), journalEntryDao, probandListEntryTagDao, this.getVisitScheduleItemDao());
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected ProbandListStatusEntryOutVO handleDeleteProbandListStatusEntry(
			AuthenticationVO auth, Long probandListStatusEntryId) throws Exception {
		ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListStatusEntry probandListStatusEntry = CheckIDUtil.checkProbandListStatusEntryId(probandListStatusEntryId, probandListStatusEntryDao);
		ProbandListEntry listEntry = probandListStatusEntry.getListEntry();
		ServiceUtil.checkTrialLocked(listEntry.getTrial());
		ServiceUtil.checkProbandLocked(listEntry.getProband());
		if (!probandListStatusEntry.equals(listEntry.getLastStatus())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_STATUS_ENTRY_NOT_LAST_ENTRY);
		}
		ProbandListStatusEntryOutVO result = probandListStatusEntryDao.toProbandListStatusEntryOutVO(probandListStatusEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		listEntry.setLastStatus(null);
		probandListEntryDao.update(listEntry);
		listEntry.removeStatusEntries(probandListStatusEntry);
		probandListStatusEntry.setListEntry(null);
		probandListStatusEntryDao.remove(probandListStatusEntry);
		listEntry.setLastStatus(probandListStatusEntryDao.findLastStatus(listEntry.getId()));
		probandListEntryDao.update(listEntry);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(listEntry.getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_STATUS_ENTRY_DELETED, result, null,
				journalEntryDao);
		ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.PROBAND_LIST_STATUS_ENTRY_DELETED, result, null,
				journalEntryDao);
		return result;
	}

	@Override
	protected StratificationRandomizationListOutVO handleDeleteStratificationRandomizationList(AuthenticationVO auth, Long stratificationRandomizationListId) throws Exception {
		StratificationRandomizationListDao stratificationRandomizationListDao = this.getStratificationRandomizationListDao();
		StratificationRandomizationList randomizationList = CheckIDUtil.checkStratificationRandomizationListId(stratificationRandomizationListId,
				stratificationRandomizationListDao);
		Trial trial = randomizationList.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		StratificationRandomizationListOutVO result = stratificationRandomizationListDao.toStratificationRandomizationListOutVO(randomizationList);
		trial.removeRandomizationLists(randomizationList);
		ServiceUtil.removeStratificationRandomizationList(randomizationList, true, stratificationRandomizationListDao,
				this.getRandomizationListCodeDao(), this.getRandomizationListCodeValueDao());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.STRATIFICATION_RANDOMIZATION_LIST_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TeamMemberOutVO handleDeleteTeamMember(AuthenticationVO auth, Long teamMemberId)
			throws Exception {
		TeamMemberDao teamMemberDao = this.getTeamMemberDao();
		TeamMember teamMember = CheckIDUtil.checkTeamMemberId(teamMemberId, teamMemberDao);
		Trial trial = teamMember.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		Staff staff = teamMember.getStaff();
		TeamMemberOutVO result = teamMemberDao.toTeamMemberOutVO(teamMember);
		trial.removeMembers(teamMember);
		teamMember.setTrial(null);
		staff.removeTrialMemberships(teamMember);
		teamMember.setStaff(null);
		teamMemberDao.remove(teamMember);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(trial, result.getStaff(), now, user, SystemMessageCodes.TEAM_MEMBER_DELETED, result, null, journalEntryDao);
		ServiceUtil.logSystemMessage(staff, result.getTrial(), now, user, SystemMessageCodes.TEAM_MEMBER_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected TimelineEventOutVO handleDeleteTimelineEvent(AuthenticationVO auth, Long timelineEventId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent timelineEvent = CheckIDUtil.checkTimelineEventId(timelineEventId, timelineEventDao);
		Trial trial = timelineEvent.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent, maxInstances, maxParentDepth, maxChildrenDepth);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		Iterator<TimelineEvent> childrenIt = timelineEvent.getChildren().iterator();
		while (childrenIt.hasNext()) {
			TimelineEvent child = childrenIt.next();
			child.setParent(null);
			CoreUtil.modifyVersion(child, child.getVersion(), now, user);
			timelineEventDao.update(child);
			TimelineEventOutVO childVO = timelineEventDao.toTimelineEventOutVO(child);
		}
		timelineEvent.getChildren().clear();
		TimelineEvent parent = timelineEvent.getParent();
		if (parent != null) {
			parent.removeChildren(timelineEvent);
			timelineEvent.setParent(null);
			timelineEventDao.update(parent);
		}
		trial.removeEvents(timelineEvent);
		timelineEvent.setTrial(null);
		ServiceUtil.removeNotifications(timelineEvent.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		timelineEventDao.remove(timelineEvent);
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.TIMELINE_EVENT_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TrialOutVO handleDeleteTrial(AuthenticationVO auth, Long trialId, boolean defer, boolean force, String deferredDeleteReason) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		TrialOutVO result;
		if (!force && defer) {
			Trial originalTrial = CheckIDUtil.checkTrialId(trialId, trialDao);
			TrialOutVO original = trialDao.toTrialOutVO(originalTrial);
			trialDao.evict(originalTrial);
			Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao, LockMode.PESSIMISTIC_WRITE);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			trial.setDeferredDelete(true);
			trial.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(trial, originalTrial.getVersion(), now, user); // no opt. locking
			trialDao.update(trial);
			result = trialDao.toTrialOutVO(trial);
			ServiceUtil.logSystemMessage(trial, result, now, user, SystemMessageCodes.TRIAL_MARKED_FOR_DELETION, result, original, journalEntryDao);
		} else {
			Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao, LockMode.PESSIMISTIC_WRITE);
			result = trialDao.toTrialOutVO(trial);
			boolean checkProbandLocked = Settings.getBoolean(SettingCodes.REMOVE_TRIAL_CHECK_PROBAND_LOCKED, Bundle.SETTINGS,
					DefaultSettings.REMOVE_TRIAL_CHECK_PROBAND_LOCKED);
			NotificationDao notificationDao = this.getNotificationDao();
			NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
			TrialTagValueDao trialTagValueDao = this.getTrialTagValueDao();
			Iterator<TrialTagValue> trialTagValuesIt = trial.getTagValues().iterator();
			while (trialTagValuesIt.hasNext()) {
				TrialTagValue tagValue = trialTagValuesIt.next();
				tagValue.setTrial(null);
				trialTagValueDao.remove(tagValue);
			}
			trial.getTagValues().clear();
			TeamMemberDao teamMemberDao = this.getTeamMemberDao();
			Iterator<TeamMember> membersIt = trial.getMembers().iterator();
			while (membersIt.hasNext()) {
				TeamMember member = membersIt.next();
				Staff staff = member.getStaff();
				TeamMemberOutVO trialMembershipVO = teamMemberDao.toTeamMemberOutVO(member);
				ServiceUtil.logSystemMessage(staff, result, now, user, SystemMessageCodes.TRIAL_DELETED_TRIAL_MEMBERSHIP_DELETED, trialMembershipVO, null, journalEntryDao);
				staff.removeTrialMemberships(member);
				member.setStaff(null);
				member.setTrial(null);
				teamMemberDao.remove(member);
			}
			trial.getMembers().clear();
			TimelineEventDao timelineEventDao = this.getTimelineEventDao();
			Iterator<TimelineEvent> eventsIt = trial.getEvents().iterator();
			while (eventsIt.hasNext()) {
				TimelineEvent event = eventsIt.next();
				event.setTrial(null);
				Iterator<TimelineEvent> childrenIt = event.getChildren().iterator();
				while (childrenIt.hasNext()) {
					TimelineEvent child = childrenIt.next();
					child.setParent(null);
					timelineEventDao.update(child);
				}
				event.getChildren().clear();
				TimelineEvent parent = event.getParent();
				if (parent != null) {
					parent.removeChildren(event);
					event.setParent(null);
					timelineEventDao.update(parent);
				}
				ServiceUtil.removeNotifications(event.getNotifications(), notificationDao, notificationRecipientDao);
				timelineEventDao.remove(event);
			}
			trial.getEvents().clear();
			InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
			Iterator<InventoryBooking> bookingsIt = trial.getInventoryBookings().iterator();
			while (bookingsIt.hasNext()) {
				InventoryBooking booking = bookingsIt.next();
				InventoryBookingOutVO original = inventoryBookingDao.toInventoryBookingOutVO(booking);
				booking.setTrial(null);
				CoreUtil.modifyVersion(booking, booking.getVersion(), now, user);
				inventoryBookingDao.update(booking);
				InventoryBookingOutVO bookingVO = inventoryBookingDao.toInventoryBookingOutVO(booking);
				logSystemMessage(booking.getInventory(), result, now, user, SystemMessageCodes.TRIAL_DELETED_BOOKING_UPDATED, bookingVO, original, journalEntryDao);
			}
			trial.getInventoryBookings().clear();
			DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
			VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
			Iterator<VisitScheduleItem> visitScheduleItemsIt = trial.getVisitScheduleItems().iterator();
			while (visitScheduleItemsIt.hasNext()) {
				VisitScheduleItem visitScheduleItem = visitScheduleItemsIt.next();
				Trial visitScheduleItemTrial = visitScheduleItem.getTrial();
				ProbandGroup group = visitScheduleItem.getGroup();
				Visit visit = visitScheduleItem.getVisit();
				ProbandListEntryTag startTag = visitScheduleItem.getStartTag();
				ProbandListEntryTag stopTag = visitScheduleItem.getStopTag();
				Iterator<DutyRosterTurn> dutyRosterTurnIt = visitScheduleItem.getDutyRosterTurns().iterator();
				while (dutyRosterTurnIt.hasNext()) {
					DutyRosterTurn dutyRosterTurn = dutyRosterTurnIt.next();
					Staff staff = dutyRosterTurn.getStaff();
					DutyRosterTurnOutVO original = null;
					if (staff != null) {
						original = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
					}
					visitScheduleItemTrial.removeDutyRosterTurns(dutyRosterTurn);
					dutyRosterTurn.setTrial(null);
					dutyRosterTurn.setVisitScheduleItem(null);
					CoreUtil.modifyVersion(dutyRosterTurn, dutyRosterTurn.getVersion(), now, user);
					dutyRosterTurnDao.update(dutyRosterTurn);
					if (staff != null) {
						DutyRosterTurnOutVO dutyRosterTurnVO = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
						ServiceUtil.logSystemMessage(staff, result, now, user, SystemMessageCodes.TRIAL_DELETED_DUTY_ROSTER_TURN_UPDATED, dutyRosterTurnVO, original,
								journalEntryDao);
					}
				}
				visitScheduleItem.getDutyRosterTurns().clear();
				visitScheduleItem.setTrial(null);
				if (group != null) {
					group.removeVisitScheduleItems(visitScheduleItem);
					visitScheduleItem.setGroup(null);
				}
				if (visit != null) {
					visit.removeVisitScheduleItems(visitScheduleItem);
					visitScheduleItem.setVisit(null);
				}
				if (startTag != null) { // required for removeProbandListEntryTag
					startTag.removeStartDates(visitScheduleItem);
					visitScheduleItem.setStartTag(null);
				}
				if (stopTag != null) { // required for removeProbandListEntryTag
					stopTag.removeStopDates(visitScheduleItem);
					visitScheduleItem.setStopTag(null);
				}
				ServiceUtil.removeNotifications(visitScheduleItem.getNotifications(), notificationDao, notificationRecipientDao);
				visitScheduleItemDao.remove(visitScheduleItem);
			}
			trial.getVisitScheduleItems().clear();
			InputFieldValueDao inputFieldValueDao = this.getInputFieldValueDao();
			ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
			ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
			Iterator<ProbandListEntryTag> listEntryTagsIt = trial.getListEntryTags().iterator();
			while (listEntryTagsIt.hasNext()) {
				ProbandListEntryTag probandListEntryTag = listEntryTagsIt.next();
				probandListEntryTag.getField().removeListEntryTags(probandListEntryTag);
				ServiceUtil.removeProbandListEntryTag(probandListEntryTag, true, checkProbandLocked, now, user, false, true, probandListEntryTagValueDao,
						inputFieldValueDao,
						journalEntryDao,
						probandListEntryTagDao,
						visitScheduleItemDao);
			}
			trial.getListEntryTags().clear();
			StratificationRandomizationListDao stratificationRandomizationListDao = this.getStratificationRandomizationListDao();
			RandomizationListCodeDao randomizationListCodeDao = this.getRandomizationListCodeDao();
			RandomizationListCodeValueDao randomizationListCodeValueDao = this.getRandomizationListCodeValueDao();
			Iterator<StratificationRandomizationList> randomizationListsIt = trial.getRandomizationLists().iterator();
			while (randomizationListsIt.hasNext()) {
				StratificationRandomizationList randomizationList = randomizationListsIt.next();
				ServiceUtil.removeStratificationRandomizationList(randomizationList, true, stratificationRandomizationListDao,
						randomizationListCodeDao, randomizationListCodeValueDao);
			}
			trial.getRandomizationLists().clear();
			Iterator<RandomizationListCode> randomizationListCodesIt = trial.getRandomizationCodes().iterator();
			while (randomizationListCodesIt.hasNext()) {
				RandomizationListCode randomizationListCode = randomizationListCodesIt.next();
				ServiceUtil.removeRandomizationListCode(randomizationListCode, true,
						randomizationListCodeDao, randomizationListCodeValueDao);
			}
			trial.getRandomizationCodes().clear();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			Iterator<ECRFField> ecrfFieldsIt = trial.getEcrfFields().iterator();
			while (ecrfFieldsIt.hasNext()) {
				ECRFField ecrfField = ecrfFieldsIt.next();
				ecrfField.getField().removeEcrfFields(ecrfField);
				ecrfField.getEcrf().removeEcrfFields(ecrfField);
				ServiceUtil.removeEcrfField(ecrfField, true, checkProbandLocked, now, user, false, true, ecrfFieldValueDao, ecrfFieldStatusEntryDao, inputFieldValueDao,
						journalEntryDao,
						ecrfFieldDao, notificationDao, notificationRecipientDao);
			}
			trial.getEcrfFields().clear();
			InquiryDao inquiryDao = this.getInquiryDao();
			InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
			Iterator<Inquiry> inquiriesIt = trial.getInquiries().iterator();
			while (inquiriesIt.hasNext()) {
				Inquiry inquiry = inquiriesIt.next();
				inquiry.getField().removeInquiries(inquiry);
				ServiceUtil.removeInquiry(inquiry, true, checkProbandLocked, now, user, false, true, inquiryValueDao, inputFieldValueDao, journalEntryDao,
						inquiryDao);
			}
			trial.getInquiries().clear();
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			SignatureDao signatureDao = this.getSignatureDao();
			ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
			ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
			Iterator<ProbandListEntry> probandListEntriesIt = trial.getProbandListEntries().iterator();
			while (probandListEntriesIt.hasNext()) {
				ProbandListEntry probandListEntry = probandListEntriesIt.next();
				if (checkProbandLocked) {
					ServiceUtil.checkProbandLocked(probandListEntry.getProband());
				}
				probandListEntry.getProband().removeTrialParticipations(probandListEntry);
				ProbandGroup group = probandListEntry.getGroup();
				if (group != null) {
					group.removeProbandListEntries(probandListEntry);
				}
				ServiceUtil.removeProbandListEntry(probandListEntry, true, now, user, false, true, probandListStatusEntryDao, probandListEntryTagValueDao, ecrfFieldValueDao,
						ecrfFieldStatusEntryDao,
						signatureDao, ecrfStatusEntryDao, inputFieldValueDao, journalEntryDao, probandListEntryDao, notificationDao, notificationRecipientDao);
			}
			trial.getProbandListEntries().clear();
			ECRFDao ecrfDao = this.getECRFDao();
			Iterator<ECRF> ecrfsIt = trial.getEcrfs().iterator();
			while (ecrfsIt.hasNext()) {
				ECRF ecrf = ecrfsIt.next();
				ecrf.setTrial(null);
				Iterator<ProbandGroup> groupsIt = ecrf.getGroups().iterator();
				while (groupsIt.hasNext()) {
					groupsIt.next().removeEcrfs(ecrf);
				}
				ecrf.getGroups().clear();
				Iterator<Visit> visitsIt = ecrf.getVisits().iterator();
				while (visitsIt.hasNext()) {
					visitsIt.next().removeEcrfs(ecrf);
				}
				ecrf.getVisits().clear();
				ecrfDao.remove(ecrf);
			}
			trial.getEcrfs().clear();
			Iterator<DutyRosterTurn> trialDutyRosterTurnIt = trial.getDutyRosterTurns().iterator();
			while (trialDutyRosterTurnIt.hasNext()) {
				DutyRosterTurn dutyRosterTurn = trialDutyRosterTurnIt.next();
				Staff staff = dutyRosterTurn.getStaff();
				DutyRosterTurnOutVO original = null;
				if (staff != null) {
					original = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
				}
				VisitScheduleItem visitScheduleItem = dutyRosterTurn.getVisitScheduleItem();
				if (visitScheduleItem != null) {
					visitScheduleItem.removeDutyRosterTurns(dutyRosterTurn);
					dutyRosterTurn.setVisitScheduleItem(null);
				}
				dutyRosterTurn.setTrial(null);
				CoreUtil.modifyVersion(dutyRosterTurn, dutyRosterTurn.getVersion(), now, user);
				dutyRosterTurnDao.update(dutyRosterTurn);
				if (staff != null) {
					DutyRosterTurnOutVO dutyRosterTurnVO = dutyRosterTurnDao.toDutyRosterTurnOutVO(dutyRosterTurn);
					ServiceUtil.logSystemMessage(staff, result, now, user, SystemMessageCodes.TRIAL_DELETED_DUTY_ROSTER_TURN_UPDATED, dutyRosterTurnVO, original,
							journalEntryDao);
				}
			}
			trial.getDutyRosterTurns().clear();
			MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
			Iterator<MoneyTransfer> payoffsIt = trial.getPayoffs().iterator();
			while (payoffsIt.hasNext()) {
				MoneyTransfer moneyTransfer = payoffsIt.next();
				if (checkProbandLocked) {
					ServiceUtil.checkProbandLocked(moneyTransfer.getProband());
				}
				MoneyTransferOutVO original = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
				moneyTransfer.setTrial(null);
				CoreUtil.modifyVersion(moneyTransfer, moneyTransfer.getVersion(), now, user);
				moneyTransferDao.update(moneyTransfer);
				MoneyTransferOutVO moneyTransferVO = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
				ServiceUtil.logSystemMessage(moneyTransfer.getProband(), result, now, user, SystemMessageCodes.TRIAL_DELETED_MONEY_TRANSFER_UPDATED, moneyTransferVO, original,
						journalEntryDao);
			}
			trial.getPayoffs().clear();
			VisitDao visitDao = this.getVisitDao();
			Iterator<Visit> visitsIt = trial.getVisits().iterator();
			while (visitsIt.hasNext()) {
				Visit visit = visitsIt.next();
				visit.setTrial(null);
				visitDao.remove(visit);
			}
			trial.getVisits().clear();
			ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
			Iterator<ProbandGroup> groupsIt = trial.getGroups().iterator();
			while (groupsIt.hasNext()) {
				ProbandGroup group = groupsIt.next();
				group.setTrial(null);
				probandGroupDao.remove(group);
			}
			trial.getGroups().clear();
			HyperlinkDao hyperlinkDao = this.getHyperlinkDao();
			Iterator<Hyperlink> hyperlinksIt = trial.getHyperlinks().iterator();
			while (hyperlinksIt.hasNext()) {
				Hyperlink hyperlink = hyperlinksIt.next();
				hyperlink.setTrial(null);
				hyperlinkDao.remove(hyperlink);
			}
			trial.getHyperlinks().clear();
			Iterator<Signature> signaturesIt = trial.getSignatures().iterator();
			while (signaturesIt.hasNext()) {
				Signature signature = signaturesIt.next();
				signature.setTrial(null);
				signatureDao.remove(signature);
			}
			trial.getSignatures().clear();
			JobDao jobDao = this.getJobDao();
			Iterator<Job> jobsIt = trial.getJobs().iterator();
			while (jobsIt.hasNext()) {
				Job job = jobsIt.next();
				job.setTrial(null);
				jobDao.remove(job);
			}
			trial.getJobs().clear();
			JobTypeDao jobTypeDao = this.getJobTypeDao();
			Iterator<JobType> jobTypesIt = trial.getJobTypes().iterator();
			while (jobTypesIt.hasNext()) {
				JobType jobType = jobTypesIt.next();
				jobType.setTrial(null);
				jobTypeDao.remove(jobType);
			}
			trial.getJobTypes().clear();
			Iterator<JournalEntry> journalEntriesIt = trial.getJournalEntries().iterator();
			while (journalEntriesIt.hasNext()) {
				JournalEntry journalEntry = journalEntriesIt.next();
				journalEntry.setTrial(null);
				journalEntryDao.remove(journalEntry);
			}
			trial.getJournalEntries().clear();
			FileDao fileDao = this.getFileDao();
			Iterator<File> filesIt = trial.getFiles().iterator();
			while (filesIt.hasNext()) {
				File file = filesIt.next();
				file.setTrial(null);
				fileDao.remove(file);
			}
			trial.getFiles().clear();
			CourseDao courseDao = this.getCourseDao();
			Iterator<Course> coursesIt = trial.getCourses().iterator();
			while (coursesIt.hasNext()) {
				Course course = coursesIt.next();
				CourseOutVO original = courseDao.toCourseOutVO(course);
				course.setTrial(null);
				CoreUtil.modifyVersion(course, course.getVersion(), now, user);
				courseDao.update(course);
				CourseOutVO courseVO = courseDao.toCourseOutVO(course);
				logSystemMessage(course, result, now, user, SystemMessageCodes.TRIAL_DELETED_COURSE_UPDATED, courseVO, original, journalEntryDao);
			}
			trial.getCourses().clear();
			MassMailDao massMailDao = this.getMassMailDao();
			Iterator<MassMail> massMailsIt = trial.getMassMails().iterator();
			while (massMailsIt.hasNext()) {
				MassMail massMail = massMailsIt.next();
				MassMailOutVO original = massMailDao.toMassMailOutVO(massMail);
				massMail.setTrial(null);
				CoreUtil.modifyVersion(massMail, massMail.getVersion(), now, user);
				massMailDao.update(massMail);
				MassMailOutVO massMailVO = massMailDao.toMassMailOutVO(massMail);
				logSystemMessage(massMail, result, now, user, SystemMessageCodes.TRIAL_DELETED_MASS_MAIL_UPDATED, massMailVO, original, journalEntryDao);
			}
			trial.getMassMails().clear();
			ServiceUtil.removeNotifications(trial.getNotifications(), notificationDao, notificationRecipientDao);
			trialDao.remove(trial);
			logSystemMessage(user, result, now, user, SystemMessageCodes.TRIAL_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected TrialTagValueOutVO handleDeleteTrialTagValue(AuthenticationVO auth, Long trialTagValueId)
			throws Exception {
		TrialTagValueDao tagValueDao = this.getTrialTagValueDao();
		TrialTagValue tagValue = CheckIDUtil.checkTrialTagValueId(trialTagValueId, tagValueDao);
		Trial trial = tagValue.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		TrialTagValueOutVO result = tagValueDao.toTrialTagValueOutVO(tagValue);
		trial.removeTagValues(tagValue);
		tagValue.setTrial(null);
		tagValueDao.remove(tagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.TRIAL_TAG_VALUE_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected VisitOutVO handleDeleteVisit(AuthenticationVO auth, Long visitId) throws Exception {
		VisitDao visitDao = this.getVisitDao();
		Visit visit = CheckIDUtil.checkVisitId(visitId, visitDao);
		Trial trial = visit.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		VisitOutVO result = visitDao.toVisitOutVO(visit);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ECRFDao ecrfDao = this.getECRFDao();
		SignatureDao signatureDao = this.getSignatureDao();
		NotificationDao notificationDao = this.getNotificationDao();
		NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		Iterator<ECRF> ecrfsIt = visit.getEcrfs().iterator();
		while (ecrfsIt.hasNext()) {
			ECRF ecrf = ecrfsIt.next();
			ecrfDao.lock(ecrf, LockMode.PESSIMISTIC_WRITE);
			ECRFOutVO original = ecrfDao.toECRFOutVO(ecrf);
			Iterator<ECRFStatusEntry> ecrfStatusEntryIt = ecrfStatusEntryDao.findByTrialListEntryEcrfVisitValidationStatus(null, null, ecrf.getId(), visitId, null, null)
					.iterator();
			//delete ecrf status entries of empty ecrfs
			while (ecrfStatusEntryIt.hasNext()) {
				ECRFStatusEntry ecrfStatusEntry = ecrfStatusEntryIt.next();
				if (ecrfFieldValueDao.getCount(ecrf.getId(), visitId) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_VISIT_WITH_VALUES,
							CommonUtil.getEcrfVisitName(ecrf.getName(), visit.getToken(), ecrf.getVisits().size()));
				} else if (ecrfFieldStatusEntryDao.getCount(ecrf.getId(), visitId) > 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_VISIT_WITH_STATUS_ENTRIES,
							CommonUtil.getEcrfVisitName(ecrf.getName(), visit.getToken(), ecrf.getVisits().size()));
				} else {
					ProbandListEntry listEntry = ecrfStatusEntry.getListEntry();
					listEntry.removeEcrfStatusEntries(ecrfStatusEntry);
					ecrf.removeEcrfStatusEntries(ecrfStatusEntry);
					ServiceUtil.removeEcrfStatusEntry(ecrfStatusEntry, true,
							signatureDao, ecrfStatusEntryDao, notificationDao, notificationRecipientDao);
				}
			}
			ecrf.removeVisits(visit);
			CoreUtil.modifyVersion(ecrf, ecrf.getVersion(), now, user);
			ecrfDao.update(ecrf);
			ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(ecrf);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.VISIT_DELETED_ECRF_UPDATED, ecrfVO, original, journalEntryDao);
		}
		visit.getEcrfs().clear();
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		Iterator<VisitScheduleItem> visitScheduleItemsIt = visit.getVisitScheduleItems().iterator();
		while (visitScheduleItemsIt.hasNext()) {
			VisitScheduleItem visitScheduleItem = visitScheduleItemsIt.next();
			VisitScheduleItemOutVO visitScheduleItemVO = visitScheduleItemDao.toVisitScheduleItemOutVO(visitScheduleItem);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.VISIT_DELETED_VISIT_SCHEDULE_ITEM_DELETED, visitScheduleItemVO, null,
					journalEntryDao);
			ServiceUtil.removeNotifications(visitScheduleItem.getNotifications(), notificationDao, notificationRecipientDao);
			visitScheduleItem.setVisit(null);
			Iterator<DutyRosterTurn> dutyRosterTurnsIt = visitScheduleItem.getDutyRosterTurns().iterator();
			while (dutyRosterTurnsIt.hasNext()) {
				DutyRosterTurn dutyRosterTurn = dutyRosterTurnsIt.next();
				dutyRosterTurn.setVisitScheduleItem(null);
				CoreUtil.modifyVersion(dutyRosterTurn, dutyRosterTurn.getVersion(), now, user);
				dutyRosterTurnDao.update(dutyRosterTurn);
			}
			visitScheduleItem.getDutyRosterTurns().clear();
			visitScheduleItem.setTrial(null);
			trial.removeVisitScheduleItems(visitScheduleItem);
			visitScheduleItemDao.remove(visitScheduleItem);
		}
		visit.getVisitScheduleItems().clear();
		trial.removeVisits(visit);
		visit.setTrial(null);
		visitDao.remove(visit);
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.VISIT_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected VisitScheduleItemOutVO handleDeleteVisitScheduleItem(
			AuthenticationVO auth, Long visitScheduleItemId) throws Exception {
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		VisitScheduleItem visitScheduleItem = CheckIDUtil.checkVisitScheduleItemId(visitScheduleItemId, visitScheduleItemDao);
		Trial trial = visitScheduleItem.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		ProbandGroup group = visitScheduleItem.getGroup();
		Visit visit = visitScheduleItem.getVisit();
		ProbandListEntryTag startTag = visitScheduleItem.getStartTag();
		ProbandListEntryTag stopTag = visitScheduleItem.getStopTag();
		VisitScheduleItemOutVO result = visitScheduleItemDao.toVisitScheduleItemOutVO(visitScheduleItem);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		trial.removeVisitScheduleItems(visitScheduleItem);
		visitScheduleItem.setTrial(null);
		if (group != null) {
			group.removeVisitScheduleItems(visitScheduleItem);
			visitScheduleItem.setGroup(null);
		}
		if (visit != null) {
			visit.removeVisitScheduleItems(visitScheduleItem);
			visitScheduleItem.setVisit(null);
		}
		if (startTag != null) {
			startTag.removeStartDates(visitScheduleItem);
			visitScheduleItem.setStartTag(null);
		}
		if (stopTag != null) {
			stopTag.removeStopDates(visitScheduleItem);
			visitScheduleItem.setStopTag(null);
		}
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		Iterator<DutyRosterTurn> dutyRosterTurnsIt = visitScheduleItem.getDutyRosterTurns().iterator();
		while (dutyRosterTurnsIt.hasNext()) {
			DutyRosterTurn dutyRosterTurn = dutyRosterTurnsIt.next();
			dutyRosterTurn.setVisitScheduleItem(null);
			CoreUtil.modifyVersion(dutyRosterTurn, dutyRosterTurn.getVersion(), now, user);
			dutyRosterTurnDao.update(dutyRosterTurn);
		}
		visitScheduleItem.getDutyRosterTurns().clear();
		ServiceUtil.removeNotifications(visitScheduleItem.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		visitScheduleItemDao.remove(visitScheduleItem);
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.VISIT_SCHEDULE_ITEM_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	private static void checkEcrfVisit(ECRF ecrf, Visit visit) throws ServiceException {
		if (visit != null) {
			if (!ecrf.getVisits().contains(visit)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_VISIT_FOR_ECRF);
			}
		} else {
			if (ecrf.getVisits().size() > 0) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INVALID_VISIT_FOR_ECRF);
			}
		}
	}

	@Override
	protected AuditTrailExcelVO handleExportAuditTrail(
			AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId, Long visitId) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = null;
		TrialOutVO trialVO = null;
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
			trialVO = trialDao.toTrialOutVO(trial);
		}
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = null;
		ProbandListEntryOutVO listEntryVO = null;
		if (probandListEntryId != null) {
			listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
			listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntry);
			trial = listEntry.getTrial();
			trialVO = listEntryVO.getTrial();
		}
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF ecrf = null;
		ECRFOutVO ecrfVO = null;
		VisitOutVO visitVO = null;
		if (ecrfId != null) {
			ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
			ecrfVO = ecrfDao.toECRFOutVO(ecrf);
			trial = ecrf.getTrial();
			trialVO = ecrfVO.getTrial();
			Visit visit = null;
			if (visitId != null) {
				visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
				visitVO = this.getVisitDao().toVisitOutVO(visit);
			}
			checkEcrfVisit(ecrf, visit);
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_ID_NOT_NULL);
			}
		}
		boolean passDecryption = CoreUtil.isPassDecryption();
		ECRFFieldStatusQueue[] queues = Settings.getEcrfFieldStatusQueueList(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_QUEUES, Bundle.AUDIT_TRAIL_EXCEL,
				AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_QUEUES).toArray(new ECRFFieldStatusQueue[0]);
		AuditTrailExcelWriter writer = ExcelWriterFactory.createAuditTrailExcelWriter(!passDecryption, queues);
		writer.setTrial(trialVO);
		writer.setListEntry(listEntryVO);
		writer.setEcrf(ecrfVO);
		writer.setVisit(visitVO);
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		Collection fieldValues = ecrfFieldValueDao.getLog(trialId, probandListEntryId, ecrfId, visitId, true, null);
		ArrayList<ECRFFieldValueOutVO> fieldValueVOs = new ArrayList<ECRFFieldValueOutVO>(fieldValues.size());
		Iterator<ECRFFieldValue> fieldValuesIt = fieldValues.iterator();
		while (fieldValuesIt.hasNext()) {
			ECRFFieldValueOutVO fieldValueVO = ecrfFieldValueDao.toECRFFieldValueOutVO(fieldValuesIt.next());
			if (CommonUtil.HTML_AUDIT_TRAIL_CHANGE_COMMENTS) {
				fieldValueVO.setChangeComment(diff_match_patch.prettyHtmlToUnicode(fieldValueVO.getChangeComment()));
			}
			fieldValueVOs.add(fieldValueVO);
		}
		writer.setVOs(fieldValueVOs);
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		for (int i = 0; i < queues.length; i++) {
			Collection statusEntries = ecrfFieldStatusEntryDao.getLog(queues[i], trialId, probandListEntryId, ecrfId, visitId, false, true, null);
			ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(statusEntries);
			writer.setVOs(queues[i], statusEntries);
		}
		User user = CoreUtil.getUser();
		writer.getExcelVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new ExcelExporter(writer, writer)).write();
		AuditTrailExcelVO result = writer.getExcelVO();
		ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.AUDIT_TRAIL_EXPORTED, result,
				null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandListExcelVO handleExportProbandList(
			AuthenticationVO auth, Long trialId, ProbandListStatusLogLevel logLevel) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialOutVO trialVO = trialDao.toTrialOutVO(trial);
		boolean passDecryption = CoreUtil.isPassDecryption();
		ProbandListExcelWriter writer = ExcelWriterFactory.createProbandListExcelWriter(logLevel, !passDecryption);
		writer.setTrial(trialVO);
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		Collection probandListEntries = probandListEntryDao.getProbandList(trialId, logLevel, false);
		boolean showProbandListEntryTags;
		boolean showAllProbandListEntryTags;
		boolean showAllProbandListEntryTagDates;
		boolean showInquiries;
		boolean showAllInquiries;
		boolean showAllInquiryDates;
		boolean showAddresses;
		boolean showContactDetails;
		boolean showTags;
		boolean showEnrollmentStatusLog;
		boolean aggregateAddresses;
		boolean aggregateContactDetails;
		boolean showICDate;
		boolean showICAge;
		boolean showScreeningDate;
		boolean showScreeningReason;
		ExcelCellFormat rowCellFormat;
		if (logLevel != null) {
			switch (logLevel) {
				case ENROLLMENT:
					showProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_PROBAND_LIST_ENTRY_TAGS);
					showAllProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS);
					showAllProbandListEntryTagDates = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES,
							Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES);
					showInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_INQUIRIES);
					showAllInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_ALL_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_ALL_INQUIRIES);
					showAllInquiryDates = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_ALL_INQUIRY_DATES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_ALL_INQUIRY_DATES);
					showAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_ADDRESSES);
					showContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_CONTACT_DETAILS);
					showTags = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_TAGS);
					showEnrollmentStatusLog = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_ENROLLMENT_STATUS_LOG, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_ENROLLMENT_STATUS_LOG);
					aggregateAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_AGGREGATE_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_AGGREGATE_ADDRESSES);
					aggregateContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_AGGREGATE_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_AGGREGATE_CONTACT_DETAILS);
					showICDate = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_IC_DATE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_IC_DATE);
					showICAge = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_IC_AGE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_IC_AGE);
					showScreeningDate = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_SCREENING_DATE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_SCREENING_DATE);
					showScreeningReason = Settings.getBoolean(ProbandListExcelSettingCodes.ENROLLMENT_LOG_SHOW_SCREENING_REASON, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_SHOW_SCREENING_REASON);
					rowCellFormat = Settings.getExcelCellFormat(ProbandListExcelSettingCodes.ENROLLMENT_LOG_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.ENROLLMENT_LOG_ROW_FORMAT);
					break;
				case SCREENING:
					showProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_PROBAND_LIST_ENTRY_TAGS);
					showAllProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS);
					showAllProbandListEntryTagDates = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES,
							Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES);
					showInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_INQUIRIES);
					showAllInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_ALL_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_ALL_INQUIRIES);
					showAllInquiryDates = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_ALL_INQUIRY_DATES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_ALL_INQUIRY_DATES);
					showAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_ADDRESSES);
					showContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_CONTACT_DETAILS);
					showTags = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_TAGS);
					showEnrollmentStatusLog = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_ENROLLMENT_STATUS_LOG, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_ENROLLMENT_STATUS_LOG);
					aggregateAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_AGGREGATE_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_AGGREGATE_ADDRESSES);
					aggregateContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_AGGREGATE_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_AGGREGATE_CONTACT_DETAILS);
					showICDate = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_IC_DATE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_IC_DATE);
					showICAge = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_IC_AGE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_IC_AGE);
					showScreeningDate = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_SCREENING_DATE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_SCREENING_DATE);
					showScreeningReason = Settings.getBoolean(ProbandListExcelSettingCodes.SCREENING_LOG_SHOW_SCREENING_REASON, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_SHOW_SCREENING_REASON);
					rowCellFormat = Settings.getExcelCellFormat(ProbandListExcelSettingCodes.SCREENING_LOG_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SCREENING_LOG_ROW_FORMAT);
					break;
				case PRE_SCREENING:
					showProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_PROBAND_LIST_ENTRY_TAGS);
					showAllProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS);
					showAllProbandListEntryTagDates = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES,
							Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES);
					showInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_INQUIRIES);
					showAllInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_ALL_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_ALL_INQUIRIES);
					showAllInquiryDates = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_ALL_INQUIRY_DATES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_ALL_INQUIRY_DATES);
					showAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_ADDRESSES);
					showContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_CONTACT_DETAILS);
					showTags = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_TAGS);
					showEnrollmentStatusLog = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_ENROLLMENT_STATUS_LOG, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_ENROLLMENT_STATUS_LOG);
					aggregateAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_AGGREGATE_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_AGGREGATE_ADDRESSES);
					aggregateContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_AGGREGATE_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_AGGREGATE_CONTACT_DETAILS);
					showICDate = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_IC_DATE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_IC_DATE);
					showICAge = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_IC_AGE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_IC_AGE);
					showScreeningDate = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_SCREENING_DATE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_SCREENING_DATE);
					showScreeningReason = Settings.getBoolean(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_SHOW_SCREENING_REASON, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_SHOW_SCREENING_REASON);
					rowCellFormat = Settings.getExcelCellFormat(ProbandListExcelSettingCodes.PRE_SCREENING_LOG_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.PRE_SCREENING_LOG_ROW_FORMAT);
					break;
				case SICL:
					showProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_PROBAND_LIST_ENTRY_TAGS);
					showAllProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS);
					showAllProbandListEntryTagDates = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES);
					showInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_INQUIRIES);
					showAllInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_ALL_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_ALL_INQUIRIES);
					showAllInquiryDates = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_ALL_INQUIRY_DATES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_ALL_INQUIRY_DATES);
					showAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_ADDRESSES);
					showContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_CONTACT_DETAILS);
					showTags = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_TAGS, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SICL_SHOW_TAGS);
					showEnrollmentStatusLog = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_ENROLLMENT_STATUS_LOG, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_ENROLLMENT_STATUS_LOG);
					aggregateAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_AGGREGATE_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_AGGREGATE_ADDRESSES);
					aggregateContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_AGGREGATE_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_AGGREGATE_CONTACT_DETAILS);
					showICDate = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_IC_DATE, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SICL_SHOW_IC_DATE);
					showICAge = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_IC_AGE, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.SICL_SHOW_IC_AGE);
					showScreeningDate = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_SCREENING_DATE, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_SCREENING_DATE);
					showScreeningReason = Settings.getBoolean(ProbandListExcelSettingCodes.SICL_SHOW_SCREENING_REASON, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_SHOW_SCREENING_REASON);
					rowCellFormat = Settings.getExcelCellFormat(ProbandListExcelSettingCodes.SICL_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
							ProbandListExcelDefaultSettings.SICL_ROW_FORMAT);
					break;
				default:
					showProbandListEntryTags = false;
					showAllProbandListEntryTags = false;
					showAllProbandListEntryTagDates = showAllProbandListEntryTags;
					showInquiries = false;
					showAllInquiries = false;
					showAllInquiryDates = showAllInquiries;
					showAddresses = false;
					showContactDetails = false;
					showTags = false;
					showEnrollmentStatusLog = false;
					aggregateAddresses = false;
					aggregateContactDetails = false;
					showICDate = false;
					showICAge = false;
					showScreeningDate = false;
					showScreeningReason = false;
					rowCellFormat = null;
					break;
			}
		} else {
			showProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_PROBAND_LIST_ENTRY_TAGS);
			showAllProbandListEntryTags = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_ALL_PROBAND_LIST_ENTRY_TAGS);
			showAllProbandListEntryTagDates = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_ALL_PROBAND_LIST_ENTRY_TAG_DATES);
			showInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_INQUIRIES);
			showAllInquiries = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_ALL_INQUIRIES, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_ALL_INQUIRIES);
			showAllInquiryDates = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_ALL_INQUIRY_DATES, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_ALL_INQUIRY_DATES);
			showAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_ADDRESSES);
			showContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_CONTACT_DETAILS);
			showTags = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_TAGS, Bundle.PROBAND_LIST_EXCEL, ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_TAGS);
			showEnrollmentStatusLog = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_ENROLLMENT_STATUS_LOG, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_ENROLLMENT_STATUS_LOG);
			aggregateAddresses = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_AGGREGATE_ADDRESSES, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_AGGREGATE_ADDRESSES);
			aggregateContactDetails = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_AGGREGATE_CONTACT_DETAILS, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_AGGREGATE_CONTACT_DETAILS);
			showICDate = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_IC_DATE, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_IC_DATE);
			showICAge = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_IC_AGE, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_IC_AGE);
			showScreeningDate = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_SCREENING_DATE, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_SCREENING_DATE);
			showScreeningReason = Settings.getBoolean(ProbandListExcelSettingCodes.PROBAND_LIST_SHOW_SCREENING_REASON, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_SHOW_SCREENING_REASON);
			rowCellFormat = Settings.getExcelCellFormat(ProbandListExcelSettingCodes.PROBAND_LIST_ROW_FORMAT, Bundle.PROBAND_LIST_EXCEL,
					ProbandListExcelDefaultSettings.PROBAND_LIST_ROW_FORMAT);
		}
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		Collection listEntryTags = showProbandListEntryTags ? probandListEntryTagDao.findByTrialExcelEcrfStratificationProbandSorted(trialId, showAllProbandListEntryTags
				|| showAllProbandListEntryTagDates ? null : true, null, null, null) : new ArrayList();
		probandListEntryTagDao.toProbandListEntryTagOutVOCollection(listEntryTags);
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = showInquiries ? inquiryDao.findByTrialActiveExcelProbandSorted(trialId, null, null, showAllInquiries || showAllInquiryDates ? null : true, null)
				: new ArrayList();
		inquiryDao.toInquiryOutVOCollection(inquiries);
		AddressTypeDao addressTypeDao = this.getAddressTypeDao();
		Collection addressTypes = showAddresses ? addressTypeDao.findByStaffProbandAnimalId(null, trialVO.getType().getPerson() ? true : null,
				!trialVO.getType().getPerson() ? true : null, null) : new ArrayList();
		addressTypeDao.toAddressTypeVOCollection(addressTypes);
		ContactDetailTypeDao contactDetailTypeDao = this.getContactDetailTypeDao();
		Collection contactDetailTypes = showContactDetails ? contactDetailTypeDao.findByStaffProbandAnimalId(null, trialVO.getType().getPerson() ? true : null, !trialVO.getType()
				.getPerson() ? true : null, null) : new ArrayList();
		contactDetailTypeDao.toContactDetailTypeVOCollection(contactDetailTypes);
		ProbandTagDao probandTagDao = this.getProbandTagDao();
		Collection probandTags = showTags ? probandTagDao.findByPersonAnimalIdExcel(trialVO.getType().getPerson() ? true : null, !trialVO.getType().getPerson() ? true : null,
				null, true) : new ArrayList();
		probandTagDao.toProbandTagVOCollection(probandTags);
		ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
		ProbandListStatusEntryOutVOComparator probandListStatusEntryOutVOComparator = new ProbandListStatusEntryOutVOComparator();
		ArrayList<String> distinctColumnNames;
		if (passDecryption) {
			distinctColumnNames = new ArrayList<String>(2 * listEntryTags.size() + 2 * inquiries.size()
					+ (aggregateAddresses ? 3 : addressTypes.size())
					+ (aggregateContactDetails ? 2 : contactDetailTypes.size())
					+ probandTags.size()
					+ (showEnrollmentStatusLog ? 1 : 0)
					+ (showICAge ? 1 : 0)
					+ (showScreeningDate ? 1 : 0)
					+ (showScreeningReason ? 1 : 0));
			if (showICDate) {
				distinctColumnNames.add(ProbandListExcelWriter.getICDateColumnName());
			}
			if (showICAge) {
				distinctColumnNames.add(ProbandListExcelWriter.getICAgeColumnName());
			}
			if (showScreeningDate) {
				distinctColumnNames.add(ProbandListExcelWriter.getScreeningDateColumnName());
			}
			if (showScreeningReason) {
				distinctColumnNames.add(ProbandListExcelWriter.getScreeningReasonColumnName());
			}
			if (showEnrollmentStatusLog) {
				distinctColumnNames.add(ProbandListExcelWriter.getEnrollmentStatusLogColumnName());
			}
			Iterator<ProbandTagVO> probandTagsIt = probandTags.iterator();
			while (probandTagsIt.hasNext()) {
				distinctColumnNames.add(probandTagsIt.next().getName());
			}
			if (aggregateAddresses) {
				distinctColumnNames.add(ProbandListExcelWriter.getStreetsColumnName());
				distinctColumnNames.add(ProbandListExcelWriter.getZipCodesColumnName());
				distinctColumnNames.add(ProbandListExcelWriter.getCityNamesColumnName());
			} else {
				Iterator<AddressTypeVO> addressTypesIt = addressTypes.iterator();
				while (addressTypesIt.hasNext()) {
					distinctColumnNames.add(addressTypesIt.next().getName());
				}
			}
			if (aggregateContactDetails) {
				distinctColumnNames.add(ProbandListExcelWriter.getEmailContactDetailsColumnName());
				distinctColumnNames.add(ProbandListExcelWriter.getPhoneContactDetailsColumnName());
			} else {
				Iterator<ContactDetailTypeVO> contactDetailTypesIt = contactDetailTypes.iterator();
				while (contactDetailTypesIt.hasNext()) {
					distinctColumnNames.add(contactDetailTypesIt.next().getName());
				}
			}
		} else {
			distinctColumnNames = new ArrayList<String>(2 * listEntryTags.size() + 2 * inquiries.size());
		}
		Iterator<ProbandListEntryTagOutVO> listEntryTagsIt = listEntryTags.iterator();
		while (listEntryTagsIt.hasNext()) {
			ProbandListEntryTagOutVO tagVO = listEntryTagsIt.next();
			if (showAllProbandListEntryTags || tagVO.isExcelValue()) {
				distinctColumnNames.add(ProbandListExcelWriter.getProbandListEntryTagColumnName(tagVO));
			}
			if (showAllProbandListEntryTagDates || tagVO.isExcelDate()) {
				distinctColumnNames.add(ProbandListExcelWriter.getProbandListEntryTagDateColumnName(tagVO));
			}
		}
		Iterator<InquiryOutVO> inquiriesIt = inquiries.iterator();
		while (inquiriesIt.hasNext()) {
			InquiryOutVO inquiryVO = inquiriesIt.next();
			if (showAllInquiries || inquiryVO.isExcelValue()) {
				distinctColumnNames.add(ProbandListExcelWriter.getInquiryColumnName(inquiryVO));
			}
			if (showAllInquiryDates || inquiryVO.isExcelDate()) {
				distinctColumnNames.add(ProbandListExcelWriter.getInquiryDateColumnName(inquiryVO));
			}
		}
		InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		ProbandTagValueDao probandTagValueDao = this.getProbandTagValueDao();
		ProbandAddressDao probandAddressDao = this.getProbandAddressDao();
		ProbandContactDetailValueDao probandContactDetailValueDao = this.getProbandContactDetailValueDao();
		ArrayList<ProbandListEntryOutVO> VOs = new ArrayList<ProbandListEntryOutVO>(probandListEntries.size());
		HashMap<Long, HashMap<String, Object>> distinctFieldRows = new HashMap<Long, HashMap<String, Object>>(probandListEntries.size());
		Iterator<ProbandListEntry> probandListEntriesIt = probandListEntries.iterator();
		while (probandListEntriesIt.hasNext()) {
			ProbandListEntry probandListEntry = probandListEntriesIt.next();
			ProbandListEntryOutVO probandListEntryVO = probandListEntryDao.toProbandListEntryOutVO(probandListEntry);
			HashMap<String, Object> fieldRow = new HashMap<String, Object>(distinctColumnNames.size());
			String fieldKey;
			Collection tagValues = showTags ? probandTagValueDao.findByProband(probandListEntryVO.getProband().getId(), null) : new ArrayList<ProbandTagValue>();
			probandTagValueDao.toProbandTagValueOutVOCollection(tagValues);
			Iterator<ProbandTagValueOutVO> tagValuesIt = tagValues.iterator();
			while (tagValuesIt.hasNext()) {
				ProbandTagValueOutVO tagValueOutVO = tagValuesIt.next();
				StringBuilder fieldValue;
				fieldKey = tagValueOutVO.getTag().getName();
				if (fieldRow.containsKey(fieldKey)) {
					fieldValue = new StringBuilder((String) fieldRow.get(fieldKey));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(tagValueOutVO.getValue());
				fieldRow.put(fieldKey, fieldValue.toString());
			}
			Collection probandAddresses = showAddresses ? probandAddressDao.findByProband(probandListEntryVO.getProband().getId(), null, null, null, null)
					: new ArrayList<ProbandAddress>();
			probandAddressDao.toProbandAddressOutVOCollection(probandAddresses);
			ServiceUtil.appendDistinctProbandAddressColumnValues(probandAddresses,
					fieldRow,
					aggregateAddresses,
					ProbandListExcelWriter.getStreetsColumnName(),
					ProbandListExcelWriter.getProvincesColumnName(),
					ProbandListExcelWriter.getZipCodesColumnName(),
					ProbandListExcelWriter.getCityNamesColumnName());
			Collection probandContactDetails = showContactDetails
					? probandContactDetailValueDao.findByProband(probandListEntryVO.getProband().getId(), null, false, null, null, null)
					: new ArrayList<ProbandContactDetailValue>();
			probandContactDetailValueDao.toProbandContactDetailValueOutVOCollection(probandContactDetails);
			ServiceUtil.appendDistinctProbandContactColumnValues(probandContactDetails,
					fieldRow,
					aggregateContactDetails,
					ProbandListExcelWriter.getEmailContactDetailsColumnName(),
					ProbandListExcelWriter.getPhoneContactDetailsColumnName());
			HashMap<Long, ProbandListEntryTagValue> listEntryTagValueMap;
			if (showProbandListEntryTags) {
				Collection<ProbandListEntryTagValue> listEntryTagValues = probandListEntryTagValueDao.findByListEntryListEntryTag(probandListEntryVO.getId(), null);
				listEntryTagValueMap = new HashMap<Long, ProbandListEntryTagValue>(listEntryTagValues.size());
				Iterator<ProbandListEntryTagValue> listEntryTagValuesIt = listEntryTagValues.iterator();
				while (listEntryTagValuesIt.hasNext()) {
					ProbandListEntryTagValue listEntryTagValue = listEntryTagValuesIt.next();
					listEntryTagValueMap.put(listEntryTagValue.getTag().getId(), listEntryTagValue);
				}
			} else {
				listEntryTagValueMap = null;
			}
			listEntryTagsIt = listEntryTags.iterator();
			while (listEntryTagsIt.hasNext()) {
				ProbandListEntryTagOutVO listEntryTagVO = listEntryTagsIt.next();
				ProbandListEntryTagValueOutVO listEntryTagValueVO;
				if (listEntryTagValueMap.containsKey(listEntryTagVO.getId())) {
					listEntryTagValueVO = probandListEntryTagValueDao.toProbandListEntryTagValueOutVO(listEntryTagValueMap.get(listEntryTagVO.getId()));
				} else {
					listEntryTagValueVO = ServiceUtil.createPresetProbandListEntryTagOutValue(probandListEntryVO, listEntryTagVO, null);
				}
				if (showAllProbandListEntryTags || listEntryTagVO.isExcelValue()) {
					Object fieldValue = null;
					InputFieldTypeVO fieldTypeVO = listEntryTagVO.getField().getFieldType();
					if (fieldTypeVO != null) {
						InputFieldType fieldType = fieldTypeVO.getType();
						if (fieldType != null) {
							switch (fieldType) {
								case SINGLE_LINE_TEXT:
									fieldValue = listEntryTagValueVO.getTextValue();
									break;
								case MULTI_LINE_TEXT:
									fieldValue = listEntryTagValueVO.getTextValue();
									break;
								case SELECT_ONE_DROPDOWN:
								case SELECT_ONE_RADIO_H:
								case SELECT_ONE_RADIO_V:
									fieldValue = ExcelUtil.selectionSetValuesToString(listEntryTagValueVO.getSelectionValues());
									break;
								case AUTOCOMPLETE:
									fieldValue = listEntryTagValueVO.getTextValue();
									break;
								case SELECT_MANY_H:
								case SELECT_MANY_V:
									fieldValue = ExcelUtil.selectionSetValuesToString(listEntryTagValueVO.getSelectionValues());
									break;
								case CHECKBOX:
									fieldValue = listEntryTagValueVO.getBooleanValue();
									break;
								case INTEGER:
									fieldValue = listEntryTagValueVO.getLongValue();
									break;
								case FLOAT:
									fieldValue = listEntryTagValueVO.getFloatValue();
									break;
								case DATE:
									fieldValue = listEntryTagValueVO.getDateValue();
									break;
								case TIME:
									fieldValue = listEntryTagValueVO.getTimeValue();
									break;
								case TIMESTAMP:
									fieldValue = listEntryTagValueVO.getTimestampValue();
									if (listEntryTagVO.getField().isUserTimeZone()) {
										fieldValue = DateCalc.convertTimezone((Date) fieldValue, TimeZone.getDefault(), CoreUtil.getUserContext().getTimeZone());
									}
									break;
								case SKETCH:
									fieldValue = ExcelUtil.selectionSetValuesToString(listEntryTagValueVO.getSelectionValues());
									break;
								default:
							}
						}
					}
					fieldKey = ProbandListExcelWriter.getProbandListEntryTagColumnName(listEntryTagVO);
					fieldRow.put(fieldKey, fieldValue);
				}
				if (showAllProbandListEntryTagDates || listEntryTagVO.isExcelDate()) {
					fieldKey = ProbandListExcelWriter.getProbandListEntryTagDateColumnName(listEntryTagVO);
					Date date = listEntryTagValueVO.getModifiedTimestamp();
					if (rowCellFormat != null && rowCellFormat.isDateTimeUserTimezone()) {
						date = DateCalc.convertTimezone(date, TimeZone.getDefault(), CoreUtil.getUserContext().getTimeZone());
					}
					fieldRow.put(fieldKey, DateCalc.getStartOfDay(date));
				}
			}
			HashMap<Long, InquiryValue> inquiryValueMap;
			if (showInquiries) {
				Collection<InquiryValue> inquiryValues = inquiryValueDao.findByTrialActiveProbandField(probandListEntryVO.getTrial().getId(), null, null, probandListEntryVO
						.getProband()
						.getId(), null);
				inquiryValueMap = new HashMap<Long, InquiryValue>(inquiryValues.size());
				Iterator<InquiryValue> inquiryValuesIt = inquiryValues.iterator();
				while (inquiryValuesIt.hasNext()) {
					InquiryValue inquiryValue = inquiryValuesIt.next();
					inquiryValueMap.put(inquiryValue.getInquiry().getId(), inquiryValue);
				}
			} else {
				inquiryValueMap = null;
			}
			inquiriesIt = inquiries.iterator();
			while (inquiriesIt.hasNext()) {
				InquiryOutVO inquiryVO = inquiriesIt.next();
				InquiryValueOutVO inquiryValueVO;
				if (inquiryValueMap.containsKey(inquiryVO.getId())) {
					inquiryValueVO = inquiryValueDao.toInquiryValueOutVO(inquiryValueMap.get(inquiryVO.getId()));
				} else {
					inquiryValueVO = ServiceUtil.createPresetInquiryOutValue(probandListEntryVO.getProband(), inquiryVO, null);
				}
				if (showAllInquiries || inquiryVO.isExcelValue()) {
					Object fieldValue = null;
					InputFieldTypeVO fieldTypeVO = inquiryVO.getField().getFieldType();
					if (fieldTypeVO != null) {
						InputFieldType fieldType = fieldTypeVO.getType();
						if (fieldType != null) {
							switch (fieldType) {
								case SINGLE_LINE_TEXT:
									fieldValue = inquiryValueVO.getTextValue();
									break;
								case MULTI_LINE_TEXT:
									fieldValue = inquiryValueVO.getTextValue();
									break;
								case SELECT_ONE_DROPDOWN:
								case SELECT_ONE_RADIO_H:
								case SELECT_ONE_RADIO_V:
									fieldValue = ExcelUtil.selectionSetValuesToString(inquiryValueVO.getSelectionValues());
									break;
								case AUTOCOMPLETE:
									fieldValue = inquiryValueVO.getTextValue();
									break;
								case SELECT_MANY_H:
								case SELECT_MANY_V:
									fieldValue = ExcelUtil.selectionSetValuesToString(inquiryValueVO.getSelectionValues());
									break;
								case CHECKBOX:
									fieldValue = inquiryValueVO.getBooleanValue();
									break;
								case INTEGER:
									fieldValue = inquiryValueVO.getLongValue();
									break;
								case FLOAT:
									fieldValue = inquiryValueVO.getFloatValue();
									break;
								case DATE:
									fieldValue = inquiryValueVO.getDateValue();
									break;
								case TIME:
									fieldValue = inquiryValueVO.getTimeValue();
									break;
								case TIMESTAMP:
									fieldValue = inquiryValueVO.getTimestampValue();
									if (inquiryVO.getField().isUserTimeZone()) {
										fieldValue = DateCalc.convertTimezone((Date) fieldValue, TimeZone.getDefault(), CoreUtil.getUserContext().getTimeZone());
									}
									break;
								case SKETCH:
									fieldValue = ExcelUtil.selectionSetValuesToString(inquiryValueVO.getSelectionValues());
									break;
								default:
							}
						}
					}
					fieldKey = ProbandListExcelWriter.getInquiryColumnName(inquiryVO);
					fieldRow.put(fieldKey, fieldValue);
				}
				if (showAllInquiryDates || inquiryVO.isExcelDate()) {
					fieldKey = ProbandListExcelWriter.getInquiryDateColumnName(inquiryVO);
					Date date = inquiryValueVO.getModifiedTimestamp();
					if (rowCellFormat != null && rowCellFormat.isDateTimeUserTimezone()) {
						date = DateCalc.convertTimezone(date, TimeZone.getDefault(), CoreUtil.getUserContext().getTimeZone());
					}
					fieldRow.put(fieldKey, DateCalc.getStartOfDay(date));
				}
			}
			if (passDecryption) {
				Collection statusEntries = probandListEntry.getStatusEntries();
				probandListStatusEntryDao.toProbandListStatusEntryOutVOCollection(statusEntries);
				ArrayList<ProbandListStatusEntryOutVO> statusEntryVOs = new ArrayList<ProbandListStatusEntryOutVO>(statusEntries);
				Collections.sort(statusEntryVOs, probandListStatusEntryOutVOComparator);
				StringBuilder statusLog = new StringBuilder();
				Integer iCAge = null;
				Date iCDate = null;
				Date screeningDate = null;
				String screeningReason = null;
				Iterator<ProbandListStatusEntryOutVO> statusEntryVOsIt = statusEntryVOs.iterator();
				while (statusEntryVOsIt.hasNext()) {
					ProbandListStatusEntryOutVO statusEntryVO = statusEntryVOsIt.next();
					statusLog.append(ProbandListExcelWriter.getEnrollmentStatusLogValue(statusEntryVO));
					if (statusEntryVOsIt.hasNext()) {
						statusLog.append(ExcelUtil.EXCEL_LINE_BREAK);
					}
					if (statusEntryVO.getStatus().getIc()) {
						iCDate = DateCalc.getStartOfDay(statusEntryVO.getRealTimestamp());
						iCAge = CommonUtil.getAge(statusEntryVO.getListEntry().getProband().getDateOfBirth(), iCDate);
					}
					if (statusEntryVO.getStatus().getScreening()) {
						screeningDate = DateCalc.getStartOfDay(statusEntryVO.getRealTimestamp());
						screeningReason = statusEntryVO.getReason();
					}
				}
				if (showEnrollmentStatusLog) {
					fieldRow.put(ProbandListExcelWriter.getEnrollmentStatusLogColumnName(), statusLog.toString());
				}
				if (showICAge) {
					fieldRow.put(ProbandListExcelWriter.getICAgeColumnName(), iCAge);
				}
				if (showICDate) {
					fieldRow.put(ProbandListExcelWriter.getICDateColumnName(), iCDate);
				}
				if (showScreeningDate) {
					fieldRow.put(ProbandListExcelWriter.getScreeningDateColumnName(), screeningDate);
				}
				if (showScreeningReason) {
					fieldRow.put(ProbandListExcelWriter.getScreeningReasonColumnName(), screeningReason);
				}
			}
			VOs.add(probandListEntryVO);
			distinctFieldRows.put(probandListEntryVO.getId(), fieldRow);
		}
		writer.setVOs(VOs);
		writer.setDistinctColumnNames(distinctColumnNames);
		writer.setDistinctFieldRows(distinctFieldRows);
		User user = CoreUtil.getUser();
		writer.getExcelVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new ExcelExporter(writer, writer)).write();
		ProbandListExcelVO result = writer.getExcelVO();
		ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.PROBAND_LIST_EXPORTED, result,
				null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ReimbursementsExcelVO handleExportReimbursements(
			AuthenticationVO auth, Long trialId, String costType,
			PaymentMethod method, Boolean paid) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialOutVO trialVO = trialDao.toTrialOutVO(trial);
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes = moneyTransferDao.getCostTypes(trialVO.getDepartment().getId(), null, null, null, method);
		Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialVO.getId(), null, null, method, costType, paid, true,
				null);
		ReimbursementsExcelVO result = ServiceUtil.createReimbursementsExcel(moneyTransfers, costTypes, trialVO, null, costType, method, paid,
				moneyTransferDao,
				this.getBankAccountDao(),
				this.getProbandAddressDao(),
				this.getAddressTypeDao(),
				this.getUserDao());
		ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
				SystemMessageCodes.REIMBURSEMENTS_EXPORTED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TeamMembersExcelVO handleExportTeamMembers(
			AuthenticationVO auth, Long trialId) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialOutVO trialVO = trialDao.toTrialOutVO(trial);
		TeamMembersExcelWriter writer = ExcelWriterFactory.createTeamMembersExcelWriter(!CoreUtil.isPassDecryption());
		writer.setTrial(trialVO);
		Collection<TeamMember> members = trial.getMembers();
		boolean showAddresses = Settings.getBoolean(TeamMembersExcelSettingCodes.SHOW_ADDRESSES, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.SHOW_ADDRESSES);
		boolean showContactDetails = Settings.getBoolean(TeamMembersExcelSettingCodes.SHOW_CONTACT_DETAILS, Bundle.TEAM_MEMBERS_EXCEL,
				TeamMembersExcelDefaultSettings.SHOW_CONTACT_DETAILS);
		boolean showCvAddressBlock = Settings.getBoolean(TeamMembersExcelSettingCodes.SHOW_CV_ADDRESS_BLOCK, Bundle.TEAM_MEMBERS_EXCEL,
				TeamMembersExcelDefaultSettings.SHOW_CV_ADDRESS_BLOCK);
		boolean showTags = Settings.getBoolean(TeamMembersExcelSettingCodes.SHOW_TAGS, Bundle.TEAM_MEMBERS_EXCEL, TeamMembersExcelDefaultSettings.SHOW_TAGS);
		boolean aggregateAddresses = Settings.getBoolean(TeamMembersExcelSettingCodes.AGGREGATE_ADDRESSES, Bundle.TEAM_MEMBERS_EXCEL,
				TeamMembersExcelDefaultSettings.AGGREGATE_ADDRESSES);
		boolean aggregateContactDetails = Settings.getBoolean(TeamMembersExcelSettingCodes.AGGREGATE_CONTACT_DETAILS, Bundle.TEAM_MEMBERS_EXCEL,
				TeamMembersExcelDefaultSettings.AGGREGATE_CONTACT_DETAILS);
		StaffTagDao staffTagDao = this.getStaffTagDao();
		Collection staffTags = showTags ? staffTagDao.findByPersonOrganisationIdExcelTrainingRecord(null, null, null, true, null) : new ArrayList();
		staffTagDao.toStaffTagVOCollection(staffTags);
		AddressTypeDao addressTypeDao = this.getAddressTypeDao();
		Collection addressTypes = showAddresses ? addressTypeDao.findByStaffProbandAnimalId(true, null, null, null) : new ArrayList();
		addressTypeDao.toAddressTypeVOCollection(addressTypes);
		ContactDetailTypeDao contactDetailTypeDao = this.getContactDetailTypeDao();
		Collection contactDetailTypes = showContactDetails ? contactDetailTypeDao.findByStaffProbandAnimalId(true, null, null, null) : new ArrayList();
		contactDetailTypeDao.toContactDetailTypeVOCollection(contactDetailTypes);
		ArrayList<String> distinctColumnNames = new ArrayList<String>(
				(showCvAddressBlock ? 1 : 0)
						+ staffTags.size()
						+ (aggregateAddresses ? 3 : addressTypes.size())
						+ (aggregateContactDetails ? 2 : contactDetailTypes.size()));
		if (showCvAddressBlock) {
			distinctColumnNames.add(TeamMembersExcelWriter.getCvAddressBlockColumnName());
		}
		Iterator<StaffTagVO> staffTagsIt = staffTags.iterator();
		while (staffTagsIt.hasNext()) {
			distinctColumnNames.add(staffTagsIt.next().getName());
		}
		if (aggregateAddresses) {
			distinctColumnNames.add(TeamMembersExcelWriter.getStreetsColumnName());
			distinctColumnNames.add(TeamMembersExcelWriter.getZipCodesColumnName());
			distinctColumnNames.add(TeamMembersExcelWriter.getCityNamesColumnName());
		} else {
			Iterator<AddressTypeVO> addressTypesIt = addressTypes.iterator();
			while (addressTypesIt.hasNext()) {
				distinctColumnNames.add(addressTypesIt.next().getName());
			}
		}
		if (aggregateContactDetails) {
			distinctColumnNames.add(TeamMembersExcelWriter.getEmailContactDetailsColumnName());
			distinctColumnNames.add(TeamMembersExcelWriter.getPhoneContactDetailsColumnName());
		} else {
			Iterator<ContactDetailTypeVO> contactDetailTypesIt = contactDetailTypes.iterator();
			while (contactDetailTypesIt.hasNext()) {
				distinctColumnNames.add(contactDetailTypesIt.next().getName());
			}
		}
		StaffTagValueDao staffTagValueDao = this.getStaffTagValueDao();
		StaffAddressDao staffAddressDao = this.getStaffAddressDao();
		StaffContactDetailValueDao staffContactDetailValueDao = this.getStaffContactDetailValueDao();
		StaffDao staffDao = this.getStaffDao();
		TeamMemberDao teamMemberDao = this.getTeamMemberDao();
		ArrayList<TeamMemberOutVO> VOs = new ArrayList<TeamMemberOutVO>(members.size());
		HashMap<Long, HashMap<String, Object>> distinctFieldRows = new HashMap<Long, HashMap<String, Object>>(members.size());
		Iterator<TeamMember> membersIt = members.iterator();
		while (membersIt.hasNext()) {
			TeamMember member = membersIt.next();
			TeamMemberOutVO memberVO = teamMemberDao.toTeamMemberOutVO(member);
			HashMap<String, Object> fieldRow = new HashMap<String, Object>(distinctColumnNames.size());
			String fieldKey;
			if (showCvAddressBlock) {
				fieldKey = TeamMembersExcelWriter.getCvAddressBlockColumnName();
				StaffOutVO staffVO = staffDao.toStaffOutVO(member.getStaff(), Settings.getInt(TeamMembersExcelSettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.TEAM_MEMBERS_EXCEL,
						TeamMembersExcelDefaultSettings.GRAPH_MAX_STAFF_INSTANCES));
				fieldRow.put(fieldKey, ServiceUtil.getCvAddressBlock(staffVO, ExcelUtil.EXCEL_LINE_BREAK, staffAddressDao));
			}
			Collection tagValues = showTags ? staffTagValueDao.findByStaff(memberVO.getStaff().getId(), null) : new ArrayList<StaffTagValue>();
			staffTagValueDao.toStaffTagValueOutVOCollection(tagValues);
			Iterator<StaffTagValueOutVO> tagValuesIt = tagValues.iterator();
			while (tagValuesIt.hasNext()) {
				StaffTagValueOutVO tagValueOutVO = tagValuesIt.next();
				StringBuilder fieldValue;
				fieldKey = tagValueOutVO.getTag().getName();
				if (fieldRow.containsKey(fieldKey)) {
					fieldValue = new StringBuilder((String) fieldRow.get(fieldKey));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(tagValueOutVO.getValue());
				fieldRow.put(fieldKey, fieldValue.toString());
			}
			Collection addresses = showAddresses ? staffAddressDao.findByStaff(memberVO.getStaff().getId(), null, null, null, null) : new ArrayList<StaffAddress>();
			staffAddressDao.toStaffAddressOutVOCollection(addresses);
			Iterator<StaffAddressOutVO> addressesIt = addresses.iterator();
			while (addressesIt.hasNext()) {
				StaffAddressOutVO addressOutVO = addressesIt.next();
				StringBuilder fieldValue;
				if (aggregateAddresses) {
					fieldKey = TeamMembersExcelWriter.getStreetsColumnName();
					if (fieldRow.containsKey(fieldKey)) {
						fieldValue = new StringBuilder((String) fieldRow.get(fieldKey));
					} else {
						fieldValue = new StringBuilder();
					}
					if (fieldValue.length() > 0) {
						fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
					}
					fieldValue.append(CommonUtil.getStreetString(addressOutVO.getStreetName(), addressOutVO.getHouseNumber(), addressOutVO.getEntrance(),
							addressOutVO.getDoorNumber()));
					fieldRow.put(fieldKey, fieldValue.toString());
					fieldKey = TeamMembersExcelWriter.getProvincesColumnName();
					if (fieldRow.containsKey(fieldKey)) {
						fieldValue = new StringBuilder((String) fieldRow.get(fieldKey));
					} else {
						fieldValue = new StringBuilder();
					}
					if (fieldValue.length() > 0) {
						fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
					}
					fieldValue.append(addressOutVO.getProvince());
					fieldKey = TeamMembersExcelWriter.getZipCodesColumnName();
					if (fieldRow.containsKey(fieldKey)) {
						fieldValue = new StringBuilder((String) fieldRow.get(fieldKey));
					} else {
						fieldValue = new StringBuilder();
					}
					if (fieldValue.length() > 0) {
						fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
					}
					fieldValue.append(addressOutVO.getZipCode());
					fieldRow.put(fieldKey, fieldValue.toString());
					fieldKey = TeamMembersExcelWriter.getCityNamesColumnName();
					if (fieldRow.containsKey(fieldKey)) {
						fieldValue = new StringBuilder((String) fieldRow.get(fieldKey));
					} else {
						fieldValue = new StringBuilder();
					}
					if (fieldValue.length() > 0) {
						fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
					}
					fieldValue.append(addressOutVO.getCityName());
					fieldRow.put(fieldKey, fieldValue.toString());
				} else {
					fieldKey = addressOutVO.getType().getName();
					if (fieldRow.containsKey(fieldKey)) {
						fieldValue = new StringBuilder((String) fieldRow.get(fieldKey));
					} else {
						fieldValue = new StringBuilder();
					}
					if (fieldValue.length() > 0) {
						fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
					}
					fieldValue.append(addressOutVO.getName());
					fieldRow.put(fieldKey, fieldValue.toString());
				}
			}
			Collection contactDetails = showContactDetails ? staffContactDetailValueDao.findByStaff(memberVO.getStaff().getId(), null, false, null, null, null)
					: new ArrayList<StaffContactDetailValue>();
			staffContactDetailValueDao.toStaffContactDetailValueOutVOCollection(contactDetails);
			Iterator<StaffContactDetailValueOutVO> contactDetailsIt = contactDetails.iterator();
			while (contactDetailsIt.hasNext()) {
				StaffContactDetailValueOutVO contactDetailOutVO = contactDetailsIt.next();
				StringBuilder fieldValue;
				if (aggregateContactDetails) {
					if (contactDetailOutVO.getType().isEmail()) {
						fieldKey = TeamMembersExcelWriter.getEmailContactDetailsColumnName();
					} else if (contactDetailOutVO.getType().isPhone()) {
						fieldKey = TeamMembersExcelWriter.getPhoneContactDetailsColumnName();
					} else {
						continue;
					}
				} else {
					fieldKey = contactDetailOutVO.getType().getName();
				}
				if (fieldRow.containsKey(fieldKey)) {
					fieldValue = new StringBuilder((String) fieldRow.get(fieldKey));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(contactDetailOutVO.getValue());
				fieldRow.put(fieldKey, fieldValue.toString());
			}
			VOs.add(memberVO);
			distinctFieldRows.put(member.getId(), fieldRow);
		}
		Collections.sort(VOs, new TeamMemberOutVOComparator());
		writer.setVOs(VOs);
		writer.setDistinctColumnNames(distinctColumnNames);
		writer.setDistinctFieldRows(distinctFieldRows);
		User user = CoreUtil.getUser();
		writer.getExcelVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new ExcelExporter(writer, writer)).write();
		TeamMembersExcelVO result = writer.getExcelVO();
		ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.TEAM_MEMBERS_EXPORTED, result,
				null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected VisitScheduleExcelVO handleExportVisitSchedule(
			AuthenticationVO auth, Long trialId, Long probandId, Long trialDepartmentId, Date from, Date to) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = null;
		TrialOutVO trialVO = null;
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
			trialVO = trialDao.toTrialOutVO(trial);
		}
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = null;
		ProbandOutVO probandVO = null;
		if (probandId != null) {
			proband = CheckIDUtil.checkProbandId(probandId, probandDao);
			probandVO = probandDao.toProbandOutVO(proband);
		}
		DepartmentDao departmentDao = this.getDepartmentDao();
		DepartmentVO trialDepartmentVO = null;
		if (trialDepartmentId != null) {
			trialDepartmentVO = departmentDao.toDepartmentVO(CheckIDUtil.checkDepartmentId(trialDepartmentId, departmentDao));
		}
		VisitScheduleExcelWriter.Styles style;
		if (trialVO != null) {
			if (probandVO != null) {
				style = VisitScheduleExcelWriter.Styles.TRAVEL_EXPENSES_VISIT_SCHEDULE;
			} else {
				style = VisitScheduleExcelWriter.Styles.TRIAL_VISIT_SCHEDULE;
			}
		} else {
			style = VisitScheduleExcelWriter.Styles.PROBAND_APPOINTMENT_SCHEDULE;
		}
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		Collection visitScheduleItems;
		Iterator it;
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				visitScheduleItems = trial.getVisitScheduleItems();
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				visitScheduleItems = visitScheduleItemDao.findByTrialGroupVisitProbandTravel(trialVO.getId(), null, null, probandVO.getId(), true, true, null);
				break;
			case PROBAND_APPOINTMENT_SCHEDULE:
				visitScheduleItems = new ArrayList<VisitScheduleAppointmentVO>();
				it = visitScheduleItemDao.findByTrialDepartmentStatusTypeInterval(trialId, trialDepartmentId, null, null,
						CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to)).iterator();
				while (it.hasNext()) {
					Object[] visitScheduleItemProband = (Object[]) it.next();
					VisitScheduleAppointmentVO visitScheduleItemProbandVO = visitScheduleItemDao.toVisitScheduleAppointmentVO((VisitScheduleItem) visitScheduleItemProband[0]);
					visitScheduleItemProbandVO.setProband(probandDao.toProbandOutVO((Proband) visitScheduleItemProband[1]));
					visitScheduleItems.add(visitScheduleItemProbandVO);
				}
				break;
			default:
				visitScheduleItems = null;
		}
		VisitScheduleExcelVO result = ServiceUtil.createVisitScheduleExcel(visitScheduleItems, style,
				probandVO, trialVO, trialDepartmentVO, from, to,
				visitScheduleItemDao,
				this.getProbandListStatusEntryDao(),
				this.getProbandAddressDao(),
				this.getUserDao());
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.VISIT_SCHEDULE_EXPORTED, result, null, this.getJournalEntryDao());
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				ServiceUtil.logSystemMessage(proband, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_EXPORTED, result, null, this.getJournalEntryDao());
				break;
			case PROBAND_APPOINTMENT_SCHEDULE:
				if (trial != null) { //not implemented atm
					ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
							SystemMessageCodes.PROBAND_APPOINTMENT_SCHEDULE_EXPORTED, result, null, this.getJournalEntryDao());
				} else {
					ServiceUtil.logSystemMessage(CoreUtil.getUser(), (UserOutVO) null, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
							SystemMessageCodes.PROBAND_APPOINTMENT_SCHEDULE_EXPORTED, result,
							null, this.getJournalEntryDao());
				}
				break;
			default:
		}
		return result;
	}

	@Override
	protected String handleGenerateRandomizationList(AuthenticationVO auth, Long trialId, RandomizationMode mode, Integer n) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		Randomization randomization = Randomization.getInstance(mode, trialDao, this.getProbandGroupDao(), this.getProbandListEntryDao(),
				this.getStratificationRandomizationListDao(), this.getProbandListEntryTagDao(), this.getInputFieldSelectionSetValueDao(), this.getProbandListEntryTagValueDao(),
				this.getRandomizationListCodeDao());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		String result = randomization.generateRandomizationList(trial, n);
		logSystemMessage(trial, trialDao.toTrialOutVO(trial), now, user, randomization.getRandomizationListInfoVO());
		return result;
	}

	@Override
	protected InventoryBookingDurationSummaryVO handleGetBookingDurationSummary(
			AuthenticationVO auth, Long trialId, String calendar, Date from, Date to)
			throws Exception {
		InventoryBookingDurationSummaryVO result = new InventoryBookingDurationSummaryVO();
		if (trialId != null) {
			result.setTrial(this.getTrialDao().toTrialOutVO(CheckIDUtil.checkTrialId(trialId, this.getTrialDao())));
		}
		result.setStart(from);
		result.setStop(to);
		result.setCalendar(calendar);
		ServiceUtil.populateBookingDurationSummary(true, result, this.getInventoryBookingDao(), this.getInventoryStatusEntryDao());
		return result;
	}

	@Override
	protected Collection<String> handleGetCalendars(AuthenticationVO auth,
			Long trialDepartmentId, Long staffId, Long trialId,
			String calendarPrefix, Integer limit) throws Exception {
		if (trialDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(trialDepartmentId, this.getDepartmentDao());
		}
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		return this.getDutyRosterTurnDao().findCalendars(trialDepartmentId, staffId, trialId, calendarPrefix, limit);
	}

	@Override
	protected Collection<ProbandStatusEntryOutVO> handleGetCollidingProbandStatusEntries(
			AuthenticationVO auth, Long probandListEntryId) throws Exception {
		ProbandListEntry probandListEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		ProbandGroup probandGroup = probandListEntry.getGroup();
		//if (probandGroup != null) {
		Collection collidingProbandStatusEntries = new HashSet();
		Long probandId = probandListEntry.getProband().getId();
		ProbandStatusEntryDao probandStatusEntryDao = this.getProbandStatusEntryDao();
		//Iterator<VisitScheduleItem> it = probandGroup.getVisitScheduleItems().iterator();
		Iterator<VisitScheduleItem> it = this.getVisitScheduleItemDao()
				.findByTrialGroupVisitProbandTravel(probandListEntry.getTrial().getId(), probandGroup != null ? probandGroup.getId() : null,
						null, probandListEntry.getProband().getId(), null, true, null)
				.iterator();
		while (it.hasNext()) {
			VisitScheduleItem expanded = it.next();
			collidingProbandStatusEntries
					.addAll(probandStatusEntryDao.findByProbandInterval(probandId, expanded.getStart(), expanded.getStop(), false, null));
		}
		probandStatusEntryDao.toProbandStatusEntryOutVOCollection(collidingProbandStatusEntries);
		return new ArrayList<ProbandStatusEntryOutVO>(collidingProbandStatusEntries);
		//} else {
		//	//we do not count collisions of subjects that have not been assigned to a group yet:
		//	return new ArrayList<ProbandStatusEntryOutVO>();
		//}
	}

	@Override
	protected Collection<ProbandStatusEntryOutVO> handleGetCollidingProbandStatusEntries(
			AuthenticationVO auth, Long visitScheduleItemId, Long probandId) throws Exception {
		VisitScheduleItem visitScheduleItem = CheckIDUtil.checkVisitScheduleItemId(visitScheduleItemId, this.getVisitScheduleItemDao());
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		Collection collidingProbandStatusEntries;
		ProbandStatusEntryDao probandStatusEntryDao = this.getProbandStatusEntryDao();
		if (probandId != null) {
			VisitScheduleItem expanded = this.getVisitScheduleItemDao().findExpandedDateMode(visitScheduleItem.getId(), probandId).iterator().next();
			collidingProbandStatusEntries = probandStatusEntryDao.findByProbandInterval(probandId, expanded.getStart(), expanded.getStop(), false, null);
			probandStatusEntryDao.toProbandStatusEntryOutVOCollection(collidingProbandStatusEntries);
		} else {
			collidingProbandStatusEntries = new HashSet();
			ProbandGroup probandGroup = visitScheduleItem.getGroup();
			if (probandGroup != null) {
				Iterator<ProbandListEntry> listEntriesit = probandGroup.getProbandListEntries().iterator();
				while (listEntriesit.hasNext()) {
					ProbandListEntry probandListEntry = listEntriesit.next();
					Iterator<VisitScheduleItem> it = this.getVisitScheduleItemDao().findExpandedDateMode(visitScheduleItem.getId(), probandListEntry.getProband().getId())
							.iterator();
					while (it.hasNext()) {
						VisitScheduleItem expanded = it.next();
						collidingProbandStatusEntries.addAll(probandStatusEntryDao.findByProbandInterval(probandListEntry.getProband().getId(), expanded.getStart(),
								expanded.getStop(), false, null));
					}
				}
				probandStatusEntryDao.toProbandStatusEntryOutVOCollection(collidingProbandStatusEntries);
				collidingProbandStatusEntries = new ArrayList<ProbandStatusEntryOutVO>(collidingProbandStatusEntries);
			} else {
				Iterator<ProbandListEntry> listEntriesit = visitScheduleItem.getTrial().getProbandListEntries().iterator();
				while (listEntriesit.hasNext()) {
					ProbandListEntry probandListEntry = listEntriesit.next();
					Iterator<VisitScheduleItem> it = this.getVisitScheduleItemDao()
							.findExpandedDateModeGroup(visitScheduleItem.getId(), probandListEntry.getProband().getId(), null)
							.iterator();
					while (it.hasNext()) {
						VisitScheduleItem expanded = it.next();
						collidingProbandStatusEntries.addAll(probandStatusEntryDao.findByProbandInterval(probandListEntry.getProband().getId(), expanded.getStart(),
								expanded.getStop(), false, null));
					}
				}
				probandStatusEntryDao.toProbandStatusEntryOutVOCollection(collidingProbandStatusEntries);
				collidingProbandStatusEntries = new ArrayList<ProbandStatusEntryOutVO>(collidingProbandStatusEntries);
			}
		}
		return collidingProbandStatusEntries;
	}

	@Override
	protected Collection<StaffStatusEntryOutVO> handleGetCollidingStaffStatusEntries(AuthenticationVO auth, Long visitScheduleItemId, Long staffId) throws Exception {
		VisitScheduleItem visitScheduleItem = CheckIDUtil.checkVisitScheduleItemId(visitScheduleItemId, this.getVisitScheduleItemDao());
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		Collection collidingStaffStatusEntries = new HashSet();
		StaffStatusEntryDao staffStatusEntryDao = this.getStaffStatusEntryDao();
		Iterator<VisitScheduleItem> it = this.getVisitScheduleItemDao().findExpandedDateMode(visitScheduleItem.getId(), null).iterator();
		while (it.hasNext()) {
			VisitScheduleItem expanded = it.next();
			collidingStaffStatusEntries.addAll(staffStatusEntryDao.findByStaffInterval(staffId, expanded.getStart(), expanded.getStop(), false, true, false));
		}
		staffStatusEntryDao.toStaffStatusEntryOutVOCollection(collidingStaffStatusEntries);
		collidingStaffStatusEntries = new ArrayList<StaffStatusEntryOutVO>(collidingStaffStatusEntries);
		return collidingStaffStatusEntries;
	}

	@Override
	protected Collection<DutyRosterTurnOutVO> handleGetDutyRoster(AuthenticationVO auth, Long trialId,
			PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		Collection dutyRosterTurns = dutyRosterTurnDao.findByTrial(trialId, psf);
		dutyRosterTurnDao.toDutyRosterTurnOutVOCollection(dutyRosterTurns);
		return dutyRosterTurns;
	}

	@Override
	protected long handleGetDutyRosterCount(AuthenticationVO auth, Long trialId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getDutyRosterTurnDao().getCount(null, trialId);
	}

	@Override
	protected Collection<DutyRosterTurnOutVO> handleGetDutyRosterInterval(
			AuthenticationVO auth, Long departmentId, Long statusId, Long staffId, boolean unassigned, Long trialId, String calendar, Date from, Date to, boolean sort)
			throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (statusId != null) {
			CheckIDUtil.checkTrialStatusTypeId(statusId, this.getTrialStatusTypeDao());
		}
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
		Collection dutyRosterTurns = dutyRosterTurnDao.findByDepartmentStatusStaffTrialCalendarInterval(departmentId, statusId, staffId, unassigned, trialId, calendar,
				CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to));
		dutyRosterTurnDao.toDutyRosterTurnOutVOCollection(dutyRosterTurns);
		if (sort) {
			dutyRosterTurns = new ArrayList(dutyRosterTurns);
			Collections.sort((ArrayList) dutyRosterTurns, new DutyRosterTurnIntervalComparator(false));
		}
		return dutyRosterTurns;
	}

	@Override
	protected Collection<String> handleGetDutyRosterTurnTitles(
			AuthenticationVO auth, Long trialDepartmentId, Long staffId,
			Long trialId, String titleInfix, Integer limit) throws Exception {
		if (trialDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(trialDepartmentId, this.getDepartmentDao());
		}
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (staffId != null) {
			CheckIDUtil.checkStaffId(staffId, this.getStaffDao());
		}
		return this.getDutyRosterTurnDao().findTitles(trialDepartmentId, staffId, trialId, titleInfix, limit);
	}

	@Override
	protected ECRFOutVO handleGetEcrf(AuthenticationVO auth, Long ecrfId) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
		ECRFOutVO result = ecrfDao.toECRFOutVO(ecrf);
		return result;
	}

	@Override
	protected long handleGetEcrfCount(AuthenticationVO auth, Long trialId, Long groupId, Long visitId, Boolean active) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (groupId != null) {
			CheckIDUtil.checkProbandGroupId(groupId, this.getProbandGroupDao());
		}
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		return this.getECRFDao().getCount(trialId, groupId, visitId, active);
	}

	@Override
	protected ECRFFieldOutVO handleGetEcrfField(AuthenticationVO auth, Long ecrfFieldId) throws Exception {
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldId, ecrfFieldDao);
		ECRFFieldOutVO result = ecrfFieldDao.toECRFFieldOutVO(ecrfField);
		return result;
	}

	@Override
	protected long handleGetEcrfFieldCount(AuthenticationVO auth, Long trialId, Long ecrfId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		return this.getECRFFieldDao().getCount(trialId, ecrfId, null, null);
	}

	@Override
	protected long handleGetEcrfFieldCount(AuthenticationVO auth, Long trialId, Long ecrfId, String section) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		return this.getECRFFieldDao().getCount(trialId, ecrfId, section, null, null);
	}

	@Override
	protected Collection<ECRFFieldOutVO> handleGetEcrfFieldList(AuthenticationVO auth, Long trialId, Long ecrfId, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		Collection ecrfFields = ecrfFieldDao.findByTrialEcrfSeriesJs(trialId, ecrfId, sort, null, null, psf);
		ecrfFieldDao.toECRFFieldOutVOCollection(ecrfFields);
		return ecrfFields;
	}

	@Override
	protected Collection<ECRFFieldOutVO> handleGetEcrfFieldList(AuthenticationVO auth, Long trialId, Long ecrfId, String section, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		Collection ecrfFields = ecrfFieldDao.findByTrialEcrfSectionSeriesJs(trialId, ecrfId, section, sort, null, null, psf);
		ecrfFieldDao.toECRFFieldOutVOCollection(ecrfFields);
		return ecrfFields;
	}

	@Override
	protected Long handleGetEcrfFieldMaxPosition(AuthenticationVO auth, Long ecrfId, String section) throws Exception {
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		return this.getECRFFieldDao().findMaxPosition(ecrfId, section);
	}

	@Override
	protected Collection<String> handleGetEcrfFieldSections(AuthenticationVO auth, Long trialId, Long ecrfId, String sectionPrefix, Integer limit) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		return this.getECRFFieldDao().findSections(trialId, ecrfId, sectionPrefix, limit);
	}

	@Override
	protected ECRFFieldStatusEntryOutVO handleGetEcrfFieldStatusEntry(
			AuthenticationVO auth, Long ecrfFieldStatusEntryId) throws Exception {
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ECRFFieldStatusEntry ecrfFieldStatusEntry = CheckIDUtil.checkEcrfFieldStatusEntryId(ecrfFieldStatusEntryId, this.getECRFFieldStatusEntryDao());
		ECRFFieldStatusEntryOutVO result = ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVO(ecrfFieldStatusEntry);
		return result;
	}

	@Override
	protected long handleGetEcrfFieldStatusEntryCount(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long trialId, Long probandListEntryId,
			Long ecrfId, Long visitId, boolean last) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			if (visitId != null) {
				CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_VISIT_ID_IS_NULL);
			}
		}
		return this.getECRFFieldStatusEntryDao().getCount(queue, trialId, probandListEntryId, ecrfId, visitId, last);
	}

	@Override
	protected long handleGetEcrfFieldStatusEntryCount(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long probandListEntryId, Long visitId, Long ecrfFieldId, Long index,
			boolean last,
			Boolean initial, Boolean updated, Boolean proposed, Boolean resolved) throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());
		return this.getECRFFieldStatusEntryDao().getCount(queue, probandListEntryId, visitId, ecrfFieldId, index, last, initial, updated, proposed, resolved);
	}

	@Override
	protected long handleGetEcrfFieldStatusEntryCount(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long trialId, Long probandListEntryId,
			Long ecrfId, Long visitId, String section, boolean last) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			if (visitId != null) {
				CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_VISIT_ID_IS_NULL);
			}
		}
		return this.getECRFFieldStatusEntryDao().getCount(queue, trialId, probandListEntryId, ecrfId, visitId, section, last);
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> handleGetEcrfFieldStatusEntryList(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long probandListEntryId, Long visitId,
			Long ecrfFieldId,
			Long index, boolean last, boolean sort, PSFVO psf) throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		Collection statusEntries = ecrfFieldStatusEntryDao.findByListEntryVisitEcrfFieldIndex(queue, probandListEntryId, visitId, ecrfFieldId, index, last, sort, psf);
		ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(statusEntries);
		return statusEntries;
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> handleGetEcrfFieldStatusEntryLog(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long trialId, Long probandListEntryId,
			Long ecrfId, Long visitId, boolean last, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			Visit visit = null;
			if (visitId != null) {
				visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
			checkEcrfVisit(ecrf, visit);
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_ID_NOT_NULL);
			}
		}
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		Collection statusEntries = ecrfFieldStatusEntryDao.getLog(queue, trialId, probandListEntryId, ecrfId, visitId, last, sort, psf);
		ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(statusEntries);
		return statusEntries;
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> handleGetEcrfFieldStatusEntryLog(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long trialId, Long probandListEntryId,
			Long ecrfId, Long visitId, String section, boolean last, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			Visit visit = null;
			if (visitId != null) {
				visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
			checkEcrfVisit(ecrf, visit);
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_ID_NOT_NULL);
			}
		}
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		Collection statusEntries = ecrfFieldStatusEntryDao.getLog(queue, trialId, probandListEntryId, ecrfId, visitId, section, last, sort, psf);
		ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(statusEntries);
		return statusEntries;
	}

	@Override
	protected ECRFFieldValuesOutVO handleGetEcrfFieldValue(AuthenticationVO auth, Long probandListEntryId, Long visitId, Long ecrfFieldId, Long index)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldId, ecrfFieldDao);
		VisitOutVO visitVO = null;
		if (visitId != null) {
			visitVO = this.getVisitDao().toVisitOutVO(CheckIDUtil.checkVisitId(visitId, this.getVisitDao()));
		}
		checkEcrfFieldValueIndex(ecrfField, probandListEntryId, visitVO != null ? visitVO.getId() : null, ecrfFieldId, index);
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		ECRFFieldValuesOutVO result = new ECRFFieldValuesOutVO();
		Iterator<ECRFFieldValue> it = ecrfFieldValueDao
				.findByListEntryVisitEcrfFieldIndex(probandListEntryId, visitVO != null ? visitVO.getId() : null, ecrfFieldId, index, false, true, null).iterator();
		if (it.hasNext()) {
			ECRFFieldValue ecrfFieldValue = it.next();
			result.getPageValues().add(ecrfFieldValueDao.toECRFFieldValueOutVO(ecrfFieldValue));
			if (!CommonUtil.isEmptyString(ecrfFieldValue.getEcrfField().getJsVariableName()) && ecrfField.getEcrf().isEnableBrowserFieldCalculation()
					&& Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues().add(ecrfFieldValueDao.toECRFFieldValueJsonVO(ecrfFieldValue)); // always return a js value?
			}
		} else {
			result.getPageValues().add(
					ServiceUtil.createPresetEcrfFieldOutValue(probandListEntryDao.toProbandListEntryOutVO(listEntry), visitVO,
							ecrfFieldDao.toECRFFieldOutVO(ecrfField), index, null,
							this.getECRFFieldStatusEntryDao(), this.getECRFFieldStatusTypeDao()));
			if (!CommonUtil.isEmptyString(ecrfField.getJsVariableName()) && ecrfField.getEcrf().isEnableBrowserFieldCalculation()
					&& Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues()
						.add(ServiceUtil.createPresetEcrfFieldJsonValue(ecrfField, visitVO != null ? visitVO.getId() : null, index, this.getInputFieldSelectionSetValueDao()));
			}
		}
		return result;
	}

	@Override
	protected ECRFFieldValuesOutVO handleGetEcrfFieldValue(AuthenticationVO auth, Long probandListEntryId, Long visitId, Long ecrfFieldId, Long index, boolean auditTrail,
			boolean sort,
			PSFVO psf)
			throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());
		if (visitId == null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		checkEcrfFieldValueIndex(ecrfField, probandListEntryId, visitId, ecrfFieldId, index);
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		ECRFFieldValuesOutVO result = new ECRFFieldValuesOutVO();
		Iterator<ECRFFieldValue> it = ecrfFieldValueDao.findByListEntryVisitEcrfFieldIndex(probandListEntryId, visitId, ecrfFieldId, index, auditTrail, sort, psf).iterator();
		if (it.hasNext()) {
			while (it.hasNext()) {
				ECRFFieldValue ecrfFieldValue = it.next();
				result.getPageValues().add(ecrfFieldValueDao.toECRFFieldValueOutVO(ecrfFieldValue));
				if (!CommonUtil.isEmptyString(ecrfFieldValue.getEcrfField().getJsVariableName()) && ecrfField.getEcrf().isEnableBrowserFieldCalculation()
						&& Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
								DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
					result.getJsValues().add(ecrfFieldValueDao.toECRFFieldValueJsonVO(ecrfFieldValue)); // always return a js value?
				}
			}
		}
		return result;
	}

	@Override
	protected ECRFFieldValueOutVO handleGetEcrfFieldValueById(AuthenticationVO auth, Long ecrfFieldValueId) throws Exception {
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		return ecrfFieldValueDao.toECRFFieldValueOutVO(CheckIDUtil.checkEcrfFieldValueId(ecrfFieldValueId, ecrfFieldValueDao));
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, String fieldQuery) throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		return this.getECRFFieldValueDao().getCountField(probandListEntryId, ecrfId, visitId, fieldQuery);
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, boolean excludeAuditTrail) throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		return this.getECRFFieldValueDao().getCount(probandListEntryId, ecrfId, visitId, excludeAuditTrail, null, null);
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, String section, boolean excludeAuditTrail)
			throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		return this.getECRFFieldValueDao().getCount(probandListEntryId, ecrfId, visitId, section, excludeAuditTrail, null);
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, String section, Long index, String fieldQuery)
			throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		index = limitEcrfFieldValueIndex(ecrfId, visitId, section, index, probandListEntryId);
		return this.getECRFFieldValueDao().getCountField(probandListEntryId, ecrfId, visitId, section, index, fieldQuery);
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> handleGetEcrfFieldValueLog(AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId, Long visitId, boolean sort,
			PSFVO psf)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			Visit visit = null;
			if (visitId != null) {
				visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
			checkEcrfVisit(ecrf, visit);
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_ID_NOT_NULL);
			}
		}
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		Collection fieldValues = ecrfFieldValueDao.getLog(trialId, probandListEntryId, ecrfId, visitId, sort, psf);
		ecrfFieldValueDao.toECRFFieldValueOutVOCollection(fieldValues);
		return fieldValues;
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> handleGetEcrfFieldValueLog(AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId, Long visitId, String section,
			boolean sort,
			PSFVO psf)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			Visit visit = null;
			if (visitId != null) {
				visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
			checkEcrfVisit(ecrf, visit);
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_ID_NOT_NULL);
			}
		}
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		Collection fieldValues = ecrfFieldValueDao.getLog(trialId, probandListEntryId, ecrfId, visitId, section, sort, psf);
		ecrfFieldValueDao.toECRFFieldValueOutVOCollection(fieldValues);
		return fieldValues;
	}

	@Override
	protected ECRFFieldValuesOutVO handleGetEcrfFieldValues(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, boolean addSeries, boolean loadAllJsValues,
			String fieldQuery,
			PSFVO psf)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		ECRFOutVO ecrfVO = this.getECRFDao().toECRFOutVO(CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao()));
		VisitOutVO visitVO = null;
		if (visitId != null) {
			visitVO = this.getVisitDao().toVisitOutVO(CheckIDUtil.checkVisitId(visitId, this.getVisitDao()));
		}
		return ServiceUtil.getEcrfFieldValues(listEntryVO, ecrfVO, visitVO, addSeries,
				ecrfVO.isEnableBrowserFieldCalculation() && Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
						DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION),
				loadAllJsValues, fieldQuery, psf, this.getECRFFieldDao(),
				this.getECRFFieldValueDao(), this.getInputFieldSelectionSetValueDao(), this.getECRFFieldStatusEntryDao(),
				this.getECRFFieldStatusTypeDao());
	}

	@Override
	protected ECRFFieldValuesOutVO handleGetEcrfFieldValues(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, String section, Long index,
			boolean addSeries,
			boolean loadAllJsValues, String fieldQuery, PSFVO psf)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		ECRFOutVO ecrfVO = this.getECRFDao().toECRFOutVO(CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao()));
		VisitOutVO visitVO = null;
		if (visitId != null) {
			visitVO = this.getVisitDao().toVisitOutVO(CheckIDUtil.checkVisitId(visitId, this.getVisitDao()));
		}
		return getEcrfFieldValues(listEntryVO, ecrfVO, visitVO, section, index, addSeries,
				ecrfVO.isEnableBrowserFieldCalculation() && Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
						DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION),
				loadAllJsValues, fieldQuery, psf);
	}

	@Override
	protected Long handleGetEcrfFieldValuesSectionMaxIndex(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, String section) throws Exception {
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		if (this.getECRFFieldDao().getCount(null, ecrfId, section, true, null) == 0l) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_SECTION_WITH_NO_FIELDS, section);
		}
		return this.getECRFFieldValueDao().getMaxIndex(probandListEntryId, ecrfId, visitId, section);
	}

	@Override
	protected Collection<ECRFOutVO> handleGetEcrfList(AuthenticationVO auth, Long trialId, Boolean active, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		ECRFDao ecrfDao = this.getECRFDao();
		Collection ecrfs = ecrfDao.findByTrialActiveSorted(trialId, active, sort, psf);
		ecrfDao.toECRFOutVOCollection(ecrfs);
		return ecrfs;
	}

	@Override
	protected Collection<ECRFVisitVO> handleGetEcrfVisitList(AuthenticationVO auth, Long probandListEntryId, Boolean active, PSFVO psf) throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		ECRFDao ecrfDao = this.getECRFDao();
		VisitDao visitDao = this.getVisitDao();
		ArrayList<ECRFVisitVO> result = new ArrayList<ECRFVisitVO>();
		Iterator<Object[]> it = ecrfDao.findByListEntryActiveSorted(probandListEntryId, active, false, psf).iterator();
		while (it.hasNext()) {
			Object[] ecrfVisit = it.next();
			ECRFVisitVO ecrfVisitVO = new ECRFVisitVO();
			ecrfVisitVO.setEcrf(ecrfDao.toECRFOutVO((ECRF) ecrfVisit[0]));
			ecrfVisitVO.setVisit(visitDao.toVisitOutVO((Visit) ecrfVisit[1]));
			result.add(ecrfVisitVO);
		}
		return result;
	}

	@Override
	protected Collection<ECRFOutVO> handleGetEcrfList(AuthenticationVO auth, Long trialId, Long groupId, Long visitId, Boolean active, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (groupId != null) {
			CheckIDUtil.checkProbandGroupId(groupId, this.getProbandGroupDao());
		}
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		ECRFDao ecrfDao = this.getECRFDao();
		Collection ecrfs = ecrfDao.findByTrialGroupVisitActiveSorted(trialId, groupId, visitId, active, sort, psf);
		ecrfDao.toECRFOutVOCollection(ecrfs);
		return ecrfs;
	}

	@Override
	protected ECRFProgressVO handleGetEcrfProgress(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, boolean sectionDetail)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		ECRFDao ecrfDao = this.getECRFDao();
		ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(CheckIDUtil.checkEcrfId(ecrfId, ecrfDao));
		VisitOutVO visitVO = null;
		if (visitId != null) {
			visitVO = this.getVisitDao().toVisitOutVO(CheckIDUtil.checkVisitId(visitId, this.getVisitDao()));
		}
		return ServiceUtil.populateEcrfProgress(listEntryVO, ecrfVO, visitVO, false, sectionDetail, null, null,
				this.getECRFStatusEntryDao(), this.getECRFStatusTypeDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao(),
				this.getVisitScheduleItemDao());
	}

	@Override
	protected ECRFProgressVO handleGetEcrfProgress(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, String section)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		ECRFDao ecrfDao = this.getECRFDao();
		ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(CheckIDUtil.checkEcrfId(ecrfId, ecrfDao));
		VisitOutVO visitVO = null;
		if (visitId != null) {
			visitVO = this.getVisitDao().toVisitOutVO(CheckIDUtil.checkVisitId(visitId, this.getVisitDao()));
		}
		return ServiceUtil.populateEcrfProgress(listEntryVO, ecrfVO, visitVO, false, null, null, section,
				this.getECRFStatusEntryDao(), this.getECRFStatusTypeDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao(),
				this.getVisitScheduleItemDao());
	}

	@Override
	protected ECRFProgressSummaryVO handleGetEcrfProgressSummary(AuthenticationVO auth, Long probandListEntryId, boolean ecrfDetail, boolean sectionDetail)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		return createEcrfProgessSummary(listEntryVO, ecrfDetail, sectionDetail, false, null, null);
	}

	@Override
	protected SignatureVO handleGetEcrfSignature(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId) throws Exception {
		ECRFStatusEntry statusEntry = checkEcrfStatusEntry(probandListEntryId, ecrfId, visitId);
		SignatureDao signatureDao = this.getSignatureDao();
		Signature signature = signatureDao.findRecentSignature(SignatureModule.ECRF_SIGNATURE, statusEntry.getId());
		if (signature == null) {
			ServiceException e = L10nUtil.initServiceException(ServiceExceptionCodes.NO_SIGNATURE);
			e.setLogError(false);
			throw e;
		}
		SignatureVO result = signatureDao.toSignatureVO(signature);
		result.setDescription(EntitySignature.getDescription(result));
		return result;
	}

	@Override
	protected ECRFStatusEntryVO handleGetEcrfStatusEntry(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId) throws Exception {
		ECRFStatusEntry statusEntry = checkEcrfStatusEntry(probandListEntryId, ecrfId, visitId);
		return this.getECRFStatusEntryDao().toECRFStatusEntryVO(statusEntry);
	}

	@Override
	protected long handleGetEcrfStatusEntryCount(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, Long ecrfStatusTypeId, Boolean valueLockdown,
			Boolean done,
			Boolean validated,
			Boolean review, Boolean verified) throws Exception {
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			Visit visit = null;
			if (visitId != null) {
				visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
			checkEcrfVisit(ecrf, visit);
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_ID_NOT_NULL);
			}
		}
		if (ecrfStatusTypeId != null) {
			CheckIDUtil.checkEcrfStatusTypeId(ecrfStatusTypeId, this.getECRFStatusTypeDao());
		}
		return this.getECRFStatusEntryDao().getCount(probandListEntryId, ecrfId, visitId, ecrfStatusTypeId, valueLockdown, done, validated,
				review, verified);
	}

	@Override
	protected Collection<TrialOutVO> handleGetEcrfTrialList(AuthenticationVO auth, Long departmentId, PSFVO psf) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		TrialDao trialDao = this.getTrialDao();
		Collection trials = trialDao.findByEcrfs(departmentId, false, psf);
		trialDao.toTrialOutVOCollection(trials);
		return trials;
	}

	@Override
	protected InquiryOutVO handleGetInquiry(AuthenticationVO auth, Long inquiryId) throws Exception {
		InquiryDao inquiryDao = this.getInquiryDao();
		Inquiry inquiry = CheckIDUtil.checkInquiryId(inquiryId, inquiryDao);
		Trial trial = inquiry.getTrial();
		InputField field = inquiry.getField();
		InquiryOutVO result = inquiryDao.toInquiryOutVO(inquiry);
		return result;
	}

	@Override
	protected Collection<String> handleGetInquiryCategories(AuthenticationVO auth, Long trialId,
			String categoryPrefix, Boolean active, Boolean signupActive, Integer limit) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getInquiryDao().findCategories(trialId, categoryPrefix, active, signupActive, limit);
	}

	@Override
	protected Collection<InquiryOutVO> handleGetInquiryList(AuthenticationVO auth, Long trialId, Boolean active, Boolean signupActive) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findByParticipantsActiveSorted(trialId, active, signupActive);
		inquiryDao.toInquiryOutVOCollection(inquiries);
		return inquiries;
	}

	@Override
	protected Collection<InquiryOutVO> handleGetInquiryList(AuthenticationVO auth, Long trialId, Boolean active, Boolean signupActive, Long probandId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findByTrialActiveExcelProbandSorted(trialId, active, signupActive, null, probandId);
		inquiryDao.toInquiryOutVOCollection(inquiries);
		return inquiries;
	}

	@Override
	protected Collection<InquiryOutVO> handleGetInquiryList(AuthenticationVO auth, Long trialId, Boolean active, Boolean signupActive,
			boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findByTrialActiveJs(trialId, active, signupActive, sort, null, psf);
		inquiryDao.toInquiryOutVOCollection(inquiries);
		return inquiries;
	}

	@Override
	protected Collection<InquiryOutVO> handleGetInquiryList(AuthenticationVO auth, Long trialId,
			String category, Boolean active, Boolean signupActive, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findByTrialCategoryActiveJs(trialId, category, active, signupActive, sort, null, psf);
		inquiryDao.toInquiryOutVOCollection(inquiries);
		return inquiries;
	}

	@Override
	protected Long handleGetInquiryMaxPosition(AuthenticationVO auth, Long trialId, String category) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getInquiryDao().findMaxPosition(trialId, category);
	}

	@Override
	protected InquiryValuesOutVO handleGetInquiryPresetValues(
			AuthenticationVO auth, Long trialId, Boolean active, Boolean signupActive,
			boolean sort, boolean loadAllJsValues, PSFVO psf) throws Exception {
		CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		InquiryDao inquiryDao = this.getInquiryDao();
		InquiryValuesOutVO result = new InquiryValuesOutVO();
		Iterator<Inquiry> inquiriesIt = inquiryDao.findByTrialActiveJs(trialId, active, signupActive, sort, null, psf).iterator();
		while (inquiriesIt.hasNext()) {
			Inquiry inquiry = inquiriesIt.next();
			result.getPageValues().add(ServiceUtil.createPresetInquiryOutValue(null, inquiryDao.toInquiryOutVO(inquiry), null));
			if (!loadAllJsValues && !CommonUtil.isEmptyString(inquiry.getJsVariableName())
					&& Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues().add(ServiceUtil.createPresetInquiryJsonValue(inquiry, inputFieldSelectionSetValueDao));
			}
		}
		if (loadAllJsValues && Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
				DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
			inquiriesIt = inquiryDao.findByTrialActiveJs(trialId, active, signupActive, false, true, null).iterator();
			while (inquiriesIt.hasNext()) {
				Inquiry inquiry = inquiriesIt.next();
				result.getJsValues().add(ServiceUtil.createPresetInquiryJsonValue(inquiry, inputFieldSelectionSetValueDao));
			}
		}
		return result;
	}

	@Override
	protected InquiryValuesOutVO handleGetInquiryPresetValues(
			AuthenticationVO auth, Long trialId, String category, Boolean active, Boolean signupActive,
			boolean sort, boolean loadAllJsValues, PSFVO psf) throws Exception {
		CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		InquiryDao inquiryDao = this.getInquiryDao();
		InquiryValuesOutVO result = new InquiryValuesOutVO();
		Iterator<Inquiry> inquiriesIt = inquiryDao.findByTrialCategoryActiveJs(trialId, category, active, signupActive, sort, null, psf).iterator();
		while (inquiriesIt.hasNext()) {
			Inquiry inquiry = inquiriesIt.next();
			result.getPageValues().add(ServiceUtil.createPresetInquiryOutValue(null, inquiryDao.toInquiryOutVO(inquiry), null));
			if (!loadAllJsValues && !CommonUtil.isEmptyString(inquiry.getJsVariableName())
					&& Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues().add(ServiceUtil.createPresetInquiryJsonValue(inquiry, inputFieldSelectionSetValueDao));
			}
		}
		if (loadAllJsValues && Settings.getBoolean(SettingCodes.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
				DefaultSettings.INQUIRY_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
			inquiriesIt = inquiryDao.findByTrialCategoryActiveJs(trialId, category, active, signupActive, false, true, null).iterator();
			while (inquiriesIt.hasNext()) {
				Inquiry inquiry = inquiriesIt.next();
				result.getJsValues().add(ServiceUtil.createPresetInquiryJsonValue(inquiry, inputFieldSelectionSetValueDao));
			}
		}
		return result;
	}

	@Override
	protected ECRFFieldStatusEntryOutVO handleGetLastEcrfFieldStatusEntry(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long probandListEntryId, Long visitId,
			Long ecrfFieldId, Long index)
			throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ECRFFieldStatusEntry ecrfFieldStatusEntry = ecrfFieldStatusEntryDao.findLastStatus(queue, probandListEntryId, visitId, ecrfFieldId, index);
		if (ecrfFieldStatusEntry != null) {
			return ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVO(ecrfFieldStatusEntry);
		}
		return null;
	}

	@Override
	protected ProbandGroupOutVO handleGetProbandGroup(AuthenticationVO auth, Long probandGroupId)
			throws Exception {
		ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
		ProbandGroup probandGroup = CheckIDUtil.checkProbandGroupId(probandGroupId, probandGroupDao);
		ProbandGroupOutVO result = probandGroupDao.toProbandGroupOutVO(probandGroup);
		return result;
	}

	@Override
	protected long handleGetProbandGroupCount(
			AuthenticationVO auth, Long trialId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getProbandGroupDao().getCount(trialId);
	}

	@Override
	protected Collection<ProbandGroupOutVO> handleGetProbandGroupList(
			AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
		Collection probandGroups = probandGroupDao.findByTrial(trialId, psf);
		probandGroupDao.toProbandGroupOutVOCollection(probandGroups);
		return probandGroups;
	}

	@Override
	protected ProbandListEntryOutVO handleGetProbandListEntry(
			AuthenticationVO auth, Long probandListEntryId) throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry probandListEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
		ProbandListEntryOutVO result = probandListEntryDao.toProbandListEntryOutVO(probandListEntry);
		return result;
	}

	@Override
	protected long handleGetProbandListEntryCount(
			AuthenticationVO auth, Long trialId, Long probandGroupId, Long probandId, boolean total) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandGroupId != null) {
			CheckIDUtil.checkProbandGroupId(probandGroupId, this.getProbandGroupDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getProbandListEntryDao().getTrialGroupProbandCount(trialId, probandGroupId, probandId, total);
	}

	@Override
	protected Collection<ProbandListEntryOutVO> handleGetProbandListEntryList(
			AuthenticationVO auth, Long trialId, Long probandGroupId, Long probandId, boolean total, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandGroupId != null) {
			CheckIDUtil.checkProbandGroupId(probandGroupId, this.getProbandGroupDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		Collection listEntries = probandListEntryDao.findByTrialGroupProbandCountPerson(trialId, probandGroupId, probandId, total, null, psf);
		probandListEntryDao.toProbandListEntryOutVOCollection(listEntries);
		return listEntries;
	}

	@Override
	protected Long handleGetProbandListEntryMaxPosition(AuthenticationVO auth, Long trialId)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getProbandListEntryDao().findMaxPosition(trialId);
	}

	@Override
	protected ProbandListEntryTagOutVO handleGetProbandListEntryTag(
			AuthenticationVO auth, Long probandListEntryTagId) throws Exception {
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		ProbandListEntryTag probandListEntryTag = CheckIDUtil.checkProbandListEntryTagId(probandListEntryTagId, probandListEntryTagDao);
		ProbandListEntryTagOutVO result = probandListEntryTagDao.toProbandListEntryTagOutVO(probandListEntryTag);
		return result;
	}

	@Override
	protected long handleGetProbandListEntryTagCount(AuthenticationVO auth, Long trialId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getProbandListEntryTagDao().getCount(trialId);
	}

	@Override
	protected Collection<ProbandListEntryTagValueOutVO> handleGetProbandListEntryTagInputFieldValues(
			AuthenticationVO auth, Long probandListEntryId, Long inputFieldId)
			throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		CheckIDUtil.checkInputFieldId(inputFieldId, this.getInputFieldDao());
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		Collection probandListEntryTagFieldValues = probandListEntryTagValueDao.findByListEntryField(probandListEntryId, inputFieldId);
		probandListEntryTagValueDao.toProbandListEntryTagValueOutVOCollection(probandListEntryTagFieldValues);
		return probandListEntryTagFieldValues;
	}

	@Override
	protected Collection<ProbandListEntryTagValueJsonVO> handleGetProbandListEntryTagJsonValues(AuthenticationVO auth, Long probandListEntryId, boolean sort, PSFVO psf)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		return ServiceUtil.getProbandListEntryTagJsonValues(probandListEntryTagValueDao.findByListEntryJs(listEntryVO.getId(), sort, null, psf),
				false, probandListEntryTagValueDao, this.getInputFieldSelectionSetValueDao());
	}

	@Override
	protected Collection<ProbandListEntryTagOutVO> handleGetProbandListEntryTagList(
			AuthenticationVO auth, Long trialId, Long probandId, Boolean stratification) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		Collection listTagValues = probandListEntryTagDao.findByTrialExcelEcrfStratificationProbandSorted(trialId, null, null, stratification, probandId);
		probandListEntryTagDao.toProbandListEntryTagOutVOCollection(listTagValues);
		return listTagValues;
	}

	@Override
	protected Collection<ProbandListEntryTagOutVO> handleGetProbandListEntryTagList(
			AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		Collection listTags = probandListEntryTagDao.findByTrialFieldJs(trialId, null, false, null, psf);
		probandListEntryTagDao.toProbandListEntryTagOutVOCollection(listTags);
		return listTags;
	}

	@Override
	protected Long handleGetProbandListEntryTagMaxPosition(AuthenticationVO auth, Long trialId)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getProbandListEntryTagDao().findMaxPosition(trialId);
	}

	@Override
	protected ProbandListEntryTagValuesOutVO handleGetProbandListEntryTagValue(
			AuthenticationVO auth, Long probandListEntryId, Long probandListEntryTagId)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		ProbandListEntryTag listEntryTag = CheckIDUtil.checkProbandListEntryTagId(probandListEntryTagId, probandListEntryTagDao);
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		ProbandListEntryTagValuesOutVO result = new ProbandListEntryTagValuesOutVO();
		Iterator<ProbandListEntryTagValue> it = probandListEntryTagValueDao.findByListEntryListEntryTag(probandListEntryId, probandListEntryTagId).iterator();
		if (it.hasNext()) {
			ProbandListEntryTagValue probandListEntryTagValue = it.next();
			result.getPageValues().add(probandListEntryTagValueDao.toProbandListEntryTagValueOutVO(probandListEntryTagValue));
			if (!CommonUtil.isEmptyString(probandListEntryTagValue.getTag().getJsVariableName())
					&& Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues().add(probandListEntryTagValueDao.toProbandListEntryTagValueJsonVO(probandListEntryTagValue));
			}
		} else {
			result.getPageValues().add(
					ServiceUtil.createPresetProbandListEntryTagOutValue(probandListEntryDao.toProbandListEntryOutVO(listEntry),
							probandListEntryTagDao.toProbandListEntryTagOutVO(listEntryTag), null));
			if (!CommonUtil.isEmptyString(listEntryTag.getJsVariableName())
					&& Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues().add(ServiceUtil.createPresetProbandListEntryTagJsonValue(listEntryTag, this.getInputFieldSelectionSetValueDao()));
			}
		}
		return result;
	}

	@Override
	protected ProbandListEntryTagValueOutVO handleGetProbandListEntryTagValueById(AuthenticationVO auth, Long probandListEntryTagValueId) throws Exception {
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		return probandListEntryTagValueDao.toProbandListEntryTagValueOutVO(CheckIDUtil.checkProbandListEntryTagValueId(probandListEntryTagValueId, probandListEntryTagValueDao));
	}

	@Override
	protected long handleGetProbandListEntryTagValueCount(AuthenticationVO auth, Long probandListEntryId) throws Exception {
		ProbandListEntry probandListEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		return this.getProbandListEntryTagValueDao().getCount(probandListEntryId, null);
	}

	@Override
	protected ProbandListEntryTagValuesOutVO handleGetProbandListEntryTagValues(
			AuthenticationVO auth, Long probandListEntryId, boolean sort, boolean loadAllJsValues, PSFVO psf) throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		return ServiceUtil.getProbandListEntryTagValues(listEntryVO,
				Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
						DefaultSettings.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION),
				loadAllJsValues, sort, psf, this.getProbandListEntryTagDao(), this.getProbandListEntryTagValueDao(), this.getInputFieldSelectionSetValueDao());
	}

	@Override
	protected Collection<ProbandListStatusEntryOutVO> handleGetProbandListStatus(
			AuthenticationVO auth, Long trialId, Long probandId, boolean last, PSFVO psf)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
		Collection statusEntries = probandListStatusEntryDao.findByTrialProband(trialId, probandId, last, psf);
		probandListStatusEntryDao.toProbandListStatusEntryOutVOCollection(statusEntries);
		return statusEntries;
	}

	@Override
	protected ProbandListStatusEntryOutVO handleGetProbandListStatusEntry(
			AuthenticationVO auth, Long probandListStatusEntryId) throws Exception {
		ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
		ProbandListStatusEntry probandListStatusEntry = CheckIDUtil.checkProbandListStatusEntryId(probandListStatusEntryId, probandListStatusEntryDao);
		ProbandListStatusEntryOutVO result = probandListStatusEntryDao.toProbandListStatusEntryOutVO(probandListStatusEntry);
		return result;
	}

	@Override
	protected ProbandListStatusEntryOutVO handleGetProbandListStatusEntryAtVisitScheduleItem(
			AuthenticationVO auth, Long probandId, Long visitScheduleItemId) throws Exception {
		ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		CheckIDUtil.checkVisitScheduleItemId(visitScheduleItemId, this.getVisitScheduleItemDao());
		VisitScheduleItem visitScheduleItem = this.getVisitScheduleItemDao().findExpandedDateMode(visitScheduleItemId, probandId).iterator().next();
		ProbandListStatusEntry probandListStatusEntry = probandListStatusEntryDao.findRecentStatus(visitScheduleItem.getTrial().getId(), probandId, visitScheduleItem.getStop());
		ProbandListStatusEntryOutVO result = null;
		if (probandListStatusEntry != null) {
			result = probandListStatusEntryDao.toProbandListStatusEntryOutVO(probandListStatusEntry);
		}
		return result;
	}

	@Override
	protected Collection<ProbandListStatusEntryOutVO> handleGetProbandListStatusEntryList(
			AuthenticationVO auth, Long probandListEntryId, PSFVO psf) throws Exception {
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
		Collection statusEntries = probandListStatusEntryDao.findByListEntry(probandListEntryId, psf);
		probandListStatusEntryDao.toProbandListStatusEntryOutVOCollection(statusEntries);
		return statusEntries;
	}

	@Override
	protected MoneyTransferSummaryVO handleGetProbandMoneyTransferNoParticipationSummary(
			AuthenticationVO auth, Long probandId, Long trialId,
			String costType, PaymentMethod method, Boolean paid) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		ProbandOutVO probandVO = probandDao.toProbandOutVO(CheckIDUtil.checkProbandId(probandId, probandDao));
		if (!probandVO.isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PROBAND_NOT_PERSON);
		}
		TrialDao trialDao = this.getTrialDao();
		TrialOutVO trialVO = null;
		if (trialId != null) {
			trialVO = trialDao.toTrialOutVO(CheckIDUtil.checkTrialId(trialId, trialDao));
		}
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes;
		Collection<MoneyTransfer> moneyTransfers;
		MoneyTransferSummaryVO summary = new MoneyTransferSummaryVO();
		if (trialVO != null) {
			costTypes = moneyTransferDao.getCostTypes(null, trialVO.getId(), null, null, method);
			moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialVO.getId(), null, probandVO.getId(), method, costType, paid, null, null);
		} else {
			costTypes = moneyTransferDao.getCostTypes(null, null, null, probandVO.getId(), method);
			moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, null, null, probandVO.getId(), method, costType, paid, null, null);
		}
		ServiceUtil.populateMoneyTransferSummary(summary, costTypes, moneyTransfers, true, true, false, false, this.getBankAccountDao());
		summary.setListEntry(null);
		summary.setTrial(trialVO);
		summary.setProband(probandVO);
		summary.setId(probandVO.getId());
		return summary;
	}

	@Override
	protected Collection<MoneyTransferSummaryVO> handleGetProbandMoneyTransferNoParticipationSummaryList(
			AuthenticationVO auth, Long trialId,
			String costType, PaymentMethod method, Boolean paid, boolean total, PSFVO psf) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		TrialOutVO trialVO = trialDao.toTrialOutVO(CheckIDUtil.checkTrialId(trialId, trialDao));
		ProbandDao probandDao = this.getProbandDao();
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes = moneyTransferDao.getCostTypes(null, trialVO.getId(), null, null, method);
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		Collection probands = probandDao.findByMoneyTransferNoParticipation(trialVO.getId(), method, costType, paid, total, true, psf);
		ArrayList<MoneyTransferSummaryVO> result = new ArrayList<MoneyTransferSummaryVO>(probands.size());
		Iterator it = probands.iterator();
		while (it.hasNext()) {
			ProbandOutVO probandVO = probandDao.toProbandOutVO((Proband) it.next());
			MoneyTransferSummaryVO summary = new MoneyTransferSummaryVO();
			Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialVO.getId(), null, probandVO.getId(), method,
					costType,
					paid, null, null);
			ServiceUtil.populateMoneyTransferSummary(summary, costTypes, moneyTransfers, true, true, false, false, bankAccountDao);
			summary.setListEntry(null);
			summary.setTrial(trialVO);
			summary.setProband(probandVO);
			summary.setId(probandVO.getId());
			result.add(summary);
		}
		return result;
	}

	@Override
	protected MoneyTransferSummaryVO handleGetProbandMoneyTransferSummary(
			AuthenticationVO auth, Long probandListEntryId,
			String costType, PaymentMethod method, Boolean paid) throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		if (!listEntryVO.getProband().isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PROBAND_NOT_PERSON);
		}
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes = moneyTransferDao.getCostTypes(null, listEntryVO.getTrial().getId(), null, null, method);
		MoneyTransferSummaryVO summary = new MoneyTransferSummaryVO();
		Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, listEntryVO.getTrial().getId(), null, listEntryVO.getProband()
				.getId(), method, costType, paid, null, null);
		ServiceUtil.populateMoneyTransferSummary(summary, costTypes, moneyTransfers, true, true, false, false, this.getBankAccountDao());
		summary.setListEntry(listEntryVO);
		summary.setTrial(listEntryVO.getTrial());
		summary.setProband(listEntryVO.getProband());
		summary.setId(listEntryVO.getId());
		return summary;
	}

	@Override
	protected Collection<MoneyTransferSummaryVO> handleGetProbandMoneyTransferSummaryList(
			AuthenticationVO auth, Long trialId,
			String costType, PaymentMethod method, Boolean paid, boolean total, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes = moneyTransferDao.getCostTypes(null, trialId, null, null, method);
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		Collection listEntries = probandListEntryDao.findByTrialGroupProbandCountPerson(trialId, null, null, total, true, psf);
		ArrayList<MoneyTransferSummaryVO> result = new ArrayList<MoneyTransferSummaryVO>(listEntries.size());
		Iterator it = listEntries.iterator();
		while (it.hasNext()) {
			ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO((ProbandListEntry) it.next());
			MoneyTransferSummaryVO summary = new MoneyTransferSummaryVO();
			Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialId, null, listEntryVO.getProband().getId(), method,
					costType, paid, null, null);
			ServiceUtil.populateMoneyTransferSummary(summary, costTypes, moneyTransfers, true, true, false, false, bankAccountDao);
			summary.setListEntry(listEntryVO);
			summary.setTrial(listEntryVO.getTrial());
			summary.setProband(listEntryVO.getProband());
			summary.setId(listEntryVO.getId());
			result.add(summary);
		}
		return result;
	}

	@Override
	protected ShiftDurationSummaryVO handleGetShiftDurationSummary(
			AuthenticationVO auth, Long staffId, String calendar, Date from, Date to)
			throws Exception {
		ShiftDurationSummaryVO result = new ShiftDurationSummaryVO();
		if (staffId != null) {
			result.setStaff(this.getStaffDao().toStaffOutVO(CheckIDUtil.checkStaffId(staffId, this.getStaffDao())));
		}
		result.setStart(from);
		result.setStop(to);
		result.setCalendar(calendar);
		ServiceUtil.populateShiftDurationSummary(true, result, this.getDutyRosterTurnDao(), this.getHolidayDao(), this.getStaffStatusEntryDao());
		return result;
	}

	@Override
	protected Collection<TrialOutVO> handleGetSignupTrialList(AuthenticationVO auth, Long departmentId, PSFVO psf) throws Exception {
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		TrialDao trialDao = this.getTrialDao();
		Collection trials = trialDao.findBySignup(departmentId, false, psf);
		trialDao.toTrialOutVOCollection(trials);
		return trials;
	}

	@Override
	protected StratificationRandomizationListOutVO handleGetStratificationRandomizationList(AuthenticationVO auth, Long stratificationRandomizationListId) throws Exception {
		StratificationRandomizationListDao stratificationRandomizationListDao = this.getStratificationRandomizationListDao();
		StratificationRandomizationList randomizationList = CheckIDUtil.checkStratificationRandomizationListId(stratificationRandomizationListId,
				stratificationRandomizationListDao);
		StratificationRandomizationListOutVO result = stratificationRandomizationListDao.toStratificationRandomizationListOutVO(randomizationList);
		return result;
	}

	@Override
	protected long handleGetStratificationRandomizationListCount(AuthenticationVO auth, Long trialId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getStratificationRandomizationListDao().getCount(trialId);
	}

	@Override
	protected Collection<StratificationRandomizationListOutVO> handleGetStratificationRandomizationListList(AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		StratificationRandomizationListDao stratificationRandomizationListDao = this.getStratificationRandomizationListDao();
		Collection randomizationLists = stratificationRandomizationListDao.findByTrial(trialId, psf);
		stratificationRandomizationListDao.toStratificationRandomizationListOutVOCollection(randomizationLists);
		return randomizationLists;
	}

	@Override
	protected TeamMemberOutVO handleGetTeamMember(AuthenticationVO auth, Long teamMemberId)
			throws Exception {
		TeamMemberDao teamMemberDao = this.getTeamMemberDao();
		TeamMember teamMember = CheckIDUtil.checkTeamMemberId(teamMemberId, teamMemberDao);
		TeamMemberOutVO result = teamMemberDao.toTeamMemberOutVO(teamMember);
		return result;
	}

	@Override
	protected long handleGetTeamMemberCount(AuthenticationVO auth, Long trialId,
			Long roleId, Boolean allocatable) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (roleId != null) {
			CheckIDUtil.checkTeamMemberRoleId(roleId, this.getTeamMemberRoleDao());
		}
		return this.getTeamMemberDao().getCount(trialId, null, roleId, allocatable);
	}

	@Override
	protected Collection<TeamMemberOutVO> handleGetTeamMemberList(AuthenticationVO auth, Long trialId,
			Long roleId, Boolean allocatable, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (roleId != null) {
			CheckIDUtil.checkTeamMemberRoleId(roleId, this.getTeamMemberRoleDao());
		}
		TeamMemberDao teamMemberDao = this.getTeamMemberDao();
		Collection teamMembers = teamMemberDao.findByTrialStaffRole(trialId, null, roleId, allocatable, psf);
		teamMemberDao.toTeamMemberOutVOCollection(teamMembers);
		return teamMembers;
	}

	@Override
	protected TimelineEventOutVO handleGetTimelineEvent(AuthenticationVO auth, Long timelineEventId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth)
			throws Exception {
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent timelineEvent = CheckIDUtil.checkTimelineEventId(timelineEventId, timelineEventDao);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent, maxInstances, maxParentDepth, maxChildrenDepth);
		return result;
	}

	@Override
	protected long handleGetTimelineEventCount(
			AuthenticationVO auth, Long trialId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getTimelineEventDao().getCount(trialId);
	}

	@Override
	protected Collection<TimelineEventOutVO> handleGetTimelineEventInterval(
			AuthenticationVO auth, Long trialId, Long departmentId, Long statusId, Long typeId,
			Boolean show, Date from, Date to) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (statusId != null) {
			CheckIDUtil.checkTrialStatusTypeId(statusId, this.getTrialStatusTypeDao());
		}
		if (typeId != null) {
			CheckIDUtil.checkTimelineEventTypeId(typeId, this.getTimelineEventTypeDao());
		}
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		Collection timelineEvents = timelineEventDao.findByTrialDepartmentStatusTypeShowInterval(trialId, departmentId, statusId, typeId, show, CommonUtil.dateToTimestamp(from),
				CommonUtil.dateToTimestamp(to));
		timelineEventDao.toTimelineEventOutVOCollection(timelineEvents);
		return timelineEvents;
	}

	@Override
	protected Collection<TimelineEventOutVO> handleGetTimelineEventList(
			AuthenticationVO auth, Long trialId, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		Collection timelineEvents = timelineEventDao.findByTrial(trialId, psf);
		//timelineEventDao.toTimelineEventOutVOCollection(timelineEvents);
		ArrayList<TimelineEventOutVO> result = new ArrayList<TimelineEventOutVO>(timelineEvents.size());
		Iterator<TimelineEvent> timelineEventsIt = timelineEvents.iterator();
		while (timelineEventsIt.hasNext()) {
			result.add(timelineEventDao.toTimelineEventOutVO(timelineEventsIt.next(), maxInstances, maxParentDepth, maxChildrenDepth));
		}
		return result;
	}

	@Override
	protected Collection<TimelineEventOutVO> handleGetTimelineInterval(
			AuthenticationVO auth, Long trialId, Long departmentId,
			Long teamMemberStaffId, Boolean notify, Boolean ignoreTimelineEvents, Date from, Date to)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (teamMemberStaffId != null) {
			CheckIDUtil.checkStaffId(teamMemberStaffId, this.getStaffDao());
		}
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		Collection timelineEvents = timelineEventDao.findByTrialDepartmentMemberInterval(trialId, departmentId, teamMemberStaffId, notify, ignoreTimelineEvents,
				CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to));
		timelineEventDao.toTimelineEventOutVOCollection(timelineEvents);
		return timelineEvents;
	}

	@Override
	protected Collection<TimelineEventOutVO> handleGetTimelineSchedule(
			AuthenticationVO auth, Date today, Long trialId, Long departmentId, Long teamMemberStaffId, Boolean notify,
			Boolean ignoreTimelineEvents, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (teamMemberStaffId != null) {
			CheckIDUtil.checkStaffId(teamMemberStaffId, this.getStaffDao());
		}
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		Collection timelineEvents = timelineEventDao.findTimelineSchedule(today, trialId, departmentId, teamMemberStaffId, notify, ignoreTimelineEvents, true, psf);
		ArrayList<TimelineEventOutVO> result = new ArrayList<TimelineEventOutVO>(timelineEvents.size());
		Iterator<TimelineEvent> timelineEventsIt = timelineEvents.iterator();
		while (timelineEventsIt.hasNext()) {
			result.add(timelineEventDao.toTimelineEventOutVO(timelineEventsIt.next(), maxInstances, maxParentDepth, maxChildrenDepth));
		}
		return result;
	}

	/**
	 * @see org.phoenixctms.ctsms.service.trial.TrialService#getTrial(Long)
	 */
	@Override
	protected TrialOutVO handleGetTrial(AuthenticationVO auth, Long trialId) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialOutVO result = trialDao.toTrialOutVO(trial);
		return result;
	}

	@Override
	protected Collection<CourseOutVO> handleGetTrialCourseList(
			AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		CourseDao courseDao = this.getCourseDao();
		Collection courses = courseDao.findByTrial(trialId, psf);
		courseDao.toCourseOutVOCollection(courses);
		return courses;
	}

	@Override
	protected TrialECRFProgressSummaryVO handleGetTrialEcrfProgressSummary(AuthenticationVO auth, Long trialId, Long probandDepartmentId, Date from, Date to)
			throws Exception {
		TrialDao trialDao = this.getTrialDao();
		TrialOutVO trialVO = trialDao.toTrialOutVO(CheckIDUtil.checkTrialId(trialId, trialDao));
		if (probandDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(probandDepartmentId, this.getDepartmentDao());
		}
		ECRFDao ecrfDao = this.getECRFDao();
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		TrialECRFProgressSummaryVO result = new TrialECRFProgressSummaryVO();
		result.setTrial(trialVO);
		result.setEcrfTotalCount(0l);
		result.setEcrfDoneCount(0l);
		result.setEcrfOverdueCount(0l);
		result.setCharge(0.0f);
		result.setEcrfStatusEntryCount(0l);
		Iterator<ProbandListEntry> listEntriesIt = probandListEntryDao.findByTrialProbandDepartment(trialId, probandDepartmentId).iterator();
		while (listEntriesIt.hasNext()) {
			ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntriesIt.next());
			ECRFProgressSummaryVO ecrfProgressSummary = createEcrfProgessSummary(listEntryVO, false, false, true, from, to);
			result.setEcrfTotalCount(result.getEcrfTotalCount() + ecrfProgressSummary.getEcrfTotalCount());
			result.setEcrfDoneCount(result.getEcrfDoneCount() + ecrfProgressSummary.getEcrfDoneCount());
			result.setEcrfOverdueCount(result.getEcrfOverdueCount() + ecrfProgressSummary.getEcrfOverdueCount());
			result.setCharge(result.getCharge() + ecrfProgressSummary.getCharge());
			result.setEcrfStatusEntryCount(result.getEcrfStatusEntryCount() + ecrfProgressSummary.getEcrfStatusEntryCount());
			result.setEcrfFieldStatusQueueCounts(ServiceUtil.addEcrfFieldStatusEntryCounts(result.getEcrfFieldStatusQueueCounts(),
					ecrfProgressSummary.getEcrfFieldStatusQueueCounts()));
			result.getListEntries().add(ecrfProgressSummary);
		}
		return result;
	}

	@Override
	protected long handleGetTrialInventoryBookingCount(
			AuthenticationVO auth, Long trialId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getInventoryBookingDao().getCount(null, null, trialId, null, null);
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetTrialInventoryBookingList(
			AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection inventoryBookings = inventoryBookingDao.findByTrial(trialId, psf);
		inventoryBookingDao.toInventoryBookingOutVOCollection(inventoryBookings);
		return inventoryBookings;
	}

	@Override
	protected Collection<TrialOutVO> handleGetTrialList(AuthenticationVO auth, Long trialId, Long departmentId,
			PSFVO psf) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, trialDao);
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		Collection trials = trialDao.findByIdDepartment(trialId, departmentId, psf);
		trialDao.toTrialOutVOCollection(trials);
		return trials;
	}

	@Override
	protected Collection<MassMailOutVO> handleGetTrialMassMailList(AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		MassMailDao massMailDao = this.getMassMailDao();
		Collection massMails = massMailDao.findByTrialProbandListStatusTypeLocked(trialId, null, null, psf);
		massMailDao.toMassMailOutVOCollection(massMails);
		return massMails;
	}

	@Override
	protected MoneyTransferSummaryVO handleGetTrialMoneyTransferSummary(
			AuthenticationVO auth, Long trialId,
			String costType, PaymentMethod method, Boolean paid) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		TrialOutVO trialVO = trialDao.toTrialOutVO(CheckIDUtil.checkTrialId(trialId, trialDao));
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes = moneyTransferDao.getCostTypes(trialVO.getDepartment().getId(), null, null, null, method);
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		MoneyTransferSummaryVO summary = new MoneyTransferSummaryVO();
		Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialVO.getId(), null, null, method, costType, paid, true,
				null);
		ServiceUtil.populateMoneyTransferSummary(summary, costTypes, moneyTransfers, true, true, false, false, bankAccountDao);
		summary.setListEntry(null);
		summary.setTrial(trialVO);
		summary.setProband(null);
		summary.setId(trialVO.getId());
		return summary;
	}

	@Override
	protected Collection<MoneyTransferSummaryVO> handleGetTrialMoneyTransferSummaryList(
			AuthenticationVO auth, Long trialDepartmentId, Long probandDepartmentId,
			String costType, PaymentMethod method, Boolean paid, PSFVO psf) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		if (trialDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(trialDepartmentId, this.getDepartmentDao());
		}
		if (probandDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(probandDepartmentId, this.getDepartmentDao());
		}
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes = moneyTransferDao.getCostTypes(trialDepartmentId, null, probandDepartmentId, null, method);
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		Collection trials = trialDao.findByDepartmentPayoffs(trialDepartmentId, true, psf);
		ArrayList<MoneyTransferSummaryVO> result = new ArrayList<MoneyTransferSummaryVO>(trials.size());
		Iterator it = trials.iterator();
		while (it.hasNext()) {
			TrialOutVO trialVO = trialDao.toTrialOutVO((Trial) it.next());
			MoneyTransferSummaryVO summary = new MoneyTransferSummaryVO();
			Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialVO.getId(), probandDepartmentId, null, method,
					costType, paid, true,
					null);
			ServiceUtil.populateMoneyTransferSummary(summary, costTypes, moneyTransfers, true, true, false, false, bankAccountDao);
			summary.setListEntry(null);
			summary.setTrial(trialVO);
			summary.setProband(null);
			summary.setId(trialVO.getId());
			result.add(summary);
		}
		return result;
	}

	@Override
	protected TrialRandomizationListVO handleGetTrialRandomizationList(AuthenticationVO auth, Long trialId) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialRandomizationListVO result = trialDao.toTrialRandomizationListVO(trial);
		return result;
	}

	@Override
	protected SignatureVO handleGetTrialSignature(AuthenticationVO auth, Long trialId)
			throws Exception {
		Trial trial = CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		SignatureDao signatureDao = this.getSignatureDao();
		Signature signature = signatureDao.findRecentSignature(SignatureModule.TRIAL_SIGNATURE, trialId);
		if (signature == null) {
			ServiceException e = L10nUtil.initServiceException(ServiceExceptionCodes.NO_SIGNATURE);
			e.setLogError(false);
			throw e;
		}
		SignatureVO result = signatureDao.toSignatureVO(signature);
		result.setDescription(EntitySignature.getDescription(result));
		return result;
	}

	@Override
	protected TrialTagValueOutVO handleGetTrialTagValue(AuthenticationVO auth, Long trialTagValueId)
			throws Exception {
		TrialTagValueDao tagValueDao = this.getTrialTagValueDao();
		TrialTagValue tagValue = CheckIDUtil.checkTrialTagValueId(trialTagValueId, tagValueDao);
		TrialTagValueOutVO result = tagValueDao.toTrialTagValueOutVO(tagValue);
		return result;
	}

	@Override
	protected long handleGetTrialTagValueCount(AuthenticationVO auth, Long trialId)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getTrialTagValueDao().getCount(trialId, null, null);
	}

	@Override
	protected Collection<TrialTagValueOutVO> handleGetTrialTagValueList(
			AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		TrialTagValueDao tagValueDao = this.getTrialTagValueDao();
		Collection trialTagValues = tagValueDao.findByTrial(trialId, psf);
		tagValueDao.toTrialTagValueOutVOCollection(trialTagValues);
		return trialTagValues;
	}

	@Override
	protected VisitOutVO handleGetVisit(AuthenticationVO auth, Long visitId) throws Exception {
		VisitDao visitDao = this.getVisitDao();
		Visit visit = CheckIDUtil.checkVisitId(visitId, visitDao);
		VisitOutVO result = visitDao.toVisitOutVO(visit);
		return result;
	}

	@Override
	protected long handleGetVisitCount(AuthenticationVO auth, Long trialId)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		return this.getVisitDao().getCount(trialId);
	}

	@Override
	protected Collection<VisitOutVO> handleGetVisitList(AuthenticationVO auth, Long trialId, PSFVO psf)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		VisitDao visitDao = this.getVisitDao();
		Collection visits = visitDao.findByTrial(trialId, psf);
		visitDao.toVisitOutVOCollection(visits);
		return visits;
	}

	@Override
	protected VisitScheduleItemOutVO handleGetVisitScheduleItem(
			AuthenticationVO auth, Long visitScheduleItemId) throws Exception {
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		VisitScheduleItem visitScheduleItem = CheckIDUtil.checkVisitScheduleItemId(visitScheduleItemId, visitScheduleItemDao);
		VisitScheduleItemOutVO result = visitScheduleItemDao.toVisitScheduleItemOutVO(visitScheduleItem);
		return result;
	}

	@Override
	protected long handleGetVisitScheduleItemCount(
			AuthenticationVO auth, Long trialId, Long probandGroupId, Long visitId, Long probandId, boolean expand) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandGroupId != null) {
			CheckIDUtil.checkProbandGroupId(probandGroupId, this.getProbandGroupDao());
		}
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		return this.getVisitScheduleItemDao().getCount(trialId, probandGroupId, visitId, probandId, null, expand);
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> handleGetVisitScheduleItems(AuthenticationVO auth, Long trialId,
			Long departmentId, Date from, Date to, Long id, boolean sort)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		if (id != null) {
			CheckIDUtil.checkVisitScheduleItemId(id, visitScheduleItemDao);
		}
		Collection visitScheduleItems = visitScheduleItemDao.findByTrialDepartmentIntervalId(trialId, departmentId,
				CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to), id);
		visitScheduleItemDao.toVisitScheduleItemOutVOCollection(visitScheduleItems);
		if (sort) {
			visitScheduleItems = new ArrayList(visitScheduleItems);
			Collections.sort((ArrayList) visitScheduleItems, new VisitScheduleItemIntervalComparator(false));
		}
		return visitScheduleItems;
	}

	@Override
	protected Collection<VisitScheduleAppointmentVO> handleGetVisitScheduleItemInterval(AuthenticationVO auth, Long trialId,
			Long departmentId, Long statusId, Long visitTypeId, Date from, Date to, boolean sort) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (departmentId != null) {
			CheckIDUtil.checkDepartmentId(departmentId, this.getDepartmentDao());
		}
		if (statusId != null) {
			CheckIDUtil.checkTrialStatusTypeId(statusId, this.getTrialStatusTypeDao());
		}
		if (visitTypeId != null) {
			CheckIDUtil.checkVisitTypeId(visitTypeId, this.getVisitTypeDao());
		}
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		ProbandDao probandDao = this.getProbandDao();
		ArrayList<VisitScheduleAppointmentVO> result = new ArrayList<VisitScheduleAppointmentVO>();
		Iterator<Object[]> it = visitScheduleItemDao.findByTrialDepartmentStatusTypeInterval(trialId, departmentId, statusId, visitTypeId,
				CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to)).iterator();
		while (it.hasNext()) {
			Object[] visitScheduleItemProband = it.next();
			VisitScheduleAppointmentVO visitScheduleItemProbandVO = visitScheduleItemDao.toVisitScheduleAppointmentVO((VisitScheduleItem) visitScheduleItemProband[0]);
			visitScheduleItemProbandVO.setProband(probandDao.toProbandOutVO((Proband) visitScheduleItemProband[1]));
			result.add(visitScheduleItemProbandVO);
		}
		if (sort) {
			Collections.sort(result, new VisitScheduleAppointmentIntervalComparator(false));
		}
		return result;
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> handleGetVisitScheduleItemList(
			AuthenticationVO auth, Long trialId, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		Collection visitScheduleItems = visitScheduleItemDao.findByTrialSorted(trialId, sort, psf);
		visitScheduleItemDao.toVisitScheduleItemOutVOCollection(visitScheduleItems);
		return visitScheduleItems;
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> handleGetVisitScheduleItemList(
			AuthenticationVO auth, Long trialId, Long probandGroupId, Long visitId, Long probandId, boolean expand, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandGroupId != null) {
			CheckIDUtil.checkProbandGroupId(probandGroupId, this.getProbandGroupDao());
		}
		if (visitId != null) {
			CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		Collection visitScheduleItems = visitScheduleItemDao.findByTrialGroupVisitProbandTravel(trialId, probandGroupId, visitId, probandId, null, expand, psf);
		visitScheduleItemDao.toVisitScheduleItemOutVOCollection(visitScheduleItems);
		return visitScheduleItems;
	}

	@Override
	protected ECRFFieldOutVO handleMoveEcrfField(AuthenticationVO auth, Long ecrfFieldId, PositionMovement movement) throws Exception {
		return (new EcrfFieldMoveAdapter(this.getJournalEntryDao(), this.getECRFFieldDao(), this.getECRFDao(), this.getTrialDao())).move(ecrfFieldId, movement);
	}

	@Override
	protected ArrayList<ECRFFieldOutVO> handleMoveEcrfFieldTo(AuthenticationVO auth, Long ecrfFieldId, Long targetPosition)
			throws Exception {
		return (new EcrfFieldMoveAdapter(this.getJournalEntryDao(), this.getECRFFieldDao(), this.getECRFDao(), this.getTrialDao())).moveTo(ecrfFieldId, targetPosition);
	}

	@Override
	protected InquiryOutVO handleMoveInquiry(AuthenticationVO auth, Long inquiryId,
			PositionMovement movement) throws Exception {
		return (new InquiryMoveAdapter(this.getJournalEntryDao(), this.getInquiryDao(), this.getTrialDao())).move(inquiryId, movement);
	}

	@Override
	protected Collection<InquiryOutVO> handleMoveInquiryTo(
			AuthenticationVO auth, Long inquiryId, Long targetPosition)
			throws Exception {
		return (new InquiryMoveAdapter(this.getJournalEntryDao(), this.getInquiryDao(), this.getTrialDao())).moveTo(inquiryId, targetPosition);
	}

	@Override
	protected ProbandListEntryOutVO handleMoveProbandListEntry(
			AuthenticationVO auth, Long probandListEntryId, PositionMovement movement)
			throws Exception {
		return (new ProbandListEntryMoveAdapter(this.getJournalEntryDao(), this.getProbandListEntryDao(), this.getTrialDao())).move(probandListEntryId,
				movement);
	}

	@Override
	protected ProbandListEntryTagOutVO handleMoveProbandListEntryTag(
			AuthenticationVO auth, Long probandListEntryTagId, PositionMovement movement)
			throws Exception {
		return (new ProbandListEntryTagMoveAdapter(this.getJournalEntryDao(), this.getProbandListEntryTagDao(), this.getTrialDao())).move(probandListEntryTagId, movement);
	}

	@Override
	protected Collection<ProbandListEntryTagOutVO> handleMoveProbandListEntryTagTo(
			AuthenticationVO auth, Long probandListEntryTagId,
			Long targetPosition) throws Exception {
		return (new ProbandListEntryTagMoveAdapter(this.getJournalEntryDao(), this.getProbandListEntryTagDao(), this.getTrialDao())).moveTo(probandListEntryTagId, targetPosition);
	}

	@Override
	protected Collection<ProbandListEntryOutVO> handleMoveProbandListEntryTo(
			AuthenticationVO auth, Long probandListEntryId, Long targetPosition)
			throws Exception {
		return (new ProbandListEntryMoveAdapter(this.getJournalEntryDao(), this.getProbandListEntryDao(), this.getTrialDao())).moveTo(probandListEntryId,
				targetPosition);
	}

	@Override
	protected Collection<ECRFFieldOutVO> handleNormalizeEcrfFieldPositions(
			AuthenticationVO auth, Long groupEcrfFieldId) throws Exception {
		return (new EcrfFieldMoveAdapter(this.getJournalEntryDao(), this.getECRFFieldDao(), this.getECRFDao(), this.getTrialDao())).normalizePositions(groupEcrfFieldId);
	}

	@Override
	protected Collection<InquiryOutVO> handleNormalizeInquiryPositions(
			AuthenticationVO auth, Long groupInquiryId) throws Exception {
		return (new InquiryMoveAdapter(this.getJournalEntryDao(), this.getInquiryDao(), this.getTrialDao())).normalizePositions(groupInquiryId);
	}

	@Override
	protected Collection<ProbandListEntryOutVO> handleNormalizeProbandListEntryPositions(
			AuthenticationVO auth, Long trialId) throws Exception {
		return (new ProbandListEntryMoveAdapter(this.getJournalEntryDao(), this.getProbandListEntryDao(), this.getTrialDao())).normalizePositions(trialId);
	}

	@Override
	protected Collection<ProbandListEntryTagOutVO> handleNormalizeProbandListEntryTagPositions(
			AuthenticationVO auth, Long trialId) throws Exception {
		return (new ProbandListEntryTagMoveAdapter(this.getJournalEntryDao(), this.getProbandListEntryTagDao(), this.getTrialDao())).normalizePositions(trialId);
	}

	@Override
	protected ECRFPDFVO handleRenderEcrf(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, String section, boolean blank) throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntry);
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
		ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(ecrf);
		VisitOutVO visitVO = null;
		if (visitId != null) {
			visitVO = this.getVisitDao().toVisitOutVO(CheckIDUtil.checkVisitId(visitId, this.getVisitDao()));
		}
		LinkedHashSet<ProbandListEntryOutVO> listEntryVOs = new LinkedHashSet<ProbandListEntryOutVO>();
		TreeSet<ECRFOutVO> ecrfVOs = new TreeSet<ECRFOutVO>(new EcrfOutVONameComparator());
		TreeSet<VisitOutVO> visitVOs = new TreeSet<VisitOutVO>(new VisitOutVOTokenComparator());
		HashMap<Long, HashMap<Long, HashMap<Long, Collection<ECRFFieldValueOutVO>>>> valueVOMap = new HashMap<Long, HashMap<Long, HashMap<Long, Collection<ECRFFieldValueOutVO>>>>();
		HashMap<Long, HashMap<Long, HashMap<Long, HashMap<Long, Collection>>>> logVOMap = new HashMap<Long, HashMap<Long, HashMap<Long, HashMap<Long, Collection>>>>();
		HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> listEntryTagValuesVOMap = new HashMap<Long, Collection<ProbandListEntryTagValueOutVO>>();
		HashMap<Long, HashMap<Long, HashMap<Long, ECRFStatusEntryVO>>> statusEntryVOMap = new HashMap<Long, HashMap<Long, HashMap<Long, ECRFStatusEntryVO>>>();
		HashMap<Long, SignatureVO> signatureVOMap = new HashMap<Long, SignatureVO>();
		HashMap<Long, InputFieldImageVO> imageVOMap = new HashMap<Long, InputFieldImageVO>();
		ServiceUtil.populateEcrfPDFVOMaps(listEntryVO, ecrfVO, visitVO, blank,
				getEcrfFieldValues(listEntryVO, ecrfVO, visitVO, section, null, blank, false, false, null, null).getPageValues(),
				listEntryVOs, ecrfVOs, visitVOs, valueVOMap, logVOMap, listEntryTagValuesVOMap, statusEntryVOMap, signatureVOMap, imageVOMap,
				this.getInputFieldDao(),
				this.getECRFFieldValueDao(),
				this.getECRFStatusEntryDao(),
				this.getECRFFieldStatusEntryDao(),
				this.getProbandListEntryTagDao(),
				this.getProbandListEntryTagValueDao(),
				this.getSignatureDao());
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		EcrfPDFPainter painter = PDFPainterFactory.createEcrfPDFPainter();
		painter.setListEntryVOs(listEntryVOs);
		painter.setEcrfVOs(ecrfVOs);
		painter.setVisitVOs(visitVOs);
		painter.setValueVOMap(valueVOMap);
		painter.setLogVOMap(logVOMap);
		painter.setListEntryTagValuesVOMap(listEntryTagValuesVOMap);
		painter.setStatusEntryVOMap(statusEntryVOMap);
		painter.setSignatureVOMap(signatureVOMap);
		painter.setImageVOMap(imageVOMap);
		painter.setBlank(blank);
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		ECRFPDFVO result = painter.getPdfVO();
		ServiceUtil.logSystemMessage(listEntry.getTrial(), listEntryVO.getProband(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
				SystemMessageCodes.ECRF_PDF_RENDERED,
				result, null, journalEntryDao);
		ServiceUtil.logSystemMessage(listEntry.getProband(), listEntryVO.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
				SystemMessageCodes.ECRF_PDF_RENDERED,
				result, null,
				journalEntryDao);
		return result;
	}

	@Override
	protected ECRFPDFVO handleRenderEcrfs(AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId, Long visitId, boolean blank) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = null;
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		}
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = null;
		ProbandListEntryOutVO listEntryVO;
		if (probandListEntryId != null) {
			listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
		}
		ECRF ecrf = null;
		Visit visit = null;
		if (ecrfId != null) {
			ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			if (visitId != null) {
				visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
			checkEcrfVisit(ecrf, visit);
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_ID_NOT_NULL);
			}
		}
		LinkedHashSet<ProbandListEntryOutVO> listEntryVOs = new LinkedHashSet<ProbandListEntryOutVO>();
		ECRFPDFVO result = ServiceUtil.renderEcrfs(listEntry, trial, ecrf, visit, blank, listEntryVOs,
				this.getProbandListEntryDao(), this.getECRFDao(), this.getVisitDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getInputFieldDao(),
				this.getInputFieldSelectionSetValueDao(), this.getECRFStatusEntryDao(), this.getECRFFieldStatusEntryDao(), this.getECRFFieldStatusTypeDao(),
				this.getProbandListEntryTagDao(), this.getProbandListEntryTagValueDao(), this.getSignatureDao(), this.getUserDao());
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (listEntry != null) {
			listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntry);
			ServiceUtil.logSystemMessage(listEntry.getTrial(), listEntryVO.getProband(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
					ecrf != null ? SystemMessageCodes.ECRF_PDF_RENDERED : SystemMessageCodes.ECRFS_PDF_RENDERED,
					result, null, journalEntryDao);
			ServiceUtil.logSystemMessage(listEntry.getProband(), listEntryVO.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
					ecrf != null ? SystemMessageCodes.ECRF_PDF_RENDERED : SystemMessageCodes.ECRFS_PDF_RENDERED,
					result, null,
					journalEntryDao);
		} else {
			Iterator<ProbandListEntryOutVO> it = listEntryVOs.iterator();
			ProbandDao probandDao = this.getProbandDao();
			while (it.hasNext()) {
				listEntryVO = it.next();
				ServiceUtil.logSystemMessage(trialDao.load(listEntryVO.getTrial().getId()), listEntryVO.getProband(), CommonUtil.dateToTimestamp(result.getContentTimestamp()),
						CoreUtil.getUser(),
						ecrf != null ? SystemMessageCodes.ECRF_PDF_RENDERED : SystemMessageCodes.ECRFS_PDF_RENDERED,
						result, null, journalEntryDao);
				ServiceUtil.logSystemMessage(probandDao.load(listEntryVO.getProband().getId()), listEntryVO.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()),
						CoreUtil.getUser(),
						ecrf != null ? SystemMessageCodes.ECRF_PDF_RENDERED : SystemMessageCodes.ECRFS_PDF_RENDERED,
						result, null,
						journalEntryDao);
			}
		}
		return result;
	}

	@Override
	protected ProbandListEntryTagsPDFVO handleRenderProbandListEntryTags(AuthenticationVO auth, Long trialId, Long probandId, boolean blank) throws Exception {
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandId, probandDao);
		ProbandOutVO probandVO = probandDao.toProbandOutVO(proband);
		TrialDao trialDao = this.getTrialDao();
		Trial trial = null;
		TrialOutVO trialVO = null;
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
			trialVO = trialDao.toTrialOutVO(trial);
		}
		ProbandListEntryTagsPDFVO result = ServiceUtil.renderProbandListEntryTags(proband, trial, blank,
				this.getProbandListEntryDao(), this.getProbandListEntryTagDao(), this.getProbandListEntryTagValueDao(),
				this.getInputFieldDao(), this.getInputFieldSelectionSetValueDao(), this.getUserDao());
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (trial != null) {
			ServiceUtil.logSystemMessage(trial, probandVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
					SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_PDF_RENDERED,
					result, null, journalEntryDao);
		}
		ServiceUtil.logSystemMessage(proband, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
				trial != null ? SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_PDF_RENDERED
						: SystemMessageCodes.PROBAND_LIST_ENTRY_TAGS_PDF_RENDERED,
				result, null,
				journalEntryDao);
		return result;
	}

	@Override
	protected ReimbursementsPDFVO handleRenderReimbursements(
			AuthenticationVO auth, Long trialId, Long probandId, PaymentMethod method, Boolean paid)
			throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = null;
		TrialOutVO trialVO = null;
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
			trialVO = trialDao.toTrialOutVO(trial);
		}
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = null;
		ProbandOutVO probandVO = null;
		if (probandId != null) {
			proband = CheckIDUtil.checkProbandId(probandId, probandDao);
			if (!proband.isPerson()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.MONEY_TRANSFER_PROBAND_NOT_PERSON);
			}
			probandVO = probandDao.toProbandOutVO(proband);
		}
		ReimbursementsPDFVO result = ServiceUtil.renderReimbursements(proband, trial, method, paid,
				this.getProbandDao(), this.getTrialDao(), this.getMoneyTransferDao(), this.getTrialTagValueDao(),
				this.getBankAccountDao(), this.getProbandAddressDao(), this.getUserDao());
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (trialVO != null) {
			if (probandVO != null) {
				ServiceUtil.logSystemMessage(trial, probandVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.REIMBURSEMENTS_PDF_RENDERED,
						result, null, journalEntryDao);
				ServiceUtil.logSystemMessage(proband, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.REIMBURSEMENTS_PDF_RENDERED,
						result, null,
						journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(trial, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.REIMBURSEMENTS_PDF_RENDERED,
						result, null, journalEntryDao);
			}
		} else if (probandVO != null) {
			ServiceUtil.logSystemMessage(proband, (TrialOutVO) null, CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
					SystemMessageCodes.REIMBURSEMENTS_PDF_NO_TRIAL_RENDERED, result, null,
					journalEntryDao);
		}
		return result;
	}

	@Override
	protected ECRFFieldValuesOutVO handleSetEcrfFieldValues(AuthenticationVO auth, Set<ECRFFieldValueInVO> ecrfFieldValuesIns, Boolean addSeries, boolean force, String fieldQuery,
			PSFVO psf)
			throws Exception {
		boolean loadPageResult = addSeries != null && psf != null;
		Object[] resultItems = setEcrfFieldValues(ecrfFieldValuesIns, loadPageResult, force);
		ECRFFieldValuesOutVO result;
		if (loadPageResult) {
			result = ServiceUtil.getEcrfFieldValues((ProbandListEntryOutVO) resultItems[1], (ECRFOutVO) resultItems[2], (VisitOutVO) resultItems[3], addSeries, false, false,
					fieldQuery,
					psf,
					this.getECRFFieldDao(),
					this.getECRFFieldValueDao(), this.getInputFieldSelectionSetValueDao(), this.getECRFFieldStatusEntryDao(),
					this.getECRFFieldStatusTypeDao());
			result.setJsValues(((ECRFFieldValuesOutVO) resultItems[0]).getJsValues());
		} else {
			result = (ECRFFieldValuesOutVO) resultItems[0];
		}
		return result;
	}

	@Override
	protected ECRFFieldValuesOutVO handleSetEcrfFieldValues(AuthenticationVO auth, Set<ECRFFieldValueInVO> ecrfFieldValuesIns, String section, Long index,
			Boolean addSeries, boolean force, String fieldQuery, PSFVO psf)
			throws Exception {
		boolean loadPageResult = addSeries != null && psf != null;
		Object[] resultItems = setEcrfFieldValues(ecrfFieldValuesIns, loadPageResult, force);
		ECRFFieldValuesOutVO result;
		if (loadPageResult) {
			result = getEcrfFieldValues((ProbandListEntryOutVO) resultItems[1], (ECRFOutVO) resultItems[2], (VisitOutVO) resultItems[3], section, index, addSeries, false, false,
					fieldQuery,
					psf);
			result.setJsValues(((ECRFFieldValuesOutVO) resultItems[0]).getJsValues());
		} else {
			return (ECRFFieldValuesOutVO) resultItems[0];
		}
		return result;
	}

	@Override
	protected ECRFStatusEntryVO handleSetEcrfStatusEntry(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long visitId, Long version, Long ecrfStatusTypeId,
			Long probandListStatusTypeId)
			throws Exception {
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao(), LockMode.PESSIMISTIC_WRITE); // lock order, field updates
		Visit visit = null;
		if (visitId != null) {
			visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
		}
		ECRFStatusType statusType = CheckIDUtil.checkEcrfStatusTypeId(ecrfStatusTypeId, this.getECRFStatusTypeDao());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ECRFStatusEntry originalStatusEntry = this.getECRFStatusEntryDao().findByListEntryEcrfVisit(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null);
		ECRFStatusEntryVO result;
		if (originalStatusEntry != null) {
			result = updateEcrfStatusEntry(originalStatusEntry, statusType, version, probandListStatusTypeId, now, user);
		} else {
			Object[] resultItems = addEcrfStatusEntry(listEntry, ecrf, visit, statusType, probandListStatusTypeId, now, user);
			result = (ECRFStatusEntryVO) resultItems[1];
		}
		return result;
	}

	@Override
	protected ProbandListEntryTagValuesOutVO handleSetProbandListEntryTagValues(
			AuthenticationVO auth, Set<ProbandListEntryTagValueInVO> probandListEntryTagValuesIn, boolean force)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ProbandListEntryTagValuesOutVO result = new ProbandListEntryTagValuesOutVO();
		ServiceException firstException = null;
		HashMap<Long, String> errorMessagesMap = new HashMap<Long, String>();
		ProbandListEntry listEntry = null;
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		if (probandListEntryTagValuesIn != null && probandListEntryTagValuesIn.size() > 0) {
			Trial trial = null;
			ArrayList<ProbandListEntryTagValueOutVO> tagValues = new ArrayList<ProbandListEntryTagValueOutVO>(probandListEntryTagValuesIn.size());
			ArrayList<ProbandListEntryTagValueJsonVO> jsTagValues = null;
			if (Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
					DefaultSettings.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				jsTagValues = new ArrayList<ProbandListEntryTagValueJsonVO>(probandListEntryTagValuesIn.size());
			}
			Iterator<ProbandListEntryTagValueInVO> probandListEntryTagValuesInIt = probandListEntryTagValuesIn.iterator();
			while (probandListEntryTagValuesInIt.hasNext()) {
				ProbandListEntryTagValueInVO probandListEntryTagValueIn = probandListEntryTagValuesInIt.next();
				ProbandListEntryTag listEntryTag = CheckIDUtil.checkProbandListEntryTagId(probandListEntryTagValueIn.getTagId(), probandListEntryTagDao);
				if (trial == null) {
					trial = listEntryTag.getTrial();
					ServiceUtil.checkTrialLocked(trial);
				} else if (!trial.equals(listEntryTag.getTrial())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_VALUES_FOR_DIFFERENT_TRIALS);
				}
				if (listEntry == null) {
					listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryTagValueIn.getListEntryId(), probandListEntryDao, LockMode.PESSIMISTIC_WRITE);
					if (!trial.equals(listEntry.getTrial())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_VALUES_FOR_WRONG_TRIAL);
					}
					ServiceUtil.checkProbandLocked(listEntry.getProband());
				} else if (!listEntry.getId().equals(probandListEntryTagValueIn.getListEntryId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_VALUES_FOR_DIFFERENT_LIST_ENTRIES);
				}
				try {
					addUpdateProbandListEntryTagValue(probandListEntryTagValueIn, listEntry, listEntryTag, now, user, force,
							Settings.getBoolean(SettingCodes.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_TRIAL, Bundle.SETTINGS, DefaultSettings.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_TRIAL),
							Settings.getBoolean(SettingCodes.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_PROBAND, Bundle.SETTINGS, DefaultSettings.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_PROBAND),
							tagValues, jsTagValues);
				} catch (ServiceException e) {
					if (firstException == null) {
						firstException = e;
					}
					errorMessagesMap.put(listEntryTag.getId(), e.getMessage());
				}
			}
			if (firstException != null) {
				firstException.setData(errorMessagesMap);
				throw firstException;
			}
			Collections.sort(tagValues, new ProbandListEntryTagValueOutVOComparator());
			result.setPageValues(tagValues);
			if (jsTagValues != null) {
				result.setJsValues(jsTagValues);
			}
		}
		return result;
	}

	@Override
	protected TimelineEventOutVO handleSetTimelineEventDismissed(
			AuthenticationVO auth, Long timelineEventId, Long version, boolean dismissed, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth) throws Exception {
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent timelineEvent = CheckIDUtil.checkTimelineEventId(timelineEventId, timelineEventDao);
		TimelineEventOutVO original = timelineEventDao.toTimelineEventOutVO(timelineEvent, maxInstances, maxParentDepth, maxChildrenDepth);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(timelineEvent, version.longValue(), now, user);
		timelineEvent.setDismissed(dismissed);
		timelineEvent.setDismissedTimestamp(now);
		timelineEventDao.update(timelineEvent);
		notifyTimelineEventReminder(timelineEvent, now);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent, maxInstances, maxParentDepth, maxChildrenDepth);
		ServiceUtil.logSystemMessage(timelineEvent.getTrial(), result.getTrial(), now, user, dismissed ? SystemMessageCodes.TIMELINE_EVENT_DISMISSED_SET
				: SystemMessageCodes.TIMELINE_EVENT_DISMISSED_UNSET, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<ECRFStatusEntryVO> handleSignVerifiedEcrfs(AuthenticationVO auth, Long trialId, Long probandListEntryId, boolean signAll) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao(), LockMode.PESSIMISTIC_WRITE);
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<ECRFStatusEntryVO> results = new ArrayList<ECRFStatusEntryVO>();
		Iterator<ECRFStatusEntry> statusEntryIt = this.getECRFStatusEntryDao()
				.findByTrialListEntryDoneValidatedReviewVerified(trialId, probandListEntryId, null, null, null, signAll ? null : true, null).iterator();
		while (statusEntryIt.hasNext()) {
			ECRFStatusEntry statusEntry = statusEntryIt.next();
			Iterator<ECRFStatusType> it = statusEntry.getStatus().getTransitions().iterator();
			ECRFStatusType newStatus = null;
			while (it.hasNext()) {
				ECRFStatusType status = it.next();
				if ((signAll || !statusEntry.getStatus().equals(status)) && hasEcrfStatusAction(status, org.phoenixctms.ctsms.enumeration.ECRFStatusAction.SIGN_ECRF)) {
					newStatus = status;
					break;
				}
			}
			if (newStatus != null) {
				checkTeamMemberSign(statusEntry.getListEntry().getTrial(), user);
				ECRFStatusEntryVO result = updateEcrfStatusEntry(statusEntry, newStatus,
						statusEntry.getVersion(), null, now,
						user);
				results.add(result);
			}
		}
		return results;
	}

	@Override
	protected ECRFOutVO handleUpdateEcrf(AuthenticationVO auth, ECRFInVO modifiedEcrf) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		SignatureDao signatureDao = this.getSignatureDao();
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		NotificationDao notificationDao = this.getNotificationDao();
		NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
		ECRF originalEcrf = CheckIDUtil.checkEcrfId(modifiedEcrf.getId(), ecrfDao, LockMode.PESSIMISTIC_WRITE);
		Iterator<ECRFStatusEntry> ecrfStatusEntriesIt = checkUpdateEcrfInput(modifiedEcrf, originalEcrf).iterator();
		ECRFOutVO original = ecrfDao.toECRFOutVO(originalEcrf);
		ecrfDao.evict(originalEcrf);
		ECRF ecrf = ecrfDao.eCRFInVOToEntity(modifiedEcrf);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		while (ecrfStatusEntriesIt.hasNext()) {
			ECRFStatusEntry ecrfStatusEntry = ecrfStatusEntriesIt.next();
			ProbandListEntry listEntry = ecrfStatusEntry.getListEntry();
			listEntry.removeEcrfStatusEntries(ecrfStatusEntry);
			ecrf.removeEcrfStatusEntries(ecrfStatusEntry);
			ServiceUtil.removeEcrfStatusEntry(ecrfStatusEntry, true,
					signatureDao, ecrfStatusEntryDao, notificationDao, notificationRecipientDao);
		}
		CoreUtil.modifyVersion(originalEcrf, ecrf, now, user);
		ecrfDao.update(ecrf);
		ECRFOutVO result = ecrfDao.toECRFOutVO(ecrf);
		ServiceUtil.logSystemMessage(ecrf.getTrial(), result.getTrial(), now, user, SystemMessageCodes.ECRF_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ECRFFieldOutVO handleUpdateEcrfField(AuthenticationVO auth, ECRFFieldInVO modifiedEcrfField) throws Exception {
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		ECRFField originalEcrfField = CheckIDUtil.checkEcrfFieldId(modifiedEcrfField.getId(), ecrfFieldDao);
		checkUpdateEcrfFieldInput(originalEcrfField, modifiedEcrfField); // access original associations before evict
		ECRFFieldOutVO original = ecrfFieldDao.toECRFFieldOutVO(originalEcrfField);
		ecrfFieldDao.evict(originalEcrfField);
		ECRFField ecrfField = ecrfFieldDao.eCRFFieldInVOToEntity(modifiedEcrfField);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalEcrfField, ecrfField, now, user);
		ecrfFieldDao.update(ecrfField);
		ECRFFieldOutVO result = ecrfFieldDao.toECRFFieldOutVO(ecrfField);
		ServiceUtil.logSystemMessage(ecrfField.getTrial(), result.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<ECRFFieldOutVO> handleUpdateEcrfFieldSections(AuthenticationVO auth, Long ecrfId, String oldSection, String newSection) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao, LockMode.PESSIMISTIC_WRITE);
		ServiceUtil.checkTrialLocked(ecrf.getTrial());
		ServiceUtil.checkLockedEcrfs(ecrf, this.getECRFStatusEntryDao(), this.getECRFDao());
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		Collection<ECRFField> ecrfFields = ecrfFieldDao.findByEcrfSectionPosition(ecrfId, oldSection, null);
		ArrayList<ECRFFieldOutVO> result;
		if (ecrfFields.size() > 0) {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			User user = CoreUtil.getUser();
			long newSectionExistingFieldCount = ecrfFieldDao.getCount(ecrfId, newSection, null);
			result = new ArrayList<ECRFFieldOutVO>(ecrfFields.size());
			JournalEntryDao journalEntryDao = this.getJournalEntryDao();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
			Iterator<ECRFField> ecrfFieldsIt = ecrfFields.iterator();
			while (ecrfFieldsIt.hasNext()) {
				ECRFField originalEcrfField = ecrfFieldsIt.next();
				ECRFFieldInVO ecrfFieldIn = new ECRFFieldInVO();
				ecrfFieldIn.setId(originalEcrfField.getId());
				ecrfFieldIn.setEcrfId(ecrfId);
				ecrfFieldIn.setPosition(originalEcrfField.getPosition());
				ecrfFieldIn.setSection(newSection);
				ecrfFieldIn.setSeries(originalEcrfField.isSeries());
				boolean sectionChanged = ecrfFieldDao.getCount(ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection(), ecrfFieldIn.getId()) == 0l;
				if (sectionChanged) {
					if (newSectionExistingFieldCount > 0) {
						if ((new EcrfFieldSeriesCollisionFinder(ecrfDao, ecrfFieldDao)).collides(ecrfFieldIn)) {
							// all fields within section must have same "series" flag
							throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_FLAG_INCONSISTENT);
						}
						if ((new EcrfFieldPositionCollisionFinder(ecrfDao, ecrfFieldDao)).collides(ecrfFieldIn)) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_POSITION_NOT_UNIQUE);
						}
						if (ecrfFieldIn.getSeries()) {
							long valueCount = ecrfFieldValueDao.getCount(ecrfFieldIn.getId(), false);
							if (valueCount > 0) {
								throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_WITH_VALUES_SECTION_CHANGED);
							}
							long statusEntryCount = ecrfFieldStatusEntryDao.getCount(ecrfFieldIn.getId(), false);
							if (statusEntryCount > 0) {
								throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_WITH_STATUS_ENTRIES_SECTION_CHANGED);
							}
							if (ecrfFieldValueDao.getCount(ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection()) > 0) {
								throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_SECTION_WITH_VALUES, ecrfFieldIn.getSection());
							}
							if (ecrfFieldStatusEntryDao.getCount(ecrfFieldIn.getEcrfId(), ecrfFieldIn.getSection()) > 0) {
								throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_SECTION_WITH_STATUS_ENTRIES, ecrfFieldIn.getSection());
							}
						}
					}
					ECRFFieldOutVO original = ecrfFieldDao.toECRFFieldOutVO(originalEcrfField);
					ecrfFieldDao.evict(originalEcrfField);
					ECRFField ecrfField = ecrfFieldDao.load(originalEcrfField.getId());
					ecrfField.setSection(newSection);
					CoreUtil.modifyVersion(originalEcrfField, ecrfField, now, user);
					ecrfFieldDao.update(ecrfField);
					ECRFFieldOutVO modified = ecrfFieldDao.toECRFFieldOutVO(ecrfField);
					ServiceUtil.logSystemMessage(ecrfField.getTrial(), modified.getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_UPDATED, modified, original, journalEntryDao);
					result.add(modified);
				}
			}
		} else {
			result = new ArrayList<ECRFFieldOutVO>();
		}
		return result;
	}

	@Override
	protected InquiryOutVO handleUpdateInquiry(AuthenticationVO auth, InquiryInVO modifiedInquiry)
			throws Exception {
		InquiryDao inquiryDao = this.getInquiryDao();
		Inquiry originalInquiry = CheckIDUtil.checkInquiryId(modifiedInquiry.getId(), inquiryDao);
		checkUpdateInquiryInput(originalInquiry, modifiedInquiry); // access original associations before evict
		InquiryOutVO original = inquiryDao.toInquiryOutVO(originalInquiry);
		inquiryDao.evict(originalInquiry);
		Inquiry inquiry = inquiryDao.inquiryInVOToEntity(modifiedInquiry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalInquiry, inquiry, now, user);
		inquiryDao.update(inquiry);
		InquiryOutVO result = inquiryDao.toInquiryOutVO(inquiry);
		ServiceUtil.logSystemMessage(inquiry.getTrial(), result.getTrial(), now, user, SystemMessageCodes.INQUIRY_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandGroupOutVO handleUpdateProbandGroup(
			AuthenticationVO auth, ProbandGroupInVO modifiedProbandGroup) throws Exception {
		ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
		ProbandGroup originalProbandGroup = CheckIDUtil.checkProbandGroupId(modifiedProbandGroup.getId(), probandGroupDao);
		checkProbandGroupInput(modifiedProbandGroup);
		if (!modifiedProbandGroup.getTrialId().equals(originalProbandGroup.getTrial().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GROUP_TRIAL_CHANGED);
		}
		ProbandGroupOutVO original = probandGroupDao.toProbandGroupOutVO(originalProbandGroup);
		probandGroupDao.evict(originalProbandGroup);
		ProbandGroup probandGroup = probandGroupDao.probandGroupInVOToEntity(modifiedProbandGroup);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalProbandGroup, probandGroup, now, user);
		probandGroupDao.update(probandGroup);
		ProbandGroupOutVO result = probandGroupDao.toProbandGroupOutVO(probandGroup);
		ServiceUtil.logSystemMessage(probandGroup.getTrial(), result.getTrial(), now, user, SystemMessageCodes.PROBAND_GROUP_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected ProbandListEntryOutVO handleUpdateProbandListEntry(
			AuthenticationVO auth, ProbandListEntryInVO modifiedProbandListEntry, Long probandListStatusTypeId, boolean randomize) throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry originalProbandListEntry = CheckIDUtil.checkProbandListEntryId(modifiedProbandListEntry.getId(), probandListEntryDao);
		ProbandListStatusEntry lastStatus = originalProbandListEntry.getLastStatus();
		checkProbandListEntryInput(modifiedProbandListEntry, false, false, lastStatus == null ? null : lastStatus.getRealTimestamp()); // access original associations before evict
		if (!modifiedProbandListEntry.getTrialId().equals(originalProbandListEntry.getTrial().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TRIAL_CHANGED);
		}
		boolean probandChanged = !modifiedProbandListEntry.getProbandId().equals(originalProbandListEntry.getProband().getId());
		boolean groupChanged;
		Trial trial = CheckIDUtil.checkTrialId(modifiedProbandListEntry.getTrialId(), this.getTrialDao());
		Randomization randomization = getRandomization(trial, randomize, modifiedProbandListEntry.getGroupId());
		ProbandGroup group;
		boolean randomized = false;
		if (randomization != null && RandomizationType.GROUP.equals(randomization.getType())) {
			group = randomization.getRandomizedProbandGroup(trial, originalProbandListEntry);
			randomized = true;
		} else {
			group = (modifiedProbandListEntry.getGroupId() != null ? CheckIDUtil.checkProbandGroupId(modifiedProbandListEntry.getGroupId(), this.getProbandGroupDao()) : null);
		}
		if (group != null && originalProbandListEntry.getGroup() != null) {
			groupChanged = !group.equals(originalProbandListEntry.getGroup());
		} else if (group == null && originalProbandListEntry.getGroup() != null) {
			groupChanged = true;
		} else if (group != null && originalProbandListEntry.getGroup() == null) {
			groupChanged = true;
		} else {
			groupChanged = false;
		}
		ProbandListEntryOutVO original = probandListEntryDao.toProbandListEntryOutVO(originalProbandListEntry);
		probandListEntryDao.evict(originalProbandListEntry);
		ProbandListEntry probandListEntry = probandListEntryDao.probandListEntryInVOToEntity(modifiedProbandListEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		if (randomization != null) {
			if (RandomizationType.GROUP.equals(randomization.getType())) {
				if (probandListEntry.getGroup() != null) {
					probandListEntry.getGroup().removeProbandListEntries(probandListEntry);
				}
				probandListEntry.setGroup(group);
				group.addProbandListEntries(probandListEntry);
			}
		}
		CoreUtil.modifyVersion(originalProbandListEntry, probandListEntry, now, user);
		probandListEntryDao.update(probandListEntry);
		if (randomization != null) {
			if (!RandomizationType.GROUP.equals(randomization.getType())) {
				applyListEntryTagRandomization(trial, probandListEntry, originalProbandListEntry, randomization, now, user);
				randomized = true;
			}
		}
		if (probandChanged || groupChanged || randomized) {
			addProbandListEntryUpdatedProbandListStatusEntry(
					randomized ? ProbandListStatusReasonCodes.LIST_ENTRY_RANDOMIZED_AND_UPDATED : ProbandListStatusReasonCodes.LIST_ENTRY_UPDATED,
					randomized ? ProbandListStatusReasonCodes.LIST_ENTRY_RANDOMIZED_AND_UPDATED_NO_GROUP : ProbandListStatusReasonCodes.LIST_ENTRY_UPDATED_NO_GROUP,
					probandListEntry, probandListStatusTypeId, now, user);
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ProbandListEntryOutVO result = probandListEntryDao.toProbandListEntryOutVO(probandListEntry);
		if (randomization != null) {
			logSystemMessage(probandListEntry.getProband(), result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_RANDOMIZED_AND_UPDATED, result,
					original,
					randomization.getRandomizationInfoVO(), journalEntryDao);
			logSystemMessage(probandListEntry.getTrial(), result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_RANDOMIZED_AND_UPDATED, result,
					original,
					randomization.getRandomizationInfoVO(), journalEntryDao);
		} else {
			ServiceUtil.logSystemMessage(probandListEntry.getProband(), result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_UPDATED, result, original,
					journalEntryDao);
			ServiceUtil.logSystemMessage(probandListEntry.getTrial(), result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_UPDATED, result, original,
					journalEntryDao);
		}
		return result;
	}

	@Override
	protected ProbandListEntryTagOutVO handleUpdateProbandListEntryTag(
			AuthenticationVO auth, ProbandListEntryTagInVO modifiedProbandListEntryTag)
			throws Exception {
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		ProbandListEntryTag originalProbandListEntryTag = CheckIDUtil.checkProbandListEntryTagId(modifiedProbandListEntryTag.getId(), probandListEntryTagDao);
		checkUpdateProbandListEntryTagInput(originalProbandListEntryTag, modifiedProbandListEntryTag); // access original associations before evict
		ProbandListEntryTagOutVO original = probandListEntryTagDao.toProbandListEntryTagOutVO(originalProbandListEntryTag);
		probandListEntryTagDao.evict(originalProbandListEntryTag);
		ProbandListEntryTag probandListEntryTag = probandListEntryTagDao.probandListEntryTagInVOToEntity(modifiedProbandListEntryTag);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalProbandListEntryTag, probandListEntryTag, now, user);
		probandListEntryTagDao.update(probandListEntryTag);
		ProbandListEntryTagOutVO result = probandListEntryTagDao.toProbandListEntryTagOutVO(probandListEntryTag);
		ServiceUtil.logSystemMessage(probandListEntryTag.getTrial(), result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StratificationRandomizationListOutVO handleUpdateStratificationRandomizationList(AuthenticationVO auth,
			StratificationRandomizationListInVO modifiedStratificationRandomizationList, RandomizationListCodesVO modifiedCodes, boolean purgeOldCodes)
			throws Exception {
		StratificationRandomizationListDao stratificationRandomizationListDao = this.getStratificationRandomizationListDao();
		StratificationRandomizationList originalRandomizationList = CheckIDUtil.checkStratificationRandomizationListId(modifiedStratificationRandomizationList.getId(),
				stratificationRandomizationListDao);
		ArrayList<RandomizationListCodeInVO> codes = checkStratificationRandomizationListInput(modifiedStratificationRandomizationList,
				modifiedCodes != null ? modifiedCodes.getCodes() : null, true);
		StratificationRandomizationListOutVO original = stratificationRandomizationListDao.toStratificationRandomizationListOutVO(originalRandomizationList);
		stratificationRandomizationListDao.evict(originalRandomizationList);
		StratificationRandomizationList randomizationList = stratificationRandomizationListDao.stratificationRandomizationListInVOToEntity(modifiedStratificationRandomizationList);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalRandomizationList, randomizationList, now, user);
		stratificationRandomizationListDao.update(randomizationList);
		ServiceUtil.createRandomizationListCodes(randomizationList, codes, purgeOldCodes, now, user, this.getRandomizationListCodeDao(), this.getRandomizationListCodeValueDao());
		StratificationRandomizationListOutVO result = stratificationRandomizationListDao.toStratificationRandomizationListOutVO(randomizationList);
		ServiceUtil.logSystemMessage(randomizationList.getTrial(), result.getTrial(), now, user, SystemMessageCodes.STRATIFICATION_RANDOMIZATION_LIST_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected StratificationRandomizationListOutVO handleAddUpdateStratificationRandomizationList(AuthenticationVO auth,
			StratificationRandomizationListInVO modifiedStratificationRandomizationList, RandomizationListCodesVO modifiedCodes, boolean purgeOldCodes)
			throws Exception {
		StratificationRandomizationListDao stratificationRandomizationListDao = this.getStratificationRandomizationListDao();
		if (modifiedStratificationRandomizationList.getId() != null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.STRATIFICATION_RANDOMIZATION_LIST_ID_NOT_NULL);
		}
		ArrayList<RandomizationListCodeInVO> codes = checkStratificationRandomizationListInput(modifiedStratificationRandomizationList,
				modifiedCodes != null ? modifiedCodes.getCodes() : null, false);
		StratificationRandomizationList originalRandomizationList;
		try {
			originalRandomizationList = stratificationRandomizationListDao.findByTrialTagValues(modifiedStratificationRandomizationList.getTrialId(),
					new HashSet<Long>(modifiedStratificationRandomizationList.getSelectionSetValueIds())).iterator().next();
		} catch (NoSuchElementException e) {
			originalRandomizationList = null;
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		StratificationRandomizationListOutVO result;
		if (originalRandomizationList != null) {
			StratificationRandomizationListOutVO original = stratificationRandomizationListDao.toStratificationRandomizationListOutVO(originalRandomizationList);
			stratificationRandomizationListDao.evict(originalRandomizationList);
			StratificationRandomizationListInVO randomizationListIn = new StratificationRandomizationListInVO(modifiedStratificationRandomizationList);
			randomizationListIn.setId(original.getId());
			randomizationListIn.setVersion(original.getVersion());
			StratificationRandomizationList randomizationList = stratificationRandomizationListDao.stratificationRandomizationListInVOToEntity(randomizationListIn);
			CoreUtil.modifyVersion(originalRandomizationList, randomizationList, now, user);
			stratificationRandomizationListDao.update(randomizationList);
			ServiceUtil.createRandomizationListCodes(randomizationList, codes, purgeOldCodes, now, user, this.getRandomizationListCodeDao(),
					this.getRandomizationListCodeValueDao());
			result = stratificationRandomizationListDao.toStratificationRandomizationListOutVO(randomizationList);
			ServiceUtil.logSystemMessage(randomizationList.getTrial(), result.getTrial(), now, user, SystemMessageCodes.STRATIFICATION_RANDOMIZATION_LIST_UPDATED, result, original,
					this.getJournalEntryDao());
		} else {
			StratificationRandomizationList randomizationList = stratificationRandomizationListDao
					.stratificationRandomizationListInVOToEntity(modifiedStratificationRandomizationList);
			CoreUtil.modifyVersion(randomizationList, now, user);
			randomizationList = stratificationRandomizationListDao.create(randomizationList);
			ServiceUtil.createRandomizationListCodes(randomizationList, codes, false, now, user, this.getRandomizationListCodeDao(), this.getRandomizationListCodeValueDao());
			result = stratificationRandomizationListDao.toStratificationRandomizationListOutVO(randomizationList);
			ServiceUtil.logSystemMessage(randomizationList.getTrial(), result.getTrial(), now, user, SystemMessageCodes.STRATIFICATION_RANDOMIZATION_LIST_CREATED, result, null,
					this.getJournalEntryDao());
		}
		return result;
	}

	@Override
	protected TeamMemberOutVO handleUpdateTeamMember(
			AuthenticationVO auth, TeamMemberInVO modifiedTeamMember) throws Exception {
		TeamMemberDao teamMemberDao = this.getTeamMemberDao();
		TeamMember originalTeamMember = CheckIDUtil.checkTeamMemberId(modifiedTeamMember.getId(), teamMemberDao);
		checkTeamMemberInput(modifiedTeamMember);
		TeamMemberOutVO original = teamMemberDao.toTeamMemberOutVO(originalTeamMember);
		teamMemberDao.evict(originalTeamMember);
		TeamMember teamMember = teamMemberDao.teamMemberInVOToEntity(modifiedTeamMember);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalTeamMember, teamMember, now, user);
		TeamMemberOutVO result = teamMemberDao.toTeamMemberOutVO(teamMember);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ServiceUtil.logSystemMessage(teamMember.getTrial(), result.getStaff(), now, user, SystemMessageCodes.TEAM_MEMBER_UPDATED, result, original, journalEntryDao);
		ServiceUtil.logSystemMessage(teamMember.getStaff(), result.getTrial(), now, user, SystemMessageCodes.TEAM_MEMBER_UPDATED, result, original, journalEntryDao);
		return result;
	}

	private void checkTimelineEventLoop(TimelineEvent timelineEvent) throws ServiceException {
		(new TimelineEventReflexionGraph(this.getTimelineEventDao())).checkGraphLoop(timelineEvent, true, false);
	}

	private void shiftTimelineEventChildren(TimelineEvent timelineEvent, long deltaDays, Timestamp now, User user, Integer maxInstances, Integer maxParentDepth,
			Integer maxChildrenDepth) throws Exception {
		if (deltaDays > 0l) {
			TimelineEventDao timelineEventDao = this.getTimelineEventDao();
			JournalEntryDao journalEntry = this.getJournalEntryDao();
			Iterator<TimelineEvent> it = timelineEvent.getChildren().iterator();
			while (it.hasNext()) {
				TimelineEvent child = it.next();
				TimelineEventOutVO original = timelineEventDao.toTimelineEventOutVO(child, maxInstances, maxParentDepth, maxChildrenDepth);
				child.setStart(DateCalc.addInterval(child.getStart(), VariablePeriod.EXPLICIT, deltaDays));
				if (child.getStop() != null) {
					child.setStop(DateCalc.addInterval(child.getStop(), VariablePeriod.EXPLICIT, deltaDays));
				}
				CoreUtil.modifyVersion(child, child.getVersion(), now, user);
				timelineEventDao.update(child);
				notifyTimelineEventReminder(child, now);
				TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(child, maxInstances, maxParentDepth, maxChildrenDepth);
				ServiceUtil
						.logSystemMessage(child.getTrial(), result.getTrial(), now, user, SystemMessageCodes.TIMELINE_EVENT_SHIFTED_CHILD_UPDATED, result, original,
								journalEntry);
			}
			it = timelineEvent.getChildren().iterator();
			while (it.hasNext()) {
				shiftTimelineEventChildren(it.next(), deltaDays, now, user, maxInstances, maxParentDepth, maxChildrenDepth);
			}
		}
	}

	@Override
	protected TimelineEventOutVO handleUpdateTimelineEvent(
			AuthenticationVO auth, TimelineEventInVO modifiedTimelineEvent, Integer maxInstances, Integer maxParentDepth, Integer maxChildrenDepth) throws Exception {
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent originalTimelineEvent = CheckIDUtil.checkTimelineEventId(modifiedTimelineEvent.getId(), timelineEventDao);
		checkTimelineEventInput(modifiedTimelineEvent);
		TimelineEventOutVO original = timelineEventDao.toTimelineEventOutVO(originalTimelineEvent, maxInstances, maxParentDepth, maxChildrenDepth);
		timelineEventDao.evict(originalTimelineEvent);
		TimelineEvent timelineEvent = timelineEventDao.timelineEventInVOToEntity(modifiedTimelineEvent);
		checkTimelineEventLoop(timelineEvent);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		shiftTimelineEventChildren(timelineEvent, DateCalc.dateDeltaDays(original.getStart(), timelineEvent.getStart()), now, user, maxInstances, maxParentDepth, maxChildrenDepth);
		CoreUtil.modifyVersion(originalTimelineEvent, timelineEvent, now, user);
		timelineEvent.setDismissedTimestamp(now);
		timelineEventDao.update(timelineEvent);
		notifyTimelineEventReminder(timelineEvent, now);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent, maxInstances, maxParentDepth, maxChildrenDepth);
		ServiceUtil
				.logSystemMessage(timelineEvent.getTrial(), result.getTrial(), now, user, SystemMessageCodes.TIMELINE_EVENT_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TrialOutVO handleUpdateTrial(AuthenticationVO auth, TrialInVO modifiedTrial, RandomizationListCodesVO modifiedCodes, boolean purgeOldCodes)
			throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial originalTrial = CheckIDUtil.checkTrialId(modifiedTrial.getId(), trialDao, LockMode.PESSIMISTIC_WRITE);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<RandomizationListCodeInVO> codes = checkUpdateTrialInput(originalTrial, modifiedTrial, modifiedCodes != null ? modifiedCodes.getCodes() : null, user);
		TrialStatusType originalTrialStatusType = originalTrial.getStatus();
		TrialOutVO original = trialDao.toTrialOutVO(originalTrial);
		this.getTrialStatusTypeDao().evict(originalTrialStatusType);
		trialDao.evict(originalTrial);
		Trial trial = trialDao.trialInVOToEntity(modifiedTrial);
		if (modifiedTrial.getRandomization() == null) {
			trial.setRandom(null);
		} else if (original.getRandomization() == null) {
			trial.setRandom(null);
		} else if (original.getRandomization().getMode().equals(modifiedTrial.getRandomization())) {
			trial.setRandom(null);
		}
		CoreUtil.modifyVersion(originalTrial, trial, now, user);
		trialDao.update(trial);
		ServiceUtil.createRandomizationListCodes(trial, codes, purgeOldCodes, now, user, this.getRandomizationListCodeDao(), this.getRandomizationListCodeValueDao());
		execTrialStatusActions(originalTrialStatusType, trial, now, user);
		TrialOutVO result = trialDao.toTrialOutVO(trial);
		ServiceUtil.logSystemMessage(trial, result, now, user, SystemMessageCodes.TRIAL_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TrialTagValueOutVO handleUpdateTrialTagValue(
			AuthenticationVO auth, TrialTagValueInVO modifiedTrialTagValue) throws Exception {
		TrialTagValueDao tagValueDao = this.getTrialTagValueDao();
		TrialTagValue originalTagValue = CheckIDUtil.checkTrialTagValueId(modifiedTrialTagValue.getId(), tagValueDao);
		checkTrialTagValueInput(modifiedTrialTagValue);
		TrialTagValueOutVO original = tagValueDao.toTrialTagValueOutVO(originalTagValue);
		tagValueDao.evict(originalTagValue);
		TrialTagValue tagValue = tagValueDao.trialTagValueInVOToEntity(modifiedTrialTagValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalTagValue, tagValue, now, user);
		tagValueDao.update(tagValue);
		TrialTagValueOutVO result = tagValueDao.toTrialTagValueOutVO(tagValue);
		ServiceUtil.logSystemMessage(tagValue.getTrial(), result.getTrial(), now, user, SystemMessageCodes.TRIAL_TAG_VALUE_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected VisitOutVO handleUpdateVisit(AuthenticationVO auth, VisitInVO modifiedVisit)
			throws Exception {
		VisitDao visitDao = this.getVisitDao();
		Visit originalVisit = CheckIDUtil.checkVisitId(modifiedVisit.getId(), visitDao);
		checkVisitInput(modifiedVisit);
		if (!modifiedVisit.getTrialId().equals(originalVisit.getTrial().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_TRIAL_CHANGED);
		}
		VisitOutVO original = visitDao.toVisitOutVO(originalVisit);
		visitDao.evict(originalVisit);
		Visit visit = visitDao.visitInVOToEntity(modifiedVisit);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalVisit, visit, now, user);
		visitDao.update(visit);
		VisitOutVO result = visitDao.toVisitOutVO(visit);
		ServiceUtil.logSystemMessage(visit.getTrial(), result.getTrial(), now, user, SystemMessageCodes.VISIT_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected VisitScheduleItemOutVO handleUpdateVisitScheduleItem(
			AuthenticationVO auth, VisitScheduleItemInVO modifiedVisitScheduleItem) throws Exception {
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		VisitScheduleItem originalVisitScheduleItem = CheckIDUtil.checkVisitScheduleItemId(modifiedVisitScheduleItem.getId(), visitScheduleItemDao);
		checkVisitScheduleItemInput(modifiedVisitScheduleItem);
		if (!modifiedVisitScheduleItem.getTrialId().equals(originalVisitScheduleItem.getTrial().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_TRIAL_CHANGED);
		}
		VisitScheduleItemOutVO original = visitScheduleItemDao.toVisitScheduleItemOutVO(originalVisitScheduleItem);
		visitScheduleItemDao.evict(originalVisitScheduleItem);
		VisitScheduleItem visitScheduleItem = visitScheduleItemDao.visitScheduleItemInVOToEntity(modifiedVisitScheduleItem);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalVisitScheduleItem, visitScheduleItem, now, user);
		visitScheduleItemDao.update(visitScheduleItem);
		notifyVisitScheduleItemReminder(visitScheduleItem, null, now);
		VisitScheduleItemOutVO result = visitScheduleItemDao.toVisitScheduleItemOutVO(visitScheduleItem);
		ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getTrial(), now, user, SystemMessageCodes.VISIT_SCHEDULE_ITEM_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<ECRFStatusEntryVO> handleValidatePendingEcrfs(AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId, Long visitId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao(), LockMode.PESSIMISTIC_WRITE);
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
		}
		if (ecrfId != null) {
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
			Visit visit = null;
			if (visitId != null) {
				visit = CheckIDUtil.checkVisitId(visitId, this.getVisitDao());
			}
			checkEcrfVisit(ecrf, visit);
		} else {
			if (visitId != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_ID_NOT_NULL);
			}
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ArrayList<ECRFStatusEntryVO> results = new ArrayList<ECRFStatusEntryVO>();
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Iterator<ECRFStatusEntry> statusEntryIt = ecrfStatusEntryDao
				.findByTrialListEntryEcrfVisitValidationStatus(trialId, probandListEntryId, ecrfId, visitId, ECRFValidationStatus.PENDING, null).iterator();
		while (statusEntryIt.hasNext()) {
			ECRFStatusEntry statusEntry = statusEntryIt.next();
			ECRFStatusEntryVO original = ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry);
			// edit checks based on subject PII are discouraged:
			if (!original.getListEntry().getProband().getDecrypted()) {
				continue; // support cron jobs with different department users, so each will not raise/close even if there are edit checks for PII
			}
			boolean noMissingValues = false;
			if (hasEcrfStatusAction(statusEntry.getStatus(), org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NO_MISSING_VALUES)) {
				try {
					checkMissingEcrfFieldValuesDeeply(statusEntry.getListEntry(), statusEntry.getEcrf(), statusEntry.getVisit());
					noMissingValues = true;
				} catch (ServiceException e) {
					statusEntry.setValidationTimestamp(now);
					statusEntry.setValidationStatus(ECRFValidationStatus.FAILED);
					statusEntry.setValidationResponseMsg(L10nUtil.getMessage(Locales.NOTIFICATION, MessageCodes.ECRF_VALIDATION_FAILED_RESPONSE,
							DefaultMessages.ECRF_VALIDATION_FAILED_RESPONSE,
							e.getMessage()));
				}
			} else {
				noMissingValues = true;
			}
			if (noMissingValues) {
				addValidationEcrfFieldStatusEntries(statusEntry, true, now, user);
			}
			ecrfStatusEntryDao.update(statusEntry);
			if (hasEcrfStatusAction(statusEntry.getStatus(), org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NOTIFY_ECRF_STATUS)) {
				this.getNotificationDao().addNotification(statusEntry, now, null);
			}
			ECRFStatusEntryVO result = ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry);
			ServiceUtil.logSystemMessage(statusEntry.getListEntry().getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.ECRF_VALIDATED, result, original,
					journalEntryDao);
			ServiceUtil.logSystemMessage(statusEntry.getListEntry().getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.ECRF_VALIDATED, result, original,
					journalEntryDao);
			results.add(result);
		}
		return results;
	}

	@Override
	protected SignatureVO handleVerifyEcrfSignature(AuthenticationVO auth,
			Long probandListEntryId, Long ecrfId, Long visitId) throws Exception {
		ECRFStatusEntry statusEntry = checkEcrfStatusEntry(probandListEntryId, ecrfId, visitId);
		Signature signature = this.getSignatureDao().findRecentSignature(SignatureModule.ECRF_SIGNATURE, statusEntry.getId());
		if (signature == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_SIGNATURE);
		}
		return ServiceUtil.getVerifiedEcrfSignatureVO(signature, this.getSignatureDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao());
	}

	@Override
	protected SignatureVO handleVerifyTrialSignature(AuthenticationVO auth,
			Long trialId) throws Exception {
		CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		Signature signature = this.getSignatureDao().findRecentSignature(SignatureModule.TRIAL_SIGNATURE, trialId);
		if (signature == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_SIGNATURE);
		}
		return getVerifiedTrialSignatureVO(signature);
	}

	private Long limitEcrfFieldValueIndex(long ecrfId, Long visitId, String section, Long index, long listEntryId) throws Exception {
		if (index != null) {
			if (index < 0l) {
				return 0l;
			} else {
				Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(listEntryId, ecrfId, visitId, section);
				if (maxIndex != null) {
					if (index > maxIndex) {
						index = maxIndex;
					}
				} else {
					index = null;
				}
			}
		}
		return index;
	}

	private void logSystemMessage(Trial trial, TrialOutVO trialVO, Timestamp now, User user, RandomizationListInfoVO randomizationListInfo) throws Exception {
		this.getJournalEntryDao().addSystemMessage(trial, now, user, SystemMessageCodes.RANDOMIZATION_LIST_GENERATED,
				new Object[] { CommonUtil.trialOutVOToString(trialVO), Integer.toString(randomizationListInfo.getN()),
						Integer.toString(randomizationListInfo.getItems().size()) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(randomizationListInfo, null, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	}

	private void logSystemMessage(Trial trial, TrialOutVO trialVO, Timestamp now, User user, ShuffleInfoVO shuffleInfo, String systemMessageCode) throws Exception {
		this.getJournalEntryDao().addSystemMessage(trial, now, user, systemMessageCode,
				new Object[] { CommonUtil.trialOutVOToString(trialVO), shuffleInfo != null ? Integer.toString(shuffleInfo.getResultIds().size()) : null,
						shuffleInfo != null ? Integer.toString(shuffleInfo.getInputIds().size()) : null },
				new Object[] { CoreUtil.getSystemMessageCommentContent(shuffleInfo, null, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	}

	private void notifyEcrfFieldStatus(ECRFStatusEntry statusEntry, ECRFFieldStatusEntry lastStatus, ECRFFieldStatusEntry newStatus, ECRFFieldStatusQueue queue, Date now)
			throws Exception {
		if (newStatus != null
				&& (newStatus.getEcrfField().isNotify() || (statusEntry != null && statusEntry.getStatus().isReview()
						&& Settings.getEcrfFieldStatusQueueList(SettingCodes.NEW_ECRF_FIELD_STATUS_NOTIFICATION_QUEUES, Bundle.SETTINGS,
								DefaultSettings.NEW_ECRF_FIELD_STATUS_NOTIFICATION_QUEUES)
								.contains(
										queue)))) {
			if (lastStatus != null) {
				ServiceUtil.cancelNotifications(lastStatus.getNotifications(), this.getNotificationDao(),
						org.phoenixctms.ctsms.enumeration.NotificationType.NEW_ECRF_FIELD_STATUS);
			}
			Map messageParameters = CoreUtil.createEmptyTemplateModel();
			messageParameters.put(NotificationMessageTemplateParameters.INDEX_FIELD_LOG,
					ServiceUtil.getIndexFieldLog(newStatus.getListEntry().getId(), newStatus.getVisit() != null ? newStatus.getVisit().getId() : null,
							newStatus.getEcrfField().getId(), newStatus.getIndex(), false, true, false, true, ECRFFieldStatusQueue.values(),
							this.getECRFFieldValueDao(),
							this.getECRFFieldStatusEntryDao()));
			this.getNotificationDao().addNotification(newStatus, now, messageParameters);
		}
	}

	private void notifyTimelineEventReminder(TimelineEvent timelineEvent, Date now) throws Exception {
		ReminderEntityAdapter reminderItem = ReminderEntityAdapter.getInstance(timelineEvent);
		Date reminderStart = reminderItem.getReminderStart(now, false, null, null);
		if (timelineEvent.isNotify() && now.compareTo(reminderStart) >= 0 && !reminderItem.isRecurrenceDismissed(reminderStart)
				&& !timelineEvent.getTrial().getStatus().isIgnoreTimelineEvents()) {
			this.getNotificationDao().addNotification(timelineEvent, now, null);
		} else {
			ServiceUtil
					.cancelNotifications(timelineEvent.getNotifications(), this.getNotificationDao(), org.phoenixctms.ctsms.enumeration.NotificationType.TIMELINE_EVENT_REMINDER);
		}
	}

	private void notifyVisitScheduleItemReminder(ProbandListEntry listEntry, ProbandListEntryTag listEntryTag,
			Timestamp now) throws Exception {
		Iterator<VisitScheduleItem> it = listEntryTag.getStartDates().iterator();
		while (it.hasNext()) {
			notifyVisitScheduleItemReminder(it.next(), listEntry, now);
		}
		it = listEntryTag.getStopDates().iterator();
		while (it.hasNext()) {
			notifyVisitScheduleItemReminder(it.next(), listEntry, now);
		}
	}

	private void notifyVisitScheduleItemReminder(VisitScheduleItem visitScheduleItem, ProbandListEntry listEntry, Date now) throws Exception {
		Collection<Notification> oldNotifications;
		if (listEntry != null) {
			oldNotifications = this.getNotificationDao().getNotifications(visitScheduleItem, listEntry.getProband());
		} else {
			oldNotifications = visitScheduleItem.getNotifications();
		}
		ServiceUtil.cancelNotifications(oldNotifications, this.getNotificationDao(),
				org.phoenixctms.ctsms.enumeration.NotificationType.VISIT_SCHEDULE_ITEM_REMINDER);
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		Iterator<Map.Entry> visitScheduleItemScheduleIt = visitScheduleItemDao
				.findVisitScheduleItemSchedule(now, visitScheduleItem.getId(), listEntry != null ? listEntry.getProband().getId() : null, null, null, true, false,
						Settings.getVariablePeriod(SettingCodes.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD, Settings.Bundle.SETTINGS,
								DefaultSettings.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD),
						Settings.getLongNullable(SettingCodes.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD_DAYS, Settings.Bundle.SETTINGS,
								DefaultSettings.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD_DAYS),
						false)
				.entrySet().iterator();
		while (visitScheduleItemScheduleIt.hasNext()) {
			Entry visitScheduleItemSchedule = visitScheduleItemScheduleIt.next();
			Long probandId = (Long) visitScheduleItemSchedule.getKey();
			Proband proband = probandId != null ? this.getProbandDao().load(probandId) : null;
			//XXXX
			Iterator<VisitScheduleItem> visitScheduleItemsIt = ((List<VisitScheduleItem>) visitScheduleItemSchedule.getValue()).iterator();
			while (visitScheduleItemsIt.hasNext()) {
				this.getNotificationDao().addNotification(visitScheduleItemsIt.next(), proband, now, null);
			}
		}
	}

	private ProbandListEntryOutVO removeProbandListEntry(ProbandListEntry probandListEntry, Timestamp now, User user, boolean deleteCascade) throws Exception {
		Trial trial = probandListEntry.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		Proband proband = probandListEntry.getProband();
		ServiceUtil.checkProbandLocked(proband);
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO result = probandListEntryDao.toProbandListEntryOutVO(probandListEntry);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ProbandGroup group = probandListEntry.getGroup();
		trial.removeProbandListEntries(probandListEntry);
		if (group != null) {
			group.removeProbandListEntries(probandListEntry);
		}
		proband.removeTrialParticipations(probandListEntry);
		ServiceUtil.removeProbandListEntry(probandListEntry, deleteCascade, now, user, true, true, this.getProbandListStatusEntryDao(), this.getProbandListEntryTagValueDao(),
				this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao(), this.getSignatureDao(), this.getECRFStatusEntryDao(), this.getInputFieldValueDao(),
				journalEntryDao,
				probandListEntryDao,
				this.getNotificationDao(), this.getNotificationRecipientDao());
		ServiceUtil.logSystemMessage(proband, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_DELETED, result, null, journalEntryDao);
		ServiceUtil.logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_DELETED, result, null, journalEntryDao);
		return result;
	}

	private Object[] setEcrfFieldValues(Set<ECRFFieldValueInVO> ecrfFieldValuesIn, boolean loadPageResult, boolean force) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ECRFFieldValuesOutVO result = new ECRFFieldValuesOutVO();
		ServiceException firstException = null;
		HashMap<Long, HashMap<Long, String>> errorMessagesMap = new HashMap<Long, HashMap<Long, String>>();
		LinkedHashMap<String, LinkedHashSet<Long>> sectionIndexMap = new LinkedHashMap<String, LinkedHashSet<Long>>();
		ProbandListEntry listEntry = null;
		ECRFStatusEntry statusEntry = null;
		ECRFStatusEntryVO statusEntryVO = null;
		ProbandListEntryOutVO listEntryVO = null;
		ECRF ecrf = null;
		Visit visit = null;
		ArrayList<ECRFFieldValueOutVO> ecrfFieldValues = null;
		ArrayList<ECRFFieldValueJsonVO> jsEcrfFieldValues = null;
		if (ecrfFieldValuesIn != null && ecrfFieldValuesIn.size() > 0) {
			ECRFFieldValueInVO ecrfFieldValueIn;
			ECRFField ecrfField = null;
			if (!loadPageResult) {
				ecrfFieldValues = new ArrayList<ECRFFieldValueOutVO>(ecrfFieldValuesIn.size());
			}
			Iterator<ECRFFieldValueInVO> ecrfFieldValuesInIt = ecrfFieldValuesIn.iterator();
			while (ecrfFieldValuesInIt.hasNext()) {
				ecrfFieldValueIn = ecrfFieldValuesInIt.next();
				ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldValueIn.getEcrfFieldId(), this.getECRFFieldDao());
				if (ecrf == null) {
					ecrf = ecrfField.getEcrf();
					if (ecrf.isEnableBrowserFieldCalculation() && Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
						jsEcrfFieldValues = new ArrayList<ECRFFieldValueJsonVO>(ecrfFieldValuesIn.size());
					}
					ServiceUtil.checkTrialLocked(ecrf.getTrial());
					if (!ecrf.getTrial().getStatus().isEcrfValueInputEnabled()) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_TRIAL,
								L10nUtil.getTrialStatusTypeName(Locales.USER, ecrf.getTrial().getStatus().getNameL10nKey()));
					}
					if (ecrf.isDisabled()) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_ECRF, ecrf.getName());
					}
					// check dataentry locked...
				} else if (!ecrf.equals(ecrfField.getEcrf())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_FOR_DIFFERENT_ECRFS);
				}
				if (ecrfFieldValueIn.getVisitId() != null) {
					visit = CheckIDUtil.checkVisitId(ecrfFieldValueIn.getVisitId(), this.getVisitDao());
				}
				if (listEntry == null) {
					listEntry = CheckIDUtil.checkProbandListEntryId(ecrfFieldValueIn.getListEntryId(), this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
					if (!ecrf.getTrial().equals(listEntry.getTrial())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_FOR_WRONG_TRIAL);
					}
					ServiceUtil.checkProbandLocked(listEntry.getProband());
					statusEntry = this.getECRFStatusEntryDao().findByListEntryEcrfVisit(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null);
					if (statusEntry == null) {
						ECRFStatusType statusType = this.getECRFStatusTypeDao().findInitialStates().iterator().next();
						this.getECRFDao().lock(ecrf, LockMode.PESSIMISTIC_WRITE);
						Object[] resultItems = addEcrfStatusEntry(listEntry, ecrf, visit, statusType, null, now, user);
						statusEntry = (ECRFStatusEntry) resultItems[0];
						statusEntryVO = (ECRFStatusEntryVO) resultItems[1];
					} else {
						statusEntryVO = this.getECRFStatusEntryDao().toECRFStatusEntryVO(statusEntry);
					}
					listEntryVO = statusEntryVO.getListEntry();
					if (listEntryVO.getLastStatus() != null && !listEntryVO.getLastStatus().getStatus().getEcrfValueInputEnabled()) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
								listEntryVO.getLastStatus().getStatus().getName());
					}
				} else if (!listEntry.getId().equals(ecrfFieldValueIn.getListEntryId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_FOR_DIFFERENT_PROBAND_LIST_ENTRIES);
				}
				if (visit != null) {
					if (!visit.equals(statusEntry.getVisit())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INVALID_VISIT);
					}
				} else {
					if (statusEntry.getVisit() != null) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INVALID_VISIT);
					}
				}
				try {
					addUpdateEcrfFieldValue(ecrfFieldValueIn, statusEntry, listEntryVO, ecrfField, now, user, force,
							Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_VALUE_TRIAL, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_VALUE_TRIAL),
							Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_VALUE_PROBAND, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_VALUE_PROBAND), sectionIndexMap,
							ecrfFieldValues, jsEcrfFieldValues);
				} catch (ServiceException e) {
					if (firstException == null) {
						firstException = e;
					}
					if (errorMessagesMap.containsKey(ecrfField.getId())) {
						errorMessagesMap.get(ecrfField.getId()).put(ecrfFieldValueIn.getIndex(), e.getMessage());
					} else {
						HashMap<Long, String> indexErrorMap = new HashMap<Long, String>();
						indexErrorMap.put(ecrfFieldValueIn.getIndex(), e.getMessage());
						errorMessagesMap.put(ecrfField.getId(), indexErrorMap);
					}
				}
			}
			Iterator<Entry<String, LinkedHashSet<Long>>> sectionIndexMapIt = sectionIndexMap.entrySet().iterator();
			while (sectionIndexMapIt.hasNext()) {
				Entry<String, LinkedHashSet<Long>> sectionIndexes = sectionIndexMapIt.next();
				String section = sectionIndexes.getKey();
				Iterator<Long> indexesIt = sectionIndexes.getValue().iterator();
				while (indexesIt.hasNext()) {
					Long index = indexesIt.next();
					Iterator<ECRFFieldValueInVO> missingSeriesInIt = ServiceUtil
							.createPresetEcrfFieldInValues(listEntry.getId(), ecrf.getId(), visit != null ? visit.getId() : null, section, index,
									this.getECRFFieldDao(),
									this.getECRFFieldValueDao(), this.getInputFieldSelectionSetValueDao())
							.iterator();
					while (missingSeriesInIt.hasNext()) {
						ecrfFieldValueIn = missingSeriesInIt.next();
						if (!(errorMessagesMap.containsKey(ecrfFieldValueIn.getEcrfFieldId())
								&& errorMessagesMap.get(ecrfFieldValueIn.getEcrfFieldId()).containsKey(ecrfFieldValueIn.getIndex()))) {
							// if there were processing errors for a indexfield, there was field input given. we do not try to insert default values instead (which can cause
							// another error and replace the original exception).
							ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldValueIn.getEcrfFieldId(), this.getECRFFieldDao());
							try {
								addUpdateEcrfFieldValue(ecrfFieldValueIn, statusEntry, listEntryVO, ecrfField, now, user, force,
										Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_VALUE_TRIAL, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_VALUE_TRIAL),
										Settings.getBoolean(SettingCodes.LOG_ECRF_FIELD_VALUE_PROBAND, Bundle.SETTINGS, DefaultSettings.LOG_ECRF_FIELD_VALUE_PROBAND), null,
										ecrfFieldValues, jsEcrfFieldValues);
							} catch (ServiceException e) {
								if (firstException == null) {
									firstException = e;
								}
								if (errorMessagesMap.containsKey(ecrfField.getId())) {
									errorMessagesMap.get(ecrfField.getId()).put(ecrfFieldValueIn.getIndex(), e.getMessage());
								} else {
									HashMap<Long, String> indexErrorMap = new HashMap<Long, String>();
									indexErrorMap.put(ecrfFieldValueIn.getIndex(), e.getMessage());
									errorMessagesMap.put(ecrfField.getId(), indexErrorMap);
								}
							}
						}
					}
				}
			}
			if (firstException != null) {
				firstException.setData(errorMessagesMap);
				throw firstException;
			}
		}
		if (ecrfFieldValues != null) {
			Collections.sort(ecrfFieldValues, new EcrfFieldValueOutVOComparator());
			result.setPageValues(ecrfFieldValues);
		}
		if (jsEcrfFieldValues != null) {
			result.setJsValues(jsEcrfFieldValues);
		}
		return new Object[] { result, listEntryVO, this.getECRFDao().toECRFOutVO(ecrf), this.getVisitDao().toVisitOutVO(visit) };
	}

	private ECRFStatusEntryVO updateEcrfStatusEntry(ECRFStatusEntry originalStatusEntry, ECRFStatusType statusType,
			Long version,
			Long probandListStatusTypeId, Timestamp now, User user) throws Exception {
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		ECRFStatusEntryVO result;
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		checkUpdateEcrfStatusEntry(originalStatusEntry, statusType, version, user);
		ECRFStatusType originalEcrfStatusType = originalStatusEntry.getStatus();
		ECRFStatusEntryVO original = ecrfStatusEntryDao.toECRFStatusEntryVO(originalStatusEntry);
		ProbandListEntry listEntry = originalStatusEntry.getListEntry();
		this.getECRFStatusTypeDao().evict(originalEcrfStatusType);
		ecrfStatusEntryDao.evict(originalStatusEntry);
		ECRFStatusEntry statusEntry = ecrfStatusEntryDao.load(originalStatusEntry.getId());
		statusEntry.setStatus(statusType);
		CoreUtil.modifyVersion(statusEntry, version.longValue(), now, user);
		ecrfStatusEntryDao.update(statusEntry);
		execEcrfStatusActions(statusEntry, probandListStatusTypeId, now, user);
		result = ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry);
		ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.ECRF_STATUS_ENTRY_UPDATED, result, original,
				journalEntryDao);
		ServiceUtil.logSystemMessage(listEntry.getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.ECRF_STATUS_ENTRY_UPDATED, result, original,
				journalEntryDao);
		return result;
	}

	@Override
	protected Collection<ProbandOutVO> handleGetInquiryProbandList(AuthenticationVO auth, Long trialId, Boolean active, Boolean activeSignup, PSFVO psf) throws Exception {
		CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		ProbandDao probandDao = this.getProbandDao();
		Collection probands = probandDao.findByInquiryValuesTrial(null, trialId, active, activeSignup, psf);
		probandDao.toProbandOutVOCollection(probands);
		return probands;
	}

	@Override
	protected RandomizationListCodeOutVO handleGetRandomizationListCode(AuthenticationVO auth, Long probandListEntryId, boolean breakCode, String reasonForBreak) throws Exception {
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		RandomizationListCodeDao randomizationListCodeDao = this.getRandomizationListCodeDao();
		Randomization randomization = Randomization.getInstance(listEntry.getTrial().getRandomization(), this.getTrialDao(), this.getProbandGroupDao(),
				this.getProbandListEntryDao(),
				this.getStratificationRandomizationListDao(), this.getProbandListEntryTagDao(), this.getInputFieldSelectionSetValueDao(),
				this.getProbandListEntryTagValueDao(), randomizationListCodeDao);
		RandomizationListCode randomizationListCode = randomization.getRandomizationListCode(listEntry);
		RandomizationListCodeOutVO result;
		if (breakCode) {
			if (CommonUtil.isEmptyString(reasonForBreak)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.REASON_FOR_BREAK_REQUIRED);
			}
			Timestamp now = new Timestamp(System.currentTimeMillis());
			User user = CoreUtil.getUser();
			randomizationListCode.setBreakUser(user);
			randomizationListCode.setReasonForBreak(reasonForBreak);
			randomizationListCode.setBreakTimestamp(now);
			randomizationListCode.setBroken(true);
			CoreUtil.modifyVersion(randomizationListCode, randomizationListCode.getVersion(), now, user);
			randomizationListCodeDao.update(randomizationListCode);
			RandomizationListCodeOutVO resultLog = randomizationListCodeDao.toRandomizationListCodeOutVO(randomizationListCode);
			if (Settings.getBoolean(SettingCodes.OBFUSCATE_BROKEN_RANDOMIZATION_CODES, Bundle.SETTINGS, DefaultSettings.OBFUSCATE_BROKEN_RANDOMIZATION_CODES)) {
				Randomization.obfuscateRandomizationListCodeValues(resultLog);
				CoreUtil.getUserContext().voMapClear();
				result = randomizationListCodeDao.toRandomizationListCodeOutVO(randomizationListCode);
			} else {
				result = resultLog;
			}
			ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.RANDOMIZATION_CODE_BREAK, resultLog, null,
					this.getJournalEntryDao());
			ServiceUtil.logSystemMessage(listEntry.getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.RANDOMIZATION_CODE_BREAK, resultLog, null,
					this.getJournalEntryDao());
		} else {
			result = randomizationListCodeDao.toRandomizationListCodeOutVO(randomizationListCode);
			Randomization.obfuscateRandomizationListCodeValues(result);
		}
		return result;
	}

	@Override
	protected Collection<RandomizationListCodeOutVO> handleGetRandomizationListCodeList(AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		RandomizationListCodeDao randomizationListCodeDao = this.getRandomizationListCodeDao();
		Collection codes = randomizationListCodeDao.findByTrialBroken(trialId, null, psf);
		randomizationListCodeDao.toRandomizationListCodeOutVOCollection(codes);
		Iterator codesIt = codes.iterator();
		while (codesIt.hasNext()) {
			Randomization.obfuscateRandomizationListCodeValues((RandomizationListCodeOutVO) codesIt.next());
		}
		return codes;
	}

	@Override
	protected RandomizationListCodeOutVO handleGetRandomizationListCodeById(AuthenticationVO auth, Long randomizationListCodeId) throws Exception {
		RandomizationListCodeDao randomizationListCodeDao = this.getRandomizationListCodeDao();
		RandomizationListCode code = CheckIDUtil.checkRandomizationListCodeId(randomizationListCodeId, randomizationListCodeDao);
		RandomizationListCodeOutVO result = randomizationListCodeDao.toRandomizationListCodeOutVO(code);
		Randomization.obfuscateRandomizationListCodeValues(result);
		return result;
	}

	@Override
	protected Collection<VisitScheduleAppointmentVO> handleGetVisitScheduleAppointmentList(AuthenticationVO auth, Long visitScheduleItemId, Long probandId, PSFVO psf)
			throws Exception {
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		CheckIDUtil.checkVisitScheduleItemId(visitScheduleItemId, visitScheduleItemDao);
		ProbandDao probandDao = this.getProbandDao();
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, probandDao);
		}
		ArrayList<VisitScheduleAppointmentVO> result = new ArrayList<VisitScheduleAppointmentVO>();
		Iterator<Object[]> it = visitScheduleItemDao.findExpandedDateMode(visitScheduleItemId, probandId, psf).iterator();
		while (it.hasNext()) {
			Object[] visitScheduleItemProband = it.next();
			VisitScheduleAppointmentVO visitScheduleItemProbandVO = visitScheduleItemDao.toVisitScheduleAppointmentVO((VisitScheduleItem) visitScheduleItemProband[0]);
			visitScheduleItemProbandVO.setProband(probandDao.toProbandOutVO((Proband) visitScheduleItemProband[1]));
			result.add(visitScheduleItemProbandVO);
		}
		return result;
	}

	@Override
	protected Collection<InventoryBookingOutVO> handleGetTrialInventoryBookingInterval(
			AuthenticationVO auth, Long trialId, Long trialDepartmentId, Date from, Date to, Boolean isRelevantForCourseAppointments, boolean sort)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (trialDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(trialDepartmentId, this.getDepartmentDao());
		}
		InventoryBookingDao inventoryBookingDao = this.getInventoryBookingDao();
		Collection inventoryBookings = inventoryBookingDao.findByTrialDepartmentInterval(
				trialId, trialDepartmentId, CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to),
				isRelevantForCourseAppointments);
		inventoryBookingDao.toInventoryBookingOutVOCollection(inventoryBookings);
		if (sort) {
			inventoryBookings = new ArrayList(inventoryBookings);
			Collections.sort((ArrayList) inventoryBookings, new InventoryBookingIntervalComparator(false));
		}
		return inventoryBookings;
	}
}