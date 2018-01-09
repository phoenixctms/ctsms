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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.EcrfFieldJsVariableNameCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfFieldMoveAdapter;
import org.phoenixctms.ctsms.adapt.EcrfFieldPositionCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfFieldSeriesCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfFieldValueCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfMoveAdapter;
import org.phoenixctms.ctsms.adapt.EcrfNameCollisionFinder;
import org.phoenixctms.ctsms.adapt.EcrfPositionCollisionFinder;
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
import org.phoenixctms.ctsms.compare.EcrfFieldValueStatusEntryOutVOComparator;
import org.phoenixctms.ctsms.compare.EcrfStatusActionComparator;
import org.phoenixctms.ctsms.compare.InquiryValueOutVOComparator;
import org.phoenixctms.ctsms.compare.ProbandListEntryTagValueOutVOComparator;
import org.phoenixctms.ctsms.compare.ProbandListStatusEntryOutVOComparator;
import org.phoenixctms.ctsms.compare.ProbandOutVOComparator;
import org.phoenixctms.ctsms.compare.TeamMemberOutVOComparator;
import org.phoenixctms.ctsms.compare.TrialStatusActionComparator;
import org.phoenixctms.ctsms.compare.VisitScheduleItemIntervalComparator;
import org.phoenixctms.ctsms.domain.AddressTypeDao;
import org.phoenixctms.ctsms.domain.BankAccountDao;
import org.phoenixctms.ctsms.domain.ContactDetailTypeDao;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.DutyRosterTurn;
import org.phoenixctms.ctsms.domain.DutyRosterTurnDao;
import org.phoenixctms.ctsms.domain.ECRF;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFField;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusEntry;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusType;
import org.phoenixctms.ctsms.domain.ECRFFieldValue;
import org.phoenixctms.ctsms.domain.ECRFFieldValueDao;
import org.phoenixctms.ctsms.domain.ECRFStatusAction;
import org.phoenixctms.ctsms.domain.ECRFStatusEntry;
import org.phoenixctms.ctsms.domain.ECRFStatusEntryDao;
import org.phoenixctms.ctsms.domain.ECRFStatusType;
import org.phoenixctms.ctsms.domain.ECRFStatusTypeDao;
import org.phoenixctms.ctsms.domain.File;
import org.phoenixctms.ctsms.domain.FileDao;
import org.phoenixctms.ctsms.domain.Hyperlink;
import org.phoenixctms.ctsms.domain.HyperlinkDao;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.InputFieldValue;
import org.phoenixctms.ctsms.domain.InputFieldValueDao;
import org.phoenixctms.ctsms.domain.Inquiry;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InquiryValue;
import org.phoenixctms.ctsms.domain.InquiryValueDao;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryBooking;
import org.phoenixctms.ctsms.domain.InventoryBookingDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.MoneyTransfer;
import org.phoenixctms.ctsms.domain.MoneyTransferDao;
import org.phoenixctms.ctsms.domain.NotificationDao;
import org.phoenixctms.ctsms.domain.NotificationRecipientDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandAddress;
import org.phoenixctms.ctsms.domain.ProbandAddressDao;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValue;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandGroup;
import org.phoenixctms.ctsms.domain.ProbandGroupDao;
import org.phoenixctms.ctsms.domain.ProbandListEntry;
import org.phoenixctms.ctsms.domain.ProbandListEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTag;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValue;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntry;
import org.phoenixctms.ctsms.domain.ProbandListStatusEntryDao;
import org.phoenixctms.ctsms.domain.ProbandListStatusType;
import org.phoenixctms.ctsms.domain.ProbandStatusEntryDao;
import org.phoenixctms.ctsms.domain.ProbandTagDao;
import org.phoenixctms.ctsms.domain.ProbandTagValue;
import org.phoenixctms.ctsms.domain.ProbandTagValueDao;
import org.phoenixctms.ctsms.domain.Signature;
import org.phoenixctms.ctsms.domain.SignatureDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffAddress;
import org.phoenixctms.ctsms.domain.StaffAddressDao;
import org.phoenixctms.ctsms.domain.StaffContactDetailValue;
import org.phoenixctms.ctsms.domain.StaffContactDetailValueDao;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffTagDao;
import org.phoenixctms.ctsms.domain.StaffTagValue;
import org.phoenixctms.ctsms.domain.StaffTagValueDao;
import org.phoenixctms.ctsms.domain.TeamMember;
import org.phoenixctms.ctsms.domain.TeamMemberDao;
import org.phoenixctms.ctsms.domain.TimelineEvent;
import org.phoenixctms.ctsms.domain.TimelineEventDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.TrialStatusAction;
import org.phoenixctms.ctsms.domain.TrialStatusType;
import org.phoenixctms.ctsms.domain.TrialStatusTypeDao;
import org.phoenixctms.ctsms.domain.TrialTag;
import org.phoenixctms.ctsms.domain.TrialTagValue;
import org.phoenixctms.ctsms.domain.TrialTagValueDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.Visit;
import org.phoenixctms.ctsms.domain.VisitDao;
import org.phoenixctms.ctsms.domain.VisitScheduleItem;
import org.phoenixctms.ctsms.domain.VisitScheduleItemDao;
import org.phoenixctms.ctsms.email.NotificationMessageTemplateParameters;
import org.phoenixctms.ctsms.enumeration.ECRFFieldStatusQueue;
import org.phoenixctms.ctsms.enumeration.ECRFValidationStatus;
import org.phoenixctms.ctsms.enumeration.ExportStatus;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.enumeration.PaymentMethod;
import org.phoenixctms.ctsms.enumeration.PositionMovement;
import org.phoenixctms.ctsms.enumeration.ProbandListStatusLogLevel;
import org.phoenixctms.ctsms.enumeration.SignatureModule;
import org.phoenixctms.ctsms.enumeration.VariablePeriod;
import org.phoenixctms.ctsms.excel.AuditTrailExcelDefaultSettings;
import org.phoenixctms.ctsms.excel.AuditTrailExcelSettingCodes;
import org.phoenixctms.ctsms.excel.AuditTrailExcelWriter;
import org.phoenixctms.ctsms.excel.ExcelExporter;
import org.phoenixctms.ctsms.excel.ExcelUtil;
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
import org.phoenixctms.ctsms.pdf.EcrfPDFDefaultSettings;
import org.phoenixctms.ctsms.pdf.EcrfPDFPainter;
import org.phoenixctms.ctsms.pdf.EcrfPDFSettingCodes;
import org.phoenixctms.ctsms.pdf.PDFImprinter;
import org.phoenixctms.ctsms.pdf.ProbandListEntryTagsPDFPainter;
import org.phoenixctms.ctsms.pdf.ReimbursementsPDFPainter;
import org.phoenixctms.ctsms.security.CipherText;
import org.phoenixctms.ctsms.security.CryptoUtil;
import org.phoenixctms.ctsms.security.EcrfSignature;
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
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.AddressTypeVO;
import org.phoenixctms.ctsms.vo.AuditTrailExcelVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ContactDetailTypeVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
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
import org.phoenixctms.ctsms.vo.ECRFStatusTypeVO;
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
import org.phoenixctms.ctsms.vo.MoneyTransferOutVO;
import org.phoenixctms.ctsms.vo.MoneyTransferSummaryVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandAddressOutVO;
import org.phoenixctms.ctsms.vo.ProbandContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandGroupInVO;
import org.phoenixctms.ctsms.vo.ProbandGroupOutVO;
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
import org.phoenixctms.ctsms.vo.ReimbursementsExcelVO;
import org.phoenixctms.ctsms.vo.ReimbursementsPDFVO;
import org.phoenixctms.ctsms.vo.ShiftDurationSummaryVO;
import org.phoenixctms.ctsms.vo.ShuffleInfoVO;
import org.phoenixctms.ctsms.vo.SignatureVO;
import org.phoenixctms.ctsms.vo.StaffAddressOutVO;
import org.phoenixctms.ctsms.vo.StaffContactDetailValueOutVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffTagVO;
import org.phoenixctms.ctsms.vo.StaffTagValueOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberInVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TeamMembersExcelVO;
import org.phoenixctms.ctsms.vo.TimelineEventInVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TrialInVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialTagValueInVO;
import org.phoenixctms.ctsms.vo.TrialTagValueOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;
import org.phoenixctms.ctsms.vo.VisitInVO;
import org.phoenixctms.ctsms.vo.VisitOutVO;
import org.phoenixctms.ctsms.vo.VisitScheduleExcelVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemInVO;
import org.phoenixctms.ctsms.vo.VisitScheduleItemOutVO;

/**
 * @see org.phoenixctms.ctsms.service.trial.TrialService
 */
