// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::shared::SearchService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.shared;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TimeZone;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.compare.VOPositionComparator;
import org.phoenixctms.ctsms.domain.AddressTypeDao;
import org.phoenixctms.ctsms.domain.ContactDetailTypeDao;
import org.phoenixctms.ctsms.domain.Course;
import org.phoenixctms.ctsms.domain.CourseDao;
import org.phoenixctms.ctsms.domain.Criteria;
import org.phoenixctms.ctsms.domain.CriteriaDao;
import org.phoenixctms.ctsms.domain.Criterion;
import org.phoenixctms.ctsms.domain.CriterionDao;
import org.phoenixctms.ctsms.domain.CriterionProperty;
import org.phoenixctms.ctsms.domain.CriterionPropertyDao;
import org.phoenixctms.ctsms.domain.CriterionRestriction;
import org.phoenixctms.ctsms.domain.CriterionRestrictionDao;
import org.phoenixctms.ctsms.domain.CriterionTie;
import org.phoenixctms.ctsms.domain.CriterionTieDao;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InquiryValue;
import org.phoenixctms.ctsms.domain.InquiryValueDao;
import org.phoenixctms.ctsms.domain.Inventory;
import org.phoenixctms.ctsms.domain.InventoryDao;
import org.phoenixctms.ctsms.domain.InventoryTagDao;
import org.phoenixctms.ctsms.domain.InventoryTagValue;
import org.phoenixctms.ctsms.domain.InventoryTagValueDao;
import org.phoenixctms.ctsms.domain.Job;
import org.phoenixctms.ctsms.domain.JobDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.MassMailDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandAddress;
import org.phoenixctms.ctsms.domain.ProbandAddressDao;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValue;
import org.phoenixctms.ctsms.domain.ProbandContactDetailValueDao;
import org.phoenixctms.ctsms.domain.ProbandDao;
import org.phoenixctms.ctsms.domain.ProbandTagDao;
import org.phoenixctms.ctsms.domain.ProbandTagValue;
import org.phoenixctms.ctsms.domain.ProbandTagValueDao;
import org.phoenixctms.ctsms.domain.Staff;
import org.phoenixctms.ctsms.domain.StaffDao;
import org.phoenixctms.ctsms.domain.StaffTagDao;
import org.phoenixctms.ctsms.domain.StaffTagValue;
import org.phoenixctms.ctsms.domain.StaffTagValueDao;
import org.phoenixctms.ctsms.domain.TeamMember;
import org.phoenixctms.ctsms.domain.TeamMemberDao;
import org.phoenixctms.ctsms.domain.TeamMemberRoleDao;
import org.phoenixctms.ctsms.domain.TimelineEvent;
import org.phoenixctms.ctsms.domain.TimelineEventDao;
import org.phoenixctms.ctsms.domain.TimelineEventTypeDao;
import org.phoenixctms.ctsms.domain.TrialDao;
import org.phoenixctms.ctsms.domain.TrialTagDao;
import org.phoenixctms.ctsms.domain.TrialTagValue;
import org.phoenixctms.ctsms.domain.TrialTagValueDao;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.domain.UserDao;
import org.phoenixctms.ctsms.enumeration.DBModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.excel.ExcelCellFormat;
import org.phoenixctms.ctsms.excel.ExcelExporter;
import org.phoenixctms.ctsms.excel.ExcelUtil;
import org.phoenixctms.ctsms.excel.ExcelWriterFactory;
import org.phoenixctms.ctsms.excel.SearchResultExcelDefaultSettings;
import org.phoenixctms.ctsms.excel.SearchResultExcelSettingCodes;
import org.phoenixctms.ctsms.excel.SearchResultExcelWriter;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.pdf.CVPDFDefaultSettings;
import org.phoenixctms.ctsms.pdf.CVPDFPainter;
import org.phoenixctms.ctsms.pdf.CVPDFSettingCodes;
import org.phoenixctms.ctsms.pdf.CourseParticipantListPDFDefaultSettings;
import org.phoenixctms.ctsms.pdf.CourseParticipantListPDFPainter;
import org.phoenixctms.ctsms.pdf.CourseParticipantListPDFSettingCodes;
import org.phoenixctms.ctsms.pdf.PDFImprinter;
import org.phoenixctms.ctsms.pdf.ProbandLetterPDFDefaultSettings;
import org.phoenixctms.ctsms.pdf.ProbandLetterPDFPainter;
import org.phoenixctms.ctsms.pdf.ProbandLetterPDFSettingCodes;
import org.phoenixctms.ctsms.pdf.TrainingRecordPDFDefaultSettings;
import org.phoenixctms.ctsms.pdf.TrainingRecordPDFPainter;
import org.phoenixctms.ctsms.pdf.TrainingRecordPDFSettingCodes;
import org.phoenixctms.ctsms.query.QueryUtil;
import org.phoenixctms.ctsms.query.parser.CriterionIntermediateSetParser;
import org.phoenixctms.ctsms.query.parser.CriterionSyntaxParser;
import org.phoenixctms.ctsms.query.parser.SyntaxException;
import org.phoenixctms.ctsms.query.parser.SyntaxException.SyntaxErrors;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.OmittedFields;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.AddressTypeVO;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ContactDetailTypeVO;
import org.phoenixctms.ctsms.vo.CourseOutVO;
import org.phoenixctms.ctsms.vo.CourseParticipantListPDFVO;
import org.phoenixctms.ctsms.vo.CriteriaInVO;
import org.phoenixctms.ctsms.vo.CriteriaInstantVO;
import org.phoenixctms.ctsms.vo.CriteriaOutVO;
import org.phoenixctms.ctsms.vo.CriterionInVO;
import org.phoenixctms.ctsms.vo.CriterionInstantVO;
import org.phoenixctms.ctsms.vo.CriterionOutVO;
import org.phoenixctms.ctsms.vo.CriterionPropertyVO;
import org.phoenixctms.ctsms.vo.CvPDFVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldTypeVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.IntermediateSetDetailVO;
import org.phoenixctms.ctsms.vo.IntermediateSetSummaryVO;
import org.phoenixctms.ctsms.vo.InventoryOutVO;
import org.phoenixctms.ctsms.vo.InventoryTagVO;
import org.phoenixctms.ctsms.vo.InventoryTagValueOutVO;
import org.phoenixctms.ctsms.vo.MassMailOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandLetterPDFVO;
import org.phoenixctms.ctsms.vo.ProbandOutVO;
import org.phoenixctms.ctsms.vo.ProbandTagVO;
import org.phoenixctms.ctsms.vo.ProbandTagValueOutVO;
import org.phoenixctms.ctsms.vo.SearchResultExcelVO;
import org.phoenixctms.ctsms.vo.StaffOutVO;
import org.phoenixctms.ctsms.vo.StaffTagVO;
import org.phoenixctms.ctsms.vo.StaffTagValueOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberOutVO;
import org.phoenixctms.ctsms.vo.TeamMemberRoleVO;
import org.phoenixctms.ctsms.vo.TimelineEventOutVO;
import org.phoenixctms.ctsms.vo.TimelineEventTypeVO;
import org.phoenixctms.ctsms.vo.TrainingRecordPDFVO;
import org.phoenixctms.ctsms.vo.TrialOutVO;
import org.phoenixctms.ctsms.vo.TrialTagVO;
import org.phoenixctms.ctsms.vo.TrialTagValueOutVO;
import org.phoenixctms.ctsms.vo.UserOutVO;

/**
 * @see org.phoenixctms.ctsms.service.shared.SearchService
 */
