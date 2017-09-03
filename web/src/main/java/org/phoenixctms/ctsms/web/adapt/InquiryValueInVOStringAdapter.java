package org.phoenixctms.ctsms.web.adapt;

import java.util.Collection;
import java.util.Date;

import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;

public class InquiryValueInVOStringAdapter extends InputFieldValueStringAdapter<InquiryValueInVO> {

	public InquiryValueInVOStringAdapter(int textClipMaxLength) {
		super(textClipMaxLength);
	}

	@Override
	protected boolean getBooleanValue(InquiryValueInVO value) {
		return value.getBooleanValue();
	}

	@Override
	protected Date getDateValue(InquiryValueInVO value) {
		return value.getDateValue();
	}

	@Override
	protected Float getFloatValue(InquiryValueInVO value) {
		return value.getFloatValue();
	}

	@Override
	protected Long getLongValue(InquiryValueInVO value) {
		return value.getLongValue();
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(
			InquiryValueInVO value) {
		return getSelectionSetValuesFromIds(value.getSelectionValueIds());
	}

	@Override
	protected String getTextValue(InquiryValueInVO value) {
		return value.getTextValue();
	}

	@Override
	protected Date getTimestampValue(InquiryValueInVO value) {
		return value.getTimestampValue();
	}

	@Override
	protected Date getTimeValue(InquiryValueInVO value) {
		return value.getTimeValue();
	}
}
