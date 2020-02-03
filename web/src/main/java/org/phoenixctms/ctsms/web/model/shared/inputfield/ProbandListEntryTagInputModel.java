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
import org.phoenixctms.ctsms.vo.ProbandListEntryTagOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueInVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueJsonVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValuesOutVO;
import org.phoenixctms.ctsms.web.adapt.ProbandListEntryTagValueInVOStringAdapter;
import org.phoenixctms.ctsms.web.model.shared.ProbandListEntryTagValueBean;
import org.phoenixctms.ctsms.web.util.DefaultSettings;
import org.phoenixctms.ctsms.web.util.JSValues;
import org.phoenixctms.ctsms.web.util.MessageCodes;
import org.phoenixctms.ctsms.web.util.Messages;
import org.phoenixctms.ctsms.web.util.SettingCodes;
import org.phoenixctms.ctsms.web.util.Settings;
import org.phoenixctms.ctsms.web.util.Settings.Bundle;
import org.phoenixctms.ctsms.web.util.WebUtil;

public final class ProbandListEntryTagInputModel extends InputModel {

	private ProbandListEntryTagValueInVO tagValue;
	private ProbandListEntryTagOutVO tag;
	private String modifiedAnnotation;

	public ProbandListEntryTagInputModel(ProbandListEntryTagValueInVO tagValue) {
		super();
		setTagValue(tagValue);
	}

	@Override
	public void applyPresets() {
		if (tagValue != null && inputField != null) {
			tagValue.setBooleanValue(inputField.getBooleanPreset());
			tagValue.setDateValue(inputField.getDatePreset());
			tagValue.setTimeValue(inputField.getTimePreset());
			tagValue.setFloatValue(inputField.getFloatPreset());
			tagValue.setLongValue(inputField.getLongPreset());
			tagValue.setTimestampValue(inputField.getTimestampPreset());
			tagValue.setInkValues(null);
			tagValue.getSelectionValueIds().clear();
			tagValue.setTextValue(inputField.getTextPreset());
			Iterator<InputFieldSelectionSetValueOutVO> it = inputField.getSelectionSetValues().iterator();
			while (it.hasNext()) {
				InputFieldSelectionSetValueOutVO selectionValue = it.next();
				if (selectionValue.isPreset()) {
					tagValue.getSelectionValueIds().add(selectionValue.getId());
				}
			}
		}
	}

	@Override
	protected Object check() {
		return null;
	}

	@Override
	public boolean getBooleanValue() {
		return tagValue == null ? null : tagValue.getBooleanValue();
	}

	@Override
	public String getComment() {
		if (tag != null) {
			return tag.getComment();
		}
		return null;
	}

	@Override
	public Date getDateValue() {
		return tagValue == null ? null : tagValue.getDateValue();
	}

	@Override
	public Float getFloatValue() {
		return tagValue == null ? null : tagValue.getFloatValue();
	}

	@Override
	public String getInkValue() throws UnsupportedEncodingException {
		return tagValue == null ? null : CommonUtil.inkValueToString(tagValue.getInkValues());
	}

	@Override
	public String getJsOutputExpression() {
		if (tag != null) {
			return tag.getJsOutputExpression();
		}
		return "";
	}

	@Override
	public String getJsValueExpression() {
		if (tag != null) {
			return tag.getJsValueExpression();
		}
		return "";
	}

	@Override
	public String getJsVariableName() {
		if (tag != null) {
			return tag.getJsVariableName();
		}
		return "";
	}

	@Override
	public Long getLongValue() {
		return tagValue == null ? null : tagValue.getLongValue();
	}

	@Override
	public String getModifiedAnnotation() {
		return modifiedAnnotation;
	}

	@Override
	public String getName() {
		if (tagValue != null && tag != null && inputField != null) {
			if (tagValue.getId() != null) {
				return Messages.getMessage(MessageCodes.PROBAND_LIST_ENTRY_TAG_VALUE_INPUT_TITLE, Long.toString(tag.getPosition()), inputField.getName(), getValueString());
			} else {
				return Messages.getMessage(MessageCodes.NEW_PROBAND_LIST_ENTRY_TAG_VALUE_INPUT_TITLE, Long.toString(tag.getPosition()), inputField.getName());
			}
		}
		return null;
	}

	@Override
	public String getOutputId() {
		StringBuilder sb = new StringBuilder(JSValues.INPUT_FIELD_OUTPUT_ID_PREFIX.toString());
		sb.append(getProbandListTagId());
		return sb.toString();
	}

	public Long getPosition() {
		if (tag != null) {
			return tag.getPosition();
		}
		return null;
	}

	public Long getProbandListTagId() {
		if (tag != null) {
			return tag.getId();
		}
		return null;
	}

	@Override
	public String getReasonForChange() {
		return null;
	}

	@Override
	public Long getSelectionValueId() {
		if (tagValue != null) {
			Collection<Long> selectionValueIds = tagValue.getSelectionValueIds();
			if (selectionValueIds != null && selectionValueIds.size() > 0) {
				return selectionValueIds.iterator().next();
			}
		}
		return null;
	}

