// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::ctsms::org.phoenixctms.ctsms::service::shared::InputFieldService
 * STEREOTYPE:  Service
 */
package org.phoenixctms.ctsms.service.shared;

import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.hibernate.LockMode;
import org.phoenixctms.ctsms.adapt.InputFieldCollisionFinder;
import org.phoenixctms.ctsms.adapt.InputFieldSelectionSetValueNameCollisionFinder;
import org.phoenixctms.ctsms.adapt.InputFieldSelectionSetValuePresetCollisionFinder;
import org.phoenixctms.ctsms.adapt.InputFieldSelectionSetValueStrokesIdCollisionFinder;
import org.phoenixctms.ctsms.adapt.InputFieldSelectionSetValueValueCollisionFinder;
import org.phoenixctms.ctsms.domain.ECRFDao;
import org.phoenixctms.ctsms.domain.ECRFField;
import org.phoenixctms.ctsms.domain.ECRFFieldDao;
import org.phoenixctms.ctsms.domain.ECRFFieldStatusEntryDao;
import org.phoenixctms.ctsms.domain.ECRFFieldValue;
import org.phoenixctms.ctsms.domain.ECRFFieldValueDao;
import org.phoenixctms.ctsms.domain.ECRFStatusEntryDao;
import org.phoenixctms.ctsms.domain.InputField;
import org.phoenixctms.ctsms.domain.InputFieldDao;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValue;
import org.phoenixctms.ctsms.domain.InputFieldSelectionSetValueDao;
import org.phoenixctms.ctsms.domain.InputFieldValue;
import org.phoenixctms.ctsms.domain.InputFieldValueDao;
import org.phoenixctms.ctsms.domain.Inquiry;
import org.phoenixctms.ctsms.domain.InquiryDao;
import org.phoenixctms.ctsms.domain.InquiryValue;
import org.phoenixctms.ctsms.domain.InquiryValueDao;
import org.phoenixctms.ctsms.domain.JournalEntry;
import org.phoenixctms.ctsms.domain.JournalEntryDao;
import org.phoenixctms.ctsms.domain.MimeType;
import org.phoenixctms.ctsms.domain.NotificationDao;
import org.phoenixctms.ctsms.domain.NotificationRecipientDao;
import org.phoenixctms.ctsms.domain.Proband;
import org.phoenixctms.ctsms.domain.ProbandListEntryTag;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagDao;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValue;
import org.phoenixctms.ctsms.domain.ProbandListEntryTagValueDao;
import org.phoenixctms.ctsms.domain.Trial;
import org.phoenixctms.ctsms.domain.User;
import org.phoenixctms.ctsms.enumeration.FileModule;
import org.phoenixctms.ctsms.enumeration.InputFieldType;
import org.phoenixctms.ctsms.enumeration.JournalModule;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CheckIDUtil;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.util.CoreUtil;
import org.phoenixctms.ctsms.util.DefaultMessages;
import org.phoenixctms.ctsms.util.DefaultSettings;
import org.phoenixctms.ctsms.util.L10nUtil;
import org.phoenixctms.ctsms.util.L10nUtil.Locales;
import org.phoenixctms.ctsms.util.MessageCodes;
import org.phoenixctms.ctsms.util.ServiceExceptionCodes;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.util.SettingCodes;
import org.phoenixctms.ctsms.util.Settings;
import org.phoenixctms.ctsms.util.Settings.Bundle;
import org.phoenixctms.ctsms.util.SystemMessageCodes;
import org.phoenixctms.ctsms.util.date.DateCalc;
import org.phoenixctms.ctsms.vo.AuthenticationVO;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldInVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueInVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldValueVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.PSFVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;

/**
 * @see org.phoenixctms.ctsms.service.shared.InputFieldService
 */
