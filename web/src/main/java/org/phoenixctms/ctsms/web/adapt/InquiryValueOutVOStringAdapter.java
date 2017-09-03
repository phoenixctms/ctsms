package org.phoenixctms.ctsms.web.adapt;

import java.util.Collection;
import java.util.Date;

import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InquiryValueOutVO;

public class InquiryValueOutVOStringAdapter extends InputFieldValueStringAdapter<InquiryValueOutVO> {

	public InquiryValueOutVOStringAdapter(int textClipMaxLength) {
		super(textClipMaxLength);
	}

	@Override
	protected boolean getBooleanValue(InquiryValueOutVO value) {
		return value.getBooleanValue();
	}

	@Override
	protected Date getDateValue(InquiryValueOutVO value) {
		return value.getDateValue();
	}

	@Override
	protected Float getFloatValue(InquiryValueOutVO value) {
		return value.getFloatValue();
	}

	@Override
	protected Long getLongValue(InquiryValueOutVO value) {
		return value.getLongValue();
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(
			InquiryValueOutVO value) {
		return value.getSelectionValues();
	}

	@Override
	protected String getTextValue(InquiryValueOutVO value) {
		return value.getTextValue();
	}

	@Override
	protected Date getTimestampValue(InquiryValueOutVO value) {
		return value.getTimestampValue();
	}

	@Override
	protected Date getTimeValue(InquiryValueOutVO value) {
		return value.getTimeValue();
	}
}
