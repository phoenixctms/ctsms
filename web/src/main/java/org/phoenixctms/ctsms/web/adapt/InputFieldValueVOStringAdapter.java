package org.phoenixctms.ctsms.web.adapt;

import java.util.Collection;
import java.util.Date;

import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldValueVO;

public class InputFieldValueVOStringAdapter extends InputFieldValueStringAdapter<InputFieldValueVO> {

	public InputFieldValueVOStringAdapter(int textClipMaxLength) {
		super(textClipMaxLength);
	}

	@Override
	protected boolean getBooleanValue(InputFieldValueVO value) {
		return value.getBooleanValue();
	}



	@Override
	protected Date getDateValue(InputFieldValueVO value) {
		return value.getDateValue();
	}

	@Override
	protected Float getFloatValue(InputFieldValueVO value) {
		return value.getFloatValue();
	}

	@Override
	protected Long getLongValue(InputFieldValueVO value) {
		return value.getLongValue();
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(
			InputFieldValueVO value) {
		return getSelectionSetValuesFromIds(value.getSelectionValueIds());
	}

	@Override
	protected String getTextValue(InputFieldValueVO value) {
		return value.getTextValue();
	}

	@Override
	protected Date getTimestampValue(InputFieldValueVO value) {
		return value.getTimestampValue();
	}

	@Override
	protected Date getTimeValue(InputFieldValueVO value) {
		return value.getTimeValue();
	}
}