public class InputFieldServiceImpl
extends InputFieldServiceBase
{

	private final static HashMap<InputFieldType, HashSet<InputFieldType>> FIELD_TYPE_TRANSITION_MAP = new HashMap<InputFieldType, HashSet<InputFieldType>>();
	static {
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.SINGLE_LINE_TEXT, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.SINGLE_LINE_TEXT,
				InputFieldType.MULTI_LINE_TEXT,
				InputFieldType.AUTOCOMPLETE)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.MULTI_LINE_TEXT, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.MULTI_LINE_TEXT)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.SELECT_ONE_DROPDOWN, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.SELECT_ONE_DROPDOWN,
				InputFieldType.SELECT_ONE_RADIO_H,
				InputFieldType.SELECT_ONE_RADIO_V,
				InputFieldType.SELECT_MANY_H,
				InputFieldType.SELECT_MANY_V)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.SELECT_ONE_RADIO_H, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.SELECT_ONE_DROPDOWN,
				InputFieldType.SELECT_ONE_RADIO_H,
				InputFieldType.SELECT_ONE_RADIO_V,
				InputFieldType.SELECT_MANY_H,
				InputFieldType.SELECT_MANY_V)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.SELECT_ONE_RADIO_V, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.SELECT_ONE_DROPDOWN,
				InputFieldType.SELECT_ONE_RADIO_H,
				InputFieldType.SELECT_ONE_RADIO_V,
				InputFieldType.SELECT_MANY_H,
				InputFieldType.SELECT_MANY_V)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.AUTOCOMPLETE, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.AUTOCOMPLETE,
				InputFieldType.SINGLE_LINE_TEXT)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.SELECT_MANY_H, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.SELECT_MANY_H,
				InputFieldType.SELECT_MANY_V)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.SELECT_MANY_V, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.SELECT_MANY_H,
				InputFieldType.SELECT_MANY_V)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.CHECKBOX, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.CHECKBOX)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.INTEGER, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.INTEGER)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.FLOAT, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.FLOAT)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.DATE, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.DATE)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.TIME, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.TIME)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.TIMESTAMP, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.TIMESTAMP)));
		FIELD_TYPE_TRANSITION_MAP.put(InputFieldType.SKETCH, new HashSet<InputFieldType>(Arrays.asList(
				InputFieldType.SKETCH,
				InputFieldType.SELECT_MANY_H,
				InputFieldType.SELECT_MANY_V)));
	}

	private static JournalEntry logSystemMessage(Proband proband, InputFieldOutVO inputFieldVO, Timestamp now, User modified, String systemMessageCode, Object result,
			Object original, JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(proband, now, modified, systemMessageCode, new Object[] { CommonUtil.inputFieldOutVOToString(inputFieldVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(Trial trial, InputFieldOutVO inputFieldVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		return journalEntryDao.addSystemMessage(trial, now, modified, systemMessageCode, new Object[] { CommonUtil.inputFieldOutVOToString(inputFieldVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.PROBAND_JOURNAL, null)) });
	}

	private static JournalEntry logSystemMessage(User user, InputFieldOutVO inputFieldVO, Timestamp now, User modified, String systemMessageCode, Object result, Object original,
			JournalEntryDao journalEntryDao) throws Exception {
		if (user == null) {
			return null;
		}
		return journalEntryDao.addSystemMessage(user, now, modified, systemMessageCode, new Object[] { CommonUtil.inputFieldOutVOToString(inputFieldVO) },
				new Object[] { CoreUtil.getSystemMessageCommentContent(result, original, !CommonUtil.getUseJournalEncryption(JournalModule.USER_JOURNAL, null)) });
	}

	private void checkAddInputFieldInput(InputFieldInVO modifiedInputField) throws ServiceException{
		checkInputFieldInput(modifiedInputField);
	}

	private void checkAddSelectionSetValueInput(InputFieldSelectionSetValueInVO selectionSetValueIn) throws ServiceException {
		InputField inputField = CheckIDUtil.checkInputFieldId(selectionSetValueIn.getFieldId(), this.getInputFieldDao());
		// if (inputField.isLocalized()) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_ADD_SELECTION_SET_VALUES_TO_LOCALIZED_INPUT_FIELD);
		// }
		checkSelectionSetValueInput(inputField, selectionSetValueIn);
	}

	private void checkDateLimits(InputFieldInVO inputFieldIn) throws ServiceException {
		Date minDate = inputFieldIn.getMinDate();
		Date maxDate = inputFieldIn.getMaxDate();
		if (minDate != null && maxDate != null && DateCalc.getStartOfDay(minDate).compareTo(DateCalc.getStartOfDay(maxDate)) > 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_MIN_DATE_LIMIT_GREATER_THAN_MAX_DATE_LIMIT);
		}
		checkValidationErrorMsg(minDate != null || maxDate != null, inputFieldIn.getValidationErrorMsg());
	}

	private void checkFloatLimits(InputFieldInVO inputFieldIn) throws ServiceException {
		Float lowerLimit = inputFieldIn.getFloatLowerLimit();
		Float upperLimit = inputFieldIn.getFloatUpperLimit();
		if (lowerLimit != null && upperLimit != null && lowerLimit > upperLimit) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_MIN_FLOAT_LIMIT_GREATER_THAN_MAX_FLOAT_LIMIT);
		}
		checkValidationErrorMsg(lowerLimit != null || upperLimit != null, inputFieldIn.getValidationErrorMsg());
	}

	private void checkInputFieldInput(InputFieldInVO inputFieldIn) throws ServiceException {
		if ((new InputFieldCollisionFinder(this.getInputFieldDao())).collides(inputFieldIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.FIELD_NAME_ALREADY_EXISTS);
		}
		InputFieldType fieldType = inputFieldIn.getFieldType();
		switch (fieldType) {
			case SINGLE_LINE_TEXT:
				checkRegExp(inputFieldIn);
				break;
			case MULTI_LINE_TEXT:
				checkRegExp(inputFieldIn);
				break;
			case SELECT_ONE_DROPDOWN:
			case SELECT_ONE_RADIO_H:
			case SELECT_ONE_RADIO_V:
				checkValidationErrorMsg(false, inputFieldIn.getValidationErrorMsg());
				break;
			case AUTOCOMPLETE:
				if (inputFieldIn.getLearn() && inputFieldIn.getStrict()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_AUTOCOMPLETE_STRICT_AND_LEARN_SET);
				}
				checkValidationErrorMsg(false, inputFieldIn.getValidationErrorMsg());
				break;
			case SELECT_MANY_H:
			case SELECT_MANY_V:
				checkSelectionLimits(inputFieldIn);
				break;
			case CHECKBOX:
				checkValidationErrorMsg(false, inputFieldIn.getValidationErrorMsg());
				break;
			case INTEGER:
				checkIntegerLimits(inputFieldIn);
				break;
			case FLOAT:
				checkFloatLimits(inputFieldIn);
				break;
			case DATE:
				checkDateLimits(inputFieldIn);
				break;
			case TIME:
				checkTimeLimits(inputFieldIn);
				break;
			case TIMESTAMP:
				checkTimestampLimits(inputFieldIn);
				break;
			case SKETCH:
				checkSelectionLimits(inputFieldIn);
				checkSketchParameters(inputFieldIn);
				break;
			default:
				throw new IllegalArgumentException(L10nUtil.getMessage(MessageCodes.UNSUPPORTED_INPUT_FIELD_TYPE, DefaultMessages.UNSUPPORTED_INPUT_FIELD_TYPE,
						new Object[] { fieldType.toString() }));
		}
		if (!(InputFieldType.SINGLE_LINE_TEXT.equals(fieldType) || InputFieldType.MULTI_LINE_TEXT.equals(fieldType)) && (
				inputFieldIn.getRegExp() != null ||
				inputFieldIn.getTextPreset() != null)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_REGEXP_OR_TEXT_PRESET_NOT_NULL);
		}
		if (!(InputFieldType.SELECT_MANY_H.equals(fieldType) || InputFieldType.SELECT_MANY_V.equals(fieldType) || InputFieldType.SKETCH.equals(fieldType)) && (
				inputFieldIn.getMinSelections() != null ||
				inputFieldIn.getMaxSelections() != null)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_SELECTION_LIMITS_NOT_NULL);
		}
		if (!(InputFieldType.AUTOCOMPLETE.equals(fieldType)) && (
				inputFieldIn.getLearn() == true ||
				inputFieldIn.getStrict() == true)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_AUTOCOMPLETE_STRICT_LEARN_NOT_FALSE);
		}
		if (!(InputFieldType.CHECKBOX.equals(fieldType)) && (
				inputFieldIn.getBooleanPreset() == true)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_CHECKBOX_PRESET_NOT_FALSE);
		}
		if (!(InputFieldType.INTEGER.equals(fieldType)) && (
				inputFieldIn.getLongLowerLimit() != null ||
				inputFieldIn.getLongUpperLimit() != null ||
				inputFieldIn.getLongPreset() != null)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_INTEGER_LIMITS_OR_PRESET_NOT_NULL);
		}
		if (!(InputFieldType.FLOAT.equals(fieldType)) && (
				inputFieldIn.getFloatLowerLimit() != null ||
				inputFieldIn.getFloatUpperLimit() != null ||
				inputFieldIn.getFloatPreset() != null)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_FLOAT_LIMITS_OR_PRESET_NOT_NULL); // "float limits and preset must not be set");
		}
		if (!(InputFieldType.DATE.equals(fieldType)) && (
				inputFieldIn.getMinDate() != null ||
				inputFieldIn.getMaxDate() != null ||
				inputFieldIn.getDatePreset() != null)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_DATE_LIMITS_OR_PRESET_NOT_NULL);
		}
		if (!(InputFieldType.TIME.equals(fieldType)) && (
				inputFieldIn.getMinTime() != null ||
				inputFieldIn.getMaxTime() != null ||
				inputFieldIn.getTimePreset() != null)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_TIME_LIMITS_OR_PRESET_NOT_NULL);
		}
		if (!(InputFieldType.TIMESTAMP.equals(fieldType)) && (
				inputFieldIn.getMinTimestamp() != null ||
				inputFieldIn.getMaxTimestamp() != null ||
				inputFieldIn.getTimestampPreset() != null)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_TIMESTAMP_LIMITS_OR_PRESET_NOT_NULL);
		}
		if (!(InputFieldType.SKETCH.equals(fieldType)) && (
				inputFieldIn.getWidth() != null ||
				inputFieldIn.getHeight() != null ||
				inputFieldIn.getFileName() != null ||
				inputFieldIn.getDatas() != null)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_SKETCH_PARAMETERS_NOT_NULL);
		}
		// preset values remain unchecked for now...
	}

	private void checkInputFieldInputFieldTypeChanged(Long inputFieldId, InputFieldType oldFieldType, InputFieldType newFieldType) throws ServiceException {
		if (!oldFieldType.equals(newFieldType)) {
			if (this.getInquiryValueDao().getCountByField(inputFieldId) > 0
					|| this.getProbandListEntryTagValueDao().getCountByField(inputFieldId) > 0
					|| this.getECRFFieldValueDao().getCountByField(inputFieldId) > 0) {
				if (FIELD_TYPE_TRANSITION_MAP.containsKey(oldFieldType)) {
					if (!FIELD_TYPE_TRANSITION_MAP.get(oldFieldType).contains(newFieldType)) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_TYPE_CHANGED,
								L10nUtil.getInputFieldTypeName(Locales.USER, oldFieldType.name()),
								L10nUtil.getInputFieldTypeName(Locales.USER, newFieldType.name()));
					}
				}
			} else {
				switch (newFieldType) {
					case AUTOCOMPLETE:
					case SELECT_ONE_DROPDOWN:
					case SELECT_ONE_RADIO_H:
					case SELECT_ONE_RADIO_V:
						if (this.getInputFieldSelectionSetValueDao().getCount(inputFieldId, true) > 1) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_MULTIPLE_PRESET_VALUES);
						}
						// no break!
					case SELECT_MANY_H:
					case SELECT_MANY_V:
						if (InputFieldType.SKETCH.equals(oldFieldType) && this.getInputFieldSelectionSetValueDao().getCount(inputFieldId) > 0) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUES_NOT_FOR_SELECT);
						}
						break;
					case SKETCH:
						if (this.getInputFieldSelectionSetValueDao().getCount(inputFieldId) > 0) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUES_NOT_FOR_SKETCH);
						}
						break;
					default:
						if (this.getInputFieldSelectionSetValueDao().getCount(inputFieldId) > 0) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUES_ALLOWED_FOR_SELECTION_INPUTS_ONLY);
						}
						break;
				}
			}
		}
	}

	private void checkIntegerLimits(InputFieldInVO inputFieldIn) throws ServiceException {
		Long lowerLimit = inputFieldIn.getLongLowerLimit();
		Long upperLimit = inputFieldIn.getLongUpperLimit();
		if (lowerLimit != null && upperLimit != null && lowerLimit > upperLimit) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_MIN_INTEGER_LIMIT_GREATER_THAN_MAX_INTEGER_LIMIT);
		}
		checkValidationErrorMsg(lowerLimit != null || upperLimit != null, inputFieldIn.getValidationErrorMsg());
	}

	private void checkRegExp(InputFieldInVO inputFieldIn) throws ServiceException {
		String regExp = inputFieldIn.getRegExp();
		if (regExp != null && regExp.length() > 0) {
			try {
				Pattern.compile(regExp);
			} catch (PatternSyntaxException e) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_INVALID_REGEXP_PATTERN, inputFieldIn.getName(), e.getMessage());
			}
			checkValidationErrorMsg(true, inputFieldIn.getValidationErrorMsg());
		} else {
			checkValidationErrorMsg(false, inputFieldIn.getValidationErrorMsg());
		}
	}

	private void checkSelectionLimits(InputFieldInVO inputFieldIn) throws ServiceException {
		Integer minSelections = inputFieldIn.getMinSelections();
		Integer maxSelections = inputFieldIn.getMaxSelections();
		if (minSelections != null && maxSelections != null && minSelections > maxSelections) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_MIN_SELECTION_LIMIT_GREATER_THAN_MAX_SELECTION_LIMIT);
		}
		checkValidationErrorMsg(minSelections != null || maxSelections != null, inputFieldIn.getValidationErrorMsg());
	}

	private void checkSelectionSetValueInput(InputField inputField, InputFieldSelectionSetValueInVO selectionSetValueIn) throws ServiceException {
		if ((new InputFieldSelectionSetValueNameCollisionFinder(this.getInputFieldDao(), this.getInputFieldSelectionSetValueDao())).collides(selectionSetValueIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_NAME_ALREADY_EXISTS);
		}
		String value = selectionSetValueIn.getValue();
		if (value != null) {// && value.length() > 0) {
			if ((new InputFieldSelectionSetValueValueCollisionFinder(this.getInputFieldDao(), this.getInputFieldSelectionSetValueDao())).collides(selectionSetValueIn)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_VALUE_ALREADY_EXISTS);
			}
		} else {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_VALUE_REQUIRED);
		}
		switch (inputField.getFieldType()) {
			case AUTOCOMPLETE:
			case SELECT_ONE_DROPDOWN:
			case SELECT_ONE_RADIO_H:
			case SELECT_ONE_RADIO_V:
				if (selectionSetValueIn.getPreset()) {
					if ((new InputFieldSelectionSetValuePresetCollisionFinder(this.getInputFieldDao(), this.getInputFieldSelectionSetValueDao())).collides(selectionSetValueIn)) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_MULTIPLE_PRESET_VALUES);
					}
				}
				// no break!
			case SELECT_MANY_H:
			case SELECT_MANY_V:
				if (selectionSetValueIn.getInkRegions() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_INK_REGION_NOT_NULL);
				}
				if (selectionSetValueIn.getStrokesId() != null) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_STROKES_ID_NOT_NULL);
				}
				break;
			case SKETCH:
				if (selectionSetValueIn.getPreset()) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_PRESET_NOT_FALSE);
				}
				String strokesId = selectionSetValueIn.getStrokesId();
				if (strokesId != null && strokesId.length() > 0) {
					if ((new InputFieldSelectionSetValueStrokesIdCollisionFinder(this.getInputFieldDao(), this.getInputFieldSelectionSetValueDao())).collides(selectionSetValueIn)) {
						throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_MULTIPLE_STROKES_IDS);
					}
				} else {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_STROKES_ID_REQUIRED);
				}
				byte[] inkRegions = selectionSetValueIn.getInkRegions();
				if (inkRegions == null || inkRegions.length == 0) {
					throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_INK_REGION_REQUIRED);
				}
				break;
			default:
				throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUES_ALLOWED_FOR_SELECTION_INPUTS_ONLY);
		}
	}

	private void checkSketchParameters(InputFieldInVO inputFieldIn) throws ServiceException {
		if (inputFieldIn.getDatas() == null || inputFieldIn.getDatas().length == 0) {
			if (inputFieldIn.getWidth() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_SKETCH_WIDTH_REQUIRED);
			}
			if (inputFieldIn.getHeight() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_SKETCH_HEIGHT_REQUIRED);
			}
		} else {
			Integer inputFieldInputSizeLimit = Settings.getIntNullable(SettingCodes.INPUT_FIELD_IMAGE_SIZE_LIMIT, Bundle.SETTINGS, DefaultSettings.INPUT_FIELD_IMAGE_SIZE_LIMIT);
			if (inputFieldInputSizeLimit != null && inputFieldIn.getDatas().length > inputFieldInputSizeLimit) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_IMAGE_SIZE_LIMIT_EXCEEDED, CommonUtil.humanReadableByteCount(inputFieldInputSizeLimit));
			}
			if (inputFieldIn.getMimeType() == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_IMAGE_MIME_TYPE_REQUIRED);
			}
			Iterator<MimeType> it = this.getMimeTypeDao().findByMimeTypeModule(inputFieldIn.getMimeType(), FileModule.INPUT_FIELD_IMAGE).iterator();
			if (!it.hasNext()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_IMAGE_MIME_TYPE_UNKNOWN, inputFieldIn.getMimeType());
			}
			if (!it.next().isImage()) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_IMAGE_MIME_TYPE_NO_IMAGE, inputFieldIn.getMimeType());
			}
			Dimension imageDimension = CoreUtil.getImageDimension(inputFieldIn.getDatas());
			if (imageDimension == null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_IMAGE_CANNOT_READ_DIMENSIONS);
			}
		}
		if (inputFieldIn.getWidth() != null && inputFieldIn.getWidth() <= 0l) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_SKETCH_WIDTH_LESS_THAN_OR_EQUAL_TO_ZERO);
		}
		if (inputFieldIn.getHeight() != null && inputFieldIn.getHeight() <= 0l) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_SKETCH_HEIGHT_LESS_THAN_OR_EQUAL_TO_ZERO);
		}
	}

	private void checkTimeLimits(InputFieldInVO inputFieldIn) throws ServiceException {
		Date minTime = inputFieldIn.getMinTime();
		Date maxTime = inputFieldIn.getMaxTime();
		if (minTime != null && maxTime != null && minTime.compareTo(maxTime) > 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_MIN_TIME_LIMIT_GREATER_THAN_MAX_TIME_LIMIT);
		}
		checkValidationErrorMsg(minTime != null || maxTime != null, inputFieldIn.getValidationErrorMsg());
	}

	private void checkTimestampLimits(InputFieldInVO inputFieldIn) throws ServiceException {
		Date minTimestamp = inputFieldIn.getMinTimestamp();
		Date maxTimestamp = inputFieldIn.getMaxTimestamp();
		if (minTimestamp != null && maxTimestamp != null && minTimestamp.compareTo(maxTimestamp) > 0) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_MIN_TIMESTAMP_LIMIT_GREATER_THAN_MAX_TIMESTAMP_LIMIT);
		}
		checkValidationErrorMsg(minTimestamp != null || maxTimestamp != null, inputFieldIn.getValidationErrorMsg());
	}

	private void checkUpdateInputFieldInput(InputFieldInVO modifiedInputField, InputField originalInputField) throws ServiceException{
		checkInputFieldInput(modifiedInputField);
		// if (originalInputField.isLocalized()) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_UPDATE_LOCALIZED_INPUT_FIELD);
		// }
		checkInputFieldInputFieldTypeChanged(modifiedInputField.getId(), originalInputField.getFieldType(), modifiedInputField.getFieldType());
		if (!originalInputField.getNameL10nKey().equals(modifiedInputField.getName())
				|| !originalInputField.getTitleL10nKey().equals(modifiedInputField.getTitle())) {
			boolean checkProbandTrialLocked = Settings.getBoolean(SettingCodes.UPDATE_INPUT_FIELD_CHECK_PROBAND_TRIAL_LOCKED, Bundle.SETTINGS,
					DefaultSettings.UPDATE_INPUT_FIELD_CHECK_PROBAND_TRIAL_LOCKED);
			if (checkProbandTrialLocked) {
				Iterator<Inquiry> inquiriesIt = originalInputField.getInquiries().iterator();
				while (inquiriesIt.hasNext()) {
					Inquiry inquiry = inquiriesIt.next();
					ServiceUtil.checkTrialLocked(inquiry.getTrial());
					Iterator<InquiryValue> inquiryValuesIt = inquiry.getInquiryValues().iterator();
					while (inquiryValuesIt.hasNext()) {
						InquiryValue inquiryValue = inquiryValuesIt.next();
						ServiceUtil.checkProbandLocked(inquiryValue.getProband());
					}
				}
				Iterator<ProbandListEntryTag> listEntryTagsIt = originalInputField.getListEntryTags().iterator();
				while (listEntryTagsIt.hasNext()) {
					ProbandListEntryTag listEntryTag = listEntryTagsIt.next();
					ServiceUtil.checkTrialLocked(listEntryTag.getTrial());
					Iterator<ProbandListEntryTagValue> tagValuesIt = listEntryTag.getTagValues().iterator();
					while (tagValuesIt.hasNext()) {
						ProbandListEntryTagValue tagValue = tagValuesIt.next();
						ServiceUtil.checkProbandLocked(tagValue.getListEntry().getProband());
					}
				}
			}
			Iterator<ECRFField> ecrfFieldsIt = originalInputField.getEcrfFields().iterator();
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			ECRFDao ecrfDao = this.getECRFDao();
			while (ecrfFieldsIt.hasNext()) {
				ECRFField ecrfField = ecrfFieldsIt.next();
				if (checkProbandTrialLocked) {
					ServiceUtil.checkTrialLocked(ecrfField.getTrial());
					Iterator<ECRFFieldValue> fieldValuesIt = ecrfField.getFieldValues().iterator();
					while (fieldValuesIt.hasNext()) {
						ECRFFieldValue fieldValue = fieldValuesIt.next();
						ServiceUtil.checkProbandLocked(fieldValue.getListEntry().getProband());
					}
				}
				ServiceUtil.checkLockedEcrfs(ecrfField.getEcrf(), ecrfStatusEntryDao, ecrfDao);
				// long valuesLockedEcrfCount = this.getECRFStatusEntryDao().getCount(null, ecrfField.getEcrf().getId(), null, true, null, null, null); // row lock order
				// if (valuesLockedEcrfCount > 0) {
				// throw L10nUtil.initServiceException(ServiceExceptionCodes.LOCKED_ECRFS, this.getECRFDao().toECRFOutVO(ecrfField.getEcrf()).getUniqueName(), valuesLockedEcrfCount);
				// }
			}
		}
	}

	private void checkUpdateSelectionSetValueInput(InputFieldSelectionSetValue originalSelectionSetValue, InputFieldSelectionSetValueInVO selectionSetValueIn,
			boolean checkProbandTrialLocked)
					throws ServiceException {
		InputField inputField = CheckIDUtil.checkInputFieldId(selectionSetValueIn.getFieldId(), this.getInputFieldDao());
		if (!inputField.equals(originalSelectionSetValue.getField())) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_INPUT_FIELD_CHANGED);
		}
		// if (originalSelectionSetValue.isLocalized()) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_UPDATE_LOCALIZED_SELECTION_SET_VALUE);
		// }
		checkSelectionSetValueInput(inputField, selectionSetValueIn);
		// InputField inputField = originalSelectionSetValue.getField();
		if (!originalSelectionSetValue.getValue().equals(selectionSetValueIn.getValue())) {
			if (checkProbandTrialLocked) {
				Iterator<Inquiry> inquiriesIt = inputField.getInquiries().iterator();
				while (inquiriesIt.hasNext()) {
					Inquiry inquiry = inquiriesIt.next();
					Iterator<InquiryValue> inquiryValuesIt = inquiry.getInquiryValues().iterator();
					while (inquiryValuesIt.hasNext()) {
						InquiryValue inquiryValue = inquiryValuesIt.next();
						InputFieldValue value = inquiryValue.getValue();
						if (value.getSelectionValues().contains(originalSelectionSetValue)) {
							ServiceUtil.checkTrialLocked(inquiryValue.getInquiry().getTrial());
							ServiceUtil.checkProbandLocked(inquiryValue.getProband());
						}
					}
				}
				Iterator<ProbandListEntryTag> listEntryTagsIt = inputField.getListEntryTags().iterator();
				while (listEntryTagsIt.hasNext()) {
					ProbandListEntryTag listEntryTag = listEntryTagsIt.next();
					Iterator<ProbandListEntryTagValue> tagValuesIt = listEntryTag.getTagValues().iterator();
					while (tagValuesIt.hasNext()) {
						ProbandListEntryTagValue tagValue = tagValuesIt.next();
						InputFieldValue value = tagValue.getValue();
						if (value.getSelectionValues().contains(originalSelectionSetValue)) {
							ServiceUtil.checkTrialLocked(tagValue.getListEntry().getTrial());
							ServiceUtil.checkProbandLocked(tagValue.getListEntry().getProband());
						}
					}
				}
			}
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			ECRFDao ecrfDao = this.getECRFDao();
			Iterator<ECRFField> ecrfFieldsIt = inputField.getEcrfFields().iterator();
			while (ecrfFieldsIt.hasNext()) {
				ECRFField ecrfField = ecrfFieldsIt.next();
				Iterator<ECRFFieldValue> fieldValuesIt = ecrfField.getFieldValues().iterator();
				while (fieldValuesIt.hasNext()) {
					ECRFFieldValue fieldValue = fieldValuesIt.next();
					InputFieldValue value = fieldValue.getValue();
					if (value.getSelectionValues().contains(originalSelectionSetValue)) {
						if (checkProbandTrialLocked) {
							ServiceUtil.checkTrialLocked(fieldValue.getListEntry().getTrial());
							ServiceUtil.checkProbandLocked(fieldValue.getListEntry().getProband());
						}
						ServiceUtil.checkLockedEcrfs(ecrfField.getEcrf(), ecrfStatusEntryDao, ecrfDao);
						// long valuesLockedEcrfCount = this.getECRFStatusEntryDao().getCount(null, ecrfField.getEcrf().getId(), null, true, null, null, null); // row lock order
						// if (valuesLockedEcrfCount > 0) {
						// throw L10nUtil.initServiceException(ServiceExceptionCodes.LOCKED_ECRFS, this.getECRFDao().toECRFOutVO(ecrfField.getEcrf()).getUniqueName(),
						// valuesLockedEcrfCount);
						// }
					}
				}
			}
		}
	}

	private void checkValidationErrorMsg(boolean required, String validationErrorMsg) throws ServiceException {
		if (required) {
			if (CommonUtil.isEmptyString(validationErrorMsg)) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_VALIDATION_ERROR_MESSAGE_REQUIRED);
			}
		} else {
			if (validationErrorMsg != null) {
				throw L10nUtil.initServiceException(ServiceExceptionCodes.INPUT_FIELD_VALIDATION_ERROR_MESSAGE_NOT_NULL);
			}
		}
	}

	private InputFieldSelectionSetValueOutVO deleteSelectionSetValueHelper(InputField inputField, Long selectionSetValueId, boolean deleteCascade, boolean checkProbandTrialLocked,
			Timestamp now, User user) throws Exception {
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		InputFieldSelectionSetValue selectionSetValue = CheckIDUtil.checkInputFieldSelectionSetValueId(selectionSetValueId, selectionSetValueDao);
		// if (selectionSetValue.isLocalized()) {
		// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_LOCALIZED_SELECTION_SET_VALUE);
		// }
		InputFieldSelectionSetValueOutVO result = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(selectionSetValue);
		if (inputField == null) {
			inputField = selectionSetValue.getField();
		}
		if (deleteCascade) {
			InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
			Iterator<Inquiry> inquiriesIt = inputField.getInquiries().iterator();
			while (inquiriesIt.hasNext()) {
				Inquiry inquiry = inquiriesIt.next();
				boolean removed = false;
				Iterator<InquiryValue> inquiryValuesIt = inquiry.getInquiryValues().iterator();
				while (inquiryValuesIt.hasNext()) {
					InquiryValue inquiryValue = inquiryValuesIt.next();
					InputFieldValue value = inquiryValue.getValue();
					if (value.removeSelectionValues(selectionSetValue)) {
						if (checkProbandTrialLocked) {
							ServiceUtil.checkTrialLocked(inquiryValue.getInquiry().getTrial());
							ServiceUtil.checkProbandLocked(inquiryValue.getProband());
						}
						logSystemMessage(inquiryValue.getProband(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_DELETED, result, null, journalEntryDao);
						CoreUtil.modifyVersion(inquiryValue, inquiryValue.getVersion(), now, user);
						inquiryValueDao.update(inquiryValue);
						removed = true;
					}
				}
				if (removed) {
					logSystemMessage(inquiry.getTrial(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_DELETED, result, null, journalEntryDao);
				}
			}
			ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
			Iterator<ProbandListEntryTag> listEntryTagsIt = inputField.getListEntryTags().iterator();
			while (listEntryTagsIt.hasNext()) {
				ProbandListEntryTag listEntryTag = listEntryTagsIt.next();
				boolean removed = false;
				Iterator<ProbandListEntryTagValue> tagValuesIt = listEntryTag.getTagValues().iterator();
				while (tagValuesIt.hasNext()) {
					ProbandListEntryTagValue tagValue = tagValuesIt.next();
					InputFieldValue value = tagValue.getValue();
					if (value.removeSelectionValues(selectionSetValue)) {
						if (checkProbandTrialLocked) {
							ServiceUtil.checkTrialLocked(tagValue.getListEntry().getTrial());
							ServiceUtil.checkProbandLocked(tagValue.getListEntry().getProband());
						}
						logSystemMessage(tagValue.getListEntry().getProband(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_DELETED, result, null,
								journalEntryDao);
						CoreUtil.modifyVersion(tagValue, tagValue.getVersion(), now, user);
						probandListEntryTagValueDao.update(tagValue);
						removed = true;
					}
				}
				if (removed) {
					logSystemMessage(listEntryTag.getTrial(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_DELETED, result, null, journalEntryDao);
				}
			}
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			ECRFDao ecrfDao = this.getECRFDao();
			Iterator<ECRFField> ecrfFieldsIt = inputField.getEcrfFields().iterator();
			while (ecrfFieldsIt.hasNext()) {
				ECRFField ecrfField = ecrfFieldsIt.next();
				boolean removed = false;
				Iterator<ECRFFieldValue> fieldValuesIt = ecrfField.getFieldValues().iterator();
				while (fieldValuesIt.hasNext()) {
					ECRFFieldValue fieldValue = fieldValuesIt.next();
					InputFieldValue value = fieldValue.getValue();
					if (value.removeSelectionValues(selectionSetValue)) {
						if (checkProbandTrialLocked) {
							ServiceUtil.checkTrialLocked(fieldValue.getListEntry().getTrial());
							ServiceUtil.checkProbandLocked(fieldValue.getListEntry().getProband());
						}
						// long valuesLockedEcrfCount = this.getECRFStatusEntryDao().getCount(null, ecrfField.getEcrf().getId(), null, true, null, null, null); // row lock order
						// if (valuesLockedEcrfCount > 0) {
						// throw L10nUtil.initServiceException(ServiceExceptionCodes.LOCKED_ECRFS, this.getECRFDao().toECRFOutVO(ecrfField.getEcrf()).getUniqueName(),
						// valuesLockedEcrfCount);
						// }
						ServiceUtil.checkLockedEcrfs(ecrfField.getEcrf(), ecrfStatusEntryDao, ecrfDao);
						logSystemMessage(fieldValue.getListEntry().getProband(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_DELETED, result, null,
								journalEntryDao);
						CoreUtil.modifyVersion(fieldValue, fieldValue.getVersion(), now, user);
						ecrfFieldValueDao.update(fieldValue);
						removed = true;
					}
				}
				if (removed) {
					logSystemMessage(ecrfField.getTrial(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_DELETED, result, null, journalEntryDao);
				}
			}
		}
		inputField.removeSelectionSetValues(selectionSetValue);
		selectionSetValue.setField(null);
		selectionSetValueDao.remove(selectionSetValue);
		ServiceUtil.logSystemMessage(inputField, result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_DELETED, result, null, journalEntryDao);
		return result;
	}

	@Override
	protected InputFieldOutVO handleAddInputField(AuthenticationVO auth, InputFieldInVO newInputField)
			throws Exception
			{
		checkAddInputFieldInput(newInputField);
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = inputFieldDao.inputFieldInVOToEntity(newInputField);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(inputField, now, user);
		inputField = inputFieldDao.create(inputField);// 2011-09-12//
		InputFieldOutVO result = inputFieldDao.toInputFieldOutVO(inputField);
		ServiceUtil.logSystemMessage(inputField, result, now, user, SystemMessageCodes.INPUT_FIELD_CREATED, result, null, this.getJournalEntryDao());
		return result;
			}

	@Override
	protected InputFieldSelectionSetValueOutVO handleAddSelectionSetValue(
			AuthenticationVO auth, InputFieldSelectionSetValueInVO newSelectionSetValue)
					throws Exception {
		checkAddSelectionSetValueInput(newSelectionSetValue);
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		InputFieldSelectionSetValue selectionSetValue = selectionSetValueDao.inputFieldSelectionSetValueInVOToEntity(newSelectionSetValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(selectionSetValue, now, user);
		selectionSetValue = selectionSetValueDao.create(selectionSetValue);
		InputFieldSelectionSetValueOutVO result = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(selectionSetValue);
		ServiceUtil.logSystemMessage(selectionSetValue.getField(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_CREATED, result, null,
				this.getJournalEntryDao());
		return result;
	}

	@Override
	protected InputFieldOutVO handleAddUpdateInputField(AuthenticationVO auth, InputFieldInVO modifiedInputField, Set<InputFieldSelectionSetValueInVO> modifiedSelectionSetValues)
			throws Exception {

		InputFieldDao inputFieldDao = this.getInputFieldDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		HashSet<Long> originalSelectionSetValueIds;
		InputFieldOutVO original;
		InputField inputField;
		boolean update;
		if (modifiedInputField.getId() != null) {
			InputField originalInputField = CheckIDUtil.checkInputFieldId(modifiedInputField.getId(), inputFieldDao, LockMode.PESSIMISTIC_WRITE);
			checkUpdateInputFieldInput(modifiedInputField,originalInputField);
			original = inputFieldDao.toInputFieldOutVO(originalInputField);
			Collection<InputFieldSelectionSetValue> originalSelectionSetValues = originalInputField.getSelectionSetValues();
			originalSelectionSetValueIds = new HashSet<Long>(originalSelectionSetValues.size());
			Iterator<InputFieldSelectionSetValue> it = originalSelectionSetValues.iterator();
			while (it.hasNext()) {
				originalSelectionSetValueIds.add(it.next().getId());
			}
			inputFieldDao.evict(originalInputField);
			inputField = inputFieldDao.inputFieldInVOToEntity(modifiedInputField);
			CoreUtil.modifyVersion(originalInputField, inputField, now, user);
			inputFieldDao.update(inputField);
			// Hibernate.initialize(inputField);
			update = true;
		} else {
			checkAddInputFieldInput(modifiedInputField);
			originalSelectionSetValueIds = new HashSet<Long>();
			original = null;
			inputField = inputFieldDao.inputFieldInVOToEntity(modifiedInputField);
			CoreUtil.modifyVersion(inputField, now, user);
			// inputField = inputFieldDao.create(inputField);
			inputField = inputFieldDao.create(inputField, LockMode.PESSIMISTIC_WRITE); // required here, as load(..,lockMode) will not work with created entities
			// inputFieldDao.evict(inputField);
			// inputField = ServiceUtil.checkInputFieldId(inputField.getId(), inputFieldDao, LockMode.PESSIMISTIC_WRITE);
			// inputFieldDao.lock(inputField, LockMode.PESSIMISTIC_WRITE);
			update = false;
		}
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Iterator<Long> originalSelectionSetValueIdsIt;

		if (modifiedSelectionSetValues != null && modifiedSelectionSetValues.size() > 0) {
			HashSet<Long> selectionSetValueIdDupeCheck = new HashSet<Long>(modifiedSelectionSetValues.size());
			Iterator<InputFieldSelectionSetValueInVO> it = modifiedSelectionSetValues.iterator();
			ArrayList<InputFieldSelectionSetValueInVO> selectionSetValueIns = new ArrayList<InputFieldSelectionSetValueInVO>(modifiedSelectionSetValues.size());
			while (it.hasNext()) {
				InputFieldSelectionSetValueInVO modifiedSelectionSetValue = it.next();
				if (modifiedSelectionSetValue != null) {
					if (modifiedSelectionSetValue.getId() != null) {
						if (!update) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.ENTITY_ID_NOT_NULL, modifiedSelectionSetValue.getId().toString());
						}
						if (!selectionSetValueIdDupeCheck.add(modifiedSelectionSetValue.getId())) {
							throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_DUPLICATE_ID, modifiedSelectionSetValue.getId().toString());
						}
						originalSelectionSetValueIds.remove(modifiedSelectionSetValue.getId());
						selectionSetValueIns.add(modifiedSelectionSetValue);
					} else {
						if (update) {
							InputField selectionSetValueInputField = CheckIDUtil.checkInputFieldId(modifiedSelectionSetValue.getFieldId(), inputFieldDao);
							if (!inputField.equals(selectionSetValueInputField)) {
								throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_FOR_WRONG_INPUT_FIELD,
										CommonUtil.inputFieldOutVOToString(inputFieldDao.toInputFieldOutVO(selectionSetValueInputField)));
							}
							selectionSetValueIns.add(modifiedSelectionSetValue);
						} else {
							if (modifiedSelectionSetValue.getFieldId() != null) {
								throw L10nUtil.initServiceException(ServiceExceptionCodes.SELECTION_SET_VALUE_FIELD_ID_NOT_NULL, modifiedSelectionSetValue.getFieldId().toString());
							}
							InputFieldSelectionSetValueInVO newSelectionSetValue = new InputFieldSelectionSetValueInVO(modifiedSelectionSetValue);
							newSelectionSetValue.setFieldId(inputField.getId());
							selectionSetValueIns.add(newSelectionSetValue);
						}
					}
				}
			}

			originalSelectionSetValueIdsIt = originalSelectionSetValueIds.iterator();
			while (originalSelectionSetValueIdsIt.hasNext()) {
				deleteSelectionSetValueHelper(inputField, originalSelectionSetValueIdsIt.next(), true, false, now, user);
			}
			it = selectionSetValueIns.iterator();
			while (it.hasNext()) {

				InputFieldSelectionSetValueInVO modifiedSelectionSetValue = it.next();

				InputFieldSelectionSetValue selectionSetValue;
				InputFieldSelectionSetValueOutVO selectionSetValueVO;

				if (modifiedSelectionSetValue.getId() != null) {

					InputFieldSelectionSetValue originalSelectionSetValue = CheckIDUtil.checkInputFieldSelectionSetValueId(modifiedSelectionSetValue.getId(),
							selectionSetValueDao);
					checkUpdateSelectionSetValueInput(originalSelectionSetValue, modifiedSelectionSetValue,false);

					InputFieldSelectionSetValueOutVO originalSelectionSetValueVO = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(originalSelectionSetValue);
					selectionSetValueDao.evict(originalSelectionSetValue);
					selectionSetValue = selectionSetValueDao.inputFieldSelectionSetValueInVOToEntity(modifiedSelectionSetValue);
					CoreUtil.modifyVersion(originalSelectionSetValue, selectionSetValue, now, user);
					selectionSetValueDao.update(selectionSetValue);
					selectionSetValueVO = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(selectionSetValue);
					if (ServiceUtil.LOG_ADD_UPDATE_INPUT_FIELD_NO_DIFF
							|| !InputFieldSelectionSetValueOutVO.equalsExcluding(originalSelectionSetValueVO, selectionSetValueVO, CoreUtil.VO_VERSION_EQUALS_EXCLUDES, true, true)) {
						ServiceUtil.logSystemMessage(selectionSetValue.getField(), selectionSetValueVO.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_UPDATED,
								selectionSetValueVO, originalSelectionSetValueVO,
								journalEntryDao);
					}
				} else {

					checkAddSelectionSetValueInput(modifiedSelectionSetValue);
					selectionSetValue = selectionSetValueDao.inputFieldSelectionSetValueInVOToEntity(modifiedSelectionSetValue);

					CoreUtil.modifyVersion(selectionSetValue, now, user);
					selectionSetValue = selectionSetValueDao.create(selectionSetValue);
					selectionSetValueVO = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(selectionSetValue);
					ServiceUtil.logSystemMessage(selectionSetValue.getField(), selectionSetValueVO.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_CREATED, selectionSetValueVO, null,
							journalEntryDao);

				}

			}
		} else {
			if (!InputFieldType.AUTOCOMPLETE.equals(inputField.getFieldType()) || !inputField.getLearn()) { // dont't drop learned values
				originalSelectionSetValueIdsIt = originalSelectionSetValueIds.iterator();
				while (originalSelectionSetValueIdsIt.hasNext()) {
					deleteSelectionSetValueHelper(inputField, originalSelectionSetValueIdsIt.next(), true, false, now, user);
				}
			}
		}

		InputFieldOutVO result = inputFieldDao.toInputFieldOutVO(inputField);
		if (!update || ServiceUtil.LOG_ADD_UPDATE_INPUT_FIELD_NO_DIFF || !InputFieldOutVO.equalsExcluding(original, result, CoreUtil.VO_VERSION_EQUALS_EXCLUDES, true, true)) {
			ServiceUtil.logSystemMessage(inputField, result, now, user, update ? SystemMessageCodes.INPUT_FIELD_UPDATED : SystemMessageCodes.INPUT_FIELD_CREATED, result,
					original, journalEntryDao);
		}
		return result;

	}

	@Override
	protected void handleCheckInputFieldValue(
			AuthenticationVO auth, InputFieldValueVO dummyInputFieldValue) throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = CheckIDUtil.checkInputFieldId(dummyInputFieldValue.getFieldId(), inputFieldDao);
		// InputFieldOutVO inputFieldVO = this.getInputFieldDao().toInputFieldOutVO(inputField);
		ServiceUtil.checkInputFieldTextValue(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getTextValue(), inputFieldDao,
				this.getInputFieldSelectionSetValueDao());
		ServiceUtil.checkInputFieldBooleanValue(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getBooleanValue(), inputFieldDao);
		ServiceUtil.checkInputFieldLongValue(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getLongValue(), inputFieldDao);
		ServiceUtil.checkInputFieldFloatValue(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getFloatValue(), inputFieldDao);
		ServiceUtil.checkInputFieldDateValue(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getDateValue(), inputFieldDao);
		ServiceUtil.checkInputFieldTimeValue(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getTimeValue(), inputFieldDao);
		ServiceUtil.checkInputFieldTimestampValue(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getTimestampValue(), inputFieldDao);
		ServiceUtil.checkInputFieldInkValue(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getInkValues(), inputFieldDao);
		ServiceUtil.checkInputFieldSelectionSetValues(inputField, dummyInputFieldValue.isOptional(), dummyInputFieldValue.getSelectionValueIds(), inputFieldDao,
				this.getInputFieldSelectionSetValueDao());
		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// User user = CoreUtil.getUser();
	}

	@Override
	protected InputFieldOutVO handleCloneInputField(AuthenticationVO auth, Long fieldId, String newName) throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField originalInputField = CheckIDUtil.checkInputFieldId(fieldId, inputFieldDao, LockMode.PESSIMISTIC_WRITE);
		InputFieldOutVO originalInputFieldVO = inputFieldDao.toInputFieldOutVO(originalInputField);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		InputFieldInVO inputFieldIn = new InputFieldInVO();
		inputFieldIn.setName(newName);
		if ((new InputFieldCollisionFinder(this.getInputFieldDao())).collides(inputFieldIn)) {
			throw L10nUtil.initServiceException(ServiceExceptionCodes.FIELD_NAME_ALREADY_EXISTS);
		}
		InputField newInputField = InputField.Factory.newInstance();
		newInputField.setBooleanPreset(originalInputField.getBooleanPreset());
		newInputField.setCommentL10nKey(originalInputField.getCommentL10nKey());
		newInputField.setDatePreset(originalInputField.getDatePreset());
		newInputField.setFieldType(originalInputField.getFieldType());
		newInputField.setFloatLowerLimit(originalInputField.getFloatLowerLimit());
		newInputField.setFloatPreset(originalInputField.getFloatPreset());
		newInputField.setFloatUpperLimit(originalInputField.getFloatUpperLimit());
		// newInputField.setId(null);
		newInputField.setLongLowerLimit(originalInputField.getLongLowerLimit());
		newInputField.setLongPreset(originalInputField.getLongPreset());
		newInputField.setLongUpperLimit(originalInputField.getLongUpperLimit());
		newInputField.setMaxDate(originalInputField.getMaxDate());
		newInputField.setMaxSelections(originalInputField.getMaxSelections());
		newInputField.setMaxTimestamp(originalInputField.getMaxTimestamp());
		newInputField.setMaxTime(originalInputField.getMaxTime());
		newInputField.setMinDate(originalInputField.getMinDate());
		newInputField.setMinSelections(originalInputField.getMinSelections());
		newInputField.setMinTimestamp(originalInputField.getMinTimestamp());
		newInputField.setMinTime(originalInputField.getMinTime());
		newInputField.setLocalized(originalInputField.isLocalized());
		newInputField.setNameL10nKey(newName);
		newInputField.setExternalId(originalInputField.getExternalId());
		newInputField.setCategory(originalInputField.getCategory());
		newInputField.setRegExp(originalInputField.getRegExp());
		newInputField.setTextPresetL10nKey(originalInputField.getTextPresetL10nKey());
		newInputField.setTimestampPreset(originalInputField.getTimestampPreset());
		newInputField.setTimePreset(originalInputField.getTimePreset());
		newInputField.setTitleL10nKey(originalInputField.getTitleL10nKey());
		newInputField.setValidationErrorMsgL10nKey(originalInputField.getValidationErrorMsgL10nKey());
		// newInputField.setVersion(out.getVersion());
		newInputField.setLearn(originalInputField.getLearn());
		newInputField.setStrict(originalInputField.getStrict());
		newInputField.setWidth(originalInputField.getWidth());
		newInputField.setHeight(originalInputField.getHeight());
		newInputField.setFileName(originalInputField.getFileName());
		newInputField.setContentType(originalInputField.getContentType());
		newInputField.setData(originalInputField.getData());
		newInputField.setFileSize(originalInputField.getFileSize());
		CoreUtil.modifyVersion(newInputField, now, user);
		newInputField = inputFieldDao.create(newInputField);
		InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		// HashMap<Class, HashMap<Long, Object>> voMap = new HashMap<Class, HashMap<Long, Object>>(); // avoid quadratic runtime with journal
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Iterator<InputFieldSelectionSetValue> originalInputFieldSelectionSetValuesIt = originalInputField.getSelectionSetValues().iterator();
		while (originalInputFieldSelectionSetValuesIt.hasNext()) {
			InputFieldSelectionSetValue originalInputFieldSelectionSetValue = originalInputFieldSelectionSetValuesIt.next();
			// InputFieldSelectionSetValueOutVO originalInputFieldSelectionSetValueVO = new InputFieldSelectionSetValueOutVO();
			// inputFieldSelectionSetValueDao.toInputFieldSelectionSetValueOutVO(originalInputFieldSelectionSetValue, originalInputFieldSelectionSetValueVO, voMap);
			InputFieldSelectionSetValueOutVO originalInputFieldSelectionSetValueVO = inputFieldSelectionSetValueDao
					.toInputFieldSelectionSetValueOutVO(originalInputFieldSelectionSetValue);
			InputFieldSelectionSetValue newInputFieldSelectionSetValue = InputFieldSelectionSetValue.Factory.newInstance();
			newInputFieldSelectionSetValue.setInkRegion(originalInputFieldSelectionSetValue.getInkRegion());
			newInputFieldSelectionSetValue.setLocalized(originalInputFieldSelectionSetValue.isLocalized());
			newInputFieldSelectionSetValue.setNameL10nKey(originalInputFieldSelectionSetValue.getNameL10nKey());
			newInputFieldSelectionSetValue.setPreset(originalInputFieldSelectionSetValue.isPreset());
			newInputFieldSelectionSetValue.setStrokesId(originalInputFieldSelectionSetValue.getStrokesId());
			newInputFieldSelectionSetValue.setValue(originalInputFieldSelectionSetValue.getValue());
			newInputFieldSelectionSetValue.setField(newInputField);
			newInputField.addSelectionSetValues(newInputFieldSelectionSetValue);
			CoreUtil.modifyVersion(newInputFieldSelectionSetValue, now, user);
			newInputFieldSelectionSetValue = inputFieldSelectionSetValueDao.create(newInputFieldSelectionSetValue);
			// InputFieldSelectionSetValueOutVO newInputFieldSelectionSetValueVO = new InputFieldSelectionSetValueOutVO();
			// inputFieldSelectionSetValueDao.toInputFieldSelectionSetValueOutVO(newInputFieldSelectionSetValue, newInputFieldSelectionSetValueVO, voMap);
			InputFieldSelectionSetValueOutVO newInputFieldSelectionSetValueVO =
					inputFieldSelectionSetValueDao.toInputFieldSelectionSetValueOutVO(newInputFieldSelectionSetValue);
			ServiceUtil.logSystemMessage(newInputFieldSelectionSetValue.getField(), newInputFieldSelectionSetValueVO.getField(), now, user,
					SystemMessageCodes.SELECTION_SET_VALUE_CLONED, newInputFieldSelectionSetValueVO,
					originalInputFieldSelectionSetValueVO,
					journalEntryDao);
		}
		InputFieldOutVO result = inputFieldDao.toInputFieldOutVO(newInputField);
		ServiceUtil.logSystemMessage(newInputField, result, now, user, SystemMessageCodes.INPUT_FIELD_CLONED, result, originalInputFieldVO, journalEntryDao);
		return result;
	}

	@Override
	protected InputFieldOutVO handleDeleteInputField(AuthenticationVO auth, Long inputFieldId,
			boolean defer, boolean force) throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		JournalEntryDao journalEntryDao = this.getJournalEntryDao();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		InputFieldOutVO result;
		if (!force && (defer
				|| this.getInquiryDao().getCountByField(inputFieldId) > 0
				|| this.getProbandListEntryTagDao().getCountByField(inputFieldId) > 0
				|| this.getECRFFieldDao().getCountByField(inputFieldId) > 0)) {
			InputField originalInputField = CheckIDUtil.checkInputFieldId(inputFieldId, inputFieldDao);
			// if (originalInputField.isLocalized()) {
			// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_LOCALIZED_INPUT_FIELD);
			// }
			InputFieldOutVO original = inputFieldDao.toInputFieldOutVO(originalInputField);
			inputFieldDao.evict(originalInputField);
			InputField inputField = CheckIDUtil.checkInputFieldId(inputFieldId, inputFieldDao, LockMode.PESSIMISTIC_WRITE);
			inputField.setDeferredDelete(true);
			CoreUtil.modifyVersion(inputField, inputField.getVersion(), now, user); // no opt. locking
			inputFieldDao.update(inputField);
			result = inputFieldDao.toInputFieldOutVO(inputField);
			ServiceUtil.logSystemMessage(inputField, result, now, user, SystemMessageCodes.INPUT_FIELD_MARKED_FOR_DELETION, result, original, journalEntryDao);
		} else {
			InputField inputField = CheckIDUtil.checkInputFieldId(inputFieldId, inputFieldDao, LockMode.PESSIMISTIC_WRITE);
			// if (inputField.isLocalized()) {
			// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_LOCALIZED_INPUT_FIELD);
			// }
			result = inputFieldDao.toInputFieldOutVO(inputField);
			// if (deleteCascade) {
			boolean checkProbandTrialLocked = Settings.getBoolean(SettingCodes.UPDATE_INPUT_FIELD_CHECK_PROBAND_TRIAL_LOCKED, Bundle.SETTINGS,
					DefaultSettings.UPDATE_INPUT_FIELD_CHECK_PROBAND_TRIAL_LOCKED);
			InquiryDao inquiryDao = this.getInquiryDao();
			InquiryValueDao inquiryValueDao = this.getInquiryValueDao();
			InputFieldValueDao inputFieldValueDao = this.getInputFieldValueDao();
			Iterator<Inquiry> inquiriesIt = inputField.getInquiries().iterator();
			while (inquiriesIt.hasNext()) {
				Inquiry inquiry = inquiriesIt.next();
				Trial trial = inquiry.getTrial();
				if (checkProbandTrialLocked) {
					ServiceUtil.checkTrialLocked(trial);
				}
				trial.removeInquiries(inquiry);
				ServiceUtil.removeInquiry(inquiry, true, checkProbandTrialLocked, now, user, true, true, inquiryValueDao, inputFieldValueDao, journalEntryDao,
						inquiryDao);
			}
			inputField.getInquiries().clear();
			ProbandListEntryTagDao probandListEntryTagDao = this.getProbandListEntryTagDao();
			ProbandListEntryTagValueDao probandListEntryTagValueDao = this.getProbandListEntryTagValueDao();
			Iterator<ProbandListEntryTag> listEntryTagsIt = inputField.getListEntryTags().iterator();
			while (listEntryTagsIt.hasNext()) {
				ProbandListEntryTag probandListEntryTag = listEntryTagsIt.next();
				Trial trial = probandListEntryTag.getTrial();
				if (checkProbandTrialLocked) {
					ServiceUtil.checkTrialLocked(trial);
				}
				trial.removeListEntryTags(probandListEntryTag);
				ServiceUtil.removeProbandListEntryTag(probandListEntryTag, true, checkProbandTrialLocked, now, user, true, true, probandListEntryTagValueDao,
						inputFieldValueDao,
						journalEntryDao,
						probandListEntryTagDao);
			}
			inputField.getListEntryTags().clear();
			ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
			NotificationDao notificationDao = this.getNotificationDao();
			NotificationRecipientDao notificationRecipientDao = this.getNotificationRecipientDao();
			ECRFFieldValueDao ecrfFieldValueDao = this.getECRFFieldValueDao();
			ECRFFieldStatusEntryDao ecrfFieldStatusEntryDao = this.getECRFFieldStatusEntryDao();
			ECRFStatusEntryDao ecrfStatusEntryDao = this.getECRFStatusEntryDao();
			ECRFDao ecrfDao = this.getECRFDao();
			Iterator<ECRFField> ecrfFieldsIt = inputField.getEcrfFields().iterator();
			while (ecrfFieldsIt.hasNext()) {
				ECRFField ecrfField = ecrfFieldsIt.next();
				Trial trial = ecrfField.getTrial();
				if (checkProbandTrialLocked) {
					ServiceUtil.checkTrialLocked(trial);
				}
				// long valuesLockedEcrfCount = this.getECRFStatusEntryDao().getCount(null, ecrfField.getEcrf().getId(), null, true, null, null, null); // row lock order
				// if (valuesLockedEcrfCount > 0) {
				// throw L10nUtil.initServiceException(ServiceExceptionCodes.LOCKED_ECRFS, this.getECRFDao().toECRFOutVO(ecrfField.getEcrf()).getUniqueName(),
				// valuesLockedEcrfCount);
				// }
				ServiceUtil.checkLockedEcrfs(ecrfField.getEcrf(), ecrfStatusEntryDao, ecrfDao);
				trial.removeEcrfFields(ecrfField);
				ecrfField.getEcrf().removeEcrfFields(ecrfField);
				ServiceUtil.removeEcrfField(ecrfField, true, checkProbandTrialLocked, now, user, true, true, ecrfFieldValueDao, ecrfFieldStatusEntryDao, inputFieldValueDao,
						journalEntryDao,
						ecrfFieldDao, notificationDao, notificationRecipientDao);
			}
			inputField.getEcrfFields().clear();
			InputFieldSelectionSetValueDao inputFieldSelectionSetValueDao = this.getInputFieldSelectionSetValueDao();
			Iterator<InputFieldSelectionSetValue> selectionSetValuesit = inputField.getSelectionSetValues().iterator();
			while (selectionSetValuesit.hasNext()) {
				InputFieldSelectionSetValue selectionSetValue = selectionSetValuesit.next();
				selectionSetValue.setField(null);
				inputFieldSelectionSetValueDao.remove(selectionSetValue);
			}
			inputField.getSelectionSetValues().clear();
			Iterator<JournalEntry> journalEntriesIt = inputField.getJournalEntries().iterator();
			while (journalEntriesIt.hasNext()) {
				JournalEntry journalEntry = journalEntriesIt.next();
				journalEntry.setInputField(null);
				journalEntryDao.remove(journalEntry);
			}
			inputField.getJournalEntries().clear();
			// }
			inputFieldDao.remove(inputField);
			logSystemMessage(user, result, now, user, SystemMessageCodes.INPUT_FIELD_DELETED, result, null, journalEntryDao);
		}
		return result;
	}

	@Override
	protected InputFieldSelectionSetValueOutVO handleDeleteSelectionSetValue(
			AuthenticationVO auth, Long selectionSetValueId, boolean defer, boolean force) throws Exception {

		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		InputFieldSelectionSetValueOutVO result;
		if (!force && (defer
				|| this.getInputFieldValueDao().getCount(selectionSetValueId) > 0)) {
			InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
			InputFieldSelectionSetValue originalSelectionSetValue = CheckIDUtil.checkInputFieldSelectionSetValueId(selectionSetValueId, selectionSetValueDao);
			// if (originalSelectionSetValue.isLocalized()) {
			// throw L10nUtil.initServiceException(ServiceExceptionCodes.CANNOT_DELETE_LOCALIZED_SELECTION_SET_VALUE);
			// }
			InputFieldSelectionSetValueOutVO original = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(originalSelectionSetValue);
			selectionSetValueDao.evict(originalSelectionSetValue);
			InputFieldSelectionSetValue selectionSetValue = CheckIDUtil.checkInputFieldSelectionSetValueId(selectionSetValueId, selectionSetValueDao);
			selectionSetValue.setDeferredDelete(true);
			CoreUtil.modifyVersion(selectionSetValue, selectionSetValue.getVersion(), now, user); // no opt. locking
			selectionSetValueDao.update(selectionSetValue);
			result = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(selectionSetValue);
			ServiceUtil.logSystemMessage(selectionSetValue.getField(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_MARKED_FOR_DELETION, result, original,
					this.getJournalEntryDao());
		} else {
			result = deleteSelectionSetValueHelper(null, selectionSetValueId, true,
					Settings.getBoolean(SettingCodes.UPDATE_INPUT_FIELD_CHECK_PROBAND_TRIAL_LOCKED, Bundle.SETTINGS,
							DefaultSettings.UPDATE_INPUT_FIELD_CHECK_PROBAND_TRIAL_LOCKED),now, user);
		}
		return result;
	}

	@Override
	protected Collection<ECRFFieldOutVO> handleGetEcrfFieldList(AuthenticationVO auth, Long fieldId, PSFVO psf) throws Exception {
		if (fieldId != null) {
			CheckIDUtil.checkInputFieldId(fieldId, this.getInputFieldDao());
		}
		ECRFFieldDao ecrfFieldDao = this.getECRFFieldDao();
		Collection ecrfFields = ecrfFieldDao.findByTrialField(null, fieldId, psf);
		ecrfFieldDao.toECRFFieldOutVOCollection(ecrfFields);
		return ecrfFields;
	}

	@Override
	protected long handleGetEcrfFieldMaxSelectionSetValueCount(AuthenticationVO auth, Long trialId) throws Exception {
		CheckIDUtil.checkTrialId(trialId, this.getTrialDao());
		return this.getInputFieldDao().getEcrfFieldMaxSelectionSetValueCount(trialId);
	}

	@Override
	protected long handleGetEcrfFieldValueCount(AuthenticationVO auth, Long ecrfFieldId, boolean excludeAuditTrail) throws Exception {
		CheckIDUtil.checkEcrfFieldId(ecrfFieldId, this.getECRFFieldDao());
		return this.getECRFFieldValueDao().getCount(ecrfFieldId, excludeAuditTrail);
	}

	@Override
	protected InputFieldOutVO handleGetInputField(AuthenticationVO auth, Long inputFieldId)
			throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField inputField = CheckIDUtil.checkInputFieldId(inputFieldId, inputFieldDao);
		InputFieldOutVO result = inputFieldDao.toInputFieldOutVO(inputField);
		return result;
	}

	@Override
	protected Collection<InputFieldOutVO> handleGetInputFieldList(AuthenticationVO auth, Long fieldId, PSFVO psf) throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		if (fieldId != null) {
			CheckIDUtil.checkInputFieldId(fieldId, inputFieldDao);
		}
		Collection inputFields = inputFieldDao.findById(fieldId, psf);
		inputFieldDao.toInputFieldOutVOCollection(inputFields);
		return inputFields;
	}

	@Override
	protected long handleGetInputFieldValueCount(AuthenticationVO auth, Long selectionSetValueId) throws Exception {
		CheckIDUtil.checkInputFieldSelectionSetValueId(selectionSetValueId, this.getInputFieldSelectionSetValueDao());
		return this.getInputFieldValueDao().getCount(selectionSetValueId);
	}

	@Override
	protected Collection<InquiryOutVO> handleGetInquiryList(AuthenticationVO auth, Long fieldId,
			PSFVO psf) throws Exception {
		if (fieldId != null) {
			CheckIDUtil.checkInputFieldId(fieldId, this.getInputFieldDao());
		}
		InquiryDao inquiryDao = this.getInquiryDao();
		Collection inquiries = inquiryDao.findByTrialField(null, fieldId, psf);
		inquiryDao.toInquiryOutVOCollection(inquiries);
		return inquiries;
	}

	@Override
	protected long handleGetInquiryValueCount(AuthenticationVO auth, Long inquiryId) throws Exception {
		CheckIDUtil.checkInquiryId(inquiryId, this.getInquiryDao());
		return this.getInquiryValueDao().getCount(inquiryId);
	}

	@Override
	protected Collection<ProbandListEntryTagOutVO> handleGetProbandListEntryTagList(
			AuthenticationVO auth, Long fieldId, PSFVO psf) throws Exception {
		if (fieldId != null) {
			CheckIDUtil.checkInputFieldId(fieldId, this.getInputFieldDao());
		}
		ProbandListEntryTagDao listEntryTagDao = this.getProbandListEntryTagDao();
		Collection listEntryTags = listEntryTagDao.findByTrialFieldJs(null, fieldId, false, null, psf);
		listEntryTagDao.toProbandListEntryTagOutVOCollection(listEntryTags);
		return listEntryTags;
	}

	@Override
	protected long handleGetProbandListEntryTagValueCount(AuthenticationVO auth, Long probandListEntryTagId) throws Exception {
		CheckIDUtil.checkProbandListEntryTagId(probandListEntryTagId, this.getProbandListEntryTagDao());
		return this.getProbandListEntryTagValueDao().getCount(null, probandListEntryTagId);
	}

	@Override
	protected InputFieldSelectionSetValueOutVO handleGetSelectionSetValue(
			AuthenticationVO auth, Long selectionSetValueId) throws Exception {
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		InputFieldSelectionSetValue selectionSetValue = CheckIDUtil.checkInputFieldSelectionSetValueId(selectionSetValueId, selectionSetValueDao);
		InputFieldSelectionSetValueOutVO result = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(selectionSetValue);
		return result;
	}

	@Override
	protected long handleGetSelectionSetValueCount(AuthenticationVO auth, Long fieldId, Boolean preset) throws Exception {
		if (fieldId != null) {
			CheckIDUtil.checkInputFieldId(fieldId, this.getInputFieldDao());
		}
		return this.getInputFieldSelectionSetValueDao().getCount(fieldId, preset);
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> handleGetSelectionSetValueList(AuthenticationVO auth, Long fieldId, Boolean preset,
			PSFVO psf) throws Exception {
		if (fieldId != null) {
			CheckIDUtil.checkInputFieldId(fieldId, this.getInputFieldDao());
		}
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		Collection selectionSetValues = selectionSetValueDao.findByFieldPreset(fieldId, preset, psf);
		selectionSetValueDao.toInputFieldSelectionSetValueOutVOCollection(selectionSetValues);
		return selectionSetValues;
	}

	@Override
	protected InputFieldOutVO handleUpdateInputField(
			AuthenticationVO auth, InputFieldInVO modifiedInputField) throws Exception {
		InputFieldDao inputFieldDao = this.getInputFieldDao();
		InputField originalInputField = CheckIDUtil.checkInputFieldId(modifiedInputField.getId(), inputFieldDao, LockMode.PESSIMISTIC_WRITE);
		checkUpdateInputFieldInput(modifiedInputField, originalInputField);
		InputFieldOutVO original = inputFieldDao.toInputFieldOutVO(originalInputField);
		inputFieldDao.evict(originalInputField);
		InputField inputField = inputFieldDao.inputFieldInVOToEntity(modifiedInputField);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalInputField, inputField, now, user);
		inputFieldDao.update(inputField);
		InputFieldOutVO result = inputFieldDao.toInputFieldOutVO(inputField);
		ServiceUtil.logSystemMessage(inputField, result, now, user, SystemMessageCodes.INPUT_FIELD_UPDATED, result, original, this.getJournalEntryDao());
		return result;
	}

	@Override
	protected InputFieldSelectionSetValueOutVO handleUpdateSelectionSetValue(
			AuthenticationVO auth, InputFieldSelectionSetValueInVO modifiedSelectionSetValue)
					throws Exception {
		InputFieldSelectionSetValueDao selectionSetValueDao = this.getInputFieldSelectionSetValueDao();
		InputFieldSelectionSetValue originalSelectionSetValue = CheckIDUtil.checkInputFieldSelectionSetValueId(modifiedSelectionSetValue.getId(),
				selectionSetValueDao);
		checkUpdateSelectionSetValueInput(originalSelectionSetValue, modifiedSelectionSetValue,
				Settings.getBoolean(SettingCodes.UPDATE_INPUT_FIELD_CHECK_PROBAND_TRIAL_LOCKED, Bundle.SETTINGS, DefaultSettings.UPDATE_INPUT_FIELD_CHECK_PROBAND_TRIAL_LOCKED));
		InputFieldSelectionSetValueOutVO original = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(originalSelectionSetValue);
		selectionSetValueDao.evict(originalSelectionSetValue);
		InputFieldSelectionSetValue selectionSetValue = selectionSetValueDao.inputFieldSelectionSetValueInVOToEntity(modifiedSelectionSetValue);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		User user = CoreUtil.getUser();
		CoreUtil.modifyVersion(originalSelectionSetValue, selectionSetValue, now, user);
		selectionSetValueDao.update(selectionSetValue);
		InputFieldSelectionSetValueOutVO result = selectionSetValueDao.toInputFieldSelectionSetValueOutVO(selectionSetValue);
		ServiceUtil.logSystemMessage(selectionSetValue.getField(), result.getField(), now, user, SystemMessageCodes.SELECTION_SET_VALUE_UPDATED, result, original,
				this.getJournalEntryDao());
		return result;
	}
}