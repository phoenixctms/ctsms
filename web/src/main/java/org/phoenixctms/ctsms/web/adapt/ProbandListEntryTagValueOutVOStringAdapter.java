package org.phoenixctms.ctsms.web.adapt;

import java.util.Collection;
import java.util.Date;

import org.phoenixctms.ctsms.vo.InputFieldSelectionSetValueOutVO;
import org.phoenixctms.ctsms.vo.ProbandListEntryTagValueOutVO;

public class ProbandListEntryTagValueOutVOStringAdapter extends InputFieldValueStringAdapter<ProbandListEntryTagValueOutVO> {

	public ProbandListEntryTagValueOutVOStringAdapter(int textClipMaxLength) {
		super(textClipMaxLength);
	}

	@Override
	protected boolean getBooleanValue(ProbandListEntryTagValueOutVO value) {
		return value.getBooleanValue();
	}

	@Override
	protected Date getDateValue(ProbandListEntryTagValueOutVO value) {
		return value.getDateValue();
	}

	@Override
	protected Float getFloatValue(ProbandListEntryTagValueOutVO value) {
		return value.getFloatValue();
	}

	@Override
	protected Long getLongValue(ProbandListEntryTagValueOutVO value) {
		return value.getLongValue();
	}

	@Override
	protected Collection<InputFieldSelectionSetValueOutVO> getSelectionSetValues(
			ProbandListEntryTagValueOutVO value) {
		return value.getSelectionValues();
	}

	@Override
	protected String getTextValue(ProbandListEntryTagValueOutVO value) {
		return value.getTextValue();
	}

	@Override
	protected Date getTimestampValue(ProbandListEntryTagValueOutVO value) {
		return value.getTimestampValue();
	}

	@Override
	protected Date getTimeValue(ProbandListEntryTagValueOutVO value) {
		return value.getTimeValue();
	}
}