public class SearchServiceImpl
		extends SearchServiceBase {

	private static CriterionProperty checkCriterionPropertyId(Long propertyId, CriterionInstantVO criterion, CriterionPropertyDao criterionPropertyDao, boolean logError)
			throws ServiceException {
		CriterionProperty property = criterionPropertyDao.load(propertyId);
		if (property == null) {
			throw initServiceExceptionWithPosition(ServiceExceptionCodes.INVALID_CRITERION_PROPERTY_ID, logError, criterion,
					propertyId == null ? null : propertyId.toString());
		}
		return property;
	}

	private static CriterionRestriction checkCriterionRestrictionId(Long restrictionId, CriterionInstantVO criterion, CriterionRestrictionDao criterionRestrictionDao,
			boolean logError) throws ServiceException {
		CriterionRestriction restriction = criterionRestrictionDao.load(restrictionId);
		if (restriction == null) {
			throw initServiceExceptionWithPosition(ServiceExceptionCodes.INVALID_CRITERION_RESTRICTION_ID, logError, criterion, restrictionId == null ? null
					: restrictionId.toString());
		}
		return restriction;
	}

	private static CriterionTie checkCriterionTieId(Long tieId, CriterionInstantVO criterion, CriterionTieDao criterionTieDao, boolean logError) throws ServiceException {
		CriterionTie tie = criterionTieDao.load(tieId);
		if (tie == null) {
			throw initServiceExceptionWithPosition(ServiceExceptionCodes.INVALID_CRITERION_TIE_ID, logError, criterion, tieId == null ? null : tieId.toString());
		}
		return tie;
	}

	private static void checkValues(CriterionInstantVO criterion, CriterionProperty property, CriterionRestriction restriction, boolean logError, boolean checkValues)
			throws ServiceException {
		if (property != null && property.getPicker() == null) {
			boolean isUnaryRestriction = CommonUtil.isUnaryCriterionRestriction(restriction.getRestriction());
			switch (property.getValueType()) {
				case BOOLEAN:
				case BOOLEAN_HASH:
					// andromda problem...
					if (numValuesSet(criterion, false) > 0) {
						// only boolean value must be set
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_BOOLEAN_VALUE_ALLOWED_ONLY, logError, criterion);
					}
					break;
				case FLOAT:
				case FLOAT_HASH:
					if (checkValues && !isUnaryRestriction && criterion.getFloatValue() == null) {
						// float value must be set
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_FLOAT_VALUE_IS_NULL, logError, criterion);
					}
					if (criterion.getDateValue() != null ||
							criterion.getTimeValue() != null ||
							criterion.getLongValue() != null ||
							criterion.getStringValue() != null ||
							criterion.getTimestampValue() != null ||
							criterion.getBooleanValue()) {
						// only float value must be set
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_FLOAT_VALUE_ALLOWED_ONLY, logError, criterion);
					}
					break;
				case DATE:
				case DATE_HASH:
					if (checkValues && !isUnaryRestriction && criterion.getDateValue() == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_DATE_VALUE_IS_NULL, logError, criterion);
					}
					if (criterion.getTimeValue() != null ||
							criterion.getFloatValue() != null ||
							criterion.getLongValue() != null ||
							criterion.getStringValue() != null ||
							criterion.getTimestampValue() != null ||
							criterion.getBooleanValue()) {
						// only date value must be set
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_DATE_VALUE_ALLOWED_ONLY, logError, criterion);
					}
					break;
				case TIME:
				case TIME_HASH:
					if (checkValues && !isUnaryRestriction && criterion.getTimeValue() == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_TIME_VALUE_IS_NULL, logError, criterion);
					}
					if (criterion.getDateValue() != null ||
							criterion.getFloatValue() != null ||
							criterion.getLongValue() != null ||
							criterion.getStringValue() != null ||
							criterion.getTimestampValue() != null ||
							criterion.getBooleanValue()) {
						// only time value must be set
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_TIME_VALUE_ALLOWED_ONLY, logError, criterion);
					}
					break;
				case LONG:
				case LONG_HASH:
					if (checkValues && !isUnaryRestriction && criterion.getLongValue() == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_LONG_VALUE_IS_NULL, logError, criterion);
					}
					if (criterion.getDateValue() != null ||
							criterion.getTimeValue() != null ||
							criterion.getFloatValue() != null ||
							criterion.getStringValue() != null ||
							criterion.getTimestampValue() != null ||
							criterion.getBooleanValue()) {
						// only long value must be set
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_LONG_VALUE_ALLOWED_ONLY, logError, criterion);
					}
					break;
				case NONE:
					if (numValuesSet(criterion, true) > 0) {
						// no value must be set
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_VALUE_ALLOWED, logError, criterion);
					}
					break;
				case STRING:
				case STRING_HASH:
					if (checkValues && !isUnaryRestriction && criterion.getStringValue() == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_STRING_VALUE_IS_NULL, logError, criterion);
					}
					if (criterion.getDateValue() != null ||
							criterion.getTimeValue() != null ||
							criterion.getFloatValue() != null ||
							criterion.getLongValue() != null ||
							criterion.getTimestampValue() != null ||
							criterion.getBooleanValue()) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_STRING_VALUE_ALLOWED_ONLY, logError, criterion);
					}
					break;
				case TIMESTAMP:
				case TIMESTAMP_HASH:
					if (checkValues && !isUnaryRestriction && criterion.getTimestampValue() == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_TIMESTAMP_VALUE_IS_NULL, logError, criterion);
					}
					if (criterion.getDateValue() != null ||
							criterion.getTimeValue() != null ||
							criterion.getFloatValue() != null ||
							criterion.getLongValue() != null ||
							criterion.getStringValue() != null ||
							criterion.getBooleanValue()) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_TIMESTAMP_VALUE_ALLOWED_ONLY, logError, criterion);
					}
					break;
				default:
					// datatype not implemented...
					throw new IllegalArgumentException(MessageFormat.format(CommonUtil.UNSUPPORTED_CRITERION_VALUE_TYPE, property.getValueType().toString()));
			}
		}
	}

	private static String getServiceExcpetionCodeFromSyntaxError(SyntaxErrors error) throws IllegalArgumentException {
		switch (error) {
			case MISSING_LEFT_PARENTHESIS:
				return ServiceExceptionCodes.CRITERION_MISSING_LEFT_PARENTHESIS;
			case MISSING_OPERAND:
				return ServiceExceptionCodes.CRITERION_MISSING_OPERAND;
			case MISSING_OPERATOR:
				return ServiceExceptionCodes.CRITERION_MISSING_OPERATOR;
			case INCOMPATIBLE_FIRST_OPERAND_TYPE:
				return ServiceExceptionCodes.CRITERION_INCOMPATIBLE_FIRST_OPERAND_TYPE;
			case INCOMPATIBLE_SECOND_OPERAND_TYPE:
				return ServiceExceptionCodes.CRITERION_INCOMPATIBLE_SECOND_OPERAND_TYPE;
			case NO_OPERATOR:
				return ServiceExceptionCodes.CRITERION_NO_OPERATOR;
			case LEFT_PARENTHESIS_AFTER_RIGHT_PARETHESIS:
				return ServiceExceptionCodes.CRITERION_LEFT_PARENTHESIS_AFTER_RIGHT_PARETHESIS;
			case LEFT_PARENTHESIS_AFTER_VALUE:
				return ServiceExceptionCodes.CRITERION_LEFT_PARENTHESIS_AFTER_VALUE;
			case LEFT_PARENTHESIS_AT_END:
				return ServiceExceptionCodes.CRITERION_LEFT_PARENTHESIS_AT_END;
			case RIGHT_PARENTHESIS_AT_BEGIN:
				return ServiceExceptionCodes.CRITERION_RIGHT_PARENTHESIS_AT_BEGIN;
			case RIGHT_PARENTHESIS_AFTER_LEFT_PARENTHESIS:
				return ServiceExceptionCodes.CRITERION_RIGHT_PARENTHESIS_AFTER_LEFT_PARENTHESIS;
			case RIGHT_PARENTHESIS_AFTER_OPERATOR:
				return ServiceExceptionCodes.CRITERION_RIGHT_PARENTHESIS_AFTER_OPERATOR;
			case OPERATOR_AT_BEGIN:
				return ServiceExceptionCodes.CRITERION_OPERATOR_AT_BEGIN;
			case OPERATOR_AFTER_LEFT_PARENTHESIS:
				return ServiceExceptionCodes.CRITERION_OPERATOR_AFTER_LEFT_PARENTHESIS;
			case OPERATOR_AFTER_OPERATOR:
				return ServiceExceptionCodes.CRITERION_OPERATOR_AFTER_OPERATOR;
			case OPERATOR_AT_END:
				return ServiceExceptionCodes.CRITERION_OPERATOR_AT_END;
			case VALUE_AFTER_RIGHT_PARENTHESIS:
				return ServiceExceptionCodes.CRITERION_VALUE_AFTER_RIGHT_PARENTHESIS;
			case VALUE_AFTER_VALUE:
				return ServiceExceptionCodes.CRITERION_VALUE_AFTER_VALUE;
			case PARENTHESIS_MISSING:
				return ServiceExceptionCodes.CRITERION_PARENTHESIS_MISSING;
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.CRITERION_NOT_IMPLEMENTED_SYNTAX_ERROR,
						DefaultMessages.CRITERION_NOT_IMPLEMENTED_SYNTAX_ERROR, new Object[] { error.toString() }));
		}
	}

	private static ServiceException initServiceExceptionWithPosition(String errorCode, boolean logError, CriterionInstantVO criterion, Object... args) throws ServiceException {
		ServiceException exception = L10nUtil.initServiceException(errorCode, args);
		if (criterion != null) {
			exception.setData(criterion.getPosition());
		}
		exception.setLogError(logError);
		return exception;
	}

	private static JournalEntry logSystemMessage(Criteria criteria, CriteriaOutVO criteriaVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(criteria, now, modified, systemMessageCode, new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(User user, CriteriaOutVO criteriaVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null)) });
	}

	private static int numValuesSet(CriterionInstantVO criterion, boolean includeBooleanValue) {
		int result = 0;
		result += (criterion.getDateValue() != null) ? 1 : 0;
		result += (criterion.getTimeValue() != null) ? 1 : 0;
		result += (criterion.getFloatValue() != null) ? 1 : 0;
		result += (criterion.getLongValue() != null) ? 1 : 0;
		result += (criterion.getStringValue() != null) ? 1 : 0;
		result += (criterion.getTimestampValue() != null) ? 1 : 0;
		if (includeBooleanValue) {
			result += criterion.getBooleanValue() ? 1 : 0;
		}
		return result;
	}

	private static CriteriaOutVO obfuscateCriterions(CriteriaOutVO criteriaVO) {
		if (criteriaVO != null) {
			CriteriaOutVO result = new CriteriaOutVO(criteriaVO);
			ArrayList<CriterionOutVO> obfuscatedCriterions = new ArrayList<CriterionOutVO>();
			Iterator<CriterionOutVO> criterionsIt = criteriaVO.getCriterions().iterator();
			while (criterionsIt.hasNext()) {
				CriterionOutVO obfuscatedCriterion = new CriterionOutVO(criterionsIt.next());
				obfuscatedCriterion.setCriteria(result);
				CriterionPropertyVO property = obfuscatedCriterion.getProperty();
				if (property != null && OmittedFields.isOmitted(property.getProperty())) {
					obfuscatedCriterion.setStringValue(CoreUtil.OBFUSCATED_STRING);
				}
				obfuscatedCriterions.add(obfuscatedCriterion);
			}
			result.setCriterions(obfuscatedCriterions);
			return result;
		}
		return null;
	}

	private CriterionIntermediateSetParser criterionIntermediateSetParser;
	private CriterionSyntaxParser criterionSyntaxParser;

	private void checkCriteriaInput(DBModule module, CriteriaInstantVO criteria, boolean logError, boolean checkValues) throws ServiceException {
		CriterionPropertyDao criterionPropertyDao = this.getCriterionPropertyDao();
		CriterionRestrictionDao criterionRestrictionDao = this.getCriterionRestrictionDao();
		CriterionTieDao criterionTieDao = this.getCriterionTieDao();
		HashSet<Long> dupeCheck = new HashSet<Long>();
		Iterator<CriterionInstantVO> it = criteria.getCriterions().iterator();
		while (it.hasNext()) {
			CriterionInstantVO criterion = it.next();
			if (criterion == null) {
				throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_IS_NULL, logError, null);
			}
			if (criterion.getPosition() == null) {
				throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_POSITION_IS_NULL, logError, criterion);
			}
			if (!dupeCheck.add(criterion.getPosition())) {
				throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_POSITION_NOT_UNIQUE, logError, criterion, criterion.getPosition().toString());
			}
			CriterionProperty property = null;
			if (criterion.getPropertyId() != null) {
				property = checkCriterionPropertyId(criterion.getPropertyId(), criterion, criterionPropertyDao, logError);
			}
			CriterionRestriction restriction = null;
			if (criterion.getRestrictionId() != null) {
				restriction = checkCriterionRestrictionId(criterion.getRestrictionId(), criterion, criterionRestrictionDao, logError);
			}
			CriterionTie tie = null;
			if (criterion.getTieId() != null) {
				tie = checkCriterionTieId(criterion.getTieId(), criterion, criterionTieDao, logError);
			}
			if (tie != null) {
				if (CommonUtil.isBlankCriterionTie(tie.getTie()) && (numValuesSet(criterion, true) > 0 || property != null || restriction != null)) {
					// for parenthesis and set operation ties, everything else must be null...
					throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_BLANK_CRITERION_TIE, logError, criterion);
				} else {
					// AND, OR, ...
					if (property == null && restriction != null || property != null && restriction == null) {
						// both property and restriction must be null or not null
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_PROPERTY_AND_RESTRICTION, logError, criterion);
					}
				}
			} else {
				if (property == null || restriction == null) {
					// if tie is null, property must be defined
					throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_TIE_IS_NULL, logError, criterion);
				}
			}
			if (property != null && !property.getModule().equals(module)) {
				// selected property belongs to wrong db module...
				throw initServiceExceptionWithPosition(ServiceExceptionCodes.PROPERTY_NOT_SUPPORTED_FOR_DB_MODULE, logError, criterion,
						L10nUtil.getCriterionPropertyName(Locales.USER, property.getNameL10nKey()),
						property.getModule().toString());
			}
			if (property != null) {
				Iterator<CriterionRestriction> validRestrictionsIt = property.getValidRestrictions().iterator();
				boolean restrictionValid = false;
				while (validRestrictionsIt.hasNext()) {
					if (restriction.equals(validRestrictionsIt.next())) {
						restrictionValid = true;
						break;
					}
				}
				if (!restrictionValid) {
					throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_RESTRICTION_INVALID, logError, criterion,
							L10nUtil.getCriterionPropertyName(Locales.USER, property.getNameL10nKey()));
				}
			}
			checkValues(criterion, property, restriction, logError, checkValues);
			checkPickedId(criterion, property, restriction, logError, checkValues);
		}
		criteria.setCriterionExpression(parseCriterions(criteria, logError, true, true));
	}

	private void checkPickedId(CriterionInstantVO criterion, CriterionProperty property, CriterionRestriction restriction, boolean logError, boolean checkValues)
			throws ServiceException {
		if (property != null && property.getPicker() != null) {
			boolean isUnaryRestriction = CommonUtil.isUnaryCriterionRestriction(restriction.getRestriction());
			Long id = criterion.getLongValue();
			switch (property.getPicker()) {
				case INVENTORY_DB:
					if (checkValues && !isUnaryRestriction && id == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_INVENTORY_SELECTED, logError, criterion);
					}
					if (checkValues && id != null) {
						try {
							CheckIDUtil.checkInventoryId(id, this.getInventoryDao());
						} catch (ServiceException e) {
							throw initServiceExceptionWithPosition(e.getErrorCode(), logError, criterion, id.toString());
						}
					}
					break;
				case STAFF_DB:
					if (checkValues && !isUnaryRestriction && id == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_STAFF_SELECTED, logError, criterion);
					}
					if (checkValues && id != null) {
						try {
							CheckIDUtil.checkStaffId(id, this.getStaffDao());
						} catch (ServiceException e) {
							throw initServiceExceptionWithPosition(e.getErrorCode(), logError, criterion, id.toString());
						}
					}
					break;
				case COURSE_DB:
					if (checkValues && !isUnaryRestriction && id == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_COURSE_SELECTED, logError, criterion);
					}
					if (checkValues && id != null) {
						try {
							CheckIDUtil.checkCourseId(id, this.getCourseDao());
						} catch (ServiceException e) {
							throw initServiceExceptionWithPosition(e.getErrorCode(), logError, criterion, id.toString());
						}
					}
					break;
				case USER_DB:
					if (checkValues && !isUnaryRestriction && id == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_USER_SELECTED, logError, criterion);
					}
					if (checkValues && id != null) {
						try {
							CheckIDUtil.checkUserId(id, this.getUserDao());
						} catch (ServiceException e) {
							throw initServiceExceptionWithPosition(e.getErrorCode(), logError, criterion, id.toString());
						}
					}
					break;
				case TRIAL_DB:
					if (checkValues && !isUnaryRestriction && id == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_TRIAL_SELECTED, logError, criterion);
					}
					if (checkValues && id != null) {
						try {
							CheckIDUtil.checkTrialId(id, this.getTrialDao());
						} catch (ServiceException e) {
							throw initServiceExceptionWithPosition(e.getErrorCode(), logError, criterion, id.toString());
						}
					}
					break;
				case PROBAND_DB:
					if (checkValues && !isUnaryRestriction && id == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_PROBAND_SELECTED, logError, criterion);
					}
					if (checkValues && id != null) {
						try {
							CheckIDUtil.checkProbandId(id, this.getProbandDao());
						} catch (ServiceException e) {
							throw initServiceExceptionWithPosition(e.getErrorCode(), logError, criterion, id.toString());
						}
					}
					break;
				case INPUT_FIELD_DB:
					if (checkValues && !isUnaryRestriction && id == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_INPUT_FIELD_SELECTED, logError, criterion);
					}
					if (checkValues && id != null) {
						try {
							CheckIDUtil.checkInputFieldId(id, this.getInputFieldDao());
						} catch (ServiceException e) {
							throw initServiceExceptionWithPosition(e.getErrorCode(), logError, criterion, id.toString());
						}
					}
					break;
				case MASS_MAIL_DB:
					if (checkValues && !isUnaryRestriction && id == null) {
						throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERION_NO_MASS_MAIL_SELECTED, logError, criterion);
					}
					if (checkValues && id != null) {
						try {
							CheckIDUtil.checkMassMailId(id, this.getMassMailDao());
						} catch (ServiceException e) {
							throw initServiceExceptionWithPosition(e.getErrorCode(), logError, criterion, id.toString());
						}
					}
					break;
				default:
					throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_PICKER_DB_MODULE, DefaultMessages.UNSUPPORTED_PICKER_DB_MODULE,
							new Object[] { property.getPicker().toString() }));
			}
		}
	}

	private void clearLoadByDefault(CriteriaInVO criteriaIn, Timestamp now, User modifiedUser) throws Exception {
		if (criteriaIn.getLoadByDefault()) {
			CriteriaDao criteriaDao = this.getCriteriaDao();
			JournalEntryDao journalEntryDao = this.getJournalEntryDao();
			Iterator<Criteria> it = criteriaDao.findByModule(criteriaIn.getModule(), true, null).iterator();
			while (it.hasNext()) {
				Criteria criteria = it.next();
				if (!criteria.getId().equals(criteriaIn.getId())) {
					CriteriaOutVO original = criteriaDao.toCriteriaOutVO(criteria);
					criteria.setLoadByDefault(false);
					CoreUtil.modifyVersion(criteria, criteria.getVersion(), now, modifiedUser);
					criteriaDao.update(criteria);
					CriteriaOutVO result = criteriaDao.toCriteriaOutVO(criteria);
					logSystemMessage(criteria, result, now, modifiedUser, SystemMessageCodes.CRITERIA_UPDATED, obfuscateCriterions(result), obfuscateCriterions(original),
							journalEntryDao);
				}
			}
		}
	}

	private SearchResultExcelVO exportExcelHelper(CriteriaInstantVO criteria, DBModule module, String spreadSheetName, PSFVO psf) throws Exception {
		SearchResultExcelWriter writer = ExcelWriterFactory.createSearchResultExcelWriter(module, !CoreUtil.isPassDecryption());
		Collection VOs;
		ArrayList<String> distinctColumnNames;
		HashMap<Long, HashMap<String, Object>> distinctFieldRows;
		switch (module) {
			case INVENTORY_DB:
				VOs = searchInventoryHelper(criteria,
						Settings.getIntNullable(SearchResultExcelSettingCodes.GRAPH_MAX_INVENTORY_INSTANCES, Bundle.SEARCH_RESULT_EXCEL,
								SearchResultExcelDefaultSettings.GRAPH_MAX_INVENTORY_INSTANCES),
						psf);
				distinctColumnNames = new ArrayList<String>();
				distinctFieldRows = new HashMap<Long, HashMap<String, Object>>(VOs.size());
				prepareInventoryDistinctColumns(writer, VOs, distinctColumnNames, distinctFieldRows);
				writer.setVOs(VOs);
				writer.setDistinctColumnNames(distinctColumnNames);
				writer.setDistinctFieldRows(distinctFieldRows);
				break;
			case STAFF_DB:
				VOs = searchStaffHelper(criteria,
						Settings.getIntNullable(SearchResultExcelSettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.SEARCH_RESULT_EXCEL,
								SearchResultExcelDefaultSettings.GRAPH_MAX_STAFF_INSTANCES),
						psf);
				distinctColumnNames = new ArrayList<String>();
				distinctFieldRows = new HashMap<Long, HashMap<String, Object>>(VOs.size());
				prepareStaffDistinctColumns(writer, VOs, distinctColumnNames, distinctFieldRows);
				writer.setVOs(VOs);
				writer.setDistinctColumnNames(distinctColumnNames);
				writer.setDistinctFieldRows(distinctFieldRows);
				break;
			case COURSE_DB:
				writer.setVOs(searchCourseHelper(criteria,
						Settings.getIntNullable(SearchResultExcelSettingCodes.GRAPH_MAX_COURSE_INSTANCES, Bundle.SEARCH_RESULT_EXCEL,
								SearchResultExcelDefaultSettings.GRAPH_MAX_COURSE_INSTANCES),
						psf));
				break;
			case USER_DB:
				writer.setVOs(searchUserHelper(criteria,
						Settings.getIntNullable(SearchResultExcelSettingCodes.GRAPH_MAX_USER_INSTANCES, Bundle.SEARCH_RESULT_EXCEL,
								SearchResultExcelDefaultSettings.GRAPH_MAX_USER_INSTANCES),
						psf));
				break;
			case TRIAL_DB:
				VOs = searchTrialHelper(criteria, psf);
				distinctColumnNames = new ArrayList<String>();
				distinctFieldRows = new HashMap<Long, HashMap<String, Object>>(VOs.size());
				prepareTrialDistinctColumns(writer, VOs, distinctColumnNames, distinctFieldRows);
				writer.setVOs(VOs);
				writer.setDistinctColumnNames(distinctColumnNames);
				writer.setDistinctFieldRows(distinctFieldRows);
				break;
			case PROBAND_DB:
				VOs = searchProbandHelper(criteria,
						Settings.getIntNullable(SearchResultExcelSettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.SEARCH_RESULT_EXCEL,
								SearchResultExcelDefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
						psf);
				distinctColumnNames = new ArrayList<String>();
				distinctFieldRows = new HashMap<Long, HashMap<String, Object>>(VOs.size());
				prepareProbandDistinctColumns(writer, VOs, distinctColumnNames, distinctFieldRows);
				writer.setVOs(VOs);
				writer.setDistinctColumnNames(distinctColumnNames);
				writer.setDistinctFieldRows(distinctFieldRows);
				break;
			case INPUT_FIELD_DB:
				writer.setVOs(searchInputFieldHelper(criteria, psf));
				break;
			case MASS_MAIL_DB:
				writer.setVOs(searchInputFieldHelper(criteria, psf));
				break;
			default:
				writer.setVOs(null);
				break;
		}
		writer.setCriteria(criteria);
		writer.setSpreadSheetName(CommonUtil.isEmptyString(spreadSheetName) ? L10nUtil.getDBModuleName(Locales.USER, module.name()) : spreadSheetName);
		writer.getExcelVO().setRequestingUser(this.getUserDao().toUserOutVO(CoreUtil.getUser()));
		(new ExcelExporter(writer, writer)).write();
		return writer.getExcelVO();
	}

	@Override
	protected CriteriaOutVO handleAddCriteria(AuthenticationVO auth, CriteriaInVO newCriteria,
			Set<CriterionInVO> newCriterions) throws Exception {
		if (CommonUtil.isEmptyString(newCriteria.getLabel())) {
			throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERIA_LABEL_EMPTY, true, null);
		}
		checkCriteriaInput(newCriteria.getModule(), ServiceUtil.toInstant(newCriterions, this.getCriterionDao()), true, false);
		CriteriaDao criteriaDao = this.getCriteriaDao();
		Criteria criteria = criteriaDao.criteriaInVOToEntity(newCriteria);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		clearLoadByDefault(newCriteria, now, user);
		CoreUtil.modifyVersion(criteria, now, user);
		criteria = criteriaDao.create(criteria);
		if (newCriterions != null) {
			CriterionDao criterionDao = this.getCriterionDao();
			Iterator<CriterionInVO> it = newCriterions.iterator();
			while (it.hasNext()) {
				Criterion criterion = criterionDao.criterionInVOToEntity(it.next());
				criteria.addCriterions(criterion);
				criterion.setCriteria(criteria);
				criterionDao.create(criterion);
			}
		}
		CriteriaOutVO result = criteriaDao.toCriteriaOutVO(criteria);
		logSystemMessage(criteria, result, now, user, SystemMessageCodes.CRITERIA_CREATED, obfuscateCriterions(result), null, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected CriteriaOutVO handleDeleteCriteria(AuthenticationVO auth, Long criteriaId,
			boolean defer, boolean force, String deferredDeleteReason) throws Exception {
		CriteriaDao criteriaDao = this.getCriteriaDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CriteriaOutVO result;
		if (!force && defer) {
			Criteria originalCriteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
			CriteriaOutVO original = criteriaDao.toCriteriaOutVO(originalCriteria);
			criteriaDao.evict(originalCriteria);
			Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao, LockMode.PESSIMISTIC_WRITE);
			if (CommonUtil.isEmptyString(deferredDeleteReason)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.DEFERRED_DELETE_REASON_REQUIRED);
			}
			criteria.setDeferredDelete(true);
			criteria.setDeferredDeleteReason(deferredDeleteReason);
			CoreUtil.modifyVersion(criteria, criteria.getVersion(), now, user); // no opt. locking
			criteriaDao.update(criteria);
			result = criteriaDao.toCriteriaOutVO(criteria);
			logSystemMessage(criteria, result, now, user, SystemMessageCodes.CRITERIA_MARKED_FOR_DELETION, obfuscateCriterions(result), obfuscateCriterions(original),
					journalEntryDao);
		} else {
			Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao, LockMode.PESSIMISTIC_WRITE);
			result = criteriaDao.toCriteriaOutVO(criteria);
			CriterionDao criterionDao = this.getCriterionDao();
			Iterator<Criterion> criterionsIt = criteria.getCriterions().iterator();
			while (criterionsIt.hasNext()) {
				Criterion criterion = criterionsIt.next();
				criterion.setCriteria(null);
				criterionDao.remove(criterion);
			}
			criteria.getCriterions().clear();
			JobDao jobDao = this.getJobDao();
			Iterator<Job> jobsIt = criteria.getJobs().iterator();
			while (jobsIt.hasNext()) {
				Job job = jobsIt.next();
				job.setCriteria(null);
				jobDao.remove(job);
			}
			criteria.getJobs().clear();
			Iterator<JournalEntry> jounralEntriesIt = criteria.getJournalEntries().iterator();
			while (jounralEntriesIt.hasNext()) {
				JournalEntry journalEntry = jounralEntriesIt.next();
				journalEntry.setCriteria(null);
				journalEntryDao.remove(journalEntry);
			}
			criteria.getJournalEntries().clear();
			criteriaDao.remove(criteria);
			logSystemMessage(user, result, now, user, SystemMessageCodes.CRITERIA_DELETED, obfuscateCriterions(result), null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected SearchResultExcelVO handleExportCourse(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		SearchResultExcelVO result = exportExcelHelper(instantCriteria, DBModule.COURSE_DB, criteria.getLabel(), psf);
		logExcelExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected SearchResultExcelVO handleExportInputField(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		SearchResultExcelVO result = exportExcelHelper(instantCriteria, DBModule.INPUT_FIELD_DB, criteria.getLabel(), psf);
		logExcelExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected SearchResultExcelVO handleExportInventory(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		SearchResultExcelVO result = exportExcelHelper(instantCriteria, DBModule.INVENTORY_DB, criteria.getLabel(), psf);
		logExcelExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected SearchResultExcelVO handleExportMassMail(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		SearchResultExcelVO result = exportExcelHelper(instantCriteria, DBModule.MASS_MAIL_DB, criteria.getLabel(), psf);
		logExcelExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected SearchResultExcelVO handleExportProband(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		SearchResultExcelVO result = exportExcelHelper(instantCriteria, DBModule.PROBAND_DB, criteria.getLabel(), psf);
		logExcelExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected SearchResultExcelVO handleExportStaff(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		SearchResultExcelVO result = exportExcelHelper(instantCriteria, DBModule.STAFF_DB, criteria.getLabel(), psf);
		logExcelExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected SearchResultExcelVO handleExportTrial(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		SearchResultExcelVO result = exportExcelHelper(instantCriteria, DBModule.TRIAL_DB, criteria.getLabel(), psf);
		logExcelExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected SearchResultExcelVO handleExportUser(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		SearchResultExcelVO result = exportExcelHelper(instantCriteria, DBModule.USER_DB, criteria.getLabel(), psf);
		logExcelExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected CriteriaOutVO handleGetCriteria(AuthenticationVO auth, Long criteriaId) throws Exception {
		CriteriaDao criteriaDao = this.getCriteriaDao();
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
		CriteriaOutVO result = criteriaDao.toCriteriaOutVO(criteria);
		return result;
	}

	@Override
	protected Collection<String> handleGetCriteriaCategories(AuthenticationVO auth, DBModule module, String categoryPrefix, Integer limit)
			throws Exception {
		return this.getCriteriaDao().findCategories(module, categoryPrefix, limit);
	}

	@Override
	protected Collection<CriteriaOutVO> handleGetCriteriaList(AuthenticationVO auth, DBModule module,
			PSFVO psf) throws Exception {
		CriteriaDao criteriaDao = this.getCriteriaDao();
		Collection criterias = criteriaDao.findByModule(module, null, psf);
		criteriaDao.toCriteriaOutVOCollection(criterias);
		return criterias;
	}

	@Override
	protected Collection<CriteriaOutVO> handleGetCriteriaList(AuthenticationVO auth, DBModule module,
			String category, PSFVO psf) throws Exception {
		CriteriaDao criteriaDao = this.getCriteriaDao();
		Collection criterias = criteriaDao.findByModuleCategory(module, category, null, psf);
		criteriaDao.toCriteriaOutVOCollection(criterias);
		return criterias;
	}

	@Override
	protected CriteriaOutVO handleGetDefaultCriteria(AuthenticationVO auth,
			DBModule module) throws Exception {
		CriteriaDao criteriaDao = this.getCriteriaDao();
		try {
			return criteriaDao.toCriteriaOutVO(criteriaDao.findByModule(module, true, null).iterator().next());
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	protected IntermediateSetSummaryVO handleGetIntermediateSets(AuthenticationVO auth, DBModule module,
			Set<CriterionInVO> newCriterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(newCriterions, this.getCriterionDao());
		checkCriteriaInput(module, instantCriteria, true, true);
		return intermediateSetHelper(module, instantCriteria, true, false, psf);
	}

	@Override
	protected IntermediateSetSummaryVO handleGetIntermediateSetsByCriteria(AuthenticationVO auth, Long criteriaId, PSFVO psf) throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		checkCriteriaInput(criteria.getModule(), instantCriteria, true, true);
		return intermediateSetHelper(criteria.getModule(), instantCriteria, true, false, psf);
	}

	@Override
	protected CriteriaInstantVO handleParseCriteria(AuthenticationVO auth, DBModule module,
			Set<CriterionInVO> newCriterions, boolean fullCheck) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(newCriterions, this.getCriterionDao());
		if (fullCheck) {
			checkCriteriaInput(module, instantCriteria, false, false);
		} else {
			instantCriteria.setCriterionExpression(parseCriterions(instantCriteria, false, false, false));
		}
		QueryUtil.parseSearchQuery(instantCriteria, module, this.getCriterionTieDao());
		return instantCriteria;
	}

	@Override
	protected CourseParticipantListPDFVO handleRenderCourseParticipantListPDFs(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		CourseParticipantListPDFVO result = renderCourseParticipantListPDFsHelper(instantCriteria, psf);
		logCourseParticipantListExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected CvPDFVO handleRenderCvPDFs(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		CvPDFVO result = renderCvPDFsHelper(instantCriteria, psf);
		logCvExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected TrainingRecordPDFVO handleRenderTrainingRecordPDFs(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, boolean allTrials, boolean appendCertificates, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		TrainingRecordPDFVO result = renderTrainingRecordPDFsHelper(instantCriteria, allTrials, appendCertificates, psf);
		logTrainingRecordExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected ProbandLetterPDFVO handleRenderProbandLetterPDFs(
			AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		ProbandLetterPDFVO result = renderProbandLetterPDFsHelper(instantCriteria, psf);
		logProbandLetterExport(criteria.getId(), instantCriteria, result);
		return result;
	}

	@Override
	protected Collection<CourseOutVO> handleSearchCourse(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, Integer maxInstances, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		Collection<CourseOutVO> result = searchCourseHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<CourseOutVO> handleSearchCourseByCriteria(
			AuthenticationVO auth, Long criteriaId, Integer maxInstances, PSFVO psf)
			throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		Collection<CourseOutVO> result = searchCourseHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<InputFieldOutVO> handleSearchInputField(
			AuthenticationVO auth, CriteriaInVO criteria, Set<CriterionInVO> criterions, PSFVO psf)
			throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		Collection<InputFieldOutVO> result = searchInputFieldHelper(instantCriteria, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<InputFieldOutVO> handleSearchInputFieldByCriteria(
			AuthenticationVO auth, Long criteriaId, PSFVO psf)
			throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		Collection<InputFieldOutVO> result = searchInputFieldHelper(instantCriteria, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<InventoryOutVO> handleSearchInventory(
			AuthenticationVO auth, CriteriaInVO criteria, Set<CriterionInVO> criterions, Integer maxInstances, PSFVO psf)
			throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		Collection<InventoryOutVO> result = searchInventoryHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<InventoryOutVO> handleSearchInventoryByCriteria(
			AuthenticationVO auth, Long criteriaId, Integer maxInstances, PSFVO psf)
			throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		Collection<InventoryOutVO> result = searchInventoryHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<MassMailOutVO> handleSearchMassMail(
			AuthenticationVO auth, CriteriaInVO criteria, Set<CriterionInVO> criterions, PSFVO psf)
			throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		Collection<MassMailOutVO> result = searchMassMailHelper(instantCriteria, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<MassMailOutVO> handleSearchMassMailByCriteria(
			AuthenticationVO auth, Long criteriaId, PSFVO psf)
			throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		Collection<MassMailOutVO> result = searchMassMailHelper(instantCriteria, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<ProbandOutVO> handleSearchProband(
			AuthenticationVO auth, CriteriaInVO criteria, Set<CriterionInVO> criterions, Integer maxInstances, PSFVO psf)
			throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		Collection<ProbandOutVO> result = searchProbandHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<ProbandOutVO> handleSearchProbandByCriteria(
			AuthenticationVO auth, Long criteriaId, Integer maxInstances, PSFVO psf)
			throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		Collection<ProbandOutVO> result = searchProbandHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<StaffOutVO> handleSearchStaff(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, Integer maxInstances, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		Collection<StaffOutVO> result = searchStaffHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<StaffOutVO> handleSearchStaffByCriteria(
			AuthenticationVO auth, Long criteriaId, Integer maxInstances, PSFVO psf)
			throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		Collection<StaffOutVO> result = searchStaffHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<TrialOutVO> handleSearchTrial(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		Collection<TrialOutVO> result = searchTrialHelper(instantCriteria, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<TrialOutVO> handleSearchTrialByCriteria(
			AuthenticationVO auth, Long criteriaId, PSFVO psf)
			throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		Collection<TrialOutVO> result = searchTrialHelper(instantCriteria, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<UserOutVO> handleSearchUser(AuthenticationVO auth, CriteriaInVO criteria,
			Set<CriterionInVO> criterions, Integer maxInstances, PSFVO psf) throws Exception {
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criterions, this.getCriterionDao());
		Collection<UserOutVO> result = searchUserHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected Collection<UserOutVO> handleSearchUserByCriteria(
			AuthenticationVO auth, Long criteriaId, Integer maxInstances, PSFVO psf)
			throws Exception {
		Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, this.getCriteriaDao());
		CriteriaInstantVO instantCriteria = ServiceUtil.toInstant(criteria.getCriterions(), this.getCriterionDao());
		Collection<UserOutVO> result = searchUserHelper(instantCriteria, maxInstances, psf);
		logSearch(criteria.getId(), instantCriteria, psf, result);
		return result;
	}

	@Override
	protected CriteriaOutVO handleUpdateCriteria(AuthenticationVO auth, CriteriaInVO modifiedCriteria,
			Set<CriterionInVO> newCriterions) throws Exception {
		CriteriaDao criteriaDao = this.getCriteriaDao();
		CriterionDao criterionDao = this.getCriterionDao();
		Criteria originalCriteria = CheckIDUtil.checkCriteriaId(modifiedCriteria.getId(), criteriaDao, LockMode.PESSIMISTIC_WRITE);
		CriteriaOutVO original = criteriaDao.toCriteriaOutVO(originalCriteria);
		criteriaDao.evict(originalCriteria);
		if (CommonUtil.isEmptyString(modifiedCriteria.getLabel())) {
			throw initServiceExceptionWithPosition(ServiceExceptionCodes.CRITERIA_LABEL_EMPTY, true, null);
		}
		checkCriteriaInput(modifiedCriteria.getModule(), ServiceUtil.toInstant(newCriterions, criterionDao), true, false);
		Criteria criteria = criteriaDao.criteriaInVOToEntity(modifiedCriteria);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		clearLoadByDefault(modifiedCriteria, now, user);
		CoreUtil.modifyVersion(originalCriteria, criteria, now, user);
		Iterator<Criterion> oldCriterionsIt = criteria.getCriterions().iterator();
		while (oldCriterionsIt.hasNext()) {
			Criterion criterion = oldCriterionsIt.next();
			criterion.setCriteria(null);
			criterionDao.remove(criterion);
		}
		criteria.getCriterions().clear();
		criteriaDao.update(criteria);
		if (newCriterions != null) {
			Iterator<CriterionInVO> newCriterionsIt = newCriterions.iterator();
			while (newCriterionsIt.hasNext()) {
				Criterion criterion = criterionDao.criterionInVOToEntity(newCriterionsIt.next());
				criteria.addCriterions(criterion);
				criterion.setCriteria(criteria);
				criterionDao.create(criterion);
			}
		}
		CriteriaOutVO result = criteriaDao.toCriteriaOutVO(criteria);
		logSystemMessage(criteria, result, now, user, SystemMessageCodes.CRITERIA_UPDATED, obfuscateCriterions(result), obfuscateCriterions(original), this.getJournalEntryDao());
		return result;
	}

	private IntermediateSetSummaryVO intermediateSetHelper(DBModule module, CriteriaInstantVO criteria, boolean prettyPrint,
			boolean obfuscateCriterions, PSFVO psf) throws Exception {
		QueryUtil.parseSearchQuery(criteria, module, this.getCriterionTieDao()); // populate selectindexes
		ArrayList<CriterionInstantVO> sortedCriterions = new ArrayList<CriterionInstantVO>(criteria.getCriterions());
		Collections.sort(sortedCriterions, new VOPositionComparator(false));
		ArrayList<IntermediateSetDetailVO> setCriterias = null;
		try {
			setCriterias = criterionIntermediateSetParser.parseCriterions(sortedCriterions, prettyPrint, obfuscateCriterions);
		} catch (SyntaxException e) {
			String errorCode = getServiceExcpetionCodeFromSyntaxError(e.getError());
			throw initServiceExceptionWithPosition(errorCode, false, (CriterionInstantVO) e.getToken());
		}
		if (setCriterias.size() == 0) {
			IntermediateSetDetailVO setCriteria = new IntermediateSetDetailVO();
			setCriteria.setA(null);
			setCriteria.setB(null);
			setCriteria.setCriteria(new CriteriaInstantVO());
			setCriteria.setIntersection(null);
			setCriteria.setOperator(null);
			setCriterias.add(setCriteria);
		}
		Iterator<IntermediateSetDetailVO> it = setCriterias.iterator();
		while (it.hasNext()) {
			IntermediateSetDetailVO setCriteria = it.next();
			switch (module) {
				case INVENTORY_DB:
					if (setCriteria.getA() != null) {
						setCriteria.setACount(this.getInventoryDao().getCountByCriteria(setCriteria.getA(), psf));
					}
					if (setCriteria.getB() != null) {
						setCriteria.setBCount(this.getInventoryDao().getCountByCriteria(setCriteria.getB(), psf));
					}
					if (setCriteria.getIntersection() != null) {
						setCriteria.setIntersectionCount(this.getInventoryDao().getCountByCriteria(setCriteria.getIntersection(), psf));
					}
					setCriteria.setCriteriaCount(this.getInventoryDao().getCountByCriteria(setCriteria.getCriteria(), psf));
					break;
				case STAFF_DB:
					if (setCriteria.getA() != null) {
						setCriteria.setACount(this.getStaffDao().getCountByCriteria(setCriteria.getA(), psf));
					}
					if (setCriteria.getB() != null) {
						setCriteria.setBCount(this.getStaffDao().getCountByCriteria(setCriteria.getB(), psf));
					}
					if (setCriteria.getIntersection() != null) {
						setCriteria.setIntersectionCount(this.getStaffDao().getCountByCriteria(setCriteria.getIntersection(), psf));
					}
					setCriteria.setCriteriaCount(this.getStaffDao().getCountByCriteria(setCriteria.getCriteria(), psf));
					break;
				case COURSE_DB:
					if (setCriteria.getA() != null) {
						setCriteria.setACount(this.getCourseDao().getCountByCriteria(setCriteria.getA(), psf));
					}
					if (setCriteria.getB() != null) {
						setCriteria.setBCount(this.getCourseDao().getCountByCriteria(setCriteria.getB(), psf));
					}
					if (setCriteria.getIntersection() != null) {
						setCriteria.setIntersectionCount(this.getCourseDao().getCountByCriteria(setCriteria.getIntersection(), psf));
					}
					setCriteria.setCriteriaCount(this.getCourseDao().getCountByCriteria(setCriteria.getCriteria(), psf));
					break;
				case USER_DB:
					if (setCriteria.getA() != null) {
						setCriteria.setACount(this.getUserDao().getCountByCriteria(setCriteria.getA(), psf));
					}
					if (setCriteria.getB() != null) {
						setCriteria.setBCount(this.getUserDao().getCountByCriteria(setCriteria.getB(), psf));
					}
					if (setCriteria.getIntersection() != null) {
						setCriteria.setIntersectionCount(this.getUserDao().getCountByCriteria(setCriteria.getIntersection(), psf));
					}
					setCriteria.setCriteriaCount(this.getUserDao().getCountByCriteria(setCriteria.getCriteria(), psf));
					break;
				case TRIAL_DB:
					if (setCriteria.getA() != null) {
						setCriteria.setACount(this.getTrialDao().getCountByCriteria(setCriteria.getA(), psf));
					}
					if (setCriteria.getB() != null) {
						setCriteria.setBCount(this.getTrialDao().getCountByCriteria(setCriteria.getB(), psf));
					}
					if (setCriteria.getIntersection() != null) {
						setCriteria.setIntersectionCount(this.getTrialDao().getCountByCriteria(setCriteria.getIntersection(), psf));
					}
					setCriteria.setCriteriaCount(this.getTrialDao().getCountByCriteria(setCriteria.getCriteria(), psf));
					break;
				case PROBAND_DB:
					if (setCriteria.getA() != null) {
						setCriteria.setACount(this.getProbandDao().getCountByCriteria(setCriteria.getA(), psf));
					}
					if (setCriteria.getB() != null) {
						setCriteria.setBCount(this.getProbandDao().getCountByCriteria(setCriteria.getB(), psf));
					}
					if (setCriteria.getIntersection() != null) {
						setCriteria.setIntersectionCount(this.getProbandDao().getCountByCriteria(setCriteria.getIntersection(), psf));
					}
					setCriteria.setCriteriaCount(this.getProbandDao().getCountByCriteria(setCriteria.getCriteria(), psf));
					break;
				case INPUT_FIELD_DB:
					if (setCriteria.getA() != null) {
						setCriteria.setACount(this.getInputFieldDao().getCountByCriteria(setCriteria.getA(), psf));
					}
					if (setCriteria.getB() != null) {
						setCriteria.setBCount(this.getInputFieldDao().getCountByCriteria(setCriteria.getB(), psf));
					}
					if (setCriteria.getIntersection() != null) {
						setCriteria.setIntersectionCount(this.getInputFieldDao().getCountByCriteria(setCriteria.getIntersection(), psf));
					}
					setCriteria.setCriteriaCount(this.getInputFieldDao().getCountByCriteria(setCriteria.getCriteria(), psf));
					break;
				case MASS_MAIL_DB:
					if (setCriteria.getA() != null) {
						setCriteria.setACount(this.getMassMailDao().getCountByCriteria(setCriteria.getA(), psf));
					}
					if (setCriteria.getB() != null) {
						setCriteria.setBCount(this.getMassMailDao().getCountByCriteria(setCriteria.getB(), psf));
					}
					if (setCriteria.getIntersection() != null) {
						setCriteria.setIntersectionCount(this.getMassMailDao().getCountByCriteria(setCriteria.getIntersection(), psf));
					}
					setCriteria.setCriteriaCount(this.getMassMailDao().getCountByCriteria(setCriteria.getCriteria(), psf));
					break;
				default:
					break;
			}
		}
		IntermediateSetSummaryVO result = new IntermediateSetSummaryVO();
		result.setSets(setCriterias);
		result.setParsed(criteria);
		return result;
	}

	private void logCourseParticipantListExport(Long criteriaId, CriteriaInstantVO criteriaInstantVO, CourseParticipantListPDFVO result) throws Exception {
		if (criteriaId != null && criteriaInstantVO != null && result != null) {
			User user = CoreUtil.getUser();
			Timestamp now = CommonUtil.dateToTimestamp(result.getContentTimestamp());
			CriteriaDao criteriaDao = this.getCriteriaDao();
			Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
			CriteriaOutVO criteriaVO = criteriaDao.toCriteriaOutVO(criteria);
			String systemMessageCode = SystemMessageCodes.SEARCH_COURSE_PARTICIPANT_LIST_EXPORTED;
			this.getJournalEntryDao().addSystemMessage(
					criteria,
					now,
					user,
					systemMessageCode,
					new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO), Integer.toString(result.getCourses().size()) },
					new Object[] {
							CoreUtil.getSystemMessageCommentContent(obfuscateCriterions(criteriaInstantVO), null,
									!CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)),
							CoreUtil.getSystemMessageCommentContent(result, null, !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)) });
		}
	}

	private void logCvExport(Long criteriaId, CriteriaInstantVO criteriaInstantVO, CvPDFVO result) throws Exception {
		if (criteriaId != null && criteriaInstantVO != null && result != null) {
			User user = CoreUtil.getUser();
			Timestamp now = CommonUtil.dateToTimestamp(result.getContentTimestamp());
			CriteriaDao criteriaDao = this.getCriteriaDao();
			Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
			CriteriaOutVO criteriaVO = criteriaDao.toCriteriaOutVO(criteria);
			String systemMessageCode = SystemMessageCodes.SEARCH_CV_EXPORTED;
			this.getJournalEntryDao().addSystemMessage(
					criteria,
					now,
					user,
					systemMessageCode,
					new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO), Integer.toString(result.getStafves().size()) },
					new Object[] {
							CoreUtil.getSystemMessageCommentContent(obfuscateCriterions(criteriaInstantVO), null,
									!CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)),
							CoreUtil.getSystemMessageCommentContent(result, null, !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)) });
		}
	}

	private void logTrainingRecordExport(Long criteriaId, CriteriaInstantVO criteriaInstantVO, TrainingRecordPDFVO result) throws Exception {
		if (criteriaId != null && criteriaInstantVO != null && result != null) {
			User user = CoreUtil.getUser();
			Timestamp now = CommonUtil.dateToTimestamp(result.getContentTimestamp());
			CriteriaDao criteriaDao = this.getCriteriaDao();
			Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
			CriteriaOutVO criteriaVO = criteriaDao.toCriteriaOutVO(criteria);
			String systemMessageCode = SystemMessageCodes.SEARCH_TRAINING_RECORD_EXPORTED;
			this.getJournalEntryDao().addSystemMessage(
					criteria,
					now,
					user,
					systemMessageCode,
					new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO), Integer.toString(result.getStafves().size()) },
					new Object[] {
							CoreUtil.getSystemMessageCommentContent(obfuscateCriterions(criteriaInstantVO), null,
									!CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)),
							CoreUtil.getSystemMessageCommentContent(result, null, !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)) });
		}
	}

	private void logExcelExport(Long criteriaId, CriteriaInstantVO criteriaInstantVO, SearchResultExcelVO result) throws Exception {
		if (criteriaId != null && criteriaInstantVO != null && result != null) {
			User user = CoreUtil.getUser();
			Timestamp now = CommonUtil.dateToTimestamp(result.getContentTimestamp());
			CriteriaDao criteriaDao = this.getCriteriaDao();
			Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
			CriteriaOutVO criteriaVO = criteriaDao.toCriteriaOutVO(criteria);
			String systemMessageCode = SystemMessageCodes.SEARCH_EXPORTED;
			this.getJournalEntryDao().addSystemMessage(
					criteria,
					now,
					user,
					systemMessageCode,
					new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO), result.getRowCount() },
					new Object[] {
							CoreUtil.getSystemMessageCommentContent(obfuscateCriterions(criteriaInstantVO), null,
									!CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)),
							CoreUtil.getSystemMessageCommentContent(result, null, !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)) });
		}
	}

	private void logProbandLetterExport(Long criteriaId, CriteriaInstantVO criteriaInstantVO, ProbandLetterPDFVO result) throws Exception {
		if (criteriaId != null && criteriaInstantVO != null && result != null) {
			User user = CoreUtil.getUser();
			Timestamp now = CommonUtil.dateToTimestamp(result.getContentTimestamp());
			CriteriaDao criteriaDao = this.getCriteriaDao();
			Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
			CriteriaOutVO criteriaVO = criteriaDao.toCriteriaOutVO(criteria);
			String systemMessageCode = SystemMessageCodes.SEARCH_PROBAND_LETTER_EXPORTED;
			this.getJournalEntryDao().addSystemMessage(
					criteria,
					now,
					user,
					systemMessageCode,
					new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO), Integer.toString(result.getProbands().size()) },
					new Object[] {
							CoreUtil.getSystemMessageCommentContent(obfuscateCriterions(criteriaInstantVO), null,
									!CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)),
							CoreUtil.getSystemMessageCommentContent(result, null, !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)) });
		}
	}

	private void logSearch(Long criteriaId, CriteriaInstantVO criteriaInstantVO, PSFVO psf, Collection result) throws Exception {
		if (psf == null || psf.getPageSize() == null || psf.getPageSize() == 0 ||
				(Settings.getBoolean(SettingCodes.LOG_SEARCH_FILTERED, Bundle.SETTINGS, DefaultSettings.LOG_SEARCH_FILTERED) && psf.getPageSize() > 0)) {
			if (criteriaId != null && criteriaInstantVO != null) {
				Timestamp now = new Timestamp(System.currentTimeMillis());
				User user = CoreUtil.getUser();
				CriteriaDao criteriaDao = this.getCriteriaDao();
				Criteria criteria = CheckIDUtil.checkCriteriaId(criteriaId, criteriaDao);
				CriteriaOutVO criteriaVO = criteriaDao.toCriteriaOutVO(criteria);
				String systemMessageCode = (psf != null && psf.getFilters() != null && psf.getFilters().size() > 0 ? SystemMessageCodes.SEARCH_FILTERED
						: SystemMessageCodes.SEARCH_PERFORMED);
				this.getJournalEntryDao().addSystemMessage(
						criteria,
						now,
						user,
						systemMessageCode,
						new Object[] { CommonUtil.criteriaOutVOToString(criteriaVO),
								psf == null ? Integer.toString(result.size()) : (psf.getRowCount() == null ? new Long(0) : psf.getRowCount()).toString() },
						new Object[] {
								CoreUtil.getSystemMessageCommentContent(obfuscateCriterions(criteriaInstantVO), null,
										!CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)),
								CoreUtil.getSystemMessageCommentContent(psf, null, !CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)),
								CoreUtil.getSystemMessageCommentContent(obfuscateCriterions(criteriaVO), null,
										!CommonUtil.getUseJournalEncryption(JournalModule.CRITERIA_JOURNAL, null)) });
			}
		}
	}

	private CriteriaInstantVO obfuscateCriterions(CriteriaInstantVO criteriaInstantVO) throws Exception {
		if (criteriaInstantVO != null) {
			CriteriaInstantVO result = new CriteriaInstantVO(criteriaInstantVO);
			ArrayList<CriterionInstantVO> obfuscatedCriterions = new ArrayList<CriterionInstantVO>();
			CriterionPropertyDao criterionPropertyDao = this.getCriterionPropertyDao();
			Iterator<CriterionInstantVO> criterionsIt = criteriaInstantVO.getCriterions().iterator();
			while (criterionsIt.hasNext()) {
				CriterionInstantVO obfuscatedCriterion = new CriterionInstantVO(criterionsIt.next());
				CriterionProperty property = null;
				if (obfuscatedCriterion.getPropertyId() != null) {
					property = criterionPropertyDao.load(obfuscatedCriterion.getPropertyId());
				}
				if (property != null && OmittedFields.isOmitted(property.getProperty())) {
					obfuscatedCriterion.setStringValue(CoreUtil.OBFUSCATED_STRING);
				}
				obfuscatedCriterions.add(obfuscatedCriterion);
			}
			result.setCriterions(obfuscatedCriterions);
			return result;
		}
		return null;
	}

	private String parseCriterions(CriteriaInstantVO instantCriteria, boolean prettyPrint, boolean obfuscateCriterions, boolean logError) throws ServiceException {
		ArrayList<CriterionInstantVO> sortedCriterions = new ArrayList<CriterionInstantVO>(instantCriteria.getCriterions());
		Collections.sort(sortedCriterions, new VOPositionComparator(false));
		try {
			return criterionSyntaxParser.parseCriterions(sortedCriterions, prettyPrint, obfuscateCriterions);
		} catch (SyntaxException e) {
			String errorCode = getServiceExcpetionCodeFromSyntaxError(e.getError());
			throw initServiceExceptionWithPosition(errorCode, logError, (CriterionInstantVO) e.getToken());
		}
	}

	private void prepareInventoryDistinctColumns(SearchResultExcelWriter writer, Collection inventoryVOs, ArrayList<String> distinctColumnNames,
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) throws Exception {
		boolean showTags = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_INVENTORY_TAGS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.SHOW_INVENTORY_TAGS);
		InventoryTagDao inventoryTagDao = this.getInventoryTagDao();
		Collection inventoryTags = showTags ? inventoryTagDao.findByVisibleIdExcel(null, null, true) : new ArrayList();
		inventoryTagDao.toInventoryTagVOCollection(inventoryTags);
		distinctColumnNames.ensureCapacity(inventoryTags.size());
		Iterator<InventoryTagVO> inventoryTagsIt = inventoryTags.iterator();
		while (inventoryTagsIt.hasNext()) {
			distinctColumnNames.add(inventoryTagsIt.next().getName());
		}
		InventoryTagValueDao inventoryTagValueDao = this.getInventoryTagValueDao();
		Iterator<InventoryOutVO> inventoryVOsIt = inventoryVOs.iterator();
		while (inventoryVOsIt.hasNext()) {
			InventoryOutVO inventoryVO = inventoryVOsIt.next();
			HashMap<String, Object> fieldRow = new HashMap<String, Object>(distinctColumnNames.size());
			Collection tagValues = showTags ? inventoryTagValueDao.findByInventory(inventoryVO.getId(), null) : new ArrayList<InventoryTagValue>();
			inventoryTagValueDao.toInventoryTagValueOutVOCollection(tagValues);
			Iterator<InventoryTagValueOutVO> tagValuesIt = tagValues.iterator();
			while (tagValuesIt.hasNext()) {
				InventoryTagValueOutVO tagValueOutVO = tagValuesIt.next();
				StringBuilder fieldValue;
				if (fieldRow.containsKey(tagValueOutVO.getTag().getName())) {
					fieldValue = new StringBuilder((String) fieldRow.get(tagValueOutVO.getTag().getName()));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(tagValueOutVO.getValue());
				fieldRow.put(tagValueOutVO.getTag().getName(), fieldValue.toString());
			}
			distinctFieldRows.put(inventoryVO.getId(), fieldRow);
		}
	}

	private void prepareProbandDistinctColumns(SearchResultExcelWriter writer, Collection probandVOs, ArrayList<String> distinctColumnNames,
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) throws Exception {
		Boolean person = Settings.getBooleanNullable(SearchResultExcelSettingCodes.SHOW_PROBAND_PERSON_COLUMNS, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_PROBAND_PERSON_COLUMNS);
		Boolean animal = Settings.getBooleanNullable(SearchResultExcelSettingCodes.SHOW_PROBAND_ANIMAL_COLUMNS, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_PROBAND_ANIMAL_COLUMNS);
		boolean showTags = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_PROBAND_TAGS, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_PROBAND_TAGS);
		boolean showContactDetails = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_PROBAND_CONTACT_DETAILS, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_PROBAND_CONTACT_DETAILS);
		boolean showAddresses = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_PROBAND_ADDRESSES, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_PROBAND_ADDRESSES);
		boolean aggregateAddresses = Settings.getBoolean(SearchResultExcelSettingCodes.AGGREGATE_PROBAND_ADDRESSES, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.AGGREGATE_PROBAND_ADDRESSES);
		boolean aggregateContactDetails = Settings.getBoolean(SearchResultExcelSettingCodes.AGGREGATE_PROBAND_CONTACT_DETAILS, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.AGGREGATE_PROBAND_CONTACT_DETAILS);
		boolean showInquiries = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_INQUIRIES, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_INQUIRIES);
		boolean showAllInquiries = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_ALL_INQUIRIES, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_ALL_INQUIRIES);
		boolean showAllInquiryDates = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_ALL_INQUIRY_DATES, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_ALL_INQUIRY_DATES);
		boolean showEmptyInquiryColumns = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_EMPTY_INQUIRY_COLUMNS, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_EMPTY_INQUIRY_COLUMNS);
		ExcelCellFormat rowCellFormat = Settings.getExcelCellFormat(SearchResultExcelSettingCodes.PROBAND_ROW_FORMAT, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.PROBAND_ROW_FORMAT);
		ProbandTagDao probandTagDao = this.getProbandTagDao();
		Collection probandTags = showTags ? probandTagDao.findByPersonAnimalIdExcel(person, animal, null, true) : new ArrayList();
		probandTagDao.toProbandTagVOCollection(probandTags);
		ContactDetailTypeDao contactDetailTypeDao = this.getContactDetailTypeDao();
		Collection contactDetailTypes = showContactDetails ? contactDetailTypeDao.findByStaffProbandAnimalId(null, person, animal, null)
				: new ArrayList();
		contactDetailTypeDao.toContactDetailTypeVOCollection(contactDetailTypes);
		AddressTypeDao addressTypeDao = this.getAddressTypeDao();
		Collection addressTypes = showAddresses ? addressTypeDao.findByStaffProbandAnimalId(null, person, animal, null) : new ArrayList();
		addressTypeDao.toAddressTypeVOCollection(addressTypes);
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = showInquiries ? inquiryDao.findByDepartmentActiveExcelSorted(null, null, null, showAllInquiries || showAllInquiryDates ? null : true, true)
				: new ArrayList();
		inquiryDao.toInquiryOutVOCollection(inquiries);
		if (CoreUtil.isPassDecryption()) {
			distinctColumnNames.ensureCapacity(probandTags.size()
					+ (aggregateContactDetails ? 2 : contactDetailTypes.size())
					+ (aggregateAddresses ? 3 : addressTypes.size()));
			Iterator<ProbandTagVO> probandTagsIt = probandTags.iterator();
			while (probandTagsIt.hasNext()) {
				distinctColumnNames.add(probandTagsIt.next().getName());
			}
			if (aggregateContactDetails) {
				distinctColumnNames.add(SearchResultExcelWriter.getEmailContactDetailsColumnName());
				distinctColumnNames.add(SearchResultExcelWriter.getPhoneContactDetailsColumnName());
			} else {
				Iterator<ContactDetailTypeVO> contactDetailTypesIt = contactDetailTypes.iterator();
				while (contactDetailTypesIt.hasNext()) {
					distinctColumnNames.add(contactDetailTypesIt.next().getName());
				}
			}
			if (aggregateAddresses) {
				distinctColumnNames.add(SearchResultExcelWriter.getStreetsColumnName());
				distinctColumnNames.add(SearchResultExcelWriter.getZipCodesColumnName());
				distinctColumnNames.add(SearchResultExcelWriter.getCityNamesColumnName());
			} else {
				Iterator<AddressTypeVO> addressTypesIt = addressTypes.iterator();
				while (addressTypesIt.hasNext()) {
					distinctColumnNames.add(addressTypesIt.next().getName());
				}
			}
		}
		Iterator<InquiryOutVO> inquiriesIt;
		HashMap<Long, Long> inquiryValueCountMap = new HashMap<Long, Long>(inquiries.size());
		ProbandTagValueDao probandTagValueDao = this.getProbandTagValueDao();
		ProbandContactDetailValueDao probandContactDetailValueDao = this.getProbandContactDetailValueDao();
		ProbandAddressDao probandAddressDao = this.getProbandAddressDao();
		InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
		Iterator<ProbandOutVO> probandVOsIt = probandVOs.iterator();
		while (probandVOsIt.hasNext()) {
			ProbandOutVO probandVO = probandVOsIt.next();
			HashMap<String, Object> fieldRow = new HashMap<String, Object>(distinctColumnNames.size());
			String fieldKey;
			Collection tagValues = showTags ? probandTagValueDao.findByProband(probandVO.getId(), null) : new ArrayList<ProbandTagValue>();
			probandTagValueDao.toProbandTagValueOutVOCollection(tagValues);
			Iterator<ProbandTagValueOutVO> tagValuesIt = tagValues.iterator();
			while (tagValuesIt.hasNext()) {
				ProbandTagValueOutVO tagValueOutVO = tagValuesIt.next();
				StringBuilder fieldValue;
				if (fieldRow.containsKey(tagValueOutVO.getTag().getName())) {
					fieldValue = new StringBuilder((String) fieldRow.get(tagValueOutVO.getTag().getName()));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(tagValueOutVO.getValue());
				fieldRow.put(tagValueOutVO.getTag().getName(), fieldValue.toString());
			}
			Collection probandContactDetails = showContactDetails ? probandContactDetailValueDao.findByProband(probandVO.getId(), null, false, null, null, null)
					: new ArrayList<ProbandContactDetailValue>();
			probandContactDetailValueDao.toProbandContactDetailValueOutVOCollection(probandContactDetails);
			ServiceUtil.appendDistinctProbandContactColumnValues(probandContactDetails,
					fieldRow,
					aggregateContactDetails,
					SearchResultExcelWriter.getEmailContactDetailsColumnName(),
					SearchResultExcelWriter.getPhoneContactDetailsColumnName());
			Collection probandAddresses = showAddresses ? probandAddressDao.findByProband(probandVO.getId(), null, null, null, null) : new ArrayList<ProbandAddress>();
			probandAddressDao.toProbandAddressOutVOCollection(probandAddresses);
			ServiceUtil.appendDistinctProbandAddressColumnValues(probandAddresses,
					fieldRow,
					aggregateAddresses,
					SearchResultExcelWriter.getStreetsColumnName(),
					SearchResultExcelWriter.getZipCodesColumnName(),
					SearchResultExcelWriter.getCityNamesColumnName());
			HashMap<Long, InquiryValue> inquiryValueMap;
			if (showInquiries) {
				Collection<InquiryValue> inquiryValues = inquiryValueDao.findByTrialActiveProbandField(null, null, null, probandVO.getId(), null);
				inquiryValueMap = new HashMap<Long, InquiryValue>(inquiryValues.size());
				Iterator<InquiryValue> inquiryValuesIt = inquiryValues.iterator();
				while (inquiryValuesIt.hasNext()) {
					InquiryValue inquiryValue = inquiryValuesIt.next();
					inquiryValueMap.put(inquiryValue.getInquiry().getId(), inquiryValue);
					long count;
					if (inquiryValueCountMap.containsKey(inquiryValue.getInquiry().getId())) {
						count = inquiryValueCountMap.get(inquiryValue.getInquiry().getId());
					} else {
						count = 0l;
					}
					inquiryValueCountMap.put(inquiryValue.getInquiry().getId(), count + 1l);
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
					inquiryValueVO = ServiceUtil.createPresetInquiryOutValue(probandVO, inquiryVO, null);
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
					fieldKey = SearchResultExcelWriter.getInquiryColumnName(inquiryVO);
					fieldRow.put(fieldKey, fieldValue);
				}
				if (showAllInquiryDates || inquiryVO.isExcelDate()) {
					fieldKey = SearchResultExcelWriter.getInquiryDateColumnName(inquiryVO);
					Date date = inquiryValueVO.getModifiedTimestamp();
					if (rowCellFormat != null && rowCellFormat.isDateTimeUserTimezone()) {
						date = DateCalc.convertTimezone(date, TimeZone.getDefault(), CoreUtil.getUserContext().getTimeZone());
					}
					fieldRow.put(fieldKey, DateCalc.getStartOfDay(date));
				}
			}
			distinctFieldRows.put(probandVO.getId(), fieldRow);
		}
		inquiriesIt = inquiries.iterator();
		while (inquiriesIt.hasNext()) {
			InquiryOutVO inquiryVO = inquiriesIt.next();
			if (showEmptyInquiryColumns || (inquiryValueCountMap.containsKey(inquiryVO.getId()) && inquiryValueCountMap.get(inquiryVO.getId()) > 0l)) {
				if (showAllInquiries || inquiryVO.isExcelValue()) {
					distinctColumnNames.add(SearchResultExcelWriter.getInquiryColumnName(inquiryVO));
				}
				if (showAllInquiryDates || inquiryVO.isExcelDate()) {
					distinctColumnNames.add(SearchResultExcelWriter.getInquiryDateColumnName(inquiryVO));
				}
			}
		}
	}

	private void prepareStaffDistinctColumns(SearchResultExcelWriter writer, Collection staffVOs, ArrayList<String> distinctColumnNames,
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) throws Exception {
		boolean showTags = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_STAFF_TAGS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.SHOW_STAFF_TAGS);
		StaffTagDao staffTagDao = this.getStaffTagDao();
		Collection staffTags = showTags ? staffTagDao.findByPersonOrganisationIdExcelTrainingRecord(null, null, null, true, null) : new ArrayList();
		staffTagDao.toStaffTagVOCollection(staffTags);
		distinctColumnNames.ensureCapacity(staffTags.size());
		Iterator<StaffTagVO> staffTagsIt = staffTags.iterator();
		while (staffTagsIt.hasNext()) {
			distinctColumnNames.add(staffTagsIt.next().getName());
		}
		StaffTagValueDao staffTagValueDao = this.getStaffTagValueDao();
		Iterator<StaffOutVO> staffVOsIt = staffVOs.iterator();
		while (staffVOsIt.hasNext()) {
			StaffOutVO staffVO = staffVOsIt.next();
			HashMap<String, Object> fieldRow = new HashMap<String, Object>(distinctColumnNames.size());
			Collection tagValues = showTags ? staffTagValueDao.findByStaff(staffVO.getId(), null) : new ArrayList<StaffTagValue>();
			staffTagValueDao.toStaffTagValueOutVOCollection(tagValues);
			Iterator<StaffTagValueOutVO> tagValuesIt = tagValues.iterator();
			while (tagValuesIt.hasNext()) {
				StaffTagValueOutVO tagValueOutVO = tagValuesIt.next();
				StringBuilder fieldValue;
				if (fieldRow.containsKey(tagValueOutVO.getTag().getName())) {
					fieldValue = new StringBuilder((String) fieldRow.get(tagValueOutVO.getTag().getName()));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(tagValueOutVO.getValue());
				fieldRow.put(tagValueOutVO.getTag().getName(), fieldValue.toString());
			}
			distinctFieldRows.put(staffVO.getId(), fieldRow);
		}
	}

	private void prepareTrialDistinctColumns(SearchResultExcelWriter writer, Collection trialVOs, ArrayList<String> distinctColumnNames,
			HashMap<Long, HashMap<String, Object>> distinctFieldRows) throws Exception {
		boolean showTags = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_TRIAL_TAGS, Bundle.SEARCH_RESULT_EXCEL, SearchResultExcelDefaultSettings.SHOW_TRIAL_TAGS);
		boolean showTeamMembers = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_TEAM_MEMBERS, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_TEAM_MEMBERS);
		boolean showTimelineEvents = Settings.getBoolean(SearchResultExcelSettingCodes.SHOW_TIMELINE_EVENTS, Bundle.SEARCH_RESULT_EXCEL,
				SearchResultExcelDefaultSettings.SHOW_TIMELINE_EVENTS);
		TrialTagDao trialTagDao = this.getTrialTagDao();
		Collection trialTags = showTags ? trialTagDao.findByVisibleIdExcelPayoffs(null, null, true, null) : new ArrayList();
		trialTagDao.toTrialTagVOCollection(trialTags);
		TeamMemberRoleDao teamMemberRoleDao = this.getTeamMemberRoleDao();
		Collection roles = showTeamMembers ? teamMemberRoleDao.findByVisibleId(true, null) : new ArrayList();
		teamMemberRoleDao.toTeamMemberRoleVOCollection(roles);
		TimelineEventTypeDao timelineEventTypeDao = this.getTimelineEventTypeDao();
		Collection eventTypes = showTimelineEvents ? timelineEventTypeDao.findByVisibleId(true, null) : new ArrayList();
		timelineEventTypeDao.toTimelineEventTypeVOCollection(eventTypes);
		distinctColumnNames.ensureCapacity(trialTags.size() + roles.size() + eventTypes.size());
		Iterator<TrialTagVO> trialTagsIt = trialTags.iterator();
		while (trialTagsIt.hasNext()) {
			distinctColumnNames.add(trialTagsIt.next().getName());
		}
		Iterator<TeamMemberRoleVO> rolesIt = roles.iterator();
		while (rolesIt.hasNext()) {
			distinctColumnNames.add(rolesIt.next().getName());
		}
		Iterator<TimelineEventTypeVO> eventTypesIt = eventTypes.iterator();
		while (eventTypesIt.hasNext()) {
			distinctColumnNames.add(eventTypesIt.next().getName());
		}
		TrialTagValueDao trialTagValueDao = this.getTrialTagValueDao();
		TeamMemberDao teamMemberDao = this.getTeamMemberDao();
		TimelineEventDao timelineEventDao = this.getTimelineEventDao();
		Iterator<TrialOutVO> trialVOsIt = trialVOs.iterator();
		while (trialVOsIt.hasNext()) {
			TrialOutVO trialVO = trialVOsIt.next();
			HashMap<String, Object> fieldRow = new HashMap<String, Object>(distinctColumnNames.size());
			Collection tagValues = showTags ? trialTagValueDao.findByTrial(trialVO.getId(), null) : new ArrayList<TrialTagValue>();
			trialTagValueDao.toTrialTagValueOutVOCollection(tagValues);
			Iterator<TrialTagValueOutVO> tagValuesIt = tagValues.iterator();
			while (tagValuesIt.hasNext()) {
				TrialTagValueOutVO tagValueOutVO = tagValuesIt.next();
				StringBuilder fieldValue;
				if (fieldRow.containsKey(tagValueOutVO.getTag().getName())) {
					fieldValue = new StringBuilder((String) fieldRow.get(tagValueOutVO.getTag().getName()));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(tagValueOutVO.getValue());
				fieldRow.put(tagValueOutVO.getTag().getName(), fieldValue.toString());
			}
			Collection teamMembers = showTeamMembers ? teamMemberDao.findByTrialStaffRole(trialVO.getId(), null, null, null, null) : new ArrayList<TeamMember>();
			teamMemberDao.toTeamMemberOutVOCollection(teamMembers);
			Iterator<TeamMemberOutVO> teamMembersIt = teamMembers.iterator();
			while (teamMembersIt.hasNext()) {
				TeamMemberOutVO teamMemberOutVO = teamMembersIt.next();
				StringBuilder fieldValue;
				if (fieldRow.containsKey(teamMemberOutVO.getRole().getName())) {
					fieldValue = new StringBuilder((String) fieldRow.get(teamMemberOutVO.getRole().getName()));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(teamMemberOutVO.getStaff().getName());
				fieldRow.put(teamMemberOutVO.getRole().getName(), fieldValue.toString());
			}
			Collection timelineEvents = showTimelineEvents ? timelineEventDao.findByTrial(trialVO.getId(), null) : new ArrayList<TimelineEvent>();
			timelineEventDao.toTimelineEventOutVOCollection(timelineEvents);
			Iterator<TimelineEventOutVO> timelineEventsIt = timelineEvents.iterator();
			while (timelineEventsIt.hasNext()) {
				TimelineEventOutVO timelineEventOutVO = timelineEventsIt.next();
				StringBuilder fieldValue;
				if (fieldRow.containsKey(timelineEventOutVO.getType().getName())) {
					fieldValue = new StringBuilder((String) fieldRow.get(timelineEventOutVO.getType().getName()));
				} else {
					fieldValue = new StringBuilder();
				}
				if (fieldValue.length() > 0) {
					fieldValue.append(ExcelUtil.EXCEL_LINE_BREAK);
				}
				fieldValue.append(writer.getTimelineEventFieldValue(timelineEventOutVO));
				fieldRow.put(timelineEventOutVO.getType().getName(), fieldValue.toString());
			}
			distinctFieldRows.put(trialVO.getId(), fieldRow);
		}
	}

	private CourseParticipantListPDFVO renderCourseParticipantListPDFsHelper(CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		Collection<CourseOutVO> courseVOs = searchCourseHelper(criteria, Settings.getInt(CourseParticipantListPDFSettingCodes.GRAPH_MAX_COURSE_INSTANCES,
				Bundle.COURSE_PARTICIPANT_LIST_PDF, CourseParticipantListPDFDefaultSettings.GRAPH_MAX_COURSE_INSTANCES), psf);
		CourseParticipantListPDFPainter painter = ServiceUtil.createCourseParticipantListPDFPainter(courseVOs, false, this.getLecturerDao(), this.getLecturerCompetenceDao(),
				this.getCourseParticipationStatusEntryDao(), this.getInventoryBookingDao());
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(CoreUtil.getUser()));
		painter.getPdfVO().setCriteria(criteria);
		(new PDFImprinter(painter, painter)).render();
		return painter.getPdfVO();
	}

	private CvPDFVO renderCvPDFsHelper(CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		Collection<StaffOutVO> staffVOs = searchStaffHelper(criteria,
				Settings.getInt(CVPDFSettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.CV_PDF, CVPDFDefaultSettings.GRAPH_MAX_STAFF_INSTANCES), psf);
		CVPDFPainter painter = ServiceUtil.createCVPDFPainter(staffVOs, this.getStaffDao(), this.getCvSectionDao(), this.getCvPositionDao(),
				this.getCourseParticipationStatusEntryDao(), this.getStaffAddressDao());
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(CoreUtil.getUser()));
		painter.getPdfVO().setCriteria(criteria);
		(new PDFImprinter(painter, painter)).render();
		return painter.getPdfVO();
	}

	private TrainingRecordPDFVO renderTrainingRecordPDFsHelper(CriteriaInstantVO criteria, boolean allTrials, boolean appendCertificates, PSFVO psf) throws Exception {
		Collection<StaffOutVO> staffVOs = searchStaffHelper(criteria,
				Settings.getInt(TrainingRecordPDFSettingCodes.GRAPH_MAX_STAFF_INSTANCES, Bundle.TRAINING_RECORD_PDF, TrainingRecordPDFDefaultSettings.GRAPH_MAX_STAFF_INSTANCES),
				psf);
		TrainingRecordPDFPainter painter = ServiceUtil.createTrainingRecordPDFPainter(staffVOs, null, !allTrials, appendCertificates, this.getStaffDao(), this.getTrialDao(),
				this.getStaffTagValueDao(),
				this.getTrainingRecordSectionDao(),
				this.getCourseParticipationStatusEntryDao());
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(CoreUtil.getUser()));
		painter.getPdfVO().setCriteria(criteria);
		(new PDFImprinter(painter, painter)).render();
		return painter.getPdfVO();
	}

	private ProbandLetterPDFVO renderProbandLetterPDFsHelper(CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		Collection<ProbandOutVO> probandVOs = searchProbandHelper(criteria,
				Settings.getInt(ProbandLetterPDFSettingCodes.GRAPH_MAX_PROBAND_INSTANCES, Bundle.PROBAND_LETTER_PDF, ProbandLetterPDFDefaultSettings.GRAPH_MAX_PROBAND_INSTANCES),
				psf);
		ProbandLetterPDFPainter painter = ServiceUtil.createProbandLetterPDFPainter(probandVOs, this.getProbandAddressDao());
		painter.getPdfVO().setRequestingUser(this.getUserDao().toUserOutVO(CoreUtil.getUser()));
		painter.getPdfVO().setCriteria(criteria);
		(new PDFImprinter(painter, painter)).render();
		return painter.getPdfVO();
	}

	private ArrayList<CourseOutVO> searchCourseHelper(CriteriaInstantVO criteria, Integer maxInstances, PSFVO psf) throws Exception {
		checkCriteriaInput(DBModule.COURSE_DB, criteria, true, true);
		CourseDao courseDao = this.getCourseDao();
		Collection<Course> courses = courseDao.findByCriteria(criteria, psf);
		ArrayList<CourseOutVO> result = new ArrayList<CourseOutVO>(courses.size());
		Iterator<Course> coursesIt = courses.iterator();
		while (coursesIt.hasNext()) {
			result.add(courseDao.toCourseOutVO(coursesIt.next(), maxInstances));
		}
		return result;
	}

	private Collection<InputFieldOutVO> searchInputFieldHelper(CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		checkCriteriaInput(DBModule.INPUT_FIELD_DB, criteria, true, true);
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		Collection inputFields = inputFieldDao.findByCriteria(criteria, psf);
		inputFieldDao.toInputFieldOutVOCollection(inputFields);
		return inputFields;
	}

	private ArrayList<InventoryOutVO> searchInventoryHelper(CriteriaInstantVO criteria, Integer maxInstances, PSFVO psf) throws Exception {
		checkCriteriaInput(DBModule.INVENTORY_DB, criteria, true, true);
		InventoryDao inventoryDao = this.getInventoryDao();
		Collection<Inventory> inventories = inventoryDao.findByCriteria(criteria, psf);
		ArrayList<InventoryOutVO> result = new ArrayList<InventoryOutVO>(inventories.size());
		Iterator<Inventory> inventoriesIt = inventories.iterator();
		while (inventoriesIt.hasNext()) {
			result.add(inventoryDao.toInventoryOutVO(inventoriesIt.next(), maxInstances));
		}
		return result;
	}

	private Collection<MassMailOutVO> searchMassMailHelper(CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		checkCriteriaInput(DBModule.MASS_MAIL_DB, criteria, true, true);
		MassMailDao massMailDao = this.getMassMailDao();
		Collection massMails = massMailDao.findByCriteria(criteria, psf);
		massMailDao.toMassMailOutVOCollection(massMails);
		return massMails;
	}

	private Collection<ProbandOutVO> searchProbandHelper(CriteriaInstantVO criteria, Integer maxInstances, PSFVO psf) throws Exception {
		checkCriteriaInput(DBModule.PROBAND_DB, criteria, true, true);
		ProbandDao probandDao = this.getProbandDao();
		Collection probands = probandDao.findByCriteria(criteria, psf);
		ArrayList<ProbandOutVO> result = new ArrayList<ProbandOutVO>(probands.size());
		Iterator<Proband> probandsIt = probands.iterator();
		while (probandsIt.hasNext()) {
			result.add(probandDao.toProbandOutVO(probandsIt.next(), maxInstances));
		}
		return result;
	}

	private ArrayList<StaffOutVO> searchStaffHelper(CriteriaInstantVO criteria, Integer maxInstances, PSFVO psf) throws Exception {
		checkCriteriaInput(DBModule.STAFF_DB, criteria, true, true);
		StaffDao staffDao = this.getStaffDao();
		Collection<Staff> staff = staffDao.findByCriteria(criteria, psf);
		ArrayList<StaffOutVO> result = new ArrayList<StaffOutVO>(staff.size());
		Iterator<Staff> staffIt = staff.iterator();
		while (staffIt.hasNext()) {
			result.add(staffDao.toStaffOutVO(staffIt.next(), maxInstances));
		}
		return result;
	}

	private Collection<TrialOutVO> searchTrialHelper(CriteriaInstantVO criteria, PSFVO psf) throws Exception {
		checkCriteriaInput(DBModule.TRIAL_DB, criteria, true, true);
		TrialDao trialDao = this.getTrialDao();
		Collection trials = trialDao.findByCriteria(criteria, psf);
		trialDao.toTrialOutVOCollection(trials);
		return trials;
	}

	private ArrayList<UserOutVO> searchUserHelper(CriteriaInstantVO criteria, Integer maxInstances, PSFVO psf) throws Exception {
		checkCriteriaInput(DBModule.USER_DB, criteria, true, true);
		UserDao userDao = this.getUserDao();
		Collection<User> users = userDao.findByCriteria(criteria, psf);
		ArrayList<UserOutVO> result = new ArrayList<UserOutVO>(users.size());
		Iterator<User> userIt = users.iterator();
		while (userIt.hasNext()) {
			result.add(userDao.toUserOutVO(userIt.next(), maxInstances));
		}
		return result;
	}

	public void setCriterionIntermediateSetParser(CriterionIntermediateSetParser criterionIntermediateSetParser) {
		this.criterionIntermediateSetParser = criterionIntermediateSetParser;
	}

	public void setCriterionSyntaxParser(CriterionSyntaxParser criterionSyntaxParser) {
		this.criterionSyntaxParser = criterionSyntaxParser;
	}
}