package org.phoenixctms.ctsms.web.model.shared.inputfield;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.phoenixctms.ctsms.exception.AuthenticationException;
import org.phoenixctms.ctsms.exception.AuthorisationException;
import org.phoenixctms.ctsms.exception.ServiceException;
import org.phoenixctms.ctsms.util.CommonUtil;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;
import org.phoenixctms.ctsms.vo.InquiryValueJsonVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValuesOutVO;
import org.phoenixctms.ctsms.web.adapt.InquiryValueInVOStringAdapter;
import org.phoenixctms.ctsms.web.model.shared.InquiryValueBeanBase;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public class InquiryInputModel extends InputModel {

	protected InquiryValueInVO inquiryValue;
	protected InquiryOutVO inquiry;
	private String modifiedAnnotation;

	public InquiryInputModel(InquiryValueInVO inquiryValue) {
		super();
		setInquiryValue(inquiryValue);
	}

	@Override
	public void applyPresets() {
		if (inquiryValue != null && inputField != null) {
			inquiryValue.setBooleanValue(inputField.getBooleanPreset());
			inquiryValue.setDateValue(inputField.getDatePreset());
			inquiryValue.setTimeValue(inputField.getTimePreset());
			inquiryValue.setFloatValue(inputField.getFloatPreset());
			inquiryValue.setLongValue(inputField.getLongPreset());
			inquiryValue.setTimestampValue(inputField.getTimestampPreset());
			inquiryValue.setInkValues(null);
			inquiryValue.getSelectionValueIds().clear();
			// if (isAutocomplete()) {
			// inquiryValue.setTextValue(autoCompletePresetValue);
			// } else {
			inquiryValue.setTextValue(inputField.getTextPreset());
			Iterator<InputFieldSelectionSetValueOutVO> it = inputField.getSelectionSetValues().iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionValue = it.next();
				if (selectionValue.isPreset()) {
					inquiryValue.getSelectionValueIds().add(selectionValue.getId());
				}
			}
			// }
		}
	}

	@Override
	protected Object check() {
		if (inquiryValue != null) {
			setErrorMessage(null);
			HashSet<InquiryValueInVO> inquiryValues = new HashSet<InquiryValueInVO>(1);
			inquiryValues.add(inquiryValue);
			try {
				InquiryValuesOutVO values = WebUtil.getServiceLocator().getTrialService().checkInquiryValues(WebUtil.getAuthentication(), inquiryValues);
				InquiryValueOutVO out = values.getPageValues().iterator().next();
				InquiryValueBeanBase.copyInquiryValueOutToIn(inquiryValue, out);
				setModifiedAnnotation(out);
				// if (isJsVariable()) {
				return values.getJsValues();
				// }
			} catch (NoSuchElementException | AuthorisationException | IllegalArgumentException e) {
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
	public boolean getBooleanValue() {
		return inquiryValue == null ? null : inquiryValue.getBooleanValue();
	}

	@Override
	public String getComment() {
		if (inquiry != null) {
			return inquiry.getComment();
		}
		return null;
	}

	@Override
	public Date getDateValue() {
		return inquiryValue == null ? null : inquiryValue.getDateValue();
	}

	@Override
	public Float getFloatValue() {
		return inquiryValue == null ? null : inquiryValue.getFloatValue();
	}

	@Override
	public String getInkValue() throws UnsupportedEncodingException {
		return inquiryValue == null ? null : CommonUtil.inkValueToString(inquiryValue.getInkValues());
	}

	public Long getInquiryId() {
		if (inquiry != null) {
			return inquiry.getId();
		}
		return null;
	}

	public InquiryValueInVO getInquiryValue() {
		return inquiryValue;
	}

	@Override
	public String getJsOutputExpression() {
		if (inquiry != null) {
			return inquiry.getJsOutputExpression();
		}
		return "";
	}

	@Override
	public String getJsValueExpression() {
		if (inquiry != null) {
			return inquiry.getJsValueExpression();
		}
		return "";
	}

	@Override
	public String getJsVariableName() {
		if (inquiry != null) {
			return inquiry.getJsVariableName();
		}
		return "";
	}

	@Override
	public Long getLongValue() {
		return inquiryValue == null ? null : inquiryValue.getLongValue();
	}

	@Override
	public String getModifiedAnnotation() {
		return modifiedAnnotation;
	}

	@Override
	public String getName() {
		if (inquiryValue != null && inquiry != null && inputField != null) {
			if (inquiryValue.getId() != null) {
				return Messages.getMessage(MessageCodes.INQUIRY_VALUE_INPUT_TITLE, Long.toString(inquiry.getPosition()), inputField.getName(), getValueString());
			} else {
				return Messages.getMessage(MessageCodes.NEW_INQUIRY_VALUE_INPUT_TITLE, Long.toString(inquiry.getPosition()), inputField.getName());
			}
		}
		return null;
	}

	@Override
	public String getOutputId() {
		StringBuilder sb = new StringBuilder(JSValues.INPUT_FIELD_OUTPUT_ID_PREFIX.toString());
		sb.append(getInquiryId());
		return sb.toString();
	}

	public Long getPosition() {
		if (inquiry != null) {
			return inquiry.getPosition();
		}
		return null;
	}

	@Override
	public String getReasonForChange() {
		return null;
	}

	@Override
	public Long getSelectionValueId() {
		if (inquiryValue != null) {
			Collection<Long> selectionValueIds = inquiryValue.getSelectionValueIds();
			if (selectionValueIds != null && selectionValueIds.size() > 0) {
				return selectionValueIds.iterator().next();
			}
		}
		return null;
	}

	@Override
	public List<String> getSelectionValueIds() {
		ArrayList<String> result;
		if (inquiryValue != null) {
			Collection<Long> selectionValueIds = inquiryValue.getSelectionValueIds();
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
		return null;
	}

	@Override
	public String getStatusComment() {
		return null;
	}

	@Override
	public String getTextValue() {
		return inquiryValue == null ? null : inquiryValue.getTextValue();
	}

	@Override
	public Date getTimestampValue() {
		return inquiryValue == null ? null : inquiryValue.getTimestampValue();
	}

	@Override
	public Date getTimeValue() {
		return inquiryValue == null ? null : inquiryValue.getTimeValue();
	}

	@Override
	public String getUniqueName() {
		if (inquiry != null) {
			return inquiry.getUniqueName();
		}
		return null;
	}

	@Override
	public String getValueString() {
		return (new InquiryValueInVOStringAdapter(Settings.getInt(SettingCodes.INPUT_MODEL_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
				DefaultSettings.INPUT_MODEL_VALUE_TEXT_CLIP_MAX_LENGTH))).toString(inputField, inquiryValue);
	}

	@Override
	protected Long getVersion() {
		return inquiryValue != null ? inquiryValue.getVersion() : null;
	}

	@Override
	public String getWidgetVar() {
		StringBuilder sb = new StringBuilder(JSValues.INPUT_FIELD_WIDGET_VAR_PREFIX.toString());
		sb.append(getInquiryId());
		return sb.toString();
	}

	@Override
	public boolean isAuditTrail() {
		return false;
	}

	@Override
	public boolean isBooleanValue() {
		return inquiryValue == null ? null : inquiryValue.getBooleanValue();
	}

	@Override
	public boolean isCollapsed() {
		return !((inquiryValue != null && inquiryValue.getId() != null) || isError());
	}

	@Override
	public boolean isCreated() {
		return inquiryValue != null && inquiryValue.getId() != null;
	}

	@Override
	public boolean isDeferredDelete() {
		if (inquiry != null) {
			return inquiry.getDeferredDelete() || super.isDeferredDelete();
		}
		return super.isDeferredDelete();
	}

	@Override
	public boolean isDisabled() {
		if (inquiry != null) {
			return inquiry.getDisabled();
		}
		return false;
	}

	// @Override
	// public boolean isEditable() {
	// return true;
	// // return inquiry != null && !WebUtil.isTrialLocked(inquiry.getTrial());
	// }
	@Override
	public boolean isDummy() {
		return false;
	}

	@Override
	public boolean isJsVariable() {
		if (inquiry != null) {
			return !CommonUtil.isEmptyString(inquiry.getJsVariableName());
		}
		return false;
	}

	@Override
	public boolean isOptional() {
		if (inquiry != null) {
			return inquiry.getOptional();
		}
		return false;
	}

	@Override
	public boolean isReasonForChangeRequired() {
		return false;
	}

	@Override
	public boolean isSeries() {
		return false;
	}

	@Override
	public boolean isShowToolbar() {
		return true;
	}

	@Override
	public Object load() {
		if (inquiryValue != null) { // && inquiryValue.getId() != null) {
			setErrorMessage(null);
			try {
				InquiryValuesOutVO values = WebUtil.getServiceLocator().getProbandService()
						.getInquiryValue(WebUtil.getAuthentication(), inquiryValue.getProbandId(), inquiryValue.getInquiryId());
				InquiryValueOutVO out = values.getPageValues().iterator().next();
				InquiryValueBeanBase.copyInquiryValueOutToIn(inquiryValue, out);
				setModifiedAnnotation(out);
				// if (isJsVariable()) {
				return values.getJsValues();
				// }
			} catch (NoSuchElementException | ServiceException | AuthorisationException | IllegalArgumentException e) {
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
			InquiryValueJsonVO out = new InquiryValueJsonVO();
			CommonUtil.copyInquiryValueInToJson(out, inquiryValue, inquiry);
			return out;
		}
		return null;
	}

	@Override
	public void setBooleanValue(boolean value) {
		if (inquiryValue != null) {
			inquiryValue.setBooleanValue(value);
		}
	}

	@Override
	public void setDateValue(Date value) {
		if (inquiryValue != null) {
			inquiryValue.setDateValue(value);
		}
	}

	@Override
	public void setFloatValue(Float value) {
		if (inquiryValue != null) {
			inquiryValue.setFloatValue(value);
		}
	}

	@Override
	public void setInkValue(String value) throws UnsupportedEncodingException {
		if (inquiryValue != null) {
			inquiryValue.setInkValues(CommonUtil.stringToInkValue(value));
		}
	}

	public void setInquiryValue(InquiryValueInVO inquiryValue) {
		this.inquiryValue = inquiryValue;
		inquiry = null;
		setField(null);
		if (inquiryValue != null) {
			inquiry = WebUtil.getInquiry(inquiryValue.getInquiryId());
			if (inquiry != null) {
				setField(inquiry.getField());
			}
		}
	}

	@Override
	public void setLongValue(Long value) {
		if (inquiryValue != null) {
			inquiryValue.setLongValue(value);
		}
	}

	public void setModifiedAnnotation(InquiryValueOutVO out) {
		if (out != null && out.getId() != null) {
			modifiedAnnotation = WebUtil.getModifiedAnnotation(out.getVersion(), out.getModifiedUser(), out.getModifiedTimestamp());
		} else {
			modifiedAnnotation = "";
		}
	}

	@Override
	public void setReasonForChange(String reasonForChange) {
	}

	@Override
	public void setSelectionValueId(Long value) {
		if (inquiryValue != null) {
			ArrayList<Long> selectionValueIds;
			if (value != null) {
				selectionValueIds = new ArrayList<Long>(1);
				selectionValueIds.add(value);
			} else {
				selectionValueIds = new ArrayList<Long>();
			}
			inquiryValue.setSelectionValueIds(selectionValueIds);
		}
	}

	@Override
	public void setSelectionValueIds(List<String> value) {
		if (inquiryValue != null) {
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
			inquiryValue.setSelectionValueIds(selectionValueIds);
		}
	}

	@Override
	public void setTextValue(String value) {
		if (inquiryValue != null) {
			inquiryValue.setTextValue(value);
		}
	}

	@Override
	public void setTimestampValue(Date value) {
		if (inquiryValue != null) {
			inquiryValue.setTimestampValue(value);
		}
	}

	@Override
	public void setTimeValue(Date value) {
		if (inquiryValue != null) {
			inquiryValue.setTimeValue(value);
		}
	}

	@Override
	public Object update() {
		return update(false);
	}

	@Override
	public Object forceUpdate() {
		return update(true);
	}

	public Object update(boolean force) {
		if (inquiryValue != null) {
			setErrorMessage(null);
			HashSet<InquiryValueInVO> inquiryValues = new HashSet<InquiryValueInVO>(1);
			inquiryValues.add(inquiryValue);
			try {
				InquiryValuesOutVO values = WebUtil.getServiceLocator().getProbandService().setInquiryValues(WebUtil.getAuthentication(), inquiryValues, force);
				InquiryValueOutVO out = values.getPageValues().iterator().next();
				InquiryValueBeanBase.copyInquiryValueOutToIn(inquiryValue, out);
				setModifiedAnnotation(out);
				// if (isJsVariable()) {
				return values.getJsValues();
				// }
			} catch (NoSuchElementException | AuthorisationException | IllegalArgumentException e) {
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
		if (inquiry != null) {
			return inquiry.getTitle();
		}
		return null;
	}
}
