
package org.phoenixctms.ctsms.web.adapt;

import java.util.Collection;
import java.util.Date;

import org.phoenixctms.ctsms.vo.ECRFFieldValueInVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;

public class EcrfFieldValueInVOStringAdapter extends InputFieldValueStringAdapter<ECRFFieldValueInVO> {

	public EcrfFieldValueInVOStringAdapter(int textClipMaxLength) {
		super(textClipMaxLength);
	}

	@Override
	protected boolean getBooleanValue(ECRFFieldValueInVO value) {
		return value.getBooleanValue();
	}

	@Override
	protected Date getDateValue(ECRFFieldValueInVO value) {
		return value.getDateValue();
	}

	@Override
	protected Float getFloatValue(ECRFFieldValueInVO value) {
		return value.getFloatValue();
	}

	@Override
	protected Long getLongValue(ECRFFieldValueInVO value) {
		return value.getLongValue();
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(
			ECRFFieldValueInVO value) {
		return getSelectionSetValuesFromIds(value.getSelectionValueIds());
	}

	@Override
	protected String getTextValue(ECRFFieldValueInVO value) {
		return value.getTextValue();
	}

	@Override
	protected Date getTimestampValue(ECRFFieldValueInVO value) {
		return value.getTimestampValue();
	}

	@Override
	protected Date getTimeValue(ECRFFieldValueInVO value) {
		return value.getTimeValue();
	}
}
