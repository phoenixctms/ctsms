package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.phoenixctms.ctsms.enumeration.Color;
import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusEntryOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldStatusTypeVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueJsonVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.ECRFFieldValuesOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.web.adapt.EcrfFieldValueInVOStringAdapter;
import org.phoenixctms.ctsms.web.model.shared.EcrfFieldValueBean;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class EcrfFieldInputModel extends InputModel {

	protected ECRFFieldValueInVO ecrfFieldValue;
	protected ECRFFieldOutVO ecrfField;
	private String modifiedAnnotation;
	// private long unresolvedEcrfFieldStatusCountSum;
	private boolean unlockValue;
	private Color fieldColor;
	private ECRFFieldStatusEntryOutVO lastUnresolvedFieldStatusEntry;

	// private EcrfFieldInputModelList modelList;
	public EcrfFieldInputModel(ECRFFieldValueInVO ecrfFieldValue) {
		super();
		unlockValue = false;
		fieldColor = null;
		lastUnresolvedFieldStatusEntry = null;
		// unresolvedEcrfFieldStatusCountSum = 0l;
		setEcrfFieldValue(ecrfFieldValue);
		// this.modelList = modelList;
	}

	@Override
	public void applyPresets() {
		if (ecrfFieldValue != null && inputField != null && ecrfField != null) {
			ecrfFieldValue.setBooleanValue(inputField.getBooleanPreset());
			ecrfFieldValue.setDateValue(inputField.getDatePreset());
			ecrfFieldValue.setTimeValue(inputField.getTimePreset());
			ecrfFieldValue.setFloatValue(inputField.getFloatPreset());
			ecrfFieldValue.setLongValue(inputField.getLongPreset());
			ecrfFieldValue.setTimestampValue(inputField.getTimestampPreset());
			ecrfFieldValue.setInkValues(null);
			ecrfFieldValue.setReasonForChange(ecrfFieldValue.getId() == null || !ecrfField.getAuditTrail() ? null
					: Messages
							.getString(MessageCodes.ECRF_FIELD_VALUE_REASON_FOR_CHANGE_PRESET));
			ecrfFieldValue.getSelectionValueIds().clear();
			// if (isAutocomplete()) {
			// ecrfFieldValue.setTextValue(autoCompletePresetValue);
			// } else {
			ecrfFieldValue.setTextValue(inputField.getTextPreset());
			Iterator<InputFieldSelectionSetValueOutVO> it = inputField.getSelectionSetValues().iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionValue = it.next();
				if (selectionValue.isPreset()) {
					ecrfFieldValue.getSelectionValueIds().add(selectionValue.getId());
				}
			}
			// }
		}
	}

	@Override
	protected Object check() {
		return null;
	}

	@Override
	public boolean getBooleanValue() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getBooleanValue();
	}

	@Override
	public String getComment() {
		if (ecrfField != null) {
			return ecrfField.getComment();
		}
		return null;
	}

	@Override
	public Date getDateValue() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getDateValue();
	}

	public Long getEcrfFieldId() {
		if (ecrfField != null) {
			return ecrfField.getId();
		}
		return null;
	}

	public ECRFFieldValueInVO getEcrfFieldValue() {
		return ecrfFieldValue;
	}

	@Override
	protected Color getFieldColor() {
		return fieldColor;
	}

	@Override
	public Float getFloatValue() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getFloatValue();
	}

	@Override
	public String getInkValue() throws UnsupportedEncodingException {
		return ecrfFieldValue == null ? null : CommonUtil.inkValueToString(ecrfFieldValue.getInkValues());
	}

	@Override
	public String getJsOutputExpression() {
		if (ecrfField != null) {
			return ecrfField.getJsOutputExpression();
		}
		return "";
	}

	@Override
	public String getJsValueExpression() {
		if (ecrfField != null) {
			return ecrfField.getJsValueExpression();
		}
		return "";
	}

	@Override
	public String getJsVariableName() {
		if (ecrfField != null) {
			return ecrfField.getJsVariableName();
		}
		return "";
	}

	@Override
	public Long getLongValue() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getLongValue();
	}

	@Override
	public String getModifiedAnnotation() {
		return modifiedAnnotation;
	}

	@Override
	public String getName() {
		if (ecrfFieldValue != null && ecrfField != null && inputField != null) {
			if (ecrfFieldValue.getId() != null) {
				return Messages.getMessage(MessageCodes.ECRF_FIELD_VALUE_INPUT_TITLE, Long.toString(ecrfField.getPosition()), inputField.getName(), getValueString());
			} else {
				return Messages.getMessage(MessageCodes.NEW_ECRF_FIELD_VALUE_INPUT_TITLE, Long.toString(ecrfField.getPosition()), inputField.getName());
			}
		}
		return null;
	}

	@Override
	public String getOutputId() {
		StringBuilder sb = new StringBuilder(JSValues.INPUT_FIELD_OUTPUT_ID_PREFIX.toString());
		sb.append(getEcrfFieldId());
		if (isSeries()) {
			sb.append(JSValues.INPUT_FIELD_OUTPUT_ID_INDEX_SEPARATOR.toString());
			sb.append(getSeriesIndex());
		}
		return sb.toString();
	}

	public Long getPosition() {
		if (ecrfField != null) {
			return ecrfField.getPosition();
		}
		return null;
	}

	@Override
	public String getReasonForChange() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getReasonForChange();
	}

	@Override
	public Long getSelectionValueId() {
		if (ecrfFieldValue != null) {
			Collection<Long> selectionValueIds = ecrfFieldValue.getSelectionValueIds();
			if (selectionValueIds != null && selectionValueIds.size() > 0) {
				return selectionValueIds.iterator().next();
			}
		}
		return null;
	}

	@Override
	public List<String> getSelectionValueIds() {
		ArrayList<String> result;
		if (ecrfFieldValue != null) {
			Collection<Long> selectionValueIds = ecrfFieldValue.getSelectionValueIds();
			result = new ArrayList<String>(selectionValueIds.size());
			Iterator<Long> it = selectionValueIds.iterator();
			while (it.hasNext()) {
				result.add(it.next().toString());
			}
		} else {
			result = new ArrayList<String>();
		}
		return result;
	}

	@Override
	public Long getSeriesIndex() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getIndex();
	}

	@Override
	public String getStatusComment() {
		return lastUnresolvedFieldStatusEntry != null && !CommonUtil.isEmptyString(lastUnresolvedFieldStatusEntry.getComment())
				&& Settings.getBoolean(SettingCodes.SHOW_LAST_UNRESOLVED_FIELD_STATUS, Bundle.SETTINGS, DefaultSettings.SHOW_LAST_UNRESOLVED_FIELD_STATUS)
						? Messages.getMessage(MessageCodes.ECRF_FIELD_VALUE_INPUT_STATUS_COMMENT, lastUnresolvedFieldStatusEntry.getStatus().getName(),
								lastUnresolvedFieldStatusEntry.getComment())
						: null;
	}

	@Override
	public String getTextValue() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getTextValue();
	}

	@Override
	public Date getTimestampValue() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getTimestampValue();
	}

	@Override
	public Date getTimeValue() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getTimeValue();
	}

	@Override
	public String getUniqueName() {
		if (ecrfField != null) {
			return ecrfField.getUniqueName();
		}
		return null;
	}

	@Override
	public String getValueString() {
		return (new EcrfFieldValueInVOStringAdapter(Settings.getInt(SettingCodes.INPUT_MODEL_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
				DefaultSettings.INPUT_MODEL_VALUE_TEXT_CLIP_MAX_LENGTH))).toString(inputField, ecrfFieldValue);
	}

	@Override
	protected Long getVersion() {
		return ecrfFieldValue != null ? ecrfFieldValue.getVersion() : null;
	}

	@Override
	public String getWidgetVar() {
		StringBuilder sb = new StringBuilder(JSValues.INPUT_FIELD_WIDGET_VAR_PREFIX.toString());
		sb.append(getEcrfFieldId());
		if (isSeries()) {
			sb.append(JSValues.INPUT_FIELD_WIDGET_VAR_INDEX_SEPARATOR.toString());
			sb.append(getSeriesIndex());
		}
		return sb.toString();
	}

	@Override
	public boolean isAuditTrail() {
		return true; // ecrfField == null ? false : isCreated() && ecrfField.getAuditTrail(); // even when ecrf status does not have audit trail
	}

	@Override
	public boolean isBooleanValue() {
		return ecrfFieldValue == null ? null : ecrfFieldValue.getBooleanValue();
	}

	@Override
	public boolean isCollapsed() {
		return !((ecrfFieldValue != null && ecrfFieldValue.getId() != null) || isError());
	}

	@Override
	public boolean isCreated() {
		return ecrfFieldValue != null && ecrfFieldValue.getId() != null;
	}

	@Override
	public boolean isDeferredDelete() {
		if (ecrfField != null) {
			return ecrfField.getDeferredDelete() || super.isDeferredDelete();
		}
		return super.isDeferredDelete();
	}

	@Override
	public boolean isDisabled() {
		if (ecrfField != null) {
			return ecrfField.getDisabled() || ecrfField.getEcrf().getDisabled();
		}
		return false;
	}
	// @Override
	// public boolean isEditable() {
	// if (ecrfField != null) {
	// return !ecrfField.getEcrf().getDisabled();
	// }
	// return false;
	// // return true;
	// // return ecrfField != null && !WebUtil.isTrialLocked(ecrfField.getTrial()); // todo: add ecrf lock check..
	// }

	@Override
	public boolean isDummy() {
		return false;
	}

	@Override
	public boolean isJsVariable() {
		if (ecrfField != null) {
			return !CommonUtil.isEmptyString(ecrfField.getJsVariableName());
		}
		return false;
	}

	@Override
	public boolean isOptional() {
		if (ecrfField != null) {
			return ecrfField.getOptional(); // || (!EcrfFieldValueBean.SAVE_NEW_SERIES && !isCreated() && ecrfField.getSeries());
		}
		return false;
	}

	@Override
	public boolean isReasonForChangeRequired() {
		return ecrfField == null ? false : isCreated() && ecrfField.isAuditTrail() && ecrfField.getReasonForChangeRequired();
	}

	@Override
	public boolean isSeries() {
		if (ecrfField != null) {
			return ecrfField.getSeries();
		}
		return false;
	}

	// public boolean isShowReasonForChange() {
	// return isAuditTrail() && isReasonForChangeRequired();
	// // if (Settings.getBoolean(SettingCodes.ECRF_FIELD_VALUES_HIDE_OPTIONAL_REASON_FOR_CHANGE_FIELD, Bundle.SETTINGS,
	// // DefaultSettings.ECRF_FIELD_VALUES_HIDE_OPTIONAL_REASON_FOR_CHANGE_FIELD)) {
	// // return isReasonForChangeRequired();
	// // } else {
	// // return isAuditTrail();
	// // }
	// }
	@Override
	public boolean isShowToolbar() {
		return true;
	}

	public boolean isUnlockValue() {
		return unlockValue;
	}

	@Override
	public Object load() {
		if (ecrfFieldValue != null) { // && ecrfFieldValue.getId() != null) {
			setErrorMessage(null);
			setLastFieldStatus(null);
			try {
				ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService()
						.getEcrfFieldValue(WebUtil.getAuthentication(), ecrfFieldValue.getListEntryId(), ecrfFieldValue.getEcrfFieldId(), ecrfFieldValue.getIndex());
				ECRFFieldValueOutVO out = values.getPageValues().iterator().next();
				EcrfFieldValueBean.copyEcrfFieldValueOutToIn(ecrfFieldValue, out);
				setLastFieldStatus(out);
				setModifiedAnnotation(out);
				// if (isJsVariable()) {
				return values.getJsValues();
				// }
				// return updateModels(values);
			} catch (NoSuchElementException|ServiceException|AuthorisationException|IllegalArgumentException e) {
				setErrorMessage(e.getMessage());
			} catch (AuthenticationException e) {
				setErrorMessage(e.getMessage());
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	@Override
	public Object reset() {
		setErrorMessage(null);
		applyPresets();
		if (isJsVariable()) {
			ECRFFieldValueJsonVO out = new ECRFFieldValueJsonVO();
			CommonUtil.copyEcrfFieldValueInToJson(out, ecrfFieldValue, ecrfField);
			return out;
		}
		return null;
	}

	public void sanitizeReasonForChange() {
		if (ecrfFieldValue != null && CommonUtil.isEmptyString(ecrfFieldValue.getReasonForChange())) {
			ecrfFieldValue.setReasonForChange(null);
		}
	}

	@Override
	public void setBooleanValue(boolean value) {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setBooleanValue(value);
		}
	}

	@Override
	public void setDateValue(Date value) {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setDateValue(value);
		}
	}

	public void setEcrfFieldValue(ECRFFieldValueInVO ecrfFieldValue) {
		this.ecrfFieldValue = ecrfFieldValue;
		ecrfField = null;
		setField(null);
		if (ecrfFieldValue != null) {
			ecrfField = WebUtil.getEcrfField(ecrfFieldValue.getEcrfFieldId());
			if (ecrfField != null) {
				setField(ecrfField.getField());
			}
		}
	}

	@Override
	protected void setErrorMessageFromServiceException(Object data, String exceptionMessage) {
		HashMap<Long, HashMap<Long, String>> errorMessagesMap;
		try {
			errorMessagesMap = (HashMap<Long, HashMap<Long, String>>) data;
		} catch (ClassCastException e) {
			errorMessagesMap = null;
		}
		if (errorMessagesMap != null && errorMessagesMap.size() > 0) {
			HashMap<Long, String> indexErrorMessagesMap = errorMessagesMap.get(getEcrfFieldId());
			String errorMessage = null;
			if (indexErrorMessagesMap != null) {
				errorMessage = indexErrorMessagesMap.get(getSeriesIndex());
			}
			setErrorMessage(errorMessage != null && errorMessage.length() > 0 ? errorMessage : exceptionMessage);
		} else {
			setErrorMessage(null);
		}
	}

	@Override
	public void setFloatValue(Float value) {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setFloatValue(value);
		}
	}

	@Override
	public void setInkValue(String value) throws UnsupportedEncodingException {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setInkValues(CommonUtil.stringToInkValue(value));
		}
	}

	public void setLastFieldStatus(ECRFFieldValueOutVO out) {
		unlockValue = false;
		fieldColor = null;
		lastUnresolvedFieldStatusEntry = null;
		if (out != null) {
			lastUnresolvedFieldStatusEntry = out.getLastUnresolvedFieldStatusEntry();
			boolean initial = false;
			boolean updated = false;
			boolean proposed = false;
			boolean resolved = false;
			Color initialColor = null;
			Color updatedColor = null;
			Color proposedColor = null;
			Color resolvedColor = null;
			Iterator<ECRFFieldStatusTypeVO> it = out.getLastFieldStatuses().iterator();
			while (it.hasNext()) {
				ECRFFieldStatusTypeVO lastStatus = it.next();
				if (lastStatus.isInitial() && !lastStatus.isResolved()) {
					initial = true;
					initialColor = lastStatus.getColor();
				}
				if (lastStatus.isUpdated()) {
					updated = true;
					updatedColor = lastStatus.getColor();
				}
				if (lastStatus.isProposed()) {
					proposed = true;
					proposedColor = lastStatus.getColor();
				}
				if (lastStatus.isResolved()) { // && !lastStatus.isInitial()) {
					resolved = true;
					resolvedColor = lastStatus.getColor();
				}
				unlockValue |= lastStatus.getUnlockValue();
			}
			if (initial) {
				fieldColor = initialColor;
			} else if (updated) {
				fieldColor = updatedColor;
			} else if (proposed) {
				fieldColor = proposedColor;
			} else if (resolved) {
				fieldColor = resolvedColor;
			}
		}
	}

	@Override
	public void setLongValue(Long value) {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setLongValue(value);
		}
	}

	public void setModifiedAnnotation(ECRFFieldValueOutVO out) {
		if (out != null && out.getId() != null) {
			modifiedAnnotation = WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			modifiedAnnotation = "";
		}
	}

	@Override
	public void setReasonForChange(String reasonForChange) {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setReasonForChange(reasonForChange);
		}
	}

	@Override
	public void setSelectionValueId(Long value) {
		if (ecrfFieldValue != null) {
			ArrayList<Long> selectionValueIds;
			if (value != null) {
				selectionValueIds = new ArrayList<Long>(1);
				selectionValueIds.add(value);
			} else {
				selectionValueIds = new ArrayList<Long>();
			}
			ecrfFieldValue.setSelectionValueIds(selectionValueIds);
		}
	}

	@Override
	public void setSelectionValueIds(List<String> value) {
		if (ecrfFieldValue != null) {
			ArrayList<Long> selectionValueIds;
			if (value != null && value.size() > 0) {
				selectionValueIds = new ArrayList<Long>(value.size());
				Iterator<String> it = value.iterator();
				while (it.hasNext()) {
					Long id = WebUtil.stringToLong(it.next());
					if (id != null) {
						selectionValueIds.add(id);
					}
				}
			} else {
				selectionValueIds = new ArrayList<Long>();
			}
			ecrfFieldValue.setSelectionValueIds(selectionValueIds);
		}
	}

	@Override
	public void setTextValue(String value) {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setTextValue(value);
		}
	}

	@Override
	public void setTimestampValue(Date value) {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setTimestampValue(value);
		}
	}

	@Override
	public void setTimeValue(Date value) {
		if (ecrfFieldValue != null) {
			ecrfFieldValue.setTimeValue(value);
		}
	}

	// private Collection<ECRFFieldValueJsonVO> updateModels(ECRFFieldValuesOutVO values) {
	//
	// if (modelList != null && values.getPageValues().size() > 1) {
	// HashMap<Long,HashMap<Long,ECRFFieldValueOutVO>> outMap = new HashMap<Long, HashMap<Long,ECRFFieldValueOutVO>>();
	// Iterator<ECRFFieldValueOutVO> pageValuesIt = values.getPageValues().iterator();
	// while (pageValuesIt.hasNext()) {
	// ECRFFieldValueOutVO out = pageValuesIt.next();
	// HashMap<Long, ECRFFieldValueOutVO> indexMap;
	// if (outMap.containsKey(out.getEcrfField().getId())) {
	// indexMap = outMap.get(out.getEcrfField().getId());
	// } else {
	// indexMap = new HashMap<Long,ECRFFieldValueOutVO>();
	// outMap.put(out.getEcrfField().getId(), indexMap);
	// }
	// indexMap.put(out.getIndex(), out);
	// }
	//
	// Iterator<EcrfFieldInputModel> modelsIt = modelList.iterator();
	// while (modelsIt.hasNext()) {
	// EcrfFieldInputModel model = modelsIt.next();
	// HashMap<Long, ECRFFieldValueOutVO> indexMap = outMap.get(model.getEcrfFieldId());
	// if (indexMap != null) {
	// ECRFFieldValueOutVO out = indexMap.get(model.getSeriesIndex());
	// if (out != null) {
	// ECRFFieldValueInVO in = new ECRFFieldValueInVO();
	// EcrfFieldValueBean.copyEcrfFieldValueOutToIn(in, out);
	// model.setEcrfFieldValue(in);
	// model.setModifiedAnnotation(out);
	// }
	// }
	// }
	// return values.getJsValues();
	// } else {
	// ECRFFieldValueOutVO out = values.getPageValues().iterator().next();
	// EcrfFieldValueBean.copyEcrfFieldValueOutToIn(ecrfFieldValue, out);
	// setModifiedAnnotation(out);
	// // if (isJsVariable()) {
	// return values.getJsValues();
	// // }
	// }
	// }
	@Override
	public Object update() {
		if (ecrfFieldValue != null) {
			setErrorMessage(null);
			setLastFieldStatus(null);
			sanitizeReasonForChange();
			HashSet<ECRFFieldValueInVO> ecrfFieldValues = new HashSet<ECRFFieldValueInVO>(1);
			ecrfFieldValues.add(ecrfFieldValue);
			try {
				ECRFFieldValuesOutVO values = WebUtil.getServiceLocator().getTrialService()
						.setEcrfFieldValues(WebUtil.getAuthentication(), ecrfFieldValues, null, null);
				ECRFFieldValueOutVO out = values.getPageValues().iterator().next();
				EcrfFieldValueBean.copyEcrfFieldValueOutToIn(ecrfFieldValue, out);
				setLastFieldStatus(out);
				setModifiedAnnotation(out);
				// if (isJsVariable()) {
				return values.getJsValues();
				// }
				// return updateModels(values);
			} catch (NoSuchElementException|AuthorisationException|IllegalArgumentException e) {
				setErrorMessage(e.getMessage());
			} catch (ServiceException e) {
				setErrorMessageFromServiceException(e.getData(), e.getMessage());
			} catch (AuthenticationException e) {
				setErrorMessage(e.getMessage());
				WebUtil.publishException(e);
			}
		}
		return null;
	}

	@Override
	protected String getInputTitle() {
		if (ecrfField != null) {
			return ecrfField.getTitle();
		}
		return null;
	}
}
