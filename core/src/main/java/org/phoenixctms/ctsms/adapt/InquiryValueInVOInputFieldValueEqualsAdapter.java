package org.phoenixctms.ctsms.adapt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.phoenixctms.ctsms.domain.InputFieldValue;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.InquiryValueInVO;

public class InquiryValueInVOInputFieldValueEqualsAdapter extends InputFieldValueEqualsAdapterBase<InquiryValueInVO, InputFieldValue> {

	@Override
	protected Boolean getABoolean(InquiryValueInVO a) {
		return a.getBooleanValue();
	}

	@Override
	protected Date getADate(InquiryValueInVO a) {
		return a.getDateValue();
	}

	@Override
	protected Float getAFloat(InquiryValueInVO a) {
		return a.getFloatValue();
	}

	@Override
	protected byte[] getAInk(InquiryValueInVO a) {
		return a.getInkValues();
	}

	@Override
	protected Long getALong(InquiryValueInVO a) {
		return a.getLongValue();
	}

	@Override
	protected ArrayList getASelectionSetSorted(InquiryValueInVO a) {
		ArrayList selectionValueIds = (ArrayList) a.getSelectionValueIds();
		Collections.sort(selectionValueIds);
		return selectionValueIds;
	}

	@Override
	protected String getAText(InquiryValueInVO a) {
		return a.getTextValue();
	}

	@Override
	protected Date getATime(InquiryValueInVO a) {
		return a.getTimeValue();
	}

	@Override
	protected Date getATimestamp(InquiryValueInVO a) {
		return a.getTimestampValue();
	}

	@Override
	protected Boolean getBBoolean(InputFieldValue b) {
		return b.getBooleanValue();
	}

	@Override
	protected Date getBDate(InputFieldValue b) {
		return b.getDateValue();
	}

	@Override
	protected Float getBFloat(InputFieldValue b) {
		return b.getFloatValue();
	}

	@Override
	protected byte[] getBInk(InputFieldValue b) {
		return b.getInkValue();
	}

	@Override
	protected Long getBLong(InputFieldValue b) {
		return b.getLongValue();
	}

	@Override
	protected ArrayList getBSelectionSetSorted(InputFieldValue b) {
		return ServiceUtil.toInputFieldSelectionSetValueIdCollection(b.getSelectionValues());
	}

	@Override
	protected String getBText(InputFieldValue b) {
		return b.getStringValue();
	}

	@Override
	protected Date getBTime(InputFieldValue b) {
		return b.getTimeValue();
	}

	@Override
	protected Date getBTimestamp(InputFieldValue b) {
		return b.getTimestampValue();
	}
}
