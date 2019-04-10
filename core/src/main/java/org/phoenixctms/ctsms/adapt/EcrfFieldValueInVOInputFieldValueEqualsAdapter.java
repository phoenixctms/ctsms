package org.phoenixctms.ctsms.adapt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.phoenixctms.ctsms.domain.InputFieldValue;
import org.phoenixctms.ctsms.util.ServiceUtil;
import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;

public class EcrfFieldValueInVOInputFieldValueEqualsAdapter extends InputFieldValueEqualsAdapterBase<ECRFFieldValueInVO,InputFieldValue> {


	@Override
	protected Boolean getABoolean(ECRFFieldValueInVO a) {
		return a.getBooleanValue();
	}

	@Override
	protected Date getADate(ECRFFieldValueInVO a) {
		return a.getDateValue();
	}

	@Override
	protected Float getAFloat(ECRFFieldValueInVO a) {
		return a.getFloatValue();
	}

	@Override
	protected byte[] getAInk(ECRFFieldValueInVO a) {
		return a.getInkValues();
	}

	@Override
	protected Long getALong(ECRFFieldValueInVO a) {
		return a.getLongValue();
	}

	@Override
	protected ArrayList getASelectionSetSorted(ECRFFieldValueInVO a) {
		ArrayList selectionValueIds = (ArrayList) a.getSelectionValueIds();
		Collections.sort(selectionValueIds);
		return selectionValueIds;
	}



	@Override
	protected String getAText(ECRFFieldValueInVO a) {
		return a.getTextValue();
	}

	@Override
	protected Date getATime(ECRFFieldValueInVO a) {
		return a.getTimeValue();
	}

	@Override
	protected Date getATimestamp(ECRFFieldValueInVO a) {
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