	@Override
	public List<String> getSelectionValueIds() {
		ArrayList<String> result;
		if (tagValue != null) {
			Collection<Long> selectionValueIds = tagValue.getSelectionValueIds();
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

	public ProbandListEntryTagValueInVO getTagValue() {
		return tagValue;
	}

	@Override
	public String getTextValue() {
		return tagValue == null ? null : tagValue.getTextValue();
	}

	@Override
	public Date getTimestampValue() {
		return tagValue == null ? null : tagValue.getTimestampValue();
	}

	@Override
	public Date getTimeValue() {
		return tagValue == null ? null : tagValue.getTimeValue();
	}

	@Override
	public String getUniqueName() {
		if (tag != null) {
			return tag.getUniqueName();
		}
		return null;
	}

	@Override
	public String getValueString() {
		return (new ProbandListEntryTagValueInVOStringAdapter(Settings.getInt(SettingCodes.INPUT_MODEL_VALUE_TEXT_CLIP_MAX_LENGTH, Bundle.SETTINGS,
				DefaultSettings.INPUT_MODEL_VALUE_TEXT_CLIP_MAX_LENGTH))).toString(inputField, tagValue);
	}

	@Override
	protected Long getVersion() {
		return tagValue != null ? tagValue.getVersion() : null;
	}

	@Override
	public String getWidgetVar() {
		StringBuilder sb = new StringBuilder(JSValues.INPUT_FIELD_WIDGET_VAR_PREFIX.toString());
		sb.append(getProbandListTagId());
		return sb.toString();
	}

	@Override
	public boolean isAuditTrail() {
		return false;
	}

	@Override
	public boolean isBooleanValue() {
		return tagValue == null ? null : tagValue.getBooleanValue();
	}

	@Override
	public boolean isCollapsed() {
		return !((tagValue != null && tagValue.getId() != null) || isError());
	}

	@Override
	public boolean isCreated() {
		return tagValue != null && tagValue.getId() != null;
	}

	@Override
	public boolean isDisabled() {
		if (tag != null) {
			return tag.getDisabled();
		}
		return false;
	}

	@Override
	public boolean isDummy() {
		return false;
	}

	@Override
	public boolean isJsVariable() {
		if (tag != null) {
			return !CommonUtil.isEmptyString(tag.getJsVariableName());
		}
		return false;
	}

	@Override
	public boolean isOptional() {
		if (tag != null) {
			return tag.getOptional();
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
		if (tagValue != null) {
			setErrorMessage(null);
			try {
				ProbandListEntryTagValuesOutVO values = WebUtil.getServiceLocator().getTrialService()
						.getProbandListEntryTagValue(WebUtil.getAuthentication(), tagValue.getListEntryId(), tagValue.getTagId());
				ProbandListEntryTagValueOutVO out = values.getPageValues().iterator().next();
				ProbandListEntryTagValueBean.copyProbandListEntryTagValueOutToIn(tagValue, out);
				setModifiedAnnotation(out);
				return values.getJsValues();
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
			ProbandListEntryTagValueJsonVO out = new ProbandListEntryTagValueJsonVO();
			CommonUtil.copyProbandListEntryTagValueInToJson(out, tagValue, tag);
			return out;
		}
		return null;
	}

	@Override
	public void setBooleanValue(boolean value) {
		if (tagValue != null) {
			tagValue.setBooleanValue(value);
		}
	}

	@Override
	public void setDateValue(Date value) {
		if (tagValue != null) {
			tagValue.setDateValue(value);
		}
	}

	@Override
	public void setFloatValue(Float value) {
		if (tagValue != null) {
			tagValue.setFloatValue(value);
		}
	}

	@Override
	public void setInkValue(String value) throws UnsupportedEncodingException {
		if (tagValue != null) {
			tagValue.setInkValues(CommonUtil.stringToInkValue(value));
		}
	}

	@Override
	public void setLongValue(Long value) {
		if (tagValue != null) {
			tagValue.setLongValue(value);
		}
	}

	public void setModifiedAnnotation(ProbandListEntryTagValueOutVO out) {
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
		if (tagValue != null) {
			ArrayList<Long> selectionValueIds;
			if (value != null) {
				selectionValueIds = new ArrayList<Long>(1);
				selectionValueIds.add(value);
			} else {
				selectionValueIds = new ArrayList<Long>();
			}
			tagValue.setSelectionValueIds(selectionValueIds);
		}
	}

	@Override
	public void setSelectionValueIds(List<String> value) {
		if (tagValue != null) {
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
			tagValue.setSelectionValueIds(selectionValueIds);
		}
	}

	public void setTagValue(ProbandListEntryTagValueInVO tagValue) {
		this.tagValue = tagValue;
		tag = null;
		setField(null);
		if (tagValue != null) {
			tag = WebUtil.getProbandListEntryTag(tagValue.getTagId());
			if (tag != null) {
				setField(tag.getField());
			}
		}
	}

	@Override
	public void setTextValue(String value) {
		if (tagValue != null) {
			tagValue.setTextValue(value);
		}
	}

	@Override
	public void setTimestampValue(Date value) {
		if (tagValue != null) {
			tagValue.setTimestampValue(value);
		}
	}

	@Override
	public void setTimeValue(Date value) {
		if (tagValue != null) {
			tagValue.setTimeValue(value);
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

	private Object update(boolean force) {
		if (tagValue != null) {
			setErrorMessage(null);
			HashSet<ProbandListEntryTagValueInVO> tagValues = new HashSet<ProbandListEntryTagValueInVO>(1);
			tagValues.add(tagValue);
			try {
				ProbandListEntryTagValuesOutVO values = WebUtil.getServiceLocator().getTrialService().setProbandListEntryTagValues(WebUtil.getAuthentication(), tagValues, force);
				ProbandListEntryTagValueOutVO out = values.getPageValues().iterator().next();
				ProbandListEntryTagValueBean.copyProbandListEntryTagValueOutToIn(tagValue, out);
				setModifiedAnnotation(out);
				return values.getJsValues();
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
		if (tag != null) {
			return tag.getTitle();
		}
		return null;
	}
}