public class TrialServiceImpl
extends TrialServiceBase
{

	private final static String SHUFFLE_SEED_RANDOM_ALGORITHM = "SHA1PRNG";
	private final static java.util.regex.Pattern JS_VARIABLE_NAME_REGEXP = Pattern.compile("^[A-Za-z0-9_]+$");

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
		return journalEntryDao.addSystemMessage(course, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.COURSE_JOURNAL, null)) });
	}

	// private static JournalEntry logSystemMessage(Proband proband, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
	// JournalEntryDao journalEntryDao) throws Exception {
	// return journalEntryDao.addSystemMessage(proband, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) }, systemMessageCode,
	// new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null)) });
	// }

	private static JournalEntry logSystemMessage(Inventory inventory, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(inventory, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) }, systemMessageCode,
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.INVENTORY_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(User user, TrialOutVO trialVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.trialOutVOToString(trialVO) }, systemMessageCode,
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

	private ECRFFieldStatusEntryOutVO addEcrfFieldStatusEntry(ECRFFieldStatusEntryInVO newEcrfFieldStatusEntry, ECRFFieldStatusQueue queue, Timestamp now, User user,
			boolean logTrial,
			boolean logProband, boolean action



			//ProbandDao probandDao, ProbandListEntryDao probandListEntryDao, ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao, ProbandListStatusTypeDao probandListStatusTypeDao,
			//JournalEntryDao journalEntryDao
			) throws Exception {

		ECRFFieldStatusEntry lastStatus = checkAddEcrfFieldStatusEntryInput(newEcrfFieldStatusEntry, queue, now, user, action);
		// ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();

		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ECRFFieldStatusEntry fieldStatus = ecrfFieldStatusEntryDao.eCRFFieldStatusEntryInVOToEntity(newEcrfFieldStatusEntry);
		fieldStatus.setQueue(queue);

		CoreUtil.modifyVersion(fieldStatus, now, user);
		fieldStatus = ecrfFieldStatusEntryDao.create(fieldStatus);

		notifyEcrfFieldStatus(lastStatus,fieldStatus,queue,now);

		ECRFFieldStatusEntryOutVO result = ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVO(fieldStatus);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		if (logProband) {
			ServiceUtil.logSystemMessage(fieldStatus.getListEntry().getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_STATUS_ENTRY_CREATED,
					result, null, journalEntryDao);
		}
		if (logTrial) {
			// ServiceUtil.logSystemMessage(fieldStatus.getListEntry().getTrial(), result.getListEntry().getTrial(), now, user,
			// SystemMessageCodes.ECRF_FIELD_STATUS_ENTRY_CREATED, result, null, journalEntryDao);
			ServiceUtil.logSystemMessage(fieldStatus.getListEntry().getTrial(), result.getListEntry().getProband(), now, user,
					SystemMessageCodes.ECRF_FIELD_STATUS_ENTRY_CREATED, result, null, journalEntryDao);
		}
		return result;
	}

	private Object[] addEcrfStatusEntry(ECRF ecrf, ProbandListEntry listEntry, ECRFStatusType statusType, Long probandListStatusTypeId, Timestamp now, User user
			)
					throws Exception {
		// ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		// JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		checkAddEcrfStatusEntry(ecrf, listEntry, statusType, user);
		ECRFStatusEntry statusEntry = ECRFStatusEntry.Factory.newInstance();
		statusEntry.setExportStatus(ExportStatus.NOT_EXPORTED);
		statusEntry.setValidationStatus(ECRFValidationStatus.NOT_VALIDATED);
		statusEntry.setStatus(statusType);
		statusEntry.setEcrf(ecrf);
		ecrf.addEcrfStatusEntries(statusEntry);
		statusEntry.setListEntry(listEntry);
		listEntry.addEcrfStatusEntries(statusEntry);
		CoreUtil.modifyVersion(statusEntry, now, user);
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		statusEntry = ecrfStatusEntryDao.create(statusEntry);
		execEcrfStatusActions(null, statusEntry, probandListStatusTypeId, now, user);
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

	private ProbandListEntryOutVO addProbandListEntry(ProbandListEntryInVO newProbandListEntry, boolean signup, // ProbandListStatusType statusType,
			Timestamp now, User user) throws Exception {
		checkProbandListEntryInput(newProbandListEntry, signup, now);
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ProbandDao probandDao = this.getProbandDao();
		ProbandListEntry listEntry = probandListEntryDao.probandListEntryInVOToEntity(newProbandListEntry);
		Proband proband = listEntry.getProband();
		ProbandListStatusType statusType = this.getProbandListStatusTypeDao().findInitialStates(signup, proband.isPerson()).iterator().next();
		Trial trial = listEntry.getTrial();
		listEntry.setExportStatus(ExportStatus.NOT_EXPORTED);
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
		ProbandListStatusEntryOutVO probandListStatusEntryVO = null;
		if (statusType != null && statusType.isInitial()) { // && !statusType.isReasonRequired()) {
			if (statusType.isSignup() && !trial.isSignupProbandList()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_SIGNUP_DISABLED);
			}
			String reason = (listEntry.getGroup() != null ?
					L10nUtil.getProbandListStatusReason(Locales.PROBAND_LIST_STATUS_ENTRY_REASON, ProbandListStatusReasonCodes.LIST_ENTRY_CREATED,
							DefaultProbandListStatusReasons.LIST_ENTRY_CREATED, new Object[] { CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)),
							listEntry.getGroup().getTitle() }) :
								L10nUtil.getProbandListStatusReason(Locales.PROBAND_LIST_STATUS_ENTRY_REASON, ProbandListStatusReasonCodes.LIST_ENTRY_CREATED_NO_GROUP,
										DefaultProbandListStatusReasons.LIST_ENTRY_CREATED_NO_GROUP, new Object[] { CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)) }));
			if (!statusType.isReasonRequired() || !CommonUtil.isEmptyString(reason)) {
				ProbandListStatusEntry statusEntry = ProbandListStatusEntry.Factory.newInstance();
				statusEntry.setListEntry(listEntry);
				listEntry.addStatusEntries(statusEntry);
				CipherText cipherText = CryptoUtil.encryptValue(reason);
				statusEntry.setReasonIv(cipherText.getIv());
				statusEntry.setEncryptedReason(cipherText.getCipherText());
				statusEntry.setReasonHash(CryptoUtil.hashForSearch(reason));
				statusEntry.setRealTimestamp(now);
				statusEntry.setStatus(statusType);
				CoreUtil.modifyVersion(statusEntry, now, user);
				ProbandListStatusEntryDao probandListStatusEntryDao = this.getProbandListStatusEntryDao();
				statusEntry = probandListStatusEntryDao.create(statusEntry);
				listEntry.setLastStatus(statusEntry);
				probandListEntryDao.update(listEntry);
				probandListStatusEntryVO = probandListStatusEntryDao.toProbandListStatusEntryOutVO(statusEntry);
			}
		}
		ProbandListEntryOutVO result = probandListEntryDao.toProbandListEntryOutVO(listEntry);
		ServiceUtil.logSystemMessage(proband, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_CREATED, result, null, journalEntryDao);
		// ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_CREATED, result, null, journalEntryDao);
		ServiceUtil.logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_CREATED, result, null, journalEntryDao);
		if (probandListStatusEntryVO != null) {
			ServiceUtil.logSystemMessage(proband, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_STATUS_ENTRY_CREATED, probandListStatusEntryVO, null,
					journalEntryDao);
			// ServiceUtil
			// .logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_STATUS_ENTRY_CREATED, probandListStatusEntryVO, null, journalEntryDao);
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
		if (group != null) {
			args = new Object[] {
					CommonUtil.probandOutVOToString(this.getProbandDao().toProbandOutVO(probandListEntry.getProband())),
					group.getTitle()
			};
			l10nKey = reasonL10nKey;
		} else {
			args = new Object[] {
					CommonUtil.probandOutVOToString(this.getProbandDao().toProbandOutVO(probandListEntry.getProband()))
			};
			l10nKey = reasonNoGroupL10nKey;
		}
		return ServiceUtil.addProbandListStatusEntry(probandListEntry, null, l10nKey, args, now, probandListStatusTypeId, now, user,
				this.getProbandDao(),
				this.getProbandListEntryDao(),
				this.getProbandListStatusEntryDao(),
				this.getProbandListStatusTypeDao(),
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
			boolean logTrial,
			boolean logProband,
			LinkedHashMap<String, LinkedHashSet<Long>> sectionIndexMap, ArrayList<ECRFFieldValueOutVO> outEcrfFieldValues, ArrayList<ECRFFieldValueJsonVO> outJsEcrfFieldValues)
					throws Exception {
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		Long id = ecrfFieldValueIn.getId();
		// ECRFFieldValue ecrfFieldValue;
		ECRFFieldValueOutVO result = null;
		ECRFFieldValueJsonVO resultJs = null;
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		// ECRFFieldOutVO ecrfFieldVO = this.getECRFFieldDao().toECRFFieldOutVO(ecrfField);
		checkEcrfFieldValueIndex(ecrfField, ecrfFieldValueIn.getListEntryId(), ecrfFieldValueIn.getEcrfFieldId(), ecrfFieldValueIn.getIndex()); // , ecrfFieldVO);

		ProbandListEntry listEntry = ecrfStatusEntry.getListEntry();
		boolean isAuditTrail = ecrfField.isAuditTrail() && ecrfStatusEntry.getStatus().isAuditTrail();
		if (id == null) {
			// if (!ecrfField.isDisabled() || ecrfField.isSeries()) {
			if (ecrfField.isDisabled()) {
				ecrfFieldValueIn = ServiceUtil.createPresetEcrfFieldInValue(ecrfField, listEntry.getId(), ecrfFieldValueIn.getIndex(), this.getInputFieldSelectionSetValueDao());
			}
			checkEcrfFieldValueInputUnlockedForFieldStatus(ecrfFieldValueIn, ecrfStatusEntry, ecrfField);
			checkEcrfFieldValueInput(ecrfFieldValueIn, ecrfStatusEntry, ecrfField); // , ecrfFieldVO);
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
			// ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getEcrfField().getTrial(), now, user, SystemMessageCodes.ECRF_FIELD_VALUE_CREATED, result, null,
			// journalEntryDao);
			if (logTrial) {
				ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.ECRF_FIELD_VALUE_CREATED, result, null,
						journalEntryDao);
			}
			// } else {
			// result = ServiceUtil.createPresetEcrfFieldOutValue(ecrfFieldVO, listEntryVO, ecrfFieldValueIn.getIndex());
			// if (outJsEcrfFieldValues != null && !CommonUtil.isEmptyString(ecrfField.getJsVariableName())) {
			// resultJs = ServiceUtil.createPresetEcrfFieldJsonValue(ecrfField, ecrfFieldValueIn.getIndex(), this.getInputFieldSelectionSetValueDao());
			// }
			// }
		} else {
			ECRFFieldValue originalEcrfFieldValue = null;
			if (!CheckIDUtil.checkEcrfFieldValueId(id, ecrfFieldValueDao).equals(
					originalEcrfFieldValue = ecrfFieldValueDao.getByListEntryEcrfFieldIndex(ecrfFieldValueIn.getListEntryId(), ecrfFieldValueIn.getEcrfFieldId(),
							ecrfFieldValueIn.getIndex()))) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ENTITY_WAS_MODIFIED_SINCE, originalEcrfFieldValue.getModifiedUser().getName());
			}
			if (!ecrfField.isDisabled()) {
				checkEcrfFieldValueInputUnlockedForFieldStatus(ecrfFieldValueIn, ecrfStatusEntry, ecrfField);
				checkEcrfFieldValueInput(ecrfFieldValueIn, ecrfStatusEntry, ecrfField); // , ecrfFieldVO); // access original associations before evict
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
				CoreUtil.modifyVersion(originalEcrfFieldValue, ecrfFieldValue, now, user);
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
		// if (sectionIndexMap != null && ecrfFieldValueIn.getIndex() != null) {
		// if (sectionIndexMap.containsKey(result.getEcrfField().getSection())) {
		// sectionIndexMap.get(result.getEcrfField().getSection()).add(ecrfFieldValueIn.getIndex());
		// } else {
		// LinkedHashSet<Long> indexes = new LinkedHashSet<Long>();
		// indexes.add(ecrfFieldValueIn.getIndex());
		// sectionIndexMap.put(result.getEcrfField().getSection(), indexes);
		// }
		// }
		if (outEcrfFieldValues != null) {
			outEcrfFieldValues.add(result);
		}
		if (resultJs != null) {
			outJsEcrfFieldValues.add(resultJs);
		}
	}

	private void addUpdateProbandListEntryTagValue(ProbandListEntryTagValueInVO probandListEntryTagValueIn, ProbandListEntry listEntry, ProbandListEntryTag listEntryTag,
			Timestamp now, User user, boolean logTrial,
			boolean logProband, ArrayList<ProbandListEntryTagValueOutVO> tagValues, ArrayList<ProbandListEntryTagValueJsonVO> jsTagValues) throws Exception {
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		Long id = probandListEntryTagValueIn.getId();
		// ProbandListEntryTagValue listEntryTagValue;
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
			// ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getTag().getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_CREATED, result, null,
			// journalEntryDao);
			if (logTrial) {
				ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_CREATED, result,
						null,
						journalEntryDao);
			}
		} else {
			ProbandListEntryTagValue originalListEntryTagValue = CheckIDUtil.checkProbandListEntryTagValueId(id, probandListEntryTagValueDao);
			if (!listEntryTag.isDisabled()) {
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
				// ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getTag().getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_UPDATED, result,
				// original,
				// journalEntryDao);
				if (logTrial) {
					ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_UPDATED, result,
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
		ProbandListEntryOutVO listEntryVO = this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry);
		if (!listEntryVO.getProband().getDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND);
		}
		UserOutVO userVO = this.getUserDao().toUserOutVO(user);
		Collection visitScheduleItems = null;
		Collection probandGroups = null;
		if (listEntry.getGroup() != null) {
			VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
			visitScheduleItems = visitScheduleItemDao.findByTrialGroupVisitProbandTravel(listEntry.getTrial().getId(), listEntry.getGroup().getId(), null, listEntry.getProband().getId(), null, null);
			visitScheduleItemDao.toVisitScheduleItemOutVOCollection(visitScheduleItems);
		} else {
			ProbandGroupDao probandGroupDao = this.getProbandGroupDao();
			probandGroups = probandGroupDao.findByTrial(listEntry.getTrial().getId(), null);
			probandGroupDao.toProbandGroupOutVOCollection(probandGroups);
		}
		// ProbandAddressDao addressDao = this.getProbandAddressDao();
		// Collection probandAddresses = addressDao.findByProband(listEntry.getProband().getId(), null, null, null, null);
		// addressDao.toProbandAddressOutVOCollection(probandAddresses);
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		Collection<ProbandListEntryTagValueJsonVO> probandListEntryTagValues = ServiceUtil.getProbandListEntryTagJsonValues(
				probandListEntryTagValueDao.findByListEntryJs(listEntryVO.getId(), true, null, null),
				false, probandListEntryTagValueDao, inputFieldSelectionSetValueDao);

		HashMap<String, Long> maxSeriesIndexMap = null;
		HashMap<String, Long> fieldMaxPositionMap = null;
		HashMap<String, Long> fieldMinPositionMap = null;
		HashMap<String, Set<ECRFField>> seriesEcrfFieldMap = null;
		// HashMap<String, Set<ECRFField>> seriesEcrfFieldJsMap = null;
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		if (addSeries) {
			maxSeriesIndexMap = new HashMap<String, Long>();
			fieldMaxPositionMap = new HashMap<String, Long>();
			fieldMinPositionMap = new HashMap<String, Long>();
			seriesEcrfFieldMap = new HashMap<String, Set<ECRFField>>();
			// seriesEcrfFieldJsMap = new HashMap<String, Set<ECRFField>>();
			ServiceUtil.initSeriesEcrfFieldMaps(
					this.getECRFFieldDao().findByTrialEcrfSeriesJs(null, ecrf.getId(), true, true, true, null),
					listEntryVO.getId(),
					ecrf.getId(),
					maxSeriesIndexMap,
					fieldMaxPositionMap,
					fieldMinPositionMap,
					seriesEcrfFieldMap,
					// seriesEcrfFieldJsMap,
					ecrfFieldValueDao
					);
		}
		// Collection values = ecrfFieldValueDao.findByListEntryEcrfJs(listEntry.getId(), ecrf.getId(), true, null, null);
		Collection<ECRFFieldValueJsonVO> jsValues = ServiceUtil.getEcrfFieldJsonValues(ecrfFieldValueDao.findByListEntryEcrfJs(listEntry.getId(), ecrf.getId(), true, true, null),
				maxSeriesIndexMap, fieldMaxPositionMap, fieldMinPositionMap, seriesEcrfFieldMap,
				false, ecrfFieldValueDao, inputFieldSelectionSetValueDao);
		// ecrfFieldValueDao.toECRFFieldValueJsonVOCollection(jsValues);

		FieldCalculation fieldCalculation = new FieldCalculation();
		// fieldCalculation.setProband(listEntryVO.getProband());
		// fieldCalculation.setTrial(listEntryVO.getTrial());
		fieldCalculation.setProbandListEntry(listEntryVO);
		fieldCalculation.setActiveUser(userVO);
		fieldCalculation.setLocale(Locales.AUDIT_TRAIL);
		// fieldCalculation.setProbandAddresses(probandAddresses);
		fieldCalculation.setProbandListEntryTagValues(probandListEntryTagValues);
		fieldCalculation.setVisitScheduleItems(visitScheduleItems);
		fieldCalculation.setProbandGroups(probandGroups);
		fieldCalculation.setECRFFieldInputFieldVariableValues(jsValues);

		Exception scriptException = null;
		// ValidationError firstError = null;
		int errorCount = 0;
		HashMap<Long, HashMap<Long, ValidationError>> validationErrorMap = new HashMap<Long, HashMap<Long, ValidationError>>();
		try {
			Iterator<ValidationError> it = fieldCalculation.initInputFieldVariables().iterator();
			while (it.hasNext()) {
				ValidationError msg = it.next();
				// if (firstError == null) {
				// firstError = msg;
				// }
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

		Iterator<Map> ecrfFieldValuesIt = ecrfFieldValueDao.findByListEntryEcrfJs(listEntry.getId(), ecrf.getId(), true, null, null).iterator();
		while (ecrfFieldValuesIt.hasNext()) { // && (maxMissingCount == null || missingCount < maxMissingCount)) {
			Map<String, Object> entities = (Map<String, Object>) ecrfFieldValuesIt.next();
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
			newEcrfFieldStatusEntry.setEcrfFieldId(ecrfField.getId());
			newEcrfFieldStatusEntry.setListEntryId(listEntry.getId());
			newEcrfFieldStatusEntry.setIndex(index);
			ValidationError msg;
			HashMap<Long, ValidationError> indexErrorMap;
			if (scriptException != null) {
				ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(ECRFFieldStatusQueue.VALIDATION, listEntry.getId(),
						ecrfField.getId(), index);
				if (lastStatus == null || lastStatus.getStatus().getTransitions().contains(this.getECRFFieldStatusTypeDao().getValidationFailed())) {
					newEcrfFieldStatusEntry.setStatusId(this.getECRFFieldStatusTypeDao().getValidationFailed().getId());
					newEcrfFieldStatusEntry.setComment(scriptException.getMessage());
				} else {
					continue;
				}
			} else if ((indexErrorMap = validationErrorMap.get(ecrfField.getId())) != null && (msg = indexErrorMap.get(index)) != null) {
				ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(ECRFFieldStatusQueue.VALIDATION, listEntry.getId(),
						ecrfField.getId(), index);
				if (lastStatus == null || lastStatus.getStatus().getTransitions().contains(this.getECRFFieldStatusTypeDao().getValidationError())) {
					newEcrfFieldStatusEntry.setStatusId(this.getECRFFieldStatusTypeDao().getValidationError().getId());
					newEcrfFieldStatusEntry.setComment(msg.getOutput());
				} else {
					continue;
				}
			} else {
				ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(ECRFFieldStatusQueue.VALIDATION, listEntry.getId(),
						ecrfField.getId(), index);
				if (lastStatus != null && lastStatus.getStatus().getTransitions().contains(this.getECRFFieldStatusTypeDao().getValidationSuccess())) { // ) {
					// or write always?
					newEcrfFieldStatusEntry.setStatusId(this.getECRFFieldStatusTypeDao().getValidationSuccess().getId());
				} else {
					continue;
				}
			}
			addEcrfFieldStatusEntry(newEcrfFieldStatusEntry, ECRFFieldStatusQueue.VALIDATION, now, user, false, false, true);
		}
		// if (statusEntry != null) {
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
		// }

		return errorCount;
	}



	private void checkAddEcrfFieldInput(ECRFFieldInVO ecrfFieldIn) throws ServiceException
	{
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

	private ECRFFieldStatusEntry checkAddEcrfFieldStatusEntryInput(ECRFFieldStatusEntryInVO ecrfFieldStatusEntryIn, ECRFFieldStatusQueue queue, Timestamp now, User user, boolean action)
			throws Exception
			{
		// referential checks
		// ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(ecrfFieldStatusEntryIn.getListEntryId(), this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
		ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldStatusEntryIn.getEcrfFieldId(), this.getECRFFieldDao());
		ECRFFieldStatusType state = CheckIDUtil.checkEcrfFieldStatusTypeId(ecrfFieldStatusEntryIn.getStatusId(), this.getECRFFieldStatusTypeDao());
		if (!listEntry.getTrial().equals(ecrfField.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_STATUS_ENTRY_FOR_WRONG_TRIAL);
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

		ECRFStatusEntry ecrfStatusEntry = this.getECRFStatusEntryDao().findByEcrfListEntry(ecrf.getId(), listEntry.getId());
		if (ecrfStatusEntry == null) {
			ECRFStatusType statusType = this.getECRFStatusTypeDao().findInitialStates().iterator().next();
			this.getECRFDao().lock(ecrf, LockMode.PESSIMISTIC_WRITE);
			// ecrf = ServiceUtil.checkEcrfId(ecrf.getId(), this.getECRFDao(), LockMode.PESSIMISTIC_WRITE); // lock order, field updates
			Object[] resultItems = addEcrfStatusEntry(ecrf, listEntry, statusType, null, now, user);
			ecrfStatusEntry = (ECRFStatusEntry) resultItems[0];
			//			statusEntryVO = (ECRFStatusEntryVO) resultItems[1];
			//		} else {
			//			statusEntryVO = ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry);
		}
		if (ecrfStatusEntry.getStatus().isFieldStatusLockdown()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_INPUT_LOCKED_FOR_ECRF_STATUS,
					L10nUtil.getEcrfStatusTypeName(Locales.USER, ecrfStatusEntry.getStatus().getNameL10nKey()));
		}
		if (listEntry.getLastStatus() != null && !listEntry.getLastStatus().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
					L10nUtil.getProbandListStatusTypeName(Locales.USER, listEntry.getLastStatus().getStatus().getNameL10nKey()));
		}

		checkEcrfFieldStatusIndex(ecrfField, listEntry.getId(), ecrfField.getId(), ecrfFieldStatusEntryIn.getIndex());
		// InputFieldDao inputFieldDao = this.getInputFieldDao();
		// if (!ecrfField.isSeries()) {
		// if (ecrfFieldStatusEntryIn.getIndex() != null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_NOT_NULL,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// } else {
		// if (ecrfFieldStatusEntryIn.getIndex() == null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_NULL,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(listEntry.getId(), ecrfField.getId());
		// if (maxIndex == null) {
		// if (!ecrfFieldStatusEntryIn.getIndex().equals(0l)) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_NOT_ZERO,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// } else {
		// if (ecrfFieldStatusEntryIn.getIndex() < 0l) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_LESS_THAN_ZERO,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// } else if (ecrfFieldStatusEntryIn.getIndex() > (maxIndex + 1l)) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_GAP,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())), maxIndex + 1l);
		// }
		// }
		// }


		if (!action
				&& (state.isResolved() && Settings.getEcrfFieldStatusQueueList(SettingCodes.RESOLVE_ECRF_FIELD_STATUS_RESTRICTION_QUEUES, Bundle.SETTINGS,
						DefaultSettings.RESOLVE_ECRF_FIELD_STATUS_RESTRICTION_QUEUES).contains(state.getQueue()))) { // !state.isInitial() &&
			checkTeamMemberResolve(listEntry.getTrial(), user);
		}

		ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(queue, listEntry.getId(), ecrfField.getId(), ecrfFieldStatusEntryIn.getIndex());
		// ProbandListStatusTypeDao probandListStatusTypeDao = this.getProbandListStatusTypeDao();

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
			//			if (ecrfFieldStatusEntryIn.get.get.getRealTimestamp().compareTo(lastStatus.getRealTimestamp()) < 0) {
			//				throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_STATUS_REAL_DATE_LESS_THAN_LAST_DATE);
			//			}
		}
		String comment = ecrfFieldStatusEntryIn.getComment();
		if (CommonUtil.isEmptyString(comment) && state.isCommentRequired()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_COMMENT_REQUIRED);
		}
		//		if ((new ProbandListStatusEntryCollisionFinder(probandDao, probandListEntryDao, probandListStatusEntryDao)).collides(probandListStatusEntryIn)) {
		//			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_PROBAND_BLOCKED,
		//					CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(probandListEntry.getProband())));
		//		}
		return lastStatus;
			}


	private void checkAddEcrfInput(ECRFInVO ecrfIn) throws ServiceException {
		checkEcrfInput(ecrfIn);
	}

	private void checkAddEcrfStatusEntry(ECRF ecrf, ProbandListEntry listEntry, ECRFStatusType statusType, User user) throws ServiceException
	{
		if (!ecrf.getTrial().equals(listEntry.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_STATUS_ENTRY_DIFFERENT_TRIALS); // , statusType.getNameL10nKey());
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
		//ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
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

	private void checkAddInquiryInput(InquiryInVO inquiryIn) throws ServiceException
	{
		InputField field = CheckIDUtil.checkInputFieldId(inquiryIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(inquiryIn.getTrialId(), this.getTrialDao());
		ServiceUtil.checkTrialLocked(trial);
		checkInquiryInput(inquiryIn);
	}

	private void checkAddProbandListEntryTagInput(ProbandListEntryTagInVO listTagIn) throws ServiceException
	{
		InputField field = CheckIDUtil.checkInputFieldId(listTagIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(listTagIn.getTrialId(), this.getTrialDao());
		ServiceUtil.checkTrialLocked(trial);
		checkProbandListEntryTagInput(listTagIn);
	}

	private void checkAddTrialInput(TrialInVO trialIn) throws ServiceException
	{
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
		// //entering signed:
		// if (hasTrialStatusAction(state, org.phoenixctms.ctsms.enumeration.TrialStatusAction.SIGN_TRIAL)) {
		// checkTeamMemberSign(trial, user);
		// }
	}

	private ProbandListEntry checkClearEcrfFieldValues(Long ecrfId, Long probandListEntryId, Timestamp now, User user) throws Exception {
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());

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
		ECRFStatusEntry statusEntry = this.getECRFStatusEntryDao().findByEcrfListEntry(ecrf.getId(), listEntry.getId());
		if (statusEntry == null) {
			ECRFStatusType statusType = this.getECRFStatusTypeDao().findInitialStates().iterator().next();
			this.getECRFDao().lock(ecrf, LockMode.PESSIMISTIC_WRITE);
			// ecrf = ServiceUtil.checkEcrfId(ecrf.getId(), this.getECRFDao(), LockMode.PESSIMISTIC_WRITE); // lock order, field updates
			Object[] resultItems = addEcrfStatusEntry(ecrf, listEntry, statusType, null, now, user);
			statusEntry = (ECRFStatusEntry) resultItems[0];
			//			statusEntryVO = (ECRFStatusEntryVO) resultItems[1];
			//		} else {
			//			statusEntryVO = this.getECRFStatusEntryDao().toECRFStatusEntryVO(statusEntry);
		}
		//listEntryVO = statusEntryVO.getListEntry();
		if (statusEntry.getStatus().isValueLockdown()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_LOCKED_FOR_ECRF_STATUS,
					L10nUtil.getEcrfStatusTypeName(Locales.USER, statusEntry.getStatus().getNameL10nKey()));
		}
		if (statusEntry.getStatus().isAuditTrail()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_AUDIT_TRAIL_ENABLED_FOR_ECRF_STATUS,
					L10nUtil.getEcrfStatusTypeName(Locales.USER, statusEntry.getStatus().getNameL10nKey()));
		}
		if (listEntry.getLastStatus() != null && !listEntry.getLastStatus().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
					L10nUtil.getProbandListStatusTypeName(Locales.USER, listEntry.getLastStatus().getStatus().getNameL10nKey()));
		}
		return listEntry;
	}

	private void checkEcrfFieldInput(ECRFFieldInVO ecrfFieldIn) throws ServiceException {
		if (CommonUtil.isEmptyString(ecrfFieldIn.getJsVariableName())) {
			if (!CommonUtil.isEmptyString(JavaScriptCompressor.compress(ecrfFieldIn.getJsValueExpression()))) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_JS_VARIABLE_NAME_REQUIRED);
			}
			if (!CommonUtil.isEmptyString(JavaScriptCompressor.compress(ecrfFieldIn.getJsOutputExpression()))) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_JS_VARIABLE_NAME_REQUIRED);
			}
		} else {
			if (!JS_VARIABLE_NAME_REGEXP.matcher(ecrfFieldIn.getJsVariableName()).find()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_JS_VARIABLE_NAME_INVALID, ecrfFieldIn.getJsVariableName());
			}
			if ((new EcrfFieldJsVariableNameCollisionFinder(this.getECRFDao(), this.getECRFFieldDao())).collides(ecrfFieldIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_JS_VARIABLE_NAME_NOT_UNIQUE);
			}
		}
		if (ecrfFieldIn.getReasonForChangeRequired() && !ecrfFieldIn.getAuditTrail()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_AUDIT_TRAIL_FALSE);
		}
		if ((new EcrfFieldSeriesCollisionFinder(this.getECRFDao(), this.getECRFFieldDao())).collides(ecrfFieldIn)) {
			// all fields within section must have same "series" flag
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_FLAG_INCONSISTENT);
		}
		if ((new EcrfFieldPositionCollisionFinder(this.getECRFDao(), this.getECRFFieldDao())).collides(ecrfFieldIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_POSITION_NOT_UNIQUE);
		}
		// long valuesLockedEcrfCount = this.getECRFStatusEntryDao().getCount(null, ecrfFieldIn.getEcrfId(), null, true, null, null, null); // row lock order
		// if (valuesLockedEcrfCount > 0) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.LOCKED_ECRFS, this.getECRFDao().toECRFOutVO(this.getECRFDao().load(ecrfFieldIn.getEcrfId())).getName(),
		// valuesLockedEcrfCount);
		// }
	}

	// private void checkEcrfFieldValueInputIndex(ECRFFieldValueInVO ecrfFieldValueIn, ECRFField ecrfField) throws ServiceException { // , ECRFFieldOutVO ecrfFieldVO
	// // InputFieldOutVO inputFieldVO = ecrfFieldVO.getField();
	// InputFieldDao inputFieldDao = this.getInputFieldDao();
	// if (!ecrfField.isSeries()) {
	// if (ecrfFieldValueIn.getIndex() != null) {
	// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NOT_NULL,
	// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
	// }
	// } else {
	// if (ecrfFieldValueIn.getIndex() == null) {
	// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NULL,
	// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
	// }
	// Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(ecrfFieldValueIn.getListEntryId(), ecrfFieldValueIn.getEcrfFieldId());
	// if (maxIndex == null) {
	// if (!ecrfFieldValueIn.getIndex().equals(0l)) {
	// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NOT_ZERO,
	// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
	// }
	// } else {
	// if (ecrfFieldValueIn.getIndex() < 0l) {
	// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_LESS_THAN_ZERO,
	// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
	// } else if (ecrfFieldValueIn.getIndex() > (maxIndex + 1l)) {
	// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_GAP,
	// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())), maxIndex + 1l);
	// }
	// }
	// }
	// }

	private  void checkEcrfFieldStatusIndex(ECRFField ecrfField, Long probandListEntryId,Long ecrfFieldId, Long index) throws ServiceException {
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
			Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(probandListEntryId, ecrfFieldId);
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
		// InputFieldDao inputFieldDao = this.getInputFieldDao();
		// if (!ecrfField.isSeries()) {
		// if (index != null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_NOT_NULL,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// // allow creating field status even w/o saved value
		// } else {
		// if (index == null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_NULL,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// if (index < 0l) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_INDEX_LESS_THAN_ZERO,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// ECRFFieldValue value = this.getECRFFieldValueDao().getByListEntryEcrfFieldIndex(probandListEntryId, ecrfFieldId, index);
		// if (value == null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_NO_VALUE,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// }
	}

	private void checkEcrfFieldValueIndex(ECRFField ecrfField, Long probandListEntryId, Long ecrfFieldId, Long index) throws ServiceException {
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
			Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(probandListEntryId, ecrfFieldId);
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

	private void checkEcrfFieldValueInput(ECRFFieldValueInVO ecrfFieldValueIn, ECRFStatusEntry ecrfStatusEntry, ECRFField ecrfField) throws ServiceException
	{
		// ProbandListEntry listEntry
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = ecrfField.getField();
		inputFieldDao.lock(inputField, LockMode.PESSIMISTIC_WRITE);
		// InputField inputField = ServiceUtil.checkInputFieldId(ecrfField.getField().getId(), inputFieldDao, LockMode.PESSIMISTIC_WRITE);
		// InputFieldOutVO inputFieldVO = ecrfFieldVO.getField();
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
		// (ecrfFieldValueIn.getId() == null || !ecrfField.isAuditTrail()) &&
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

	private void checkEcrfFieldValueInputUnlockedForFieldStatus(ECRFFieldValueInVO ecrfFieldValueIn, ECRFStatusEntry statusEntry, ECRFField ecrfField) throws Exception {
		if (statusEntry.getStatus().isValueLockdown()) {
			// ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(null, listEntry.getId(), ecrfField.getId(), index);
			// if (lastStatus != null) {
			if (ecrfFieldValueIn.getId() == null && ecrfField.isSeries()
					&& getEcrfSectionUnlockValue(statusEntry.getListEntry().getId(), ecrfFieldValueIn.getIndex(), ecrfField.getEcrf().getId(), ecrfField.getSection())) {
				return;
			} else {
				ECRFFieldStatusQueue[] queues = ECRFFieldStatusQueue.values();
				for (int i = 0; i < queues.length; i++) {
					ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(queues[i], statusEntry.getListEntry().getId(), ecrfField.getId(),
							ecrfFieldValueIn.getIndex());
					if (lastStatus != null && lastStatus.getStatus().isUnlockValue()) {
						return;
					}
				}
				// }
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_LOCKED_FOR_ECRF_STATUS,
						L10nUtil.getEcrfStatusTypeName(Locales.USER, statusEntry.getStatus().getNameL10nKey()));
			}
		}
	}

	private void checkEcrfInput(ECRFInVO ecrfIn) throws ServiceException {
		Trial trial = CheckIDUtil.checkTrialId(ecrfIn.getTrialId(), this.getTrialDao());
		ProbandGroup group = null;
		if (ecrfIn.getGroupId() != null) {
			group = CheckIDUtil.checkProbandGroupId(ecrfIn.getGroupId(), this.getProbandGroupDao());
			if (!trial.equals(group.getTrial())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_WRONG_PROBAND_GROUP, CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
			}
		}
		Visit visit = null;
		if (ecrfIn.getVisitId() != null) {
			visit = CheckIDUtil.checkVisitId(ecrfIn.getVisitId(), this.getVisitDao());
			if (!trial.equals(visit.getTrial())) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_WRONG_VISIT, CommonUtil.trialOutVOToString(this.getTrialDao().toTrialOutVO(trial)));
			}
		}
		if (ecrfIn.getProbandListStatusId() != null) {
			CheckIDUtil.checkProbandListStatusTypeId(ecrfIn.getProbandListStatusId(), this.getProbandListStatusTypeDao());
		}
		ServiceUtil.checkTrialLocked(trial);
		if (Settings.getBoolean(SettingCodes.UNIQUE_ECRF_NAMES, Bundle.SETTINGS, DefaultSettings.UNIQUE_ECRF_NAMES)) {
			if ((new EcrfNameCollisionFinder(this.getTrialDao(), this.getECRFDao())).collides(ecrfIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_NAME_NOT_UNIQUE);
			}
		}
		if ((new EcrfPositionCollisionFinder(this.getTrialDao(), this.getECRFDao())).collides(ecrfIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_POSITION_NOT_UNIQUE);
		}
	}

	private ECRFStatusEntry checkEcrfStatusEntry(Long ecrfId, Long probandListEntryId) throws ServiceException {
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		ECRFStatusEntry statusEntry = this.getECRFStatusEntryDao().findByEcrfListEntry(ecrfId, probandListEntryId);
		if (statusEntry == null) {
			ProbandListEntryOutVO listEntryVO = this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry);
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_ECRF_STATUS_ENTRY, this.getECRFDao().toECRFOutVO(ecrf).getUniqueName(),
					CommonUtil.trialOutVOToString(listEntryVO.getTrial()), Long.toString(listEntryVO.getProband().getId())); // CommonUtil.probandOutVOToString(listEntryVO.getProband()));
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

	private void checkMissingEcrfFieldValues(ProbandListEntry listEntry, ECRF ecrf) throws Exception {
		ECRFProgressVO ecrfProgress = ServiceUtil.populateEcrfProgress(this.getECRFDao().toECRFOutVO(ecrf), this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry),
				false,
				this.getECRFStatusEntryDao(), this.getECRFStatusTypeDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao());
		if (ecrfProgress.getMandatoryFieldCount() > ecrfProgress.getMandatorySavedValueCount()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_MISSING, ecrfProgress.getMandatorySavedValueCount(), ecrfProgress.getMandatoryFieldCount());
		}
	}

	private void checkMissingEcrfFieldValuesDeeply(ProbandListEntry listEntry, ECRF ecrf) throws Exception {
		checkMissingEcrfFieldValues(listEntry, ecrf);
		// ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		Integer maxMissingCount = Settings.getIntNullable(SettingCodes.MAX_MISSING_ECRF_FIELD_VALUES, Bundle.SETTINGS, DefaultSettings.MAX_MISSING_ECRF_FIELD_VALUES);
		if (maxMissingCount == null || maxMissingCount > 0) {
			ServiceException firstException = null;
			HashMap<Long, HashMap<Long, String>> errorMessagesMap = new HashMap<Long, HashMap<Long, String>>();
			int missingCount = 0;
			InputFieldDao inputFieldDao = this.getInputFieldDao();
			Iterator<Map> ecrfFieldValuesIt = this.getECRFFieldValueDao().findByListEntryEcrfJs(listEntry.getId(), ecrf.getId(), true, null, null).iterator();
			while (ecrfFieldValuesIt.hasNext() && (maxMissingCount == null || missingCount < maxMissingCount)) {
				Map<String, Object> entities = (Map<String, Object>) ecrfFieldValuesIt.next();
				ECRFFieldValue ecrfFieldValue = (ECRFFieldValue) entities.get(ServiceUtil.ECRF_FIELD_VALUE_DAO_ECRF_FIELD_VALUE_ALIAS);
				ECRFField ecrfField;
				if (ecrfFieldValue == null) {
					ecrfField = (ECRFField) entities.get(ServiceUtil.ECRF_FIELD_VALUE_DAO_ECRF_FIELD_ALIAS);
					if (!ecrfField.isSeries() && !ecrfField.isOptional()) { // !ecrfField.isDisabled() &&
						try {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_MISSING,
									CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField()))
									// ecrfFieldDao.toECRFFieldOutVO(ecrfField).getField().getName()
									);
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
					// if (!ecrfField.isDisabled()) {
					InputField inputField = ecrfField.getField();
					InputFieldValue value = ecrfFieldValue.getValue();
					try {
						// Long inputFieldVO = inputFieldDao.toInputFieldOutVO(ecrfField.getField());
						ServiceUtil.checkInputFieldTextValue(inputField, ecrfField.isOptional(), value.getStringValue(), inputFieldDao,
								this.getInputFieldSelectionSetValueDao());
						ServiceUtil.checkInputFieldBooleanValue(inputField, ecrfField.isOptional(), value.getBooleanValue() == null ? false : value
								.getBooleanValue()
								.booleanValue(), inputFieldDao);
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
					// }
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
		if ((new ProbandGroupTitleCollisionFinder(this.getTrialDao(), this.getProbandGroupDao())).collides(probandGroupIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GROUP_TITLE_ALREADY_EXISTS);
		}
		if ((new ProbandGroupTokenCollisionFinder(this.getTrialDao(), this.getProbandGroupDao())).collides(probandGroupIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_GROUP_TOKEN_ALREADY_EXISTS);
		}
	}

	private void checkProbandListEntryInput(ProbandListEntryInVO probandListEntryIn, boolean signup, Timestamp now) throws ServiceException {
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
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = CheckIDUtil.checkProbandId(probandListEntryIn.getProbandId(), probandDao);
		ServiceUtil.checkProbandLocked(proband);
		if (proband.isPerson() && !trial.getType().isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_PROBAND_NOT_ANIMAL, proband.getId().toString()); // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)));
		} else if (!proband.isPerson() && trial.getType().isPerson()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_PROBAND_NOT_PERSON, proband.getId().toString()); // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)));
		}
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		if ((new ProbandListEntryProbandCollisionFinder(trialDao, probandListEntryDao)).collides(probandListEntryIn)) {
			throw L10nUtil
			.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_ALREADY_PARTICIPATING, proband.getId().toString()); // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)));
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
		if (now != null && (new ProbandListEntryStatusCollisionFinder(probandDao, trialDao, probandListEntryDao, now)).collides(probandListEntryIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_PROBAND_BLOCKED, proband.getId().toString()); // CommonUtil.probandOutVOToString(probandDao.toProbandOutVO(proband)));
		}
	}

	private void checkProbandListEntryTagInput(ProbandListEntryTagInVO listTagIn) throws ServiceException {
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
	}

	private void checkProbandListEntryTagValueInput(ProbandListEntryTagValueInVO probandListEntryTagValueIn, ProbandListEntry listEntry, ProbandListEntryTag listEntryTag)
			throws ServiceException
			{
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = listEntryTag.getField();
		inputFieldDao.lock(inputField, LockMode.PESSIMISTIC_WRITE);
		// InputField inputField = ServiceUtil.checkInputFieldId(listEntryTag.getField().getId(), inputFieldDao, LockMode.PESSIMISTIC_WRITE);
		// InputFieldOutVO inputFieldVO = this.getInputFieldDao().toInputFieldOutVO(listEntryTag.getField());
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

	private void checkTeamMemberInput(TeamMemberInVO teamMemberIn) throws ServiceException
	{
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
		(new TimelineEventTypeTagAdapter(this.getTrialDao(), this.getTimelineEventTypeDao())).checkTagValueInput(timelineEventIn);
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
		if ((trialIn.getSignupProbandList() || trialIn.getSignupInquiries()) && CommonUtil.isEmptyString(trialIn.getSignupDescription())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.TRIAL_SIGNUP_DESCRIPTION_EMPTY);
		}
	}

	private void checkTrialTagValueInput(TrialTagValueInVO tagValueIn) throws ServiceException
	{
		(new TrialTagAdapter(this.getTrialDao(), this.getTrialTagDao())).checkTagValueInput(tagValueIn);
	}

	private void checkUnresolvedEcrfFieldStatus(ProbandListEntry listEntry, ECRF ecrf) throws Exception {
		ECRFProgressVO ecrfProgress = ServiceUtil.populateEcrfProgress(this.getECRFDao().toECRFOutVO(ecrf), this.getProbandListEntryDao().toProbandListEntryOutVO(listEntry),
				false,
				this.getECRFStatusEntryDao(), this.getECRFStatusTypeDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao());
		Iterator<ECRFFieldStatusQueueCountVO> it = ecrfProgress.getEcrfFieldStatusQueueCounts().iterator();
		long unresolved = 0l;
		while (it.hasNext()) {
			unresolved += it.next().getUnresolved();
		}
		if (unresolved > 0l) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.UNRESOLVED_ECRF_FIELD_ISSUES, unresolved);
		}
	}

	private void checkUpdateEcrfFieldInput(ECRFField originalEcrfField, ECRFFieldInVO ecrfFieldIn) throws ServiceException
	{
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
				|| originalEcrfField.getPosition() != ecrfFieldIn.getPosition()
				) {
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



	private void checkUpdateEcrfInput(ECRFInVO modifiedEcrf, ECRF originalEcrf) throws ServiceException {
		checkEcrfInput(modifiedEcrf);
		if (!modifiedEcrf.getTrialId().equals(originalEcrf.getTrial().getId())
				|| !originalEcrf.getName().equals(modifiedEcrf.getName())
				|| !originalEcrf.getTitle().equals(modifiedEcrf.getTitle())
				|| !((originalEcrf.getDescription() == null && modifiedEcrf.getDescription() == null) || (originalEcrf.getDescription() != null
						&& modifiedEcrf.getDescription() != null && originalEcrf.getDescription().equals(modifiedEcrf.getDescription())))
				|| !((modifiedEcrf.getVisitId() == null && originalEcrf.getVisit() == null) || (modifiedEcrf.getVisitId() != null && originalEcrf.getVisit() != null && modifiedEcrf
						.getVisitId().equals(originalEcrf.getVisit().getId())))
				|| !((modifiedEcrf.getGroupId() == null && originalEcrf.getGroup() == null) || (modifiedEcrf.getGroupId() != null && originalEcrf.getGroup() != null && modifiedEcrf
						.getGroupId().equals(originalEcrf.getGroup().getId())))) {
			ServiceUtil.checkLockedEcrfs(originalEcrf, this.getECRFStatusEntryDao(), this.getECRFDao());
		}
		if (!modifiedEcrf.getTrialId().equals(originalEcrf.getTrial().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_TRIAL_CHANGED);
		}
	}

	private void checkUpdateEcrfStatusEntry(ECRFStatusEntry originalStatusEntry, ECRFStatusType statusType, Long version, User user) throws Exception
	{
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

	private void checkUpdateInquiryInput(Inquiry originalInquiry, InquiryInVO inquiryIn) throws ServiceException
	{
		InputField field = CheckIDUtil.checkInputFieldId(inquiryIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(inquiryIn.getTrialId(), this.getTrialDao());
		if (!trial.equals(originalInquiry.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_TRIAL_CHANGED);
		}
		ServiceUtil.checkTrialLocked(trial);
		checkInquiryInput(inquiryIn);
		if (!field.equals(originalInquiry.getField()) && this.getInquiryValueDao().getCount(originalInquiry.getId()) > 0) { // originalInquiry.getInquiryValues().Xsize() > 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INQUIRY_INPUT_FIELD_CHANGED);
		}
	}

	private void checkUpdateProbandListEntryTagInput(ProbandListEntryTag originalProbandListEntryTag, ProbandListEntryTagInVO listTagIn) throws ServiceException
	{
		InputField field = CheckIDUtil.checkInputFieldId(listTagIn.getFieldId(), this.getInputFieldDao(), LockMode.PESSIMISTIC_WRITE);
		Trial trial = CheckIDUtil.checkTrialId(listTagIn.getTrialId(), this.getTrialDao());
		if (!trial.equals(originalProbandListEntryTag.getTrial())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_TRIAL_CHANGED);
		}
		ServiceUtil.checkTrialLocked(trial);
		checkProbandListEntryTagInput(listTagIn);
		if (!field.equals(originalProbandListEntryTag.getField()) && this.getProbandListEntryTagValueDao().getCount(null, originalProbandListEntryTag.getId()) > 0) { // originalProbandListEntryTag.getTagValues().Xsize()
			// > 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TAG_INPUT_FIELD_CHANGED);
		}
	}

	private void checkUpdateTrialInput(Trial originalTrial, TrialInVO trialIn, User user) throws ServiceException
	{
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
	}

	private void checkVisitInput(VisitInVO visitIn) throws ServiceException {
		(new VisitTypeTagAdapter(this.getTrialDao(), this.getVisitTypeDao())).checkTagValueInput(visitIn);
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
		if (visitScheduleItemIn.getStop().compareTo(visitScheduleItemIn.getStart()) <= 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_END_TIMESTAMP_LESS_THAN_OR_EQUAL_TO_START_TIMESTAMP);
		}
		if ((new VisitScheduleItemCollisionFinder(this.getTrialDao(), this.getVisitScheduleItemDao())).collides(visitScheduleItemIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.VISIT_SCHEDULE_ITEM_TOKEN_EXISTS_ALREADY, group == null ? null : group.getToken(), visit == null ? null
					: visit.getToken(), visitScheduleItemIn.getToken());
		}
	}

	private ArrayList<ECRFFieldStatusEntryOutVO> clearEcrfFieldStatusEntries(ProbandListEntry listEntry, Collection<ECRFFieldStatusEntry> statusEntries,
			Timestamp now, User user) throws Exception {


		//ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
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
			result.add(ServiceUtil.removeEcrfFieldStatusEntry(fieldStatus, now, user, ServiceUtil.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL,
					ServiceUtil.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND, ecrfFieldStatusEntryDao, journalEntryDao, notificationDao, notificationRecipientDao));
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

	private void execEcrfStatusActions(ECRFStatusType oldStatus, ECRFStatusEntry statusEntry, Long probandListStatusTypeId, Timestamp now, User user) throws Exception {
		ECRFStatusType newState = statusEntry.getStatus();
		if (oldStatus == null || !oldStatus.equals(newState)) {
			ArrayList<ECRFStatusAction> sortedActions = new ArrayList<ECRFStatusAction>(newState.getActions());
			Collections.sort(sortedActions, new EcrfStatusActionComparator());
			Iterator<ECRFStatusAction> sortedActionsIt = sortedActions.iterator();

			ProbandListEntry listEntry = statusEntry.getListEntry();
			ECRF ecrf = statusEntry.getEcrf();

			Integer scheduleValidationLimit = Settings.getIntNullable(SettingCodes.ECRF_FIELD_VALUES_SCHEDULE_VALIDATION_LIMIT, Bundle.SETTINGS,
					DefaultSettings.ECRF_FIELD_VALUES_SCHEDULE_VALIDATION_LIMIT);
			// InputFieldDao inputFieldDao = this.getInputFieldDao();
			// ECRFDao ecrfDao = this.getECRFDao();



			// JournalEntryDao journalEntryDao = this.getJournalEntryDao();
			while (sortedActionsIt.hasNext()) {
				ECRFStatusAction ecrfStatusAction = sortedActionsIt.next();
				switch (ecrfStatusAction.getAction()) {
					case CLEAR_VALUES:
						// defer?
						clearEcrfFieldValues(listEntry,
								this.getECRFFieldValueDao().findByListEntryEcrf(listEntry.getId(), ecrf.getId(), true, null),now,user);
						break;
					case CLEAR_STATUS_ENTRIES:
						// defer?
						clearEcrfFieldStatusEntries(listEntry,
								this.getECRFFieldStatusEntryDao().findByListEntryEcrf(listEntry.getId(), ecrf.getId(), true, null), now, user);
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
							this.getJournalEntryDao());
						} catch (ServiceException e) {
							// already end state
						}
						break;
					case SCHEDULE_EXPORT_VALUES:
					case EXPORT_VALUES:
						throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_ECRF_STATUS_ACTION, ecrfStatusAction.getAction());
					case SCHEDULE_VALIDATE_VALUES:
						//throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_ECRF_STATUS_ACTION, ecrfStatusAction.getAction());
						// statusEntry.setValidationTimestamp(now);
						statusEntry.setValidationStatus(ECRFValidationStatus.PENDING);
						// statusEntry.setValidationResponseMsg(null);
						this.getECRFStatusEntryDao().update(statusEntry);
						break;
					case VALIDATE_VALUES:
						if (scheduleValidationLimit == null
						|| (scheduleValidationLimit > 0 && this.getECRFFieldValueDao().getCount(listEntry.getId(), ecrf.getId()) <= scheduleValidationLimit)) {
							addValidationEcrfFieldStatusEntries(statusEntry, true, now, user);
						} else {
							statusEntry.setValidationStatus(ECRFValidationStatus.PENDING);
							this.getECRFStatusEntryDao().update(statusEntry);
						}
						break;
					case NOTIFY_ECRF_STATUS:
						// throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_ECRF_STATUS_ACTION, ecrfStatusAction.getAction());
						this.getNotificationDao().addNotification(statusEntry, now, null);
						break;
					case CANCEL_NOTIFICATIONS:
						ServiceUtil.cancelNotifications(statusEntry.getNotifications(), this.getNotificationDao(),
								org.phoenixctms.ctsms.enumeration.NotificationType.ECRF_STATUS_UPDATED);
						break;
					case NO_MISSING_VALUES:
						if (scheduleValidationLimit == null
						|| (scheduleValidationLimit > 0 && this.getECRFFieldValueDao().getCount(listEntry.getId(), ecrf.getId()) <= scheduleValidationLimit)) {
							checkMissingEcrfFieldValuesDeeply(listEntry, ecrf);
						} else {
							checkMissingEcrfFieldValues(listEntry,ecrf);
						}
						break;
					case NO_UNRESOLVED_FIELD_STATUS_ENTRIES:
						checkUnresolvedEcrfFieldStatus(listEntry, ecrf);
						break;
					case SIGN_ECRF:
						// checkTeamMemberSign(listEntry.getTrial(), user);
						this.getSignatureDao().addEcrfSignature(statusEntry, now);
						break;
					default:
						throw L10nUtil.initServiceException(ServiceExceptionCodes.UNSUPPORTED_ECRF_STATUS_ACTION, ecrfStatusAction.getAction());
				}
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
						// checkTeamMemberSign(trial, user);
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


	private ECRFFieldValuesOutVO getEcrfFieldValues(ECRF ecrf, ProbandListEntryOutVO listEntryVO, boolean addSeries, boolean jsValues, boolean loadAllJsValues, PSFVO psf)
			throws Exception {
		ECRFFieldValuesOutVO result = new ECRFFieldValuesOutVO();
		if (listEntryVO != null && ecrf != null) {
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			Collection<Map> ecrfFieldValues = ecrfFieldValueDao.findByListEntryEcrfJs(listEntryVO.getId(), ecrf.getId(), true, null, psf);
			HashMap<String, Long> maxSeriesIndexMap = null;
			HashMap<String, Long> fieldMaxPositionMap = null;
			HashMap<String, Long> fieldMinPositionMap = null;
			HashMap<String, Set<ECRFField>> seriesEcrfFieldMap = null;
			// HashMap<String, Set<ECRFField>> seriesEcrfFieldJsMap = null;
			if (addSeries) {
				maxSeriesIndexMap = new HashMap<String, Long>();
				fieldMaxPositionMap = new HashMap<String, Long>();
				fieldMinPositionMap = new HashMap<String, Long>();
				seriesEcrfFieldMap = new HashMap<String, Set<ECRFField>>();
				// seriesEcrfFieldJsMap = new HashMap<String, Set<ECRFField>>();
				ServiceUtil.initSeriesEcrfFieldMaps(
						ecrfFieldDao.findByTrialEcrfSeriesJs(null, ecrf.getId(), true, true, null, null),
						listEntryVO.getId(),
						ecrf.getId(),
						maxSeriesIndexMap,
						fieldMaxPositionMap,
						fieldMinPositionMap,
						seriesEcrfFieldMap,
						// seriesEcrfFieldJsMap,
						ecrfFieldValueDao
						);
			}
			result.setPageValues(ServiceUtil.getEcrfFieldValues(listEntryVO, ecrfFieldValues, maxSeriesIndexMap, fieldMaxPositionMap, fieldMinPositionMap, seriesEcrfFieldMap,
					null,
					ecrfFieldDao,
					ecrfFieldValueDao,
					this.getECRFFieldStatusEntryDao(),
					this.getECRFFieldStatusTypeDao())); // this.getInputFieldSelectionSetValueDao()
			if (jsValues) {
				if (addSeries) {
					maxSeriesIndexMap.clear();
					fieldMaxPositionMap.clear();
					fieldMinPositionMap.clear();
					seriesEcrfFieldMap.clear();
					// seriesEcrfFieldJsMap.clear();
					ServiceUtil.initSeriesEcrfFieldMaps(
							ecrfFieldDao.findByTrialEcrfSeriesJs(null, ecrf.getId(), true, true, true, null),
							listEntryVO.getId(),
							ecrf.getId(),
							maxSeriesIndexMap,
							fieldMaxPositionMap,
							fieldMinPositionMap,
							seriesEcrfFieldMap,
							// seriesEcrfFieldJsMap,
							ecrfFieldValueDao
							);
				}
				if (loadAllJsValues) {
					result.setJsValues(ServiceUtil.getEcrfFieldJsonValues(ecrfFieldValueDao.findByListEntryEcrfJs(listEntryVO.getId(), ecrf.getId(), true, true, null),
							maxSeriesIndexMap, fieldMaxPositionMap, fieldMinPositionMap, seriesEcrfFieldMap,
							false, ecrfFieldValueDao,
							this.getInputFieldSelectionSetValueDao()));
				} else {
					result.setJsValues(ServiceUtil.getEcrfFieldJsonValues(ecrfFieldValues, maxSeriesIndexMap, fieldMaxPositionMap, fieldMinPositionMap, seriesEcrfFieldMap,
							true, ecrfFieldValueDao,
							this.getInputFieldSelectionSetValueDao()));
				}
			}
		}
		return result;
	}

	private ECRFFieldValuesOutVO getEcrfFieldValues(ECRF ecrf, String section, ProbandListEntryOutVO listEntryVO, boolean addSeries, boolean jsValues, boolean loadAllJsValues,
			PSFVO psf) throws Exception {
		ECRFFieldValuesOutVO result = new ECRFFieldValuesOutVO();
		if (listEntryVO != null && ecrf != null) {
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			Collection<Map> ecrfFieldValues = ecrfFieldValueDao.findByListEntryEcrfSectionJs(listEntryVO.getId(), ecrf.getId(), section, true, null, psf);
			HashMap<String, Long> maxSeriesIndexMap = null;
			HashMap<String, Long> fieldMaxPositionMap = null;
			HashMap<String, Long> fieldMinPositionMap = null;
			HashMap<String, Set<ECRFField>> seriesEcrfFieldMap = null;
			// HashMap<String, Set<ECRFField>> seriesEcrfFieldJsMap = null;
			if (addSeries) {
				maxSeriesIndexMap = new HashMap<String, Long>();
				fieldMaxPositionMap = new HashMap<String, Long>();
				fieldMinPositionMap = new HashMap<String, Long>();
				seriesEcrfFieldMap = new HashMap<String, Set<ECRFField>>();
				// seriesEcrfFieldJsMap = new HashMap<String, Set<ECRFField>>();
				ServiceUtil.initSeriesEcrfFieldMaps(
						ecrfFieldDao.findByTrialEcrfSectionSeriesJs(null, ecrf.getId(), section, true, true, null, null),
						listEntryVO.getId(),
						ecrf.getId(),
						maxSeriesIndexMap,
						fieldMaxPositionMap,
						fieldMinPositionMap,
						seriesEcrfFieldMap,
						// seriesEcrfFieldJsMap,
						ecrfFieldValueDao
						);
			}
			result.setPageValues(ServiceUtil.getEcrfFieldValues(listEntryVO, ecrfFieldValues, maxSeriesIndexMap, fieldMaxPositionMap, fieldMinPositionMap, seriesEcrfFieldMap,
					null,
					ecrfFieldDao,
					ecrfFieldValueDao,
					this.getECRFFieldStatusEntryDao(),
					this.getECRFFieldStatusTypeDao())); // this.getInputFieldSelectionSetValueDao()
			if (jsValues) {
				if (addSeries) {
					maxSeriesIndexMap.clear();
					fieldMaxPositionMap.clear();
					fieldMinPositionMap.clear();
					seriesEcrfFieldMap.clear();
					// seriesEcrfFieldJsMap.clear();
					ServiceUtil.initSeriesEcrfFieldMaps(
							(loadAllJsValues ? ecrfFieldDao.findByTrialEcrfSeriesJs(null, ecrf.getId(), true, true, true, null) :
								ecrfFieldDao.findByTrialEcrfSectionSeriesJs(null, ecrf.getId(), section, true, true, true, null)),
								listEntryVO.getId(),
								ecrf.getId(),
								maxSeriesIndexMap,
								fieldMaxPositionMap,
								fieldMinPositionMap,
								seriesEcrfFieldMap,
								// seriesEcrfFieldJsMap,
								ecrfFieldValueDao
							);
				}
				if (loadAllJsValues) {
					result.setJsValues(ServiceUtil.getEcrfFieldJsonValues(
							// ecrfFieldValueDao.findByListEntryEcrfSectionJs(listEntryVO.getId(), ecrf.getId(), section, true, true, null),
							ecrfFieldValueDao.findByListEntryEcrfJs(listEntryVO.getId(), ecrf.getId(), true, true, null),
							maxSeriesIndexMap, fieldMaxPositionMap,
							fieldMinPositionMap, seriesEcrfFieldMap,
							false, ecrfFieldValueDao,
							this.getInputFieldSelectionSetValueDao()));
				} else {
					result.setJsValues(ServiceUtil.getEcrfFieldJsonValues(ecrfFieldValues,
							maxSeriesIndexMap, fieldMaxPositionMap,
							fieldMinPositionMap, seriesEcrfFieldMap,
							true, ecrfFieldValueDao,
							this.getInputFieldSelectionSetValueDao()));
				}
			}
		}
		return result;
	}

	private boolean getEcrfSectionUnlockValue(Long probandListEntryId, Long index, Long ecrfId, String section) throws Exception {
		Iterator<ECRFField> ecrfFieldIt = this.getECRFFieldDao().findByTrialEcrfSectionSeriesJs(null, ecrfId, section, false, true, null, null).iterator();
		ECRFFieldStatusQueue[] queues = ECRFFieldStatusQueue.values();
		while (ecrfFieldIt.hasNext()) {
			ECRFField ecrfField = ecrfFieldIt.next();
			for (int i = 0; i < queues.length; i++) {
				ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(queues[i], probandListEntryId, ecrfField.getId(),index);
				if (lastStatus != null && lastStatus.getStatus().isUnlockValue()) {
					return true;
				}
			}
		}
		return false;
	}

	private ArrayList getIndexFieldLog(long listEntryId, long ecrfFieldId, Long index, boolean blank, boolean auditTrail, boolean skipMostRecentValue,
			boolean skipMostRecentStatus, ECRFFieldStatusQueue[] queues) throws Exception {

		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		EcrfFieldValueStatusEntryOutVOComparator ecrfFieldValueStatusEntryOutVOComparator = new EcrfFieldValueStatusEntryOutVOComparator(true);

		ArrayList indexFieldLog = new ArrayList();
		Collection log;
		if (!blank && auditTrail) { // x) {
			log = ecrfFieldValueDao.findByListEntryEcrfFieldIndex(listEntryId, ecrfFieldId, index, true, true, null);
			Iterator logIt = log.iterator();
			if (skipMostRecentValue && logIt.hasNext()) {
				logIt.next();
			} // skip most actual element
			while (logIt.hasNext()) {
				indexFieldLog.add(ecrfFieldValueDao.toECRFFieldValueOutVO((ECRFFieldValue) logIt.next()));
			}
			// indexFieldLog.addAll(log);
		}
		if (!blank && queues != null) {
			for (int i = 0; i < queues.length; i++) {
				log = ecrfFieldStatusEntryDao.findByListEntryEcrfFieldIndex(queues[i], listEntryId, ecrfFieldId, index, false, true, // false,
						null);
				// ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(log);
				// indexFieldLog.addAll(log);
				Iterator logIt = log.iterator();
				if (skipMostRecentStatus && logIt.hasNext()) {
					logIt.next();
				} // skip most actual element
				while (logIt.hasNext()) {
					indexFieldLog.add(ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVO((ECRFFieldStatusEntry) logIt.next()));
				}
			}
		}
		Collections.sort(indexFieldLog, ecrfFieldValueStatusEntryOutVOComparator);

		return indexFieldLog;
	}

	private ProbandListEntryTagValuesOutVO getProbandListEntryTagValues( ProbandListEntryOutVO listEntryVO,  boolean jsValues,boolean loadAllJsValues, boolean sort,PSFVO psf) throws Exception {

		ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
		ProbandListEntryTagValuesOutVO result = new ProbandListEntryTagValuesOutVO();
		Collection<Map> tagValues = probandListEntryTagValueDao.findByListEntryJs(listEntryVO.getId(), sort, null, psf);
		result.setPageValues(ServiceUtil.getProbandListEntryTagValues(listEntryVO, tagValues, null,
				this.getProbandListEntryTagDao(), probandListEntryTagValueDao)); // this.getInputFieldSelectionSetValueDao()
		if (jsValues) {
			if (loadAllJsValues) {
				result.setJsValues(ServiceUtil.getProbandListEntryTagJsonValues(probandListEntryTagValueDao.findByListEntryJs(listEntryVO.getId(), sort, true, null),
						false, probandListEntryTagValueDao, this.getInputFieldSelectionSetValueDao()));
			} else {
				result.setJsValues(ServiceUtil.getProbandListEntryTagJsonValues(tagValues,
						true, probandListEntryTagValueDao, this.getInputFieldSelectionSetValueDao()));
			}
		}
		return result;

	}

	private SignatureVO getVerifiedEcrfSignatureVO(Signature signature) throws Exception {
		SignatureVO result = this.getSignatureDao().toSignatureVO(signature);
		StringBuilder comment = new StringBuilder();
		result.setVerificationTimestamp(new Date());
		result.setValid(EcrfSignature.verify(signature, comment, this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao()));
		result.setVerified(true);
		result.setComment(comment.toString());
		result.setDescription(EntitySignature.getDescription(result));
		return result;
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
				newEcrfField.setSeries(series);
				// newEcrfField.s
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
		return addEcrfFieldStatusEntry(newEcrfFieldStatusEntry, queue, now, user, ServiceUtil.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL, ServiceUtil.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND,
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
			AuthenticationVO auth, Long trialId, Long groupId, Long rating, Long ratingMax, Set<Long> probandIds, boolean shuffle, Long limit) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		// ProbandListStatusType statusType = this.getProbandListStatusTypeDao().findInitialStates(false).iterator().next();
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
		if (probandIds != null) {
			Iterator<Long> probandIt;
			ArrayList<Long> ids = new ArrayList<Long>(probandIds);
			shuffleInfo.setInputIds(ids);
			if (shuffle) {
				long seed = SecureRandom.getInstance(SHUFFLE_SEED_RANDOM_ALGORITHM).nextLong();
				Random random = new Random(seed); // reproducable
				shuffleInfo.setPrngClass(random.getClass().getCanonicalName());
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
					result.add(addProbandListEntry(newProbandListEntry, false, now, user)); // statusType
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
			AuthenticationVO auth, boolean signup, ProbandListEntryInVO newProbandListEntry) throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();

		// ProbandListStatusType statusType = this.getProbandListStatusTypeDao().findInitialStates(signup).iterator().next();
		return addProbandListEntry(newProbandListEntry, signup, now, user); // statusType
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
			AuthenticationVO auth, Long trialId, boolean optional, boolean excel, boolean ecrf, Set<Long> inputFieldIds)
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
				this.getProbandDao(), this.getProbandListEntryDao(), this.getProbandListStatusEntryDao(), this.getProbandListStatusTypeDao(), this.getJournalEntryDao());
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
			AuthenticationVO auth, TimelineEventInVO newTimelineEvent) throws Exception {
		checkTimelineEventInput(newTimelineEvent);
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent timelineEvent = timelineEventDao.timelineEventInVOToEntity(newTimelineEvent);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(timelineEvent, now, user);
		timelineEvent.setDismissedTimestamp(now);
		timelineEvent = timelineEventDao.create(timelineEvent);
		notifyTimelineEventReminder(timelineEvent, now);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent);
		ServiceUtil.logSystemMessage(timelineEvent.getTrial(), result.getTrial(), now, user, SystemMessageCodes.TIMELINE_EVENT_CREATED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TrialOutVO handleAddTrial(AuthenticationVO auth, TrialInVO newTrial) throws Exception {
		checkAddTrialInput(newTrial);
		TrialDao trialDao = this.getTrialDao();
		Trial trial = trialDao.trialInVOToEntity(newTrial);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(trial, now, user);
		trial = trialDao.create(trial);
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
			ECRF originalEcrf = CheckIDUtil.checkEcrfId(modifiedEcrf.getId(), ecrfDao, LockMode.PESSIMISTIC_WRITE);
			checkUpdateEcrfInput(modifiedEcrf, originalEcrf);
			original = ecrfDao.toECRFOutVO(originalEcrf);
			Collection<ECRFField> originalEcrfFields = originalEcrf.getEcrfFields();
			originalEcrfFieldIds = new HashSet<Long>(originalEcrfFields.size());
			Iterator<ECRFField> it = originalEcrfFields.iterator();
			while (it.hasNext()) {
				originalEcrfFieldIds.add(it.next().getId());
			}
			ecrfDao.evict(originalEcrf);
			ecrf = ecrfDao.eCRFInVOToEntity(modifiedEcrf);
			CoreUtil.modifyVersion(originalEcrf, ecrf, now, user);
			ecrfDao.update(ecrf);
			// Hibernate.initialize(inputField);
			update = true;
		} else {
			checkAddEcrfInput(modifiedEcrf);
			originalEcrfFieldIds = new HashSet<Long>();
			original = null;
			ecrf = ecrfDao.eCRFInVOToEntity(modifiedEcrf);
			CoreUtil.modifyVersion(ecrf, now, user);
			// ecrf = ecrfDao.create(ecrf);
			ecrf = ecrfDao.create(ecrf, LockMode.PESSIMISTIC_WRITE); // required here, as load(..,lockMode) will not work with created entities
			// inputFieldDao.evict(inputField);
			// inputField = ServiceUtil.checkInputFieldId(inputField.getId(), inputFieldDao, LockMode.PESSIMISTIC_WRITE);
			// inputFieldDao.lock(inputField, LockMode.PESSIMISTIC_WRITE);
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
		notifyVisitScheduleItemReminder(visitScheduleItem, now);
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
				// InquiryOutVO inquiryVO = inquiryDao.toInquiryOutVO(inquiry);
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
	protected Collection<ECRFFieldValueOutVO> handleClearEcrfFieldValues(AuthenticationVO auth, Long ecrfId, Long probandListEntryId)
			throws Exception {

		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ProbandListEntry listEntry = checkClearEcrfFieldValues(ecrfId, probandListEntryId, now, user);
		clearEcrfFieldStatusEntries(listEntry,
				this.getECRFFieldStatusEntryDao().findByListEntryEcrf(probandListEntryId, ecrfId, true, null), now, user);
		return clearEcrfFieldValues(listEntry,
				this.getECRFFieldValueDao().findByListEntryEcrf(probandListEntryId, ecrfId, true, null),now,user);
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> handleClearEcrfFieldValues(AuthenticationVO auth, Long ecrfId, String section, Long probandListEntryId)
			throws Exception {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ProbandListEntry listEntry = checkClearEcrfFieldValues(ecrfId, probandListEntryId, now, user);
		clearEcrfFieldStatusEntries(listEntry,
				this.getECRFFieldStatusEntryDao().findByListEntryEcrfSection(probandListEntryId, ecrfId, section, true, null), now, user);
		return clearEcrfFieldValues(this.getProbandListEntryDao().load(probandListEntryId),
				this.getECRFFieldValueDao().findByListEntryEcrfSection(probandListEntryId, ecrfId, section, true, null), now, user);
	}

	@Override
	protected ECRFOutVO handleCloneEcrf(AuthenticationVO auth, Long ecrfId, Long trialId) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF originalEcrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
		ECRFOutVO originalEcrfVO = ecrfDao.toECRFOutVO(originalEcrf);
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao, LockMode.PESSIMISTIC_WRITE);
		// if (trialId != null) {
		// trial = ServiceUtil.checkTrialId(trialId, trialDao, LockMode.PESSIMISTIC_WRITE);
		// } else {
		// trial = ServiceUtil.checkTrialId(originalEcrf.getTrial().getId(), trialDao, LockMode.PESSIMISTIC_WRITE);
		// }
		ServiceUtil.checkTrialLocked(trial);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ECRF newEcrf = ECRF.Factory.newInstance();
		newEcrf.setActive(originalEcrf.isActive());
		newEcrf.setDeferredDelete(originalEcrf.isDeferredDelete());
		newEcrf.setDescription(originalEcrf.getDescription());
		newEcrf.setDisabled(originalEcrf.isDisabled());
		newEcrf.setEnableBrowserFieldCalculation(originalEcrf.isEnableBrowserFieldCalculation());
		newEcrf.setName(originalEcrf.getName());
		newEcrf.setExternalId(originalEcrf.getExternalId());
		newEcrf.setProbandListStatus(originalEcrf.getProbandListStatus());
		Long ecrfMaxPosition = ecrfDao.findMaxPosition(trial.getId(), null);
		newEcrf.setPosition(ecrfMaxPosition != null ? ecrfMaxPosition + 1l : CommonUtil.LIST_INITIAL_POSITION);
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
			newEcrfField.setDeferredDelete(originalEcrfField.isDeferredDelete());
			newEcrfField.setDisabled(originalEcrfField.isDisabled());
			newEcrfField.setJsOutputExpression(originalEcrfField.getJsOutputExpression());
			newEcrfField.setJsValueExpression(originalEcrfField.getJsValueExpression());
			newEcrfField.setJsVariableName(originalEcrfField.getJsVariableName());
			newEcrfField.setOptional(originalEcrfField.isOptional());
			newEcrfField.setPosition(originalEcrfField.getPosition());
			newEcrfField.setReasonForChangeRequired(originalEcrfField.isReasonForChangeRequired());
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
	protected ECRFOutVO handleDeleteEcrf(AuthenticationVO auth, Long ecrfId,  boolean defer, boolean force) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ECRFOutVO result;
		if (!force && (defer
				|| this.getECRFStatusEntryDao().getCount(null, ecrfId, null, null, null, null, null) > 0)) {
			// || this.getECRFFieldValueDao().getCount(ecrfId, section)) {
			ECRF originalEcrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
			Trial trial = originalEcrf.getTrial();
			ServiceUtil.checkTrialLocked(trial);
			ECRFOutVO original = ecrfDao.toECRFOutVO(originalEcrf);
			ecrfDao.evict(originalEcrf);
			ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
			ecrf.setDeferredDelete(true);
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
			ProbandGroup group = ecrf.getGroup();
			if (group != null) {
				group.removeEcrfs(ecrf);
			}
			Visit visit = ecrf.getVisit();
			if (visit != null) {
				visit.removeEcrfs(ecrf);
			}
			//if (deleteCascade) {
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
			//}
			ecrfDao.remove(ecrf);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.ECRF_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected ECRFFieldOutVO handleDeleteEcrfField(AuthenticationVO auth, Long ecrfFieldId,  boolean defer,boolean force) throws Exception {
		// JournalEntryDao journalEntryDao = this.getJournalEntryDao();
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
			ecrfField.setDeferredDelete(true);
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
		//ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ECRFFieldStatusEntry fieldStatus = CheckIDUtil.checkEcrfFieldStatusEntryId(ecrfFieldStatusEntryId, ecrfFieldStatusEntryDao);
		ProbandListEntry listEntry = fieldStatus.getListEntry();
		this.getProbandListEntryDao().lock(listEntry, LockMode.PESSIMISTIC_WRITE);
		ECRFField ecrfField = fieldStatus.getEcrfField();
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
		ECRFStatusEntry ecrfStatusEntry = this.getECRFStatusEntryDao().findByEcrfListEntry(ecrf.getId(), listEntry.getId());
		if (ecrfStatusEntry != null && ecrfStatusEntry.getStatus().isFieldStatusLockdown()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_INPUT_LOCKED_FOR_ECRF_STATUS,
					L10nUtil.getEcrfStatusTypeName(Locales.USER, ecrfStatusEntry.getStatus().getNameL10nKey()));
		}
		if (listEntry.getLastStatus() != null && !listEntry.getLastStatus().getStatus().isEcrfValueInputEnabled()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
					L10nUtil.getProbandListStatusTypeName(Locales.USER, listEntry.getLastStatus().getStatus().getNameL10nKey()));
		}
		if (!fieldStatus.equals(ecrfFieldStatusEntryDao.findLastStatus(fieldStatus.getQueue(), listEntry.getId(), ecrfField.getId(), fieldStatus.getIndex()))) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_STATUS_ENTRY_NOT_LAST_ENTRY);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		// if (!fieldStatus.getStatus().isInitial() && fieldStatus.getStatus().isResolved()) {
		if (fieldStatus.getStatus().isResolved()
				&& Settings.getEcrfFieldStatusQueueList(SettingCodes.RESOLVE_ECRF_FIELD_STATUS_RESTRICTION_QUEUES, Bundle.SETTINGS,
						DefaultSettings.RESOLVE_ECRF_FIELD_STATUS_RESTRICTION_QUEUES).contains(fieldStatus.getStatus().getQueue())) {
			checkTeamMemberResolve(listEntry.getTrial(), user);
		}
		ECRFFieldStatusEntryOutVO result = ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVO(fieldStatus);
		// if (!result.isDecrypted()) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_LIST_STATUS_ENTRY);
		// }

		listEntry.removeEcrfFieldStatusEntries(fieldStatus);
		fieldStatus.getEcrfField().removeEcrfFieldStatusEntries(fieldStatus);
		ServiceUtil.removeEcrfFieldStatusEntry(fieldStatus, now, user, ServiceUtil.LOG_ECRF_FIELD_STATUS_ENTRY_TRIAL, ServiceUtil.LOG_ECRF_FIELD_STATUS_ENTRY_PROBAND,
				ecrfFieldStatusEntryDao, this.getJournalEntryDao(), this.getNotificationDao(),
				this.getNotificationRecipientDao());

		return result;
	}

	@Override
	protected InquiryOutVO handleDeleteInquiry(AuthenticationVO auth, Long inquiryId, boolean defer, boolean force) throws Exception {
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
			inquiry.setDeferredDelete(true);
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
					journalEntryDao, inquiryDao); // ,
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
		// if (deleteCascade) {
		ECRFDao ecrfDao = this.getECRFDao();
		EcrfPositionCollisionFinder ecrfPositionCollisionFinder = new EcrfPositionCollisionFinder(this.getTrialDao(), ecrfDao);
		Iterator<ECRF> ecrfsIt = probandGroup.getEcrfs().iterator();
		while (ecrfsIt.hasNext()) {
			ECRF ecrf = ecrfsIt.next();
			ECRFInVO ecrfIn = new ECRFInVO();
			ecrfIn.setId(ecrf.getId());
			ecrfIn.setTrialId(ecrf.getTrial().getId());
			ecrfIn.setPosition(ecrf.getPosition());
			ecrfIn.setGroupId(null);
			if (ecrfPositionCollisionFinder.collides(ecrfIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_POSITION_NOT_UNIQUE);
			} else {
				ECRFOutVO original = ecrfDao.toECRFOutVO(ecrf);
				ecrf.setGroup(null);
				CoreUtil.modifyVersion(ecrf, ecrf.getVersion(), now, user);
				ecrfDao.update(ecrf);
				ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(ecrf);
				ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_GROUP_DELETED_ECRF_UPDATED, ecrfVO, original, journalEntryDao);
			}
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

		// }
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
				shuffleInfo.setPrngClass(random.getClass().getCanonicalName());
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
		// Trial trial = probandListEntry.getTrial(); //locking not required, as uniqueness cannot be harmed by deleting stuff
		// ServiceUtil.checkTrialId(probandListEntry.getTrial().getId(), this.getTrialDao(), LockMode.PESSIMISTIC_WRITE);
		// Proband proband = probandListEntry.getProband();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		return removeProbandListEntry(probandListEntry, now, user, true);
		// ServiceUtil.checkProbandLocked(proband);
		// ProbandListEntryOutVO result = probandListEntryDao.toProbandListEntryOutVO(probandListEntry);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// User user = CoreUtil.getUser();
		// JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		// ProbandGroup group = probandListEntry.getGroup();
		// trial.removeProbandListEntries(probandListEntry);
		// if (group != null) {
		// group.removeProbandListEntries(probandListEntry);
		// }
		// proband.removeTrialParticipations(probandListEntry);
		// ServiceUtil.removeProbandListEntry(probandListEntry, deleteCascade, now, user, true, true, this.getProbandListStatusEntryDao(), this.getProbandListEntryTagValueDao(),
		// this.getECRFFieldValueDao(), this.getSignatureDao(), this.getECRFStatusEntryDao(), this.getInputFieldValueDao(), journalEntryDao, probandListEntryDao,
		// this.getNotificationDao(), this.getNotificationRecipientDao());
		// ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_DELETED, result, null, journalEntryDao);
		// logSystemMessage(proband, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_DELETED, result, null, journalEntryDao);
		// return result;
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
				this.getInputFieldValueDao(), journalEntryDao, probandListEntryTagDao);
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
		if (!result.isDecrypted()) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DECRYPT_PROBAND_LIST_STATUS_ENTRY);
		}
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
		// ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_STATUS_ENTRY_DELETED, result,
		// null,journalEntryDao);
		ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.PROBAND_LIST_STATUS_ENTRY_DELETED, result, null,
				journalEntryDao);
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
	protected TimelineEventOutVO handleDeleteTimelineEvent(AuthenticationVO auth, Long timelineEventId)
			throws Exception {
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent timelineEvent = CheckIDUtil.checkTimelineEventId(timelineEventId, timelineEventDao);
		Trial trial = timelineEvent.getTrial();
		ServiceUtil.checkTrialLocked(trial);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent);
		trial.removeEvents(timelineEvent);
		timelineEvent.setTrial(null);
		ServiceUtil.removeNotifications(timelineEvent.getNotifications(), this.getNotificationDao(), this.getNotificationRecipientDao());
		timelineEventDao.remove(timelineEvent);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.TIMELINE_EVENT_DELETED, result, null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected TrialOutVO handleDeleteTrial(AuthenticationVO auth, Long trialId,  boolean defer,boolean force) throws Exception {
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
			trial.setDeferredDelete(true);
			CoreUtil.modifyVersion(trial, originalTrial.getVersion(), now, user); // no opt. locking
			trialDao.update(trial);
			result = trialDao.toTrialOutVO(trial);
			ServiceUtil.logSystemMessage(trial, result, now, user, SystemMessageCodes.TRIAL_MARKED_FOR_DELETION, result, original, journalEntryDao);
		} else {
			Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao, LockMode.PESSIMISTIC_WRITE);
			result = trialDao.toTrialOutVO(trial);
			//if (deleteCascade) {
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
						probandListEntryTagDao);
			}
			trial.getListEntryTags().clear();
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
				ProbandGroup group = ecrf.getGroup();
				if (group != null) {
					group.removeEcrfs(ecrf);
				}
				Visit visit = ecrf.getVisit();
				if (visit != null) {
					visit.removeEcrfs(ecrf);
				}
				// ecrf.getEcrfFields() empty
				// ecrf.getEcrfStatusEntries() empty
				ecrfDao.remove(ecrf);
			}
			trial.getEcrfs().clear();
			DutyRosterTurnDao dutyRosterTurnDao = this.getDutyRosterTurnDao();
			VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
			Iterator<VisitScheduleItem> visitScheduleItemsIt = trial.getVisitScheduleItems().iterator();
			while (visitScheduleItemsIt.hasNext()) {
				VisitScheduleItem visitScheduleItem = visitScheduleItemsIt.next();
				Trial visitScheduleItemTrial = visitScheduleItem.getTrial();
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
				ServiceUtil.removeNotifications(visitScheduleItem.getNotifications(), notificationDao, notificationRecipientDao);
				visitScheduleItemDao.remove(visitScheduleItem);
			}
			trial.getVisitScheduleItems().clear();
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
			ServiceUtil.removeNotifications(trial.getNotifications(), notificationDao, notificationRecipientDao);
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
			//}
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
		// if (deleteCascade) {
		ECRFDao ecrfDao = this.getECRFDao();
		Iterator<ECRF> ecrfsIt = visit.getEcrfs().iterator();
		while (ecrfsIt.hasNext()) {
			ECRF ecrf = ecrfsIt.next();
			ECRFOutVO original = ecrfDao.toECRFOutVO(ecrf);
			ecrf.setVisit(null);
			CoreUtil.modifyVersion(ecrf, ecrf.getVersion(), now, user);
			ecrfDao.update(ecrf);
			ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(ecrf);
			ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.VISIT_DELETED_ECRF_UPDATED, ecrfVO, original, journalEntryDao);
		}
		visit.getEcrfs().clear();
		NotificationDao notificationDao = this.getNotificationDao();
		NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
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
		// }
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

	@Override
	protected AuditTrailExcelVO handleExportAuditTrail(
			AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId) throws Exception {

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
		if (ecrfId != null) {
			ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
			ecrfVO = ecrfDao.toECRFOutVO(ecrf);
			trial = ecrf.getTrial();
			trialVO = ecrfVO.getTrial();
		}

		boolean passDecryption = CoreUtil.isPassDecryption();
		ECRFFieldStatusQueue[] queues = Settings.getEcrfFieldStatusQueueList(AuditTrailExcelSettingCodes.ECRF_FIELD_STATUS_QUEUES, Bundle.AUDIT_TRAIL_EXCEL,
				AuditTrailExcelDefaultSettings.ECRF_FIELD_STATUS_QUEUES).toArray(new ECRFFieldStatusQueue[0]);
		// ECRFFieldStatusQueue[] queues = new ECRFFieldStatusQueue[] {
		// ECRFFieldStatusQueue.VALIDATION,ECRFFieldStatusQueue.QUERY, ECRFFieldStatusQueue.ANNOTATION
		// };
		AuditTrailExcelWriter writer = new AuditTrailExcelWriter(!passDecryption, queues);
		writer.setTrial(trialVO);
		writer.setListEntry(listEntryVO);
		writer.setEcrf(ecrfVO);


		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		Collection fieldValues = ecrfFieldValueDao.getLog(trialId, probandListEntryId, ecrfId, true, null);
		ecrfFieldValueDao.toECRFFieldValueOutVOCollection(fieldValues);
		// Collections.sort(fieldValues, new XTeamMemberOutVOComparator());
		writer.setVOs(fieldValues);

		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		for (int i = 0; i < queues.length; i++) {
			Collection statusEntries = ecrfFieldStatusEntryDao.getLog(queues[i], trialId, probandListEntryId, ecrfId, false, true, null);
			ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(statusEntries);
			// Collections.sort(statusEntries, new XTeamMemberOutVOComparator());
			writer.setVOs(queues[i],statusEntries);
		}


		User user = CoreUtil.getUser();
		writer.getExcelVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new ExcelExporter(writer, writer)).write();
		AuditTrailExcelVO result = writer.getExcelVO();
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.AUDIT_TRAIL_EXPORTED, result,
				null, this.getJournalEntryDao());

		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected ProbandListExcelVO handleExportProbandList(
			AuthenticationVO auth, Long trialId, ProbandListStatusLogLevel logLevel) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialOutVO trialVO = trialDao.toTrialOutVO(trial);
		boolean passDecryption = CoreUtil.isPassDecryption();
		ProbandListExcelWriter writer = new ProbandListExcelWriter(logLevel, !passDecryption);
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
		}
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		Collection listEntryTags = showProbandListEntryTags ? probandListEntryTagDao.findByTrialExcelEcrfProbandSorted(trialId, showAllProbandListEntryTags
				|| showAllProbandListEntryTagDates ? null : true, null, null) : new ArrayList();
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
		// InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
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
			Collection addresses = showAddresses ? probandAddressDao.findByProband(probandListEntryVO.getProband().getId(), null, null, null, null)
					: new ArrayList<ProbandAddress>();
			probandAddressDao.toProbandAddressOutVOCollection(addresses);
			ServiceUtil.appendDistinctProbandAddressColumnValues(addresses,
					fieldRow,
					aggregateAddresses,
					ProbandListExcelWriter.getStreetsColumnName(),
					ProbandListExcelWriter.getZipCodesColumnName(),
					ProbandListExcelWriter.getCityNamesColumnName());
			Collection contactDetails = showContactDetails ? probandContactDetailValueDao.findByProband(probandListEntryVO.getProband().getId(), null, false, null, null, null)
					: new ArrayList<ProbandContactDetailValue>();
			probandContactDetailValueDao.toProbandContactDetailValueOutVOCollection(contactDetails);
			Iterator<ProbandContactDetailValueOutVO> contactDetailsIt = contactDetails.iterator();
			while (contactDetailsIt.hasNext()) {
				ProbandContactDetailValueOutVO contactDetailOutVO = contactDetailsIt.next();
				StringBuilder fieldValue;
				if (aggregateContactDetails) {
					if (contactDetailOutVO.getType().isEmail()) {
						fieldKey = ProbandListExcelWriter.getEmailContactDetailsColumnName();
					} else if (contactDetailOutVO.getType().isPhone()) {
						fieldKey = ProbandListExcelWriter.getPhoneContactDetailsColumnName();
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
					listEntryTagValueVO = ServiceUtil.createPresetProbandListEntryTagOutValue(probandListEntryVO, listEntryTagVO, null); // inputFieldSelectionSetValueDao
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
									fieldValue = ServiceUtil.selectionSetValuesToString(listEntryTagValueVO.getSelectionValues());
									break;
								case AUTOCOMPLETE:
									fieldValue = listEntryTagValueVO.getTextValue();
									break;
								case SELECT_MANY_H:
								case SELECT_MANY_V:
									fieldValue = ServiceUtil.selectionSetValuesToString(listEntryTagValueVO.getSelectionValues());
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
									break;
								case SKETCH:
									fieldValue = ServiceUtil.selectionSetValuesToString(listEntryTagValueVO.getSelectionValues());
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
					fieldRow.put(fieldKey, DateCalc.getStartOfDay(listEntryTagValueVO.getModifiedTimestamp()));
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
					inquiryValueVO = ServiceUtil.createPresetInquiryOutValue(probandListEntryVO.getProband(), inquiryVO, null); // inputFieldSelectionSetValueDao
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
									fieldValue = ServiceUtil.selectionSetValuesToString(inquiryValueVO.getSelectionValues());
									break;
								case AUTOCOMPLETE:
									fieldValue = inquiryValueVO.getTextValue();
									break;
								case SELECT_MANY_H:
								case SELECT_MANY_V:
									fieldValue = ServiceUtil.selectionSetValuesToString(inquiryValueVO.getSelectionValues());
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
									break;
								case SKETCH:
									fieldValue = ServiceUtil.selectionSetValuesToString(inquiryValueVO.getSelectionValues());
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
					fieldRow.put(fieldKey, DateCalc.getStartOfDay(inquiryValueVO.getModifiedTimestamp()));
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
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.PROBAND_LIST_EXPORTED, result,
				null, this.getJournalEntryDao());
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected Collection<ProbandListEntryOutVO> handleExportProbandListEntries(
			AuthenticationVO auth, Long trialId) throws Exception {
		return null;
	}

	@Override
	protected ProbandListEntryOutVO handleExportProbandListEntry(AuthenticationVO auth, Long probandListEntryId)
			throws Exception {
		return null;
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
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
				SystemMessageCodes.REIMBURSEMENTS_EXPORTED, result, null, this.getJournalEntryDao());
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected TeamMembersExcelVO handleExportTeamMembers(
			AuthenticationVO auth, Long trialId) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialOutVO trialVO = trialDao.toTrialOutVO(trial);
		TeamMembersExcelWriter writer = new TeamMembersExcelWriter(!CoreUtil.isPassDecryption());
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
		Collection staffTags = showTags ? staffTagDao.findByPersonOrganisationIdExcel(null, null, null, true) : new ArrayList(); // staffTagDao.findByVisibleIdExcel(null, null,
		// true)
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
					fieldKey = addressOutVO.getType().getName(); // aggregateAddresses ? ProbandListExcelWriter.getAddressesColumnName() : addressOutVO.getType().getName();
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
						fieldKey = TeamMembersExcelWriter.getEmailContactDetailsColumnName(); //ProbandListExcelWriter
					} else if (contactDetailOutVO.getType().isPhone()) {
						fieldKey = TeamMembersExcelWriter.getPhoneContactDetailsColumnName(); //ProbandListExcelWriter
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
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.TEAM_MEMBERS_EXPORTED, result,
				null, this.getJournalEntryDao());
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected VisitScheduleExcelVO handleExportVisitSchedule(
			AuthenticationVO auth, Long trialId, Long probandId) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		TrialOutVO trialVO = trialDao.toTrialOutVO(trial);
		ProbandDao probandDao = this.getProbandDao();
		Proband proband = null;
		ProbandOutVO probandVO = null;
		if (probandId != null) {
			proband = CheckIDUtil.checkProbandId(probandId, probandDao);
			probandVO = probandDao.toProbandOutVO(proband);
		}
		VisitScheduleExcelWriter.Styles style = probandVO == null ? VisitScheduleExcelWriter.Styles.TRIAL_VISIT_SCHEDULE
				: VisitScheduleExcelWriter.Styles.TRAVEL_EXPENSES_VISIT_SCHEDULE;
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		Collection<VisitScheduleItem> visitScheduleItems;
		switch (style) {
			case TRIAL_VISIT_SCHEDULE:
				visitScheduleItems = trial.getVisitScheduleItems();
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				visitScheduleItems = visitScheduleItemDao.findByTrialGroupVisitProbandTravel(trialVO.getId(), null, null, probandVO.getId(), true, null);
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
			case TRIAL_VISIT_SCHEDULE:
				ServiceUtil.logSystemMessage(trial, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.VISIT_SCHEDULE_EXPORTED, result, null, this.getJournalEntryDao());
				break;
			case TRAVEL_EXPENSES_VISIT_SCHEDULE:
				ServiceUtil.logSystemMessage(proband, result.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), CoreUtil.getUser(),
						SystemMessageCodes.TRAVEL_EXPENSES_VISIT_SCHEDULE_EXPORTED, result, null, this.getJournalEntryDao());
				break;
			default:
		}
		// result.setDocumentDatas(documentDataBackup);
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
		ServiceUtil.populateBookingDurationSummary(true, result, this.getInventoryBookingDao());
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
		if (probandGroup != null) {
			Collection collidingProbandStatusEntries = new HashSet(); // new ArrayList();
			Long probandId = probandListEntry.getProband().getId();
			ProbandStatusEntryDao probandStatusEntryDao = this.getProbandStatusEntryDao();
			Iterator<VisitScheduleItem> it = probandGroup.getVisitScheduleItems().iterator(); // this.getVisitScheduleItemDao().findByTrialGroupVisit(probandListEntry.getTrial().getId(),
			// probandGroup.getId(), null).iterator();
			while (it.hasNext()) {
				VisitScheduleItem visitScheduleItem = it.next();
				collidingProbandStatusEntries.addAll(probandStatusEntryDao.findByProbandInterval(probandId, visitScheduleItem.getStart(), visitScheduleItem.getStop(), false));
			}
			probandStatusEntryDao.toProbandStatusEntryOutVOCollection(collidingProbandStatusEntries);
			return new ArrayList<ProbandStatusEntryOutVO>(collidingProbandStatusEntries);
		} else {
			return new ArrayList<ProbandStatusEntryOutVO>();
		}
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
			collidingProbandStatusEntries = probandStatusEntryDao.findByProbandInterval(probandId, visitScheduleItem.getStart(), visitScheduleItem.getStop(), false);
			probandStatusEntryDao.toProbandStatusEntryOutVOCollection(collidingProbandStatusEntries);
		} else {
			ProbandGroup probandGroup = visitScheduleItem.getGroup();
			if (probandGroup != null) {
				collidingProbandStatusEntries = new HashSet(); // new ArrayList();
				Iterator<ProbandListEntry> it = probandGroup.getProbandListEntries().iterator();
				while (it.hasNext()) {
					ProbandListEntry probandListEntry = it.next();
					collidingProbandStatusEntries.addAll(probandStatusEntryDao.findByProbandInterval(probandListEntry.getProband().getId(), visitScheduleItem.getStart(),
							visitScheduleItem.getStop(), false));
				}
				probandStatusEntryDao.toProbandStatusEntryOutVOCollection(collidingProbandStatusEntries);
				collidingProbandStatusEntries = new ArrayList<ProbandStatusEntryOutVO>(collidingProbandStatusEntries);
			} else {
				collidingProbandStatusEntries = new ArrayList<ProbandStatusEntryOutVO>();
			}
		}
		return collidingProbandStatusEntries;
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
			Long ecrfId, boolean last) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		return this.getECRFFieldStatusEntryDao().getCount(queue, trialId, probandListEntryId, ecrfId, last);

	}

	@Override
	protected long handleGetEcrfFieldStatusEntryCount(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long probandListEntryId, Long ecrfFieldId, Long index, boolean last,
			Boolean initial, Boolean updated, Boolean proposed, Boolean resolved) throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());
		return this.getECRFFieldStatusEntryDao().getCount(queue, probandListEntryId, ecrfFieldId, index, last, initial, updated, proposed, resolved);
		// TODO Auto-generated method stub xx
		// return 0;
	}

	@Override
	protected long handleGetEcrfFieldStatusEntryCount(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long trialId, Long probandListEntryId,
			Long ecrfId, String section, boolean last) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		return this.getECRFFieldStatusEntryDao().getCount(queue, trialId, probandListEntryId, ecrfId, section, last);

	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> handleGetEcrfFieldStatusEntryList(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long probandListEntryId, Long ecrfFieldId,
			Long index, boolean last, boolean sort, PSFVO psf) throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		Collection statusEntries = ecrfFieldStatusEntryDao.findByListEntryEcrfFieldIndex(queue, probandListEntryId, ecrfFieldId, index, last, sort, psf);
		ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(statusEntries);
		return statusEntries;
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> handleGetEcrfFieldStatusEntryLog(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long trialId, Long probandListEntryId,
			Long ecrfId, boolean last, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		Collection statusEntries = ecrfFieldStatusEntryDao.getLog(queue, trialId, probandListEntryId, ecrfId, last, sort, psf);
		ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(statusEntries);
		return statusEntries;
	}

	@Override
	protected Collection<ECRFFieldStatusEntryOutVO> handleGetEcrfFieldStatusEntryLog(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long trialId, Long probandListEntryId,
			Long ecrfId, String section, boolean last, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		Collection statusEntries = ecrfFieldStatusEntryDao.getLog(queue, trialId, probandListEntryId, ecrfId, section, last, sort, psf);
		ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(statusEntries);
		return statusEntries;
	}

	@Override
	protected ECRFFieldValuesOutVO handleGetEcrfFieldValue(AuthenticationVO auth, Long probandListEntryId, Long ecrfFieldId, Long index)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		// ECRFFieldOutVO ecrfFieldVO = ecrfFieldDao.toECRFFieldOutVO(ServiceUtil.checkEcrfFieldId(ecrfFieldId, ecrfFieldDao));
		//InputFieldDao inputFieldDao = this.getInputFieldDao();
		ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldId, ecrfFieldDao);
		// xx
		checkEcrfFieldValueIndex(ecrfField, probandListEntryId, ecrfFieldId, index);
		// if (ecrfField.isSeries()) {
		// if (index == null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NULL,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(probandListEntryId, ecrfFieldId);
		// if (maxIndex == null) {
		// if (index != 0l) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NOT_ZERO,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// } else {
		// if (index < 0) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_LESS_THAN_ZERO,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// } else if (index > (maxIndex + 1l)) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_GAP,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())), maxIndex + 1l);
		// }
		// }
		// } else {
		// if (index != null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NOT_NULL,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// }
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		ECRFFieldValuesOutVO result = new ECRFFieldValuesOutVO();
		Iterator<ECRFFieldValue> it = ecrfFieldValueDao.findByListEntryEcrfFieldIndex(probandListEntryId, ecrfFieldId, index, false, true, null).iterator();
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
					ServiceUtil.createPresetEcrfFieldOutValue(ecrfFieldDao.toECRFFieldOutVO(ecrfField), probandListEntryDao.toProbandListEntryOutVO(listEntry), index, null,
							this.getECRFFieldStatusEntryDao(), this.getECRFFieldStatusTypeDao()));
			if (!CommonUtil.isEmptyString(ecrfField.getJsVariableName()) && ecrfField.getEcrf().isEnableBrowserFieldCalculation()
					&& Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
							DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION)) {
				result.getJsValues().add(ServiceUtil.createPresetEcrfFieldJsonValue(ecrfField, index, this.getInputFieldSelectionSetValueDao()));
			}
		}

		return result;
	}

	@Override
	protected ECRFFieldValuesOutVO handleGetEcrfFieldValue(AuthenticationVO auth, Long probandListEntryId, Long ecrfFieldId, Long index, boolean auditTrail, boolean sort, PSFVO psf)
			throws Exception {
		//ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		//ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		// ECRFFieldOutVO ecrfFieldVO = ecrfFieldDao.toECRFFieldOutVO(ServiceUtil.checkEcrfFieldId(ecrfFieldId, ecrfFieldDao));
		//InputFieldDao inputFieldDao = this.getInputFieldDao();
		ECRFField ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());
		// xx
		checkEcrfFieldValueIndex(ecrfField, probandListEntryId, ecrfFieldId, index);
		// if (ecrfField.isSeries()) {
		// if (index == null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NULL,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// Long maxIndex = this.getECRFFieldValueDao().getMaxIndex(probandListEntryId, ecrfFieldId);
		// if (maxIndex == null) {
		// if (index != 0l) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NOT_ZERO,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// } else {
		// if (index < 0) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_LESS_THAN_ZERO,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// } else if (index > (maxIndex + 1l)) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_GAP,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())), maxIndex + 1l);
		// }
		// }
		// } else {
		// if (index != null) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INDEX_NOT_NULL,
		// CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(ecrfField.getField())));
		// }
		// }
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		ECRFFieldValuesOutVO result = new ECRFFieldValuesOutVO();
		Iterator<ECRFFieldValue> it = ecrfFieldValueDao.findByListEntryEcrfFieldIndex(probandListEntryId, ecrfFieldId, index, auditTrail, sort, psf).iterator();
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
			//		} else if (!auditTrail && psf == null) {
			//			result.getPageValues().add(
			//					ServiceUtil.createPresetEcrfFieldOutValue(ecrfFieldDao.toECRFFieldOutVO(ecrfField), probandListEntryDao.toProbandListEntryOutVO(listEntry), index, null,
			//							this.getECRFFieldStatusEntryDao()));
			//			if (!CommonUtil.isEmptyString(ecrfField.getJsVariableName())) {
			//				result.getJsValues().add(ServiceUtil.createPresetEcrfFieldJsonValue(ecrfField, index, this.getInputFieldSelectionSetValueDao()));
			//			}
		}
		return result;
	}

	@Override
	protected ECRFFieldValueOutVO handleGetEcrfFieldValueById(AuthenticationVO auth, Long ecrfFieldValueId) throws Exception {
		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		return ecrfFieldValueDao.toECRFFieldValueOutVO(CheckIDUtil.checkEcrfFieldValueId(ecrfFieldValueId, ecrfFieldValueDao));
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long ecrfId, Long probandListEntryId) throws Exception {
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		return this.getECRFFieldValueDao().getCount(probandListEntryId, ecrfId);
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long ecrfId, Long probandListEntryId, boolean excludeAuditTrail) throws Exception {
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		return this.getECRFFieldValueDao().getCount(probandListEntryId, ecrfId, excludeAuditTrail, null, null);
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long ecrfId, String section, Long probandListEntryId) throws Exception {
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		return this.getECRFFieldValueDao().getCount(probandListEntryId, ecrfId, section);
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long ecrfId, String section, Long probandListEntryId, boolean excludeAuditTrail) throws Exception {
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		return this.getECRFFieldValueDao().getCount(probandListEntryId, ecrfId, section, excludeAuditTrail, null);
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> handleGetEcrfFieldValueLog(AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId, boolean sort, PSFVO psf)
			throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}

		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		Collection fieldValues = ecrfFieldValueDao.getLog(trialId, probandListEntryId, ecrfId, sort, psf);
		ecrfFieldValueDao.toECRFFieldValueOutVOCollection(fieldValues);

		return fieldValues;
	}

	@Override
	protected Collection<ECRFFieldValueOutVO> handleGetEcrfFieldValueLog(AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId, String section, boolean sort,
			PSFVO psf)
					throws Exception {

		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}

		ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
		Collection fieldValues = ecrfFieldValueDao.getLog(trialId, probandListEntryId, ecrfId, section, sort, psf);
		ecrfFieldValueDao.toECRFFieldValueOutVOCollection(fieldValues);

		return fieldValues;
	}

	@Override
	protected ECRFFieldValuesOutVO handleGetEcrfFieldValues(AuthenticationVO auth, Long ecrfId, Long probandListEntryId, boolean addSeries, boolean loadAllJsValues, PSFVO psf)
			throws Exception {
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		return getEcrfFieldValues(ecrf, listEntryVO, addSeries,
				ecrf.isEnableBrowserFieldCalculation() && Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
						DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION), loadAllJsValues, psf);
	}

	@Override
	protected ECRFFieldValuesOutVO handleGetEcrfFieldValues(AuthenticationVO auth, Long ecrfId, String section, Long probandListEntryId, boolean addSeries,
			boolean loadAllJsValues, PSFVO psf)
					throws Exception {
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		return getEcrfFieldValues(ecrf, section, listEntryVO, addSeries,
				ecrf.isEnableBrowserFieldCalculation() && Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
						DefaultSettings.ECRF_FIELD_VALUES_ENABLE_BROWSER_FIELD_CALCULATION), loadAllJsValues, psf);
	}

	@Override
	protected Long handleGetEcrfFieldValuesSectionMaxIndex(AuthenticationVO auth, Long ecrfId, String section, Long probandListEntryId) throws Exception {
		// ECRF ecrf =
		CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		if (probandListEntryId != null) {
			// ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		//		if (!ecrf.getTrial().equals(listEntry.getTrial())) {
		//			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_FOR_WRONG_TRIAL);
		//		}
		if (this.getECRFFieldDao().getCount(null, ecrfId, section, true, null) == 0l) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_SERIES_SECTION_WITH_NO_FIELDS, section);
		}
		return this.getECRFFieldValueDao().getMaxIndex(probandListEntryId, ecrfId, section);
	}

	@Override
	protected Collection<ECRFOutVO> handleGetEcrfList(AuthenticationVO auth, Long trialId, Boolean active, boolean sort, PSFVO psf) throws Exception {
		// Collection<Map> test = this.getECRFFieldValueDao().findByListEntryEcrfActiveJs(3800331l, 3800200l, null, true, null, null);
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		// if (groupId != null) {
		// ServiceUtil.checkProbandGroupId(groupId, this.getProbandGroupDao());
		// }
		// if (visitId != null) {
		// ServiceUtil.checkVisitId(visitId, this.getVisitDao());
		// }
		ECRFDao ecrfDao = this.getECRFDao();
		Collection ecrfs = ecrfDao.findByTrialActiveSorted(trialId, active, sort, psf);
		ecrfDao.toECRFOutVOCollection(ecrfs);
		return ecrfs;
	}

	@Override
	protected Collection<ECRFOutVO> handleGetEcrfList(AuthenticationVO auth, Long probandListEntryId, Boolean active, PSFVO psf) throws Exception {
		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		ECRFDao ecrfDao = this.getECRFDao();
		Collection ecrfs = ecrfDao.findByListEntryActiveSorted(probandListEntryId, active, false, psf);
		ecrfDao.toECRFOutVOCollection(ecrfs);
		return ecrfs;
	}

	@Override
	protected Collection<ECRFOutVO> handleGetEcrfList(AuthenticationVO auth, Long trialId, Long groupId, Long visitId, Boolean active, boolean sort, PSFVO psf) throws Exception {
		// Collection<Map> test = this.getECRFFieldValueDao().findByListEntryEcrfActiveJs(3800331l, 3800200l, null, true, null, null);
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
	protected Long handleGetEcrfMaxPosition(AuthenticationVO auth, Long trialId, Long groupId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (groupId != null) {
			CheckIDUtil.checkProbandGroupId(groupId, this.getProbandGroupDao());
		}
		return this.getECRFDao().findMaxPosition(trialId, groupId);
	}

	@Override
	protected ECRFProgressVO handleGetEcrfProgress(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, boolean sectionDetail)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		ECRFDao ecrfDao = this.getECRFDao();
		ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(CheckIDUtil.checkEcrfId(ecrfId, ecrfDao));
		return ServiceUtil.populateEcrfProgress(ecrfVO, listEntryVO, sectionDetail,
				this.getECRFStatusEntryDao(), this.getECRFStatusTypeDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao());
	}

	@Override
	protected ECRFProgressVO handleGetEcrfProgress(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, String section)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		ECRFDao ecrfDao = this.getECRFDao();
		ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(CheckIDUtil.checkEcrfId(ecrfId, ecrfDao));
		return ServiceUtil.populateEcrfProgress(ecrfVO, listEntryVO, section,
				this.getECRFStatusEntryDao(), this.getECRFStatusTypeDao(), this.getECRFFieldDao(), this.getECRFFieldValueDao(), this.getECRFFieldStatusEntryDao());
	}

	@Override
	protected ECRFProgressSummaryVO handleGetEcrfProgressSummary(AuthenticationVO auth, Long probandListEntryId, boolean ecrfDetail, boolean sectionDetail)
			throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao));
		ECRFDao ecrfDao = this.getECRFDao();
		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ECRFProgressSummaryVO result = new ECRFProgressSummaryVO();
		result.setListEntry(listEntryVO);
		if (ecrfDetail) {
			result.setEcrfTotalCount(0l);
			// result.setEcrfValidatedCount(0l);
			// result.setEcrfReviewCount(0l);
			// result.setEcrfVerifiedCount(0l);
			result.setEcrfDoneCount(0l);
			// result.setBlank(true);
			// result.setUnresolvedValidationLastFieldStatusCount(0l);
			// result.setUnresolvedQueryLastFieldStatusCount(0l);
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			Iterator<ECRF> it = ecrfDao.findByTrialGroupVisitActiveSorted(listEntryVO.getTrial().getId(),
					listEntryVO.getGroup() != null ? listEntryVO.getGroup().getId() : null, null, true, true, null).iterator();
			while (it.hasNext()) {
				ECRFProgressVO ecrfProgress = ServiceUtil.populateEcrfProgress(ecrfDao.toECRFOutVO(it.next()), listEntryVO, sectionDetail,
						ecrfStatusEntryDao, ecrfStatusTypeDao, ecrfFieldDao, ecrfFieldValueDao, ecrfFieldStatusEntryDao);
				// result.setBlank(result.getBlank() && (ecrfProgress.getStatus() == null));
				result.setEcrfTotalCount(result.getEcrfTotalCount() + 1l);
				if (ecrfProgress.getStatus() != null) {
					// if (ecrfProgress.getStatus().getValidated()) {
					// result.setEcrfValidatedCount(result.getEcrfValidatedCount() + 1l);
					// }
					// if (ecrfProgress.getStatus().getReview()) {
					// result.setEcrfReviewCount(result.getEcrfReviewCount() + 1l);
					// }
					// if (ecrfProgress.getStatus().getVerified()) {
					// result.setEcrfVerifiedCount(result.getEcrfVerifiedCount() + 1l);
					// }
					if (ecrfProgress.getStatus().getDone()) {
						result.setEcrfDoneCount(result.getEcrfDoneCount() + 1l);
					}
				}
				result.setEcrfFieldStatusQueueCounts(ServiceUtil.addEcrfFieldStatusEntryCounts(result.getEcrfFieldStatusQueueCounts(),
						ecrfProgress.getEcrfFieldStatusQueueCounts()));
				// result.setUnresolvedValidationLastFieldStatusCount(result.getUnresolvedValidationLastFieldStatusCount()
				// + ecrfProgress.getUnresolvedValidationLastFieldStatusCount());
				// result.setUnresolvedQueryLastFieldStatusCount(result.getUnresolvedQueryLastFieldStatusCount() + ecrfProgress.getUnresolvedQueryLastFieldStatusCount());
				result.getEcrfs().add(ecrfProgress);
			}
		} else {
			// result.setBlank(this.getECRFStatusEntryDao().getCount(listEntryVO.getId(), null, null, null) == 0l);
			// result.setEcrfCount(ecrfDao.getCount(probandListEntryId, null, true, null, null, null));
			// result.setDoneEcrfCount(ecrfDao.getCount(probandListEntryId, null, true, null, true, null));
			// result.setEcrfTotalCount(ecrfDao.getCount(probandListEntryId, null, true, null));
			// this.getECRFStatusEntryDao().getCount(probandListEntryId, ecrfId, ecrfStatusTypeId, validated, review, verified, done, valueLockdown, fieldStatusLockdown)
			// result.setEcrfValidatedCount(ecrfDao.getCountValidated(probandListEntryId, null, true, true));
			// result.setEcrfReviewCount(ecrfDao.getCountReview(probandListEntryId, null, true, true));
			// result.setEcrfVerifiedCount(ecrfDao.getCountVerified(probandListEntryId, null, true, true));
			// result.setEcrfDoneCount(ecrfDao.getCountDone(probandListEntryId, null, true, true));
			// result.setUnresolvedValidationLastFieldStatusCount(ecrfFieldStatusEntryDao.getCount(ECRFFieldStatusQueue.VALIDATION, listEntryVO.getId(), true, false));
			// result.setUnresolvedQueryLastFieldStatusCount(ecrfFieldStatusEntryDao.getCount(ECRFFieldStatusQueue.QUERY, listEntryVO.getId(), true, false));
			ServiceUtil.populateEcrfFieldStatusEntryCount(result.getEcrfFieldStatusQueueCounts(), listEntryVO.getId(), ecrfFieldStatusEntryDao);
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			ECRFStatusTypeDao ecrfStatusTypeDao = this.getECRFStatusTypeDao();
			Iterator<ECRF> it = ecrfDao.findByTrialGroupVisitActiveSorted(listEntryVO.getTrial().getId(),
					listEntryVO.getGroup() != null ? listEntryVO.getGroup().getId() : null, null, true, true, null).iterator();
			while (it.hasNext()) {
				ECRF ecrf = it.next();
				result.setEcrfTotalCount(result.getEcrfTotalCount() + 1l);
				ECRFProgressVO ecrfProgress = new ECRFProgressVO();
				// result.setEcrf(ecrfVO);
				// result.setListEntry(listEntryVO);
				ECRFStatusEntry statusEntry = ecrfStatusEntryDao.findByEcrfListEntry(ecrf.getId(), listEntryVO.getId());
				if (statusEntry != null) {
					ECRFStatusTypeVO status = ecrfStatusTypeDao.toECRFStatusTypeVO(statusEntry.getStatus());
					ecrfProgress.setStatus(status);
					if (status.getDone()) {
						result.setEcrfDoneCount(result.getEcrfDoneCount() + 1l);
					}
				} else {
					ecrfProgress.setStatus(null);
				}
				result.getEcrfs().add(ecrfProgress);
			}
		}
		return result;
	}

	@Override
	protected SignatureVO handleGetEcrfSignature(AuthenticationVO auth, Long ecrfId, Long probandListEntryId) throws Exception {
		ECRFStatusEntry statusEntry = checkEcrfStatusEntry(ecrfId, probandListEntryId);
		SignatureDao signatureDao = this.getSignatureDao();
		Signature signature = signatureDao.findRecentSignature(SignatureModule.ECRF_SIGNATURE, statusEntry.getId());
		if (signature == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_SIGNATURE);
		}
		SignatureVO result = signatureDao.toSignatureVO(signature);
		result.setDescription(EntitySignature.getDescription(result));
		return result;
	}

	@Override
	protected ECRFStatusEntryVO handleGetEcrfStatusEntry(AuthenticationVO auth, Long ecrfId, Long probandListEntryId) throws Exception {
		ECRFStatusEntry statusEntry = checkEcrfStatusEntry(ecrfId, probandListEntryId);
		return this.getECRFStatusEntryDao().toECRFStatusEntryVO(statusEntry);
		// ServiceUtil.checkEcrfId(ecrfId, this.getECRFDao());
		// ServiceUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		//
		// ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		// ECRFStatusEntry statusEntry = ecrfStatusEntryDao.findByEcrfListEntry(ecrfId, probandListEntryId);
		// if (statusEntry != null) {
		// return ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry);
		// }
		// return null;
	}

	@Override
	protected long handleGetEcrfStatusEntryCount(AuthenticationVO auth, Long probandListEntryId, Long ecrfId, Long ecrfStatusTypeId, Boolean valueLockdown, Boolean validated,
			Boolean review, Boolean verified) throws Exception {
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao());
		}
		if (ecrfStatusTypeId != null) {
			CheckIDUtil.checkEcrfStatusTypeId(ecrfStatusTypeId, this.getECRFStatusTypeDao());
		}
		return this.getECRFStatusEntryDao().getCount(probandListEntryId, ecrfId, ecrfStatusTypeId, valueLockdown, validated,
				review, verified);
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

	// @Override
	// protected InquiryValuesOutVO handleGetInquiryValues(
	// AuthenticationVO auth, Long trialId, Boolean active,
	// boolean sort, PSFVO psf) throws Exception {
	// Trial trial = ServiceUtil.checkTrialId(trialId, this.getTrialDao());
	// InquiryDao inquiryDao = this.getInquiryDao();
	// InquiryValuesOutVO result = new InquiryValuesOutVO();
	// Collection inquiries = inquiryDao.findByTrialActiveJs(trialId, active, sort, null, psf);
	// inquiryDao.toInquiryOutVOCollection(inquiries);
	// result.setPageValues(ServiceUtil.getInquiryValues(null, inquiries, inquiryDao, this.getInquiryValueDao()));
	// result.setJsValues(ServiceUtil.getInquiryJsonValues(trialId, null, inquiryDao, this.getInquiryValueDao(), this.getInputFieldSelectionSetValueDao()));
	// return result;
	// }
	//
	// @Override
	// protected InquiryValuesOutVO handleGetInquiryValues(
	// AuthenticationVO auth, Long trialId, String category, Boolean active,
	// boolean sort, PSFVO psf) throws Exception {
	// Trial trial = ServiceUtil.checkTrialId(trialId, this.getTrialDao());
	// InquiryDao inquiryDao = this.getInquiryDao();
	// InquiryValuesOutVO result = new InquiryValuesOutVO();
	// Collection inquiries = inquiryDao.findByTrialCategoryActiveJs(trialId, category, active, sort, null, psf);
	// inquiryDao.toInquiryOutVOCollection(inquiries);
	// result.setPageValues(ServiceUtil.getInquiryValues(null, inquiries, inquiryDao, this.getInquiryValueDao()));
	// result.setJsValues(ServiceUtil.getInquiryJsonValues(trialId, null, inquiryDao, this.getInquiryValueDao(), this.getInputFieldSelectionSetValueDao()));
	// return result;
	// }
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
			PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findByTrialActiveJs(trialId, active, signupActive, false, null, psf);
		inquiryDao.toInquiryOutVOCollection(inquiries);
		return inquiries;
	}

	@Override
	protected Collection<InquiryOutVO> handleGetInquiryList(AuthenticationVO auth, Long trialId,
			String category, Boolean active, Boolean signupActive, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findByTrialCategoryActiveJs(trialId, category, active, signupActive, false, null, psf);
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
			result.getPageValues().add(ServiceUtil.createPresetInquiryOutValue(null, inquiryDao.toInquiryOutVO(inquiry), null)); // inputFieldSelectionSetValueDao
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
			result.getPageValues().add(ServiceUtil.createPresetInquiryOutValue(null, inquiryDao.toInquiryOutVO(inquiry), null)); // inputFieldSelectionSetValueDao
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
	protected ECRFFieldStatusEntryOutVO handleGetLastEcrfFieldStatusEntry(AuthenticationVO auth, ECRFFieldStatusQueue queue, Long probandListEntryId, Long ecrfFieldId, Long index)
			throws Exception {

		CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao());
		CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());

		ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
		ECRFFieldStatusEntry ecrfFieldStatusEntry = ecrfFieldStatusEntryDao.findLastStatus(queue, probandListEntryId, ecrfFieldId, index);
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
		return ServiceUtil.getProbandListEntryTagJsonValues(probandListEntryTagValueDao.findByListEntryJs(listEntryVO.getId(), sort,  null, psf),
				false, probandListEntryTagValueDao, this.getInputFieldSelectionSetValueDao());
	}

	@Override
	protected Collection<ProbandListEntryTagOutVO> handleGetProbandListEntryTagList(
			AuthenticationVO auth, Long trialId, Long probandId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		if (probandId != null) {
			CheckIDUtil.checkProbandId(probandId, this.getProbandDao());
		}
		ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
		Collection listTagValues = probandListEntryTagDao.findByTrialExcelEcrfProbandSorted(trialId, null, null, probandId);
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
		return getProbandListEntryTagValues(listEntryVO, Settings.getBoolean(SettingCodes.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION, Bundle.SETTINGS,
				DefaultSettings.PROBAND_LIST_ENTRY_TAG_VALUES_ENABLE_BROWSER_FIELD_CALCULATION), loadAllJsValues, sort, psf);
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
		VisitScheduleItem visitScheduleItem = CheckIDUtil.checkVisitScheduleItemId(visitScheduleItemId, this.getVisitScheduleItemDao());
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
		ServiceUtil.populateShiftDurationSummary(true, result, this.getDutyRosterTurnDao(), this.getHolidayDao());
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
	protected TimelineEventOutVO handleGetTimelineEvent(AuthenticationVO auth, Long timelineEventId)
			throws Exception {
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent timelineEvent = CheckIDUtil.checkTimelineEventId(timelineEventId, timelineEventDao);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent);
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
			AuthenticationVO auth, Long trialId, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		Collection timelineEvents = timelineEventDao.findByTrial(trialId, psf);
		timelineEventDao.toTimelineEventOutVOCollection(timelineEvents);
		return timelineEvents;
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
			Boolean ignoreTimelineEvents, PSFVO psf) throws Exception {
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
		Collection timelineEvents = timelineEventDao.findTimelineSchedule(today, trialId, departmentId, teamMemberStaffId, notify, ignoreTimelineEvents, psf);
		timelineEventDao.toTimelineEventOutVOCollection(timelineEvents);
		return timelineEvents;
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
			AuthenticationVO auth, Long trialDepartmentId,
			String costType, PaymentMethod method, Boolean paid, PSFVO psf) throws Exception {
		TrialDao trialDao = this.getTrialDao();
		if (trialDepartmentId != null) {
			CheckIDUtil.checkDepartmentId(trialDepartmentId, this.getDepartmentDao());
		}
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		Collection<String> costTypes = moneyTransferDao.getCostTypes(trialDepartmentId, null, null, null, method);
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		Collection trials = trialDao.findByDepartmentPayoffs(trialDepartmentId, true, psf);
		ArrayList<MoneyTransferSummaryVO> result = new ArrayList<MoneyTransferSummaryVO>(trials.size());
		Iterator it = trials.iterator();
		while (it.hasNext()) {
			TrialOutVO trialVO = trialDao.toTrialOutVO((Trial) it.next());
			MoneyTransferSummaryVO summary = new MoneyTransferSummaryVO();
			Collection<MoneyTransfer> moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialVO.getId(), null, null, method, costType, paid, true,
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
	protected SignatureVO handleGetTrialSignature(AuthenticationVO auth, Long trialId)
			throws Exception {
		Trial trial = CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		SignatureDao signatureDao = this.getSignatureDao();
		Signature signature = signatureDao.findRecentSignature(SignatureModule.TRIAL_SIGNATURE, trialId);
		if (signature == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_SIGNATURE);
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
			AuthenticationVO auth, Long trialId, Long probandGroupId, Long visitId, Long probandId) throws Exception {
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
		return this.getVisitScheduleItemDao().getCount(trialId, probandGroupId, visitId, probandId, null);
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> handleGetVisitScheduleItemInterval(AuthenticationVO auth, Long trialId,
			Long departmentId, Long statusId, Long visitTypeId, Date from, Date to, Long id, boolean sort)
					throws Exception {
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
		if (id != null) {
			CheckIDUtil.checkVisitScheduleItemId(id, visitScheduleItemDao);
		}
		Collection visitScheduleItems = visitScheduleItemDao.findByTrialDepartmentStatusTypeIntervalId(trialId, departmentId, statusId, visitTypeId,
				CommonUtil.dateToTimestamp(from), CommonUtil.dateToTimestamp(to), id);
		visitScheduleItemDao.toVisitScheduleItemOutVOCollection(visitScheduleItems);
		if (sort) {
			visitScheduleItems = new ArrayList(visitScheduleItems);
			Collections.sort((ArrayList) visitScheduleItems, new VisitScheduleItemIntervalComparator(false));
		}
		return visitScheduleItems;
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> handleGetVisitScheduleItemList(
			AuthenticationVO auth, Long trialId, boolean sort, PSFVO psf) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		}
		// if (probandGroupId != null) {
		// ServiceUtil.checkProbandGroupId(probandGroupId, this.getProbandGroupDao());
		// }
		// if (visitId != null) {
		// ServiceUtil.checkVisitId(visitId, this.getVisitDao());
		// }
		VisitScheduleItemDao visitScheduleItemDao = this.getVisitScheduleItemDao();
		Collection visitScheduleItems = visitScheduleItemDao.findByTrialSorted(trialId, sort, psf);
		visitScheduleItemDao.toVisitScheduleItemOutVOCollection(visitScheduleItems);
		return visitScheduleItems;
	}

	@Override
	protected Collection<VisitScheduleItemOutVO> handleGetVisitScheduleItemList(
			AuthenticationVO auth, Long trialId, Long probandGroupId, Long visitId, Long probandId, PSFVO psf) throws Exception {
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
		Collection visitScheduleItems = visitScheduleItemDao.findByTrialGroupVisitProbandTravel(trialId, probandGroupId, visitId, probandId, null, psf);
		visitScheduleItemDao.toVisitScheduleItemOutVOCollection(visitScheduleItems);
		return visitScheduleItems;
	}

	@Override
	protected ECRFOutVO handleMoveEcrf(AuthenticationVO auth, Long ecrfId, PositionMovement movement) throws Exception {
		return (new EcrfMoveAdapter(this.getJournalEntryDao(), this.getECRFDao(), this.getTrialDao())).move(ecrfId, movement);
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
	protected Collection<ECRFOutVO> handleMoveEcrfTo(AuthenticationVO auth, Long ecrfId, Long targetPosition)
			throws Exception {
		return (new EcrfMoveAdapter(this.getJournalEntryDao(), this.getECRFDao(), this.getTrialDao())).moveTo(ecrfId, targetPosition);
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
	protected Collection<ECRFOutVO> handleNormalizeEcrfPositions(
			AuthenticationVO auth, Long groupEcrfId) throws Exception {
		return (new EcrfMoveAdapter(this.getJournalEntryDao(), this.getECRFDao(), this.getTrialDao())).normalizePositions(groupEcrfId);
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
	protected ECRFPDFVO handleRenderEcrf(AuthenticationVO auth, Long ecrfId, String section, Long probandListEntryId, boolean blank) throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
		ProbandListEntryOutVO listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntry);


		ECRFDao ecrfDao = this.getECRFDao();
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
		ECRFOutVO ecrfVO = ecrfDao.toECRFOutVO(ecrf);

		ArrayList<ProbandListEntryOutVO> listEntryVOs = new ArrayList<ProbandListEntryOutVO>();
		HashMap<Long, Collection<ECRFOutVO>> ecrfVOMap = new HashMap<Long, Collection<ECRFOutVO>>();
		HashMap<Long, HashMap<Long, Collection<ECRFFieldValueOutVO>>> valueVOMap = new HashMap<Long, HashMap<Long,Collection<ECRFFieldValueOutVO>>>();
		HashMap<Long, HashMap<Long, HashMap<Long, Collection>>> logVOMap = new HashMap<Long, HashMap<Long, HashMap<Long, Collection>>>();
		HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> listEntryTagValuesVOMap = new HashMap<Long, Collection<ProbandListEntryTagValueOutVO>>();
		HashMap<Long, HashMap<Long, ECRFStatusEntryVO>> statusEntryVOMap = new HashMap<Long, HashMap<Long,ECRFStatusEntryVO>>();
		HashMap<Long, SignatureVO> signatureVOMap = new HashMap<Long, SignatureVO>();
		HashMap<Long, InputFieldImageVO> imageVOMap = new HashMap<Long, InputFieldImageVO>();
		HashSet<Long> ecrfIds=new HashSet<Long>();

		populateEcrfPDFVOMaps(listEntry, listEntryVO, ecrf, ecrfVO, blank,
				getEcrfFieldValues(ecrf, section, listEntryVO, blank, false, false, null).getPageValues(),
				listEntryVOs, ecrfVOMap, valueVOMap, logVOMap, listEntryTagValuesVOMap, statusEntryVOMap, signatureVOMap, imageVOMap, ecrfIds);


		JournalEntryDao journalEntryDao = this.getJournalEntryDao();

		EcrfPDFPainter painter = new EcrfPDFPainter();

		painter.setListEntryVOs(listEntryVOs);
		painter.setEcrfVOMap(ecrfVOMap);
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
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);

		ServiceUtil.logSystemMessage(listEntry.getTrial(), listEntryVO.getProband(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.ECRF_PDF_RENDERED,
				result, null, journalEntryDao);
		ServiceUtil.logSystemMessage(listEntry.getProband(), listEntryVO.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
				SystemMessageCodes.ECRF_PDF_RENDERED,
				result, null,
				journalEntryDao);

		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected ECRFPDFVO handleRenderEcrfs(AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId, boolean blank) throws Exception {

		TrialDao trialDao = this.getTrialDao();
		Trial trial = null;
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
		}
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = null;
		ProbandListEntryOutVO listEntryVO = null;
		if (probandListEntryId != null) {
			listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, probandListEntryDao);
			listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntry);
		}
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF ecrf = null;
		ECRFOutVO ecrfVO = null;
		if (ecrfId != null) {
			ecrf = CheckIDUtil.checkEcrfId(ecrfId, ecrfDao);
			ecrfVO = ecrfDao.toECRFOutVO(ecrf);
		}
		ArrayList<ProbandListEntryOutVO> listEntryVOs = new ArrayList<ProbandListEntryOutVO>();
		HashMap<Long, Collection<ECRFOutVO>> ecrfVOMap = new HashMap<Long, Collection<ECRFOutVO>>();
		HashMap<Long, HashMap<Long, Collection<ECRFFieldValueOutVO>>> valueVOMap = new HashMap<Long, HashMap<Long, Collection<ECRFFieldValueOutVO>>>();
		HashMap<Long, HashMap<Long, HashMap<Long, Collection>>> logVOMap = new HashMap<Long, HashMap<Long, HashMap<Long, Collection>>>();
		HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> listEntryTagValuesVOMap = new HashMap<Long, Collection<ProbandListEntryTagValueOutVO>>();
		HashMap<Long, HashMap<Long, ECRFStatusEntryVO>> statusEntryVOMap = new HashMap<Long, HashMap<Long, ECRFStatusEntryVO>>();
		HashMap<Long, SignatureVO> signatureVOMap = new HashMap<Long, SignatureVO>();
		HashMap<Long, InputFieldImageVO> imageVOMap = new HashMap<Long, InputFieldImageVO>();
		HashSet<Long> ecrfIds = new HashSet<Long>();

		if (listEntry != null) {
			if (ecrf != null) {
				populateEcrfPDFVOMaps(listEntry, listEntryVO, ecrf, ecrfVO, blank,
						getEcrfFieldValues(ecrf, listEntryVO, blank, false, false, null).getPageValues(),
						listEntryVOs, ecrfVOMap, valueVOMap, logVOMap, listEntryTagValuesVOMap, statusEntryVOMap, signatureVOMap, imageVOMap, ecrfIds);
			} else {
				Iterator<ECRF> ecrfIt = ecrfDao.findByListEntryActiveSorted(listEntry.getId(), true, true, null).iterator();
				while (ecrfIt.hasNext()) {
					ecrf = ecrfIt.next();
					ecrfVO = ecrfDao.toECRFOutVO(ecrf);
					populateEcrfPDFVOMaps(listEntry, listEntryVO, ecrf, ecrfVO, blank,
							getEcrfFieldValues(ecrf, listEntryVO, blank, false, false, null).getPageValues(),
							listEntryVOs, ecrfVOMap, valueVOMap, logVOMap, listEntryTagValuesVOMap, statusEntryVOMap, signatureVOMap, imageVOMap, ecrfIds);
				}
			}
		} else if (trial != null) {
			Iterator<ProbandListEntry> listEntryIt = probandListEntryDao.findByTrialProbandSorted(trialId, null).iterator();
			while (listEntryIt.hasNext()) {
				listEntry = listEntryIt.next();
				listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntry);
				if (ecrf != null) {
					populateEcrfPDFVOMaps(listEntry, listEntryVO, ecrf, ecrfVO, blank,
							getEcrfFieldValues(ecrf, listEntryVO, blank, false, false, null).getPageValues(),
							listEntryVOs, ecrfVOMap, valueVOMap, logVOMap, listEntryTagValuesVOMap, statusEntryVOMap, signatureVOMap, imageVOMap, ecrfIds);
				} else {
					Iterator<ECRF> ecrfIt = ecrfDao.findByListEntryActiveSorted(listEntry.getId(), true, true, null).iterator();
					while (ecrfIt.hasNext()) {
						ecrf = ecrfIt.next();
						ecrfVO = ecrfDao.toECRFOutVO(ecrf);
						populateEcrfPDFVOMaps(listEntry, listEntryVO, ecrf, ecrfVO, blank,
								getEcrfFieldValues(ecrf, listEntryVO, blank, false, false, null).getPageValues(),
								listEntryVOs, ecrfVOMap, valueVOMap, logVOMap, listEntryTagValuesVOMap, statusEntryVOMap, signatureVOMap, imageVOMap, ecrfIds);
					}
					ecrf = null;
				}
			}
			listEntry = null;
			// } else {
			// xxx
		}

		//		if (ecrf != null) {
		//			populateEcrfPDFVOMaps(listEntry, listEntryVO, ecrf, ecrfVO, blank,
		//					getEcrfFieldValues(ecrf, listEntryVO, blank, false, false, null).getPageValues(),
		//					listEntryVOs, ecrfVOMap, valueVOMap, logVOMap, listEntryTagValuesVOMap, statusEntryVOMap, signatureVOMap, imageVOMap, ecrfIds);
		//		} else {
		//			Iterator<ECRF> ecrfIt = ecrfDao.findByListEntryActiveSorted(listEntry.getId(), true, true, null).iterator();
		//			while (ecrfIt.hasNext()) {
		//				ecrf = ecrfIt.next();
		//				ecrfVO = ecrfDao.toECRFOutVO(ecrf);
		//				populateEcrfPDFVOMaps(listEntry, listEntryVO, ecrf, ecrfVO, blank,
		//						getEcrfFieldValues(ecrf, listEntryVO, blank, false, false, null).getPageValues(),
		//						listEntryVOs, ecrfVOMap, valueVOMap, logVOMap, listEntryTagValuesVOMap, statusEntryVOMap, signatureVOMap, imageVOMap, ecrfIds);
		//			}
		//		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		EcrfPDFPainter painter = new EcrfPDFPainter();
		painter.setListEntryVOs(listEntryVOs);
		painter.setEcrfVOMap(ecrfVOMap);
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
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		if (listEntry != null) {
			ServiceUtil.logSystemMessage(listEntry.getTrial(), listEntryVO.getProband(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
					ecrf != null ? SystemMessageCodes.ECRF_PDF_RENDERED : SystemMessageCodes.ECRFS_PDF_RENDERED,
							result, null, journalEntryDao);
			ServiceUtil.logSystemMessage(listEntry.getProband(), listEntryVO.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
					ecrf != null ? SystemMessageCodes.ECRF_PDF_RENDERED : SystemMessageCodes.ECRFS_PDF_RENDERED,
							result, null,
							journalEntryDao);
		} else {
			Iterator<ProbandListEntryOutVO> it = listEntryVOs.iterator();
			ProbandDao probandDao = this.getProbandDao();
			while (it.hasNext()) {
				listEntryVO = it.next();
				ServiceUtil.logSystemMessage(trialDao.load(listEntryVO.getTrial().getId()), listEntryVO.getProband(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
						ecrf != null ? SystemMessageCodes.ECRF_PDF_RENDERED : SystemMessageCodes.ECRFS_PDF_RENDERED,
								result, null, journalEntryDao);
				ServiceUtil.logSystemMessage(probandDao.load(listEntryVO.getProband().getId()), listEntryVO.getTrial(), CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
						ecrf != null ? SystemMessageCodes.ECRF_PDF_RENDERED : SystemMessageCodes.ECRFS_PDF_RENDERED,
								result, null,
								journalEntryDao);
			}
		}
		// result.setDocumentDatas(documentDataBackup);
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
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry listEntry = null;
		ProbandListEntryOutVO listEntryVO = null;
		if (trialId != null) {
			trial = CheckIDUtil.checkTrialId(trialId, trialDao);
			trialVO = trialDao.toTrialOutVO(trial);
			listEntry = probandListEntryDao.findByTrialProband(trial.getId(), proband.getId());
			if (listEntry != null) {
				listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntry);
			}
		}
		ArrayList<ProbandListEntryOutVO> listEntryVOs = new ArrayList<ProbandListEntryOutVO>();
		HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> valueVOMap = new HashMap<Long, Collection<ProbandListEntryTagValueOutVO>>();
		HashMap<Long, InputFieldImageVO> imageVOMap = new HashMap<Long, InputFieldImageVO>();
		if (trial != null) {
			populateProbandListEntryTagsPDFVOMaps(listEntry, listEntryVO,
					getProbandListEntryTagValues(listEntryVO, false, false, true, null).getPageValues(),
					listEntryVOs, valueVOMap, imageVOMap);
		} else {
			Iterator<ProbandListEntry> listEntryIt = probandListEntryDao.findByTrialProbandSorted(null, proband.getId()).iterator();
			while (listEntryIt.hasNext()) {
				listEntry = listEntryIt.next();
				listEntryVO = probandListEntryDao.toProbandListEntryOutVO(listEntry);
				populateProbandListEntryTagsPDFVOMaps(listEntry, listEntryVO,
						getProbandListEntryTagValues(listEntryVO, false, false, true, null).getPageValues(),
						listEntryVOs, valueVOMap, imageVOMap);
			}
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ProbandListEntryTagsPDFPainter painter = new ProbandListEntryTagsPDFPainter();
		painter.setListEntryVOs(listEntryVOs);
		painter.setValueVOMap(valueVOMap);
		painter.setImageVOMap(imageVOMap);
		painter.setBlank(blank);
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		ProbandListEntryTagsPDFVO result = painter.getPdfVO();
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		if (trial != null) {
			ServiceUtil.logSystemMessage(trial, probandVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_PDF_RENDERED,
					result, null, journalEntryDao);
		}
		ServiceUtil.logSystemMessage(proband, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
				trial != null ? SystemMessageCodes.PROBAND_LIST_ENTRY_TAG_PDF_RENDERED
						: SystemMessageCodes.PROBAND_LIST_ENTRY_TAGS_PDF_RENDERED,
						result, null,
						journalEntryDao);
		// result.setDocumentDatas(documentDataBackup);
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
		MoneyTransferDao moneyTransferDao = this.getMoneyTransferDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Collection trialTagValues = null;
		Collection<String> costTypes = null;
		Iterator<MoneyTransfer> moneyTransfersIt = null;
		if (trialVO != null) {
			TrialTagValueDao trialTagValueDao = this.getTrialTagValueDao();
			trialTagValues = trialTagValueDao.findByTrialExcelPayoffsSorted(trialVO.getId(), true, null);
			trialTagValueDao.toTrialTagValueOutVOCollection(trialTagValues);
			costTypes = moneyTransferDao.getCostTypes(null, trialVO.getId(), null, null, method);
			moneyTransfersIt = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialVO.getId(), null, probandVO == null ? null : probandVO.getId(), method, null,
					paid,
					true, null).iterator();
		} else if (probandVO != null) {
			costTypes = moneyTransferDao.getCostTypesNoTrial(probandVO.getId(), method);
			moneyTransfersIt = moneyTransferDao.findByProbandNoTrialMethodCostTypePaid(probandVO.getId(), method, null, paid).iterator();
		}
		BankAccountDao bankAccountDao = this.getBankAccountDao();
		ProbandAddressDao probandAddressDao = this.getProbandAddressDao();
		TreeSet<ProbandOutVO> probandVOs = new TreeSet<ProbandOutVO>(new ProbandOutVOComparator());
		HashMap<Long, MoneyTransferSummaryVO> summaryMap = new HashMap<Long, MoneyTransferSummaryVO>();
		HashMap<Long, ProbandAddressOutVO> addressVOMap = new HashMap<Long, ProbandAddressOutVO>();
		if (moneyTransfersIt != null) {
			while (moneyTransfersIt.hasNext()) {
				MoneyTransfer moneyTransfer = moneyTransfersIt.next();
				Proband moneyTransferProband = moneyTransfer.getProband();
				if (moneyTransferProband.isPerson() && probandVOs.add(probandDao.toProbandOutVO(moneyTransferProband))) { // && !moneyTransferProband.isBlinded()
					MoneyTransferOutVO moneyTransferVO = moneyTransferDao.toMoneyTransferOutVO(moneyTransfer);
					MoneyTransferSummaryVO summary = new MoneyTransferSummaryVO();
					Collection<MoneyTransfer> moneyTransfers;
					if (trialVO != null) {
						moneyTransfers = moneyTransferDao.findByProbandTrialMethodCostTypePaidPerson(null, trialVO.getId(), null, moneyTransferVO.getProband().getId(), method,
								null,
								paid, null, null);
					} else {
						moneyTransfers = moneyTransferDao.findByProbandNoTrialMethodCostTypePaid(moneyTransferVO.getProband().getId(), method, null, paid);
					}
					ServiceUtil.populateMoneyTransferSummary(summary, costTypes, moneyTransfers, true, true, true, true, bankAccountDao);
					summary.setListEntry(null);
					summary.setTrial(trialVO);
					summary.setProband(moneyTransferVO.getProband());
					summary.setId(moneyTransferVO.getProband().getId());
					addressVOMap.put(moneyTransferVO.getProband().getId(),
							probandAddressDao.toProbandAddressOutVO(probandAddressDao.findByProbandWireTransfer(moneyTransferVO.getProband().getId())));
					summaryMap.put(moneyTransferVO.getProband().getId(), summary);
				}
			}
		}
		ReimbursementsPDFPainter painter = new ReimbursementsPDFPainter();
		painter.setTrialVO(trialVO);
		painter.setTrialTagValueVOs(trialTagValues);
		painter.setProbandVOs(probandVOs);
		painter.setSummaryMap(summaryMap);
		painter.setAddressVOMap(addressVOMap);
		User user = CoreUtil.getUser();
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(user));
		(new PDFImprinter(painter, painter)).render();
		ReimbursementsPDFVO result = painter.getPdfVO();
		// byte[] documentDataBackup = result.getDocumentDatas();
		// result.setDocumentDatas(null);
		if (trialVO != null) {
			if (probandVO != null) {
				ServiceUtil.logSystemMessage(trial, probandVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.REIMBURSEMENTS_PDF_RENDERED,
						result, null, journalEntryDao);
				ServiceUtil.logSystemMessage(proband, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.REIMBURSEMENTS_PDF_RENDERED,
						result, null,
						journalEntryDao);
			} else {
				ServiceUtil.logSystemMessage(trial, trialVO, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user, SystemMessageCodes.REIMBURSEMENTS_PDF_RENDERED,
						result, null, journalEntryDao);
			}
		} else if (probandVO != null) {
			ServiceUtil.logSystemMessage(proband, (TrialOutVO) null, CommonUtil.dateToTimestamp(result.getContentTimestamp()), user,
					SystemMessageCodes.REIMBURSEMENTS_PDF_NO_TRIAL_RENDERED, result, null,
					journalEntryDao);
		}
		// result.setDocumentDatas(documentDataBackup);
		return result;
	}

	@Override
	protected ECRFFieldValuesOutVO handleSetEcrfFieldValues(AuthenticationVO auth, Set<ECRFFieldValueInVO> ecrfFieldValuesIns, Boolean addSeries, PSFVO psf) throws Exception {
		boolean loadPageResult = addSeries != null && psf != null;
		Object[] resultItems = setEcrfFieldValues(ecrfFieldValuesIns, loadPageResult);
		ECRFFieldValuesOutVO result;
		if (loadPageResult) {
			result = getEcrfFieldValues((ECRF) resultItems[1], (ProbandListEntryOutVO) resultItems[2], addSeries, false, false, psf);
			result.setJsValues(((ECRFFieldValuesOutVO) resultItems[0]).getJsValues());
		} else {
			result = (ECRFFieldValuesOutVO) resultItems[0];
		}
		return result;
	}

	@Override
	protected ECRFFieldValuesOutVO handleSetEcrfFieldValues(AuthenticationVO auth, Set<ECRFFieldValueInVO> ecrfFieldValuesIns, String section, Boolean addSeries, PSFVO psf)
			throws Exception {
		boolean loadPageResult = addSeries != null && psf != null;
		Object[] resultItems = setEcrfFieldValues(ecrfFieldValuesIns, loadPageResult);
		ECRFFieldValuesOutVO result;
		if (loadPageResult) {
			result = getEcrfFieldValues((ECRF) resultItems[1], section, (ProbandListEntryOutVO) resultItems[2], addSeries, false, false, psf);
			result.setJsValues(((ECRFFieldValuesOutVO) resultItems[0]).getJsValues());
		} else {
			return (ECRFFieldValuesOutVO) resultItems[0];
		}
		return result;
	}

	@Override
	protected ECRFStatusEntryVO handleSetEcrfStatusEntry(AuthenticationVO auth, Long ecrfId, Long probandListEntryId, Long version, Long ecrfStatusTypeId,
			Long probandListStatusTypeId)
					throws Exception {
		ProbandListEntry listEntry = CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
		ECRF ecrf = CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao(), LockMode.PESSIMISTIC_WRITE); // lock order, field updates
		ECRFStatusType statusType = CheckIDUtil.checkEcrfStatusTypeId(ecrfStatusTypeId, this.getECRFStatusTypeDao());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		ECRFStatusEntry originalStatusEntry = this.getECRFStatusEntryDao().findByEcrfListEntry(ecrf.getId(), listEntry.getId());
		ECRFStatusEntryVO result;
		if (originalStatusEntry != null) {
			result = updateEcrfStatusEntry(originalStatusEntry, ecrf, listEntry, statusType, version, probandListStatusTypeId, now, user);
		} else {
			Object[] resultItems = addEcrfStatusEntry(ecrf, listEntry, statusType, probandListStatusTypeId, now, user);
			result = (ECRFStatusEntryVO) resultItems[1];
		}
		return result;
	}

	@Override
	protected ProbandListEntryTagValuesOutVO handleSetProbandListEntryTagValues(
			AuthenticationVO auth, Set<ProbandListEntryTagValueInVO> probandListEntryTagValuesIn)
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
					addUpdateProbandListEntryTagValue(probandListEntryTagValueIn, listEntry, listEntryTag, now, user, ServiceUtil.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_TRIAL,
							ServiceUtil.LOG_PROBAND_LIST_ENTRY_TAG_VALUE_PROBAND, tagValues, jsTagValues);
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
			AuthenticationVO auth, Long timelineEventId, Long version, boolean dismissed) throws Exception {
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent timelineEvent = CheckIDUtil.checkTimelineEventId(timelineEventId, timelineEventDao);
		TimelineEventOutVO original = timelineEventDao.toTimelineEventOutVO(timelineEvent);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(timelineEvent, version.longValue(), now, user);
		timelineEvent.setDismissed(dismissed);
		timelineEvent.setDismissedTimestamp(now);
		timelineEventDao.update(timelineEvent);
		notifyTimelineEventReminder(timelineEvent, now);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent);
		ServiceUtil.logSystemMessage(timelineEvent.getTrial(), result.getTrial(), now, user, dismissed ? SystemMessageCodes.TIMELINE_EVENT_DISMISSED_SET
				: SystemMessageCodes.TIMELINE_EVENT_DISMISSED_UNSET, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<ECRFStatusEntryVO> handleSignVerifiedEcrfs(AuthenticationVO auth, Long trialId, Long probandListEntryId) throws Exception {
		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao(),LockMode.PESSIMISTIC_WRITE);
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao(),LockMode.PESSIMISTIC_WRITE);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();

		ArrayList<ECRFStatusEntryVO> results = new ArrayList<ECRFStatusEntryVO>();
		Iterator<ECRFStatusEntry> statusEntryIt = this.getECRFStatusEntryDao().findByTrialListEntryValidatedReviewVerified(trialId, probandListEntryId, null,null,true, null).iterator();
		while (statusEntryIt.hasNext()) {
			ECRFStatusEntry statusEntry = statusEntryIt.next();
			Iterator<ECRFStatusType> it = statusEntry.getStatus().getTransitions().iterator();
			ECRFStatusType newStatus = null;
			while (it.hasNext()) {
				ECRFStatusType status = it.next();
				if (hasEcrfStatusAction(status, org.phoenixctms.ctsms.enumeration.ECRFStatusAction.SIGN_ECRF)) {
					newStatus = status;
					break;
				}
			}
			if (newStatus != null) {
				checkTeamMemberSign(statusEntry.getListEntry().getTrial(), user);
				ECRFStatusEntryVO result = updateEcrfStatusEntry(statusEntry, statusEntry.getEcrf(), statusEntry.getListEntry(), newStatus, statusEntry.getVersion(), null, now, user);
				results.add(result);
			}
		}
		return results;
	}

	@Override
	protected ECRFOutVO handleUpdateEcrf(AuthenticationVO auth, ECRFInVO modifiedEcrf) throws Exception {
		ECRFDao ecrfDao = this.getECRFDao();
		ECRF originalEcrf = CheckIDUtil.checkEcrfId(modifiedEcrf.getId(), ecrfDao);
		checkUpdateEcrfInput(modifiedEcrf, originalEcrf);
		ECRFOutVO original = ecrfDao.toECRFOutVO(originalEcrf);
		ecrfDao.evict(originalEcrf);
		ECRF ecrf = ecrfDao.eCRFInVOToEntity(modifiedEcrf);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
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
		// long valuesLockedEcrfCount = this.getECRFStatusEntryDao().getCount(null, ecrfId, null, true, null, null, null); // row lock order
		// if (valuesLockedEcrfCount > 0) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.LOCKED_ECRFS, ecrfDao.toECRFOutVO(ecrf).getName(), valuesLockedEcrfCount);
		// }
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
			AuthenticationVO auth, ProbandListEntryInVO modifiedProbandListEntry, Long probandListStatusTypeId) throws Exception {
		ProbandListEntryDao probandListEntryDao = this.getProbandListEntryDao();
		ProbandListEntry originalProbandListEntry = CheckIDUtil.checkProbandListEntryId(modifiedProbandListEntry.getId(), probandListEntryDao);
		ProbandListStatusEntry lastStatus = originalProbandListEntry.getLastStatus();
		checkProbandListEntryInput(modifiedProbandListEntry, false, lastStatus == null ? null : lastStatus.getRealTimestamp()); // access original associations before evict
		if (!modifiedProbandListEntry.getTrialId().equals(originalProbandListEntry.getTrial().getId())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.PROBAND_LIST_ENTRY_TRIAL_CHANGED);
		}
		boolean probandChanged = !modifiedProbandListEntry.getProbandId().equals(originalProbandListEntry.getProband().getId());
		boolean groupChanged;
		if (modifiedProbandListEntry.getGroupId() != null && originalProbandListEntry.getGroup() != null) {
			groupChanged = !modifiedProbandListEntry.getGroupId().equals(originalProbandListEntry.getGroup().getId());
		} else if (modifiedProbandListEntry.getGroupId() == null && originalProbandListEntry.getGroup() != null) {
			groupChanged = true;
		} else if (modifiedProbandListEntry.getGroupId() != null && originalProbandListEntry.getGroup() == null) {
			groupChanged = true;
		} else {
			groupChanged = false;
		}
		ProbandListEntryOutVO original = probandListEntryDao.toProbandListEntryOutVO(originalProbandListEntry);
		probandListEntryDao.evict(originalProbandListEntry);
		ProbandListEntry probandListEntry = probandListEntryDao.probandListEntryInVOToEntity(modifiedProbandListEntry);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalProbandListEntry, probandListEntry, now, user);
		probandListEntryDao.update(probandListEntry);
		if (probandChanged || groupChanged) {
			addProbandListEntryUpdatedProbandListStatusEntry(ProbandListStatusReasonCodes.LIST_ENTRY_UPDATED, ProbandListStatusReasonCodes.LIST_ENTRY_UPDATED_NO_GROUP,
					probandListEntry, probandListStatusTypeId, now, user);
		}
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		ProbandListEntryOutVO result = probandListEntryDao.toProbandListEntryOutVO(probandListEntry);

		ServiceUtil.logSystemMessage(probandListEntry.getProband(), result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_UPDATED, result, original, journalEntryDao);
		// ServiceUtil.logSystemMessage(probandListEntry.getTrial(), result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_UPDATED, result, original,
		// journalEntryDao);
		ServiceUtil.logSystemMessage(probandListEntry.getTrial(), result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_UPDATED, result, original, journalEntryDao);
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


	@Override
	protected TimelineEventOutVO handleUpdateTimelineEvent(
			AuthenticationVO auth, TimelineEventInVO modifiedTimelineEvent) throws Exception {
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		TimelineEvent originalTimelineEvent = CheckIDUtil.checkTimelineEventId(modifiedTimelineEvent.getId(), timelineEventDao);
		checkTimelineEventInput(modifiedTimelineEvent);
		TimelineEventOutVO original = timelineEventDao.toTimelineEventOutVO(originalTimelineEvent);
		timelineEventDao.evict(originalTimelineEvent);
		TimelineEvent timelineEvent = timelineEventDao.timelineEventInVOToEntity(modifiedTimelineEvent);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalTimelineEvent, timelineEvent, now, user);
		timelineEvent.setDismissedTimestamp(now);
		timelineEventDao.update(timelineEvent);
		notifyTimelineEventReminder(timelineEvent, now);
		TimelineEventOutVO result = timelineEventDao.toTimelineEventOutVO(timelineEvent);
		ServiceUtil
		.logSystemMessage(timelineEvent.getTrial(), result.getTrial(), now, user, SystemMessageCodes.TIMELINE_EVENT_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}




	@Override
	protected TrialOutVO handleUpdateTrial(AuthenticationVO auth, TrialInVO modifiedTrial)
			throws Exception {
		TrialDao trialDao = this.getTrialDao();
		Trial originalTrial = CheckIDUtil.checkTrialId(modifiedTrial.getId(), trialDao, LockMode.PESSIMISTIC_WRITE);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		checkUpdateTrialInput(originalTrial, modifiedTrial, user);
		TrialStatusType originalTrialStatusType = originalTrial.getStatus();
		TrialOutVO original = trialDao.toTrialOutVO(originalTrial);
		this.getTrialStatusTypeDao().evict(originalTrialStatusType);
		trialDao.evict(originalTrial);
		Trial trial = trialDao.trialInVOToEntity(modifiedTrial);
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalTrial, trial, now, user);
		trialDao.update(trial);
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
		notifyVisitScheduleItemReminder(visitScheduleItem, now);
		VisitScheduleItemOutVO result = visitScheduleItemDao.toVisitScheduleItemOutVO(visitScheduleItem);
		ServiceUtil.logSystemMessage(visitScheduleItem.getTrial(), result.getTrial(), now, user, SystemMessageCodes.VISIT_SCHEDULE_ITEM_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected Collection<ECRFStatusEntryVO> handleValidatePendingEcrfs(AuthenticationVO auth, Long trialId, Long probandListEntryId, Long ecrfId) throws Exception {

		if (trialId != null) {
			CheckIDUtil.checkTrialId(trialId, this.getTrialDao(),LockMode.PESSIMISTIC_WRITE);
		}
		if (probandListEntryId != null) {
			CheckIDUtil.checkProbandListEntryId(probandListEntryId, this.getProbandListEntryDao(),LockMode.PESSIMISTIC_WRITE);
		}
		if (ecrfId != null) {
			CheckIDUtil.checkEcrfId(ecrfId, this.getECRFDao(),LockMode.PESSIMISTIC_WRITE);
		}
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();

		ArrayList<ECRFStatusEntryVO> results = new ArrayList<ECRFStatusEntryVO>();
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Iterator<ECRFStatusEntry> statusEntryIt = ecrfStatusEntryDao.findByTrialListEntryEcrfValidationStatusExportStatus(trialId, probandListEntryId, ecrfId, ECRFValidationStatus.PENDING, null, null).iterator();
		while (statusEntryIt.hasNext()) {
			ECRFStatusEntry statusEntry = statusEntryIt.next();
			ECRFStatusEntryVO original = ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry);
			if (!original.getListEntry().getProband().getDecrypted()) {
				continue; // prevent unexpected validationcheck ecrf issues, when subject PII fields
			}
			// CoreUtil.modifyVersion(statusEntry, version.longValue(), now, user);
			boolean noMissingValues = false;
			if (hasEcrfStatusAction(statusEntry.getStatus(), org.phoenixctms.ctsms.enumeration.ECRFStatusAction.NO_MISSING_VALUES)) {
				try {
					checkMissingEcrfFieldValuesDeeply(statusEntry.getListEntry(), statusEntry.getEcrf());
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
			Long ecrfId, Long probandListEntryId) throws Exception {
		ECRFStatusEntry statusEntry = checkEcrfStatusEntry(ecrfId, probandListEntryId);
		Signature signature = this.getSignatureDao().findRecentSignature(SignatureModule.ECRF_SIGNATURE, statusEntry.getId());
		if (signature == null) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.NO_SIGNATURE);
		}
		return getVerifiedEcrfSignatureVO(signature);
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

	private void logSystemMessage(Trial trial, TrialOutVO trialVO, Timestamp now, User user, ShuffleInfoVO shuffleInfo, String systemMessageCode) throws Exception {
		this.getJournalEntryDao().addSystemMessage(trial, now, user, systemMessageCode,
				new Object[] { CommonUtil.trialOutVOToString(trialVO), shuffleInfo != null ? Integer.toString(shuffleInfo.getResultIds().size()) : null,
						shuffleInfo != null ? Integer.toString(shuffleInfo.getInputIds().size()) : null },
						systemMessageCode,
						new Object[] { CoreUtil.getSystemMessageCommentContent(shuffleInfo, null, !CommonUtil.getUseJournalEncryption(JournalModule.TRIAL_JOURNAL, null)) });
	}

	private void notifyEcrfFieldStatus(ECRFFieldStatusEntry lastStatus, ECRFFieldStatusEntry newStatus, ECRFFieldStatusQueue queue, Date now) throws Exception {

		//		if (lastStatus != null) {
		//			ServiceUtil.cancelNotifications(lastStatus.getNotifications(), this.getNotificationDao(),
		//					org.phoenixctms.ctsms.enumeration.NotificationType.NEW_ECRF_);
		//			}
		//
		//		if (ECRFFieldStatusQueue.QUERY.equals(newStatus.getQueue())) {
		//
		//		}
		//
		//		if (Arrays.asList(Settings.getEcrfFieldStatusQueueList(SettingCodes.NOTIFICATION_ECRF_FIELD_STATUS_QUEUES, Bundle.SETTINGS, DefaultSettings.NOTIFICATION_ECRF_FIELD_STATUS_QUEUES)).contains(queue)) {
		//
		//			this.getNotificationDao().addNotification(newStatus,now,null);
		//		}

		if (newStatus != null
				// && (newStatus.getStatus().isInitial() || newStatus.getStatus().isResolved())
				&& Settings.getEcrfFieldStatusQueueList(SettingCodes.NEW_ECRF_FIELD_STATUS_NOTIFICATION_QUEUES, Bundle.SETTINGS,
						DefaultSettings.NEW_ECRF_FIELD_STATUS_NOTIFICATION_QUEUES)
						.contains(
								queue)) {

			if (lastStatus != null) {
				ServiceUtil.cancelNotifications(lastStatus.getNotifications(), this.getNotificationDao(),
						org.phoenixctms.ctsms.enumeration.NotificationType.NEW_ECRF_FIELD_STATUS);
			}
			Map messageParameters = CoreUtil.createEmptyTemplateModel();
			// new ECRFFieldStatusQueue[0]
			messageParameters.put(NotificationMessageTemplateParameters.INDEX_FIELD_LOG,
					getIndexFieldLog(newStatus.getListEntry().getId(),
							newStatus.getEcrfField().getId(), newStatus.getIndex(), false, true, false, true, ECRFFieldStatusQueue.values()));
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

	private void notifyVisitScheduleItemReminder(VisitScheduleItem visitScheduleItem, Date now) throws Exception {
		ReminderEntityAdapter reminderItem = ReminderEntityAdapter.getInstance(visitScheduleItem);
		VariablePeriod visitScheduleItemReminderPeriod = Settings.getVariablePeriod(SettingCodes.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD);
		Long visitScheduleItemReminderPeriodDays = Settings.getLongNullable(SettingCodes.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD_DAYS, Settings.Bundle.SETTINGS,
				DefaultSettings.NOTIFICATION_VISIT_SCHEDULE_ITEM_REMINDER_PERIOD_DAYS);
		Date reminderStart = reminderItem.getReminderStart(now, false, visitScheduleItemReminderPeriod, visitScheduleItemReminderPeriodDays);
		if (reminderItem.isNotify() && now.compareTo(reminderStart) >= 0 && now.compareTo(visitScheduleItem.getStop()) <= 0
				&& !visitScheduleItem.getTrial().getStatus().isIgnoreTimelineEvents()) {
			this.getNotificationDao().addNotification(visitScheduleItem, now, null);
		} else {
			ServiceUtil
			.cancelNotifications(visitScheduleItem.getNotifications(), this.getNotificationDao(),
					org.phoenixctms.ctsms.enumeration.NotificationType.VISIT_SCHEDULE_ITEM_REMINDER);
		}
	}

	private void populateEcrfPDFVOMaps(
			ProbandListEntry listEntry,
			ProbandListEntryOutVO listEntryVO,
			ECRF ecrf,
			ECRFOutVO ecrfVO,
			boolean blank,
			Collection<ECRFFieldValueOutVO> ecrfValues,
			ArrayList<ProbandListEntryOutVO> listEntryVOs,
			HashMap<Long, Collection<ECRFOutVO>> ecrfVOMap,
			HashMap<Long, HashMap<Long, Collection<ECRFFieldValueOutVO>>> valueVOMap,
			HashMap<Long, HashMap<Long, HashMap<Long, Collection>>> logVOMap,
			HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> listEntryTagValuesVOMap,
			HashMap<Long, HashMap<Long, ECRFStatusEntryVO>> statusEntryVOMap,
			HashMap<Long, SignatureVO> signatureVOMap,
			HashMap<Long, InputFieldImageVO> imageVOMap,
			HashSet<Long> ecrfIds
			) throws Exception {
		if (ecrfValues.size() > 0) {
			boolean auditTrail = Settings.getBoolean(EcrfPDFSettingCodes.AUDIT_TRAIL, Bundle.ECRF_PDF, EcrfPDFDefaultSettings.AUDIT_TRAIL);
			ECRFFieldStatusQueue[] queues = Settings.getEcrfFieldStatusQueueList(EcrfPDFSettingCodes.ECRF_FIELD_STATUS_QUEUES, Bundle.ECRF_PDF,
					EcrfPDFDefaultSettings.ECRF_FIELD_STATUS_QUEUES).toArray(new ECRFFieldStatusQueue[0]);
			boolean showProbandListEntryTags = Settings.getBoolean(EcrfPDFSettingCodes.SHOW_PROBAND_LIST_ENTRY_TAGS, Bundle.ECRF_PDF,
					EcrfPDFDefaultSettings.SHOW_PROBAND_LIST_ENTRY_TAGS);
			boolean showAllProbandListEntryTags = Settings.getBoolean(EcrfPDFSettingCodes.SHOW_ALL_PROBAND_LIST_ENTRY_TAGS, Bundle.ECRF_PDF,
					EcrfPDFDefaultSettings.SHOW_ALL_PROBAND_LIST_ENTRY_TAGS);
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
			SignatureDao signatureDao = this.getSignatureDao();
			InputFieldDao inputFieldDao = this.getInputFieldDao();
			// EcrfFieldValueStatusEntryOutVOComparator ecrfFieldValueStatusEntryOutVOComparator = new EcrfFieldValueStatusEntryOutVOComparator(true);
			Collection<ECRFOutVO> ecrfVOs;
			if (ecrfVOMap.containsKey(listEntry.getId())) {
				ecrfVOs = ecrfVOMap.get(listEntry.getId());
			} else {
				ecrfVOs = new ArrayList<ECRFOutVO>();
				ecrfVOMap.put(listEntry.getId(), ecrfVOs);
				listEntryVOs.add(listEntryVO);
			}
			ecrfVOs.add(ecrfVO);
			if (!listEntryTagValuesVOMap.containsKey(listEntry.getId())) {
				Collection listEntryTags = showProbandListEntryTags ? this.getProbandListEntryTagDao().findByTrialExcelEcrfProbandSorted(listEntry.getTrial().getId(),
						null, showAllProbandListEntryTags
						? null : true, null) : new ArrayList();
				// probandListEntryTagDao.toProbandListEntryTagOutVOCollection(listEntryTags);
				Collection<ProbandListEntryTagValueOutVO> listEntryTagValueVOs = new ArrayList<ProbandListEntryTagValueOutVO>(listEntryTags.size());
				listEntryTagValuesVOMap.put(listEntry.getId(), listEntryTagValueVOs);
				ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
				ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
				Iterator listEntryTagsIt = listEntryTags.iterator();
				while (listEntryTagsIt.hasNext()) {
					ProbandListEntryTag listEntryTag = (ProbandListEntryTag) listEntryTagsIt.next();
					Iterator<ProbandListEntryTagValue> listEntryTagValueIt = probandListEntryTagValueDao.findByListEntryListEntryTag(listEntry.getId(), listEntryTag.getId())
							.iterator();
					ProbandListEntryTagValueOutVO listEntryTagValueVO;
					if (listEntryTagValueIt.hasNext()) {
						listEntryTagValueVO = probandListEntryTagValueDao.toProbandListEntryTagValueOutVO(listEntryTagValueIt.next());
					} else {
						listEntryTagValueVO = ServiceUtil.createPresetProbandListEntryTagOutValue(listEntryVO,
								probandListEntryTagDao.toProbandListEntryTagOutVO(listEntryTag), Locales.ECRF_PDF);
					}
					listEntryTagValueVOs.add(listEntryTagValueVO);
					InputFieldOutVO field = listEntryTagValueVO.getTag().getField();
					if (InputFieldType.SKETCH.equals(field.getFieldType().getType()) && !imageVOMap.containsKey(field.getId())) {
						// if (field.getHasImage() ) {
						imageVOMap.put(field.getId(), inputFieldDao.toInputFieldImageVO(inputFieldDao.load(field.getId())));
						// }
					}
				}
			}
			HashMap<Long, Collection<ECRFFieldValueOutVO>> ecrfValueVOMap;
			if (valueVOMap.containsKey(listEntry.getId())) {
				ecrfValueVOMap = valueVOMap.get(listEntry.getId());
			} else {
				ecrfValueVOMap = new HashMap<Long, Collection<ECRFFieldValueOutVO>>();
				valueVOMap.put(listEntry.getId(), ecrfValueVOMap);
			}
			// Collection<ECRFFieldValueOutVO> ecrfValues = getEcrfFieldValues(ecrf, listEntryVO, false, false, false, null).getPageValues();
			ecrfValueVOMap.put(ecrf.getId(), ecrfValues);
			HashMap<Long, HashMap<Long, Collection>> ecrfLogVOMap;
			if (logVOMap.containsKey(listEntry.getId())) {
				ecrfLogVOMap = logVOMap.get(listEntry.getId());
			} else {
				ecrfLogVOMap = new HashMap<Long, HashMap<Long, Collection>>();
				logVOMap.put(listEntry.getId(), ecrfLogVOMap);
			}
			Iterator<ECRFFieldValueOutVO> it = ecrfValues.iterator();
			while (it.hasNext()) {
				ECRFFieldValueOutVO value = it.next();
				HashMap<Long, Collection> fieldLogVOMap;
				if (ecrfLogVOMap.containsKey(value.getEcrfField().getId())) {
					fieldLogVOMap = ecrfLogVOMap.get(value.getEcrfField().getId());
				} else {
					fieldLogVOMap = new HashMap<Long, Collection>();
					ecrfLogVOMap.put(value.getEcrfField().getId(), fieldLogVOMap);
				}
				fieldLogVOMap.put(value.getIndex(), getIndexFieldLog(listEntry.getId(), value.getEcrfField().getId(), value.getIndex(), blank, auditTrail, true, false, queues));
				// ArrayList indexFieldLog = new ArrayList();
				// Collection log;
				// if (!blank && auditTrail) { // x) {
				// log = ecrfFieldValueDao.findByListEntryEcrfFieldIndex(listEntry.getId(), value.getEcrfField().getId(), value.getIndex(), true, true, null);
				// Iterator logIt = log.iterator();
				// if (logIt.hasNext()) {
				// logIt.next();
				// } // skip most actual element
				// while (logIt.hasNext()) {
				// indexFieldLog.add(ecrfFieldValueDao.toECRFFieldValueOutVO((ECRFFieldValue) logIt.next()));
				// }
				// // indexFieldLog.addAll(log);
				// }
				// if (!blank && queues != null) {
				// for (int i = 0; i < queues.length; i++) {
				// log = ecrfFieldStatusEntryDao.findByListEntryEcrfFieldIndex(queues[i], listEntry.getId(), value.getEcrfField().getId(), value.getIndex(), false, false,
				// null);
				// ecrfFieldStatusEntryDao.toECRFFieldStatusEntryOutVOCollection(log);
				// indexFieldLog.addAll(log);
				// }
				// }
				// Collections.sort(indexFieldLog, ecrfFieldValueStatusEntryOutVOComparator);
				// fieldLogVOMap.put(value.getIndex(), indexFieldLog);
			}
			HashMap<Long, ECRFStatusEntryVO> ecrfStatusEntryVOMap;
			if (statusEntryVOMap.containsKey(listEntry.getId())) {
				ecrfStatusEntryVOMap = statusEntryVOMap.get(listEntry.getId());
			} else {
				ecrfStatusEntryVOMap = new HashMap<Long, ECRFStatusEntryVO>();
				statusEntryVOMap.put(listEntry.getId(), ecrfStatusEntryVOMap);
			}
			ECRFStatusEntry statusEntry = ecrfStatusEntryDao.findByEcrfListEntry(ecrf.getId(), listEntry.getId());
			if (statusEntry != null) {
				ecrfStatusEntryVOMap.put(ecrf.getId(), ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry));
				Signature signature = signatureDao.findRecentSignature(SignatureModule.ECRF_SIGNATURE, statusEntry.getId());
				if (!blank && signature != null) {
					signatureVOMap.put(statusEntry.getId(), getVerifiedEcrfSignatureVO(signature)); // result.setDescription(EntitySignature.getDescription(result));
				} else {
					signatureVOMap.put(statusEntry.getId(), null);
				}
			} else {
				ecrfStatusEntryVOMap.put(ecrf.getId(), null);
			}
			// statusEntryVOMap.put(listEntry.getId(), ecrfStatusEntryVOMap);
			if (ecrfIds.add(ecrf.getId())) {
				Iterator<ECRFFieldValueOutVO> ecrfValuesIt = ecrfValues.iterator();
				while (ecrfValuesIt.hasNext()) {
					InputFieldOutVO field = ecrfValuesIt.next().getEcrfField().getField();
					if (InputFieldType.SKETCH.equals(field.getFieldType().getType()) && !imageVOMap.containsKey(field.getId())) {
						// if (field.getHasImage() ) {
						imageVOMap.put(field.getId(), inputFieldDao.toInputFieldImageVO(inputFieldDao.load(field.getId())));
						// }
					}
				}
			}
			// } else {
			// int x = 5;
			// x = x + 1;
		}
	}

	private void populateProbandListEntryTagsPDFVOMaps(
			ProbandListEntry listEntry,
			ProbandListEntryOutVO listEntryVO,
			Collection<ProbandListEntryTagValueOutVO> probandListEntryTagValues,
			ArrayList<ProbandListEntryOutVO> listEntryVOs,
			HashMap<Long, Collection<ProbandListEntryTagValueOutVO>> valueVOMap,
			HashMap<Long, InputFieldImageVO> imageVOMap
			) throws Exception {
		if (probandListEntryTagValues.size() > 0) {
			InputFieldDao inputFieldDao = this.getInputFieldDao();
			listEntryVOs.add(listEntryVO);
			if (!valueVOMap.containsKey(listEntry.getId())) {
				valueVOMap.put(listEntry.getId(), probandListEntryTagValues);
				Iterator<ProbandListEntryTagValueOutVO> probandListEntryTagValuesIt = probandListEntryTagValues.iterator();
				while (probandListEntryTagValuesIt.hasNext()) {
					InputFieldOutVO field = probandListEntryTagValuesIt.next().getTag().getField();
					if (InputFieldType.SKETCH.equals(field.getFieldType().getType()) && !imageVOMap.containsKey(field.getId())) {
						// if (field.getHasImage() ) {
						imageVOMap.put(field.getId(), inputFieldDao.toInputFieldImageVO(inputFieldDao.load(field.getId())));
						// }
					}
				}
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
		// ServiceUtil.logSystemMessage(trial, result.getTrial(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_DELETED, result, null, journalEntryDao);
		ServiceUtil.logSystemMessage(trial, result.getProband(), now, user, SystemMessageCodes.PROBAND_LIST_ENTRY_DELETED, result, null, journalEntryDao);
		return result;
	}

	private Object[] setEcrfFieldValues(Set<ECRFFieldValueInVO> ecrfFieldValuesIn, boolean loadPageResult) throws Exception {
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
		ArrayList<ECRFFieldValueOutVO> ecrfFieldValues = null;
		ArrayList<ECRFFieldValueJsonVO> jsEcrfFieldValues = null;
		if (ecrfFieldValuesIn != null && ecrfFieldValuesIn.size() > 0) {
			ECRFFieldValueInVO ecrfFieldValueIn;
			ECRFField ecrfField = null;
			if (!loadPageResult) {
				ecrfFieldValues = new ArrayList<ECRFFieldValueOutVO>(ecrfFieldValuesIn.size());
			}
			// jsEcrfFieldValues = new ArrayList<ECRFFieldValueJsonVO>(ecrfFieldValuesIn.size());
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
				if (listEntry == null) {
					listEntry = CheckIDUtil.checkProbandListEntryId(ecrfFieldValueIn.getListEntryId(), this.getProbandListEntryDao(), LockMode.PESSIMISTIC_WRITE);
					if (!ecrf.getTrial().equals(listEntry.getTrial())) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_FOR_WRONG_TRIAL);
					}
					ServiceUtil.checkProbandLocked(listEntry.getProband());
					statusEntry = this.getECRFStatusEntryDao().findByEcrfListEntry(ecrf.getId(), listEntry.getId());
					if (statusEntry == null) {
						ECRFStatusType statusType = this.getECRFStatusTypeDao().findInitialStates().iterator().next();
						this.getECRFDao().lock(ecrf, LockMode.PESSIMISTIC_WRITE);
						// ecrf = ServiceUtil.checkEcrfId(ecrf.getId(), this.getECRFDao(), LockMode.PESSIMISTIC_WRITE); // lock order, field updates
						Object[] resultItems = addEcrfStatusEntry(ecrf, listEntry, statusType, null, now, user);
						statusEntry = (ECRFStatusEntry) resultItems[0];
						statusEntryVO = (ECRFStatusEntryVO) resultItems[1];
					} else {
						statusEntryVO = this.getECRFStatusEntryDao().toECRFStatusEntryVO(statusEntry);
					}
					listEntryVO = statusEntryVO.getListEntry();
					// if (statusEntryVO.getStatus().getValueLockdown()) {
					// // ECRFFieldStatusEntry lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(null, listEntry.getId(), ecrfField.getId(), index);
					// // if (lastStatus == null) {
					// throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_LOCKED_FOR_ECRF_STATUS,
					// statusEntryVO.getStatus().getName());
					// // } else {
					// // ECRFFieldStatusQueue[] queues = ECRFFieldStatusQueue.values();
					// // for (int i = 0; i < queues.length; i++) {
					// // lastStatus = this.getECRFFieldStatusEntryDao().findLastStatus(queues[i], listEntry.getId(), ecrfField.getId(), index);
					// // if (lastStatus != null && lastStatus.getStatus().isValueLockdown()) {
					// // throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_LOCKED_FOR_ECRF_FIELD_STATUS,
					// // L10nUtil.getEcrfFieldStatusTypeName(Locales.USER, lastStatus.getStatus().getNameL10nKey()));
					// // }
					// // }
					// // }
					// }
					if (listEntryVO.getLastStatus() != null && !listEntryVO.getLastStatus().getStatus().getEcrfValueInputEnabled()) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUE_INPUT_DISABLED_FOR_PROBAND_LIST_STATUS,
								listEntryVO.getLastStatus().getStatus().getName());
					}
				} else if (!listEntry.getId().equals(ecrfFieldValueIn.getListEntryId())) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.ECRF_FIELD_VALUES_FOR_DIFFERENT_PROBAND_LIST_ENTRIES);
				}

				try {
					addUpdateEcrfFieldValue(ecrfFieldValueIn, statusEntry, listEntryVO, ecrfField, now, user, ServiceUtil.LOG_ECRF_FIELD_VALUE_TRIAL,
							ServiceUtil.LOG_ECRF_FIELD_VALUE_PROBAND, sectionIndexMap, ecrfFieldValues, jsEcrfFieldValues);
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
			// HashMap<String, Long> maxSeriesIndexMap = null;
			// HashMap<String, Set<ECRFField>> seriesEcrfFieldMap = null;
			// HashMap<String, Set<ECRFField>> seriesEcrfFieldJsMap = null;
			// if (addSeries) {
			// maxSeriesIndexMap = new HashMap<String, Long>();
			// seriesEcrfFieldMap = new HashMap<String, Set<ECRFField>>();
			// seriesEcrfFieldJsMap = new HashMap<String, Set<ECRFField>>();
			// ServiceUtil.initSeriesEcrfFieldMaps(
			// this.getECRFFieldDao().findByTrialEcrfSeriesJs(null, ecrf.getId(), true, true, null, null),
			// listEntry.getId(),
			// ecrf.getId(),
			// maxSeriesIndexMap,
			// seriesEcrfFieldMap,
			// seriesEcrfFieldJsMap,
			// this.getECRFFieldValueDao()
			// );
			// }
			// if (firstException == null) {
			Iterator<Entry<String, LinkedHashSet<Long>>> sectionIndexMapIt = sectionIndexMap.entrySet().iterator();
			while (sectionIndexMapIt.hasNext()) {
				Entry<String, LinkedHashSet<Long>> sectionIndexes = sectionIndexMapIt.next();
				String section = sectionIndexes.getKey();
				Iterator<Long> indexesIt = sectionIndexes.getValue().iterator();
				// boolean sectionAddSeries = false;
				// Long maxSeriesIndex = null;
				while (indexesIt.hasNext()) {
					Long index = indexesIt.next();
					// if (!sectionAddSeries
					// && maxSeriesIndexMap != null
					// && (maxSeriesIndex = maxSeriesIndexMap.get(section)) != null
					// && maxSeriesIndex.equals(index)) {
					// sectionAddSeries = true;
					// }
					Iterator<ECRFFieldValueInVO> missingSeriesInIt = ServiceUtil.createPresetEcrfFieldInValues(listEntry.getId(), ecrf.getId(), section, index,
							this.getECRFFieldDao(),
							this.getECRFFieldValueDao(), this.getInputFieldSelectionSetValueDao()).iterator();
					while (missingSeriesInIt.hasNext()) {
						ecrfFieldValueIn = missingSeriesInIt.next();
						if (!(errorMessagesMap.containsKey(ecrfFieldValueIn.getEcrfFieldId())
								&& errorMessagesMap.get(ecrfFieldValueIn.getEcrfFieldId()).containsKey(ecrfFieldValueIn.getIndex()))) {
							// if there were processing errors for a indexfield, there was field input given. we do not try to insert default values instead (which can cause
							// another error and replace the original exception).
							ecrfField = CheckIDUtil.checkEcrfFieldId(ecrfFieldValueIn.getEcrfFieldId(), this.getECRFFieldDao());
							try {
								addUpdateEcrfFieldValue(ecrfFieldValueIn, statusEntry, listEntryVO, ecrfField, now, user, ServiceUtil.LOG_ECRF_FIELD_VALUE_TRIAL,
										ServiceUtil.LOG_ECRF_FIELD_VALUE_PROBAND, null, ecrfFieldValues, jsEcrfFieldValues);
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
				// if (sectionAddSeries) {
				// Set<ECRFField> seriesEcrfFields = seriesEcrfFieldMap.get(section);
				// if (seriesEcrfFields != null && seriesEcrfFields.size() > 0) {
				// ECRFFieldValueOutVO ecrfFieldValueVO = null;
				// Iterator<ECRFField> seriesEcrfFieldsIt = seriesEcrfFields.iterator();
				// while (seriesEcrfFieldsIt.hasNext()) {
				// ECRFField seriesEcrfField = seriesEcrfFieldsIt.next();
				// ecrfFieldValueVO = ServiceUtil
				// .createPresetEcrfFieldOutValue(this.getECRFFieldDao().toECRFFieldOutVO(seriesEcrfField), listEntryVO, maxSeriesIndex + 1l);
				// ecrfFieldValues.add(ecrfFieldValueVO);
				// }
				// }
				// seriesEcrfFields = seriesEcrfFieldJsMap.get(section);
				// if (seriesEcrfFields != null && seriesEcrfFields.size() > 0) {
				// ECRFFieldValueJsonVO ecrfFieldValueVO = null;
				// Iterator<ECRFField> seriesEcrfFieldsIt = seriesEcrfFields.iterator();
				// while (seriesEcrfFieldsIt.hasNext()) {
				// ECRFField seriesEcrfField = seriesEcrfFieldsIt.next();
				// ecrfFieldValueVO = ServiceUtil.createPresetEcrfFieldJsonValue(seriesEcrfField, maxSeriesIndex + 1l, this.getInputFieldSelectionSetValueDao());
				// jsEcrfFieldValues.add(ecrfFieldValueVO);
				// }
				// }
				// }
				// }
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
		return new Object[] { result, ecrf, listEntryVO };
	}

	private ECRFStatusEntryVO updateEcrfStatusEntry(ECRFStatusEntry originalStatusEntry, ECRF ecrf, ProbandListEntry listEntry, ECRFStatusType statusType, Long version,
			Long probandListStatusTypeId,Timestamp now, User user) throws Exception {
		ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
		ECRFStatusEntryVO result;
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		checkUpdateEcrfStatusEntry(originalStatusEntry, statusType, version, user);
		ECRFStatusType originalEcrfStatusType = originalStatusEntry.getStatus();
		ECRFStatusEntryVO original = ecrfStatusEntryDao.toECRFStatusEntryVO(originalStatusEntry);
		this.getECRFStatusTypeDao().evict(originalEcrfStatusType);
		ecrfStatusEntryDao.evict(originalStatusEntry);
		ECRFStatusEntry statusEntry = ecrfStatusEntryDao.load(originalStatusEntry.getId());
		statusEntry.setStatus(statusType);
		CoreUtil.modifyVersion(statusEntry, version.longValue(), now, user);
		ecrfStatusEntryDao.update(statusEntry);
		execEcrfStatusActions(originalEcrfStatusType, statusEntry, probandListStatusTypeId, now, user);
		result = ecrfStatusEntryDao.toECRFStatusEntryVO(statusEntry);
		ServiceUtil.logSystemMessage(listEntry.getTrial(), result.getListEntry().getProband(), now, user, SystemMessageCodes.ECRF_STATUS_ENTRY_UPDATED, result, original,
				journalEntryDao);
		ServiceUtil.logSystemMessage(listEntry.getProband(), result.getListEntry().getTrial(), now, user, SystemMessageCodes.ECRF_STATUS_ENTRY_UPDATED, result, original,
				journalEntryDao);
		return result;
	}

}