
package org.phoenixctms.ctsms.web.adapt;

import java.util.Collection;
import java.util.Date;

import org.phoenixctms.ctsms.vo.ECRFFieldValueOutVO;
import org.phoenixctms.ctsms.vo.InputFieldOutVO;
import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;

public class EcrfFieldValueOutVOStringAdapter extends InputFieldValueStringAdapter<ECRFFieldValueOutVO> {

	public EcrfFieldValueOutVOStringAdapter() {
		super();
	}

	public EcrfFieldValueOutVOStringAdapter(int textClipMaxLength) {
		super(textClipMaxLength);
	}

	@Override
	protected boolean getBooleanValue(ECRFFieldValueOutVO value) {
		return value.getBooleanValue();
	}



	@Override
	protected Date getDateValue(ECRFFieldValueOutVO value) {
		return value.getDateValue();
	}

	@Override
	protected Float getFloatValue(ECRFFieldValueOutVO value) {
		return value.getFloatValue();
	}

	@Override
	protected InputFieldOutVO getInputField(ECRFFieldValueOutVO value) {
		return value != null ? value.getEcrfField().getField() : null;
	}

	@Override
	protected Long getLongValue(ECRFFieldValueOutVO value) {
		return value.getLongValue();
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(
			ECRFFieldValueOutVO value) {
		return value.getSelectionValues();
	}

	@Override
	protected String getTextValue(ECRFFieldValueOutVO value) {
		return value.getTextValue();
	}

	@Override
	protected Date getTimestampValue(ECRFFieldValueOutVO value) {
		return value.getTimestampValue();
	}

	@Override
	protected Date getTimeValue(ECRFFieldValueOutVO value) {
		return value.getTimeValue();
	}
}
